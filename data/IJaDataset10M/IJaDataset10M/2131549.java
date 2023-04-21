package uk.ac.ebi.intact.dataexchange.cvutils;

import uk.ac.ebi.intact.business.IntactException;
import uk.ac.ebi.intact.config.impl.SmallCvPrimer;
import uk.ac.ebi.intact.context.CvContext;
import uk.ac.ebi.intact.context.DataContext;
import uk.ac.ebi.intact.context.IntactContext;
import uk.ac.ebi.intact.dataexchange.cvutils.model.*;
import uk.ac.ebi.intact.model.*;
import uk.ac.ebi.intact.model.util.AnnotatedObjectUtils;
import uk.ac.ebi.intact.model.util.CvObjectBuilder;
import uk.ac.ebi.intact.model.util.CvObjectUtils;
import uk.ac.ebi.intact.persistence.dao.CvObjectDao;
import uk.ac.ebi.intact.persistence.dao.DaoFactory;
import uk.ac.ebi.intact.persistence.dao.XrefDao;
import uk.ac.ebi.intact.core.persister.PersisterHelper;
import java.io.*;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.net.URL;

/**
 * Class handling the update of CvObject.
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id: UpdateCVs.java 10626 2007-12-07 01:33:21Z baranda $
 * @since <pre>16-Oct-2005</pre>
 *
 * @deprecated use CvUpdater
 */
@SuppressWarnings("unchecked")
@Deprecated
public class UpdateCVs {

    private static CvObjectCache cvCache = new CvObjectCache();

    public static long searchLastIntactId(IntactOntology ontology) {
        long max = 0;
        Collection cvTerms = ontology.getCvTerms();
        for (Iterator iterator = cvTerms.iterator(); iterator.hasNext(); ) {
            CvTerm cvTerm = (CvTerm) iterator.next();
            final String prefix = "IA:";
            String id = cvTerm.getId();
            if (id.startsWith(prefix)) {
                String value = id.substring(prefix.length(), id.length());
                max = Math.max(max, Long.parseLong(value));
            }
        }
        return max;
    }

    /**
     * Global Update of the IntAct CVs, based upon an Ontology object. This is an iterative process that will update
     * each supported CVs independantely.
     *
     * @param ontology Controlled vocabularies upon which we do the update.
     *
     * @throws IntactException upon data access error
     */
    public static void update(IntactOntology ontology, PrintStream output, UpdateCVsReport report, UpdateCVsConfig config) throws IntactException {
        output.println("Config: " + config);
        Collection allTypes = ontology.getTypes();
        for (Iterator iterator = allTypes.iterator(); iterator.hasNext(); ) {
            Class aClass = (Class) iterator.next();
            update(ontology, aClass, output, report, config);
            IntactContext.getCurrentInstance().getDataContext().flushSession();
        }
    }

    private static String getShortClassName(Class clazz) {
        return clazz.getName().substring(clazz.getName().lastIndexOf('.') + 1, clazz.getName().length());
    }

    /**
     * Update a specific supported Controlled Vocabulary.
     * <p/>
     * Note: The Ontology contains a mapping Class -> CV.
     *
     * @param ontology
     * @param cvObjectClass
     *
     * @throws IntactException
     */
    public static void update(IntactOntology ontology, Class cvObjectClass, PrintStream output, UpdateCVsReport report, UpdateCVsConfig config) throws IntactException {
        output.println("Updating " + cvObjectClass.getName());
        Institution institution = IntactContext.getCurrentInstance().getInstitution();
        Collection<CvObject> intactTerms = getDaoFactory().getCvObjectDao(CvObject.class).getAll();
        output.println("\t " + intactTerms.size() + " term(s) found in IntAct.");
        output.println("\nIndexing of the IntAct terms by MI number");
        List<Mi2Cv> intactIndex = new ArrayList<Mi2Cv>(intactTerms.size());
        for (CvObject cvObject : intactTerms) {
            String psi = getPsiId(cvObject);
            if (psi != null) {
                intactIndex.add(new Mi2Cv(psi, cvObject));
                output.println("\tIndexed: " + psi + "\t" + cvObject.getShortLabel());
            } else {
                output.println("\tWARN: Could not index ( '" + cvObject.getShortLabel() + "' doesn't have an MI ID).");
                String ia = getIntactId(cvObject);
                if (ia == null) {
                    output.println("\tWARN: Could not find an IntAct id (IA:xxxx), add a new one.");
                    try {
                        ia = SequenceManager.getNextId();
                        CvObjectXref xref = new CvObjectXref(institution, intact, ia, null, null, identity);
                        cvObject.addXref(xref);
                        getDaoFactory().getXrefDao(CvObjectXref.class).persist(xref);
                        output.println("\tAdded: " + xref + " to ");
                        output.println("\t\t Created Xref( " + intact.getShortLabel() + ", " + identity.getShortLabel() + ", " + xref.getPrimaryId() + " ) (" + cvObject.getShortLabel() + ")");
                    } catch (Exception e) {
                        output.println("ERROR: An error occured while add the IntAct id, see exception below:");
                        e.printStackTrace(output);
                    }
                }
            }
        }
        output.println("\nupdate of the terms' content");
        Collection<CvTerm> oboTerms = ontology.getCvTerms(cvObjectClass);
        oboTerms.addAll(ontology.getObsoleteTerms());
        output.println("\t " + oboTerms.size() + " term(s) for " + cvObjectClass + " loaded from definition file.");
        for (CvTerm cvTerm : oboTerms) {
            CvObject cvObject = getCVWithMi(intactIndex, cvObjectClass, cvTerm.getId());
            if (cvObject == null && cvTerm.isObsolete()) {
                continue;
            }
            if (cvTerm.getShortName().equals("obsolete")) {
                continue;
            }
            output.println("----------------------------------------------------------------------------------");
            if (cvObject == null) {
                CvObjectDao<CvObject> cvObjectDao = getDaoFactory().getCvObjectDao(CvObject.class);
                String shortLabelName = AnnotatedObjectUtils.prepareShortLabel(cvTerm.getShortName());
                if (!shortLabelName.equals(cvTerm.getShortName())) {
                    output.println("WARN: Cv Term name trimmed: " + cvTerm.getShortName() + " - to " + shortLabelName);
                }
                cvObject = IntactContext.getCurrentInstance().getCvContext().getByLabel(cvObjectClass, shortLabelName);
                if (cvObject == null) {
                    try {
                        Constructor constructor = cvObjectClass.getConstructor(new Class[] { Institution.class, String.class });
                        cvObject = (CvObject) constructor.newInstance(new Object[] { institution, shortLabelName });
                        cvObject.setFullName(cvTerm.getFullName());
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                    cvObjectDao.persist(cvObject);
                    String className = getShortClassName(cvObject.getClass());
                    output.println("\t Creating " + className + "( " + shortLabelName + " )");
                    report.addCreatedTerm(cvObject);
                    intactIndex.add(new Mi2Cv(cvTerm.getId(), cvObject));
                }
            } else {
                String className = getShortClassName(cvObject.getClass());
                output.println("\t Updating existing " + className + "( " + cvObject.getShortLabel() + " )");
                report.addUpdatedTerm(cvObject);
            }
            updateTerm(cvObject, cvTerm, output, report, config);
        }
        output.println("+++++++++++++++++++++++++++++++++++++++++");
        output.println("\t Updating Vocabulary hierarchy...");
        boolean stopUpdate = false;
        for (Iterator iterator = oboTerms.iterator(); iterator.hasNext() && !stopUpdate; ) {
            CvTerm cvTerm = (CvTerm) iterator.next();
            CvObject cvObject = getCVWithMi(intactIndex, cvObjectClass, cvTerm.getId());
            if (cvObject == null) {
                output.println("ERROR: Could not find " + cvTerm.getId() + " - " + cvTerm.getShortName() + ". skipping term.");
                continue;
            }
            CvDagObject dagObject = null;
            if (cvObject instanceof CvDagObject) {
                dagObject = (CvDagObject) cvObject;
            } else {
                output.println("WARNING: " + cvObject.getClass().getName() + " is not a DAG, skip hierarchy update.");
                stopUpdate = true;
                continue;
            }
            Collection allChildren = new ArrayList(dagObject.getChildren());
            for (Iterator iterator2 = cvTerm.getChildren().iterator(); iterator2.hasNext(); ) {
                CvTerm child = (CvTerm) iterator2.next();
                CvDagObject intactChild = (CvDagObject) getCVWithMi(intactIndex, cvObjectClass, child.getId());
                if (intactChild == null) {
                    output.println("ERROR: Could not find Child term of " + cvTerm.getShortName() + "(" + cvTerm.getId() + ") in the index (" + child.getId() + ").");
                    continue;
                }
                if (!dagObject.getChildren().contains(intactChild)) {
                    dagObject.addChild(intactChild);
                    output.println("\t\t Adding Relationship[(" + dagObject.getAc() + ") " + dagObject.getShortLabel() + " ---child---> (" + intactChild.getAc() + ") " + intactChild.getShortLabel() + "]");
                    getDaoFactory().getCvObjectDao(CvDagObject.class).persist(dagObject);
                    getDaoFactory().getCvObjectDao(CvDagObject.class).persist(intactChild);
                }
                allChildren.remove(intactChild);
            }
            for (Iterator iterator2 = allChildren.iterator(); iterator2.hasNext(); ) {
                CvDagObject child = (CvDagObject) iterator2.next();
                try {
                    Connection connection = getDaoFactory().connection();
                    Statement statement = connection.createStatement();
                    statement.execute("DELETE FROM ia_cv2cv " + "WHERE parent_ac = '" + dagObject.getAc() + "' AND " + "      child_ac = '" + child.getAc() + "'");
                    statement.close();
                    output.println("\t\t Removing Relationship[(" + dagObject.getAc() + ") " + dagObject.getShortLabel() + " ---child---> (" + child.getAc() + ") " + child.getShortLabel() + "]");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        Collection roots = ontology.getRoots(cvObjectClass);
        CvTopic hidden = IntactContext.getCurrentInstance().getCvContext().getByLabel(CvTopic.class, CvTopic.HIDDEN);
        if (hidden == null) {
            output.println("WARN: The CvTopic(" + CvTopic.HIDDEN + ") could not be found or created in IntAct. " + "Skip flagging of the root terms.");
        } else {
            for (Iterator iterator = roots.iterator(); iterator.hasNext(); ) {
                CvTerm rootTerm = (CvTerm) iterator.next();
                CvObject root = getCVWithMi(intactIndex, cvObjectClass, rootTerm.getId());
                if (root == null) {
                    output.println("ERROR: the term " + rootTerm.getId() + " should have been found in IntAct.");
                } else {
                    addUniqueAnnotation(root, hidden, "Root term deprecated during manual curation as too unspecific.", output);
                }
            }
        }
    }

    /**
     * Go through all obsolete OBO terms and list all terms that weren't in IntAct.
     * <p/>
     * These terms are not going to be included in IntAct as we cannot guess their type.
     *
     * @param ontology the ontology from which we will get the list of obsolete terms
     *
     * @throws IntactException if something goes wrong
     */
    public static Collection<CvTerm> listOrphanObsoleteTerms(IntactOntology ontology, PrintStream output, UpdateCVsReport report) throws IntactException {
        output.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        output.println("Updating Obsolete Terms");
        List<CvTerm> missingTerms = new ArrayList<CvTerm>();
        Collection<CvTerm> obsoleteTerms = ontology.getObsoleteTerms();
        for (Iterator iterator = obsoleteTerms.iterator(); iterator.hasNext(); ) {
            CvTerm cvTerm = (CvTerm) iterator.next();
            String id = cvTerm.getId();
            CvObject cvObject = getDaoFactory().getCvObjectDao(CvObject.class).getByXref(id);
            if (cvObject == null) {
                missingTerms.add(cvTerm);
            }
        }
        if (!missingTerms.isEmpty()) {
            output.println("WARN: ---------------------------------------------------------------------------------------");
            output.println("WARN: WARNING - The list of terms below could not be added to your IntAct node");
            output.println("WARN:           Reason:   These terms are obsolete in PSI-MI and the ontology doesn't keep ");
            output.println("WARN:                     track of the root of obsolete terms.");
            output.println("WARN:           Solution: if you really want to add these terms into IntAct, you will have to ");
            output.println("WARN:                     do it manually and make sure that they get their MI:xxxx.");
            output.println("WARN: ---------------------------------------------------------------------------------------");
            for (Iterator iterator = missingTerms.iterator(); iterator.hasNext(); ) {
                CvTerm cvTerm = (CvTerm) iterator.next();
                output.println(cvTerm.getId() + " - " + cvTerm.getShortName());
            }
        }
        report.getObsoleteTerms().addAll(missingTerms);
        return obsoleteTerms;
    }

    /**
     * Add an annotation in a CvObject if it is not in there yet. The CvTopic and the text of the annotation are given
     * as parameters so the methods is flexible.
     *
     * @param cvObject the CvObject in which we want to add the annotation
     * @param topic    the topic of the annotation. must not be null.
     * @param text     the text of the annotation. Can be null.
     *
     * @throws IntactException if something goes wrong during the update.
     */
    private static void addUniqueAnnotation(final CvObject cvObject, final CvTopic topic, final String text, PrintStream output) throws IntactException {
        Institution institution = IntactContext.getCurrentInstance().getInstitution();
        if (topic == null) {
            output.println("ERROR: You must give a non null topic when updating term " + cvObject.getShortLabel());
        } else {
            Collection annotationByTopic = new ArrayList();
            for (Iterator iterator = cvObject.getAnnotations().iterator(); iterator.hasNext(); ) {
                Annotation annot = (Annotation) iterator.next();
                if (topic.equals(annot.getCvTopic())) {
                    annotationByTopic.add(annot);
                }
            }
            if (annotationByTopic.isEmpty()) {
                Annotation annotation = new Annotation(institution, topic);
                annotation.setAnnotationText(text);
                getDaoFactory().getAnnotationDao().persist(annotation);
                cvObject.addAnnotation(annotation);
                getDaoFactory().getCvObjectDao(CvObject.class).update(cvObject);
                output.println("Added Annotation " + topic.getShortLabel() + " to '" + cvObject.getShortLabel() + "'.");
            } else {
                Annotation newAnnotation = new Annotation(institution, topic);
                newAnnotation.setAnnotationText(text);
                if (annotationByTopic.contains(newAnnotation)) {
                    annotationByTopic.remove(newAnnotation);
                } else {
                    Iterator iterator = annotationByTopic.iterator();
                    Annotation annotation = (Annotation) iterator.next();
                    String oldText = annotation.getAnnotationText();
                    annotation.setAnnotationText(text);
                    getDaoFactory().getAnnotationDao().update(annotation);
                    output.println("Updated " + cvObject.getShortLabel() + ", Annotation(" + topic.getShortLabel() + ")\n" + "        updated text from '" + oldText + "' to '" + text + "'.");
                    iterator.remove();
                }
            }
            for (Iterator iterator = annotationByTopic.iterator(); iterator.hasNext(); ) {
                Annotation annotation = (Annotation) iterator.next();
                String _text = annotation.getAnnotationText();
                cvObject.removeAnnotation(annotation);
                getDaoFactory().getCvObjectDao(CvObject.class).update(cvObject);
                getDaoFactory().getAnnotationDao().delete(annotation);
                output.println("Deleted redondant Annotation(" + topic.getShortLabel() + ", '" + _text + "'), we want it unique and there's already one.");
            }
        }
    }

    /**
     * Browse the CvObject's Xref and find (if possible) the primary ID of the first Xref( CvDatabase( psi-mi ) ).
     *
     * @param cvObject the Object we are introspecting.
     *
     * @return a PSI ID or null is none is found.
     */
    private static String getPsiId(CvObject cvObject) {
        final DaoFactory daoFactory = IntactContext.getCurrentInstance().getDataContext().getDaoFactory();
        if (psi == null) {
            psi = daoFactory.getCvObjectDao(CvDatabase.class).getByPsiMiRef(CvDatabase.PSI_MI_MI_REF);
        }
        if (identity == null) {
            identity = daoFactory.getCvObjectDao(CvXrefQualifier.class).getByPsiMiRef(CvXrefQualifier.IDENTITY_MI_REF);
        }
        for (Iterator iterator = cvObject.getXrefs().iterator(); iterator.hasNext(); ) {
            Xref xref = (Xref) iterator.next();
            if (psi.equals(xref.getCvDatabase()) && identity.equals(xref.getCvXrefQualifier())) {
                return xref.getPrimaryId();
            }
        }
        return null;
    }

    /**
     * Browse the CvObject's Xref and find (if possible) the primary ID of the first Xref( CvDatabase( intact ) ).
     *
     * @param cvObject the Object we are introspecting.
     *
     * @return a PSI ID or null is none is found.
     */
    private static String getIntactId(CvObject cvObject) {
        if (intact == null) {
            final DaoFactory daoFactory = IntactContext.getCurrentInstance().getDataContext().getDaoFactory();
            intact = daoFactory.getCvObjectDao(CvDatabase.class).getByPsiMiRef(CvDatabase.INTACT_MI_REF);
        }
        for (Iterator iterator = cvObject.getXrefs().iterator(); iterator.hasNext(); ) {
            Xref xref = (Xref) iterator.next();
            if (intact.equals(xref.getCvDatabase()) && identity.equals(xref.getCvXrefQualifier())) {
                return xref.getPrimaryId();
            }
        }
        return null;
    }

    /**
     * Get the requested CvObject from the Database, create it if it doesn't exist. It is searched by shortlabel. If not
     * found, a CvObject is create using the information given (shortlabel)
     *
     * @param clazz      the CvObject concrete type
     * @param shortlabel shortlabel of the term
     *
     * @return a CvObject persistent in the backend.
     *
     * @throws IntactException          is an error occur while writting on the database.
     * @throws IllegalArgumentException if the class given is not a concrete type of CvObject (eg. CvDatabase)
     */
    public static CvObject getCvObject(Class clazz, String shortlabel, PrintStream output, UpdateCVsReport report) throws IntactException {
        return getCvObject(clazz, shortlabel, null, output, report);
    }

    public static CvObject getCvObject(Class clazz, String shortlabel, String mi, PrintStream output, UpdateCVsReport report) throws IntactException {
        return getCvObject(clazz, shortlabel, mi, shortlabel, output, report);
    }

    /**
     * Get the requested CvObject from the Database, create it if it doesn't exist. It is first searched by Xref(psi-mi)
     * if an ID is given, then by shortlabel. If not found, a CvObject is create using the information given
     * (shortlabel, then potentially a PSI ID)
     *
     * @param clazz      the CvObject concrete type
     * @param shortlabel shortlabel of the term
     * @param mi         MI id of the term
     *
     * @return a CvObject persistent in the backend.
     *
     * @throws IntactException          is an error occur while writting on the database.
     * @throws IllegalArgumentException if the class given is not a concrete type of CvObject (eg. CvDatabase)
     */
    public static CvObject getCvObject(Class clazz, String shortlabel, String mi, String defaultFullName, PrintStream output, UpdateCVsReport report) throws IntactException {
        if (!CvObject.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("The given class (" + getShortClassName(clazz) + ") must be a sub type of CvObject");
        }
        CvObject cv = cvCache.get(clazz, mi, shortlabel);
        if (cv != null) {
            return cv;
        }
        CvObjectDao<?> dao = IntactContext.getCurrentInstance().getDataContext().getDaoFactory().getCvObjectDao(clazz);
        if (mi != null) {
            output.println("Looking up term by mi: " + mi);
            cv = dao.getByPsiMiRef(mi);
        }
        if (cv == null) {
            output.println("Not found. Now, looking up term by short label: " + shortlabel);
            cv = dao.getByShortLabel(clazz, shortlabel);
        }
        if (cv == null) {
            output.println("Not found. Then, create it: " + shortlabel + " (" + mi + ")");
            try {
                if (mi == null) {
                    output.println("CvObject without MI: " + shortlabel);
                    throw new NullPointerException();
                }
                cv = CvObjectUtils.createCvObject(IntactContext.getCurrentInstance().getInstitution(), clazz, mi, shortlabel);
                cv.setFullName(defaultFullName);
                output.println("Created missing CV Term: " + getShortClassName(clazz) + "( " + cv.getShortLabel() + " - " + cv.getFullName() + " ).");
                report.addCreatedTerm(cv);
                cvCache.put(clazz, mi, shortlabel, cv);
                PersisterHelper.saveOrUpdate(cv);
            } catch (Exception e) {
                throw new IntactException("Error while creating " + getShortClassName(clazz) + "(" + shortlabel + ", " + mi + ").", e);
            }
        }
        return cv;
    }

    /**
     * Select the list of topics that are unique in a CvObject.
     *
     * @return a non null Set of CvTopics. may be empty.
     *
     * @throws IntactException
     */
    private static Set loadUniqueCvTopics(PrintStream output, UpdateCVsReport report) throws IntactException {
        Set uniqueTopic = new HashSet();
        uniqueTopic.add(getCvObject(CvTopic.class, CvTopic.DEFINITION, output, report));
        uniqueTopic.add(getCvObject(CvTopic.class, CvTopic.OBSOLETE, CvTopic.OBSOLETE_MI_REF, output, report));
        uniqueTopic.add(getCvObject(CvTopic.class, CvTopic.XREF_VALIDATION_REGEXP, CvTopic.XREF_VALIDATION_REGEXP_MI_REF, output, report));
        return uniqueTopic;
    }

    /**
     * Select the list of qualifiers that are unique in a CvObject.
     *
     * @return a non null Set of CvXrefQualifiers. may be empty.
     *
     * @throws IntactException
     */
    private static Set loadUniqueCvXrefQualifiers(PrintStream output, UpdateCVsReport report) throws IntactException {
        Set uniqueQualifier = new HashSet();
        uniqueQualifier.add(getCvObject(CvXrefQualifier.class, CvXrefQualifier.PRIMARY_REFERENCE, CvXrefQualifier.PRIMARY_REFERENCE_MI_REF, output, report));
        uniqueQualifier.add(getCvObject(CvXrefQualifier.class, CvXrefQualifier.IDENTITY, CvXrefQualifier.IDENTITY_MI_REF, output, report));
        return uniqueQualifier;
    }

    /**
     * Selects all annotations from the given collection having the given topic.
     *
     * @param annotations a collection of annotation.
     * @param topic       the filter
     *
     * @return a non null collection of annotation. may be empty.
     */
    private static Collection select(Collection annotations, CvTopic topic) {
        if (annotations == null || annotations.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        Collection selectedAnnotations = new ArrayList(annotations.size());
        for (Iterator iterator1 = annotations.iterator(); iterator1.hasNext(); ) {
            Annotation _annot = (Annotation) iterator1.next();
            if (topic.equals(_annot.getCvTopic())) {
                selectedAnnotations.add(_annot);
            }
        }
        return selectedAnnotations;
    }

    /**
     * Selects all Xrefs from the given collection having the given database and qualifier.
     *
     * @param xrefs     a collection of annotation.
     * @param database  the database filter
     * @param qualifier the qualifier filter
     *
     * @return a non null collection of xrefs. may be empty.
     */
    private static Collection select(Collection xrefs, CvDatabase database, CvXrefQualifier qualifier) {
        if (xrefs == null || xrefs.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        Collection selectedXrefs = new ArrayList(xrefs.size());
        for (Iterator iterator1 = xrefs.iterator(); iterator1.hasNext(); ) {
            Xref xref = (Xref) iterator1.next();
            if (database.equals(xref.getCvDatabase())) {
                if (qualifier.equals(xref.getCvXrefQualifier())) {
                    selectedXrefs.add(xref);
                }
            }
        }
        return selectedXrefs;
    }

    /**
     * Selects all Xrefs from the given collection having the given database and primaryId.
     *
     * @param xrefs     a collection of annotation.
     * @param database  the database filter
     * @param primaryId the primaryId filter
     *
     * @return a non null collection of xrefs. may be empty.
     */
    private static Collection select(Collection xrefs, CvDatabase database, String primaryId) {
        if (xrefs == null || xrefs.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        Collection selectedXrefs = new ArrayList(xrefs.size());
        for (Iterator iterator1 = xrefs.iterator(); iterator1.hasNext(); ) {
            Xref xref = (Xref) iterator1.next();
            if (database.equals(xref.getCvDatabase())) {
                if (primaryId.equals(xref.getPrimaryId())) {
                    selectedXrefs.add(xref);
                }
            }
        }
        return selectedXrefs;
    }

    /**
     * Update an IntAct CV term based on the definition read externally, and contained in a CvTerm.
     *
     * @param cvObject the IntAct CV to update.
     * @param cvTerm   the new definition of that CV.
     *
     * @throws IntactException if error occur.
     */
    private static void updateTerm(CvObject cvObject, CvTerm cvTerm, PrintStream output, UpdateCVsReport report, UpdateCVsConfig config) throws IntactException {
        String mi = getPsiId(cvObject);
        String ia = getIntactId(cvObject);
        boolean hasIntactTermGotPsiIdentifier = (mi != null);
        boolean hasIntactTermGotIntactIdentifier = (ia != null);
        String id = cvTerm.getId();
        boolean hasPsiIdentifier = id.startsWith("MI:");
        boolean hasIntactIdentifier = id.startsWith("IA:");
        String trimmedShortName = AnnotatedObjectUtils.prepareShortLabel(cvTerm.getShortName());
        boolean needsUpdate = false;
        if (!cvObject.getShortLabel().equals(trimmedShortName)) {
            cvObject.setShortLabel(trimmedShortName);
            needsUpdate = true;
            output.println("\t\t Updated shortlabel (" + cvTerm.getShortName() + ")");
        }
        if (cvObject.getFullName() != null) {
            if (!cvObject.getFullName().equals(cvTerm.getFullName())) {
                cvObject.setFullName(cvTerm.getFullName());
                output.println("\t\t Updated fullname from '" + cvObject.getFullName() + "' to '" + cvTerm.getFullName() + "'. (" + trimmedShortName + ")");
                needsUpdate = true;
            }
        } else {
            if (cvTerm.getFullName() != null) {
                cvObject.setFullName(cvTerm.getFullName());
                needsUpdate = true;
                output.println("\t\t Updated fullname, from null to '" + cvTerm.getFullName() + "' (" + trimmedShortName + ")");
            }
        }
        if (needsUpdate) {
            output.println("\t Updating CV: " + trimmedShortName + " (" + id + ")");
        }
        Institution institution = IntactContext.getCurrentInstance().getInstitution();
        if (!hasIntactTermGotPsiIdentifier && hasPsiIdentifier) {
            CvDatabase psi = (CvDatabase) getCvObject(CvDatabase.class, CvDatabase.PSI_MI, CvDatabase.PSI_MI_MI_REF, output, report);
            CvXrefQualifier identity = (CvXrefQualifier) getCvObject(CvXrefQualifier.class, CvXrefQualifier.IDENTITY, CvXrefQualifier.IDENTITY_MI_REF, output, report);
            CvObjectXref xref = new CvObjectXref(institution, psi, id, null, null, identity);
            cvObject.addXref(xref);
            getDaoFactory().getXrefDao().persist(xref);
            output.println("\t\t Added PSI Xref (" + id + ")");
        }
        if (!hasIntactTermGotIntactIdentifier && hasIntactIdentifier) {
            CvDatabase intact = (CvDatabase) getCvObject(CvDatabase.class, CvDatabase.INTACT, CvDatabase.INTACT_MI_REF, output, report);
            CvXrefQualifier identity = (CvXrefQualifier) getCvObject(CvXrefQualifier.class, CvXrefQualifier.IDENTITY, CvXrefQualifier.IDENTITY_MI_REF, output, report);
            Collection<CvObject> conflictingTerms = getDaoFactory().getCvObjectDao(CvObject.class).getByXrefLike(intact, identity, id);
            for (CvObject conflict : conflictingTerms) {
                output.println("WARN: !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                output.println("WARN: Found a CV term using the same IntAct Xref: " + id + " replacing id...");
                Collection xrefs = select(conflict.getXrefs(), intact, identity);
                String newId = SequenceManager.getNextId();
                if (!xrefs.isEmpty()) {
                    Iterator it = xrefs.iterator();
                    CvObjectXref xref = (CvObjectXref) it.next();
                    xref.setPrimaryId(newId);
                    getDaoFactory().getXrefDao().update(xref);
                    output.println("Updated Xref (" + id + ") updated to " + newId + " on term '" + conflict.getShortLabel() + "'.");
                    while (it.hasNext()) {
                        xref = (CvObjectXref) it.next();
                        conflict.removeXref(xref);
                        getDaoFactory().getXrefDao().delete(xref);
                        getDaoFactory().getCvObjectDao(CvObject.class).update(conflict);
                        output.println("Deleted additional Xref:" + xref);
                    }
                } else {
                    output.println("ERROR: no matching Xref found in that term.");
                }
            }
            CvObjectXref xref = new CvObjectXref(institution, intact, id, null, null, identity);
            cvObject.addXref(xref);
            getDaoFactory().getXrefDao().persist(xref);
            output.println("\t\t Added IntAct Xref (" + id + ")");
        }
        if (needsUpdate) {
            getDaoFactory().getCvObjectDao(CvObject.class).update(cvObject);
        }
        updateAnnotations(cvObject, cvTerm, output, report, config);
        updateXrefs(cvObject, cvTerm, output, report);
        updateAliases(cvObject, cvTerm, output, report);
    }

    private static DaoFactory getDaoFactory() {
        return IntactContext.getCurrentInstance().getDataContext().getDaoFactory();
    }

    /**
     * Update Annotation of the given CvObject using the cvTerm definition read from the psi ontology file.
     *
     * @param cvObject the cvObject we want to update.
     * @param cvTerm   the cvTerm containing the data to update from.
     *
     * @throws IntactException if an error occurs during the update.
     */
    private static void updateAnnotations(CvObject cvObject, CvTerm cvTerm, PrintStream output, UpdateCVsReport report, UpdateCVsConfig config) throws IntactException {
        List<CvTermAnnotation> annotations = new ArrayList<CvTermAnnotation>(cvTerm.getAnnotations());
        if (!cvTerm.getId().equals(CvTopic.OBSOLETE_MI_REF)) {
            output.println("\t\t CvTerm '" + cvTerm.getShortName() + "' is obsolete? " + (cvTerm.isObsolete() || cvTerm.getObsoleteMessage() != null));
            if (cvTerm.isObsolete() || cvTerm.getObsoleteMessage() != null) {
                output.println("\t\t Marking as obsolete, adding the annotation 'obsolete' to term: " + cvTerm.getShortName());
                annotations.add(new CvTermAnnotation(CvTopic.OBSOLETE, cvTerm.getObsoleteMessage()));
                report.getObsoleteTerms().add(cvTerm);
            }
        }
        String def = cvTerm.getDefinition();
        if (def != null && def.length() > 0) {
            CvTermAnnotation annot = new CvTermAnnotation(CvTopic.DEFINITION, cvTerm.getDefinition());
            annotations.add(annot);
        }
        Set uniqueCvTopics = loadUniqueCvTopics(output, report);
        Institution institution = IntactContext.getCurrentInstance().getInstitution();
        for (CvTermAnnotation annotation : annotations) {
            CvTopic topic = (CvTopic) getCvObject(CvTopic.class, annotation.getTopic(), output, report);
            if (topic != null) {
                DaoFactory daoFactory = getDaoFactory();
                if (uniqueCvTopics.contains(topic)) {
                    Collection annotationsByTopic = select(cvObject.getAnnotations(), topic);
                    output.println("\t\t Select from Annotation with topic '" + topic.getShortLabel() + "' returned " + annotationsByTopic.size() + " hit(s).");
                    if (annotationsByTopic.isEmpty()) {
                        Annotation annot = new Annotation(institution, topic, annotation.getAnnotation());
                        daoFactory.getAnnotationDao().persist(annot);
                        if (!config.isIgnoreObsoletionOfObsolete()) {
                            cvObject.addAnnotation(annot);
                            daoFactory.getCvObjectDao(CvObject.class).update(cvObject);
                            output.println("\t\t Created unique Annotation( " + topic.getShortLabel() + ", '" + annot.getAnnotationText() + "' ) for CVObject: " + cvObject.getShortLabel() + "(" + cvObject.getAc() + ")");
                        } else {
                            boolean obsoleteObjectAndTopic = isObsoleteObjectAndObsoleteTopic(cvObject, topic);
                            output.println("\t\t 'Obsolete' object and 'obsolete' topic - " + cvObject.getShortLabel() + "," + topic.getShortLabel() + "? " + obsoleteObjectAndTopic);
                            if (!obsoleteObjectAndTopic) {
                                cvObject.addAnnotation(annot);
                                daoFactory.getCvObjectDao(CvObject.class).update(cvObject);
                                output.println("\t\t Created unique Annotation( " + topic.getShortLabel() + ", '" + annot.getAnnotationText() + "' ) for CVObject: " + cvObject.getShortLabel() + "(" + cvObject.getAc() + ")");
                            } else {
                                output.println("\t\t IGNORING unique Annotation( " + topic.getShortLabel() + ", '" + annot.getAnnotationText() + "' ) for CVObject: " + cvObject.getShortLabel() + "(" + cvObject.getAc() + ")");
                            }
                        }
                    } else {
                        Iterator i = annotationsByTopic.iterator();
                        Annotation annot = (Annotation) i.next();
                        String text = (annot.getAnnotationText() == null ? "" : annot.getAnnotationText());
                        String newtext = (annotation.getAnnotation() == null ? "" : annotation.getAnnotation());
                        if (!text.equals(newtext)) {
                            annot.setAnnotationText(annotation.getAnnotation());
                            daoFactory.getAnnotationDao().update(annot);
                            output.println("\t\t Updated Annotation( " + topic.getShortLabel() + ", '" + annot.getAnnotationText() + "' )");
                        }
                        while (i.hasNext()) {
                            annot = (Annotation) i.next();
                            output.println("\t\t Removed Annotation( " + topic.getShortLabel() + ", '" + annot.getAnnotationText() + "' )");
                            cvObject.removeAnnotation(annot);
                            daoFactory.getCvObjectDao(CvObject.class).update(cvObject);
                            daoFactory.getAnnotationDao().delete(annot);
                        }
                    }
                } else {
                    Annotation annot = new Annotation(institution, topic, annotation.getAnnotation());
                    if (!cvObject.getAnnotations().contains(annot)) {
                        daoFactory.getAnnotationDao().persist(annot);
                        cvObject.addAnnotation(annot);
                        daoFactory.getCvObjectDao(CvObject.class).update(cvObject);
                        output.println("\t\t Created Annotation( " + topic.getShortLabel() + ", '" + annot.getAnnotationText() + "' ). <topic not unique>");
                    }
                }
            } else {
                output.println("ERROR: Could not find or create CvTopic( " + annotation.getTopic() + " ) in IntAct. Skip annotation.");
            }
        }
    }

    private static boolean isObsoleteObjectAndObsoleteTopic(CvObject object, CvTopic topic) {
        CvObject obsolete = IntactContext.getCurrentInstance().getCvContext().getByMiRef(CvTopic.class, CvTopic.OBSOLETE_MI_REF);
        if (object.getAc().equals(obsolete.getAc()) && topic.getAc().equals(obsolete.getAc())) {
            return true;
        }
        return false;
    }

    /**
     * Update Xrefs of the given CvObject using the cvTerm definition read from the psi ontology file.
     *
     * @param cvObject the cvObject we want to update.
     * @param cvTerm   the cvTerm containing the data to update from.
     *
     * @throws IntactException if an error occurs during the update.
     */
    private static void updateXrefs(CvObject cvObject, CvTerm cvTerm, PrintStream output, UpdateCVsReport report) throws IntactException {
        Map dbMapping = new HashMap();
        dbMapping.put("PMID", CvDatabase.PUBMED);
        dbMapping.put("RESID", CvDatabase.RESID);
        dbMapping.put("SO", CvDatabase.SO);
        dbMapping.put("GO", CvDatabase.GO);
        XrefDao xrefDao = getDaoFactory().getXrefDao();
        Institution institution = IntactContext.getCurrentInstance().getInstitution();
        Set uniqueQualifiers = loadUniqueCvXrefQualifiers(output, report);
        for (Iterator iterator = cvTerm.getXrefs().iterator(); iterator.hasNext(); ) {
            CvTermXref cvTermXref = (CvTermXref) iterator.next();
            CvDatabase database = (CvDatabase) getCvObject(CvDatabase.class, cvTermXref.getDatabase(), output, report);
            CvXrefQualifier qualifier = (CvXrefQualifier) getCvObject(CvXrefQualifier.class, cvTermXref.getQualifier(), output, report);
            CvObjectXref newXref = new CvObjectXref(institution, database, cvTermXref.getId(), null, null, qualifier);
            if (cvObject.getXrefs().contains(newXref)) {
                continue;
            }
            boolean updated = false;
            Collection xrefs = select(cvObject.getXrefs(), database, cvTermXref.getId());
            if (!xrefs.isEmpty()) {
                output.println("\t\tFound " + xrefs.size() + " Xref" + (xrefs.size() > 1 ? "s" : "") + " having database(" + database.getShortLabel() + ") and id(" + cvTermXref.getId() + ").");
                Iterator itx = xrefs.iterator();
                CvObjectXref xref = (CvObjectXref) itx.next();
                CvXrefQualifier old = xref.getCvXrefQualifier();
                xref.setCvXrefQualifier(qualifier);
                xrefDao.update(xref);
                updated = true;
                output.println("\t\tUpdated (" + xref.getAc() + ") Xref's qualifier from " + old.getShortLabel() + " to " + qualifier.getShortLabel());
                if (itx.hasNext()) {
                    output.println("\t\t Deleting Xrefs having the same Database / ID as only one should be there:");
                }
                while (itx.hasNext()) {
                    xref = (CvObjectXref) itx.next();
                    cvObject.removeXref(xref);
                    xrefDao.delete(xref);
                    output.println("\t\t\t Xref( " + database.getShortLabel() + ", " + xref.getCvXrefQualifier().getShortLabel() + ", " + xref.getPrimaryId() + " ) (" + cvTerm.getShortName() + ")");
                }
            } else {
                if (uniqueQualifiers.contains(qualifier)) {
                    Collection selectedXrefs = select(cvObject.getXrefs(), database, qualifier);
                    if (selectedXrefs.isEmpty()) {
                        cvObject.addXref(newXref);
                        xrefDao.persist(newXref);
                        output.println("\t\t Created Xref( " + database.getShortLabel() + ", " + qualifier.getShortLabel() + ", " + newXref.getPrimaryId() + " ) (" + cvTerm.getShortName() + ")");
                    } else {
                        Iterator itXrefs = null;
                        if (updated) {
                            selectedXrefs.remove(newXref);
                            itXrefs = selectedXrefs.iterator();
                        } else {
                            itXrefs = selectedXrefs.iterator();
                            Xref xref = (Xref) itXrefs.next();
                            if (!xref.equals(newXref)) {
                                xref.setPrimaryId(cvTermXref.getId());
                                xrefDao.update(xref);
                                output.println("\t\t Updated Xref( " + database.getShortLabel() + ", " + qualifier.getShortLabel() + ", " + xref.getPrimaryId() + " ) (" + cvTerm.getShortName() + ")");
                            }
                        }
                        while (itXrefs.hasNext()) {
                            Xref xref = (Xref) itXrefs.next();
                            xrefDao.delete(xref);
                            output.println("\t\t Deleted Xref( " + database.getShortLabel() + ", " + qualifier.getShortLabel() + ", " + xref.getPrimaryId() + " ) (" + cvTerm.getShortName() + ")");
                        }
                    }
                } else {
                    if (!cvObject.getXrefs().contains(newXref)) {
                        cvObject.addXref(newXref);
                        xrefDao.persist(newXref);
                        output.println("\t\t Created Xref( " + database.getShortLabel() + ", " + qualifier.getShortLabel() + ", " + newXref.getPrimaryId() + " ) (" + cvTerm.getShortName() + ")");
                    }
                }
            }
        }
    }

    /**
     * Update Aliases of the given CvObject using the cvTerm definition read from the psi ontology file.
     *
     * @param cvObject the cvObject we want to update.
     * @param cvTerm   the cvTerm containing the data to update from.
     *
     * @throws IntactException if an error occurs during the update.
     */
    private static void updateAliases(CvObject cvObject, CvTerm cvTerm, PrintStream output, UpdateCVsReport report) throws IntactException {
        CvAliasType defaultAliasType = (CvAliasType) getCvObject(CvAliasType.class, CvAliasType.GO_SYNONYM, CvAliasType.GO_SYNONYM_MI_REF, output, report);
        if (defaultAliasType == null) {
            throw new IllegalStateException("Could not find " + CvAliasType.GO_SYNONYM + " in the IntAct node. Abort.");
        }
        Institution institution = IntactContext.getCurrentInstance().getInstitution();
        for (Iterator iterator = cvTerm.getSynonyms().iterator(); iterator.hasNext(); ) {
            CvTermSynonym synonym = (CvTermSynonym) iterator.next();
            CvAliasType specificType = null;
            if (synonym.hasType()) {
                specificType = (CvAliasType) getCvObject(CvAliasType.class, synonym.getType(), output, report);
            }
            if (specificType == null) {
                output.println("ERROR: Could not find or create CvAliasType( '" + synonym.getType() + "' ). Using '" + defaultAliasType.getShortLabel() + "' instead.");
                specificType = defaultAliasType;
            }
            CvObjectAlias alias = new CvObjectAlias(institution, cvObject, specificType, synonym.getName());
            if (!alias.getName().equals(synonym.getName())) {
                output.println("\t\t Skipping Alias( " + specificType.getShortLabel() + ", '" + synonym.getName() + "' ) ... the content would be truncated.");
                continue;
            }
            if (!cvObject.getAliases().contains(alias)) {
                cvObject.addAlias(alias);
                getDaoFactory().getAliasDao(CvObjectAlias.class).persist(alias);
                output.println("\t\t Created Alias( " + specificType.getShortLabel() + ", '" + synonym.getName() + "' )");
            }
        }
    }

    /**
     * search for the first Annotations having the given CvTopic and deletes all others.
     *
     * @param cvObject the object on which we search for an annotation
     *
     * @return a unique annotation or null if none is found.
     *
     * @throws IntactException
     */
    private static Annotation getUniqueAnnotation(CvObject cvObject, CvTopic topicFilter) throws IntactException {
        if (topicFilter == null) {
            throw new NullPointerException();
        }
        Annotation myAnnotation = null;
        Collection<Annotation> toDelete = new ArrayList<Annotation>();
        for (Iterator iterator = cvObject.getAnnotations().iterator(); iterator.hasNext(); ) {
            Annotation annotation = (Annotation) iterator.next();
            if (topicFilter.equals(annotation.getCvTopic())) {
                if (myAnnotation == null) {
                    myAnnotation = annotation;
                } else {
                    toDelete.add(annotation);
                }
            }
        }
        for (Annotation annotation : toDelete) {
            System.out.println("Removing extra annotation: Annotation(" + annotation.getCvTopic().getShortLabel() + ", '" + annotation.getAnnotationText() + "')");
            cvObject.removeAnnotation(annotation);
            getDaoFactory().getCvObjectDao(CvObject.class).update(cvObject);
            getDaoFactory().getAnnotationDao().delete(annotation);
        }
        return myAnnotation;
    }

    private static CvDatabase psi;

    private static CvDatabase intact;

    private static CvXrefQualifier identity;

    /**
     * Assures that necessary Controlled vocabulary terms are present prior to manipulation of other terms.
     *
     * @throws uk.ac.ebi.intact.business.IntactException
     *
     */
    public static void createNecessaryCvTerms(PrintStream output, UpdateCVsReport report) throws IntactException {
        IntactContext intactContext = IntactContext.getCurrentInstance();
        identity = intactContext.getCvContext().getByMiRef(CvXrefQualifier.class, CvXrefQualifier.IDENTITY_MI_REF);
        CvObjectBuilder cvBuilder = new CvObjectBuilder();
        if (identity == null) {
            identity = cvBuilder.createIdentityCvXrefQualifier(IntactContext.getCurrentInstance());
            identity.setFullName("identical object");
            intactContext.getDataContext().getDaoFactory().getCvObjectDao(CvXrefQualifier.class).persist(identity);
        }
        psi = intactContext.getCvContext().getByMiRef(CvDatabase.class, CvDatabase.PSI_MI_MI_REF);
        if (psi == null) {
            psi = cvBuilder.createPsiMiCvDatabase(IntactContext.getCurrentInstance());
            psi.setFullName("psi-mi");
            intactContext.getDataContext().getDaoFactory().getCvObjectDao(CvDatabase.class).persist(psi);
        }
        intactContext.getDataContext().flushSession();
        identity = (CvXrefQualifier) getCvObject(CvXrefQualifier.class, CvXrefQualifier.IDENTITY, CvXrefQualifier.IDENTITY_MI_REF, "identical object", output, report);
        psi = (CvDatabase) getCvObject(CvDatabase.class, CvDatabase.PSI_MI, CvDatabase.PSI_MI_MI_REF, output, report);
        intact = (CvDatabase) getCvObject(CvDatabase.class, CvDatabase.INTACT, CvDatabase.INTACT_MI_REF, output, report);
        getCvObject(CvDatabase.class, CvDatabase.PUBMED, CvDatabase.PUBMED_MI_REF, output, report);
        getCvObject(CvDatabase.class, CvDatabase.GO, CvDatabase.GO_MI_REF, "gene ontology definition reference", output, report);
        getCvObject(CvDatabase.class, CvDatabase.SO, CvDatabase.SO_MI_REF, "sequence ontology", output, report);
        getCvObject(CvDatabase.class, CvDatabase.RESID, CvDatabase.RESID_MI_REF, output, report);
        getCvObject(CvXrefQualifier.class, CvXrefQualifier.GO_DEFINITION_REF, output, report);
        getCvObject(CvXrefQualifier.class, CvXrefQualifier.SEE_ALSO, CvXrefQualifier.SEE_ALSO_MI_REF, output, report);
        getCvObject(CvAliasType.class, CvAliasType.GO_SYNONYM, CvAliasType.GO_SYNONYM_MI_REF, output, report);
        getCvObject(CvTopic.class, CvTopic.COMMENT, CvTopic.COMMENT_MI_REF, output, report);
        getCvObject(CvTopic.class, CvTopic.OBSOLETE, CvTopic.OBSOLETE_MI_REF, output, report);
        intactContext.getDataContext().flushSession();
    }

    /**
     * Reads a file and update CvObject's annotations accordingly.
     * <p/>
     * File format:
     * <pre>
     * shortlabel &lt;tab&gt; fullname &lt;tab&gt; type &lt;tab&gt; mi &lt;tab&gt; topic &lt;tab&gt; text &lt;tab&gt;
     * apply to children (true|false)
     * </pre>
     *
     * @param is the stream containing the annotations.
     *
     * @throws IntactException if an error occurs during update.
     * @throws IOException     if an error occurs while handling the file.
     */
    private static void updateAnnotationsFromFile(InputStream is, PrintStream output) throws IntactException, IOException {
        BufferedReader in = null;
        try {
            try {
                output.println("Database: " + getDaoFactory().getBaseDao().getDbName());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            in = new BufferedReader(new InputStreamReader(is));
            String line;
            int lineCount = 0;
            while ((line = in.readLine()) != null) {
                lineCount++;
                line = line.trim();
                if (line.startsWith("#")) {
                    continue;
                }
                if (line.length() == 0) {
                    continue;
                }
                StringTokenizer stringTokenizer = new StringTokenizer(line, "\t");
                final String shorltabel = stringTokenizer.nextToken();
                final String fullname = stringTokenizer.nextToken();
                final String type = stringTokenizer.nextToken();
                final String mi = stringTokenizer.nextToken();
                final String topic = stringTokenizer.nextToken();
                final String reason = stringTokenizer.nextToken();
                final String applyToChildrenValue = stringTokenizer.nextToken();
                try {
                    boolean applyToChildren = false;
                    if ("true".equalsIgnoreCase(applyToChildrenValue.trim())) {
                        applyToChildren = true;
                    }
                    CvObject cv = null;
                    if (mi != null && mi.startsWith("MI:")) {
                        cv = getDaoFactory().getCvObjectDao(CvObject.class).getByXref(mi);
                        if (cv == null) {
                            output.println("WARN: Could not find the object by the given reference: '" + mi + "'.");
                        }
                    }
                    if (cv == null) {
                        if (shorltabel != null && shorltabel.trim().length() > 0) {
                            cv = getDaoFactory().getCvObjectDao(CvObject.class).getByShortLabel(shorltabel);
                        } else {
                            throw new Exception("Line " + lineCount + ": Neither a valid shortlabel (" + shorltabel + ") " + "nor MI ref (" + mi + ") were given, could not find the corresponding " + "CvObject. Skip line.");
                        }
                    }
                    if (cv != null) {
                        output.println("-------------------------------------------------------------------------");
                        output.println("Read line " + lineCount + ": " + cv.getShortLabel() + "...");
                        if (applyToChildren) {
                            if (!CvDagObject.class.isAssignableFrom(cv.getClass())) {
                                applyToChildren = false;
                                System.out.println("Line " + lineCount + ": The specified type (" + cv.getClass() + ") is " + "not hierarchical, though you have requested an updated on children " + "term. set not to apply to children.");
                            }
                        }
                        CvTopic cvTopic = getDaoFactory().getCvObjectDao(CvTopic.class).getByShortLabel(topic);
                        if (cvTopic == null) {
                            throw new Exception("Line " + lineCount + ": Could not find CvTopic( " + topic + " ). Skip line.");
                        }
                        Set<CvObject> termsToUpdate = new HashSet<CvObject>();
                        termsToUpdate.add(cv);
                        if (applyToChildren) {
                            collectAllChildren((CvDagObject) cv, termsToUpdate);
                        }
                        for (CvObject aTermToUpdate : termsToUpdate) {
                            String termMi = getPsiId(aTermToUpdate);
                            if (cv.equals(aTermToUpdate)) {
                                output.println("Updating term: " + aTermToUpdate.getShortLabel() + " (" + termMi + ")");
                            } else {
                                output.println("Updating child: " + aTermToUpdate.getShortLabel() + " (" + termMi + ")");
                            }
                            Annotation annot = getUniqueAnnotation(aTermToUpdate, cvTopic);
                            Institution institution = IntactContext.getCurrentInstance().getInstitution();
                            Annotation newAnnotation = new Annotation(institution, cvTopic, reason);
                            if (annot == null) {
                                getDaoFactory().getAnnotationDao().persist(newAnnotation);
                                aTermToUpdate.addAnnotation(newAnnotation);
                                getDaoFactory().getCvObjectDao(CvObject.class).update(aTermToUpdate);
                                output.println("\tCREATED new Annotation( " + cvTopic.getShortLabel() + ", '" + reason + "' )");
                            } else {
                                if (!newAnnotation.equals(annot)) {
                                    output.println("\tOLD: " + annot);
                                    output.println("\tNEW: " + newAnnotation);
                                    annot.setAnnotationText(reason);
                                    getDaoFactory().getAnnotationDao().update(annot);
                                    String myClassName = type.substring(type.lastIndexOf(".") + 1, type.length());
                                    output.println("\tUPDATED Annotation( " + cvTopic.getShortLabel() + ", '" + reason + "' )");
                                }
                            }
                        }
                    }
                } catch (ClassNotFoundException e) {
                    output.println("ERROR: Line " + lineCount + ": Object Type not supported: '" + type + "'. skipping.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    /**
     * Recursive methods that collects all children terms of the given terms.
     *
     * @param cv
     * @param termsToUpdate
     */
    private static void collectAllChildren(CvDagObject cv, Collection termsToUpdate) {
        if (termsToUpdate == null) {
            throw new IllegalArgumentException("You must give a non null collection.");
        }
        for (Iterator iterator = cv.getChildren().iterator(); iterator.hasNext(); ) {
            CvDagObject child = (CvDagObject) iterator.next();
            termsToUpdate.add(child);
            collectAllChildren(child, termsToUpdate);
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1 && args.length != 2) {
            System.err.println("Usage: UpdateCVs <obo file> [<annotation update file>]");
            System.exit(1);
        }
        String oboFilename = args[0];
        String annotFilename = null;
        if (args.length == 2) {
            annotFilename = args[1];
        }
        String instanceName = getDaoFactory().getBaseDao().getDbName();
        System.out.println("Database: " + instanceName);
        System.out.println("User: " + getDaoFactory().getBaseDao().getDbUserName());
        load(new File(oboFilename), new File(annotFilename), System.out, new UpdateCVsConfig());
    }

    public static UpdateCVsReport loadDefaultCVs() throws PsiLoaderException, IOException {
        return loadDefaultCVs(System.out, new UpdateCVsConfig());
    }

    public static UpdateCVsReport loadDefaultCVs(PrintStream output, UpdateCVsConfig config) throws PsiLoaderException, IOException {
        return load(new URL("http://intact.svn.sourceforge.net/viewvc/*checkout*/intact/repo/utils/data/controlledVocab/psi-mi25-4intact.obo"), output, config);
    }

    public static UpdateCVsReport load(URL url, PrintStream output, UpdateCVsConfig config) throws PsiLoaderException, IOException {
        File oboFile = createFileFromURL(url);
        return load(oboFile, output, config);
    }

    public static UpdateCVsReport load(File oboFile, PrintStream output, UpdateCVsConfig config) throws PsiLoaderException, IOException {
        return load(new FileInputStream(oboFile), null, output, config);
    }

    public static UpdateCVsReport load(File oboFile, File annotFile, PrintStream output, UpdateCVsConfig config) throws PsiLoaderException, IOException {
        return load(new FileInputStream(oboFile), new FileInputStream(annotFile), output, config);
    }

    public static UpdateCVsReport load(InputStream oboFile, PrintStream output, UpdateCVsConfig config) throws PsiLoaderException, IOException {
        return load(oboFile, null, output, config);
    }

    public static UpdateCVsReport load(InputStream oboFile, InputStream annotFile, PrintStream output, UpdateCVsConfig config) throws PsiLoaderException, IOException {
        UpdateCVsReport report = new UpdateCVsReport();
        output.println("Parsing OBO File...\n");
        PSILoader psi = new PSILoader();
        IntactOntology ontology = psi.parseOboFile(oboFile);
        checkConsistency(ontology);
        report.setOntology(ontology);
        long max = searchLastIntactId(ontology);
        SequenceManager.synchronizeUpTo(max);
        output.println("\nCreating necessary vocabulary terms...\n");
        final DataContext dataContext = IntactContext.getCurrentInstance().getDataContext();
        SmallCvPrimer cvPrimer = new SmallCvPrimer(dataContext.getDaoFactory());
        cvPrimer.createCVs();
        output.println("\nUpdating CVs...\n");
        update(ontology, output, report, config);
        dataContext.flushSession();
        output.println("\nUpdating Obsolete terms...\n");
        Collection<CvTerm> orphanTerms = listOrphanObsoleteTerms(ontology, output, report);
        report.setOrphanTerms(orphanTerms);
        if (annotFile != null) {
            try {
                updateAnnotationsFromFile(annotFile, output);
            } catch (IOException e) {
                output.println("ERROR: Could not Update CVs' annotations.");
                e.printStackTrace();
            }
        }
        dataContext.flushSession();
        return report;
    }

    private static void checkConsistency(IntactOntology ontology) {
        for (Class cvType : ontology.getTypes()) {
            Collection<CvTerm> termsOfSameType = ontology.getCvTerms(cvType);
            Set<String> clazzAndLabelSet = new HashSet<String>(termsOfSameType.size());
            for (CvTerm cvTerm : termsOfSameType) {
                String setItem = cvTerm.getShortName();
                if (clazzAndLabelSet.contains(setItem)) {
                    throw new InvalidOboFormatException("At least two terms of type '" + cvType.getName() + "' with the same label found: '" + setItem + "'");
                }
                clazzAndLabelSet.add(setItem);
            }
        }
    }

    private static CvObject getCVWithMi(List<Mi2Cv> cvList, Class cvType, String mi) {
        for (Mi2Cv mi2cv : cvList) {
            if (mi2cv.getMi().equals(mi) && cvType.equals(mi2cv.getCv().getClass())) {
                return mi2cv.getCv();
            }
        }
        return null;
    }

    private static class Mi2Cv {

        private String mi;

        private CvObject cv;

        public Mi2Cv(String mi, CvObject cv) {
            this.mi = mi;
            this.cv = cv;
        }

        public String getMi() {
            return mi;
        }

        public CvObject getCv() {
            return cv;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Mi2Cv mi2Cv = (Mi2Cv) o;
            if (cv != null ? !cv.equals(mi2Cv.cv) : mi2Cv.cv != null) {
                return false;
            }
            if (mi != null ? !mi.equals(mi2Cv.mi) : mi2Cv.mi != null) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            int result;
            result = (mi != null ? mi.hashCode() : 0);
            result = 31 * result + (cv != null ? cv.hashCode() : 0);
            return result;
        }
    }

    private static class CvObjectCache {

        private Map<String, CvObject> cvMap;

        public CvObjectCache() {
            cvMap = new HashMap<String, CvObject>();
        }

        public CvObject get(Class cvClass, String label, String mi) {
            return cvMap.get(toKey(cvClass, mi, label));
        }

        public void put(Class cvClass, String mi, String label, CvObject cv) {
            cvMap.put(toKey(cvClass, mi, label), cv);
        }

        private String toKey(Class cvClass, String mi, String label) {
            return cvClass.getSimpleName() + "_" + mi + "_" + label;
        }
    }

    private static File createFileFromURL(URL url) throws IOException {
        File tempFile = File.createTempFile("oboFile", ".obo");
        PrintStream ps = new PrintStream(tempFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            ps.println(line);
        }
        ps.close();
        return tempFile;
    }
}

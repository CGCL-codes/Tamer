package uk.ac.ebi.intact.dataexchange.cvutils;

import org.apache.log4j.Logger;
import org.bbop.dataadapter.DataAdapterException;
import org.obo.dataadapter.OBOFileAdapter;
import org.obo.datamodel.*;
import org.obo.datamodel.impl.*;
import uk.ac.ebi.intact.dataexchange.cvutils.model.CvObjectOntologyBuilder;
import uk.ac.ebi.intact.model.*;
import uk.ac.ebi.intact.model.util.CvObjectUtils;
import java.io.*;
import java.util.*;
import static java.util.Collections.sort;

/**
 * Contains methods to recreate the OBOSession object from a list of CVObjects
 * The CVObject is stripped and a OBOObject is created which is then added
 * to the OBOSession and finally written to an OBO 1.2 file using a DataAdapter
 *
 * @author Prem Anand (prem@ebi.ac.uk)
 * @version $Id$
 * @since 2.0.1
 */
public class CvExporter {

    protected static final Logger log = Logger.getLogger(CvExporter.class);

    private static final String ALIAS_IDENTIFIER = "PSI-MI-alternate";

    private static final String SHORTLABEL_IDENTIFIER = "PSI-MI-short";

    private Map<CvClassIdentifier, OBOClass> cvToOboCache;

    private OBOClass rootObj;

    private OBOClass tissueObj;

    private OBOClass cellTypeObj;

    private OBOSession oboSession;

    public CvExporter() {
        ObjectFactory objFactory;
        objFactory = new DefaultObjectFactory();
        oboSession = new OBOSessionImpl(objFactory);
        cvToOboCache = new HashMap<CvClassIdentifier, OBOClass>();
    }

    /**
     * Converts a list of Cvs to list of OBOObjects and add it to the OBOSession
     *
     * @param allCvs List of all Cvs
     * @return OBOSession objects with all Cvs converted to OBOObject and added to the OBOsession
     */
    public OBOSession convertToOBOSession(List<CvDagObject> allCvs) {
        List<CvDagObject> allUniqCvs;
        allUniqCvs = allCvs;
        sort(allUniqCvs, new Comparator<CvObject>() {

            public int compare(CvObject o1, CvObject o2) {
                String id1 = CvObjectUtils.getIdentity(o1);
                String id2 = CvObjectUtils.getIdentity(o2);
                return id1.compareTo(id2);
            }
        });
        Map<String, HashSet<CvDagObject>> cvMapWithParents = groupByMis(allUniqCvs);
        int counter = 1;
        for (CvDagObject cvDagObj : allUniqCvs) {
            if (CvObjectUtils.getIdentity(cvDagObj) == null) {
                throw new NullPointerException("No Identifier for the cvObject " + cvDagObj);
            }
            if (log.isTraceEnabled()) log.trace(counter + "  " + CvObjectUtils.getIdentity(cvDagObj));
            oboSession.addObject(getRootObject());
            OBOClass oboObj = convertCv2OBO(cvDagObj, cvMapWithParents);
            oboSession.addObject(oboObj);
            counter++;
        }
        if (log.isDebugEnabled()) {
            log.debug("Total CvTerms written " + counter);
        }
        addHeaderInfo();
        addFooterInfo();
        return oboSession;
    }

    public void exportToFile(List<? extends CvObject> cvObjects, File oboFileToExport) throws IOException, DataAdapterException {
        OBOSession oboSession = convertToOBOSession((List<CvDagObject>) cvObjects);
        writeOBOFile(oboSession, oboFileToExport);
    }

    protected Map<String, HashSet<CvDagObject>> groupByMis(List<CvDagObject> allCvs) {
        Map<String, HashSet<CvDagObject>> cvMapWithParents = new HashMap<String, HashSet<CvDagObject>>();
        for (CvDagObject cvdag : allCvs) {
            if (cvMapWithParents.get(CvObjectUtils.getIdentity(cvdag)) == null) {
                cvMapWithParents.put(CvObjectUtils.getIdentity(cvdag), new HashSet<CvDagObject>(cvdag.getParents()));
            } else {
                HashSet<CvDagObject> alreadyExisting = cvMapWithParents.get(CvObjectUtils.getIdentity(cvdag));
                alreadyExisting.addAll(cvdag.getParents());
                cvMapWithParents.put(CvObjectUtils.getIdentity(cvdag), alreadyExisting);
            }
        }
        return cvMapWithParents;
    }

    protected void addObject(OBOClass oboObj) {
        oboSession.addObject(oboObj);
    }

    /**
     * The OBOFileAdapter writes the OBOSession object in to the given file specified
     *
     * @param oboSession The OBOsession object with all OBOClass instances added to it
     * @param outFile    The OBO file
     * @throws DataAdapterException refer org.bbop.dataadapter.DataAdapterException
     * @throws IOException          refer java.io.IOException
     */
    public void writeOBOFile(OBOSession oboSession, File outFile) throws DataAdapterException, IOException {
        File tempFile = File.createTempFile("obofile-", ".obo");
        OboUtils.saveSession(oboSession, tempFile);
        FileWriter writer = new FileWriter(outFile);
        BufferedReader reader = new BufferedReader(new FileReader(tempFile));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("regexp")) {
                line = line.replaceAll("\\\\\\\\\"", "\\\\\"");
            }
            writer.write(line + "\n");
        }
        writer.close();
        tempFile.delete();
    }

    private void addHeaderInfo() {
        oboSession.setDefaultNamespace(new Namespace("PSI-MI"));
        oboSession.addPropertyValue(new PropertyValueImpl("auto-generated-by", "IntAct CvExporter"));
        oboSession.addSynonymCategory(new SynonymCategoryImpl("PSI-MI-alternate", "Alternate label curated by PSI-MI"));
        oboSession.addSynonymCategory(new SynonymCategoryImpl("PSI-MI-short", "Unique short label curated by PSI-MI"));
    }

    private void addFooterInfo() {
        OBOPropertyImpl partOf = new OBOPropertyImpl("part_of");
        partOf.setName("part of");
        partOf.setTransitive(true);
        oboSession.addObject(partOf);
    }

    /**
     * The List contains duplicates as the method itselfAndChildrenAsList adds
     * itself and the children and again the child gets added.
     * This method removes the dubplicates from the list
     *
     * @param allCvs List of all Cvs with duplicates
     * @return Lists of Uniq Cvs
     */
    protected List<CvDagObject> removeCvsDuplicated(List<CvDagObject> allCvs) {
        HashMap<String, CvDagObject> cvHash = new HashMap<String, CvDagObject>();
        List<CvDagObject> allUniqCvs = new ArrayList<CvDagObject>();
        for (CvDagObject cvObj : allCvs) {
            cvHash.put(CvObjectUtils.getIdentity(cvObj), cvObj);
        }
        for (String s : cvHash.keySet()) {
            CvDagObject cvDagObject = cvHash.get(s);
            allUniqCvs.add(cvDagObject);
        }
        return allUniqCvs;
    }

    /**
     * Converts cvobject to OBOobject
     *
     * @param dagObj           CvObject that needs to be converted to OBOOBject
     * @param cvMapWithParents a HashMap with CvDag object and all its parents as HashSet
     * @return a OBOClass instance
     */
    protected OBOClass convertCv2OBO(CvDagObject dagObj, Map<String, HashSet<CvDagObject>> cvMapWithParents) {
        final String cvId = CvObjectUtils.getIdentity(dagObj);
        if (cvId == null) {
            throw new NullPointerException("Identifier is null");
        }
        CvClassIdentifier cvClassId = new CvClassIdentifier(cvId, dagObj.getClass());
        if (cvToOboCache.containsKey(cvClassId)) {
            return cvToOboCache.get(cvClassId);
        }
        OBOClass oboObj = new OBOClassImpl(dagObj.getFullName(), cvId);
        cvToOboCache.put(cvClassId, oboObj);
        if (dagObj.getShortLabel() != null) {
            Synonym syn = createSynonym(dagObj.getShortLabel());
            oboObj.addSynonym(syn);
        }
        Collection<CvObjectXref> xrefs = dagObj.getXrefs();
        for (CvObjectXref xref : xrefs) {
            boolean isIdentity = false;
            CvXrefQualifier qualifier = xref.getCvXrefQualifier();
            CvDatabase database = xref.getCvDatabase();
            String qualMi;
            String dbMi;
            if (qualifier != null && database != null && (qualMi = CvObjectUtils.getIdentity(qualifier)) != null && (dbMi = CvObjectUtils.getIdentity(database)) != null && qualMi.equals(CvXrefQualifier.IDENTITY_MI_REF) && dbMi.equals(CvDatabase.PSI_MI_MI_REF)) {
                isIdentity = true;
            }
            if (!isIdentity) {
                String dbx = "";
                if (database != null && database.getShortLabel() != null) {
                    if (database.getShortLabel().equals(CvDatabase.PUBMED)) {
                        dbx = "PMID";
                    } else {
                        dbx = database.getShortLabel().toUpperCase();
                    }
                }
                Dbxref dbxref = new DbxrefImpl(dbx, xref.getPrimaryId());
                dbxref.setType(Dbxref.DEFINITION);
                oboObj.addDefDbxref(dbxref);
            }
        }
        Collection<Annotation> annotations = dagObj.getAnnotations();
        String definitionPrefix = "";
        String definitionSuffix = "";
        for (Annotation annotation : annotations) {
            if (annotation.getCvTopic() != null && annotation.getCvTopic().getShortLabel() != null) {
                CvTopic cvTopic = annotation.getCvTopic();
                if (cvTopic.getShortLabel().equalsIgnoreCase(CvTopic.DEFINITION)) {
                    definitionPrefix = annotation.getAnnotationText();
                } else if (cvTopic.getShortLabel().equalsIgnoreCase(CvTopic.URL)) {
                    definitionSuffix = "\n" + annotation.getAnnotationText();
                } else if (cvTopic.getShortLabel().equalsIgnoreCase(CvTopic.SEARCH_URL)) {
                    String annotationText = "\"" + annotation.getAnnotationText() + "\"";
                    oboObj.addPropertyValue(new PropertyValueImpl("xref", CvTopic.SEARCH_URL + ": " + annotationText));
                } else if (cvTopic.getShortLabel().equalsIgnoreCase(CvTopic.XREF_VALIDATION_REGEXP)) {
                    String annotationText = "\\\"" + annotation.getAnnotationText() + "\\\"";
                    oboObj.addPropertyValue(new PropertyValueImpl("xref", CvTopic.XREF_VALIDATION_REGEXP + ":" + annotationText));
                } else if (cvTopic.getShortLabel().equalsIgnoreCase(CvTopic.COMMENT)) {
                    oboObj.setComment(removeLineReturns(annotation.getAnnotationText()));
                } else if (cvTopic.getShortLabel().equalsIgnoreCase(CvTopic.OBSOLETE)) {
                    oboObj.setObsolete(true);
                    definitionSuffix = "\n" + annotation.getAnnotationText();
                }
            }
        }
        oboObj.setDefinition(definitionPrefix + definitionSuffix);
        for (CvObjectAlias cvAlias : dagObj.getAliases()) {
            Synonym altSyn = createAlias(cvAlias);
            oboObj.addSynonym(altSyn);
        }
        if (checkIfRootMI(cvId)) {
            addLinkToRootFor(oboObj);
        }
        Set<CvDagObject> cvParents = cvMapWithParents.get(dagObj.getIdentifier());
        if (cvParents != null) {
            for (CvDagObject cvParentObj : cvParents) {
                OBOClass isA = convertCv2OBO(cvParentObj, cvMapWithParents);
                addChild(isA, oboObj);
            }
        }
        if (dagObj instanceof CvTissue && !"IAX:0001".equals(dagObj.getIdentifier())) {
            addChild(getOBOTissue(), oboObj);
        } else if (dagObj instanceof CvCellType && !"IAX:0002".equals(dagObj.getIdentifier())) {
            addChild(getOBOCellType(), oboObj);
        }
        return oboObj;
    }

    private void addChild(OBOClass parent, OBOClass child) {
        Link linkToIsA = new OBORestrictionImpl(child);
        linkToIsA.setType(OBOProperty.IS_A);
        parent.addChild(linkToIsA);
    }

    private void addLinkToRootFor(OBOClass oboObj) {
        OBOClass rootObject = getRootObject();
        Link linkToRoot = new OBORestrictionImpl(oboObj);
        OBOProperty oboProp = new OBOPropertyImpl("part_of");
        linkToRoot.setType(oboProp);
        rootObject.addChild(linkToRoot);
    }

    private String removeLineReturns(String annotationText) {
        return annotationText.replaceAll("\r\n", " ");
    }

    private OBOClass getRootObject() {
        if (rootObj != null) {
            return rootObj;
        }
        rootObj = new OBOClassImpl("molecular interaction", "MI:0000");
        rootObj.setDefinition("Controlled vocabularies originally created for protein protein interactions, extended to other molecules interactions.");
        Dbxref dbxref = new DbxrefImpl("PMID", "14755292");
        dbxref.setType(Dbxref.DEFINITION);
        rootObj.addDefDbxref(dbxref);
        addPsimiShortSyn(rootObj, "mi");
        return rootObj;
    }

    private void addPsimiShortSyn(OBOClass oboObj, String synonym) {
        Synonym syn = new SynonymImpl();
        syn.setText(synonym);
        SynonymCategory synCat = new SynonymCategoryImpl();
        synCat.setID(SHORTLABEL_IDENTIFIER);
        syn.setSynonymCategory(synCat);
        syn.setScope(1);
        oboObj.addSynonym(syn);
    }

    public OBOClass getOBOTissue() {
        if (tissueObj != null) {
            return tissueObj;
        }
        tissueObj = new OBOClassImpl("tissue", "IAX:0001");
        tissueObj.setDefinition("IntAct tissue base object");
        addLinkToRootFor(tissueObj);
        oboSession.addObject(tissueObj);
        return tissueObj;
    }

    public OBOClass getOBOCellType() {
        if (cellTypeObj != null) {
            return cellTypeObj;
        }
        cellTypeObj = new OBOClassImpl("cell type", "IAX:0002");
        cellTypeObj.setDefinition("IntAct cell type base object");
        addLinkToRootFor(cellTypeObj);
        oboSession.addObject(cellTypeObj);
        return cellTypeObj;
    }

    private boolean checkIfRootMI(String mi) {
        for (String s : CvObjectOntologyBuilder.mi2Class.keySet()) {
            if (mi.equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }

    private Synonym createAlias(CvObjectAlias cvAlias) {
        Synonym syn = new SynonymImpl();
        syn.setText(cvAlias.getName());
        SynonymCategory synCat = new SynonymCategoryImpl();
        synCat.setID(ALIAS_IDENTIFIER);
        syn.setSynonymCategory(synCat);
        syn.setScope(1);
        return syn;
    }

    private Synonym createSynonym(String shortLabel) {
        Synonym syn = new SynonymImpl();
        syn.setText(shortLabel);
        SynonymCategory synCat = new SynonymCategoryImpl();
        synCat.setID(SHORTLABEL_IDENTIFIER);
        syn.setSynonymCategory(synCat);
        syn.setScope(1);
        return syn;
    }

    protected OBOSession getOboSession() {
        return oboSession;
    }

    private static class CvClassIdentifier {

        private String identifier;

        private Class<? extends CvObject> cvClass;

        private CvClassIdentifier(String identifier, Class<? extends CvObject> cvClass) {
            this.identifier = identifier;
            this.cvClass = cvClass;
        }

        public String getIdentifier() {
            return identifier;
        }

        public Class<? extends CvObject> getCvClass() {
            return cvClass;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CvClassIdentifier that = (CvClassIdentifier) o;
            if (!cvClass.equals(that.cvClass)) return false;
            if (!identifier.equals(that.identifier)) return false;
            return true;
        }

        @Override
        public int hashCode() {
            int result = identifier.hashCode();
            result = 31 * result + cvClass.hashCode();
            return result;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(identifier).append('(');
            sb.append(cvClass.getSimpleName());
            sb.append(')');
            return sb.toString();
        }
    }
}

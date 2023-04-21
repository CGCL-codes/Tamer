package gcr.rdf;

import gcr.mmm2.model.IPerson;
import gcr.mmm2.model.ObjectSorter;
import gcr.mmm2.model.PersonManager;
import gcr.mmm2.model.StringFormatException;
import gcr.mmm2.rdb.RDBConnection;
import gcr.mmm2.util.StringUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * @author Simon King
 *
 */
public class RDFManipulator {

    public static final int DATATYPE_PROPERTY = 1;

    public static final String INSTANTIABLE_CLASS_URI = "http://garage.sims.berkeley.edu/mmm-2/ontology#Instantiable";

    public static final int INSTANTIABLE_INSTANCE_PROPERTY = 3;

    public static final String MMMBASE_CLASS_URI = "http://garage.sims.berkeley.edu/mmm-2/ontology#mmmBase";

    public static final int MMMBASE_INSTANCE_PROPERTY = 5;

    public static final String PERSON_ID_CLASS_URI = "http://garage.sims.berkeley.edu/mmm-2/ontology#mmmPersonID";

    public static final int PERSON_ID_PROPERTY = 4;

    public static final String PROPERTY_CREATOR_URI = "http://garage.sims.berkeley.edu/mmm-2/ontology#creator";

    public static final String PROPERTY_EDIT_SUBJECT_URI = "http://garage.sims.berkeley.edu/mmm-2/ontology#editSubject";

    public static final String PROPERTY_HAS_ASSOC_OBJ_URI = "http://garage.sims.berkeley.edu/mmm-2/ontology#hasAssociatedObject";

    public static final String PROPERTY_HIDDEN_URI = "http://garage.sims.berkeley.edu/mmm-2/ontology#hidden";

    public static final String PROPERTY_SUBJECT_URI = "http://garage.sims.berkeley.edu/mmm-2/ontology#subject";

    public static final String PROPERTY_TIMESTAMP_URI = "http://garage.sims.berkeley.edu/mmm-2/ontology#timestamp";

    public static final String PROPERTY_HAS_LABEL = "http://garage.sims.berkeley.edu/mmm-2/ontology#label";

    public static final int TAXONOMY_CLASS_PROPERTY = 2;

    public static final String TAXONOMYITEM_CLASS_URI = "http://garage.sims.berkeley.edu/mmm-2/ontology#taxonomyItem";

    public static String addAnnotationEditTarget(Individual subject, String targetURI) {
        if (addAnnotationProperty(subject, PROPERTY_EDIT_SUBJECT_URI, targetURI)) {
            return setHidden(targetURI);
        }
        return null;
    }

    public static boolean addAnnotationProperty(Resource subject, String propURI, String propValue) {
        Property p = MMMModelFactory.getInstance().getOntModel().createProperty(propURI);
        String propTypeURI = (String) MMMModelFactory.getInstance().getTypeMap().get(propURI);
        if (MMMModelFactory.TYPE_OWL_DATATYPE_PROP.equals(propTypeURI)) {
            subject.addProperty(p, propValue);
            return true;
        }
        Resource ind = MMMModelFactory.getInstance().getOntModel().createResource(propValue);
        if (ind == null) {
            return false;
        }
        subject.addProperty(p, ind);
        return true;
    }

    public static boolean addProperty2(String subjectURI, String propertyURI, String value) {
        Resource subj = MMMModelFactory.getInstance().getOntModel().createResource(subjectURI);
        return addAnnotationProperty(subj, propertyURI, value);
    }

    private static String[] convert1(String uri) {
        String[] temp = { uri, (String) MMMModelFactory.getInstance().getLabelMap().get(uri) };
        return temp;
    }

    public static String getInstanceClassLabel(String indURI) {
        String type = (String) MMMModelFactory.getInstance().getTypeMap().get(indURI);
        if (type == null) {
            return null;
        }
        return (String) MMMModelFactory.getInstance().getLabelMap().get(type);
    }

    public static Individual createIndividual(String classURI) {
        String newURI = MMMModelFactory.BASE_URI + "annotation/" + RDBConnection.getNextVal("rdf_uri_seq");
        return createIndividual(newURI, classURI);
    }

    public static Individual createIndividual(String indURI, String classURI) {
        Resource c = MMMModelFactory.getInstance().getOntModel().createResource(classURI);
        if (c == null) {
            System.out.println("createIndividual c==null");
            return null;
        }
        Individual i = MMMModelFactory.getInstance().getOntModel().createIndividual(indURI, c);
        MMMModelFactory.getInstance().addType(i.getURI(), classURI);
        return i;
    }

    public static String[] getAnnotationPropertyRange(String propURI) {
        Set rangeSet = (Set) MMMModelFactory.getInstance().getRangeMap().get(propURI);
        Iterator i = rangeSet.iterator();
        String range = null;
        while (i.hasNext()) {
            range = (String) i.next();
        }
        return convert1(range);
    }

    public static int getAnnotationPropertyType(String propURI) {
        Set propTypes = (Set) MMMModelFactory.getInstance().getAllTypesMap().get(propURI);
        if (propTypes.contains(MMMModelFactory.TYPE_OWL_DATATYPE_PROP)) {
            return DATATYPE_PROPERTY;
        }
        String[] classURIs = { TAXONOMYITEM_CLASS_URI, INSTANTIABLE_CLASS_URI, PERSON_ID_CLASS_URI, MMMBASE_CLASS_URI };
        for (int i = 0; i < classURIs.length; i++) {
            String c = classURIs[i];
            Set s = (Set) MMMModelFactory.getInstance().getRangeMap().get(propURI);
            if (s.contains(c)) {
                return i + 2;
            }
            Iterator ei = s.iterator();
            while (ei.hasNext()) {
                String c2 = (String) ei.next();
                Set s2 = (Set) MMMModelFactory.getInstance().getSubclassMap().get(c);
                if (s2.contains(c2)) {
                    return i + 2;
                }
            }
        }
        return -2;
    }

    public static String getBaseSubjectURI(String indURI) {
        String subjectURI = null;
        Iterator i = listDeclaredInstancePropertyValues(indURI, MMMModelFactory.NS_MMM_BASE, MMMModelFactory.MMM_PROP_SUBJECT_URI);
        if (i.hasNext()) {
            subjectURI = i.next().toString();
        }
        return subjectURI;
    }

    public static String getClassLabel(String uri) {
        return (String) MMMModelFactory.getInstance().getLabelMap().get(uri);
    }

    public static String getInstanceClass(Resource ind) {
        return getInstanceClass(ind.getURI());
    }

    public static String getInstanceClass(String indURI) {
        return (String) MMMModelFactory.getInstance().getTypeMap().get(indURI);
    }

    public static Map getObjectAsPropMap2(String indURI, boolean concise) {
        HashMap rtnMap = new HashMap();
        Resource ind = MMMModelFactory.getInstance().getOntModel().createResource(indURI);
        if (ind == null) {
            return rtnMap;
        }
        String creatorURI = null;
        Iterator i2 = listDeclaredInstancePropertyValues(indURI, MMMModelFactory.NS_MMM_BASE, MMMModelFactory.MMM_PROP_CREATOR_URI);
        if (i2.hasNext()) {
            creatorURI = i2.next().toString();
        }
        String subjectURI = null;
        i2 = listDeclaredInstancePropertyValues(indURI, MMMModelFactory.NS_MMM_BASE, MMMModelFactory.MMM_PROP_SUBJECT_URI);
        if (i2.hasNext()) {
            subjectURI = i2.next().toString();
        }
        String editSubjURI = null;
        i2 = listDeclaredInstancePropertyValues(indURI, MMMModelFactory.NS_MMM_BASE, MMMModelFactory.MMM_PROP_EDIT_SUBJECT_URI);
        if (i2.hasNext()) {
            editSubjURI = i2.next().toString();
        }
        String hidden = null;
        i2 = listDeclaredInstancePropertyValues(indURI, MMMModelFactory.NS_MMM_BASE, MMMModelFactory.MMM_PROP_HIDDEN_URI);
        if (i2.hasNext()) {
            hidden = i2.next().toString();
        }
        String timestamp = null;
        i2 = listDeclaredInstancePropertyValues(indURI, MMMModelFactory.NS_MMM_BASE, MMMModelFactory.MMM_PROP_TIMESTAMP_URI);
        if (i2.hasNext()) {
            timestamp = i2.next().toString();
        }
        String prettyDate = timestamp;
        try {
            Date dts = StringUtils.parseXMLDate(timestamp);
            prettyDate = StringUtils.formatDetailsDate(dts) + " " + StringUtils.formatDetailsTime(dts);
        } catch (StringFormatException ignored) {
        }
        String createdBy = getPersonLabel(creatorURI) + " on " + prettyDate;
        if (!concise) {
            rtnMap.put("CREATED_BY", createdBy);
        }
        String classURI = (String) MMMModelFactory.getInstance().getTypeMap().get(ind.getURI());
        rtnMap.put("CLASS_URI", classURI);
        List propList = (List) MMMModelFactory.getInstance().getClassMap().get(classURI);
        Iterator i = propList.iterator();
        while (i.hasNext()) {
            PropertyDescriptor pd = (PropertyDescriptor) i.next();
            if (concise && !pd.isInConcise()) {
                continue;
            }
            Property p = MMMModelFactory.getInstance().getOntModel().createProperty(pd.getURI());
            i2 = listDeclaredInstancePropertyValues(ind.getURI(), p);
            RDFNode propVal = null;
            if (i2.hasNext()) {
                propVal = (RDFNode) i2.next();
            }
            String objURI = null;
            if (propVal == null) {
                continue;
            }
            objURI = propVal.toString();
            Object propValue = null;
            switch(pd.getType()) {
                case DATATYPE_PROPERTY:
                    propValue = objURI;
                    break;
                case TAXONOMY_CLASS_PROPERTY:
                    List breadcrumbs = RDFManipulator.getTaxonomyPath(objURI);
                    propValue = "";
                    Iterator breadIter = breadcrumbs.iterator();
                    while (breadIter.hasNext()) {
                        String[] s = (String[]) breadIter.next();
                        propValue = propValue + s[1] + " > ";
                    }
                    propValue = propValue + (String) MMMModelFactory.getInstance().getLabelMap().get(objURI);
                    break;
                case INSTANTIABLE_INSTANCE_PROPERTY:
                    break;
                case PERSON_ID_PROPERTY:
                    propValue = getPersonLabel(objURI);
                    break;
                case MMMBASE_INSTANCE_PROPERTY:
                    propValue = getObjectAsPropMap2(objURI, concise);
                    break;
                default:
                    propValue = objURI;
            }
            Object[] item = { pd, propValue };
            rtnMap.put(pd.getURI(), propValue);
        }
        return rtnMap;
    }

    public static String getPersonLabel(String personURI) {
        String temp = personURI.substring(personURI.lastIndexOf("/") + 1);
        int personID = -1;
        try {
            personID = Integer.parseInt(temp);
        } catch (NumberFormatException nfe) {
        }
        IPerson p = PersonManager.getByID(personID);
        if (p != null) {
            return p.toString();
        }
        return null;
    }

    public static String getPropertyValue(String indURI, String propURI) {
        Resource ind = MMMModelFactory.getInstance().getOntModel().createResource(indURI);
        Property p = MMMModelFactory.getInstance().getOntModel().createProperty(propURI);
        RDFNode r = getPropertyValue(ind, p);
        if (r != null) {
            return r.toString();
        }
        return null;
    }

    public static RDFNode getPropertyValue(Resource ind, Property p) {
        Iterator i = listDeclaredInstancePropertyValues(ind.getURI(), p);
        RDFNode rtnVal = null;
        while (i.hasNext()) {
            rtnVal = (RDFNode) i.next();
        }
        return rtnVal;
    }

    public static String getResourceLabel(String rsrcURI) {
        String rtnVal = (String) MMMModelFactory.getInstance().getLabelMap().get(rsrcURI);
        if (rtnVal == null) {
            rtnVal = "Unknown URI";
        }
        return rtnVal;
    }

    public static List getTaxonomyChildren(String classURI) {
        List l = MMMModelFactory.getInstance().getTaxonomyChildren(classURI);
        return listConvert1(l);
    }

    public static List getTaxonomyPath(String childURI) {
        ArrayList l = new ArrayList(getTaxonomyPath(TAXONOMYITEM_CLASS_URI, childURI));
        if (l.size() > 0) {
            l.remove(0);
        }
        return l;
    }

    public static List getTaxonomyPath(String ancestorURI, String childURI) {
        List l = MMMModelFactory.getInstance().getTaxonomyPath(ancestorURI, childURI);
        Iterator i = listConvert1(l).iterator();
        while (i.hasNext()) {
            String[] s = (String[]) i.next();
        }
        return listConvert1(l);
    }

    public static AnnotationWrapper getWrappedRDFObject2(String indURI, boolean concise) {
        Map rtnMap = new HashMap();
        List rtnList = new ArrayList();
        Resource r = MMMModelFactory.getInstance().getOntModel().createResource(indURI);
        if (r == null) {
            return new AnnotationWrapper(rtnList, null, indURI, null, null, null, null, rtnMap);
        }
        String creatorURI = null;
        Iterator i2 = listDeclaredInstancePropertyValues(indURI, MMMModelFactory.NS_MMM_BASE, MMMModelFactory.MMM_PROP_CREATOR_URI);
        if (i2.hasNext()) {
            creatorURI = i2.next().toString();
        }
        String subjectURI = null;
        i2 = listDeclaredInstancePropertyValues(indURI, MMMModelFactory.NS_MMM_BASE, MMMModelFactory.MMM_PROP_SUBJECT_URI);
        if (i2.hasNext()) {
            subjectURI = i2.next().toString();
        }
        String editSubjURI = null;
        i2 = listDeclaredInstancePropertyValues(indURI, MMMModelFactory.NS_MMM_BASE, MMMModelFactory.MMM_PROP_EDIT_SUBJECT_URI);
        if (i2.hasNext()) {
            editSubjURI = i2.next().toString();
        }
        String hidden = null;
        i2 = listDeclaredInstancePropertyValues(indURI, MMMModelFactory.NS_MMM_BASE, MMMModelFactory.MMM_PROP_HIDDEN_URI);
        if (i2.hasNext()) {
            hidden = i2.next().toString();
        }
        String timestamp = null;
        i2 = listDeclaredInstancePropertyValues(indURI, MMMModelFactory.NS_MMM_BASE, MMMModelFactory.MMM_PROP_TIMESTAMP_URI);
        if (i2.hasNext()) {
            timestamp = i2.next().toString();
        }
        rtnMap = getObjectAsPropMap2(indURI, concise);
        return new AnnotationWrapper(rtnList, timestamp, indURI, subjectURI, creatorURI, editSubjURI, hidden, rtnMap);
    }

    public static boolean hasTaxonomyChildren(String classURI) {
        return (getTaxonomyChildren(classURI).size() > 0);
    }

    public static boolean isAnnotationObject(Resource ind) {
        if (ind == null) {
            return false;
        }
        return isAnnotationObject(ind.getURI());
    }

    public static boolean isAnnotationObject(String indURI) {
        Set s = (Set) MMMModelFactory.getInstance().getSuperclassMap().get(indURI);
        if (s == null) {
            String typeURI = (String) MMMModelFactory.getInstance().getTypeMap().get(indURI);
            s = (Set) MMMModelFactory.getInstance().getSuperclassMap().get(typeURI);
        }
        return (s != null && s.contains(MMMModelFactory.TYPE_MMM_ANNOTATION_BASE));
    }

    public static List listAnnotationClasses(String namespace) {
        List classList = null;
        if (namespace == null) {
            classList = new ArrayList();
            Iterator i = MMMModelFactory.getInstance().getAnnotClassMap().keySet().iterator();
            while (i.hasNext()) {
                String key = (String) i.next();
                List l = (List) MMMModelFactory.getInstance().getAnnotClassMap().get(key);
                classList.addAll(l);
            }
        } else {
            classList = (List) MMMModelFactory.getInstance().getAnnotClassMap().get(namespace);
        }
        return listConvert1(classList);
    }

    public static List listAnnotationHistory(String subjURI, boolean concise) {
        ArrayList l = new ArrayList();
        Resource ind = MMMModelFactory.getInstance().getOntModel().createResource(subjURI);
        Property p = MMMModelFactory.getInstance().getOntModel().createProperty(PROPERTY_EDIT_SUBJECT_URI);
        if ((ind != null) && (p != null)) {
            l.add(getWrappedRDFObject2(ind.getURI(), concise));
            RDFNode prev = getPropertyValue(ind, p);
            Resource prevInd = null;
            if (prev != null) {
                prevInd = MMMModelFactory.getInstance().getOntModel().createResource(prev.toString());
            }
            while (isAnnotationObject(prevInd)) {
                l.add(getWrappedRDFObject2(prevInd.getURI(), concise));
                prev = getPropertyValue(prevInd, p);
                if (prev != null) {
                    prevInd = MMMModelFactory.getInstance().getOntModel().createResource(prev.toString());
                } else {
                    prevInd = null;
                }
            }
        }
        return l;
    }

    public static List listAnnotationProperties(String classURI) {
        ArrayList l = new ArrayList();
        List l2 = (List) MMMModelFactory.getInstance().getClassMap().get(classURI);
        Iterator i = l2.iterator();
        while (i.hasNext()) {
            PropertyDescriptor pd = (PropertyDescriptor) i.next();
            String[] temp = { pd.getURI(), pd.getLabel() };
            l.add(temp);
        }
        return l;
    }

    public static List listAnnotationWrappers2(String subjURI, boolean concise) {
        ArrayList l = new ArrayList();
        Resource r = MMMModelFactory.getInstance().getOntModel().createResource(subjURI);
        Property p = MMMModelFactory.getInstance().getOntModel().createProperty(MMMModelFactory.NS_MMM_BASE, MMMModelFactory.MMM_PROP_SUBJECT_URI);
        if ((r != null) && (p != null)) {
            StmtIterator i = MMMModelFactory.getInstance().getOntModel().listStatements((Resource) null, p, r);
            while (i.hasNext()) {
                Statement s = (Statement) i.next();
                l.add(getWrappedRDFObject2(s.getSubject().getURI(), concise));
            }
        }
        Collections.sort(l, ObjectSorter.annotationWrapperByDate());
        Collections.sort(l, ObjectSorter.annotationWrapperByType());
        return l;
    }

    public static List listAssociatedObjects(String subjURI, String classURI) {
        ArrayList l = new ArrayList();
        Resource ind = MMMModelFactory.getInstance().getOntModel().createResource(subjURI);
        Property p = MMMModelFactory.getInstance().getOntModel().createProperty(PROPERTY_HAS_ASSOC_OBJ_URI);
        if (p != null) {
            StmtIterator i = MMMModelFactory.getInstance().getOntModel().listStatements(ind, p, (RDFNode) null);
            while (i.hasNext()) {
                Statement stmt = (Statement) i.next();
                String candidateURI = stmt.getObject().toString();
                Set s = (Set) MMMModelFactory.getInstance().getSuperclassMap().get(candidateURI);
                if (s != null && s.contains(classURI)) {
                    l.add(candidateURI);
                } else {
                    String typeURI = (String) MMMModelFactory.getInstance().getTypeMap().get(candidateURI);
                    s = (Set) MMMModelFactory.getInstance().getSuperclassMap().get(typeURI);
                    if (s != null && s.contains(classURI)) {
                        l.add(candidateURI);
                    }
                }
            }
        }
        return l;
    }

    private static List listConvert1(List l) {
        List rtnList = new ArrayList();
        Iterator i = l.iterator();
        while (i.hasNext()) {
            String uri = (String) i.next();
            String[] temp = convert1(uri);
            rtnList.add(temp);
        }
        return rtnList;
    }

    public static Iterator listDeclaredInstancePropertyValues(String subjectURI, Property p) {
        Resource r = MMMModelFactory.getInstance().getOntModel().createResource(subjectURI);
        List l = new ArrayList();
        if ((r != null) && (p != null)) {
            StmtIterator i = MMMModelFactory.getInstance().getOntModel().listStatements(r, p, (RDFNode) null);
            while (i.hasNext()) {
                Statement s = (Statement) i.next();
                l.add(s.getObject());
            }
        }
        return l.iterator();
    }

    public static Iterator listDeclaredInstancePropertyValues(String subjectURI, String propertyNS, String propertyName) {
        Property p = MMMModelFactory.getInstance().getOntModel().createProperty(propertyNS, propertyName);
        return listDeclaredInstancePropertyValues(subjectURI, p);
    }

    public static String setHidden(String subjectURI) {
        Resource ind = MMMModelFactory.getInstance().getOntModel().createResource(subjectURI);
        if (ind == null) {
            return null;
        }
        Property p = MMMModelFactory.getInstance().getOntModel().createProperty(PROPERTY_HIDDEN_URI);
        ind.addProperty(p, true);
        p = MMMModelFactory.getInstance().getOntModel().createProperty(PROPERTY_SUBJECT_URI);
        String rtn = getPropertyValue(ind, p).toString();
        return rtn;
    }

    public static boolean setHiddenAndUnhide(String subjectURI) {
        setHidden(subjectURI);
        Resource ind = MMMModelFactory.getInstance().getOntModel().createResource(subjectURI);
        Property pHidden = MMMModelFactory.getInstance().getOntModel().createProperty(PROPERTY_HIDDEN_URI);
        Property pEdit = MMMModelFactory.getInstance().getOntModel().createProperty(PROPERTY_EDIT_SUBJECT_URI);
        RDFNode r = getPropertyValue(ind, pEdit);
        if (r != null) {
            ind = MMMModelFactory.getInstance().getOntModel().createResource(r.toString());
            if (isAnnotationObject(ind)) {
                ind.removeAll(pHidden);
            }
        }
        return true;
    }
}

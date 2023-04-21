package it.polimi.elet.si.urbe.similarity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class OntOperations {

    private static final String TERMS = "SEMANTIC TERMS";

    private static final String DOMAIN = "DOMAIN";

    private String URI;

    private File ontologyFile;

    private String ontologyPath;

    private OntDocumentManager dm;

    private OntModel model;

    private Object[] classes;

    private String[] properties;

    private Set namespace;

    public OntOperations(String sPath) throws FileNotFoundException, IOException {
        OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_DL_MEM_TRANS_INF);
        model = ModelFactory.createOntologyModel(spec, null);
        model.getDocumentManager().addAltEntry("http://www.polimi.it/myont.owl", sPath);
        model.read(sPath);
        classes = getOntClasses();
        properties = getOntProperties();
    }

    public OntModel getModel() {
        return model;
    }

    public String getOntologyFilePath() {
        return ontologyPath;
    }

    public String getOntologyURI() {
        return URI;
    }

    public Object[] getHierarchyClasses() {
        return classes;
    }

    public String[] getProperties() {
        return properties;
    }

    public Set getNameSpace() {
        return namespace;
    }

    private Object[] getOntClasses() {
        ArrayList<Object> classes = new ArrayList<Object>();
        Iterator classIterator = model.listClasses();
        this.namespace = model.listImportedOntologyURIs(true);
        namespace.add((String) URI);
        classes.add(TERMS);
        while (classIterator.hasNext()) {
            OntClass ontClass = (OntClass) classIterator.next();
            if (!ontClass.isUnionClass() && !ontClass.isIntersectionClass() && !ontClass.isAnon() && !ontClass.isEnumeratedClass() && !ontClass.isRestriction() && !ontClass.isIndividual()) {
                String className = ontClass.getLocalName();
                {
                    ArrayList<Object> tmp = new ArrayList<Object>();
                    tmp.add(className);
                    getOntClasses(tmp, ontClass);
                    classes.add((Object[]) tmp.toArray(new Object[0]));
                }
            }
        }
        return (Object[]) classes.toArray(new Object[0]);
    }

    private void getOntClasses(ArrayList<Object> rootList, OntClass rootClass) {
        Iterator classIterator = rootClass.listSubClasses();
        while (classIterator.hasNext()) {
            OntClass ontClass = (OntClass) classIterator.next();
            if (!ontClass.isUnionClass() && !ontClass.isIntersectionClass() && !ontClass.isAnon() && !ontClass.isEnumeratedClass() && !ontClass.isRestriction() && !ontClass.isIndividual()) {
                String className = ontClass.getLocalName();
                {
                    ArrayList<Object> tmp = new ArrayList<Object>();
                    tmp.add(ontClass.getLocalName());
                    getOntClasses(tmp, ontClass);
                    rootList.add((Object[]) tmp.toArray(new Object[0]));
                }
            }
        }
    }

    private String[] getOntProperties() {
        ArrayList<String> properties = new ArrayList<String>();
        Iterator propIterator = model.listDatatypeProperties();
        while (propIterator.hasNext()) {
            OntProperty prop = (OntProperty) propIterator.next();
            properties.add(prop.getLocalName());
        }
        propIterator = model.listObjectProperties();
        while (propIterator.hasNext()) {
            OntProperty prop = (OntProperty) propIterator.next();
            properties.add(prop.getLocalName());
        }
        String[] ret = properties.toArray(new String[0]);
        for (int i = 0; i < ret.length - 1; i++) {
            for (int j = i + 1; j < ret.length; j++) {
                if (ret[i].compareToIgnoreCase(ret[j]) > 0) {
                    String tmp = ret[i];
                    ret[i] = ret[j];
                    ret[j] = tmp;
                }
            }
        }
        return ret;
    }

    public Object[] getDomainClasses(String propName) {
        OntProperty prop;
        prop = model.getDatatypeProperty(URI + "#" + propName);
        if (prop == null) prop = model.getObjectProperty(URI + "#" + propName);
        if (prop != null) {
            ArrayList<Object> classes = new ArrayList<Object>();
            Iterator domain = prop.listDomain();
            classes.add(DOMAIN);
            while (domain.hasNext()) {
                OntClass classe = (OntClass) domain.next();
                if (classe.isUnionClass()) {
                    Iterator union = classe.asUnionClass().listOperands();
                    while (union.hasNext()) {
                        OntClass unionClass = (OntClass) union.next();
                        ArrayList<Object> tmp = new ArrayList<Object>();
                        tmp.add(unionClass.getLocalName());
                        getOntClasses(tmp, unionClass);
                        classes.add((Object[]) tmp.toArray(new Object[0]));
                    }
                } else {
                    ArrayList<Object> tmp = new ArrayList<Object>();
                    tmp.add(classe.getLocalName());
                    getOntClasses(tmp, classe);
                    classes.add((Object[]) tmp.toArray(new Object[0]));
                }
            }
            return (Object[]) classes.toArray(new Object[0]);
        } else {
            return null;
        }
    }

    public String[] getCommentsOntClass(String className) {
        ArrayList<String> commentsArray = new ArrayList<String>(1);
        OntClass ontClass = model.getOntClass(URI + "#" + className);
        if (ontClass != null) {
            Iterator comments = ontClass.listComments(null);
            while (comments.hasNext()) {
                Literal comment = (Literal) comments.next();
                commentsArray.add(comment.getString());
            }
            if (!commentsArray.isEmpty()) return (String[]) commentsArray.toArray(new String[0]);
        }
        return null;
    }
}

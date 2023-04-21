package br.ufpe.cin.ontocompo.infra.checker;

import br.ufpe.cin.ontocompo.infra.loader.ILoader;
import br.ufpe.cin.ontocompo.infra.loader.OntologyLoader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLClassAxiom;
import org.semanticweb.owl.model.OWLEntity;

/**
 * This class is responsible for validations from imported classes.
 * @author Camila Bezerra
 * @date Jan 21, 2008
 */
public class ClassChecker implements IChecker {

    private OWLClass classz;

    private List<String> classes = new ArrayList();

    private List<String> properties = new ArrayList();

    private ILoader loader;

    /**
     * Creates a new instance of ClassChecker
     */
    public ClassChecker() {
        loader = OntologyLoader.getInstance();
    }

    /**
     * Creates a new instance of ClassChecker
     * @param uri physical path ontology
     */
    public ClassChecker(URI uri) {
        loader = new OntologyLoader(uri);
    }

    /**
     * Validate the imported class using some criterions.
     * @param o the class
     * @param classesList the imported classes
     * @param propertiesList the imported properties
     */
    public String validate(Object o, Collection classesList, Collection propertiesList) {
        classz = (OWLClass) o;
        for (Iterator<OWLEntity> it = classesList.iterator(); it.hasNext(); ) {
            OWLEntity oWLEntity = (OWLEntity) it.next();
            this.classes.add(oWLEntity.toString());
        }
        for (Iterator<OWLEntity> it = propertiesList.iterator(); it.hasNext(); ) {
            OWLEntity oWLEntity = (OWLEntity) it.next();
            this.properties.add(oWLEntity.toString());
        }
        return checkerValuesRestriction();
    }

    /**
     * Validate the <I>ObjectSomeValueFrom</I> and <I>ObjectAllValuesFrom</I>
     * axioms from imported classes.
     * @return validation message
     */
    private String checkerValuesRestriction() {
        String output = "";
        Dictionary dicProperties = new Hashtable();
        List axioms = new ArrayList();
        for (Iterator<OWLClassAxiom> it = loader.getClassAxioms(classz).iterator(); it.hasNext(); ) {
            OWLClassAxiom axiom = it.next();
            String s = axiom.toString();
            if (s.contains("ObjectSomeValueFrom") || s.contains("ObjectAllValuesFrom")) {
                int indexi = s.indexOf("From");
                int indexf = s.indexOf(" ", indexi + 5);
                String propertyStr = s.substring(indexi + 5, indexf);
                if (!this.properties.contains(propertyStr)) {
                    if (dicProperties.get(propertyStr) != null) {
                        axioms = (ArrayList) dicProperties.get(propertyStr);
                        axioms.add(s);
                        dicProperties.put(propertyStr, axioms);
                    } else {
                        dicProperties = new Hashtable();
                        axioms.add(s);
                        dicProperties.put(propertyStr, axioms);
                    }
                }
                if (s.contains("ObjectUnionOf")) {
                    indexi = s.indexOf("UnionOf(");
                    indexf = s.indexOf(")", indexi + 8);
                    String str = s.substring(indexi + 9, indexf);
                    String[] classes = str.split(" ");
                    output = output + "It`s needed to add ";
                    for (int i = 0; i < classes.length; i++) {
                        String cls = classes[i];
                        if (!this.classes.contains(cls) && i < classes.length - 1) {
                            output = output + cls + ", ";
                        } else {
                            output = output + cls;
                        }
                    }
                    output = output + " classes because of axiom : \n" + axiom + "\n";
                }
            }
        }
        Enumeration keys = dicProperties.keys();
        Enumeration elements = dicProperties.elements();
        while (keys.hasMoreElements()) {
            String propertyStr = (String) keys.nextElement();
            String axiom = this.arrayListoString((ArrayList) elements.nextElement());
            output = output + "It`s needed to add " + propertyStr + "  property  because of axiom : \n" + axiom + "\n";
        }
        return output;
    }

    private String arrayListoString(ArrayList arrayList) {
        String ret = "";
        Iterator ite = arrayList.iterator();
        while (ite.hasNext()) {
            ret = ret + (String) (ite.next()) + "\n";
        }
        return ret;
    }
}

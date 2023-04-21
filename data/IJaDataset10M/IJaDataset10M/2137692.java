package net.sourceforge.fluxion.runcible.graph.utils;

import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerAdapter;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.*;
import org.semanticweb.owl.vocab.OWLRDFVocabulary;
import java.util.*;

/**
 * A utils class that provides functionality to evaluate the possible set of
 * restrictions that can be applied to any class held in a reaonser.  To use
 * this class, you should pass an {@link OWLReasoner} that has been loaded and
 * classified with the set of required ontologies.  Calling the {@link
 * #evaluate} method will then calculate the possible restrictions that can be
 * applied to the defined classes in the reasoner.
 *
 * @author Tony Burdett
 * @date 08-Feb-2009
 */
public class OWLRestrictionEvaluator {

    /**
   * Evaluates the possible set of existential relationships that can be applied
   * to every defined class that has been classified by the reasoner.  This will
   * populate a map, with the keys being each classified defined class in the
   * reasoner and the values being the set of restrictions it is possible to
   * make on this class.
   * <p/>
   * In effect, this means that for each defined class, this method inspects
   * every declared property - which will be termed "Property P" - and
   * determines if the intersection of all its domains and the defined class is
   * non-empty.  If this evaluates to true, it constructs a "some" restriction
   * on the inverse of this property with a value of the defined class - in
   * other words, a restriction that describes the set of individuals that have
   * some value for the inverse property that is an instance of the defined
   * class.  It then determines the most specific single superclass of this
   * restriction, which will be termed "Class C".  Finally, a creates a new
   * {@link org.semanticweb.owl.model.OWLObjectSomeRestriction} or {@link
   * org.semanticweb.owl.model.OWLDataSomeRestriction}, depending on the
   * property type, where the property is Property P and the class is Class C.
   * This restriction is added to the map.
   * <p/>
   * The result of this is that a client can call the evaluate method, and then
   * lookup the possible set of all inferrable "Some" restrictions in an
   * ontology, by the class they can possibly be applied to.  This enables the
   * construction of mapping rules - every "some" restriction represents a
   * possible walk operation.  Of course, once walks are combined many levels
   * down a tree, other more specific inferances might be possible, and these
   * will not be considered by this method.  This means callers should be aware
   * that if building rules that contain deep trees, some nodes may represent
   * empty sets.
   *
   * @param reasoner the reasoner that contains the ontologies being used
   * @return a map of owl classes to restrictions that represent the possible
   *         walk operations
   * @throws org.semanticweb.owl.inference.OWLReasonerException
   *          if the reasoner fails
   */
    public static Map<OWLClass, Set<OWLRestriction>> evaluate(OWLReasoner reasoner) throws OWLReasonerException {
        Map<OWLClass, Set<OWLRestriction>> evalMap = new HashMap<OWLClass, Set<OWLRestriction>>();
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLDataFactory factory = manager.getOWLDataFactory();
        Set<OWLOntology> ontologies = reasoner.getLoadedOntologies();
        Set<OWLClass> classes = new HashSet<OWLClass>();
        for (OWLOntology ontology : ontologies) {
            classes.addAll(ontology.getReferencedClasses());
        }
        Set<OWLObjectProperty> objectProperties = new HashSet<OWLObjectProperty>();
        for (OWLOntology ontology : ontologies) {
            objectProperties.addAll(ontology.getReferencedObjectProperties());
        }
        Set<OWLDataProperty> dataProperties = new HashSet<OWLDataProperty>();
        for (OWLOntology ontology : ontologies) {
            dataProperties.addAll(ontology.getReferencedDataProperties());
        }
        for (OWLClass cls : classes) {
            evalMap.put(cls, new HashSet<OWLRestriction>());
        }
        for (OWLClass cls : classes) {
            Set<OWLRestriction> restrictions = new HashSet<OWLRestriction>();
            for (OWLObjectProperty prop : objectProperties) {
                Set<OWLDescription> domains = new HashSet<OWLDescription>();
                Set ambiguousDomains = reasoner.getDomains(prop);
                for (Object o : ambiguousDomains) {
                    OWLDescription d = (OWLDescription) o;
                    domains.add(d);
                }
                OWLObjectUnionOf domUnion = factory.getOWLObjectUnionOf(domains);
                OWLObjectIntersectionOf intersect = factory.getOWLObjectIntersectionOf(domUnion, cls);
                if (reasoner.isSatisfiable(intersect)) {
                    Set<OWLObjectProperty> inverseProperties = OWLReasonerAdapter.flattenSetOfSets(reasoner.getInverseProperties(prop));
                    for (OWLObjectProperty inverseProperty : inverseProperties) {
                        OWLObjectSomeRestriction inverseRestriction = factory.getOWLObjectSomeRestriction(inverseProperty, cls);
                        if (reasoner.isSatisfiable(inverseRestriction)) {
                            Set<OWLClass> superclasses = OWLReasonerAdapter.flattenSetOfSets(reasoner.getSuperClasses(inverseRestriction));
                            OWLClass commonClass = getMostSpecificCommonClass(reasoner, superclasses);
                            OWLObjectSomeRestriction restriction = factory.getOWLObjectSomeRestriction(prop, commonClass);
                            restrictions.add(restriction);
                        }
                    }
                }
            }
            for (OWLDataProperty prop : dataProperties) {
                Set<OWLDescription> domains = OWLReasonerAdapter.flattenSetOfSets(reasoner.getDomains(prop));
                OWLObjectUnionOf domUnion = factory.getOWLObjectUnionOf(domains);
                OWLObjectIntersectionOf intersect = factory.getOWLObjectIntersectionOf(domUnion, cls);
                if (reasoner.isSatisfiable(intersect)) {
                    Set<OWLDataRange> ranges = reasoner.getRanges(prop);
                    if (ranges.size() > 1) {
                        OWLDataSomeRestriction restriction = factory.getOWLDataSomeRestriction(prop, factory.getTopDataType());
                        restrictions.add(restriction);
                    } else {
                        OWLDataSomeRestriction restriction = factory.getOWLDataSomeRestriction(prop, ranges.iterator().next());
                        restrictions.add(restriction);
                    }
                }
            }
            evalMap.put(cls, restrictions);
        }
        return evalMap;
    }

    public static OWLClass getMostSpecificCommonClass(OWLReasoner reasoner, Set<OWLClass> classes) throws OWLReasonerException {
        Set<OWLClass> commonAncestors;
        if (classes.size() > 1) {
            Iterator<OWLClass> clsIt = classes.iterator();
            OWLClass cls = clsIt.next();
            commonAncestors = OWLReasonerAdapter.flattenSetOfSets(reasoner.getAncestorClasses(cls));
            commonAncestors.add(cls);
            while (clsIt.hasNext()) {
                OWLClass nextCls = clsIt.next();
                Set<OWLClass> ancestors = OWLReasonerAdapter.flattenSetOfSets(reasoner.getAncestorClasses(nextCls));
                ancestors.add(nextCls);
                Set<OWLClass> copyAncestors = new HashSet<OWLClass>();
                for (OWLClass oc : commonAncestors) {
                    copyAncestors.add(oc);
                }
                for (OWLClass nextCommonAnc : copyAncestors) {
                    if (!ancestors.contains(nextCommonAnc)) {
                        commonAncestors.remove(nextCommonAnc);
                    }
                }
            }
            Set<OWLClass> copyAncestors = new HashSet<OWLClass>();
            for (OWLClass oc : commonAncestors) {
                copyAncestors.add(oc);
            }
            for (OWLClass ca1 : copyAncestors) {
                for (OWLClass ca2 : copyAncestors) {
                    if (reasoner.isSubClassOf(ca1, ca2)) {
                        commonAncestors.remove(ca2);
                    }
                }
            }
            if (commonAncestors.size() == 1) {
                return commonAncestors.iterator().next();
            } else {
                OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
                OWLDataFactory factory = manager.getOWLDataFactory();
                return factory.getOWLClass(OWLRDFVocabulary.OWL_THING.getURI());
            }
        } else if (classes.size() == 1) {
            return classes.iterator().next();
        } else {
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            OWLDataFactory factory = manager.getOWLDataFactory();
            return factory.getOWLClass(OWLRDFVocabulary.OWL_THING.getURI());
        }
    }
}

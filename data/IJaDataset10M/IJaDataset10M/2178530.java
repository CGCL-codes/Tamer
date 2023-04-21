package de.tudresden.inf.lat.jcel.core.algorithm.rulebased;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import de.tudresden.inf.lat.jcel.core.completion.basic.CR1Rule;
import de.tudresden.inf.lat.jcel.core.completion.basic.CR2Rule;
import de.tudresden.inf.lat.jcel.core.completion.basic.CR3Rule;
import de.tudresden.inf.lat.jcel.core.completion.basic.CR4RRule;
import de.tudresden.inf.lat.jcel.core.completion.basic.CR4SRule;
import de.tudresden.inf.lat.jcel.core.completion.basic.CR5Rule;
import de.tudresden.inf.lat.jcel.core.completion.basic.CR6Rule;
import de.tudresden.inf.lat.jcel.core.completion.basic.CRBottomRule;
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.SObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.ext.CR3ExtRule;
import de.tudresden.inf.lat.jcel.core.completion.ext.CR4RExtRule;
import de.tudresden.inf.lat.jcel.core.completion.ext.CR4SExtRule;
import de.tudresden.inf.lat.jcel.core.completion.ext.CR5ExtRule;
import de.tudresden.inf.lat.jcel.core.completion.ext.CR6RExtRule;
import de.tudresden.inf.lat.jcel.core.completion.ext.CR6SExtRule;
import de.tudresden.inf.lat.jcel.core.completion.ext.CR7ExtRule;
import de.tudresden.inf.lat.jcel.core.completion.ext.CR8RExtRule;
import de.tudresden.inf.lat.jcel.core.completion.ext.CR8SExtRule;
import de.tudresden.inf.lat.jcel.core.completion.ext.CR9ExtOptRule;
import de.tudresden.inf.lat.jcel.core.saturation.SubPropertyNormalizer;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.ComplexAxiomExpressivityDetector;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.OntologyExpressivity;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.ExtendedOntology;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.ExtendedOntologyImpl;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerEntityManager;
import de.tudresden.inf.lat.jcel.ontology.normalization.OntologyNormalizer;

/**
 * An object of this class preprocesses an ontology. This preprocessing includes
 * <ul>
 * <li>finding the class identifiers and object property identifiers</li>
 * <li>creating an identifier generator for the auxiliary classes and object
 * properties</li>
 * <li>detecting the required expressivity for the ontology</li>
 * <li>creating appropriate completion rule chains to process the ontology</li>
 * <li>normalizing the ontology</li>
 * <li>extending the ontology</li>
 * </ul>
 * 
 * @author Julian Mendez
 * 
 */
public class OntologyPreprocessor {

    private RChain chainR = new RChain(new ArrayList<RObserverRule>());

    private SChain chainS = new SChain(new ArrayList<SObserverRule>());

    private final OntologyExpressivity expressivityDetector;

    private ExtendedOntology extendedOntology = new ExtendedOntologyImpl();

    private final IntegerOntologyObjectFactory ontologyObjectFactory;

    private Set<Integer> originalClassSet = new HashSet<Integer>();

    private Set<Integer> originalObjectPropertySet = new HashSet<Integer>();

    /**
	 * Constructs a new ontology preprocessor.
	 * 
	 * @param axiomSet
	 *            set of axioms
	 */
    public OntologyPreprocessor(Set<ComplexIntegerAxiom> axiomSet, IntegerOntologyObjectFactory factory) {
        if (axiomSet == null) {
            throw new IllegalArgumentException("Null argument.");
        }
        if (factory == null) {
            throw new IllegalArgumentException("Null argument.");
        }
        this.ontologyObjectFactory = factory;
        this.originalClassSet.add(IntegerEntityManager.bottomClassId);
        this.originalClassSet.add(IntegerEntityManager.topClassId);
        this.originalObjectPropertySet.add(IntegerEntityManager.bottomObjectPropertyId);
        this.originalObjectPropertySet.add(IntegerEntityManager.topObjectPropertyId);
        this.expressivityDetector = new ComplexAxiomExpressivityDetector(axiomSet);
        preProcess(axiomSet);
    }

    private void activateBottomRules() {
        List<RObserverRule> listR = new ArrayList<RObserverRule>();
        listR.addAll(this.chainR.getList());
        listR.add(new CRBottomRule());
        this.chainR = new RChain(listR);
    }

    private void activateExtendedRules() {
        List<SObserverRule> listS = new ArrayList<SObserverRule>();
        listS.addAll(this.chainS.getList());
        listS.add(new CR1Rule());
        listS.add(new CR2Rule());
        listS.add(new CR3ExtRule());
        listS.add(new CR4SExtRule());
        listS.add(new CR6SExtRule());
        listS.add(new CR8SExtRule());
        this.chainS = new SChain(listS);
        List<RObserverRule> listR = new ArrayList<RObserverRule>();
        listR.addAll(this.chainR.getList());
        listR.add(new CR4RExtRule());
        listR.add(new CR5ExtRule());
        listR.add(new CR6RExtRule());
        listR.add(new CR7ExtRule());
        listR.add(new CR8RExtRule());
        listR.add(new CR9ExtOptRule());
        this.chainR = new RChain(listR);
    }

    /**
	 * Activates a profiler for the completion rule chains.
	 */
    public void activateProfiler() {
        List<SObserverRule> listS = this.chainS.getList();
        List<SObserverRule> listSWithProfiler = new ArrayList<SObserverRule>();
        for (SObserverRule current : listS) {
            listSWithProfiler.add(new RuleProfiler(current));
        }
        this.chainS = new SChain(listSWithProfiler);
        List<RObserverRule> listR = this.chainR.getList();
        List<RObserverRule> listRWithProfiler = new ArrayList<RObserverRule>();
        for (RObserverRule current : listR) {
            listRWithProfiler.add(new RuleProfiler(current));
        }
        this.chainR = new RChain(listRWithProfiler);
    }

    private void activateSimpleRules() {
        List<SObserverRule> listS = new ArrayList<SObserverRule>();
        listS.addAll(this.chainS.getList());
        listS.add(new CR1Rule());
        listS.add(new CR2Rule());
        listS.add(new CR3Rule());
        listS.add(new CR4SRule());
        this.chainS = new SChain(listS);
        List<RObserverRule> listR = new ArrayList<RObserverRule>();
        listR.addAll(this.chainR.getList());
        listR.add(new CR4RRule());
        listR.add(new CR5Rule());
        listR.add(new CR6Rule());
        this.chainR = new RChain(listR);
    }

    /**
	 * Returns the ontology expressivity.
	 * 
	 * @return the ontology expressivity
	 */
    public OntologyExpressivity getExpressivity() {
        return this.expressivityDetector;
    }

    /**
	 * Returns the extended ontology.
	 * 
	 * @return the extended ontology
	 */
    public ExtendedOntology getExtendedOntology() {
        return this.extendedOntology;
    }

    private OntologyExpressivity getOntologyExpressivity() {
        return this.expressivityDetector;
    }

    /**
	 * Returns the ontology object factory.
	 * 
	 * @return the ontology object factory
	 */
    public IntegerOntologyObjectFactory getOntologyObjectFactory() {
        return this.ontologyObjectFactory;
    }

    /**
	 * Returns the set of classes that were before the normalization.
	 * 
	 * @return the set of classes that were before the normalization
	 */
    public Set<Integer> getOriginalClassSet() {
        return this.originalClassSet;
    }

    /**
	 * Returns the set of object properties that were before the normalization.
	 * 
	 * @return the set of object properties that were before the normalization
	 */
    public Set<Integer> getOriginalObjectPropertySet() {
        return this.originalObjectPropertySet;
    }

    /**
	 * Returns the completion rule chain for the set of relations.
	 * 
	 * @return the completion rule chain for the set of relations.
	 */
    public RChain getRChain() {
        return this.chainR;
    }

    /**
	 * Returns the completion rule chain for the set of subsumers.
	 * 
	 * @return the completion rule chain for the set of subsumers
	 */
    public SChain getSChain() {
        return this.chainS;
    }

    private void preProcess(Set<ComplexIntegerAxiom> axiomSet) {
        for (ComplexIntegerAxiom axiom : axiomSet) {
            this.originalClassSet.addAll(axiom.getClassesInSignature());
            this.originalObjectPropertySet.addAll(axiom.getObjectPropertiesInSignature());
        }
        if (getOntologyExpressivity().hasInverseObjectProperty() || getOntologyExpressivity().hasFunctionalObjectProperty()) {
            activateExtendedRules();
        } else {
            activateSimpleRules();
        }
        if (getOntologyExpressivity().hasBottom()) {
            activateBottomRules();
        }
        OntologyNormalizer axiomNormalizer = new OntologyNormalizer();
        SubPropertyNormalizer subPropNormalizer = new SubPropertyNormalizer(getOntologyObjectFactory());
        this.extendedOntology.load(subPropNormalizer.apply(axiomNormalizer.normalize(axiomSet, getOntologyObjectFactory())));
        for (Integer elem : this.originalObjectPropertySet) {
            this.extendedOntology.addObjectProperty(elem);
        }
        for (Integer elem : this.originalClassSet) {
            this.extendedOntology.addClass(elem);
        }
    }
}

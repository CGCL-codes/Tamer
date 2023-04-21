package de.tudresden.inf.lat.jcel.ontology.axiom.normalized;

import java.util.Set;

/**
 * This is the interface of an extended ontology provides methods to efficiently
 * retrieve axioms from the ontology.
 * 
 * @author Julian Mendez
 */
public interface ExtendedOntology {

    /**
	 * Adds a class to the ontology.
	 * 
	 * @param classId
	 *            class to be added
	 */
    public void addClass(Integer classId);

    /**
	 * Adds an object property to the ontology.
	 * 
	 * @param objectPropertyId
	 *            object property to be added
	 */
    public void addObjectProperty(Integer objectPropertyId);

    /**
	 * Clears the ontology.
	 */
    public void clear();

    /**
	 * Returns the set of axioms in this ontology.
	 * 
	 * @return the set of axioms in this ontology
	 */
    public Set<NormalizedIntegerAxiom> getAxiomSet();

    /**
	 * Returns the set of all classes in the ontology.
	 * 
	 * @return the set of all classes in the ontology
	 */
    public Set<Integer> getClassSet();

    /**
	 * Returns the set of all functional object properties in the ontology.
	 * 
	 * @return the set of all functional object properties in the ontology
	 */
    public Set<Integer> getFunctionalObjectProperties();

    /**
	 * Returns the set of all axioms GCI0 in the ontology such that the given
	 * class occurs in the left part of the axiom.
	 * 
	 * @param classId
	 *            class identifier occurring in the left part of the axiom
	 * 
	 * @return the set of all axioms GCI0 in the ontology such that the given
	 *         class occurs in the left part of the axiom
	 */
    public Set<GCI0Axiom> getGCI0Axioms(Integer classId);

    /**
	 * Returns the set of all axioms GCI1 in the ontology such that the given
	 * class occurs in the left part of the axiom.
	 * 
	 * @param classId
	 *            class identifier occurring in the left part of the axiom
	 * 
	 * @return the set of all axioms GCI1 in the ontology such that the given
	 *         class occurs in the left part of the axiom
	 */
    public Set<GCI1Axiom> getGCI1Axioms(Integer classId);

    /**
	 * Returns the set of all axioms GCI2 in the ontology such that the given
	 * class occurs in the left part of the axiom.
	 * 
	 * @param classId
	 *            class identifier occurring in the left part of the axiom
	 * 
	 * @return the set of all axioms GCI2 in the ontology such that the given
	 *         class occurs in the left part of the axiom
	 */
    public Set<GCI2Axiom> getGCI2Axioms(Integer classId);

    /**
	 * Returns the set of all axioms GCI3 in the ontology such that the given
	 * class occurs in the left part of the axiom.
	 * 
	 * @param classId
	 *            class identifier occurring in the left part of the axiom
	 * 
	 * @return the set of all axioms GCI3 in the ontology such that the given
	 *         class occurs in the left part of the axiom
	 */
    public Set<GCI3Axiom> getGCI3AAxioms(Integer classId);

    /**
	 * Returns the set of all axioms GCI3 in the ontology such that the given
	 * classes occur in the axiom.
	 * 
	 * @param leftClassId
	 *            class identifier occurring in the left part of the axiom
	 * @param rightClassId
	 *            class identifier occurring in the right part of the axiom
	 * 
	 * @return the set of all axioms GCI3 in the ontology such that the given
	 *         classes occur in the axiom
	 */
    public Set<GCI3Axiom> getGCI3ABAxioms(Integer leftClassId, Integer rightClassId);

    /**
	 * Returns the set of all axioms GCI3 in the ontology such that the given
	 * object property and the given class occur in the left part of the axiom.
	 * 
	 * @param objectPropertyId
	 *            object property identifier occurring in the left part of the
	 *            axiom
	 * @param leftClassId
	 *            class identifier occurring in the left part of the axiom
	 * 
	 * @return the set of all axioms GCI3 in the ontology such that the given
	 *         object property and the given class occur in the left part of the
	 *         axiom
	 */
    public Set<GCI3Axiom> getGCI3rAAxioms(Integer objectPropertyId, Integer leftClassId);

    /**
	 * Returns the set of all axioms GCI3 in the ontology such that the given
	 * object property occurs in the axiom.
	 * 
	 * @param objectPropertyId
	 *            object property identifier occurring in the axiom
	 * 
	 * @return the set of all axioms GCI3 in the ontology such that the given
	 *         object property occurs in the axiom
	 */
    public Set<GCI3Axiom> getGCI3rAxioms(Integer objectPropertyId);

    /**
	 * Returns the set of all axioms GCI3 in the ontology such that the given
	 * object property occurs in left part of the axiom and the given class
	 * occurs in the right part of the axiom.
	 * 
	 * @param objectPropertyId
	 *            object property identifier occurring in the left part of the
	 *            axiom
	 * @param rightClassId
	 *            class identifier occurring in the right part of the axiom
	 * 
	 * @return the set of all axioms GCI3 in the ontology such that the given
	 *         object property occurs in the left part of the axiom and the
	 *         given class occur in the right part of the axiom
	 */
    public Set<GCI3Axiom> getGCI3rBAxioms(Integer objectPropertyId, Integer rightClassId);

    /**
	 * Returns the set of all object properties in the ontology.
	 * 
	 * @return the set of all object properties in the ontology
	 */
    public Set<Integer> getObjectPropertySet();

    /**
	 * Returns the set of all reflexive object properties in the ontology.
	 * 
	 * @return the set of all reflexive object properties in the ontology
	 */
    public Set<Integer> getReflexiveObjectProperties();

    /**
	 * Returns the set of all axioms RI2 in the ontology such that the given
	 * object property occurs in the left part of the axiom.
	 * 
	 * @param objectPropertyId
	 *            object property identifier occurring in the left part of the
	 *            axiom
	 * 
	 * @return the set of all axioms RI2 in the ontology such that the given
	 *         object property occurs in the left part of the axiom
	 */
    public Set<RI2Axiom> getRI2rAxioms(Integer objectPropertyId);

    /**
	 * Returns the set of all axioms RI2 in the ontology such that the given
	 * object property occurs in the right part of the axiom.
	 * 
	 * @param objectPropertyId
	 *            object property identifier occurring in the right part of the
	 *            axiom
	 * 
	 * @return the set of all axioms RI2 in the ontology such that the given
	 *         object property occurs in the right part of the axiom
	 */
    public Set<RI2Axiom> getRI2sAxioms(Integer objectPropertyId);

    /**
	 * Returns the set of all axioms RI3 in the ontology such that the given
	 * object property occurs in the left part of the object property
	 * composition, in the left part of the axiom.
	 * 
	 * @param objectPropertyId
	 *            object property identifier occurring in the left part of the
	 *            composition, in the left part of the axiom
	 * 
	 * @return the set of all axioms RI3 in the ontology such that the given
	 *         object property occurs in the left part of the object property
	 *         composition, in the left part of the axiom
	 */
    public Set<RI3Axiom> getRI3AxiomsByLeft(Integer objectPropertyId);

    /**
	 * Returns the set of all axioms RI3 in the ontology such that the given
	 * object property occurs in the right part of the object property
	 * composition, in the left part of the axiom.
	 * 
	 * @param objectPropertyId
	 *            object property identifier occurring in the right part of the
	 *            composition, in the left part of the axiom
	 * 
	 * @return the set of all axioms RI3 in the ontology such that the given
	 *         object property occurs in the right part of the object property
	 *         composition, in the left part of the axiom
	 */
    public Set<RI3Axiom> getRI3AxiomsByRight(Integer objectPropertyId);

    /**
	 * Returns the set of all transitive object properties in the ontology.
	 * 
	 * @return the set of all transitive object properties in the ontology
	 */
    public Set<Integer> getTransitiveObjectProperties();

    /**
	 * Loads a set of normalized axioms.
	 * 
	 * @param axiomSet
	 *            set of normalized axioms to be loaded
	 */
    public void load(Set<NormalizedIntegerAxiom> axiomSet);
}

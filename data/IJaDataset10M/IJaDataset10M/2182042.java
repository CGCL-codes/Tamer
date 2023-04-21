package org.omg.TerminologyServices;

/**
 * Interface definition: Systemization.
 * 
 * @author OpenORB Compiler
 */
public class SystemizationPOATie extends SystemizationPOA {

    private SystemizationOperations _tie;

    private org.omg.PortableServer.POA _poa;

    /**
     * Constructor
     */
    public SystemizationPOATie(SystemizationOperations tieObject) {
        _tie = tieObject;
    }

    /**
     * Constructor
     */
    public SystemizationPOATie(SystemizationOperations tieObject, org.omg.PortableServer.POA poa) {
        _tie = tieObject;
        _poa = poa;
    }

    /**
     * Get the delegate
     */
    public SystemizationOperations _delegate() {
        return _tie;
    }

    /**
     * Set the delegate
     */
    public void _delegate(SystemizationOperations delegate_) {
        _tie = delegate_;
    }

    /**
     * _default_POA method
     */
    public org.omg.PortableServer.POA _default_POA() {
        if (_poa != null) return _poa; else return super._default_POA();
    }

    /**
     * Read accessor for systemization_id attribute
     */
    public String systemization_id() {
        return _tie.systemization_id();
    }

    /**
     * Read accessor for coding_scheme_version attribute
     */
    public org.omg.TerminologyServices.CodingSchemeVersion coding_scheme_version() {
        return _tie.coding_scheme_version();
    }

    /**
     * Operation get_association_ids
     */
    public String[] get_association_ids() {
        return _tie.get_association_ids();
    }

    /**
     * Operation get_association_definition
     */
    public org.omg.TerminologyServices.AssociationDef get_association_definition(String association_id) throws org.omg.TerminologyServices.AssociationNotInSystemization {
        return _tie.get_association_definition(association_id);
    }

    /**
     * Operation list_all_association_instances
     */
    public void list_all_association_instances(int how_many, org.omg.TerminologyServices.AssociationInstanceSeqHolder association_instance_seq, org.omg.TerminologyServices.AssociationInstanceIterHolder association_instance_iter) {
        _tie.list_all_association_instances(how_many, association_instance_seq, association_instance_iter);
    }

    /**
     * Operation are_entities_associated
     */
    public org.omg.TerminologyServices.Trinary are_entities_associated(String source_code, org.omg.TerminologyServices.AssociatableElement target_element, String association_id, boolean direct_only) throws org.omg.TerminologyServices.AssociationNotInSystemization {
        return _tie.are_entities_associated(source_code, target_element, association_id, direct_only);
    }

    /**
     * Operation could_association_be_inferred
     */
    public org.omg.TerminologyServices.Trinary could_association_be_inferred(String source_code, org.omg.TerminologyServices.AssociatableElement target_element, String association_id) throws org.omg.TerminologyServices.AssociationNotInSystemization, org.omg.TerminologyServices.NotImplemented {
        return _tie.could_association_be_inferred(source_code, target_element, association_id);
    }

    /**
     * Operation list_associated_target_entities
     */
    public void list_associated_target_entities(String source_code, String association_id, boolean direct_only, int how_many, org.omg.TerminologyServices.TargetElementSeqSeqHolder related_elements, org.omg.TerminologyServices.TargetElementSeqIterHolder related_elements_iter) throws org.omg.TerminologyServices.AssociationNotInSystemization {
        _tie.list_associated_target_entities(source_code, association_id, direct_only, how_many, related_elements, related_elements_iter);
    }

    /**
     * Operation list_associated_source_codes
     */
    public void list_associated_source_codes(org.omg.TerminologyServices.AssociatableElement target_element, String association_id, boolean direct_only, int how_many, org.omg.TerminologyServices.ConceptInfoSeqHolder concept_info_seq, org.omg.TerminologyServices.ConceptInfoIterHolder concept_info_iter) throws org.omg.TerminologyServices.AssociationNotInSystemization {
        _tie.list_associated_source_codes(target_element, association_id, direct_only, how_many, concept_info_seq, concept_info_iter);
    }

    /**
     * Operation get_entity_graph
     */
    public org.omg.TerminologyServices.GraphEntry[] get_entity_graph(org.omg.TerminologyServices.AssociatableElement root_node, String association_id, org.omg.TerminologyServices.AssociationRole node_one_role, boolean direct_only) throws org.omg.TerminologyServices.AssociationNotInSystemization, org.omg.TerminologyServices.NotImplemented, org.omg.TerminologyServices.TooManyToList {
        return _tie.get_entity_graph(root_node, association_id, node_one_role, direct_only);
    }

    /**
     * Operation get_associations_for_source
     */
    public String[] get_associations_for_source(String source_code) {
        return _tie.get_associations_for_source(source_code);
    }

    /**
     * Operation get_associations_for_target
     */
    public String[] get_associations_for_target(org.omg.TerminologyServices.AssociatableElement target_element) {
        return _tie.get_associations_for_target(target_element);
    }

    /**
     * Operation validate_concept_expression
     */
    public org.omg.TerminologyServices.ValidationResult validate_concept_expression(org.omg.TerminologyServices.ConceptExpressionElement[] expression) throws org.omg.TerminologyServices.InvalidExpression, org.omg.TerminologyServices.NotImplemented, org.omg.TerminologyServices.AssociationNotInSystemization {
        return _tie.validate_concept_expression(expression);
    }

    /**
     * Operation get_simplest_form
     */
    public org.omg.TerminologyServices.ConceptExpressionElement[] get_simplest_form(org.omg.TerminologyServices.ConceptExpressionElement[] expression) throws org.omg.TerminologyServices.InvalidExpression, org.omg.TerminologyServices.NotImplemented, org.omg.TerminologyServices.AssociationNotInSystemization {
        return _tie.get_simplest_form(expression);
    }

    /**
     * Operation expand_concept
     */
    public org.omg.TerminologyServices.ConceptExpressionElement[] expand_concept(String concept, org.omg.TerminologyServices.QualifiedCode[] association_qualifier_seq) throws org.omg.TerminologyServices.ConceptNotExpandable, org.omg.TerminologyServices.UnknownCodingScheme, org.omg.TerminologyServices.NotImplemented, org.omg.TerminologyServices.AssociationNotInSystemization {
        return _tie.expand_concept(concept, association_qualifier_seq);
    }

    /**
     * Operation are_expressions_equivalent
     */
    public org.omg.TerminologyServices.Trinary are_expressions_equivalent(org.omg.TerminologyServices.ConceptExpressionElement[] expression1, org.omg.TerminologyServices.ConceptExpressionElement[] expression2) throws org.omg.TerminologyServices.InvalidExpression, org.omg.TerminologyServices.UnknownCodingScheme, org.omg.TerminologyServices.AssociationNotInSystemization, org.omg.TerminologyServices.NotImplemented, org.omg.TerminologyServices.UnableToEvaluate {
        return _tie.are_expressions_equivalent(expression1, expression2);
    }

    /**
     * Operation are_expression_difference
     */
    public org.omg.TerminologyServices.ConceptExpressionElement[] are_expression_difference(org.omg.TerminologyServices.ConceptExpressionElement[] expression1, org.omg.TerminologyServices.ConceptExpressionElement[] expression2) throws org.omg.TerminologyServices.InvalidExpression, org.omg.TerminologyServices.UnknownCodingScheme, org.omg.TerminologyServices.AssociationNotInSystemization, org.omg.TerminologyServices.NotImplemented, org.omg.TerminologyServices.UnableToEvaluate {
        return _tie.are_expression_difference(expression1, expression2);
    }

    /**
     * Operation minimal_common_supertype
     */
    public org.omg.TerminologyServices.ConceptExpressionElement[] minimal_common_supertype(org.omg.TerminologyServices.ConceptExpressionElement[][] expressions) throws org.omg.TerminologyServices.InvalidExpression, org.omg.TerminologyServices.AssociationNotInSystemization, org.omg.TerminologyServices.NotImplemented, org.omg.TerminologyServices.NoCommonSupertype {
        return _tie.minimal_common_supertype(expressions);
    }

    /**
     * Operation maximal_common_subtype
     */
    public org.omg.TerminologyServices.ConceptExpressionElement[] maximal_common_subtype(org.omg.TerminologyServices.ConceptExpressionElement[][] expressions) throws org.omg.TerminologyServices.InvalidExpression, org.omg.TerminologyServices.AssociationNotInSystemization, org.omg.TerminologyServices.NotImplemented, org.omg.TerminologyServices.NoCommonSubtype {
        return _tie.maximal_common_subtype(expressions);
    }
}

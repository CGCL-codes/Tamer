package com.ontotext.ordi.wsmo4rdf;

/**
 * All constants defined by http://www.wsmo.org/TR/d32/v0.1/,
 * 
 * @author vassil
 * 
 */
public class Constants {

    public static String PART_WHOLE_NS = "http://www.w3.org/2001/sw/BestPractices/OEP/SimplePartWhole/part.owl#";

    public static String PART_hasPart_directly = PART_WHOLE_NS + "hasPart_directly";

    public static String WSML_NS = "http://www.wsmo.org/wsml/wsml-syntax#";

    public static String WSML_variant = WSML_NS + "variant";

    public static String WSML_wsml_core = "http://www.wsmo.org/wsml/wsml-syntax/wsml_core";

    public static String WSML_wsml_flight = "http://www.wsmo.org/wsml/wsml-syntax/wsml_flight";

    public static String WSML_wsml_rule = "http://www.wsmo.org/wsml/wsml-syntax/wsml_rule";

    public static String WSML_wsml_full = "http://www.wsmo.org/wsml/wsml-syntax/wsml_full";

    public static String WSML_wsml_dl = "http://www.wsmo.org/wsml/wsml-syntax/wsml_dl";

    public static String WSML_ontology = WSML_NS + "Ontology";

    public static String WSML_attribute_definition = WSML_NS + "AttributeDefinition";

    public static String WSML_forAttribute = WSML_NS + "forAttribute";

    public static String WSML_concept = WSML_NS + "Concept";

    public static String WSML_Attribute = WSML_NS + "Attribute";

    public static String WSML_ofType = WSML_NS + "ofType";

    public static String WSML_impliesType = WSML_NS + "impliesType";

    public static String WSML_transitiveAttributeDefinition = WSML_NS + "TransitiveAttributeDefinition";

    public static String WSML_symmetricAttributeDefinition = WSML_NS + "SymmetricAttributeDefinition";

    public static String WSML_reflexiveAttributeDefinition = WSML_NS + "ReflexiveAttributeDefinition";

    public static String WSML_inverseOf = WSML_NS + "inverseOf";

    public static String WSML_minCardinality = WSML_NS + "minCardinality";

    public static String WSML_maxCardinality = WSML_NS + "maxCardinality";

    public static String WSML_Cardinality = WSML_NS + "Cardinality";

    public static String WSML_Relation = WSML_NS + "Relation";

    public static String WSML_arity = WSML_NS + "arity";

    public static String WSML_ParameterDefinition = WSML_NS + "ParameterDefinition";

    public static String WSML_subRelationOf = WSML_NS + "subRelationOf";

    public static String WSML_RelationInstance = WSML_NS + "RelationInstance";

    public static String WSML_axiom = WSML_NS + "Axiom";

    public static String WSML_goal = WSML_NS + "goal";

    public static String WSML_ooMediator = WSML_NS + "ooMediator";

    public static String WSML_ggMediator = WSML_NS + "ggMediator";

    public static String WSML_wgMediator = WSML_NS + "wgMediator";

    public static String WSML_wwMediator = WSML_NS + "wwMediator";

    public static String WSML_source = WSML_NS + "source";

    public static String WSML_target = WSML_NS + "target";

    public static String WSML_usesService = WSML_NS + "usesService";

    public static String WSML_webService = WSML_NS + "webService";

    public static String WSML_sharedVariable = WSML_NS + "sharedVariable";

    public static String WSML_hasPrecondition = WSML_NS + "hasPrecondition";

    public static String WSML_hasAssumption = WSML_NS + "hasAssumption";

    public static String WSML_hasPostcondition = WSML_NS + "hasPostcondition";

    public static String WSML_hasEffect = WSML_NS + "hasEffect";

    public static String WSML_Choreography = WSML_NS + "Choreography";

    public static String WSML_Orchestration = WSML_NS + "Orchestration";

    public static String WSML_Capability = WSML_NS + "Capability";

    public static String WSML_Interface = WSML_NS + "Interface";

    public static String WSML_importsOntology = WSML_NS + "importsOntology";

    public static String WSML_usesMediator = WSML_NS + "usesMediator";

    public static String WSML_false = WSML_NS + "false";

    public static String WSML_true = WSML_NS + "true";

    public static String RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

    public static String RDF_type = RDF_NS + "type";

    public static String RDF_XMLLiteral = RDF_NS + "XMLLiteral";

    public static String RDF_List = RDF_NS + "List";

    public static String RDF_first = RDF_NS + "first";

    public static String RDF_rest = RDF_NS + "rest";

    public static String RDF_Property = RDF_NS + "Property";

    public static String RDF_nil = RDF_NS + "nil";

    public static String RDFS_NS = "http://www.w3.org/2000/01/rdf-schema#";

    public static String RDFS_subClassOf = RDFS_NS + "subClassOf";

    public static String RDFS_range = RDFS_NS + "range";

    public static String RDFS_label = RDFS_NS + "label";

    public static String RDFS_comment = RDFS_NS + "comment";

    public static String RDFS_seeAlso = RDFS_NS + "seeAlso";

    public static String RDFS_isDefinedBy = RDFS_NS + "isDefinedBy";

    public static String RDFS_domain = RDFS_NS + "domain";

    public static String RDFS_Resource = RDFS_NS + "Resource";

    public static String RDFS_Datatype = RDFS_NS + "Datatype";

    public static String RDFS_Class = RDFS_NS + "Class";

    public static String RDFS_subPropertyOf = RDFS_NS + "subPropertyOf";

    public static String RDFS_member = RDFS_NS + "member";

    public static String RDFS_Container = RDFS_NS + "Container";

    public static String RDFS_ContainerMembershipProperty = RDFS_NS + "ContainerMembershipProperty";

    public static String DC_NS = "http://purl.org/dc/elements/1.1/";

    public static String DC_relation = DC_NS + "relation";

    public static String DC_title = DC_NS + "title";

    public static String DC_subject = DC_NS + "subject";

    public static String DC_description = DC_NS + "description";

    public static String DC_contributor = DC_NS + "contributor";

    public static String DC_date = DC_NS + "date";

    public static String DC_format = DC_NS + "format";

    public static String DC_language = DC_NS + "language";

    public static String DC_rights = DC_NS + "rights";

    public static String DC_type = DC_NS + "type";

    public static String XSD_NS = "http://www.w3.org/2001/XMLSchema#";

    public static String XSD_string = XSD_NS + "string";

    public static String XSD_integer = XSD_NS + "integer";

    public static String XSD_decimal = XSD_NS + "decimal";

    public static String XSD_date = XSD_NS + "date";

    public static String XSD_anyURI = XSD_NS + "anyURI";

    public static String XSD_nonNegateiveInteger = XSD_NS + "nonNegativeInteger";

    public static String OWL_NS = "http://www.w3.org/2002/07/owl#";

    public static String OWL_AnnotationProperty = OWL_NS + "AnnotationProperty";
}

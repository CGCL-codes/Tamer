package de.igdr.STTMod2;

/**
 * This class was modified. It extends the org.TELCERT.CRT.STT.utilities.STTConstants and
 * adds some new Contants for Merging the Schema so that the changes from the SchemaProf
 * Profile will not overwritten in the original Schema. 
 * Cross-class constants for the Schema Tranform Tool
 * 
 * @author wluxton
 */
public class STTConstantsMod2 {

    public static final String STT_VERSION = "2.10";

    public static boolean DOCUMENT_VALIDATION = false;

    public static final String SAX_BUILDER_CLASS = "org.apache.xerces.parsers.SAXParser";

    public static final String XML_SCHEMA_LOCATION = "org/TELCERT/CRT/STT/resources/XMLSchema.xsd";

    public static final String FILENAME_CONSTRAINT = "_constraintsDocument.scmt";

    public static final String FILENAME_CONDITION_ONLY = "_conditionElements.xml";

    public static final String FILENAME_DEFINITION_DOCUMENT = "_definition.xsd";

    public static final String FILENAME_ALT_BASE_SCHEMA = "_def_copy.xsd";

    public static final String FILENAME_LOCALISED = "temp.xsd";

    public static final String ATTRIBUTE_MOD_MAX_OCCURS = "mod_maxOccurs";

    public static final String ATTRIBUTE_MOD_MIN_OCCURS = "mod_minOccurs";

    public static final String ATTRIBUTE_MOD_USE = "mod_use";

    public static final String ATTRIBUTE_MOD_NAMESPACE = "mod_namespace";

    public static final String ATTRIBUTE_MOD_PROCESS_CONTENTS = "mod_processContents";

    public static final String ATTRIBUTE_MOD_TYPE = "mod_type";

    public static final String ATTRIBUTE_MOD_DEFAULT = "mod_default";

    public static final String ATTRIBUTE_MOD_FIXED = "mod_fixed";

    public static final String ATTRIBUTE_MOD_MAPPING = "mod_mapping";

    public static final String ATTRIBUTE_MOD_BASE = "mod_base";

    public static final String ATTRIBUTE_OLD_REF = "mod_ref";

    public static final String[] CARDINALITY_MOD_VALUES_LIST = { ATTRIBUTE_MOD_MIN_OCCURS, ATTRIBUTE_MOD_MAX_OCCURS };

    public static final String[] ATTRIBUTE_MOD_PROPERTIES_VALUES_LIST = { ATTRIBUTE_MOD_TYPE, ATTRIBUTE_MOD_USE, ATTRIBUTE_MOD_DEFAULT, ATTRIBUTE_MOD_FIXED, ATTRIBUTE_MOD_MAPPING };

    public static final String DERIVED_SCHEMA_NAMESPACE = "localised_Schema";

    public static final String NAMESPACE_XS_XSD = "http://www.w3.org/2001/XMLSchema";

    public static final String NAMESPACE_XSI = "http://www.w3.org/2001/XMLSchema-instance";

    public static final String NAMESPACE_PREFIX_XS = "xs";

    public static final String NAMESPACE_PREFIX_XSD = "xsd";

    public static final String NAMESPACE_PREFIX_XML = "xml";

    public static final String NAMESPACE_PREFIX_XSI = "xsi";

    public static final String[] NAMESPACE_PREFIX_LIST = { NAMESPACE_PREFIX_XS, NAMESPACE_PREFIX_XSD, NAMESPACE_PREFIX_XML, NAMESPACE_PREFIX_XSI };

    public static final String ELEMENT_SCHEMA = "schema";

    public static final String ELEMENT_DEFINITION_HOLDING_ELEMENT = "definitions";

    public static final String ELEMENT_MODIFICATION_HOLDING_ELEMENT = "modifications";

    public static final String ELEMENT_MAPPING_HOLDING_ELEMENT = "mappings";

    public static final String ELEMENT_MODIFICATION = "modification";

    public static final String ELEMENT_CONDITION = "condition";

    public static final String ELEMENT_ANNOTATION = "annotation";

    public static final String ELEMENT_DOCUMENTATION = "documentation";

    public static final String ELEMENT_INCLUDE = "include";

    public static final String ELEMENT_IMPORT = "import";

    public static final String ELEMENT_EXTENSION = "extension";

    public static final String ELEMENT_RESTRICTION = "restriction";

    public static final String ELEMENT_ENUMERATION = "enumeration";

    public static final String ELEMENT_ATTRIBUTE = "attribute";

    public static final String ELEMENT_ELEMENT = "element";

    public static final String ELEMENT_SIMPLE_TYPE = "simpleType";

    public static final String ELEMENT_COMPLEX_TYPE = "complexType";

    public static final String ELEMENT_SEQUENCE = "sequence";

    public static final String ELEMENT_CHOICE = "choice";

    public static final String ELEMENT_ANY = "any";

    public static final String ELEMENT_ANY_ATTRIBUTE = "anyAttribute";

    public static final String ELEMENT_GROUP = "group";

    public static final String ELEMENT_ATTRIBUTE_GROUP = "attributeGroup";

    public static final String ELEMENT_ANY_TYPE = "anyType";

    public static final String ELEMENT_PATTERN = "pattern";

    public static final String ELEMENT_UNION = "union";

    public static final String ATTRIBUTE_TARGET_NAMESPACE = "targetNamespace";

    public static final String PROFILE_NAMESPACE = "profileNamespace";

    public static final String ATTRIBUTE_SCHEMA_LOCATION = "schemaLocation";

    public static final String ATTRIBUTE_SCHEMA_VERSION = "version";

    public static final String ATTRIBUTE_BASE_SCHEMA = "baseSchema";

    public static final String ATTRIBUTE_PARENT_XPATH = "element";

    public static final String ATTRIBUTE_PREFIX = "prefix";

    public static final String ATTRIBUTE_URI = "uri";

    public static final String ATTRIBUTE_CHILD_XPATH = "subelement";

    public static final String ATTRIBUTE_NAME = "name";

    public static final String ATTRIBUTE_ID = "id";

    public static final String ATTRIBUTE_REF = "ref";

    public static final String ATTRIBUTE_NEXT_SIBLING = "nextSibling";

    public static final String ATTRIBUTE_CONDITION = "cnd";

    public static final String ATTRIBUTE_MAX_OCCURS = "maxOccurs";

    public static final String ATTRIBUTE_MIN_OCCURS = "minOccurs";

    public static final String ATTRIBUTE_TYPE = "type";

    public static final String ATTRIBUTE_USE = "use";

    public static final String ATTRIBUTE_DEFAULT = "default";

    public static final String ATTRIBUTE_FIXED = "fixed";

    public static final String ATTRIBUTE_MAPPING = "mapping";

    public static final String ATTRIBUTE_NAMESPACE = "namespace";

    public static final String ATTRIBUTE_PROCESS_CONTENTS = "processContents";

    public static final String ATTRIBUTE_OCCURS_UNBOUNDED = "unbounded";

    public static final String ATTRIBUTE_BASE = "base";

    public static final String ATTRIBUTE_VALUE = "value";

    public static final String ATTRIBUTE_CATEGORY = "category";

    public static final String ATTRIBUTE_MIXED = "mixed";

    public static final String ATTRIBUTE_PROFILE = "profile";

    public static final String ATTRIBUTE_MEMBER_TYPES = "memberTypes";

    public static final String MOD_CARDINALITY = "cardinality";

    public static final String MOD_ELEMENT_EXTENSION = "element_extension";

    public static final String MOD_ATTRIBUTE_EXTENSION = "attribute_extension";

    public static final String MOD_ATTRIBUTE_PROPERTIES = "attribute_properties";

    public static final String MOD_NEW_ELEMENT_EXTENSION = "new_element_extension";

    public static final String MOD_NEW_ATTRIBUTE_EXTENSION = "new_attribute_extension";

    public static final String MOD_ASSERTION = "assertion";

    public static final String TYPE_SIMPLE_CONTENT = "simpleContent";

    public static final String TYPE_COMPLEX_TYPE = "complexType";

    public static final String TYPE_ATTRIBUTE = "attribute";

    public static final String TYPE_ANY = ":any";

    public static final String TYPE_ANY_ATTRIBUTE = "anyAttribute";

    public static final String TYPE_ELEMENT = "element";

    public static final String TYPE_SIMPLE_TYPE = "simpleType";

    public static final String[] CARDINALITY_VALUES_LIST = { ATTRIBUTE_MIN_OCCURS, ATTRIBUTE_MAX_OCCURS };

    public static final String[] ATTRIBUTE_PROPERTIES_VALUES_LIST = { ATTRIBUTE_TYPE, ATTRIBUTE_USE, ATTRIBUTE_DEFAULT, ATTRIBUTE_FIXED, ATTRIBUTE_MAPPING };

    public static final String ATTRIBUTE_USE_PROHIBITED = "prohibited";

    public static final String ATTRIBUTE_USE_OPTIONAL = "optional";

    public static final String ATTRIBUTE_USE_REQUIRED = "required";

    public static final String[] ALLOWABLE_ATTRIBUTE_USE_VALUES_LIST = { ATTRIBUTE_USE_PROHIBITED, ATTRIBUTE_USE_OPTIONAL, ATTRIBUTE_USE_REQUIRED };

    public static final String[] EXTENSION_VALUES_LIST = { ATTRIBUTE_NAMESPACE, ATTRIBUTE_PROCESS_CONTENTS, ATTRIBUTE_MAX_OCCURS, ATTRIBUTE_MIN_OCCURS };

    public static final String ATTRIBUTE_PROCESS_CONTENTS_LAX = "lax";

    public static final String ATTRIBUTE_PROCESS_CONTENTS_STRICT = "strict";

    public static final String ATTRIBUTE_PROCESS_CONTENTS_SKIP = "skip";

    public static final String[] ALLOWABLE_ATTRIBUTE_PROCESS_CONTENTS_VALUES_LIST = { ATTRIBUTE_PROCESS_CONTENTS_LAX, ATTRIBUTE_PROCESS_CONTENTS_STRICT, ATTRIBUTE_PROCESS_CONTENTS_SKIP };
}

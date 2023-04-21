package org.exist.xquery.value;

import java.util.HashSet;
import org.apache.log4j.Logger;
import org.exist.Namespaces;
import org.exist.dom.QName;
import org.exist.util.hashtable.Int2ObjectHashMap;
import org.exist.util.hashtable.Object2IntHashMap;
import org.exist.xquery.XPathException;

/**
 * Defines all built-in types and their relations.
 * 
 * @author Wolfgang Meier (wolfgang@exist-db.org)
 */
public class Type {

    private static final Logger LOG = Logger.getLogger(Type.class);

    public static final String[] NODETYPES = { "node", "element", "attribute", "text", "processing-instruction", "comment", "document", "namespace", "cdata-section" };

    public static final int NODE = -1;

    public static final int ELEMENT = 1;

    public static final int ATTRIBUTE = 2;

    public static final int TEXT = 3;

    public static final int PROCESSING_INSTRUCTION = 4;

    public static final int COMMENT = 5;

    public static final int DOCUMENT = 6;

    public static final int NAMESPACE = 500;

    public static final int CDATA_SECTION = 501;

    public static final int EMPTY = 10;

    public static final int ITEM = 11;

    public static final int ANY_TYPE = 12;

    public static final int ANY_SIMPLE_TYPE = 13;

    public static final int UNTYPED = 14;

    public static final int ATOMIC = 20;

    public static final int UNTYPED_ATOMIC = 21;

    public static final int STRING = 22;

    public static final int BOOLEAN = 23;

    public static final int QNAME = 24;

    public static final int ANY_URI = 25;

    public static final int BASE64_BINARY = 26;

    public static final int HEX_BINARY = 27;

    public static final int NOTATION = 28;

    public static final int NUMBER = 30;

    public static final int INTEGER = 31;

    public static final int DECIMAL = 32;

    public static final int FLOAT = 33;

    public static final int DOUBLE = 34;

    public static final int NON_POSITIVE_INTEGER = 35;

    public static final int NEGATIVE_INTEGER = 36;

    public static final int LONG = 37;

    public static final int INT = 38;

    public static final int SHORT = 39;

    public static final int BYTE = 40;

    public static final int NON_NEGATIVE_INTEGER = 41;

    public static final int UNSIGNED_LONG = 42;

    public static final int UNSIGNED_INT = 43;

    public static final int UNSIGNED_SHORT = 44;

    public static final int UNSIGNED_BYTE = 45;

    public static final int POSITIVE_INTEGER = 46;

    public static final int DATE_TIME = 50;

    public static final int DATE = 51;

    public static final int TIME = 52;

    public static final int DURATION = 53;

    public static final int YEAR_MONTH_DURATION = 54;

    public static final int DAY_TIME_DURATION = 55;

    public static final int GYEAR = 56;

    public static final int GMONTH = 57;

    public static final int GDAY = 58;

    public static final int GYEARMONTH = 59;

    public static final int GMONTHDAY = 71;

    public static final int TOKEN = 60;

    public static final int NORMALIZED_STRING = 61;

    public static final int LANGUAGE = 62;

    public static final int NMTOKEN = 63;

    public static final int NAME = 64;

    public static final int NCNAME = 65;

    public static final int ID = 66;

    public static final int IDREF = 67;

    public static final int ENTITY = 68;

    public static final int JAVA_OBJECT = 100;

    public static final int FUNCTION_REFERENCE = 101;

    /**
     * Special type constant to indicate that an item has been
     * fulltext indexed.
     */
    public static final int IDX_FULLTEXT = 200;

    private static final int[] superTypes = new int[512];

    static {
        defineSubType(ANY_TYPE, ANY_SIMPLE_TYPE);
        defineSubType(ANY_TYPE, UNTYPED);
        defineSubType(ANY_SIMPLE_TYPE, ATOMIC);
        defineSubType(NODE, ELEMENT);
        defineSubType(NODE, ATTRIBUTE);
        defineSubType(NODE, TEXT);
        defineSubType(NODE, PROCESSING_INSTRUCTION);
        defineSubType(NODE, COMMENT);
        defineSubType(NODE, DOCUMENT);
        defineSubType(NODE, NAMESPACE);
        defineSubType(NODE, CDATA_SECTION);
        defineSubType(ITEM, ATOMIC);
        defineSubType(ATOMIC, STRING);
        defineSubType(ATOMIC, BOOLEAN);
        defineSubType(ATOMIC, QNAME);
        defineSubType(ATOMIC, ANY_URI);
        defineSubType(ATOMIC, NUMBER);
        defineSubType(ATOMIC, UNTYPED_ATOMIC);
        defineSubType(ATOMIC, JAVA_OBJECT);
        defineSubType(ATOMIC, FUNCTION_REFERENCE);
        defineSubType(ATOMIC, DATE_TIME);
        defineSubType(ATOMIC, DATE);
        defineSubType(ATOMIC, TIME);
        defineSubType(ATOMIC, DURATION);
        defineSubType(ATOMIC, GYEAR);
        defineSubType(ATOMIC, GMONTH);
        defineSubType(ATOMIC, GDAY);
        defineSubType(ATOMIC, GYEARMONTH);
        defineSubType(ATOMIC, GMONTHDAY);
        defineSubType(ATOMIC, BASE64_BINARY);
        defineSubType(ATOMIC, HEX_BINARY);
        defineSubType(ATOMIC, NOTATION);
        defineSubType(DURATION, YEAR_MONTH_DURATION);
        defineSubType(DURATION, DAY_TIME_DURATION);
        defineSubType(NUMBER, DECIMAL);
        defineSubType(NUMBER, FLOAT);
        defineSubType(NUMBER, DOUBLE);
        defineSubType(DECIMAL, INTEGER);
        defineSubType(INTEGER, NON_POSITIVE_INTEGER);
        defineSubType(NON_POSITIVE_INTEGER, NEGATIVE_INTEGER);
        defineSubType(INTEGER, LONG);
        defineSubType(LONG, INT);
        defineSubType(INT, SHORT);
        defineSubType(SHORT, BYTE);
        defineSubType(INTEGER, NON_NEGATIVE_INTEGER);
        defineSubType(NON_NEGATIVE_INTEGER, POSITIVE_INTEGER);
        defineSubType(NON_NEGATIVE_INTEGER, UNSIGNED_LONG);
        defineSubType(UNSIGNED_LONG, UNSIGNED_INT);
        defineSubType(UNSIGNED_INT, UNSIGNED_SHORT);
        defineSubType(UNSIGNED_SHORT, UNSIGNED_BYTE);
        defineSubType(STRING, NORMALIZED_STRING);
        defineSubType(NORMALIZED_STRING, TOKEN);
        defineSubType(TOKEN, LANGUAGE);
        defineSubType(TOKEN, NMTOKEN);
        defineSubType(TOKEN, NAME);
        defineSubType(NAME, NCNAME);
        defineSubType(NCNAME, ID);
        defineSubType(NCNAME, IDREF);
        defineSubType(NCNAME, ENTITY);
    }

    private static final Int2ObjectHashMap<String[]> typeNames = new Int2ObjectHashMap<String[]>(100);

    private static final Object2IntHashMap<String> typeCodes = new Object2IntHashMap<String>(100);

    static {
        defineBuiltInType(NODE, "node()");
        defineBuiltInType(ITEM, "item()");
        defineBuiltInType(EMPTY, "empty()");
        defineBuiltInType(ELEMENT, "element()");
        defineBuiltInType(DOCUMENT, "document-node()");
        defineBuiltInType(ATTRIBUTE, "attribute()");
        defineBuiltInType(TEXT, "text()");
        defineBuiltInType(PROCESSING_INSTRUCTION, "processing-instruction()");
        defineBuiltInType(COMMENT, "comment()");
        defineBuiltInType(NAMESPACE, "namespace()");
        defineBuiltInType(CDATA_SECTION, "cdata-section()");
        defineBuiltInType(JAVA_OBJECT, "object");
        defineBuiltInType(FUNCTION_REFERENCE, "function");
        defineBuiltInType(NUMBER, "numeric");
        defineBuiltInType(ANY_TYPE, "xs:anyType");
        defineBuiltInType(ANY_SIMPLE_TYPE, "xs:anySimpleType");
        defineBuiltInType(UNTYPED, "xs:untyped");
        defineBuiltInType(ATOMIC, "xs:anyAtomicType", "xdt:anyAtomicType");
        defineBuiltInType(UNTYPED_ATOMIC, "xs:untypedAtomic", "xdt:untypedAtomic");
        defineBuiltInType(BOOLEAN, "xs:boolean");
        defineBuiltInType(DECIMAL, "xs:decimal");
        defineBuiltInType(FLOAT, "xs:float");
        defineBuiltInType(DOUBLE, "xs:double");
        defineBuiltInType(INTEGER, "xs:integer");
        defineBuiltInType(NON_POSITIVE_INTEGER, "xs:nonPositiveInteger");
        defineBuiltInType(NEGATIVE_INTEGER, "xs:negativeInteger");
        defineBuiltInType(LONG, "xs:long");
        defineBuiltInType(INT, "xs:int");
        defineBuiltInType(SHORT, "xs:short");
        defineBuiltInType(BYTE, "xs:byte");
        defineBuiltInType(NON_NEGATIVE_INTEGER, "xs:nonNegativeInteger");
        defineBuiltInType(UNSIGNED_LONG, "xs:unsignedLong");
        defineBuiltInType(UNSIGNED_INT, "xs:unsignedInt");
        defineBuiltInType(UNSIGNED_SHORT, "xs:unsignedShort");
        defineBuiltInType(UNSIGNED_BYTE, "xs:unsignedByte");
        defineBuiltInType(POSITIVE_INTEGER, "xs:positiveInteger");
        defineBuiltInType(STRING, "xs:string");
        defineBuiltInType(QNAME, "xs:QName");
        defineBuiltInType(ANY_URI, "xs:anyURI");
        defineBuiltInType(BASE64_BINARY, "xs:base64Binary");
        defineBuiltInType(HEX_BINARY, "xs:hexBinary");
        defineBuiltInType(NOTATION, "xs:NOTATION");
        defineBuiltInType(DATE_TIME, "xs:dateTime");
        defineBuiltInType(DATE, "xs:date");
        defineBuiltInType(TIME, "xs:time");
        defineBuiltInType(DURATION, "xs:duration");
        defineBuiltInType(GYEAR, "xs:gYear");
        defineBuiltInType(GMONTH, "xs:gMonth");
        defineBuiltInType(GDAY, "xs:gDay");
        defineBuiltInType(GYEARMONTH, "xs:gYearMonth");
        defineBuiltInType(GMONTHDAY, "xs:gMonthDay");
        defineBuiltInType(YEAR_MONTH_DURATION, "xs:yearMonthDuration", "xdt:yearMonthDuration");
        defineBuiltInType(DAY_TIME_DURATION, "xs:dayTimeDuration", "xdt:dayTimeDuration");
        defineBuiltInType(NORMALIZED_STRING, "xs:normalizedString");
        defineBuiltInType(TOKEN, "xs:token");
        defineBuiltInType(LANGUAGE, "xs:language");
        defineBuiltInType(NMTOKEN, "xs:NMTOKEN");
        defineBuiltInType(NAME, "xs:Name");
        defineBuiltInType(NCNAME, "xs:NCName");
        defineBuiltInType(ID, "xs:ID");
        defineBuiltInType(IDREF, "xs:IDREF");
        defineBuiltInType(ENTITY, "xs:ENTITY");
    }

    /**
     * @param name The first name is the default name, any other names are aliases.
     */
    public static void defineBuiltInType(int type, String... name) {
        typeNames.put(type, name);
        for (String n : name) {
            typeCodes.put(n, type);
        }
    }

    /**
     * Get the internal default name for the built-in type.
     * 
     * @param type
     */
    public static String getTypeName(int type) {
        return typeNames.get(type)[0];
    }

    /**
     * Get the internal aliases for the built-in type.
     * 
     * @param type
     */
    public static String[] getTypeAliases(int type) {
        String names[] = typeNames.get(type);
        if (names != null && names.length > 1) {
            String aliases[] = new String[names.length - 1];
            System.arraycopy(names, 1, aliases, 0, names.length - 1);
            return aliases;
        }
        return null;
    }

    /**
	 * Get the type code for a type identified by its internal name.
	 * 
	 * @param name
	 * @throws XPathException
	 */
    public static int getType(String name) throws XPathException {
        int code = typeCodes.get(name);
        if (code == Object2IntHashMap.UNKNOWN_KEY) throw new XPathException("Type: " + name + " is not defined");
        return code;
    }

    /**
	 * Get the type code for a type identified by its QName.
	 * 
	 * @param qname
	 * @throws XPathException
	 */
    public static int getType(QName qname) throws XPathException {
        String uri = qname.getNamespaceURI();
        if (uri.equals(Namespaces.SCHEMA_NS)) return getType("xs:" + qname.getLocalName()); else if (uri.equals(Namespaces.XPATH_DATATYPES_NS)) return getType("xdt:" + qname.getLocalName()); else return getType(qname.getLocalName());
    }

    /**
	 * Define supertype/subtype relation.
	 * 
	 * @param supertype
	 * @param subtype
	 */
    public static void defineSubType(int supertype, int subtype) {
        superTypes[subtype] = supertype;
    }

    /**
	 * Check if the given type code is a subtype of the specified supertype.
	 * 
	 * @param subtype
	 * @param supertype
         *
         * @throws IllegalArgumentException When the type is invalid
	 */
    public static boolean subTypeOf(int subtype, int supertype) {
        if (subtype == supertype) return true;
        if (supertype == ITEM || supertype == ANY_TYPE) return true;
        if (subtype == ITEM || subtype == EMPTY || subtype == ANY_TYPE || subtype == NODE) return false;
        subtype = superTypes[subtype];
        if (subtype == 0) throw new IllegalArgumentException("type " + subtype + " is not a valid type");
        return subTypeOf(subtype, supertype);
    }

    /**
	 * Get the type code of the supertype of the specified subtype.
	 * 
	 * @param subtype
	 */
    public static int getSuperType(int subtype) {
        if (subtype == ITEM || subtype == NODE) return ITEM;
        int supertype = superTypes[subtype];
        if (supertype == 0) {
            LOG.warn("no supertype for " + getTypeName(subtype), new Throwable());
            return ITEM;
        }
        return supertype;
    }

    /**
	 * Find a common supertype for two given type codes.
	 * 
	 * Type.ITEM is returned if no other common supertype
	 * is found.
	 *  
	 * @param type1
	 * @param type2
	 */
    public static int getCommonSuperType(int type1, int type2) {
        if (type1 == type2) return type1;
        HashSet<Integer> t1 = new HashSet<Integer>();
        int t;
        for (t = type1; t != ITEM; t = getSuperType(t)) {
            if (t == type2) return t;
            t1.add(new Integer(t));
        }
        for (t = getSuperType(type2); t != ITEM; t = getSuperType(t)) {
            if (t1.contains(Integer.valueOf(t))) return t;
        }
        return ITEM;
    }
}

package javacream.xml.xsd;

/**
 * DefaultNamespace
 * 
 * @author Glenn Powell
 *
 */
public class DefaultNamespace extends Namespace {

    public static final String IDENTIFIER = "http://www.w3.org/2001/XMLSchema";

    public DefaultNamespace() {
        super(IDENTIFIER);
    }

    public void addDeclaration(Declaration declaration) {
    }

    public <T> T getDeclaration(String name, Class<T> classType) {
        if (classType.isAssignableFrom(SimpleType.class)) {
            if (name.equals("string") || name.equals("boolean") || name.equals("decimal") || name.equals("float") || name.equals("double") || name.equals("duration") || name.equals("dateTime") || name.equals("time") || name.equals("date") || name.equals("gYearMonth") || name.equals("gYear") || name.equals("gMonthDay") || name.equals("gDay") || name.equals("gMonth") || name.equals("hexBinary") || name.equals("base64Binary") || name.equals("anyURI") || name.equals("QName") || name.equals("NOTATION")) {
                return classType.cast(new PrimitiveType(name));
            } else if (name.equals("normalizedString") || name.equals("token") || name.equals("language") || name.equals("NMTOKEN") || name.equals("NMTOKENS") || name.equals("Name") || name.equals("NCName") || name.equals("ID") || name.equals("IDREF") || name.equals("IDREFS") || name.equals("ENTITIY") || name.equals("ENTITIES") || name.equals("integer") || name.equals("nonPositiveInteger") || name.equals("negativeInteger") || name.equals("long") || name.equals("int") || name.equals("short") || name.equals("byte") || name.equals("nonNegativeInteger") || name.equals("unsignedLong") || name.equals("unsignedInt") || name.equals("unsignedShort") || name.equals("unsignedByte") || name.equals("positiveInteger")) {
                return classType.cast(new PrimitiveType(name));
            }
        }
        return null;
    }
}

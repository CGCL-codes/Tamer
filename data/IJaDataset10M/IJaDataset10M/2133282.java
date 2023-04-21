package krum.sectorzero.codegen;

import java.util.HashMap;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Node;

/**
 * 
 *
 * @author Kevin Krumwiede (kjkrum@gmail.com)
 */
class ParameterElement {

    private String name;

    private String comment;

    private String key;

    private String type;

    private static final HashMap<String, Class<?>> parameterTypes = new HashMap<String, Class<?>>();

    private static final HashMap<String, String> getterNames = new HashMap<String, String>();

    static {
        parameterTypes.put("int", int.class);
        parameterTypes.put("int[]", new int[0].getClass());
        parameterTypes.put("long", long.class);
        parameterTypes.put("float", float.class);
        parameterTypes.put("boolean", boolean.class);
        parameterTypes.put("String", String.class);
        parameterTypes.put("UUID", java.util.UUID.class);
        getterNames.put("int", "getInt");
        getterNames.put("int[]", "getIntArray");
        getterNames.put("long", "getLong");
        getterNames.put("float", "getFloat");
        getterNames.put("boolean", "getBoolean");
        getterNames.put("String", "getString");
        getterNames.put("UUID", "getUUID");
    }

    ParameterElement(XPath xp, Node node) throws XPathExpressionException {
        name = (String) xp.evaluate("Name", node, XPathConstants.STRING);
        comment = (String) xp.evaluate("Comment", node, XPathConstants.STRING);
        key = (String) xp.evaluate("Key", node, XPathConstants.STRING);
        type = (String) xp.evaluate("Type", node, XPathConstants.STRING);
    }

    String getName() {
        return name;
    }

    boolean hasComment() {
        return (!comment.equals(""));
    }

    String getComment() {
        return comment;
    }

    String getKey() {
        return key;
    }

    Class<?> getType() {
        return parameterTypes.get(type);
    }

    String getGetterName() {
        return getterNames.get(type);
    }
}

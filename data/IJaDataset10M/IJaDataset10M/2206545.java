package onepoint.resource;

import java.util.HashMap;
import onepoint.xml.XContext;
import onepoint.xml.XNodeHandler;

public final class XLanguageResourceHandler implements XNodeHandler {

    public static final String RESOURCE = "resource";

    public static final String ID = "id";

    public static final String PARAMETER = "parameter";

    public static final String NAME = "name";

    public static final String DELIMITER = "delimiter";

    public static final String GLOBAL_PREFIX = "globalprefix";

    public Object newNode(XContext context, String name, HashMap attributes) {
        Object node = null;
        Object value = attributes.get(ID);
        if ((value != null) && (value instanceof String)) {
            XLanguageResource n = new XLanguageResource((String) value);
            n.addText("");
            node = n;
        }
        return node;
    }

    public void addNodeContent(XContext context, Object node, String content) {
        XLanguageResource language_resource = (XLanguageResource) node;
        language_resource.addText(content);
    }

    public void addChildNode(XContext context, Object node, String child_name, Object child) {
        if (child_name.equals(PARAMETER)) {
            XLanguageResource language_resource = (XLanguageResource) node;
            language_resource.addParameter((String) ((HashMap) child).get(NAME));
            language_resource.addDelimiter((String) ((HashMap) child).get(DELIMITER));
            language_resource.addGlobalPrefix((String) ((HashMap) child).get(GLOBAL_PREFIX));
        }
    }

    public void nodeFinished(XContext context, String name, Object node, Object parent) {
    }
}

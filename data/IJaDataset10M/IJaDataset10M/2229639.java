package onepoint.project.module;

import java.util.HashMap;
import java.util.Map;
import onepoint.project.util.OpProjectConstants;
import onepoint.xml.XContext;
import onepoint.xml.XNodeHandler;

public class OpToolHandler implements XNodeHandler {

    static final String TOOL = "tool";

    private static final String NAME = "name";

    private static final String CAPTION = "caption";

    private static final String ICON = "icon";

    private static final String START_FORM = "start-form";

    private static final String START_PARAMS = "start-params";

    private static final String GROUP_REF = "group-ref";

    private static final String SEQUENCE = "sequence";

    private static final String MULTI_USER = "multi-user-only";

    private static final String LICENSE_OPTION = "license-option";

    private static final String SELECTED = "selected";

    private static final String LEVEL = "level";

    public static final String PARAM_DELIM = "?";

    public static final String ARRAY_DELIM = "&";

    public static final String KEY_VALUE_DELIM = "=";

    /**
    * The mapping between the user level names and the byte code of each level.
    * The structure of the map is: Key - user level name
    * Value - the level's byte code.
    */
    private static Map<String, Byte> LEVEL_NAME_TYPE_MAP;

    static {
        LEVEL_NAME_TYPE_MAP = new HashMap<String, Byte>();
        LEVEL_NAME_TYPE_MAP.put("Customer", OpProjectConstants.OBSERVER_CUSTOMER_USER_LEVEL);
        LEVEL_NAME_TYPE_MAP.put("Observer", OpProjectConstants.OBSERVER_USER_LEVEL);
        LEVEL_NAME_TYPE_MAP.put("Contributor", OpProjectConstants.CONTRIBUTOR_USER_LEVEL);
        LEVEL_NAME_TYPE_MAP.put("Manager", OpProjectConstants.MANAGER_USER_LEVEL);
    }

    protected static Byte getLevelType(String levelName) {
        return LEVEL_NAME_TYPE_MAP.get(levelName);
    }

    public Object newNode(XContext context, String name, HashMap attributes) {
        OpToolType tool = new OpToolType();
        Object value = attributes.get(NAME);
        if ((value != null) && (value instanceof String)) {
            tool.setName((String) value);
        }
        value = attributes.get(CAPTION);
        if ((value != null) && (value instanceof String)) {
            tool.setCaption((String) value);
        }
        value = attributes.get(ICON);
        if ((value != null) && (value instanceof String)) {
            tool.setIcon((String) value);
        }
        value = attributes.get(START_FORM);
        if ((value != null) && (value instanceof String)) {
            tool.setStartForm((String) value);
        }
        value = attributes.get(START_PARAMS);
        if ((value != null) && (value instanceof String)) {
            String[] pairs = ((String) value).split(ARRAY_DELIM);
            for (int pos = 0; pos < pairs.length; pos++) {
                int keyValDel = pairs[pos].indexOf(KEY_VALUE_DELIM);
                if (keyValDel > 0) {
                    tool.addStartParam(pairs[pos].substring(0, keyValDel), pairs[pos].substring(keyValDel + KEY_VALUE_DELIM.length()));
                } else {
                    tool.addStartParam(pairs[pos], "true");
                }
            }
        }
        value = attributes.get(GROUP_REF);
        if ((value != null) && (value instanceof String)) {
            tool.setGroupRef((String) value);
        }
        value = attributes.get(SEQUENCE);
        if ((value != null) && (value instanceof String)) {
            tool.setSequence(Integer.parseInt((String) value));
        }
        value = attributes.get(MULTI_USER);
        if ((value != null) && (value instanceof String)) {
            tool.setMultiUserOnly(Boolean.valueOf((String) value));
        }
        value = attributes.get(SELECTED);
        if ((value != null) && (value instanceof String)) {
            tool.setSelected(Boolean.valueOf((String) value).booleanValue());
        }
        value = attributes.get(LEVEL);
        if ((value != null) && (value instanceof String)) {
            if (LEVEL_NAME_TYPE_MAP.containsKey(value)) {
                tool.setLevel(LEVEL_NAME_TYPE_MAP.get(value));
            }
        }
        value = attributes.get(LICENSE_OPTION);
        if ((value != null) && (value instanceof String)) {
            tool.setLicenseOption((String) value);
        }
        return tool;
    }

    public void addChildNode(XContext context, Object node, String child_name, Object child) {
    }

    public void addNodeContent(XContext context, Object node, String content) {
    }

    public void nodeFinished(XContext context, String name, Object node, Object parent) {
    }
}

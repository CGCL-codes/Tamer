package fr.itris.glips.rtda.toolkit;

import java.text.*;
import java.util.*;
import org.w3c.dom.*;
import fr.itris.glips.rtda.resources.*;

/**
 * the toolkit used for handling tags
 * @author Jordi SUC
 */
public class TagToolkit {

    /**
	 * the date format used to convert dates to strings
	 */
    public static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);

    /**
	 * the date and time format used to convert dates and times to strings
	 */
    public static DateFormat dateTimeFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

    /**
	 * the tag name for the module node
	 */
    public static final String moduleName = "module";

    /**
	 *  the tag name for the enumerated node
	 */
    public static final String enumeratedName = "tag.enumerated";

    /**
	 *  the tag name for the float node
	 */
    public static final String floatName = "tag.float";

    /**
	 *  the tag name for the integer node
	 */
    public static final String integerName = "tag.integer";

    /**
	 *  the tag name for the string node
	 */
    public static final String stringName = "tag.string";

    /**
	 *  the tag name for the enumerated tag child 
	 */
    public static final String itemName = "item";

    /**
	 *  the tag name for the view 
	 */
    public static final String viewName = "view";

    /**
	 * the not a tag constant
	 */
    public static final int NOT_A_TAG = 0;

    /**
	 * the enumerated constant
	 */
    public static final int ENUMERATED = 1;

    /**
	 * the analogic float tag constant
	 */
    public static final int ANALOGIC_FLOAT = 2;

    /**
	 * the analogic integer tag constant
	 */
    public static final int ANALOGIC_INTEGER = 3;

    /**
	 * the string tag constant
	 */
    public static final int STRING = 4;

    /**
	 * the enumerated tag child constant
	 */
    public static final int ENUMERATED_CHILD = 5;

    /**
	 * the view constant
	 */
    public static final int VIEW = 6;

    /**
	 * the analogic integer tag constant
	 */
    public static final int ANALOGIC = 7;

    /**
	 * the none constant
	 */
    public static final int NONE = 8;

    /**
	 * the analogic or enumerated integer tag constant
	 */
    public static final int ANALOGIC_OR_ENUMERATED = 9;

    /**
	 * the constant for the animations that need children
	 */
    public static final int NEED_CHILDREN = 10;

    /**
	 * the constant for specifying to use any tag
	 */
    public static final int ANY_TAG = 11;

    /**
	 * the module label
	 */
    public static String moduleLabel = "";

    /**
	 * the analogic tag label
	 */
    public static String analogicTagLabel = "";

    /**
	 * the float tag label
	 */
    public static String floatTagLabel = "";

    /**
	 * the integer tag label
	 */
    public static String integerTagLabel = "";

    /**
	 * the enumerated tag label
	 */
    public static String enumeratedTagLabel = "";

    /**
	 * the string tag label
	 */
    public static String stringTagLabel = "";

    /**
	 * the enumerated value tag label
	 */
    public static String enumeratedValueLabel = "";

    /**
	 * the view label
	 */
    public static String viewLabel = "";

    /**
	 * the module id label
	 */
    public static String moduleIdLabel = "";

    /**
	 * the float tag id label
	 */
    public static String floatTagIdLabel = "";

    /**
	 * the integer tag id label
	 */
    public static String integerTagIdLabel = "";

    /**
	 * the enumerated tag id label
	 */
    public static String enumeratedTagIdLabel = "";

    /**
	 * the string tag id label
	 */
    public static String stringTagIdLabel = "";

    /**
	 * the enumerated value id label
	 */
    public static String enumeratedValueIdLabel = "";

    /**
	 * the none label
	 */
    public static String noneLabel = "";

    /**
	 * creating the labels
	 */
    static {
        ResourceBundle bundle = RtdaResources.bundle;
        if (bundle != null) {
            try {
                analogicTagLabel = bundle.getString("rtdadb_analogictag");
                floatTagLabel = bundle.getString("rtdadb_floattag");
                integerTagLabel = bundle.getString("rtdadb_integertag");
                enumeratedTagLabel = bundle.getString("rtdadb_enumeratedtag");
                stringTagLabel = bundle.getString("rtdadb_stringtag");
                enumeratedValueLabel = bundle.getString("rtdadb_enumeratedvalue");
                moduleLabel = bundle.getString("rtdadb_moduleid");
                viewLabel = bundle.getString("rtdadb_view");
                floatTagIdLabel = bundle.getString("rtdadb_floattagid");
                integerTagIdLabel = bundle.getString("rtdadb_integertagid");
                enumeratedTagIdLabel = bundle.getString("rtdadb_enumeratedtagid");
                stringTagIdLabel = bundle.getString("rtdadb_stringtagid");
                enumeratedValueIdLabel = bundle.getString("rtdadb_enumeratedvalueid");
                moduleIdLabel = bundle.getString("rtdadb_moduleid");
                noneLabel = bundle.getString("rtdadb_none");
            } catch (Exception ex) {
            }
        }
    }

    /**
	 * returns the type of the node denoted by the given node name
	 * @param nodeName a node name
	 * @return the type of the node denoted by the given node name
	 */
    public static int getNodeType(String nodeName) {
        int nodeType = -1;
        if (nodeName != null && !nodeName.equals("")) {
            if (nodeName.startsWith("rtda:")) {
                nodeName = nodeName.replaceFirst("rtda:", "");
            }
            if (nodeName.equals(enumeratedName)) {
                nodeType = ENUMERATED;
            } else if (nodeName.equals(itemName)) {
                nodeType = ENUMERATED_CHILD;
            } else if (nodeName.equals(floatName)) {
                nodeType = ANALOGIC_FLOAT;
            } else if (nodeName.equals(integerName)) {
                nodeType = ANALOGIC_INTEGER;
            } else if (nodeName.equals(stringName)) {
                nodeType = STRING;
            } else if (nodeName.equals(viewName)) {
                nodeType = VIEW;
            } else {
                nodeType = NOT_A_TAG;
            }
        }
        return nodeType;
    }

    /**
	 * returns the name of the icon corresponding to the given tag type
	 * @param nodeType the type of a node
	 * @return the name of the icon corresponding to the given tag type
	 */
    public static String getIconName(int nodeType) {
        String iconName = "";
        switch(nodeType) {
            case ENUMERATED:
                iconName = "Enumerated";
                break;
            case ENUMERATED_CHILD:
                iconName = "EnumeratedValue";
                break;
            case ANALOGIC:
            case ANALOGIC_FLOAT:
                iconName = "Float";
                break;
            case ANALOGIC_INTEGER:
                iconName = "Integer";
                break;
            case STRING:
                iconName = "String";
                break;
            case NOT_A_TAG:
                iconName = "Module";
                break;
            case VIEW:
                iconName = "View";
                break;
        }
        return iconName;
    }

    /**
	 * returns the name associated with a tag, a view, a module, or the "none" type
	 * @param type the type
	 * @return the name associated with a tag, a view, a module, or the "none" type
	 */
    public static String getTagName(int type) {
        String tagLabel = "";
        switch(type) {
            case ENUMERATED:
                tagLabel = enumeratedName;
                break;
            case ENUMERATED_CHILD:
                tagLabel = itemName;
                break;
            case ANALOGIC:
            case ANALOGIC_FLOAT:
                tagLabel = floatName;
                break;
            case ANALOGIC_INTEGER:
                tagLabel = integerName;
                break;
            case STRING:
                tagLabel = stringName;
                break;
            case NOT_A_TAG:
                tagLabel = moduleName;
                break;
            case VIEW:
                tagLabel = viewName;
                break;
        }
        return tagLabel;
    }

    /**
	 * returns the label associated with a tag, a view, a module, or the "none" type
	 * @param type the type
	 * @return the label associated with a tag, a view, a module, or the "none" type
	 */
    public static String getTagLabel(int type) {
        String tagLabel = "";
        switch(type) {
            case ENUMERATED:
                tagLabel = enumeratedTagLabel;
                break;
            case ENUMERATED_CHILD:
                tagLabel = enumeratedValueLabel;
                break;
            case ANALOGIC:
            case ANALOGIC_FLOAT:
                tagLabel = floatTagLabel;
                break;
            case ANALOGIC_INTEGER:
                tagLabel = integerTagLabel;
                break;
            case STRING:
                tagLabel = stringTagLabel;
                break;
            case NOT_A_TAG:
                tagLabel = moduleLabel;
                break;
            case VIEW:
                tagLabel = viewLabel;
                break;
        }
        return tagLabel;
    }

    /**
	 * returns the label of the default id (for the widget data base) 
	 * associated with a tag, a view, a module, or the "none" type
	 * @param type the type
	 * @return  returns the label of the default id (for the widget data base) 
	 * associated with a tag, a view, a module, or the "none" type
	 */
    public static String getTagIdLabel(int type) {
        String tagId = "";
        switch(type) {
            case ENUMERATED:
                tagId = enumeratedTagIdLabel;
                break;
            case ENUMERATED_CHILD:
                tagId = enumeratedValueIdLabel;
                break;
            case ANALOGIC:
            case ANALOGIC_FLOAT:
                tagId = floatTagIdLabel;
                break;
            case ANALOGIC_INTEGER:
                tagId = integerTagIdLabel;
                break;
            case STRING:
                tagId = stringTagIdLabel;
                break;
            case NOT_A_TAG:
                tagId = moduleIdLabel;
                break;
        }
        return tagId;
    }

    /**
	 * checks if the two given types are equivalent or not
	 * @param type1 the first type
	 * @param type2 the second type
	 * @param strictEquality whether the equivalence is an equality or not
	 * @return whether the two given types are equivalent or not
	 */
    public static boolean areTypesEquivalent(int type1, int type2, boolean strictEquality) {
        boolean equivalent = false;
        if (strictEquality) {
            if (type1 == type2) {
                equivalent = true;
            }
        } else {
            if (type1 != type2) {
                if (type1 == ANALOGIC) {
                    if (type2 == ANALOGIC_FLOAT || type2 == ANALOGIC_INTEGER || type2 == ANALOGIC_OR_ENUMERATED || type2 == ANY_TAG) {
                        equivalent = true;
                    }
                } else if (type1 == ENUMERATED) {
                    if (type2 == ANALOGIC_OR_ENUMERATED || type2 == ANY_TAG) {
                        equivalent = true;
                    }
                } else if (type1 == ANALOGIC_FLOAT || type1 == ANALOGIC_INTEGER) {
                    if (type2 == ANALOGIC || type2 == ANALOGIC_OR_ENUMERATED || type2 == ANY_TAG) {
                        equivalent = true;
                    }
                } else if (type1 == STRING) {
                    if (type2 == ANY_TAG) {
                        equivalent = true;
                    }
                } else if (type2 == ANALOGIC) {
                    if (type1 == ANALOGIC_FLOAT || type1 == ANALOGIC_INTEGER || type1 == ANALOGIC_OR_ENUMERATED || type1 == ANY_TAG) {
                        equivalent = true;
                    }
                } else if (type2 == ENUMERATED) {
                    if (type1 == ANALOGIC_OR_ENUMERATED || type1 == ANY_TAG) {
                        equivalent = true;
                    }
                } else if (type2 == ANALOGIC_FLOAT || type2 == ANALOGIC_INTEGER) {
                    if (type1 == ANALOGIC || type1 == ANALOGIC_OR_ENUMERATED || type1 == ANY_TAG) {
                        equivalent = true;
                    }
                } else if (type2 == STRING) {
                    if (type1 == ANY_TAG) {
                        equivalent = true;
                    }
                }
            } else {
                equivalent = true;
            }
        }
        return equivalent;
    }

    /**
	 * creates and returns the xml path of an given element of this compiled xml file
	 * @param element an element
	 * @return the xml path of this given element of this compiled xml file
	 */
    public static String getPath(Element element) {
        String path = "";
        if (element != null) {
            Node node = element;
            Element root = element.getOwnerDocument().getDocumentElement();
            path = element.getAttribute("id");
            node = element.getParentNode();
            while (node != null && node instanceof Element) {
                path = ((Element) node).getAttribute("id") + "/" + path;
                if (node.equals(root)) {
                    break;
                }
                node = node.getParentNode();
            }
            if (path.indexOf("/") != -1) {
                path = "/" + path;
            }
        }
        return path;
    }
}

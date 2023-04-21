package com.ssg.tools.jsonxml.xml;

import com.ssg.tools.jsonxml.common.FormatterContext;
import com.ssg.tools.jsonxml.common.Utilities;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simplified XML format provider.
 * Inserts "list" element if root is list or array.
 * Inserts "item" element if root is not list or array.
 * Items within lists are inserted as "item" elements.
 *
 * @author ssg
 */
public class XMLFormatterContext extends FormatterContext {

    static Logger log = Logger.getLogger(XMLFormatterContext.class.getName());

    Stack<String> _names = new Stack<String>();

    public String DEFAULT_LIST_TAG_NAME = "list";

    public String DEFAULT_ITEM_TAG_NAME = "item";

    public XMLFormatterContext() {
        init(false);
    }

    public XMLFormatterContext(boolean indented) {
        init(indented);
    }

    @Override
    protected void init(boolean indented) {
        setPrefix("");
        setNewLine((indented) ? "\n" : "");
        setValueSeparator("");
        setObjectSeparator("");
        setIndentString((indented) ? "    " : "");
        setQuoteName("");
        setQuoteValue("");
        getReplacements().clear();
        getReplacements().put("&", "&amp;");
        getReplacements().put("<", "&lt;");
        getReplacements().put(">", "&gt;");
    }

    @Override
    public String encodeStringValue(String value) {
        return super.encodeStringValue(value);
    }

    String normalizeName(String value) {
        String translateFrom = "/()=?`{}[]^~'*,;<>|@�!#�%&+";
        String translateTo = "___________________________";
        for (int i = 0; i < translateFrom.length(); i++) {
            if (value.indexOf((int) translateFrom.charAt(i)) > -1) {
                value = value.replace(translateFrom.charAt(i), translateTo.charAt(i));
            }
        }
        return value.replace('"', '_').replace('\\', '_');
    }

    @Override
    public String startMap(Map object) {
        Object parent = (getStack().size() > 1) ? getStack().get(getStack().size() - 2) : null;
        if (parent != null && parent instanceof List) {
            if (!_names.isEmpty()) {
                String parentName = _names.peek();
                String childName = Utilities.multiple2single(parentName);
                if (!childName.equals(parentName)) {
                    return "<" + normalizeName(childName) + ">";
                }
            }
            return "<" + DEFAULT_ITEM_TAG_NAME + ">";
        } else {
            return "";
        }
    }

    @Override
    public String endMap(Map object) {
        Object parent = (getStack().size() > 1) ? getStack().get(getStack().size() - 2) : null;
        if (parent != null && parent instanceof List) {
            if (!_names.isEmpty()) {
                String parentName = _names.peek();
                String childName = Utilities.multiple2single(parentName);
                if (!childName.equals(parentName)) {
                    return "</" + normalizeName(childName) + ">";
                }
            }
            return "</" + DEFAULT_ITEM_TAG_NAME + ">";
        } else {
            return "";
        }
    }

    @Override
    public String startList(List object) {
        if (getStack().size() == 1) {
            return "<" + DEFAULT_LIST_TAG_NAME + " " + getElementExtendedAttributes(null, object) + ">";
        } else {
            return "";
        }
    }

    @Override
    public String endList(List object) {
        if (getStack().size() == 1) {
            return "</" + DEFAULT_LIST_TAG_NAME + ">";
        } else {
            return "";
        }
    }

    @Override
    public String startElement(String name, Object object) {
        _names.push(name);
        return "<" + normalizeName(name) + getElementExtendedAttributes(name, object) + ">";
    }

    @Override
    public String endElement(String name, Object object) {
        String savedName = _names.pop();
        if (savedName != name) {
            log.log(Level.SEVERE, "XMLFormat: structure processing error: start element '" + savedName + "' does not match end element '" + name + "'.");
        }
        return "</" + normalizeName(name) + ">";
    }

    @Override
    public String startValue(Object object) {
        if (isCurrentItemInList() && !(object instanceof List || object instanceof Map) && Utilities.isScalarOrString(object)) {
            return "<" + DEFAULT_ITEM_TAG_NAME + ">";
        } else {
            return "";
        }
    }

    @Override
    public String endValue(Object object) {
        if (isCurrentItemInList() && !(object instanceof List || object instanceof Map) && Utilities.isScalarOrString(object)) {
            return "</" + DEFAULT_ITEM_TAG_NAME + ">";
        } else {
            return "";
        }
    }

    public String getElementExtendedAttributes(String name, Object object) {
        String extensions = "";
        return extensions;
    }

    public boolean isCurrentItemInList() {
        Stack stack = getStack();
        return stack.size() > 1 && stack.get(stack.size() - 2) instanceof List;
    }
}

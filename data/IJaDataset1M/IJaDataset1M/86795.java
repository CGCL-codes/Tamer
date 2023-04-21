package com.nexirius.framework.htmlview.function;

import com.nexirius.framework.datamodel.ArrayModel;
import com.nexirius.framework.datamodel.DataModel;
import com.nexirius.framework.datamodel.DataModelEnumeration;
import com.nexirius.framework.htmlview.HTMLParser;
import com.nexirius.framework.htmlview.VariableStore;
import com.nexirius.framework.htmlview.HTMLSessionVariable;

/**
 * Translates the $!array("fieldname", view|edit, "templatename") function.
 * <br>
 * The array function is replaced with a sequence of templates where each template is associated to
 * one array status model.
 */
public class ArrayHTMLFunction extends DefaultHTMLFunction {

    public String getFunctionName() {
        return "array";
    }

    public String getFieldName() {
        return getArgument(0);
    }

    public Boolean getIsEditor() {
        String arg = getArgument(1);
        if (arg == null) {
            return null;
        }
        if (arg.equalsIgnoreCase(HTMLParser.EDIT)) {
            return new Boolean(true);
        }
        if (arg.equalsIgnoreCase(HTMLParser.VIEW)) {
            return new Boolean(false);
        }
        return null;
    }

    public String getTemplate() {
        return getArgument(2);
    }

    public void translate(HTMLSessionVariable sessionVariable, HTMLParser parser) throws Exception {
        String childName = getFieldName();
        DataModel child = parser.getModel().getChild(childName);
        if (!(child instanceof ArrayModel)) {
            if (childName == null) {
                throw new Exception(parser.getModel().getFieldName() + " is not an ArrayModel");
            }
            throw new Exception("Child " + childName + " of " + parser.getModel().getFieldName() + " is not an ArrayModel");
        }
        ArrayModel array = (ArrayModel) child;
        DataModelEnumeration e = array.getEnumeration();
        VariableStore variableStore = parser.getResolver().getVariableStore();
        int index = 0;
        String oldIndex = variableStore.getValueOf(VariableStore.INDEX);
        String oldCount = variableStore.getValueOf(VariableStore.COUNT);
        String template = getTemplate();
        boolean childIsEditor = parser.isEditor();
        Boolean type = getIsEditor();
        if (type != null) {
            childIsEditor = type.booleanValue();
        }
        variableStore.setVariable(VariableStore.COUNT, Integer.toString(array.getSize()));
        while (e.hasMore()) {
            DataModel item = e.next();
            variableStore.setVariable(VariableStore.INDEX, Integer.toString(index + 1));
            byte result[] = parser.getResolver().resolve(sessionVariable, item, template, childIsEditor);
            parser.getOut().write(result);
            ++index;
        }
        variableStore.setVariable(VariableStore.COUNT, oldCount);
        variableStore.setVariable(VariableStore.INDEX, oldIndex);
    }
}

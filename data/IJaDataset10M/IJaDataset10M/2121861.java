package uk.ac.ed.rapid.jsp.output;

import java.util.Stack;
import uk.ac.ed.rapid.jsp.ElementMap;
import uk.ac.ed.rapid.jsp.Util;

public class Item {

    public static final String ELEMENT = "item";

    public static final String VALUE = "value";

    public static final String INDEX = "index";

    public static final String REFRESH = "refresh";

    public static final String LIST = "list";

    public static final String RADIO = "radio";

    public static final String CHECKBOXLIST = "checkboxlist";

    /**
     * Generates the JSP code at the end of an <code>item</code> element, which are
     * .part of a <code>list</code> or <code>radio</code> element.
     *  @param symbol unique name of the symbol.
     *  @param element can take values: 'list', 'radio' or 'checkbox', indicating which type of <code>item</code>  this is.
     *  @param value value that this item can take.
     *  @param refresh if true, add an event that reloads the page if selected.
     *  @param itemContents
     *  @param index a positive number, indicating that this item is supposed to replace one index in a value
     * @return String containing JSP code.
     */
    public static String end(Stack<ElementMap> elementMap, String itemContents) {
        ElementMap variableElement = elementMap.elementAt(elementMap.size() - 3);
        ElementMap parentElement = elementMap.elementAt(elementMap.size() - 2);
        ElementMap itemElement = elementMap.peek();
        String symbol = variableElement.getValue(Variable.SYMBOL);
        String elementName = parentElement.getName();
        String value = itemElement.getValue(VALUE);
        String refresh = (itemElement.getValue(REFRESH) == null) ? "false" : itemElement.getValue(REFRESH);
        String index = (itemElement.getValue(INDEX) == null) ? "-1" : itemElement.getValue(INDEX);
        String result = "";
        if (LIST.equals(elementName)) result += "<%printer = new JSPListItemPrinter(currentPage, VariableResolver.resolve(\"" + value + "\", rapidData), \"" + itemContents + "\");%>\n"; else if (RADIO.equals(elementName)) result += "<%printer = new JSPRadioItemPrinter(currentPage, \"" + symbol + "\" , \"" + refresh + "\", VariableResolver.resolve(\"" + value + "\", rapidData),  \"" + index + "\");%>\n"; else if (CHECKBOXLIST.equals(elementName)) result += "<%printer = new JSPCheckBoxItemPrinter(\"" + symbol + "\" , VariableResolver.resolve(\"" + value + "\", rapidData));%>\n";
        String classOutput = Common.getClassOutput(itemElement);
        if (!classOutput.isEmpty()) result += "<%printer.addAttribute(\"" + classOutput + "\");%>\n";
        String idOutput = Common.getIDOutput(itemElement);
        if (!idOutput.isEmpty()) result += "<%printer.addAttribute(\"" + idOutput + "\");%>\n";
        result += Util.callPrinter(symbol);
        return result;
    }
}

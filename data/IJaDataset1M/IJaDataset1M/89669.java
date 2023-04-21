package uk.ac.ed.rapid.jsp;

import java.util.List;
import java.util.Vector;
import uk.ac.ed.rapid.value.impl.ArrayValue;
import uk.ac.ed.rapid.value.impl.RangeValue;
import uk.ac.ed.rapid.value.impl.SingleValue;

/**
 *
 * @author jos
 * Class prints a value. Follows the visitor pattern.
 */
public class JSPListPrinter implements ValuePrinter {

    private String symbol = null;

    private List<String> attributes = new Vector<String>();

    public JSPListPrinter(String symbol) {
        this.symbol = symbol;
    }

    public JSPListPrinter(String page, String symbol, String refresh) {
        this.symbol = symbol;
        if ("true".equalsIgnoreCase(refresh)) this.attributes.add("onchange=\"document." + page + ".submit()\" ");
    }

    public void addAttribute(String attribute) {
        this.attributes.add(attribute);
    }

    public String print(SingleValue value) {
        String result = "";
        result += "<select name='" + this.symbol + "' style='width: 100%;' ";
        for (String attribute : this.attributes) result += " " + attribute;
        result += ">\n";
        return result;
    }

    public String print(ArrayValue value) {
        String result = "";
        result += "<select name='" + this.symbol + "' MULTIPLE style='width: 100%;'";
        for (String attribute : this.attributes) result += " " + attribute;
        result += ">\n";
        return result;
    }

    public String print(RangeValue value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

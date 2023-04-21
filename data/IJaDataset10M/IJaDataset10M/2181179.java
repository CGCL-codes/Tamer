package fr.brgm.exows.filter;

import java.io.InputStream;
import java.io.StringReader;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import fr.brgm.exows.filter.gml.operators.OpsType;
import fr.brgm.utilities.Loader;

/**
 * @author BRGM
 * @version $Id$
 */
public class Filter {

    private OpsType operator;

    public void addOperator(OpsType operator) {
        this.operator = operator;
    }

    public void setOperator(OpsType operator) {
        this.operator = operator;
    }

    public OpsType getOperator() {
        return operator;
    }

    public static Filter readTest(String fileToRead) throws Exception {
        InputStream in = Loader.getResourceStream(fileToRead);
        Digester digester = DigesterLoader.createDigester(Filter.class.getResource("mapping-Filter.xml"));
        Filter filter = (Filter) digester.parse(in);
        in.close();
        return filter;
    }

    public static Filter parseString(String stringToParse) throws Exception {
        Digester digester = DigesterLoader.createDigester(Filter.class.getResource("mapping-Filter.xml"));
        Filter filter = (Filter) digester.parse(new StringReader(stringToParse));
        return filter;
    }

    public String toGML() {
        StringBuilder sb = new StringBuilder("<ogc:Filter>");
        sb.append(operator.toGML());
        sb.append("</ogc:Filter>");
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Filter" + "\n");
        if (getOperator() != null) sb.append(getOperator().toString());
        return sb.toString();
    }
}

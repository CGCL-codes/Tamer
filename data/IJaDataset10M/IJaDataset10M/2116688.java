package org.hitchhackers.tools.jmx.util.parser.formatter;

import java.util.Collection;
import java.util.Formatter;
import org.apache.log4j.Logger;
import org.hitchhackers.tools.jmx.util.parser.Param;
import org.hitchhackers.tools.jmx.util.parser.ParameterParser;
import org.hitchhackers.tools.jmx.util.parser.Param.ExampleValueFormatter;

public class HelpFormatter {

    private static final Logger LOGGER = Logger.getLogger(HelpFormatter.class);

    private boolean formatForWeb = false;

    public boolean isFormatForWeb() {
        return formatForWeb;
    }

    public void setFormatForWeb(boolean formatForWeb) {
        this.formatForWeb = formatForWeb;
    }

    public String getSyntaxLine(ParameterParser parser) {
        StringBuffer sb = new StringBuffer();
        Collection<Param> params = parser.getParams();
        boolean isFirstParam = true;
        for (Param param : params) {
            LOGGER.debug("formatting syntax line for param : " + param.getName() + "; required : " + param.isRequired());
            ExampleValueFormatter exampleValueFormatter = param.new DefaultExampleValueFormatter();
            if (formatForWeb) exampleValueFormatter = param.new WebExampleValueFormatter();
            if (!isFirstParam) if (formatForWeb) sb.append("&"); else sb.append(" ");
            if (param.isMultiParam()) {
                if (!param.isRequired()) sb.append("[");
                sb.append(exampleValueFormatter.getExampleValue("1"));
                if (formatForWeb) sb.append("&"); else sb.append(" ");
                if (!param.isRequired()) sb.append("[");
                sb.append(exampleValueFormatter.getExampleValue("2"));
                if (!param.isRequired()) {
                    sb.append("]");
                    sb.append("]");
                }
            } else {
                if (!param.isRequired()) sb.append("[");
                sb.append(exampleValueFormatter.getExampleValue(""));
                if (!param.isRequired()) sb.append("]");
            }
            isFirstParam = false;
        }
        return sb.toString();
    }

    public String getParameterDetails(ParameterParser parser) {
        StringBuffer sb = new StringBuffer();
        Collection<Param> params = parser.getParams();
        if (formatForWeb) sb.append("<ul>");
        for (Param param : params) {
            ExampleValueFormatter exampleValueFormatter = param.new DefaultExampleValueFormatter();
            if (formatForWeb) exampleValueFormatter = param.new WebExampleValueFormatter();
            Formatter formatter = new Formatter();
            if (formatForWeb) {
                sb.append("<li>");
                sb.append(formatter.format("<tt>%s</tt>&nbsp;%s", exampleValueFormatter.getExampleValue(null), param.getDescription()));
                sb.append("</li>\n");
            } else {
                sb.append(formatter.format("  %-20s\t%s", exampleValueFormatter.getExampleValue(null), param.getDescription()));
                sb.append("\n");
            }
        }
        if (formatForWeb) sb.append("</ul>\n");
        return sb.toString();
    }
}

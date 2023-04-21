package ca.ucalgary.cpsc.ebe.fitClipseRunner.core.data.xml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ca.ucalgary.cpsc.ebe.fitClipseRunner.core.data.history.HTMLResultDoc;
import ca.ucalgary.cpsc.ebe.fitClipseRunner.core.data.structure.HTMLDoc;
import ca.ucalgary.cpsc.ebe.fitClipseRunner.core.data.structure.TableCell;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Converter {

    static final XStream stream = new XStream(new DomDriver()) {

        {
            alias("td", TableCell.class);
            alias("HTMLDoc", HTMLDoc.class);
            alias("HTMLResultDoc", HTMLResultDoc.class);
            registerConverter(new TableCellConverter());
            registerConverter(new HTMLDocConverter());
            registerConverter(new HTMLResultDocConverter());
        }
    };

    public static String toXml(Object obj) {
        return stream.toXML(obj);
    }

    public static Object fromXml(String xml) {
        String modXml = xml.replaceAll("=([^'\"(&quot;)].*?)(\\s|>|(&gt;))", "=\"$1\"$2");
        modXml = modXml.replaceAll("<hr>", "<hr />");
        modXml = modXml.replaceAll("&nbsp;", "&#160;");
        Matcher m = Pattern.compile("^(<td[^>]*?class=\".*?\".*?>)(.*)").matcher(modXml);
        if (m.matches()) {
            modXml = m.group(1).replaceAll("class=\"", "klass=\"") + m.group(2);
        }
        return stream.fromXML(modXml);
    }
}

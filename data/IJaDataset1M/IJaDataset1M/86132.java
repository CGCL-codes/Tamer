package gov.lanl.adore.diag;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.InputSource;

public class XMLFormatter {

    public static String prettyPrint(String document) {
        try {
            SAXReader xmlReader = new SAXReader(false);
            InputSource is = new InputSource(new StringReader(document));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(out, format);
            writer.write(xmlReader.read(is));
            writer.close();
            return new String(out.toByteArray(), "UTF-8");
        } catch (Exception e) {
            document = e.getMessage();
        }
        return document;
    }

    public static String prettyPrintAsTextArea(String title, String document) {
        title = "<h3>" + title + "</h3>";
        String area = "<textarea cols=\"120\" rows=\"10\" class=\"response\">" + XMLFormatter.prettyPrint(document) + "</textarea>";
        return title + "\n" + area;
    }
}

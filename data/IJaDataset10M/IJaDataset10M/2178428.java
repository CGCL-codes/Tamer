package test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import data.TypedProperty;
import data.xmltree.XMLTag;
import data.xmltree.XMLTreeUtil;

public class TypedPropertyTest {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Hashtable<String, TypedProperty> properties = new Hashtable<String, TypedProperty>();
        properties.put("prop1", new TypedProperty(2.0));
        properties.put("prop2", new TypedProperty(32));
        properties.put("prop3", new TypedProperty(18273));
        properties.put("prop4", new TypedProperty(12.32));
        properties.put("prop5", new TypedProperty("Hello"));
        properties.put("prop6", new TypedProperty("Hello There"));
        properties.put("prop7", new TypedProperty("hello there"));
        properties.put("prop8", new TypedProperty(TypedProperty.DATE, "12/24/07"));
        properties.put("prop9", new TypedProperty(true));
        properties.put("prop10", new TypedProperty(TypedProperty.DATE, Calendar.getInstance()));
        properties.put("prop11", new TypedProperty("Alpha"));
        properties.put("prop12", new TypedProperty(TypedProperty.DATE, "January 3, 2004"));
        TypedProperty propArray[] = properties.values().toArray(new TypedProperty[0]);
        Arrays.sort(propArray);
        for (int i = 0; i < propArray.length; i++) {
            System.out.println("" + i + ": " + propArray[i]);
        }
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            XMLTag props = new XMLTag("props");
            TypedProperty.mapToXML(props, properties);
            doc.appendChild(XMLTreeUtil.convertToXMLDocElement(doc, props));
            OutputFormat format = new OutputFormat(doc);
            format.setIndenting(true);
            XMLSerializer s = new XMLSerializer(System.out, format);
            s.serialize(doc);
            Map<String, TypedProperty> readBack = TypedProperty.mapFromXML(props);
            propArray = readBack.values().toArray(new TypedProperty[0]);
            Arrays.sort(propArray);
            for (int i = 0; i < propArray.length; i++) {
                System.out.println("" + i + ": " + propArray[i]);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

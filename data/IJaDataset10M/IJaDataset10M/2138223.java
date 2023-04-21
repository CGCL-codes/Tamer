package org.gvsig.gpe.gml.parser.v2.features;

import java.io.IOException;
import org.gvsig.gpe.gml.parser.GPEDefaultGmlParser;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;

/**
 * This class parses the gml:name tag. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;gml:name&gt;GML tag name&lt;/gml:name&gt;
 * </code>
 * </pre>
 * </p>
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public class NameBinding {

    /**
	 * It parses the gml:name tag
	 * @param parser
	 * The XML parser
	 * @param handler
	 * The GPE parser that contains the content handler and
	 * the error handler
	 * @return
	 * The name
	 * @throws XmlStreamException
	 * @throws IOException
	 */
    public static String parse(IXmlStreamReader parser, GPEDefaultGmlParser handler) throws XmlStreamException, IOException {
        parser.next();
        String name = parser.getText();
        parser.next();
        return name;
    }
}

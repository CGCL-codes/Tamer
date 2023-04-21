package org.gvsig.gpe.kml.parser.v21.geometries;

import java.io.IOException;
import javax.xml.namespace.QName;
import org.gvsig.gpe.kml.parser.GPEDeafultKmlParser;
import org.gvsig.gpe.kml.utils.Kml2_1_Tags;
import org.gvsig.gpe.parser.ICoordinateIterator;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;
import org.gvsig.gpe.xml.utils.CompareUtils;

/**
 * It parses the outerBoundary tag. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;outerBoundaryIs&gt;
 * &lt;LinearRing&gt;
 * &lt;coordinates&gt;10.0,10.0 10.0,40.0 40.0,40.0 40.0,10.0 10.0,10.0&lt;/coordinates&gt;
 * &lt;/LinearRing&gt;
 * &lt;/outerBoundaryIs&gt;
 * </code>
 * </pre>
 * </p> 
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 * @see http://code.google.com/apis/kml/documentation/kml_tags_21.html#outerboundaryis
 */
public class OuterBoundaryIsBinding {

    /**
	 * It parses the outerBoundaryIs tag
	 * @param parser
	 * The XML parser
	 * @param handler
	 * The GPE parser that contains the content handler and
	 * the error handler
	 * @return
	 * A coordinates iterator
	 * @throws IOException 
	 * @throws XmlStreamException 
	 * @throws XmlStreamException
	 * @throws IOException
	 */
    public ICoordinateIterator parse(IXmlStreamReader parser, GPEDeafultKmlParser handler) throws XmlStreamException, IOException {
        boolean endFeature = false;
        int currentTag;
        QName tag = parser.getName();
        currentTag = parser.getEventType();
        while (!endFeature) {
            switch(currentTag) {
                case IXmlStreamReader.START_ELEMENT:
                    if (CompareUtils.compareWithOutNamespace(tag, Kml2_1_Tags.LINEARRING)) {
                        return handler.getProfile().getLinearRingBinding().parse(parser, handler);
                    }
                    break;
                case IXmlStreamReader.END_ELEMENT:
                    if (CompareUtils.compareWithOutNamespace(tag, Kml2_1_Tags.OUTERBOUNDARYIS)) {
                        endFeature = true;
                    }
                    break;
                case IXmlStreamReader.CHARACTERS:
                    break;
            }
            if (!endFeature) {
                currentTag = parser.next();
                tag = parser.getName();
            }
        }
        return null;
    }
}

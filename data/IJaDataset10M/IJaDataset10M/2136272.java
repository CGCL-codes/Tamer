package org.gvsig.gpe.kml.parser.v21.geometries;

import java.io.IOException;
import javax.xml.namespace.QName;
import org.gvsig.gpe.kml.parser.GPEDeafultKmlParser;
import org.gvsig.gpe.kml.parser.v21.coordinates.CoordinatesTypeIterator;
import org.gvsig.gpe.kml.utils.Kml2_1_Tags;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;
import org.gvsig.gpe.xml.utils.CompareUtils;

/**
 * It parses the LineQName tag. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;LineString&gt;
 * &lt;coord&gt;&lt;X&gt;56.1&lt;/X&gt;&lt;Y&gt;0.45&lt;/Y&gt;&lt;/coord&gt;
 * &lt;coord&gt;&lt;X&gt;67.23&lt;/X&gt;&lt;Y&gt;0.98&lt;/Y&gt;&lt;/coord&gt;
 * &lt;/LineString&gt;
 * </code>
 * </pre>
 * </p> 
 * @author Jorge Piera Llodr� (piera_jor@gva.es)
 * @see http://code.google.com/apis/kml/documentation/kml_tags_21.html#linestring
 */
public class LineStringTypeBinding {

    /**
	 * It parses the LineQName tag
	 * @param parser
	 * The XML parser
	 * @param handler
	 * The GPE parser that contains the content handler and
	 * the error handler
	 * @return
	 * A line string
	 * @throws IOException 
	 * @throws XmlStreamException 
	 * @throws XmlStreamException
	 * @throws IOException
	 */
    public Object parse(IXmlStreamReader parser, GPEDeafultKmlParser handler) throws XmlStreamException, IOException {
        boolean endFeature = false;
        int currentTag;
        Object lineString = null;
        String id = handler.getProfile().getGeometryBinding().getID(parser, handler);
        QName tag = parser.getName();
        currentTag = parser.getEventType();
        while (!endFeature) {
            switch(currentTag) {
                case IXmlStreamReader.START_ELEMENT:
                    if (CompareUtils.compareWithNamespace(tag, Kml2_1_Tags.COORDINATES)) {
                        CoordinatesTypeIterator coordinatesIterator = handler.getProfile().getCoordinatesTypeBinding();
                        coordinatesIterator.initialize(parser, handler, Kml2_1_Tags.LINESTRING);
                        lineString = handler.getContentHandler().startLineString(id, coordinatesIterator, Kml2_1_Tags.DEFAULT_SRS);
                        return lineString;
                    }
                    break;
                case IXmlStreamReader.END_ELEMENT:
                    if (CompareUtils.compareWithNamespace(tag, Kml2_1_Tags.LINESTRING)) {
                        endFeature = true;
                        handler.getContentHandler().endLineString(lineString);
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
        return lineString;
    }
}

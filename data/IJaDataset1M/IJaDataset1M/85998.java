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
 * It parses a Point tag. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;Point gid="P6776"&gt;
 * &lt;coord&gt;&lt;X&gt;50.0&lt;/X&gt;&lt;Y&gt;50.0&lt;/Y&gt;&lt;/coord&gt;
 * &lt;/Point&gt;
 * </code>
 * </pre>
 * </p> 
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 * @see http://code.google.com/apis/kml/documentation/kml_tags_21.html#point
 */
public class PointTypeBinding {

    /**
	 * It parses the Point tag
	 * @param parser
	 * The XML parser
	 * @param handler
	 * The GPE parser that contains the content handler and
	 * the error handler
	 * @return
	 * A point
	 * @throws IOException 
	 * @throws XmlStreamException 
	 * @throws XmlStreamException 
	 * @throws IOException 
	 */
    public Object parse(IXmlStreamReader parser, GPEDeafultKmlParser handler) throws XmlStreamException, IOException {
        boolean endFeature = false;
        int currentTag;
        Object point = null;
        String id = handler.getProfile().getGeometryBinding().getID(parser, handler);
        QName tag = parser.getName();
        currentTag = parser.getEventType();
        while (!endFeature) {
            switch(currentTag) {
                case IXmlStreamReader.START_ELEMENT:
                    if (CompareUtils.compareWithNamespace(tag, Kml2_1_Tags.COORDINATES)) {
                        CoordinatesTypeIterator coordinatesIteartor = handler.getProfile().getCoordinatesTypeBinding();
                        coordinatesIteartor.initialize(parser, handler, Kml2_1_Tags.POINT);
                        point = handler.getContentHandler().startPoint(id, coordinatesIteartor, Kml2_1_Tags.DEFAULT_SRS);
                        return point;
                    }
                    break;
                case IXmlStreamReader.END_ELEMENT:
                    if (CompareUtils.compareWithNamespace(tag, Kml2_1_Tags.POINT)) {
                        endFeature = true;
                        handler.getContentHandler().endPoint(point);
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
        return point;
    }
}

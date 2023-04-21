package org.gvsig.gpe.gml.parser.v2.geometries;

import java.io.IOException;
import javax.xml.namespace.QName;
import org.gvsig.gpe.gml.parser.GPEDefaultGmlParser;
import org.gvsig.gpe.gml.utils.GMLTags;
import org.gvsig.gpe.parser.ICoordinateIterator;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;
import org.gvsig.gpe.xml.utils.CompareUtils;

/**
 * It parses a gml:PolygonType object. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;Polygon gid="_877789"&gt;
 * &lt;outerBoundaryIs&gt;
 * &lt;LinearRing&gt;
 * &lt;coordinates&gt;0.0,0.0 100.0,0.0 50.0,100.0 0.0,0.0&lt;/coordinates&gt;
 * &lt;/LinearRing&gt;
 * &lt;/outerBoundaryIs&gt;
 * &lt;/Polygon&gt;
 * </code>
 * </pre>
 * </p> 
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public class PolygonTypeBinding extends GeometryBinding {

    protected Object polygon = null;

    /**
	 * It parses the gml:Polygon tag
	 * @param parser
	 * The XML parser
	 * @param handler
	 * The GPE parser that contains the content handler and
	 * the error handler
	 * @return
	 * A polygon
	 * @throws XmlStreamException
	 * @throws IOException
	 */
    public Object parse(IXmlStreamReader parser, GPEDefaultGmlParser handler) throws XmlStreamException, IOException {
        boolean endFeature = false;
        int currentTag;
        super.setAtributtes(parser, handler.getErrorHandler());
        QName tag = parser.getName();
        currentTag = parser.getEventType();
        while (!endFeature) {
            switch(currentTag) {
                case IXmlStreamReader.START_ELEMENT:
                    polygon = parseTag(parser, handler, tag, id, srsName);
                    break;
                case IXmlStreamReader.END_ELEMENT:
                    endFeature = parseLastTag(parser, handler, tag);
                    if (endFeature) {
                        handler.getContentHandler().endPolygon(polygon);
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
        return polygon;
    }

    /**
	 * It parses the XML tag
	 * @param parser
	 * @param handler
	 * @param tag
	 * @param id
	 * @param srsName
	 * @return
	 * @throws XmlStreamException
	 * @throws IOException
	 */
    protected Object parseTag(IXmlStreamReader parser, GPEDefaultGmlParser handler, QName tag, String id, String srsName) throws XmlStreamException, IOException {
        if (CompareUtils.compareWithNamespace(tag, GMLTags.GML_OUTERBOUNDARYIS)) {
            OuterBoundaryIsTypeBinding coordinatesBinding = handler.getProfile().getOuterBoundaryIsTypeBinding();
            ICoordinateIterator coordinatesIterator = coordinatesBinding.parse(parser, handler);
            this.polygon = handler.getContentHandler().startPolygon(id, coordinatesIterator, srsName);
        } else if (CompareUtils.compareWithNamespace(tag, GMLTags.GML_INNERBOUNDARYIS)) {
            InnerBoundaryIsTypeBinding coordinatesBinding = handler.getProfile().getInnerBoundaryIsTypeBinding();
            ICoordinateIterator coordinatesIterator = coordinatesBinding.parse(parser, handler);
            Object innerPolygon = handler.getContentHandler().startInnerPolygon(null, coordinatesIterator, srsName);
            handler.getContentHandler().endInnerPolygon(innerPolygon);
            handler.getContentHandler().addInnerPolygonToPolygon(innerPolygon, this.polygon);
        }
        return polygon;
    }

    /**
	 * Parses the last tag
	 * @param parser
	 * @param handler
	 * @param tag
	 * @return
	 */
    protected boolean parseLastTag(IXmlStreamReader parser, GPEDefaultGmlParser handler, QName tag) {
        if (CompareUtils.compareWithNamespace(tag, GMLTags.GML_POLYGON)) {
            return true;
        }
        return false;
    }
}

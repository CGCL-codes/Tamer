package org.gvsig.gpe.gml.parser.v2.geometries;

import java.io.IOException;
import javax.xml.namespace.QName;
import org.gvsig.gpe.gml.parser.GPEDefaultGmlParser;
import org.gvsig.gpe.gml.utils.GMLTags;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;
import org.gvsig.gpe.xml.utils.CompareUtils;

/**
 * It parses a gml:pointPropertyType object. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;pointProperty&gt;
 * &lt;Point gid="P6776"&gt;
 * &lt;coord&gt;&lt;X&gt;50.0&lt;/X&gt;&lt;Y&gt;50.0&lt;/Y&gt;&lt;/coord&gt;
 * &lt;/Point&gt;
 * &lt;/pointProperty&gt;
 * </code>
 * </pre>
 * </p> 
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public class PointPropertyTypeBinding {

    /**
	 * It parses the gml:pointProperty tag
	 * @param parser
	 * The XML parser
	 * @param handler
	 * The GPE parser that contains the content handler and
	 * the error handler
	 * @return
	 * A point
	 * @throws XmlStreamException
	 * @throws IOException
	 */
    public Object parse(IXmlStreamReader parser, GPEDefaultGmlParser handler) throws XmlStreamException, IOException {
        boolean endFeature = false;
        int currentTag;
        Object point = null;
        QName tag = parser.getName();
        currentTag = parser.getEventType();
        while (!endFeature) {
            switch(currentTag) {
                case IXmlStreamReader.START_ELEMENT:
                    if (CompareUtils.compareWithNamespace(tag, GMLTags.GML_POINT)) {
                        point = handler.getProfile().getPointTypeBinding().parse(parser, handler);
                    }
                    break;
                case IXmlStreamReader.END_ELEMENT:
                    if ((CompareUtils.compareWithNamespace(tag, GMLTags.GML_POINTPROPERTY)) || (CompareUtils.compareWithNamespace(tag, GMLTags.GML_LOCATION)) || (CompareUtils.compareWithNamespace(tag, GMLTags.GML_CENTEROF)) || (CompareUtils.compareWithNamespace(tag, GMLTags.GML_POSITION))) {
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
        return point;
    }
}

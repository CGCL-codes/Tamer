package org.gvsig.gpe.kml.writer.v21.features;

import java.io.IOException;
import javax.xml.namespace.QName;
import org.gvsig.gpe.kml.utils.Kml2_1_Tags;
import org.gvsig.gpe.kml.writer.GPEKmlWriterHandlerImplementor;
import org.gvsig.gpe.xml.stream.IXmlStreamWriter;

/**
 * This is an abstract element and cannot be used 
 * directly in a KML file. Document, Folder, NetworkLink,
 * Placemark, GroundOverLay and ScreenOverlay inherit from
 * it. It writes the common attributes
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 * @see http://code.google.com/apis/kml/documentation/kml_tags_21.html#feature
 */
public class FeatureWriter {

    /**
	 * It writes the init tag and the common attributes
	 * for a Document, Folder, NetworkLink, Placemark, 
	 * GroundOverLay or a ScreenOverlay.
	 * @param writer
	 * Writer to write the labels
	 * @param handler
	 * The writer handler implementor
	 * @param id
	 * Feature ID
	 * @param tagName
	 * Feature type
	 * @throws IOException
	 */
    public void start(IXmlStreamWriter writer, GPEKmlWriterHandlerImplementor handler, String id, QName tagName) throws IOException {
        writer.writeStartElement(tagName);
        if (id != null) {
            writer.writeStartAttribute(Kml2_1_Tags.ID);
            writer.writeValue(id);
        }
        writer.writeEndAttributes();
    }

    /**
	 * It writes the end tag of a Document, Folder, NetworkLink, 
	 * Placemark, GroundOverLay or a ScreenOverlay.
	 * @param writer
	 * Writer to write the labels
	 * @param handler
	 * The writer handler implementor
	 * @param tagName
	 * Feature type
	 * @throws IOException
	 */
    public void end(IXmlStreamWriter writer, GPEKmlWriterHandlerImplementor hanlder, QName tagName) throws IOException {
        writer.writeEndElement();
    }
}

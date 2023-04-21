package org.gvsig.gpe.kml.writer.v21.geometries;

import java.io.IOException;
import org.gvsig.gpe.kml.utils.Kml2_1_Tags;
import org.gvsig.gpe.kml.writer.GPEKmlWriterHandlerImplementor;
import org.gvsig.gpe.writer.ICoordinateSequence;
import org.gvsig.gpe.xml.stream.IXmlStreamWriter;

/**
 * This class writes a Region Kml tag. Example:
 * <p>
 * <pre>
 * <code> 
 * &lt;Region&gt;
 * &lt;LatLonAltBox&gt;
 * &lt;north&gt;43.374&lt;/north&gt;
 * &lt;south&gt;42.983&lt;/south&gt;
 * &lt;east&gt;-0.335&lt;/east&gt;
 * &lt;west&gt;-1.423&lt;/west&gt;
 * &lt;rotation&gt;39.37878630116985&lt;/rotation&gt;
 * &lt;/LatLonAltBox&gt;
 * &lt;/Region&gt;
 * </code>
 * </pre>
 * </p> 
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 * @see http://code.google.com/apis/kml/documentation/kml_tags_21.html#region
 */
public class RegionWriter {

    /**
	 * It writes the Region init tag and its fields
	 * @param writer
	 * Writer to write the labels
	 * @param handler
	 * The writer handler implementor
	 * @param coords
	 * A coordinates iterator. 
	 * @throws IOException
	 */
    public void start(IXmlStreamWriter writer, GPEKmlWriterHandlerImplementor handler, ICoordinateSequence coords) throws IOException {
        writer.writeStartElement(Kml2_1_Tags.REGION);
        handler.getProfile().getLatLonAltBoxWriter().start(writer, handler, coords);
    }

    /**
	/**
	 * It writes the LatLonAltBox end tag and its fields
	 * @param writer
	 * Writer to write the labels
	 *  @param handler
	 * The writer handler implementor
	 * @throws IOException
	 */
    public void end(IXmlStreamWriter writer, GPEKmlWriterHandlerImplementor handler) throws IOException {
        handler.getProfile().getLatLonAltBoxWriter().end(writer, handler);
        writer.writeEndElement();
    }
}

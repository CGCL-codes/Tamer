package org.gvsig.gpe.gml.writer.v2.geometries;

import java.io.IOException;
import org.gvsig.gpe.gml.utils.GMLTags;
import org.gvsig.gpe.gml.writer.GPEGmlWriterHandlerImplementor;
import org.gvsig.gpe.writer.ICoordinateSequence;
import org.gvsig.gpe.xml.stream.EventType;
import org.gvsig.gpe.xml.stream.IXmlStreamWriter;

/**
 * It writes a gml:CoordinatesType object. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;coordinates&gt;60.0,60.0 60.0,90.0 90.0,90.0&lt;/coordinates&gt;
 * </code>
 * </pre>
 * </p> 
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public class CoordinatesWriter {

    /**
	 * It writes a gml:coordinates tag list
	 * @param writer
	 * Writer to write the labels
	 * @param handler
	 * The writer handler implementor
	 * @param coords
	 * A coordinates sequence
	 * @throws IOException
	 */
    public void write(IXmlStreamWriter writer, GPEGmlWriterHandlerImplementor handler, ICoordinateSequence coords) throws IOException {
        writer.writeStartElement(GMLTags.GML_COORDINATES);
        writer.startArray(EventType.VALUE_DOUBLE, coords.getSize() * coords.iterator().getDimension());
        double[] buffer = new double[coords.iterator().getDimension()];
        while (coords.iterator().hasNext()) {
            coords.iterator().next(buffer);
            writer.writeValue(buffer, 0, buffer.length);
        }
        writer.endArray();
        writer.writeEndElement();
    }
}

package org.gvsig.gpe.gml.writer.v2.features;

import java.io.IOException;
import org.gvsig.gpe.gml.utils.GMLTags;
import org.gvsig.gpe.gml.writer.GPEGmlWriterHandlerImplementor;
import org.gvsig.gpe.xml.stream.IXmlStreamWriter;

/**
 * This class writes the gml:description tag. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;gml:gid&gt;5&lt;/gml:gid&gt;
 * </code>
 * </pre>
 * </p>
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public class GidWriter {

    /**
	 * It writes a GID tag
	 * @param writer
	 * Writer to write the labels
	 * @param handler
	 * The writer handler implementor
	 * @param id
	 * ID value
	 * @throws IOException
	 */
    public void write(IXmlStreamWriter writer, GPEGmlWriterHandlerImplementor handler, String id) throws IOException {
        if (id != null) {
            writer.writeStartElement(GMLTags.GML_GID);
            writer.writeValue(id);
            writer.writeEndElement();
        }
    }
}

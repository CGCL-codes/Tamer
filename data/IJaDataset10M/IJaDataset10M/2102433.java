package org.gvsig.gpe.gml.writer.v2.features;

import java.io.IOException;
import org.gvsig.gpe.gml.utils.GMLTags;
import org.gvsig.gpe.gml.writer.GPEGmlWriterHandlerImplementor;
import org.gvsig.gpe.xml.stream.IXmlStreamWriter;

/**
 * This class writes the gml:name tag. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;gml:name&gt;GML tag name&lt;/gml:name&gt;
 * </code>
 * </pre>
 * </p>
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public class NameWriter {

    /**
	 * It writes a gml:name tag
	 * @param writer
	 * Writer to write the labels
	 * @param handler
	 * The writer handler implementor
	 * @param name
	 * Name to write
	 * @throws IOException
	 */
    public void write(IXmlStreamWriter writer, GPEGmlWriterHandlerImplementor handler, String name) throws IOException {
        if (name != null) {
            writer.writeStartElement(GMLTags.GML_NAME);
            writer.writeValue(name);
            writer.writeEndElement();
        }
    }
}

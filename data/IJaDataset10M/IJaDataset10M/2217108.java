package org.gvsig.gpe.gml.writer.v2.features;

import java.io.IOException;
import org.gvsig.gpe.GPEDefaults;
import org.gvsig.gpe.gml.writer.GPEGmlWriterHandlerImplementor;
import org.gvsig.gpe.xml.XmlProperties;
import org.gvsig.gpe.xml.stream.IXmlStreamWriter;

/**
 * This class writes the Elemtent tag. One Element
 * is a feature attribute.
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public class ElementWriter {

    /**
	 * It writes an Element tag
	 * @param writer
	 * Writer to write the labels
	 * @param handler
	 * The writer handler implementor
	 * @param namespace
	 * Element namespcae
	 * @param name
	 * Element name
	 * @param value
	 * Element value
	 * @throws IOException
	 */
    public void start(IXmlStreamWriter writer, GPEGmlWriterHandlerImplementor handler, String namespace, String name, Object value) throws IOException {
        if (namespace != null) {
            writer.writeStartElement(namespace, name);
        } else {
            writer.writeStartElement(GPEDefaults.getStringProperty(XmlProperties.DEFAULT_NAMESPACE_URI), name);
        }
        if (value != null) {
            writer.writeValue(value.toString());
        }
    }

    /**
	 * It writes an end Element tag
	 * @param writer
	 * Writer to write the labels
	 * @param handler
	 * The writer handler implementor
	 * @param namespace
	 * Element namespace
	 * @param name
	 * Element name
	 * @throws IOException
	 */
    public void end(IXmlStreamWriter writer, GPEGmlWriterHandlerImplementor hanlder, String namespace, String name) throws IOException {
        writer.writeEndElement();
    }
}

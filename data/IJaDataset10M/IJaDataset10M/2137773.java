package org.achup.generador.project.writer.generator.content;

import org.achup.generador.generator.content.PhoneStringGenerator;
import org.achup.generador.project.writer.ElementWriter;
import org.achup.generador.project.writer.ElementWriterException;
import org.achup.generador.project.writer.WriterHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Marco Bassaletti Olivos.
 */
public class PhoneStringGeneratorWriter implements ElementWriter<PhoneStringGenerator> {

    public Element createXML(Document document, PhoneStringGenerator object) throws ElementWriterException {
        Element element = document.createElement("phone");
        WriterHelper.writeAttribute(element, "template", object.getTemplate());
        WriterHelper.writeAttribute(element, "random-seed", object.getSeed());
        return element;
    }
}

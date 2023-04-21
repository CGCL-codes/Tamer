package org.formaria.xml.nanoxml;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import net.n3.nanoxml.XMLElement;
import net.n3.nanoxml.XMLWriter;
import org.formaria.xml.XmlElement;
import org.formaria.xml.XmlWriter;

/**
 * <p> Copyright (c) Formaria Ltd., 2008</p>
 * <p> $Revision: 2.2 $</p>
 * <p> License: see License.txt</p>
 */
public class NanoXmlWriter extends XMLWriter implements XmlWriter {

    /**
   * ctor which takes the OutputStream as a parameter.
   * @param stream The OutputStream to which the contents will be written
   */
    public NanoXmlWriter(OutputStream stream) {
        super(stream);
    }

    /**
   * ctor which takes the Writer as a parameter.
   * @param writer The Writer to which the contents will be written
   */
    public NanoXmlWriter(Writer writer) {
        super(writer);
    }

    /**
   * Write the contents of an XmlElement out to the outputstream specified by
   * setOutputStream
   * @param xml The root XmlElement to be output
   * @param prettyPrint If true the XML will be output in a more readable format
   * @param indent If true the XML will be indented
   * @throws java.io.IOException Throw an IOException if anything goes wrong
   */
    public void write(XmlElement xml, boolean prettyPrint, int indent) throws IOException {
        super.write((XMLElement) xml.getImplementation(), prettyPrint, indent);
    }

    /**
   * Set the OutputStream to be used by the write function. This needs to be set
   * before writing.
   * @param fos The FileOutputStream to which the XML will be written
   */
    public void setOutputStream(FileOutputStream fos) {
    }
}

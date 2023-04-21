package net.sf.jelly.apt;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.XMLOutput;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.Writer;

/**
 * Output implementation for jelly templates.
 *
 * @author Ryan Heaton
 */
public class JellyTemplateOutput implements TemplateOutput<APTJellyTag> {

    private XMLOutput output;

    public JellyTemplateOutput(XMLOutput output) {
        this.output = output;
    }

    public void redirect(APTJellyTag block, Writer writer) throws IOException, TemplateException {
        this.output = XMLOutput.createXMLOutput(writer);
    }

    public void write(APTJellyTag block) throws IOException, TemplateException {
        try {
            block.invokeBody(this.output);
        } catch (JellyTagException e) {
            throw new TemplateException(e);
        }
    }

    public void write(String value) throws IOException, TemplateException {
        try {
            this.output.write(value);
        } catch (SAXException e) {
            throw new TemplateException(e);
        }
    }
}

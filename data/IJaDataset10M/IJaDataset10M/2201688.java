package de.psisystems.dmachinery.jobs;

import java.net.URL;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import de.psisystems.dmachinery.core.exeptions.PrintException;
import de.psisystems.dmachinery.xml.AbstractMarshallable;
import de.psisystems.dmachinery.xml.XMLHelper;

public class SimpleTemplate extends AbstractMarshallable implements Template {

    private Properties properties = new Properties();

    private Set<Block> blocks = new HashSet<Block>();

    private URL url;

    @SuppressWarnings("unused")
    private SimpleTemplate() {
    }

    public SimpleTemplate(URL url) {
        this(url, null, null);
    }

    public SimpleTemplate(URL url, Properties properties) {
        this(url, properties, null);
    }

    public SimpleTemplate(URL url, Properties properties, Set<Block> blocks) {
        super();
        this.properties = properties;
        this.blocks = blocks;
        this.url = url;
    }

    public Properties getProperties() {
        return properties;
    }

    public Set<Block> getBlocks() {
        return blocks;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public void setAttributes(Properties properties) {
        this.properties = properties;
    }

    public void setBlocks(Set<Block> blocks) {
        this.blocks = blocks;
    }

    @Override
    public void toXML(XMLStreamWriter streamWriter, boolean standalone) throws PrintException {
        try {
            if (standalone) {
                XMLHelper.writeStartDocument(streamWriter, "UTF-8", "1.0");
            }
            XMLHelper.writeStartElement(streamWriter, "template");
            XMLHelper.writeStartElement(streamWriter, "url");
            if (url != null) {
                XMLHelper.writeData(streamWriter, url.toExternalForm());
            }
            XMLHelper.writeEndElement(streamWriter, "url");
            XMLHelper.writeStartElement(streamWriter, "attributes");
            XMLHelper.writeAttributes(streamWriter, properties);
            XMLHelper.writeEndElement(streamWriter, "attributes");
            XMLHelper.writeEndElement(streamWriter, "template");
        } catch (XMLStreamException e) {
            throw new PrintException(e.getMessage(), e);
        }
    }
}

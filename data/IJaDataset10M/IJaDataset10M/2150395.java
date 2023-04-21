package de.fzj.pkikits.conf;

import java.io.File;
import java.io.IOException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class XmlConfigSource extends ConfigSource {

    private Document doc;

    private Element root;

    public XmlConfigSource(String file) throws IOException {
        SAXBuilder builder = new SAXBuilder();
        try {
            doc = builder.build(new File(file));
            root = doc.getRootElement();
        } catch (JDOMException e) {
            logger.debug(e);
            e.printStackTrace();
        }
    }

    public ConfigValue getValue(String key) {
        return (new XmlConfigValue(root)).getPath(key, "/");
    }
}

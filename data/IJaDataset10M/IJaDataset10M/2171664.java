package jp.co.withone.osgi.gadget.upnp;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class CommonParser {

    public static Contents parseContents(String str) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(str.getBytes());
        return parseContents(bais);
    }

    public static Contents parseContents(InputStream is) throws Exception {
        XMLReader reader = XMLReaderFactory.createXMLReader();
        ContainerHandler handler = new ContainerHandler();
        reader.setContentHandler(handler);
        reader.parse(new InputSource(is));
        final Contents contents = handler.getContents();
        handler.clear();
        return contents;
    }
}

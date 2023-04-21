package fiswidgets.fisutils;

import org.xml.sax.*;

public class XmlEntityResolver implements EntityResolver {

    private final String DTD_document = "fiswidgets.dtd";

    private final String DTD_publicID = "-//Fiswidgets//XML Serialized Document 1.0//EN";

    public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
        if (publicId.equals(DTD_publicID)) {
            InputSource is = new InputSource(ClassLoader.getSystemResource(DTD_document).toString());
            is.setPublicId(DTD_publicID);
            return is;
        } else throw new SAXException("Invalid DOCTYPE in the document");
    }
}

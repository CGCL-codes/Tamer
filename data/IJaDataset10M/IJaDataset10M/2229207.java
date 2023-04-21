package org.iwtemplatingj;

import java.io.IOException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class IwovDcrParserEntityResolver implements EntityResolver {

    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        if (systemId.endsWith(Constants.IWOV_STYLE_DCR_DTD)) return new InputSource(Constants.IWOV_STYLE_DCR_DTD_URL.openStream());
        return null;
    }
}

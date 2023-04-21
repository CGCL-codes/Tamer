package org.docflower.util;

import java.io.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamSource;
import org.docflower.resources.ResourceManager;
import org.docflower.util.LowLevelException;

public class XslURIResolver implements URIResolver {

    @Override
    public Source resolve(String href, String base) throws TransformerException {
        StreamSource result = null;
        try {
            InputStream is = ResourceManager.getInstance().getResourceInputStream(href);
            result = new StreamSource(is, href);
        } catch (IOException e) {
            throw new LowLevelException(Messages.XslURIResolver_UnableToResolveXslHref + href + Messages.XslURIResolver_Details + e.getMessage());
        }
        return result;
    }
}

package au.edu.archer.metadata.mdsr.utils.validator;

import org.apache.log4j.Logger;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

public class DebugResourceResolver implements LSResourceResolver {

    private final Logger logger = Logger.getLogger(DebugResourceResolver.class);

    private LSResourceResolver rr;

    public DebugResourceResolver(LSResourceResolver rr) {
        this.rr = rr;
    }

    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
        logger.trace("resolveResource:" + f(type) + f(namespaceURI) + f(publicId) + f(systemId) + f(baseURI));
        return (rr == null) ? null : rr.resolveResource(type, namespaceURI, publicId, systemId, baseURI);
    }

    private String f(String str) {
        return (str == null) ? " null" : (" '" + str + "'");
    }
}

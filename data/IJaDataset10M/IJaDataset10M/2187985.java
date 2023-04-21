package net.wotonomy.web;

import java.util.Enumeration;
import net.wotonomy.foundation.NSData;
import net.wotonomy.foundation.NSMutableDictionary;

/**
* An implementation of WORequestHandler that  
* retrieves resources from the deployed application.
*
* @author michael@mpowers.net
* @author $Author: cgruber $
* @version $Revision: 905 $
*/
public class WOResourceRequestHandler extends WORequestHandler {

    private NSMutableDictionary resourceCache;

    private WOResourceManager resourceManager;

    public WOResourceRequestHandler() {
        resourceCache = new NSMutableDictionary();
        resourceManager = WOApplication.application().resourceManager();
    }

    public WOResponse handleRequest(WORequest aRequest) {
        WOResponse response = new WOResponse();
        StringBuffer buf = new StringBuffer();
        String framework = null;
        Enumeration e = aRequest.requestHandlerPathArray().objectEnumerator();
        if (e.hasMoreElements()) {
            framework = e.nextElement().toString();
            if (framework.equals("application")) {
                buf.append('/').append(framework);
                framework = null;
            }
        }
        if (e.hasMoreElements()) {
            buf.append(e.nextElement());
        }
        while (e.hasMoreElements()) {
            buf.append('/').append(e.nextElement());
        }
        String resource;
        if (buf.length() > 0) {
            resource = buf.toString();
            byte[] data = resourceManager.bytesForResourceNamed(resource, framework, aRequest.browserLanguages());
            if (data != null) {
                response.setHeader(resourceManager.contentTypeForResourceNamed(resource), "Content-Type");
                response.setContent(new NSData(data));
                return response;
            }
        }
        response.setStatus(404);
        return response;
    }
}

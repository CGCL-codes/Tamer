package org.pustefixframework.webservices;

import java.io.IOException;
import java.io.OutputStream;
import org.pustefixframework.webservices.config.ServiceConfig;

/**
 * @author mleidig@schlund.de
 */
public interface ServiceStubGenerator {

    public void generateStub(ServiceConfig service, String requestPath, OutputStream out) throws ServiceException, IOException;
}

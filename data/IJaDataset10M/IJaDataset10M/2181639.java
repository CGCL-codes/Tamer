package de.schlund.pfixcore.webservice;

import java.io.IOException;
import java.io.OutputStream;
import de.schlund.pfixcore.webservice.config.ServiceConfig;

/**
 * @author mleidig@schlund.de
 */
public interface ServiceStubGenerator {

    public void generateStub(ServiceConfig service, OutputStream out) throws ServiceException, IOException;
}

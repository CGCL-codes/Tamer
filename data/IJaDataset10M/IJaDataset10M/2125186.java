package org.broadleafcommerce.gwt.security;

import org.gwtwidgets.server.spring.RPCServiceExporter;
import org.gwtwidgets.server.spring.RPCServiceExporterFactory;

/**
 * 
 * @author jfischer
 */
public class CompatibleGWTSecuredRPCServiceExporterFactory implements RPCServiceExporterFactory {

    public RPCServiceExporter create() {
        CompatibleGWTSecuredRPCServiceExporter exporter = new CompatibleGWTSecuredRPCServiceExporter();
        return exporter;
    }
}

package org.dcm4chee.xdsa.registry.ws;

import java.util.List;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.xml.bind.JAXBException;
import org.dcm4chee.xds.common.XDSConstants;
import org.dcm4chee.xds.common.exception.XDSException;
import org.dcm4chee.xds.infoset.v21.ErrorType;
import org.dcm4chee.xds.infoset.v21.ObjectFactory;
import org.dcm4chee.xds.infoset.v21.RegistryError;
import org.dcm4chee.xds.infoset.v21.RegistryErrorList;
import org.dcm4chee.xds.infoset.v21.RegistryResponse;
import org.dcm4chee.xds.infoset.v21.SubmitObjectsRequest;
import org.jboss.mx.util.MBeanServerLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author franz.willer@agfa.com
 * @version $Revision: 2389 $ $Date: 2006-04-05 11:55:23 +0200 (Mi, 05 Apr 2006) $
 * @since Mar 08, 2006
 */
public class XDSaRegistryDelegate {

    private static final String DEFAULT_XDS_REGISTRY_SERVICE_NAME = "dcm4chee.xds:service=XDSRegistryService";

    private static ObjectName xdsRegistryServiceName = null;

    private static MBeanServer server;

    private ObjectFactory objFac = new ObjectFactory();

    private Logger log = LoggerFactory.getLogger(XDSaRegistryDelegate.class);

    public XDSaRegistryDelegate() {
        init();
    }

    public void init() {
        if (server != null) return;
        server = MBeanServerLocator.locate();
        String s = DEFAULT_XDS_REGISTRY_SERVICE_NAME;
        try {
            xdsRegistryServiceName = new ObjectName(s);
        } catch (Exception e) {
            log.error("Exception in init! Cant create xdsRegistryServiceName for " + s, e);
        }
    }

    /**
     * Makes the MBean call to export the document packed in a SOAP message.
     * @param submitRequest 
     * 
     * @param msg The document(s) packed in a SOAP message.
     * 
     * @return List of StoredDocument objects.
     */
    public RegistryResponse registerDocuments(SubmitObjectsRequest req) throws JAXBException {
        try {
            return (RegistryResponse) server.invoke(xdsRegistryServiceName, "registerDocumentsA", new Object[] { req }, new String[] { SubmitObjectsRequest.class.getName() });
        } catch (Exception e) {
            XDSException x = (e instanceof XDSException) ? (XDSException) e : (e.getCause() instanceof XDSException) ? (XDSException) e.getCause() : new XDSException(XDSConstants.XDS_ERR_REGISTRY_ERROR, "Unexpected error in registerDocuments of XDS Registry service !: " + e.getMessage(), e);
            log.error("XDS.a registerDocuments transaction failed!", x);
            return getErrorRegistryResponse(x);
        }
    }

    public RegistryResponse getErrorRegistryResponse(XDSException x) throws JAXBException {
        RegistryResponse rsp = objFac.createRegistryResponse();
        rsp.setStatus(XDSConstants.XDS_A_STATUS_FAILURE);
        RegistryErrorList errList = objFac.createRegistryErrorList();
        List<RegistryError> errors = errList.getRegistryError();
        rsp.setRegistryErrorList(errList);
        RegistryError error = getRegistryError(x);
        errors.add(error);
        return rsp;
    }

    private RegistryError getRegistryError(XDSException xdsException) throws JAXBException {
        RegistryError error = objFac.createRegistryError();
        error.setErrorCode(xdsException.getErrorCode());
        error.setCodeContext(xdsException.getMsg());
        ErrorType sev = XDSConstants.XDS_ERR_SEVERITY_WARNING.equals(xdsException.getSeverity()) ? ErrorType.WARNING : ErrorType.ERROR;
        error.setSeverity(sev);
        error.setLocation(xdsException.getLocation());
        error.setValue(xdsException.getMsg());
        return error;
    }
}

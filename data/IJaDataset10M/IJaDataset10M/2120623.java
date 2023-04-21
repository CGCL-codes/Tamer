package org.infoeng.x2005.x10.icws;

import org.apache.ws.resource.Resource;
import org.apache.ws.resource.ResourceContext;
import org.apache.ws.resource.ResourceContextException;
import org.apache.ws.resource.ResourceException;
import org.apache.ws.resource.ResourceUnknownException;
import org.apache.ws.resource.NamespaceVersionHolder;
import org.apache.ws.resource.impl.AbstractResourceHome;
import org.apache.ws.addressing.EndpointReference;
import javax.xml.namespace.QName;
import java.io.Serializable;
import org.infoeng.icws.util.ICWSConstants;

/**
 * The home for ICWS resources.
 * <p />
 * NOTE: This file is generated, but is meant to be modified.
 *       It will NOT be overwritten by subsequent runs of Wsdl2Java.
 */
public class ICWSHome extends AbstractICWSHome implements Serializable {

    private static final QName SERVICE_NAME = QName.valueOf("{http://infoeng.org/2005/10/icws#}ICWS");

    private static final QName PORT_TYPE = QName.valueOf("{http://infoeng.org/2005/10/icws#}ICWSServerImpl");

    private static final String PORT_NAME = "ICWS";

    private static final QName RESOURCE_KEY_NAME = QName.valueOf("{http://infoeng.org/2005/10/icws#}ResourceIdentifier");

    private static final org.apache.ws.notification.base.v2004_06.impl.WsnNamespaceVersionHolderImpl SPEC_NAMESPACE_SET = new org.apache.ws.notification.base.v2004_06.impl.WsnNamespaceVersionHolderImpl();

    /**
     * Create and add any resource instances.
     *
     * @throws Exception on error
     */
    public void init() throws Exception {
        super.init();
        ICWSResource instance1 = (ICWSResource) createInstance(ICWSConstants.ICWS_RESOURCE_ID);
        add(instance1);
    }

    public QName getServiceName() {
        return SERVICE_NAME;
    }

    public QName getPortType() {
        return PORT_TYPE;
    }

    public String getServicePortName() {
        return PORT_NAME;
    }

    public QName getResourceKeyNameQName() {
        return RESOURCE_KEY_NAME;
    }

    public NamespaceVersionHolder getNamespaceSet() {
        return SPEC_NAMESPACE_SET;
    }
}

package com.intel.gpe.clients.gpe4gtk.jms.workflow;

import java.util.List;
import com.intel.gpe.clients.api.Constants;
import com.intel.gpe.clients.api.WSRFClient;
import com.intel.gpe.clients.api.workflow.CustomizableDataStagingSetup;
import com.intel.gpe.clients.api.workflow.DataStagingSetup;
import com.intel.gpe.clients.api.workflow.GetResourceProperty;
import com.intel.gpe.clients.api.workflow.PartnerLink;
import com.intel.gpe.clients.api.workflow.PartnerLinkValue;
import com.intel.gpe.clients.api.workflow.Variable;
import com.intel.gpe.clients.api.workflow.WorkflowConstants;
import com.intel.gpe.clients.gpe.GPEClientProperties;
import com.intel.gpe.clients.gpe4gtk.GPE4GTKConstants;
import com.intel.gpe.clients.gtk.WSRFClientImpl;
import com.intel.util.xml.Namespaces;

/**
 * 
 * @author Alexander Lukichev
 * @version $Id: GTKCustomizableDataStagingSetup.java,v 1.9 2006/11/20 14:04:34 mlukichev Exp $
 */
public class GTKCustomizableDataStagingSetup extends CustomizableDataStagingSetup {

    private GPEClientProperties props;

    public GTKCustomizableDataStagingSetup(Namespaces namespaces, GPEClientProperties props) {
        this(listSupportedProtocols(props), namespaces, props);
    }

    public GTKCustomizableDataStagingSetup(List<String> protocols, Namespaces namespaces, GPEClientProperties props) {
        super(protocols, namespaces);
        this.props = props;
    }

    protected DataStagingSetup getDataStagingSetup(String protocol, Namespaces namespaces) {
        String dssClassName = props.getDataStagingSetup(protocol);
        if (dssClassName != null) {
            Class clazz;
            try {
                clazz = Class.forName(dssClassName);
            } catch (Throwable e) {
                throw new IllegalArgumentException("Unsupported protocol: " + protocol);
            }
            try {
                return (DataStagingSetup) clazz.getConstructor(new Class[] { Namespaces.class }).newInstance(new Object[] { namespaces });
            } catch (Throwable e) {
                throw new IllegalArgumentException("Unsupported protocol: " + protocol);
            }
        } else {
            throw new IllegalArgumentException("Unknown protocol: " + protocol);
        }
    }

    protected GetResourceProperty getProtocolsProperty(PartnerLink storage, Variable protocolsResponse, Namespaces namespaces) {
        return new GTKGetResourceProperty(GPE4GTKConstants.StorageManagement.PROTOCOL, storage, protocolsResponse, namespaces);
    }

    private static List<String> listSupportedProtocols(GPEClientProperties props) {
        return props.getDataStagingSetupEntries();
    }

    @Override
    protected String getBPWSNS() {
        return WorkflowConstants.Workflow.BPWS_NAMESPACE;
    }

    @Override
    protected String getUnigridsNS() {
        return Constants.Unigrids.UNIGRIDS_TYPES_NS;
    }

    @Override
    protected String getWESNS() {
        return WorkflowConstants.Workflow.NAMESPACE;
    }

    @Override
    protected PartnerLinkValue getPLValue(WSRFClient client) {
        if (!(client instanceof WSRFClientImpl)) return null;
        return new PartnerLinkValueImpl((WSRFClientImpl) client);
    }
}

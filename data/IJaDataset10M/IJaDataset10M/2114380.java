package org.openremote.modeler.client.utils;

import java.util.Map;
import org.openremote.modeler.client.rpc.ProtocolRPCService;
import org.openremote.modeler.client.rpc.ProtocolRPCServiceAsync;
import org.openremote.modeler.protocol.ProtocolDefinition;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The Class Protocols. Used for get all protocol definitions from xml files.
 */
public class Protocols {

    /** The m_instance. */
    private static Map<String, ProtocolDefinition> instanceMap;

    /** The Constant protocolService. */
    private static final ProtocolRPCServiceAsync protocolService = (ProtocolRPCServiceAsync) GWT.create(ProtocolRPCService.class);

    /**
    * Instantiates a new protocols.
    */
    private Protocols() {
    }

    /**
    * Gets the single instance of Protocols.
    * 
    * @return single instance of Protocols
    */
    public static synchronized Map<String, ProtocolDefinition> getInstance() {
        if (instanceMap == null) {
            protocolService.getProtocols(new AsyncCallback<Map<String, ProtocolDefinition>>() {

                public void onFailure(Throwable caught) {
                    MessageBox.info("Error", "Can't get protocols from xml file!", null);
                }

                public void onSuccess(Map<String, ProtocolDefinition> protocols) {
                    instanceMap = protocols;
                }
            });
        }
        return instanceMap;
    }
}

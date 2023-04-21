package org.personalsmartspace.cm.source.impl.sources;

import org.personalsmartspace.cm.source.api.pss3p.callback.ICtxSource;
import org.personalsmartspace.log.impl.PSSLog;
import org.personalsmartspace.sre.ems.api.pss3p.EventListener;
import org.personalsmartspace.sre.ems.api.pss3p.PSSEvent;
import org.personalsmartspace.sre.ems.api.pss3p.PeerEvent;

/**
 * @author Korbinian Frank (DLR)
 *
 */
public class ServiceCtxSource extends EventListener implements ICtxSource {

    private final PSSLog logger = new PSSLog(this);

    @Override
    public void handlePSSEvent(PSSEvent arg0) {
    }

    @Override
    public void handlePeerEvent(PeerEvent arg0) {
    }

    /**
	 * Can only be called in return to unregister. Result is ignored
	 */
    public void handleCallbackObject(Object arg0) {
        logger.info(arg0 + " is result of Callback to unregister()");
    }

    public void handleErrorMessage(String arg0) {
        logger.error("Error in transmitting message from ProxyCSM to CSM: " + arg0);
    }

    /**
	 * Can only be called in return to register. Result has to be returned
	 */
    public void handleCallbackString(String arg0) {
        logger.info(arg0 + " is result of Callback to register()");
    }
}

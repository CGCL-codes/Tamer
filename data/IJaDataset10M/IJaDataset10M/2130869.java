package org.dago.atanxt;

import lejos.pc.comm.*;
import org.dago.atacom.controller.*;
import org.dago.atacom.grammar.Device;
import org.dago.common.*;

/**
 * Controller implementation for NXT
 */
public final class NxtController extends AbstractController<NxtDriver> {

    private static final NxtInfoAllocator nxtinfoAllocator = new NxtInfoAllocator();

    @Override
    public void initialize(Device device) throws DagoException {
        NXTInfo nxtInfo = nxtinfoAllocator.getInfo(device);
        if (nxtInfo == null) {
            throw new DagoException(I18NMessages.errorNoNXTFound);
        }
        setDriver(new NxtDriver(this, nxtInfo));
        super.initialize(device);
    }

    /**
	 * Allocates NXT by scanning using NXTInfo structure
	 */
    private static final class NxtInfoAllocator extends AbstractAllocator<NXTInfo> {

        @Override
        protected NXTInfo scan(String name) {
            this.logger.info(I18N.format(I18NMessages.waitingNXT, name));
            NXTConnector connector = new NXTConnector();
            NXTInfo[] nxtInfo = connector.search(name, null, NXTCommFactory.BLUETOOTH);
            if (nxtInfo != null) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("found NXT: " + nxtInfo[0].name);
                }
                return nxtInfo[0];
            }
            return null;
        }
    }
}

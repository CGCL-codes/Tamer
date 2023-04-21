package com.thirdparty.serviceproviders.memorydeltastore;

import com.codelaboration.wiac.services.concrete.deltastore.definition.adapter.AbstractCloseWavelet;
import com.codelaboration.wiac.services.concrete.deltastore.definition.service.VoidResult;

/**
 * 
 * @author egarcia
 *
 */
public class CloseWavelet extends AbstractCloseWavelet {

    private static final long serialVersionUID = 1L;

    /**
	 * This Command will close the wavelet if it is openened
	 * 
	 */
    @Override
    public VoidResult execute() throws ACloseException {
        if (!OpenWavelet.isOpened()) {
            throw new ACloseException();
        }
        OpenWavelet.setOpened(false);
        return null;
    }
}

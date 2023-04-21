package com.acv.service.bus.util;

import com.acv.common.model.container.BusData;

/**
 * The observer classe of the bus manager.
 * @author Bin Chen
 *
 */
public interface BusManagerListener {

    /**
	 * @param data
	 * @param event
	 */
    public void onMessage(BusData data, int event);
}

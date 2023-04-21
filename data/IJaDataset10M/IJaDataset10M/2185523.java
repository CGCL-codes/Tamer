package com.acv.bus.impl;

import org.apache.log4j.Logger;
import com.acv.bus.BusServiceDelegate;
import com.acv.bus.engine.EngineManager;
import com.acv.common.exception.ConnectorException;
import com.acv.common.exception.UnsuccessfulRequestException;
import com.acv.common.model.container.BusRequest;
import com.acv.common.model.container.BusResponse;

/**
 * Classs to delegates a <code>BusRequest</code> object
 * to a <code>EngineManager</code>.
 *
 */
public class BusServiceDelegateImpl implements BusServiceDelegate {

    private static final Logger logger = Logger.getLogger(BusServiceDelegateImpl.class);

    private EngineManager engineManager;

    private String bookingRequestDelimiter = ";";

    public BusServiceDelegateImpl() {
    }

    public String getBookingRequestDelimiter() {
        return bookingRequestDelimiter;
    }

    public void setBookingRequestDelimiter(String bookingRequestDelimiter) {
        this.bookingRequestDelimiter = bookingRequestDelimiter;
    }

    /**
	 * Delegate a <code>BusRequest</code> object to the <code>EngineManager</code>.
	 * @param request A <code>BusRequest</code> object to be processed.
	 * @return A <code>BusResponse</code> object produced by connector.
	 * @throws ConnectorException
	 * @throws UnsuccessfulRequestException
	 */
    public BusResponse process(BusRequest busRequest) throws ConnectorException, UnsuccessfulRequestException {
        validateRequest(busRequest);
        logger.info("uid: " + busRequest.getUid());
        BusResponse response = null;
        logger.info("Mail box id:" + busRequest.getMailBoxID());
        if (engineManager.acceptRequest(busRequest)) {
            response = engineManager.send(busRequest);
        } else {
            logger.warn("Request rejected - no manager was willing to handle it.");
            throw new ConnectorException("No engine manager is willing to handle the request!");
        }
        if (logger.isDebugEnabled()) logger.debug(response.toString());
        return response;
    }

    /**
	 * Check if this is a valid <code>BusRequest</code> object.
	 * @param request A <code>BusRequest</code> object to be processed.
	 * @throws ConnectorException
	 */
    private void validateRequest(BusRequest request) throws ConnectorException {
        if (request == null) {
            logger.error("Null request, aborting processing.");
            throw new ConnectorException("Null request, aborting processing.");
        }
    }

    public String testBooking(String uid, int bookingTypeFlag, String paramValues) {
        return "The bokking request has been sent, please check the mailbox to see the booking result.";
    }

    public void testService() {
    }

    public EngineManager getEngineManager() {
        return engineManager;
    }

    public void setEngineManager(EngineManager engineManager) {
        this.engineManager = engineManager;
    }
}

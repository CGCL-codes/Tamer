package org.dicom4j.network.association.listeners.defaults;

import java.util.Iterator;
import org.apache.commons.lang.NullArgumentException;
import org.dicom4j.management.Device;
import org.dicom4j.network.NetworkStaticProperties;
import org.dicom4j.network.association.Association;
import org.dicom4j.network.association.associate.AssociateRequest;
import org.dicom4j.network.association.associate.AssociateResponse;
import org.dicom4j.network.association.listeners.AssociateRequestHandler;
import org.dicom4j.network.protocoldataunit.items.PresentationContextItemRQ;
import org.dicom4j.network.protocoldataunit.support.PresentationContextItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * implementation of {@link AssociateRequestHandler AssociateRequestHandler}
 * using a {@link Device Device} to know supported SOPClass and
 * TransferSyntaxes.
 * 
 * If not Device is set, a warning is write in the log, and all requests will be
 * refused
 * 
 * @since 0.0.2
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte
 */
public class DeviceAssociateRequestHandler implements AssociateRequestHandler {

    private static Logger flogger = LoggerFactory.getLogger(DeviceAssociateRequestHandler.class);

    /**
	 * The device
	 */
    private Device fDevice;

    public DeviceAssociateRequestHandler(Device aDevice) {
        super();
        setDevice(aDevice);
    }

    /**
	 * Internal perfom of request
	 * 
	 * @param aDevice
	 *          the device (always != null)
	 * @param aAssociation
	 *          the assocation
	 * @param aAssociateRequest
	 * @param aResponse
	 *          the reponse already created
	 * @return
	 * @throws Exception
	 */
    protected AssociateResponse dorequestReceived(Device aDevice, Association aAssociation, AssociateRequest aAssociateRequest, AssociateResponse aResponse) throws Exception {
        Iterator<PresentationContextItem> lIt = aAssociateRequest.getPresentationIterator();
        while (lIt.hasNext()) {
            PresentationContextItemRQ lPres = (PresentationContextItemRQ) lIt.next();
            if (getDevice() == null) {
                aResponse.addPresentationContext(lPres.getID(), NetworkStaticProperties.PresentationContextReasons.USER_REJECTION, lPres.getTransferSyntax(0));
            }
        }
        return aResponse;
    }

    public Device getDevice() {
        return fDevice;
    }

    public AssociateResponse requestReceived(Association aAssociation, AssociateRequest aAssociateRequest) throws Exception {
        AssociateResponse lResponse = new AssociateResponse(aAssociateRequest.getCalledAET(), aAssociateRequest.getCallingAET());
        if (getDevice() == null) {
            flogger.warn("No Device was set, we send Reject");
            return null;
        } else {
            return dorequestReceived(getDevice(), aAssociation, aAssociateRequest, lResponse);
        }
    }

    public void setDevice(Device aDevice) {
        if (aDevice != null) {
            flogger.debug("setDevice: " + aDevice.getDeviceName());
            fDevice = aDevice;
        } else {
            throw new NullArgumentException("DeviceAssociateRequestHandler.setDevice");
        }
    }

    @Override
    public String toString() {
        String lRes = "DeviceAssociateRequestHandler";
        if (getDevice() != null) {
            return lRes + " (Device: " + getDevice().getDeviceName() + ")";
        } else {
            return lRes + " (No Device)";
        }
    }
}

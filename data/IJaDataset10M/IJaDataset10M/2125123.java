package org.mobicents.protocols.ss7.cap.service.gprs;

import org.mobicents.protocols.ss7.cap.MessageImpl;
import org.mobicents.protocols.ss7.cap.api.service.gprs.CAPDialogGprs;
import org.mobicents.protocols.ss7.cap.api.service.gprs.GprsMessage;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public abstract class GprsMessageImpl extends MessageImpl implements GprsMessage, CAPAsnPrimitive {

    public CAPDialogGprs getCAPDialog() {
        return (CAPDialogGprs) super.getCAPDialog();
    }
}

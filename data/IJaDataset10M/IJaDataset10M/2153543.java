package org.mobicents.protocols.ss7.cap;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.mobicents.protocols.ss7.cap.api.CAPApplicationContext;
import org.mobicents.protocols.ss7.cap.api.CAPDialog;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPProvider;
import org.mobicents.protocols.ss7.cap.api.CAPServiceBase;
import org.mobicents.protocols.ss7.cap.api.CAPServiceListener;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPComponentErrorReason;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessage;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;

/**
 * This class must be the super class of all CAP services
 * 
 * @author sergey vetyutnev
 * 
 */
public abstract class CAPServiceBaseImpl implements CAPServiceBase {

    protected Boolean _isActivated = false;

    protected List<CAPServiceListener> serviceListeners = new CopyOnWriteArrayList<CAPServiceListener>();

    protected CAPProviderImpl capProviderImpl = null;

    protected CAPServiceBaseImpl(CAPProviderImpl capProviderImpl) {
        this.capProviderImpl = capProviderImpl;
    }

    @Override
    public CAPProvider getCAPProvider() {
        return this.capProviderImpl;
    }

    /**
	 * Creation a CAP Dialog implementation for the specific service
	 * 
	 * @param appCntx
	 * @param tcapDialog
	 * @return
	 */
    protected abstract CAPDialogImpl createNewDialogIncoming(CAPApplicationContext appCntx, Dialog tcapDialog);

    /**
	 * Creating new outgoing TCAP Dialog. Used when creating a new outgoing CAP
	 * Dialog
	 * 
	 * @param origAddress
	 * @param destAddress
	 * @return
	 * @throws CAPException
	 */
    protected Dialog createNewTCAPDialog(SccpAddress origAddress, SccpAddress destAddress) throws CAPException {
        try {
            return this.capProviderImpl.getTCAPProvider().getNewDialog(origAddress, destAddress);
        } catch (TCAPException e) {
            throw new CAPException(e.getMessage(), e);
        }
    }

    public abstract void processComponent(ComponentType compType, OperationCode oc, Parameter parameter, CAPDialog capDialog, Long invokeId, Long linkedId) throws CAPParsingComponentException;

    /**
	 * Adding CAP Dialog into CAPProviderImpl.dialogs Used when creating a new
	 * outgoing CAP Dialog
	 * 
	 * @param dialog
	 */
    protected void putCAPDialogIntoCollection(CAPDialogImpl dialog) {
        this.capProviderImpl.addDialog((CAPDialogImpl) dialog);
    }

    protected void addCAPServiceListener(CAPServiceListener capServiceListener) {
        this.serviceListeners.add(capServiceListener);
    }

    protected void removeCAPServiceListener(CAPServiceListener capServiceListener) {
        this.serviceListeners.remove(capServiceListener);
    }

    /**
	 * This method is invoked when CAPProviderImpl.onInvokeTimeOut() is invoked.
	 * An InvokeTimeOut may be a normal situation for the component class 2, 3,
	 * or 4. In this case checkInvokeTimeOut() should return true and deliver to
	 * the CAP-user correct indication
	 * 
	 * @param dialog
	 * @param invoke
	 * @return
	 */
    public boolean checkInvokeTimeOut(CAPDialog dialog, Invoke invoke) {
        return false;
    }

    /**
	 * {@inheritDoc}
	 */
    public boolean isActivated() {
        return this._isActivated;
    }

    /**
	 * {@inheritDoc}
	 */
    public void acivate() {
        this._isActivated = true;
    }

    /**
	 * {@inheritDoc}
	 */
    public void deactivate() {
        this._isActivated = false;
    }

    protected void deliverErrorComponent(CAPDialog capDialog, Long invokeId, CAPErrorMessage capErrorMessage) {
        for (CAPServiceListener serLis : this.serviceListeners) {
            serLis.onErrorComponent(capDialog, invokeId, capErrorMessage);
        }
    }

    protected void deliverRejectComponent(CAPDialog capDialog, Long invokeId, Problem problem) {
        for (CAPServiceListener serLis : this.serviceListeners) {
            serLis.onRejectComponent(capDialog, invokeId, problem);
        }
    }

    protected void deliverInvokeTimeout(CAPDialog capDialog, Invoke invoke) {
        for (CAPServiceListener serLis : this.serviceListeners) {
            serLis.onInvokeTimeout(capDialog, invoke.getInvokeId());
        }
    }

    protected void deliverProviderErrorComponent(CAPDialog mapDialog, Long invokeId, CAPComponentErrorReason providerError) {
        for (CAPServiceListener serLis : this.serviceListeners) {
            serLis.onProviderErrorComponent(mapDialog, invokeId, providerError);
        }
    }
}

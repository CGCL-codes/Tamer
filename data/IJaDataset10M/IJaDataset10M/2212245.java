package org.itsnat.impl.comp.button;

import org.itsnat.impl.comp.ItsNatHTMLInputImpl;
import org.itsnat.impl.comp.ItsNatHTMLComponentManagerImpl;
import org.itsnat.comp.button.ItsNatButtonUI;
import javax.swing.ButtonModel;
import javax.swing.event.ChangeEvent;
import org.itsnat.core.NameValue;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.comp.button.ItsNatButtonSharedImpl;
import org.itsnat.impl.comp.button.ItsNatButtonInternal;
import org.w3c.dom.events.Event;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatHTMLInputButtonBaseImpl extends ItsNatHTMLInputImpl implements ItsNatButtonInternal {

    protected ItsNatButtonSharedImpl buttonDelegate = createItsNatButtonShared();

    protected ItsNatHTMLFormCompButtonSharedImpl htmlButtonDeleg = new ItsNatHTMLFormCompButtonSharedImpl(this);

    /**
     * Creates a new instance of ItsNatHTMLInputButtonBaseImpl
     */
    public ItsNatHTMLInputButtonBaseImpl(HTMLInputElement element, NameValue[] artifacts, ItsNatHTMLComponentManagerImpl componentMgr) {
        super(element, artifacts, componentMgr);
    }

    public ItsNatButtonSharedImpl getItsNatButtonShared() {
        return buttonDelegate;
    }

    protected ParamTransport[] getInternalParamTransports(String type) {
        return null;
    }

    public void enableEventListeners() {
        enableEventListener("click");
    }

    public ItsNatButtonUI getItsNatButtonUI() {
        return (ItsNatButtonUI) compUI;
    }

    public void bindDataModel() {
        buttonDelegate.bindDataModel();
    }

    public void unbindDataModel() {
        buttonDelegate.unbindDataModel();
    }

    public void initialSyncUIWithDataModel() {
        buttonDelegate.initialSyncUIWithDataModel();
    }

    public void syncWithDataModel() {
        buttonDelegate.syncWithDataModel();
    }

    public void stateChanged(ChangeEvent e) {
        buttonDelegate.stateChanged(e);
    }

    public ButtonModel getButtonModel() {
        return (ButtonModel) dataModel;
    }

    public void setButtonModel(ButtonModel dataModel) {
        setDataModel(dataModel);
    }

    public boolean isEnabled() {
        return getButtonModel().isEnabled();
    }

    public void setEnabled(boolean b) {
        getButtonModel().setEnabled(b);
    }

    public void setDOMEnabled(boolean b) {
        super.setEnabled(b);
    }

    public void processDOMEvent(Event evt) {
        if (!htmlButtonDeleg.handleEvent(evt)) return;
        super.processDOMEvent(evt);
    }
}

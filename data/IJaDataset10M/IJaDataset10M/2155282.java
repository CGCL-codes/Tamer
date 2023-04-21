package org.itsnat.impl.comp.button.toggle;

import org.itsnat.impl.comp.button.ItsNatHTMLInputButtonBaseImpl;
import org.itsnat.impl.comp.ItsNatHTMLComponentManagerImpl;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputButtonToggle;
import org.itsnat.comp.button.toggle.ItsNatButtonToggleUI;
import javax.swing.ButtonModel;
import javax.swing.JToggleButton.ToggleButtonModel;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.button.toggle.ItsNatButtonToggleInternal;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatHTMLInputButtonToggleImpl extends ItsNatHTMLInputButtonBaseImpl implements ItsNatHTMLInputButtonToggle, ItsNatButtonToggleInternal {

    protected ItsNatHTMLInputButtonToggleMarkupDrivenUtil markupDrivenUtil;

    /**
     * Creates a new instance of ItsNatHTMLInputButtonToggleImpl
     */
    public ItsNatHTMLInputButtonToggleImpl(HTMLInputElement element, NameValue[] artifacts, ItsNatHTMLComponentManagerImpl componentMgr) {
        super(element, artifacts, componentMgr);
        this.markupDrivenUtil = ItsNatHTMLInputButtonToggleMarkupDrivenUtil.initMarkupDriven(this);
    }

    public ToggleButtonModel getToggleButtonModel() {
        return (ToggleButtonModel) dataModel;
    }

    public void setToggleButtonModel(ToggleButtonModel dataModel) {
        setDataModel(dataModel);
    }

    public boolean isSelected() {
        return getButtonModel().isSelected();
    }

    public void setSelected(boolean b) {
        getButtonModel().setSelected(b);
    }

    public Object createDefaultModelInternal() {
        return createDefaultButtonModel();
    }

    public ButtonModel createDefaultButtonModel() {
        return new ToggleButtonModel();
    }

    public ItsNatButtonToggleUI getItsNatButtonToggleUI() {
        return (ItsNatButtonToggleUI) compUI;
    }

    public void setUISelected(boolean selected) {
        if (!isUIEnabled()) return;
        ItsNatButtonToggleUI compUI = getItsNatButtonToggleUI();
        boolean wasDisabled = disableSendCodeToRequesterIfServerUpdating();
        try {
            compUI.setSelected(selected);
        } finally {
            if (wasDisabled) enableSendCodeToRequester();
        }
    }

    public void initialSyncUIWithDataModel() {
        super.initialSyncUIWithDataModel();
        if (markupDrivenUtil != null) markupDrivenUtil.initialSyncUIWithDataModel();
    }

    public void setDefaultDataModel(Object dataModel) {
        if (markupDrivenUtil != null) markupDrivenUtil.preSetDefaultDataModel(dataModel);
        super.setDefaultDataModel(dataModel);
    }

    public void dispose(boolean updateClient) {
        super.dispose(updateClient);
        if (markupDrivenUtil != null) {
            markupDrivenUtil.dispose();
            this.markupDrivenUtil = null;
        }
    }

    public boolean isMarkupDriven() {
        return markupDrivenUtil != null;
    }

    public void setMarkupDriven(boolean value) {
        this.markupDrivenUtil = ItsNatHTMLInputButtonToggleMarkupDrivenUtil.setMarkupDriven(this, markupDrivenUtil, value);
    }
}

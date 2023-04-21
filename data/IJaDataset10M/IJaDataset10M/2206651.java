package org.regola.webapp.dialogs;

import javax.faces.event.ActionEvent;
import org.regola.model.Invoice;
import org.regola.model.Item;
import org.regola.model.ItemId;
import org.regola.model.pattern.ItemPattern;
import org.regola.webapp.action.icefaces.FormPageIceFaces;
import org.regola.webapp.jsf.Dialog;
import org.regola.webapp.jsf.FormDialog;

public class InvoiceDialog extends Dialog {

    private Invoice model;

    public void openEdit(Invoice model) {
        this.model = model;
    }

    public void closePopupAfterValidation(ActionEvent e) {
        if (e.getComponent().getId().matches(".*OK.*") && getCallback() != null) getCallback().onConfirm(); else if (e.getComponent().getId().matches(".*Cancel.*") && getCallback() != null) getCallback().onCancel();
    }

    public Invoice getModel() {
        return model;
    }

    public void setModel(Invoice model) {
        this.model = model;
    }
}

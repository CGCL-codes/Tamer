package org.opensecurepay.ui.spring.views;

import org.opensecurepay.dao.PersistenceService;
import org.opensecurepay.entity.Contract;
import org.opensecurepay.entity.Mandator;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.dialog.FormBackedDialogPage;
import org.springframework.richclient.dialog.TitledPageApplicationDialog;
import org.springframework.richclient.form.Form;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.util.Assert;

/**
 * This is a dialog for editing the properties of a Contact object. It is a simple "form backed" dialog, meaning that
 * the body of the dialog is provided from a "form backed" dialog page. The Ok (finish) button will be wired into the
 * "page complete" state of the dialog page, which in turn gets its state from the automatic validation of the
 * properties on the form.
 * @author Larry Streepy
 * @see FormBackedDialogPage
 * @see PersonForm
 */
public class ContractPropertiesDialog extends TitledPageApplicationDialog {

    /** The form that allows for editing the contract. */
    private Form form;

    /** Are we creating a new Contact or editing an existing one? */
    private boolean creatingNew = false;

    private PersistenceService persistenceService;

    public ContractPropertiesDialog(Mandator mandator, PersistenceService persistenceService) {
        creatingNew = true;
        Contract contract = new Contract();
        contract.setMandator(mandator);
        initialize(contract, persistenceService);
    }

    public ContractPropertiesDialog(Contract contract, PersistenceService persistenceService) {
        initialize(contract, persistenceService);
    }

    private void initialize(Contract entity, PersistenceService persistenceService) {
        Assert.notNull(persistenceService, "The persistence service is required to edit a contract!");
        setCloseAction(CloseAction.DISPOSE);
        form = new ContractForm(FormModelHelper.createFormModel(entity));
        setDialogPage(new FormBackedDialogPage(form));
        this.persistenceService = persistenceService;
    }

    private Contract getEditingEntity() {
        return (Contract) form.getFormModel().getFormObject();
    }

    protected void onAboutToShow() {
        setDescription(getMessage("contractProperties.description"));
        if (creatingNew) {
            setTitle(getMessage("contractProperties.new.title"));
        } else {
            Contract entity = getEditingEntity();
            String title = getMessage("contractProperties.edit.title", new Object[] { entity.getName(), entity.getNr() });
            setTitle(title);
        }
    }

    protected boolean onFinish() {
        form.getFormModel().commit();
        String eventType;
        if (creatingNew) {
            eventType = LifecycleApplicationEvent.CREATED;
            persistenceService.persistEntity(getEditingEntity());
        } else {
            eventType = LifecycleApplicationEvent.MODIFIED;
            persistenceService.persist();
        }
        getApplicationContext().publishEvent(new LifecycleApplicationEvent(eventType, getEditingEntity()));
        return true;
    }

    protected void onCancel() {
        if (form.getFormModel().isDirty()) {
            String msg = getMessage("contractProperties.dirtyCancelMessage");
            String title = getMessage("contractProperties.dirtyCancelTitle");
            ConfirmationDialog dlg = new ConfirmationDialog(title, msg) {

                protected void onConfirm() {
                    ContractPropertiesDialog.super.onCancel();
                }
            };
            dlg.showDialog();
        } else {
            super.onCancel();
        }
    }
}

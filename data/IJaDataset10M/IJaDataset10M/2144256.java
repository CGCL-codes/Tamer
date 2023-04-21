package de.forsthaus.webui.security.right;

import java.io.Serializable;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import de.forsthaus.backend.model.SecRight;
import de.forsthaus.backend.model.SecTyp;
import de.forsthaus.backend.service.SecurityService;
import de.forsthaus.webui.security.right.model.SecRightSecTypListModelItemRenderer;
import de.forsthaus.webui.util.ButtonStatusCtrl;
import de.forsthaus.webui.util.GFCBaseCtrl;
import de.forsthaus.webui.util.MultiLineMessageBox;
import de.forsthaus.webui.util.ZksampleMessageUtils;

/**
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the controller class for the
 * /WEB-INF/pages/sec_right/secRightDialog.zul file.<br>
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * 
 * @changes 05/15/2009: sge Migrating the list models for paging. <br>
 *          07/24/2009: sge changes for clustering.<br>
 *          10/12/2009: sge changings in the saving routine.<br>
 *          11/07/2009: bbr changed to extending from GFCBaseCtrl<br>
 *          (GenericForwardComposer) for spring-managed creation.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class SecRightDialogCtrl extends GFCBaseCtrl implements Serializable {

    private static final long serialVersionUID = -546886879998950467L;

    private static final Logger logger = Logger.getLogger(SecRightDialogCtrl.class);

    protected Window secRightDialogWindow;

    protected Textbox rigName;

    protected Listbox rigType;

    private transient Listbox listBoxSecRights;

    private transient SecRight right;

    private transient String oldVar_rigName;

    private transient Listitem oldVar_rigType;

    private transient boolean validationOn;

    private final transient String btnCtroller_ClassPrefix = "button_SecRightDialog_";

    private transient ButtonStatusCtrl btnCtrl;

    protected Button btnNew;

    protected Button btnEdit;

    protected Button btnDelete;

    protected Button btnSave;

    protected Button btnCancel;

    protected Button btnClose;

    private transient SecurityService securityService;

    /**
	 * default constructor.<br>
	 */
    public SecRightDialogCtrl() {
        super();
    }

    /**
	 * Before binding the data and calling the dialog window we check, if the
	 * zul-file is called with a parameter for a selected user object in a Map.
	 * 
	 * @param event
	 * @throws Exception
	 */
    public void onCreate$secRightDialogWindow(Event event) throws Exception {
        btnCtrl = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, true, btnNew, btnEdit, btnDelete, btnSave, btnCancel, btnClose);
        Map<String, Object> args = getCreationArgsMap(event);
        if (args.containsKey("right")) {
            right = (SecRight) args.get("right");
            setRight(right);
        } else {
            setRight(null);
        }
        if (args.containsKey("listBoxSecRights")) {
            listBoxSecRights = (Listbox) args.get("listBoxSecRights");
        } else {
            listBoxSecRights = null;
        }
        rigType.setModel(new ListModelList(getSecurityService().getAllTypes()));
        rigType.setItemRenderer(new SecRightSecTypListModelItemRenderer());
        ListModelList lml = (ListModelList) rigType.getModel();
        SecTyp typ = getSecurityService().getTypById(right.getRigType().intValue());
        if (right.isNew()) {
            rigType.setSelectedIndex(-1);
        } else {
            rigType.setSelectedIndex(lml.indexOf(typ));
        }
        doSetFieldProperties();
        doShowDialog(getRight());
    }

    /**
	 * If we close the dialog window. <br>
	 * 
	 * @param event
	 * @throws Exception
	 */
    public void onClose$secRightDialogWindow(Event event) throws Exception {
        doClose();
    }

    /**
	 * when the "save" button is clicked. <br>
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
    public void onClick$btnSave(Event event) throws InterruptedException {
        doSave();
    }

    /**
	 * when the "edit" button is clicked. <br>
	 * 
	 * @param event
	 */
    public void onClick$btnEdit(Event event) {
        doEdit();
    }

    /**
	 * when the "help" button is clicked. <br>
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
    public void onClick$btnHelp(Event event) throws InterruptedException {
        ZksampleMessageUtils.doShowNotImplementedMessage();
    }

    /**
	 * when the "new" button is clicked. <br>
	 * 
	 * @param event
	 */
    public void onClick$btnNew(Event event) {
        doNew();
    }

    /**
	 * when the "delete" button is clicked. <br>
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
    public void onClick$btnDelete(Event event) throws InterruptedException {
        doDelete();
    }

    /**
	 * when the "cancel" button is clicked. <br>
	 * 
	 * @param event
	 */
    public void onClick$btnCancel(Event event) {
        doCancel();
    }

    /**
	 * when the "close" button is clicked. <br>
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
    public void onClick$btnClose(Event event) throws InterruptedException {
        try {
            doClose();
        } catch (final Exception e) {
            secRightDialogWindow.onClose();
        }
    }

    /**
	 * Closes the dialog window. <br>
	 * <br>
	 * Before closing we check if there are unsaved changes in <br>
	 * the components and ask the user if saving the modifications. <br>
	 * 
	 */
    private void doClose() throws Exception {
        if (isDataChanged()) {
            String msg = Labels.getLabel("message_Data_Modified_Save_Data_YesNo");
            String title = Labels.getLabel("message.Information");
            MultiLineMessageBox.doSetTemplate();
            if (MultiLineMessageBox.show(msg, title, MultiLineMessageBox.YES | MultiLineMessageBox.NO, MultiLineMessageBox.QUESTION, true, new EventListener() {

                @Override
                public void onEvent(Event evt) {
                    switch(((Integer) evt.getData()).intValue()) {
                        case MultiLineMessageBox.YES:
                            try {
                                doSave();
                            } catch (final InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        case MultiLineMessageBox.NO:
                            break;
                    }
                }
            }) == MultiLineMessageBox.YES) {
            }
        }
        secRightDialogWindow.onClose();
    }

    /**
	 * Cancel the actual operation. <br>
	 * <br>
	 * Resets to the original status.<br>
	 * 
	 */
    private void doCancel() {
        doResetInitValues();
        doReadOnly();
        btnCtrl.setInitEdit();
    }

    /**
	 * Writes the bean data to the components.<br>
	 * 
	 * @param aRight
	 *            SecRight
	 */
    public void doWriteBeanToComponents(SecRight aRight) {
        rigName.setValue(aRight.getRigName());
    }

    /**
	 * Writes the components values to the bean.<br>
	 * 
	 * @param aRight
	 */
    public void doWriteComponentsToBean(SecRight aRight) {
        aRight.setRigName(rigName.getValue());
    }

    /**
	 * Opens the Dialog window modal.
	 * 
	 * It checks if the dialog opens with a new or existing object, if so they
	 * set the readOnly mode accordingly.
	 * 
	 * @param aRight
	 * @throws InterruptedException
	 */
    public void doShowDialog(SecRight aRight) throws InterruptedException {
        if (aRight == null) {
            aRight = getSecurityService().getNewSecRight();
            setRight(aRight);
        } else {
            setRight(aRight);
        }
        if (aRight.isNew()) {
            doEdit();
            btnCtrl.setInitNew();
        } else {
            btnCtrl.setInitEdit();
            doReadOnly();
        }
        try {
            doWriteBeanToComponents(aRight);
            doStoreInitValues();
            secRightDialogWindow.doModal();
        } catch (final Exception e) {
            Messagebox.show(e.toString());
        }
    }

    /**
	 * Set the properties of the fields, like maxLength.<br>
	 */
    private void doSetFieldProperties() {
        rigName.setMaxlength(50);
    }

    /**
	 * Stores the init values in mem vars. <br>
	 */
    private void doStoreInitValues() {
        oldVar_rigName = rigName.getValue();
        oldVar_rigType = rigType.getSelectedItem();
    }

    /**
	 * Resets the init values from mem vars. <br>
	 */
    private void doResetInitValues() {
        rigName.setValue(oldVar_rigName);
        rigType.setSelectedItem(oldVar_rigType);
    }

    /**
	 * Checks, if data are changed since the last call of <br>
	 * doStoreInitData() . <br>
	 * 
	 * @return true, if data are changed, otherwise false
	 */
    private boolean isDataChanged() {
        boolean changed = false;
        if (oldVar_rigName != rigName.getValue()) {
            changed = true;
        }
        if (oldVar_rigType != rigType.getSelectedItem()) {
            changed = true;
        }
        return changed;
    }

    /**
	 * Sets the Validation by setting the accordingly constraints to the fields.
	 */
    private void doSetValidation() {
        setValidationOn(true);
        rigName.setConstraint("NO EMPTY");
    }

    /**
	 * Disables the Validation by setting empty constraints.
	 */
    private void doRemoveValidation() {
        setValidationOn(false);
        rigName.setConstraint("");
    }

    /**
	 * Deletes a secRight object from database.<br>
	 * 
	 * @throws InterruptedException
	 */
    private void doDelete() throws InterruptedException {
        final SecRight aRight = getRight();
        String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + aRight.getRigName();
        String title = Labels.getLabel("message.Deleting.Record");
        MultiLineMessageBox.doSetTemplate();
        if (MultiLineMessageBox.show(msg, title, MultiLineMessageBox.YES | MultiLineMessageBox.NO, MultiLineMessageBox.QUESTION, true, new EventListener() {

            @Override
            public void onEvent(Event evt) {
                switch(((Integer) evt.getData()).intValue()) {
                    case MultiLineMessageBox.YES:
                        try {
                            delete();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    case MultiLineMessageBox.NO:
                        break;
                }
            }

            private void delete() throws InterruptedException {
                try {
                    getSecurityService().delete(aRight);
                } catch (DataAccessException e) {
                    ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                }
                final ListModelList lml = (ListModelList) listBoxSecRights.getListModel();
                if (lml.indexOf(aRight) == -1) {
                } else {
                    lml.remove(lml.indexOf(aRight));
                }
                secRightDialogWindow.onClose();
            }
        }) == MultiLineMessageBox.YES) {
        }
    }

    /**
	 * Create a new secRight object. <br>
	 */
    private void doNew() {
        doStoreInitValues();
        setRight(getSecurityService().getNewSecRight());
        doClear();
        doEdit();
        rigType.setSelectedIndex(-1);
        btnCtrl.setBtnStatus_New();
    }

    /**
	 * Set the components for edit mode. <br>
	 */
    private void doEdit() {
        rigName.setReadonly(false);
        rigType.setDisabled(false);
        btnCtrl.setBtnStatus_Edit();
        doStoreInitValues();
    }

    /**
	 * Set the components to ReadOnly. <br>
	 */
    public void doReadOnly() {
        rigName.setReadonly(true);
        rigType.setDisabled(true);
    }

    /**
	 * Clears the components values. <br>
	 */
    public void doClear() {
        doRemoveValidation();
        rigName.setValue("");
        rigType.setSelectedIndex(-1);
    }

    /**
	 * Saves the components to table. <br>
	 * 
	 * @throws InterruptedException
	 */
    public void doSave() throws InterruptedException {
        final SecRight aRight = getRight();
        if (!isValidationOn()) {
            doSetValidation();
        }
        doWriteComponentsToBean(aRight);
        Listitem item = this.rigType.getSelectedItem();
        if (item == null) {
            try {
                Messagebox.show("Please select a right type !");
            } catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        ListModelList lml1 = (ListModelList) rigType.getListModel();
        SecTyp typ = (SecTyp) lml1.get(item.getIndex());
        aRight.setRigType(Integer.valueOf(typ.getStpId()));
        try {
            getSecurityService().saveOrUpdate(aRight);
        } catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
            doResetInitValues();
            doReadOnly();
            btnCtrl.setBtnStatus_Save();
            return;
        }
        ListModelList lml = (ListModelList) this.listBoxSecRights.getListModel();
        if (lml.indexOf(aRight) == -1) {
            lml.add(aRight);
        } else {
            lml.set(lml.indexOf(aRight), aRight);
        }
        doReadOnly();
        btnCtrl.setBtnStatus_Save();
        doStoreInitValues();
    }

    public SecRight getRight() {
        return this.right;
    }

    public void setRight(SecRight right) {
        this.right = right;
    }

    public void setValidationOn(boolean validationOn) {
        this.validationOn = validationOn;
    }

    public boolean isValidationOn() {
        return this.validationOn;
    }

    public SecurityService getSecurityService() {
        return this.securityService;
    }

    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }
}

package net.cygeek.tech.client.ui.form;

import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.Validator;
import com.gwtext.client.widgets.form.ValidationException;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Record;
import net.cygeek.tech.client.ui.form.AbstractForm;
import net.cygeek.tech.client.ui.form.AbstractWindow;
import net.cygeek.tech.client.ui.validator.TextLengthValidator;
import net.cygeek.tech.client.cache.GeneralCache;
import net.cygeek.tech.client.data.Versions;
import net.cygeek.tech.client.adapters.VersionsAdapter;
import net.cygeek.tech.client.ohrm;
import java.util.Date;

/**
 * 
 */
public class VersionsForm extends AbstractForm {

    TextField txtEnteredDate;

    TextField txtModifiedDate;

    TextField txtName;

    TextField txtId;

    TextField txtDeleted;

    ComboBox cmbFileVersion;

    ComboBox cmbCreatedBy;

    ComboBox cmbModifiedBy;

    TextField txtDescription;

    ComboBox cmbDbVersion;

    Button save = new Button("Save");

    Button cancel = new Button("Cancel");

    public VersionsForm() {
        this.setFrame(true);
        this.setWidth(365);
        this.setLabelWidth(75);
        txtEnteredDate = new TextField("EnteredDate", "txtEnteredDate", 230);
        txtEnteredDate.setAllowBlank(false);
        this.add(txtEnteredDate);
        txtModifiedDate = new TextField("ModifiedDate", "txtModifiedDate", 230);
        txtModifiedDate.setAllowBlank(false);
        this.add(txtModifiedDate);
        txtName = new TextField("Name", "txtName", 230);
        txtName.setAllowBlank(false);
        this.add(txtName);
        txtId = new TextField("Id", "txtId", 230);
        txtId.setAllowBlank(false);
        this.add(txtId);
        txtDeleted = new TextField("Deleted", "txtDeleted", 230);
        txtDeleted.setAllowBlank(false);
        this.add(txtDeleted);
        cmbFileVersion = new ComboBox();
        cmbFileVersion.setForceSelection(true);
        cmbFileVersion.setMinChars(1);
        cmbFileVersion.setFieldLabel("FileVersion");
        cmbFileVersion.setDisplayField("name");
        cmbFileVersion.setValueField("id");
        cmbFileVersion.setMode(ComboBox.LOCAL);
        cmbFileVersion.setTriggerAction(ComboBox.ALL);
        cmbFileVersion.setEmptyText("Select");
        cmbFileVersion.setLoadingText("Searching...");
        cmbFileVersion.setTypeAhead(true);
        cmbFileVersion.setSelectOnFocus(true);
        cmbFileVersion.setWidth(230);
        cmbFileVersion.setHideTrigger(false);
        this.add(cmbFileVersion);
        cmbCreatedBy = new ComboBox();
        cmbCreatedBy.setForceSelection(true);
        cmbCreatedBy.setMinChars(1);
        cmbCreatedBy.setFieldLabel("CreatedBy");
        cmbCreatedBy.setDisplayField("isAdmin");
        cmbCreatedBy.setValueField("id");
        cmbCreatedBy.setMode(ComboBox.LOCAL);
        cmbCreatedBy.setTriggerAction(ComboBox.ALL);
        cmbCreatedBy.setEmptyText("Select");
        cmbCreatedBy.setLoadingText("Searching...");
        cmbCreatedBy.setTypeAhead(true);
        cmbCreatedBy.setSelectOnFocus(true);
        cmbCreatedBy.setWidth(230);
        cmbCreatedBy.setHideTrigger(false);
        this.add(cmbCreatedBy);
        cmbModifiedBy = new ComboBox();
        cmbModifiedBy.setForceSelection(true);
        cmbModifiedBy.setMinChars(1);
        cmbModifiedBy.setFieldLabel("ModifiedBy");
        cmbModifiedBy.setDisplayField("isAdmin");
        cmbModifiedBy.setValueField("id");
        cmbModifiedBy.setMode(ComboBox.LOCAL);
        cmbModifiedBy.setTriggerAction(ComboBox.ALL);
        cmbModifiedBy.setEmptyText("Select");
        cmbModifiedBy.setLoadingText("Searching...");
        cmbModifiedBy.setTypeAhead(true);
        cmbModifiedBy.setSelectOnFocus(true);
        cmbModifiedBy.setWidth(230);
        cmbModifiedBy.setHideTrigger(false);
        this.add(cmbModifiedBy);
        txtDescription = new TextField("Description", "txtDescription", 230);
        txtDescription.setAllowBlank(false);
        this.add(txtDescription);
        cmbDbVersion = new ComboBox();
        cmbDbVersion.setForceSelection(true);
        cmbDbVersion.setMinChars(1);
        cmbDbVersion.setFieldLabel("DbVersion");
        cmbDbVersion.setDisplayField("modifiedBy");
        cmbDbVersion.setValueField("id");
        cmbDbVersion.setMode(ComboBox.LOCAL);
        cmbDbVersion.setTriggerAction(ComboBox.ALL);
        cmbDbVersion.setEmptyText("Select");
        cmbDbVersion.setLoadingText("Searching...");
        cmbDbVersion.setTypeAhead(true);
        cmbDbVersion.setSelectOnFocus(true);
        cmbDbVersion.setWidth(230);
        cmbDbVersion.setHideTrigger(false);
        this.add(cmbDbVersion);
        save.addListener(new SaveButtonAdapter(this));
        this.addButton(save);
        cancel.addListener(new CancelButtonAdapter(this));
        this.addButton(cancel);
    }

    public void reinitialize(int mode) {
        this.isNew = (mode == AbstractWindow.ADD);
        txtId.setReadOnly(!isNew);
        cmbFileVersion.setStore(ohrm.GR_FileVersion.store);
        cmbCreatedBy.setStore(ohrm.GR_Users.store);
        cmbModifiedBy.setStore(ohrm.GR_Users.store);
        cmbDbVersion.setStore(ohrm.GR_DbVersion.store);
        if (isNew) {
            txtEnteredDate.setValue(null);
            txtModifiedDate.setValue(null);
            txtName.setValue(null);
            txtId.setValue(null);
            txtDeleted.setValue(null);
            cmbFileVersion.setValue(null);
            cmbCreatedBy.setValue(null);
            cmbModifiedBy.setValue(null);
            txtDescription.setValue(null);
            cmbDbVersion.setValue(null);
        } else if (mode == AbstractWindow.VIEW) {
            txtEnteredDate.setReadOnly(true);
            txtModifiedDate.setReadOnly(true);
            txtName.setReadOnly(true);
            txtId.setReadOnly(true);
            txtDeleted.setReadOnly(true);
            cmbFileVersion.setReadOnly(true);
            cmbCreatedBy.setReadOnly(true);
            cmbModifiedBy.setReadOnly(true);
            txtDescription.setReadOnly(true);
            cmbDbVersion.setReadOnly(true);
            save.setVisible(false);
        }
        if (mode != AbstractWindow.VIEW) {
            save.setVisible(true);
            txtEnteredDate.setReadOnly(false);
            txtModifiedDate.setReadOnly(false);
            txtName.setReadOnly(false);
            txtId.setReadOnly(false);
            txtDeleted.setReadOnly(false);
            cmbFileVersion.setReadOnly(false);
            cmbCreatedBy.setReadOnly(false);
            cmbModifiedBy.setReadOnly(false);
            txtDescription.setReadOnly(false);
            cmbDbVersion.setReadOnly(false);
        }
        if (mode != AbstractWindow.VIEW) {
            txtId.setReadOnly(true);
        }
    }

    public void save() {
        if (cmbFileVersion.getValue() == null) {
            MessageBox.alert("No FileVersion selected");
            return;
        }
        if (cmbCreatedBy.getValue() == null) {
            MessageBox.alert("No CreatedBy selected");
            return;
        }
        if (cmbModifiedBy.getValue() == null) {
            MessageBox.alert("No ModifiedBy selected");
            return;
        }
        if (cmbDbVersion.getValue() == null) {
            MessageBox.alert("No DbVersion selected");
            return;
        }
        if (txtEnteredDate.validate() && txtModifiedDate.validate() && txtName.validate() && txtId.validate() && txtDeleted.validate() && txtDescription.validate()) {
            Versions loc = new Versions();
            loc.setEnteredDate(txtEnteredDate.getText());
            loc.setModifiedDate(txtModifiedDate.getText());
            loc.setName(txtName.getText());
            loc.setId(txtId.getText());
            loc.setDeleted(Byte.parseByte(txtDeleted.getText()));
            loc.setFileVersion(cmbFileVersion.getValue());
            loc.setCreatedBy(cmbCreatedBy.getValue());
            loc.setModifiedBy(cmbModifiedBy.getValue());
            loc.setDescription(txtDescription.getText());
            loc.setDbVersion(cmbDbVersion.getValue());
            VersionsAdapter.getInstance().addVersions(loc, getGrid(), isNew);
        } else {
            MessageBox.alert("Invalid values");
            return;
        }
        getGrid().window.hide();
    }

    public void cancel() {
        getGrid().window.hide();
    }

    public void setData(Record record) {
        Versions loc = (Versions) getItemByID(record.getAsString("id"));
        txtEnteredDate.setValue(String.valueOf(loc.getEnteredDate()));
        txtModifiedDate.setValue(String.valueOf(loc.getModifiedDate()));
        txtName.setValue(String.valueOf(loc.getName()));
        txtId.setValue(String.valueOf(loc.getId()));
        txtDeleted.setValue(String.valueOf(loc.getDeleted()));
        cmbFileVersion.setValue(String.valueOf(loc.getFileVersion()));
        cmbCreatedBy.setValue(String.valueOf(loc.getCreatedBy()));
        cmbModifiedBy.setValue(String.valueOf(loc.getModifiedBy()));
        txtDescription.setValue(String.valueOf(loc.getDescription()));
        cmbDbVersion.setValue(String.valueOf(loc.getDbVersion()));
    }

    class SaveButtonAdapter extends ButtonListenerAdapter {

        AbstractForm parent;

        SaveButtonAdapter(AbstractForm parent) {
            this.parent = parent;
        }

        public void onClick(Button button, EventObject e) {
            parent.save();
        }
    }

    class CancelButtonAdapter extends ButtonListenerAdapter {

        AbstractForm parent;

        CancelButtonAdapter(AbstractForm parent) {
            this.parent = parent;
        }

        public void onClick(Button button, EventObject e) {
            parent.cancel();
        }
    }
}

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
import net.cygeek.tech.client.data.Leavetype;
import net.cygeek.tech.client.adapters.LeavetypeAdapter;
import net.cygeek.tech.client.ohrm;
import java.util.Date;

/**
 *
 */
public class LeavetypeForm extends AbstractForm {

    TextField txtAvailableFlag;

    TextField txtLeaveTypeId;

    TextField txtLeaveTypeName;

    Button save = new Button("Save");

    Button cancel = new Button("Cancel");

    public LeavetypeForm() {
        this.setFrame(true);
        this.setWidth(365);
        this.setLabelWidth(75);
        txtAvailableFlag = new TextField("AvailableFlag", "txtAvailableFlag", 230);
        txtAvailableFlag.setAllowBlank(false);
        this.add(txtAvailableFlag);
        txtLeaveTypeId = new TextField("LeaveTypeId", "txtLeaveTypeId", 230);
        txtLeaveTypeId.setAllowBlank(false);
        this.add(txtLeaveTypeId);
        txtLeaveTypeName = new TextField("LeaveTypeName", "txtLeaveTypeName", 230);
        txtLeaveTypeName.setAllowBlank(false);
        this.add(txtLeaveTypeName);
        save.addListener(new SaveButtonAdapter(this));
        this.addButton(save);
        cancel.addListener(new CancelButtonAdapter(this));
        this.addButton(cancel);
    }

    public void reinitialize(int mode) {
        this.isNew = (mode == AbstractWindow.ADD);
        txtLeaveTypeId.setReadOnly(!isNew);
        if (isNew) {
            txtAvailableFlag.setValue(null);
            txtLeaveTypeId.setValue(null);
            txtLeaveTypeName.setValue(null);
        } else if (mode == AbstractWindow.VIEW) {
            txtAvailableFlag.setReadOnly(true);
            txtLeaveTypeId.setReadOnly(true);
            txtLeaveTypeName.setReadOnly(true);
            save.setVisible(false);
        }
        if (mode != AbstractWindow.VIEW) {
            save.setVisible(true);
            txtAvailableFlag.setReadOnly(false);
            txtLeaveTypeId.setReadOnly(false);
            txtLeaveTypeName.setReadOnly(false);
        }
        if (mode != AbstractWindow.VIEW) {
            txtLeaveTypeId.setReadOnly(true);
        }
    }

    public void save() {
        if (txtAvailableFlag.validate() && txtLeaveTypeId.validate() && txtLeaveTypeName.validate()) {
            Leavetype loc = new Leavetype();
            loc.setAvailableFlag(Short.parseShort(txtAvailableFlag.getText()));
            loc.setLeaveTypeId(txtLeaveTypeId.getText());
            loc.setLeaveTypeName(txtLeaveTypeName.getText());
            LeavetypeAdapter.getInstance().addLeavetype(loc, getGrid(), isNew);
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
        Leavetype loc = (Leavetype) getItemByID(record.getAsString("leaveTypeId"));
        txtAvailableFlag.setValue(String.valueOf(loc.getAvailableFlag()));
        txtLeaveTypeId.setValue(String.valueOf(loc.getLeaveTypeId()));
        txtLeaveTypeName.setValue(String.valueOf(loc.getLeaveTypeName()));
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

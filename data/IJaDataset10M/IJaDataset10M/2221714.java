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
import net.cygeek.tech.client.data.EmpWorkExperience;
import net.cygeek.tech.client.adapters.EmpWorkExperienceAdapter;
import net.cygeek.tech.client.ohrm;
import java.util.Date;

/**
 * 
 */
public class EmpWorkExperienceForm extends AbstractForm {

    TextField txtEexpToDate;

    TextField txtEexpFromDate;

    TextField txtEexpComments;

    TextField txtEexpInternal;

    TextField txtEexpSeqno;

    TextField txtEexpEmployer;

    TextField txtEexpJobtit;

    ComboBox cmbEmpNumber;

    Button save = new Button("Save");

    Button cancel = new Button("Cancel");

    public EmpWorkExperienceForm() {
        this.setFrame(true);
        this.setWidth(365);
        this.setLabelWidth(75);
        txtEexpToDate = new TextField("EexpToDate", "txtEexpToDate", 230);
        txtEexpToDate.setAllowBlank(false);
        this.add(txtEexpToDate);
        txtEexpFromDate = new TextField("EexpFromDate", "txtEexpFromDate", 230);
        txtEexpFromDate.setAllowBlank(false);
        this.add(txtEexpFromDate);
        txtEexpComments = new TextField("EexpComments", "txtEexpComments", 230);
        txtEexpComments.setAllowBlank(false);
        this.add(txtEexpComments);
        txtEexpInternal = new TextField("EexpInternal", "txtEexpInternal", 230);
        txtEexpInternal.setAllowBlank(false);
        this.add(txtEexpInternal);
        txtEexpSeqno = new TextField("EexpSeqno", "txtEexpSeqno", 230);
        txtEexpSeqno.setAllowBlank(false);
        this.add(txtEexpSeqno);
        txtEexpEmployer = new TextField("EexpEmployer", "txtEexpEmployer", 230);
        txtEexpEmployer.setAllowBlank(false);
        this.add(txtEexpEmployer);
        txtEexpJobtit = new TextField("EexpJobtit", "txtEexpJobtit", 230);
        txtEexpJobtit.setAllowBlank(false);
        this.add(txtEexpJobtit);
        cmbEmpNumber = new ComboBox();
        cmbEmpNumber.setForceSelection(true);
        cmbEmpNumber.setMinChars(1);
        cmbEmpNumber.setFieldLabel("EmpNumber");
        cmbEmpNumber.setDisplayField("empNickName");
        cmbEmpNumber.setValueField("empNumber");
        cmbEmpNumber.setMode(ComboBox.LOCAL);
        cmbEmpNumber.setTriggerAction(ComboBox.ALL);
        cmbEmpNumber.setEmptyText("Select");
        cmbEmpNumber.setLoadingText("Searching...");
        cmbEmpNumber.setTypeAhead(true);
        cmbEmpNumber.setSelectOnFocus(true);
        cmbEmpNumber.setWidth(230);
        cmbEmpNumber.setHideTrigger(false);
        this.add(cmbEmpNumber);
        save.addListener(new SaveButtonAdapter(this));
        this.addButton(save);
        cancel.addListener(new CancelButtonAdapter(this));
        this.addButton(cancel);
    }

    public void reinitialize(int mode) {
        this.isNew = (mode == AbstractWindow.ADD);
        txtEexpSeqno.setReadOnly(!isNew);
        cmbEmpNumber.setReadOnly(!isNew);
        cmbEmpNumber.setStore(ohrm.GR_Employee.store);
        if (isNew) {
            txtEexpToDate.setValue(null);
            txtEexpFromDate.setValue(null);
            txtEexpComments.setValue(null);
            txtEexpInternal.setValue(null);
            txtEexpSeqno.setValue(null);
            txtEexpEmployer.setValue(null);
            txtEexpJobtit.setValue(null);
            cmbEmpNumber.setValue(null);
        } else if (mode == AbstractWindow.VIEW) {
            txtEexpToDate.setReadOnly(true);
            txtEexpFromDate.setReadOnly(true);
            txtEexpComments.setReadOnly(true);
            txtEexpInternal.setReadOnly(true);
            txtEexpSeqno.setReadOnly(true);
            txtEexpEmployer.setReadOnly(true);
            txtEexpJobtit.setReadOnly(true);
            cmbEmpNumber.setReadOnly(true);
            save.setVisible(false);
        }
        if (mode != AbstractWindow.VIEW) {
            save.setVisible(true);
            txtEexpToDate.setReadOnly(false);
            txtEexpFromDate.setReadOnly(false);
            txtEexpComments.setReadOnly(false);
            txtEexpInternal.setReadOnly(false);
            txtEexpSeqno.setReadOnly(false);
            txtEexpEmployer.setReadOnly(false);
            txtEexpJobtit.setReadOnly(false);
            cmbEmpNumber.setReadOnly(false);
        }
        if (mode != AbstractWindow.VIEW) {
            txtEexpSeqno.setReadOnly(true);
            cmbEmpNumber.setReadOnly(true);
        }
    }

    public void save() {
        if (cmbEmpNumber.getValue() == null) {
            MessageBox.alert("No EmpNumber selected");
            return;
        }
        if (txtEexpToDate.validate() && txtEexpFromDate.validate() && txtEexpComments.validate() && txtEexpInternal.validate() && txtEexpSeqno.validate() && txtEexpEmployer.validate() && txtEexpJobtit.validate()) {
            EmpWorkExperience loc = new EmpWorkExperience();
            loc.setEexpToDate(txtEexpToDate.getText());
            loc.setEexpFromDate(txtEexpFromDate.getText());
            loc.setEexpComments(txtEexpComments.getText());
            loc.setEexpInternal(Integer.parseInt(txtEexpInternal.getText()));
            loc.setEexpSeqno(new Double(Double.parseDouble(txtEexpSeqno.getText())));
            loc.setEexpEmployer(txtEexpEmployer.getText());
            loc.setEexpJobtit(txtEexpJobtit.getText());
            loc.setEmpNumber(Integer.parseInt(cmbEmpNumber.getValue()));
            EmpWorkExperienceAdapter.getInstance().addEmpWorkExperience(loc, getGrid(), isNew);
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
        EmpWorkExperience loc = (EmpWorkExperience) getItemByID(record.getAsString("eexpSeqno") + "#" + record.getAsString("empNumber"));
        txtEexpToDate.setValue(String.valueOf(loc.getEexpToDate()));
        txtEexpFromDate.setValue(String.valueOf(loc.getEexpFromDate()));
        txtEexpComments.setValue(String.valueOf(loc.getEexpComments()));
        txtEexpInternal.setValue(String.valueOf(loc.getEexpInternal()));
        txtEexpSeqno.setValue(String.valueOf(loc.getEexpSeqno()));
        txtEexpEmployer.setValue(String.valueOf(loc.getEexpEmployer()));
        txtEexpJobtit.setValue(String.valueOf(loc.getEexpJobtit()));
        cmbEmpNumber.setValue(String.valueOf(loc.getEmpNumber()));
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

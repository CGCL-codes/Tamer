package net.cygeek.tech.client.ui.form;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.TextField;
import net.cygeek.tech.client.adapters.EducationAdapter;
import net.cygeek.tech.client.data.Education;
import net.cygeek.tech.client.util.TS;
import net.cygeek.tech.client.util.MessageStore;

/**
 *
 */
public class EducationForm extends AbstractForm {

    TextField txtEduCode;

    TextField txtEduDeg;

    TextField txtEduUni;

    Button save = new Button(TS.gi().get("L0001"));

    Button cancel = new Button(TS.gi().get("L0003"));

    public EducationForm() {
        this.setFrame(true);
        this.setWidth(375);
        this.setLabelWidth(75);
        txtEduCode = new TextField(TS.gi().get("L0125"), "txtEduCode", 230);
        txtEduDeg = new TextField(TS.gi().get("L0126"), "txtEduDeg", 230);
        txtEduUni = new TextField(TS.gi().get("L0127"), "txtEduUni", 230);
        this.add(txtEduCode);
        this.add(txtEduUni);
        this.add(txtEduDeg);
        save.addListener(new SaveButtonAdapter(this));
        this.addButton(save);
        cancel.addListener(new CancelButtonAdapter(this));
        this.addButton(cancel);
    }

    public void reinitialize(int mode) {
        this.setTitle(TS.gi().get("L0124"));
        this.isNew = (mode == AbstractWindow.ADD);
        if (isNew) {
            txtEduCode.setValue("");
            txtEduDeg.setValue("");
            txtEduUni.setValue("");
        } else if (mode == AbstractWindow.VIEW) {
            txtEduCode.setReadOnly(true);
            txtEduDeg.setReadOnly(true);
            txtEduUni.setReadOnly(true);
            save.setVisible(false);
        }
        if (mode != AbstractWindow.VIEW) {
            save.setVisible(true);
            txtEduCode.setReadOnly(false);
            txtEduDeg.setReadOnly(false);
            txtEduUni.setReadOnly(false);
        }
        if (mode != AbstractWindow.ADD) {
            txtEduCode.setReadOnly(true);
        } else {
            txtEduCode.setReadOnly(false);
        }
    }

    public void save() {
        if (txtEduCode.getText() == null || txtEduCode.getText().trim().equals("")) {
            MessageStore.showIsEmptyError("L0125");
            return;
        }
        if (isNew && getItemByID(txtEduCode.getText()) != null) {
            MessageStore.showDuplicate("L0124");
            return;
        }
        if (txtEduUni.getText() == null || txtEduUni.getText().trim().equals("")) {
            MessageStore.showIsEmptyError("L0127");
            return;
        }
        if (txtEduDeg.getText() == null || txtEduDeg.getText().trim().equals("")) {
            MessageStore.showIsEmptyError("L0126");
            return;
        }
        Education loc = new Education();
        loc.setEduCode(txtEduCode.getText());
        loc.setEduDeg(txtEduUni.getText() + "/" + txtEduDeg.getText());
        loc.setEduUni(txtEduUni.getText());
        EducationAdapter.getInstance().addEducation(loc, getGrid(), isNew);
        getGrid().window.hide();
    }

    public void cancel() {
        getGrid().window.hide();
    }

    public void setData(Record record) {
        Education loc = (Education) getItemByID(record.getAsString("eduCode"));
        txtEduCode.setValue(String.valueOf(loc.getEduCode()));
        txtEduDeg.setValue(String.valueOf(loc.getEduDeg()));
        txtEduUni.setValue(String.valueOf(loc.getEduUni()));
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

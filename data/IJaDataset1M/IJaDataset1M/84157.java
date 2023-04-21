package com.qotsa.j2me.form;

import com.qotsa.j2me.list.ComputerMenu;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.TextField;

/**
 *
 * @author Francisco
 */
public class PowerOffRestartForm extends ComputerForm implements ItemStateListener {

    private ChoiceGroup chGMode;

    private TextField tfTime;

    /** Creates a new instance of PowerOffForm */
    public PowerOffRestartForm(String title, ComputerMenu cm) {
        super(title, cm);
        tfTime = new TextField(null, null, 5, TextField.UNEDITABLE);
        chGMode = new ChoiceGroup(null, Choice.EXCLUSIVE);
        chGMode.append("Now", null);
        chGMode.append("In Seconds", null);
        chGMode.setSelectedIndex(0, true);
        setItemStateListener(this);
        append(chGMode);
        append(tfTime);
    }

    public void itemStateChanged(Item item) {
        if (item.equals(chGMode)) {
            if (chGMode.getSelectedIndex() == 0) tfTime.setConstraints(TextField.UNEDITABLE); else tfTime.setConstraints(TextField.NUMERIC);
            tfTime.setString("0");
        }
    }

    public TextField getTfTime() {
        return tfTime;
    }
}

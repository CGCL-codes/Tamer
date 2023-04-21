package net.pleso.framework.client.ui.custom.controls.data;

import net.pleso.framework.client.dal.db.IDBValue;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Data control with text box for float values
 * 
 * <h3>CSS Style Rules</h3>
 * <ul>
 * <li>.pf-floatDataControl { control itself }</li>
 * </ul>
 * 
 * @author Scater
 *
 */
public class FloatDataControl extends TextBoxDataControl {

    public FloatDataControl() {
        super();
        this.textBox.addKeyboardListener(new KeyboardListenerAdapter() {

            public void onKeyPress(Widget sender, char keyCode, int modifiers) {
                if (!Character.isDigit(keyCode) && keyCode != KEY_BACKSPACE && keyCode != KEY_DELETE && keyCode != KEY_LEFT && keyCode != KEY_RIGHT && !(keyCode == 'v' && modifiers == MODIFIER_CTRL) && !(keyCode == 'V' && modifiers == MODIFIER_CTRL) && !(keyCode == 'c' && modifiers == MODIFIER_CTRL) && !(keyCode == 'C' && modifiers == MODIFIER_CTRL) && !Character.toString(keyCode).equals(".")) {
                    ((TextBox) sender).cancelKey();
                }
            }
        });
        this.setStyleName("pf-floatDataControl");
    }

    public IDBValue getValue() {
        return this.value;
    }
}

package ezsudoku.view;

import java.awt.Dimension;
import java.text.MessageFormat;
import javax.swing.JToggleButton;
import javax.swing.Icon;

/**
 * Button in toolbar where to choose value.
 *
 * @author Cedric Chantepie (cchantepie@corsaire.fr)
 */
public class NumberButton extends JToggleButton {

    /**
     */
    public static final MessageFormat ACTION_NAME_FMT = new MessageFormat("setItemValue({0,number,integer})");

    /**
     */
    private Integer value = null;

    /**
     */
    public Integer getValue() {
        return this.value;
    }

    /**
     */
    public void setValue(final Integer value) {
        this.value = value;
        String actionName = ACTION_NAME_FMT.format(new Object[] { value });
        this.setActionCommand("NumberButton:" + actionName);
    }

    /**
     */
    private void init() {
    }

    /**
     */
    public NumberButton(Integer value) {
        init();
        setValue(value);
    }

    /**
     */
    public NumberButton(Icon icon) {
        super(icon);
        init();
    }

    /**
     */
    public NumberButton(Integer value, Icon icon) {
        super(icon);
        init();
        setValue(value);
    }
}

package DE.FhG.IGD.semoa.envision;

import javax.swing.border.*;
import javax.swing.*;
import java.awt.GridLayout;

/**
 * A swing based editor for <code>String</code> values.   
 *
 * @author Jan Haevecker (U.Pinsdorf)
 * @version 26.10.2001
 */
public class StringEditor extends AbstractEditor {

    public StringEditor(String title) {
        super();
        setValueComponent(new TextField());
        setBorder(new EtchedBorder());
        setLayout(new GridLayout(0, 2));
        add(new JLabel(title));
        add(getValueComponent());
    }

    public void setString(String s) {
        ((TextComponent) getValueComponent()).setText(s);
    }

    public String getString() {
        return ((TextComponent) getValueComponent()).getText();
    }

    protected void chooseValueComponent() {
        remove(getValueComponent());
        if (this.isEditable()) {
            setValueComponent(new TextField());
        } else {
            setValueComponent(new Label());
        }
        add(getValueComponent());
    }
}

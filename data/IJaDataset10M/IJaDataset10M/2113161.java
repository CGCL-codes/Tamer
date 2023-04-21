package de.fhg.igd.semoa.envision.uihelper;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;

/**
 * A swing based editor for (positive) <code>long</code> values.
 *
 * @author Jan Haevecker (U.Pinsdorf)
 * @version 07.11.2001
 */
public class LongEditor extends AbstractEditor {

    public LongEditor(String title) {
        super();
        setValueComponent(new LongField());
        setBorder(new EtchedBorder());
        setLayout(new GridLayout(0, 2));
        add(new JLabel(title));
        add(getValueComponent());
    }

    public void setValue(long value) {
        if (this.isEditable()) {
            ((LongField) getValueComponent()).setValue(value);
        } else {
            ((TextComponent) getValueComponent()).setText(Long.toString(value));
        }
    }

    public long getValue() {
        if (this.isEditable()) {
            return ((LongField) getValueComponent()).getValue();
        } else {
            return Long.parseLong(((Label) getValueComponent()).getText());
        }
    }

    protected void chooseValueComponent() {
        remove(getValueComponent());
        if (this.isEditable()) {
            setValueComponent(new LongField());
        } else {
            setValueComponent(new Label());
        }
        add(getValueComponent());
    }
}

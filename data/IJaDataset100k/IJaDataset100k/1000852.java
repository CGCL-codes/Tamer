package de.schwarzrot.ui.control;

import java.awt.event.FocusEvent;
import java.text.Format;
import javax.swing.JFormattedTextField;

/**
 * wrapper to {@code JFormattedTextField} to allow content selection on focus
 * entry.
 * 
 * @author <a href="mailto:rmantey@users.sourceforge.net">Reinhard Mantey</a>
 * 
 */
public class FormattedTextField extends JFormattedTextField {

    private static final long serialVersionUID = 713L;

    public FormattedTextField() {
        super();
    }

    public FormattedTextField(AbstractFormatter formatter) {
        super(formatter);
    }

    public FormattedTextField(AbstractFormatterFactory factory) {
        super(factory);
    }

    public FormattedTextField(AbstractFormatterFactory factory, Object currentValue) {
        super(factory, currentValue);
    }

    public FormattedTextField(Format format) {
        super(format);
    }

    public FormattedTextField(Object value) {
        super(value);
    }

    @Override
    protected void processFocusEvent(FocusEvent e) {
        super.processFocusEvent(e);
        selectAll();
    }
}

package net.sf.doolin.gui.field.support.swing;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.sf.doolin.gui.field.FieldText;
import net.sf.doolin.gui.field.event.EventAction;
import net.sf.doolin.gui.field.support.TextSupport;
import net.sf.doolin.gui.swing.MaxlengthDocument;

/**
 * Support for the text field, based on a text field.
 * 
 * @author Damien Coraboeuf
 * @version $Id: SwingTextSupport.java,v 1.2 2007/08/07 16:47:05 guinnessman Exp $
 */
public class SwingTextSupport extends AbstractSwingInfoFieldSupport<FieldText, JTextField> implements TextSupport {

    private JTextField field;

    @Override
    protected void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        field.setEditable(!readOnly);
    }

    @Override
    protected JTextField createComponent() {
        field = new JTextField();
        field.setColumns(getField().getColumns());
        int maxlength = getField().getMaxlength();
        if (maxlength > 0) {
            MaxlengthDocument maxlengthDocument = new MaxlengthDocument(maxlength);
            field.setDocument(maxlengthDocument);
        }
        return field;
    }

    public void setText(String text) {
        field.setText(text);
    }

    public String getText() {
        return field.getText();
    }

    public void bindEditEvent(final EventAction eventAction) {
        field.getDocument().addDocumentListener(new DocumentListener() {

            public void removeUpdate(DocumentEvent e) {
                edit();
            }

            public void insertUpdate(DocumentEvent e) {
                edit();
            }

            public void changedUpdate(DocumentEvent e) {
                edit();
            }

            protected void edit() {
                eventAction.execute(getView(), getField(), null);
            }
        });
    }
}

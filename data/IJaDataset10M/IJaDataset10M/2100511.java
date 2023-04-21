package net.sf.doolin.gui.field.support.swing;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.sf.doolin.gui.field.FieldMemo;
import net.sf.doolin.gui.field.event.EventAction;
import net.sf.doolin.gui.field.support.MemoSupport;
import net.sf.doolin.gui.swing.MaxlengthDocument;

/**
 * Support for the text field, based on a text field.
 * 
 * @author Damien Coraboeuf
 */
public class SwingMemoSupport extends AbstractSwingInfoFieldSupport<FieldMemo, JScrollPane> implements MemoSupport {

    private JTextArea field;

    private JScrollPane scrollPane;

    @Override
    protected void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        field.setEditable(!readOnly);
    }

    @Override
    protected JScrollPane createComponent() {
        field = new JTextArea();
        field.setLineWrap(true);
        field.setWrapStyleWord(true);
        field.setColumns(getField().getColumns());
        field.setRows(getField().getRows());
        int maxlength = getField().getMaxlength();
        if (maxlength > 0) {
            MaxlengthDocument maxlengthDocument = new MaxlengthDocument(maxlength);
            field.setDocument(maxlengthDocument);
        }
        scrollPane = new JScrollPane(field);
        return scrollPane;
    }

    @Override
    public JComponent getFocusableComponent() {
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

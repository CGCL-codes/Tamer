package ao.dd.desktop.dash.model.field;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.text.SimpleAttributeSet;
import java.io.Serializable;

/**
 * User: AO
 * Date: 3/26/11
 * Time: 7:58 PM
 */
public class BoundTextField implements Serializable {

    private final Document model;

    public BoundTextField(String initialValue) {
        model = new PlainDocument();
        set(initialValue);
    }

    public String get() {
        try {
            return model.getText(0, model.getLength());
        } catch (BadLocationException e) {
            return null;
        }
    }

    private void set(String value) {
        if (value == null) {
            value = "";
        }
        try {
            model.remove(0, model.getLength());
            model.insertString(0, value, new SimpleAttributeSet());
        } catch (BadLocationException e) {
            throw new Error(e);
        }
    }

    public JComponent view(boolean readOnly) {
        if (!readOnly) {
            return new JTextField(model, get(), 16);
        }
        final JLabel readOnlyView = new JLabel(get());
        readOnlyView.addAncestorListener(new AncestorListener() {

            private DocumentListener listener = new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    readOnlyView.setText(get());
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    readOnlyView.setText(get());
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    readOnlyView.setText(get());
                }
            };

            @Override
            public void ancestorAdded(AncestorEvent event) {
                model.addDocumentListener(listener);
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
                model.removeDocumentListener(listener);
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
            }
        });
        return readOnlyView;
    }
}

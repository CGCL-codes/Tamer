package org.progeeks.meta.swing.editor;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import org.progeeks.meta.swing.*;

/**
 *  An editor implementation for editing long string values.  It
 *  uses a multi-line JEditPane for the job.
 *
 *  @version   $Revision: 1.8 $
 *  @author    Paul Speed
 */
public class LongStringEditor extends AbstractPropertyEditor implements MetaPropertyRenderer {

    private JEditorPane editor;

    private JScrollPane scrollPane;

    private EditorListener editListener = new EditorListener();

    private boolean ignoreEvents = false;

    private boolean autoscroll = false;

    public LongStringEditor() {
        this(false);
    }

    public LongStringEditor(boolean readOnly) {
        editor = new JEditorPane() {

            public Dimension getPreferredScrollableViewportSize() {
                return (new Dimension(300, 200));
            }
        };
        editor.setEditable(!readOnly);
        scrollPane = new JScrollPane(editor);
        editor.getDocument().addDocumentListener(editListener);
    }

    /**
     *  Set to true for the component to automatically scroll to the end
     *  of the text whenever the value changes.
     */
    public void setAutoScroll(boolean f) {
        this.autoscroll = f;
    }

    public boolean isAutoScroll() {
        return (autoscroll);
    }

    /**
     *  Returns false since editor pane should be a full-width control.
     */
    public boolean isSingleColumn() {
        return (false);
    }

    /**
     *  Returns the component that allows modification of the
     *  associated property mutator.
     */
    public Component getUIComponent() {
        return (scrollPane);
    }

    /**
     *  Implemented by subclasses to release any component-related
     *  resources.
     */
    protected void releaseComponent() {
        editor.getDocument().removeDocumentListener(editListener);
    }

    /**
     *  Called to set the current value displayed in the component.
     */
    protected void setComponentValue(Object value) {
        ignoreEvents = true;
        try {
            if (value == null) editor.setText(""); else editor.setText(String.valueOf(value));
            editor.setMinimumSize(new Dimension(300, 200));
            editor.setPreferredSize(new Dimension(300, 200));
            scrollPane.setMinimumSize(new Dimension(300, 200));
            if (autoscroll) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        JScrollBar bar = scrollPane.getVerticalScrollBar();
                        bar.setValue(bar.getMaximum());
                    }
                });
            }
        } finally {
            ignoreEvents = false;
        }
    }

    /**
     *  Called to set the component value to a default state.
     *  The default implementation calls setComponentValue(null).
     */
    protected void resetComponentValue() {
        editor.setText("");
    }

    private class EditorListener implements DocumentListener {

        public void insertUpdate(DocumentEvent e) {
            if (ignoreEvents) return;
            setPropertyValue(editor.getText());
        }

        public void removeUpdate(DocumentEvent e) {
            if (ignoreEvents) return;
            setPropertyValue(editor.getText());
        }

        public void changedUpdate(DocumentEvent e) {
            if (ignoreEvents) return;
            setPropertyValue(editor.getText());
        }
    }
}

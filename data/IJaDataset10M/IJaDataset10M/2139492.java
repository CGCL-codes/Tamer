package org.octave.graphics;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class EditControl extends JTextField implements UIControl, ActionListener, DocumentListener, FocusListener {

    UIControlObject uiObj;

    HandleEventSinkAdapter sink;

    private boolean contentChanged = false;

    public EditControl(UIControlObject obj) {
        super();
        addActionListener(this);
        addFocusListener(this);
        getDocument().addDocumentListener(this);
        uiObj = obj;
        setAlignment();
        setText(obj.UIString.toString());
        sink = new HandleEventSinkAdapter() {

            public void eventOccured(HandleEvent evt) throws PropertyException {
                propertyChanged(evt.getProperty());
            }
        };
        sink.listen(obj.UIString);
        sink.listen(obj.HorizontalAlignment);
    }

    public void setAlignment() {
        setHorizontalAlignment(uiObj.HorizontalAlignment.is("center") ? JTextField.CENTER : uiObj.HorizontalAlignment.is("left") ? JTextField.LEFT : uiObj.HorizontalAlignment.is("right") ? JTextField.RIGHT : JTextField.LEFT);
    }

    public void propertyChanged(Property p) throws PropertyException {
        if (p == uiObj.UIString) setText(uiObj.UIString.toString()); else if (p == uiObj.HorizontalAlignment) setAlignment();
    }

    public void update() {
        uiObj.UIString.reset(getText());
    }

    public JComponent getComponent() {
        return this;
    }

    public void dispose() {
        sink.dispose();
    }

    public void actionPerformed(ActionEvent event) {
        uiObj.controlActivated(new UIControlEvent(this));
        contentChanged = false;
    }

    public void changedUpdate(DocumentEvent e) {
    }

    public void insertUpdate(DocumentEvent e) {
        contentChanged = true;
    }

    public void removeUpdate(DocumentEvent e) {
        contentChanged = true;
    }

    public void focusGained(FocusEvent e) {
        contentChanged = false;
    }

    public void focusLost(FocusEvent e) {
        if (contentChanged) {
            uiObj.controlActivated(new UIControlEvent(this));
            contentChanged = false;
        }
    }
}

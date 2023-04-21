package com.vscorp.ui.swing;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * Abstract class providing support for a text field and a button
 * that invokes a popup or dialog.
 *
 * @author Daniel A. Syrstad
 */
public abstract class ChooserField extends JPanel {

    private ActionListener mTextFieldActionListener = new TextFieldActionListener();

    private ActionListener mPopupButtonActionListener = new PopupButtonActionListener();

    private JTextField mTextField;

    private JButton mPopupButton;

    private String mLastTextValue = null;

    /** ChangeListeners to receive notifications when the value changes.  */
    private EventListenerList mListeners;

    /**
     * Create a new ChooserField with no button. Button can be set with the
     * setPopupButtonMethod.
     */
    protected ChooserField() {
        super(new BorderLayout());
        mTextField = new JTextField();
        mTextField.setInputVerifier(new FieldInputVerifier());
        mTextField.addActionListener(mTextFieldActionListener);
        this.add(mTextField, BorderLayout.CENTER);
    }

    /**
     * Gets the text field for this component.
     *
     * @return the JTextField portion of this component.
     */
    public JTextField getTextField() {
        return mTextField;
    }

    /**
     * Gets the popup button for this component.
     *
     * @return the JButton portion of this component.
     */
    public JButton getPopupButton() {
        return mPopupButton;
    }

    /**
     * Sets the popup button for this component.
     *
     * @param aPopupButton the JButton portion of this component.
     */
    protected void setPopupButton(JButton aPopupButton) {
        if (mPopupButton != null) {
            mPopupButton.removeActionListener(mPopupButtonActionListener);
            this.remove(mPopupButton);
        }
        mPopupButton = aPopupButton;
        if (mPopupButton != null) {
            mPopupButton.addActionListener(mPopupButtonActionListener);
            this.add(mPopupButton, BorderLayout.EAST);
        }
    }

    /**
     * Opens the popup if it is closed.
     */
    public abstract void openPopup();

    /**
     * Closes the popup if it is open.
     */
    public abstract void closePopup();

    /**
     * Verifies the content of the field. Default implementation returns true.
     *
     * @return true if the field contents are valid, else false.
     */
    protected boolean verify() {
        return true;
    }

    /**
     * Gets the text of this field. Equivalent to getTextField().getText().
     *
     * @return the text of the field.
     */
    protected String getText() {
        return mTextField.getText();
    }

    /**
     * Sets the text of this field. Equivalent to getTextField().setText(s).
     *
     * @param aString the text of the field.
     */
    protected void setText(String aString) {
        if (mLastTextValue == null || !mLastTextValue.equals(aString)) {
            mTextField.setText(aString);
            mLastTextValue = aString;
            fireStateChanged();
        }
    }

    /**
     * Enables or disables the field.
     *
     * @param anEnabledFlag true if the field is enabled, else false.
     */
    public void setEnabled(boolean anEnabledFlag) {
        super.setEnabled(anEnabledFlag);
        mTextField.setEnabled(anEnabledFlag);
        if (mPopupButton != null) {
            mPopupButton.setEnabled(anEnabledFlag);
        }
    }

    /**
     * Adds a ChangeListener to be notified when the field's value changes.
     *
     * @param aChangeListener the listener to be added
     */
    public synchronized void addChangeListener(ChangeListener aChangeListener) {
        if (mListeners == null) {
            mListeners = new EventListenerList();
        }
        mListeners.add(ChangeListener.class, aChangeListener);
    }

    /**
     * Removes an action listener from the list.
     *
     * @param aListener the listener to be removed.
     */
    public synchronized void removeChangeListener(ChangeListener aChangeListener) {
        if (mListeners != null) {
            mListeners.remove(ChangeListener.class, aChangeListener);
        }
    }

    /**
     * Conditionally fire ChangeEvent if value has changed since last firing.
     */
    protected void fireStateChanged() {
        ChangeEvent event = new ChangeEvent(this);
        if (mListeners != null) {
            Object[] listenerList = mListeners.getListenerList();
            for (int i = listenerList.length - 1; i > 0; i -= 2) {
                ((ChangeListener) listenerList[i]).stateChanged(event);
            }
        }
    }

    /**
     * Sets the font on the text field and the panel.
     */
    public void setFont(Font aFont) {
        super.setFont(aFont);
        if (mTextField != null) {
            mTextField.setFont(aFont);
        }
    }

    /**
     * Called when the drop-down button is clicked.
     */
    private class PopupButtonActionListener implements ActionListener {

        public void actionPerformed(ActionEvent anEvent) {
            ChooserField.this.openPopup();
        }
    }

    /**
     * Called when the Enter key is pressed on the text field. Verifies entry.
     */
    private class TextFieldActionListener implements ActionListener {

        public void actionPerformed(ActionEvent anEvent) {
            ChooserField.this.verify();
        }
    }

    /**
     * Verifies the fields contents via a call to the sub-class's verify
     * method.
     */
    private class FieldInputVerifier extends InputVerifier {

        public boolean verify(JComponent aComponent) {
            return ChooserField.this.verify();
        }
    }
}

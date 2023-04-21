package fr.fg.client.openjwt.ui;

import java.util.ArrayList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import fr.fg.client.i18n.StaticMessages;
import fr.fg.client.openjwt.OpenJWT;

public class JSOptionPane extends JSDialog implements DialogListener, ClickListener {

    public static final String ERROR_MESSAGE = "error", INFORMATION_MESSAGE = "information", WARNING_MESSAGE = "warning", QUESTION_MESSAGE = "question", PLAIN_MESSAGE = "plain";

    public static final int NO_OPTION = 1, YES_OPTION = 2, CANCEL_OPTION = 4, OK_OPTION = 8, CLOSED_OPTION = 16;

    private static final String ICON_OK = "ok", ICON_CANCEL = "cancel", ICON_YES = "yes", ICON_NO = "no";

    private JSButton yesBt, noBt, okBt, cancelBt;

    private JSComboBox valuesComboBox;

    private JSTextField inputField;

    private OptionPaneListener listener;

    protected JSOptionPane(String message, String title, int options, String type, OptionPaneListener listener, String[] values, String defaultValue, boolean input, String inputText, boolean passwordField) {
        super(title, true, true, true);
        if (type == null || type.length() == 0) type = PLAIN_MESSAGE;
        this.listener = listener;
        addDialogListener(this);
        ArrayList<JSButton> buttons = new ArrayList<JSButton>();
        StaticMessages messages = (StaticMessages) GWT.create(StaticMessages.class);
        if ((options & YES_OPTION) != 0) {
            yesBt = new JSButton(messages.yes());
            yesBt.addStyleName(ICON_YES);
            buttons.add(yesBt);
        }
        if ((options & NO_OPTION) != 0) {
            noBt = new JSButton(messages.no());
            noBt.addStyleName(ICON_NO);
            buttons.add(noBt);
        }
        if ((options & OK_OPTION) != 0) {
            okBt = new JSButton(messages.ok());
            okBt.addStyleName(ICON_OK);
            buttons.add(okBt);
        }
        if ((options & CANCEL_OPTION) != 0) {
            cancelBt = new JSButton(messages.cancel());
            cancelBt.addStyleName(ICON_CANCEL);
            buttons.add(cancelBt);
        }
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setPixelWidth(100);
            buttons.get(i).addClickListener(this);
        }
        HTMLPanel contentPanel = new HTMLPanel("<table class=\"standard " + type + "\" unselectable=\"on\">" + "<tr unselectable=\"on\">" + "<td class=\"icon\" unselectable=\"on\">" + "<div unselectable=\"on\"></div></td>" + "<td>" + message + "</td></tr></table>");
        contentPanel.addStyleName("content");
        OpenJWT.setElementFloat(contentPanel.getElement(), "left");
        JSRowLayout layout = new JSRowLayout();
        layout.addComponent(contentPanel);
        layout.addRow();
        if (values != null) {
            valuesComboBox = new JSComboBox();
            valuesComboBox.setPixelWidth(200);
            for (int i = 0; i < values.length; i++) {
                valuesComboBox.addItem(values[i]);
                if (values[i] == defaultValue) valuesComboBox.setSelectedIndex(i);
            }
            layout.setRowAlignment(JSRowLayout.ALIGN_CENTER);
            layout.addComponent(valuesComboBox);
            layout.addRowSeparator(8);
        } else if (input) {
            if (passwordField) inputField = new JSPasswordField(inputText != null ? inputText : ""); else inputField = new JSTextField(inputText != null ? inputText : "");
            inputField.setPixelWidth(200);
            layout.setRowAlignment(JSRowLayout.ALIGN_CENTER);
            layout.addComponent(inputField);
            layout.addRowSeparator(8);
        }
        layout.setRowAlignment(JSRowLayout.ALIGN_CENTER);
        for (int i = 0; i < buttons.size(); i++) layout.addComponent(buttons.get(i));
        setComponent(layout);
        centerOnScreen();
        setDefaultCloseOperation(DESTROY_ON_CLOSE);
        setVisible(true);
        if (inputText != null && inputText.length() > 0) inputField.select();
    }

    public void dialogClosed(Widget sender) {
        if (listener != null) {
            if (inputField != null || valuesComboBox != null) listener.optionSelected(null); else listener.optionSelected(CLOSED_OPTION);
        }
        cleanUp();
    }

    public void onClick(Widget sender) {
        if (listener != null) {
            if (inputField != null) {
                if (sender == yesBt || sender == okBt) listener.optionSelected(inputField.getText()); else if (sender == noBt || sender == cancelBt) listener.optionSelected(null);
            } else if (valuesComboBox != null) {
                if (sender == yesBt || sender == okBt) listener.optionSelected(valuesComboBox.getSelectedItem().toString()); else if (sender == noBt || sender == cancelBt) listener.optionSelected(null);
            } else {
                if (sender == yesBt) listener.optionSelected(YES_OPTION); else if (sender == noBt) listener.optionSelected(NO_OPTION); else if (sender == okBt) listener.optionSelected(OK_OPTION); else if (sender == cancelBt) listener.optionSelected(CANCEL_OPTION);
            }
        }
        cleanUp();
        setVisible(false);
    }

    public static void showMessageDialog(String message, String title, int options, String type, OptionPaneListener listener) {
        new JSOptionPane(message, title, options, type, listener, null, null, false, null, false);
    }

    public static void showOptionDialog(String message, String title, int options, String type, OptionPaneListener listener, String[] values, String defaultValue) {
        new JSOptionPane(message, title, options, type, listener, values, defaultValue, false, null, false);
    }

    public static void showInputDialog(String message, String title, int options, String type, OptionPaneListener listener, String inputText) {
        new JSOptionPane(message, title, options, type, listener, null, null, true, inputText, false);
    }

    public static void showInputDialog(String message, String title, int options, String type, OptionPaneListener listener, String inputText, boolean passwordField) {
        new JSOptionPane(message, title, options, type, listener, null, null, true, inputText, passwordField);
    }

    private void cleanUp() {
        listener = null;
        removeDialogListener(this);
        if (yesBt != null) {
            yesBt.removeClickListener(this);
            yesBt = null;
        }
        if (noBt != null) {
            noBt.removeClickListener(this);
            noBt = null;
        }
        if (cancelBt != null) {
            cancelBt.removeClickListener(this);
            cancelBt = null;
        }
        if (okBt != null) {
            okBt.removeClickListener(this);
            okBt = null;
        }
        inputField = null;
        valuesComboBox = null;
    }
}

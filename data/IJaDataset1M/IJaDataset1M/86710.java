package fr.fg.client.ajax;

import com.google.gwt.core.client.GWT;
import fr.fg.client.data.AnswerData;
import fr.fg.client.i18n.StaticMessages;
import fr.fg.client.openjwt.ui.JSOptionPane;

public class ActionCallbackAdapter implements ActionCallback {

    public void onSuccess(AnswerData data) {
    }

    public void onFailure(String error) {
        onFailureDefaultBehavior(error);
    }

    public static void onFailureDefaultBehavior(String error) {
        StaticMessages messages = (StaticMessages) GWT.create(StaticMessages.class);
        if (error.length() > 1000) error = error.substring(0, 1000) + "[...]";
        JSOptionPane.showMessageDialog(error, messages.error(), JSOptionPane.OK_OPTION, JSOptionPane.ERROR_MESSAGE, null);
    }
}

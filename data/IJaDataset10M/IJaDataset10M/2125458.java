package net.sourceforge.openwebarch.app.thetimeloop.client.common;

import net.sourceforge.openwebarch.app.thetimeloop.client.TheTimeLoop;
import net.sourceforge.openwebarch.app.thetimeloop.client.composite.communicationerror.CommunicationErrorComposite;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;

public abstract class AbstractAsyncCallback<T> implements AsyncCallback<T> {

    private TheTimeLoop application;

    protected AbstractAsyncCallback(final TheTimeLoop application) {
        this.application = application;
    }

    private String buildMessage(Throwable t) {
        final StringBuilder sb = new StringBuilder();
        while (t != null) {
            sb.append(t.getMessage());
            sb.append("\n");
            t = t.getCause();
        }
        return sb.toString();
    }

    @Override
    public void onFailure(final Throwable caught) {
        application.getEntryComposite().getStatusTextWidget().setText(application.getEntryComposite().getConstants().main_menu_status_error() + " " + caught.getMessage());
        final DialogBox dialogBox = new DialogBox();
        dialogBox.setText("Error");
        final CommunicationErrorComposite errorComposite = application.createCommunicationErrorComposite();
        errorComposite.getErrorMessageWidget().setText(buildMessage(caught));
        dialogBox.setWidget(errorComposite.getRootWidget());
        dialogBox.center();
        dialogBox.show();
    }
}

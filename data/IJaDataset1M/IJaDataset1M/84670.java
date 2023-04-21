package client;

import java.util.ArrayList;
import java.util.List;
import client.events.IEvent;
import client.model.Contact;
import client.model.Message;
import client.translations.YaChatConstants;
import client.view.ChatView;
import client.view.SignInView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * Process messages from server
 * @author Konstantin A. Kudryavtsev
 *
 */
public class ChatServiceClientImpl implements IMessageViewListener {

    private IChatServiceAsync messengerService;

    private ChatView view = new ChatView(this);

    private class SignInCallback implements AsyncCallback {

        public void onFailure(Throwable throwable) {
            YaChatConstants translate = GWT.create(YaChatConstants.class);
            DockPanel mainPanel = view.getMainPanel();
            mainPanel.clear();
            Label label = new Label(translate.LoginPasswordWrong());
            label.setStyleName("error");
            mainPanel.add(label, DockPanel.NORTH);
            mainPanel.add(new SignInView(view), DockPanel.CENTER);
            Window.alert(translate.LoginPasswordWrong());
        }

        public void onSuccess(Object obj) {
            messengerService.getEvents(new GetEventsCallback());
        }
    }

    private class EmptyCallback implements AsyncCallback {

        public void onFailure(Throwable throwable) {
            Window.alert("error" + throwable.getMessage());
        }

        public void onSuccess(Object obj) {
        }
    }

    private class GetEventsCallback implements AsyncCallback {

        public void onFailure(Throwable throwable) {
            Window.alert("error get events" + throwable.getMessage());
        }

        public void onSuccess(Object obj) {
            List<IEvent> events = (List<IEvent>) obj;
            for (IEvent event : events) {
                handleEvent(event);
            }
            messengerService.getEvents(this);
        }
    }

    public ChatServiceClientImpl(String URL) {
        messengerService = (IChatServiceAsync) GWT.create(IChatService.class);
        ServiceDefTarget endpoint = (ServiceDefTarget) messengerService;
        endpoint.setServiceEntryPoint(URL);
    }

    public ChatView getView() {
        return view;
    }

    public void onSignIn(String login, String password) {
        messengerService.signIn(login, password, new SignInCallback());
    }

    public void onSignOut() {
        messengerService.signOut(new EmptyCallback());
    }

    public void onSendMessage(Contact toContact, Message message) {
        messengerService.sendMessage(toContact, message, new EmptyCallback());
    }

    protected void handleEvent(Object event) {
    }
}

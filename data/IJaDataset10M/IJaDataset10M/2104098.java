package client;

import client.model.Contact;
import client.model.Message;

public interface IMessageViewListener {

    void onSignIn(String login, String password);

    void onSignOut();

    void onSendMessage(Contact toContact, Message message);
}

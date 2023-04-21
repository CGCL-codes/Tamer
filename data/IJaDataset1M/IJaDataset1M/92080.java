package aburakc.client;

import aburakc.shared.db.Message;
import aburakc.shared.db.Person;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SMSServiceAsync {

    void sendSMS(Person person, String message, AsyncCallback<Void> callback);

    void sendSMS(Message message, AsyncCallback<Void> callback);
}

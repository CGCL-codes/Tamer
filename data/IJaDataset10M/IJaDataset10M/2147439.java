package budgeteventplanner.client;

import java.security.NoSuchAlgorithmException;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserServiceAsync {

    void register(String email, String password, Integer role, AsyncCallback<Void> callback) throws NoSuchAlgorithmException;

    void login(String email, String password, AsyncCallback<Integer> callback) throws NoSuchAlgorithmException;

    void attendeeLogin(String registrationCode, AsyncCallback<Integer> callback);
}

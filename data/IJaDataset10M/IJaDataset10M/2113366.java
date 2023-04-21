package audictiv.client.services.register;

import audictiv.shared.RegistrationInfo;
import audictiv.shared.RegistrationStatus;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RegisterServiceAsync {

    void register(RegistrationInfo loginInfo, AsyncCallback<RegistrationStatus> callback);
}

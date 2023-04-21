package com.google.solarchallenge.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.solarchallenge.shared.dtos.UserAccountDto;
import java.util.ArrayList;

/**
 * Async Interface for GWT RPC {@link AdminService}
 *
 * @author Arjun Satyapal
 */
public interface AdminServiceAsync {

    void getAllUserAccountDtos(AsyncCallback<ArrayList<UserAccountDto>> callback);

    void updateUserAccountDto(UserAccountDto dto, AsyncCallback<Void> callback);
}

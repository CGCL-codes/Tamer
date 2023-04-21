package com.simconomy.cas.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.simconomy.cas.client.model.User;

public interface UserServiceAsync {

    public void getUser(String authenticationToken, AsyncCallback<User> callback);

    public void login(String userName, String password, AsyncCallback<String> callback);

    public void userStillHere(String securityToken, AsyncCallback<Long> callback);

    public void logout(String securityToken, AsyncCallback<Void> callback);

    public void createUser(User userDTO, String baseURL, Country country, Language language, AsyncCallback<String> callback);

    public void activateUser(String activationToken, AsyncCallback<String> callback);

    public void passwordForgotten(String email, String baseURL, Country country, Language language, AsyncCallback<Boolean> callback);

    public void setPassword(String token, String password, AsyncCallback<Boolean> callback);

    public void isActivated(String email, AsyncCallback<Boolean> callback);
}

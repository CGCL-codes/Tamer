package de.lema.server.api.login;

public interface LoginModule {

    LoginStatus login(String username, String password) throws Exception;
}

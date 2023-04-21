package com.mercury.training.java.mims.service;

public interface AuthenticationService {

    public boolean Authenticate(String username, String password);

    public String AccessLevel();
}

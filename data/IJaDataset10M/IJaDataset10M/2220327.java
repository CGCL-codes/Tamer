package de.mogwai.common.business.service;

import org.springframework.security.concurrent.SessionIdentifierAware;

public interface LoginService extends Service {

    void login(String aUsername, String aPassword, SessionIdentifierAware aSessionIdentifier) throws LoginException;
}

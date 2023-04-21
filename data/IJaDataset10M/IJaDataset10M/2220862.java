package com.googlecode.webduff.authentication;

import com.googlecode.webduff.Configurable;
import com.googlecode.webduff.authentication.provider.Credential;
import com.googlecode.webduff.exceptions.MethodResponseError;

public interface WebdavAuthentication extends Configurable {

    public void authenticate(Credential credential) throws MethodResponseError;
}

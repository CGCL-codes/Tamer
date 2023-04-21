package org.josso.auth.scheme;

import org.josso.auth.Credential;
import org.josso.auth.BaseCredential;

/**
 * Created by IntelliJ IDEA.
 * User: sgonzalez
 * Date: Nov 10, 2008
 * Time: 2:55:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class RememberMeCredential extends BaseCredential {

    private String token;

    public RememberMeCredential(Object token) {
        super(token);
    }

    public RememberMeCredential() {
        super();
    }
}

package org.fb4j.client.helper;

import org.fb4j.client.AuthenticationToken;
import org.fb4j.client.Client;
import org.fb4j.client.ClientException;
import org.fb4j.client.Session;
import org.fb4j.client.method.CreateTokenCall;
import org.fb4j.client.method.GetSessionCall;

public class AuthenticationHelper {

    private Client client;

    public AuthenticationHelper(Client client) {
        this.client = client;
    }

    public AuthenticationToken createToken() throws ClientException {
        return client.call(new CreateTokenCall());
    }

    public Session getSession(AuthenticationToken token) throws ClientException {
        GetSessionCall call = new GetSessionCall(token);
        Session session = client.call(call);
        client.setSession(session);
        return session;
    }
}

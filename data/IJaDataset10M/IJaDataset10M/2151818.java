package org.restlet.example.book.rest.ch6;

import org.restlet.Client;
import org.restlet.data.Form;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;

/**
 * Sample map client to create a user account
 * 
 * @author Jerome Louvel (contact@noelios.com)
 */
public class Example6_1 {

    public void makeUser(String user, String password) {
        Form input = new Form();
        input.add("password", password);
        String uri = "https://maps.example.com/user/" + Reference.encode(user);
        new Client(Protocol.HTTP).put(uri, input.getWebRepresentation());
    }
}

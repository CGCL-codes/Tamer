package Pump;

import java.net.*;

public class HttpRequest implements IfHttpRequest {

    private StringBuffer myContent;

    private URL myTarget;

    public HttpRequest(URL u) {
        myContent = new StringBuffer();
        try {
            myTarget = new URL(u.toString());
        } catch (MalformedURLException e) {
            ;
        }
    }

    public StringBuffer flushMessage() {
        return myContent;
    }

    public void sendHttpRequest(HttpQueue where) {
    }

    public URL showUrl() {
        return this.myTarget;
    }
}

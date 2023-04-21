package ants.p2p.http;

import ants.p2p.Message;
import ants.p2p.*;
import javax.crypto.*;

public class HttpResponsePartMessage extends Message implements Comparable {

    Object body;

    Object httpRequest;

    Object id;

    public HttpResponsePartMessage(byte[] body, Message httpRequest, Integer id) {
        this.body = body;
        this.httpRequest = httpRequest;
        this.id = id;
    }

    public byte[] getBody() {
        if (this.body instanceof byte[]) return (byte[]) this.body; else return null;
    }

    public Integer getId() {
        if (this.id instanceof Integer) return (Integer) this.id; else return null;
    }

    public Message getRequest() {
        if (this.httpRequest instanceof Message) return (Message) this.httpRequest; else return null;
    }

    public int compareTo(Object o) {
        if (o instanceof HttpResponsePartMessage) {
            HttpResponsePartMessage rm = (HttpResponsePartMessage) o;
            return this.getId().compareTo(rm.getId());
        } else return 0;
    }

    public void encrypt(Cipher c) throws Exception {
        this.body = new SealedObject((byte[]) this.body, c);
        this.httpRequest = new SealedObject((Message) this.httpRequest, c);
        this.id = new SealedObject((Integer) this.id, c);
    }

    public void decrypt(Cipher c) throws Exception {
        this.body = (byte[]) ((SealedObject) this.body).getObject(c);
        this.httpRequest = (Message) ((SealedObject) this.httpRequest).getObject(c);
        this.id = (Integer) ((SealedObject) this.id).getObject(c);
    }
}

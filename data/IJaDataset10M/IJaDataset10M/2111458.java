package org.helper;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class ReceivedMessage {

    @Persistent
    private String realname;

    @Persistent
    private String pseudonym;

    @Persistent
    private String service;

    @Persistent
    private String time;

    @Persistent
    private String message;

    @Persistent
    private String tokenInformation;

    @Persistent
    private Date receivedOn;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    public ReceivedMessage(String pseudonym, String realname, String service, String time, String message, String tokenInformation) {
        this.pseudonym = pseudonym;
        this.realname = realname;
        this.service = service;
        this.time = time;
        this.message = message;
        this.tokenInformation = tokenInformation;
        this.receivedOn = new Date();
    }

    public String getRealname() {
        return realname;
    }

    public String getPseudonym() {
        return pseudonym;
    }

    public String getService() {
        return service;
    }

    public String getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    public String getTokenInformation() {
        return tokenInformation;
    }

    public Date getReceivedOn() {
        return receivedOn;
    }

    public org.test.id.verifier.shared.ReceivedMessage transferable() {
        return new org.test.id.verifier.shared.ReceivedMessage(realname, pseudonym, service, time, message, tokenInformation);
    }
}

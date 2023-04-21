package audictiv.server.model;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public abstract class Account implements Serializable {

    private int ID;

    private String login;

    private String email;

    private Date subscriptionDate;

    protected char type;

    public Account() {
    }

    public Account(int id, String l, String e, Date sd) {
        ID = id;
        login = l;
        email = e;
        subscriptionDate = sd;
    }

    public int getID() {
        return ID;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public Date getSubscriptionDate() {
        return subscriptionDate;
    }

    public char getType() {
        return type;
    }
}

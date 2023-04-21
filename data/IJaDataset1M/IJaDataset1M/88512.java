package org.jaytter.model.tweet;

import java.util.Date;
import org.jaytter.model.user.TwitterAccount;

/**
 * This is a abstract class for Tweet instance on Jaytter
 * @author joao-neto
 */
public abstract class Tweet {

    protected Date date;

    protected TwitterAccount from;

    protected String message;

    protected String fromApp;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public TwitterAccount getFrom() {
        return from;
    }

    public void setFrom(TwitterAccount from) {
        this.from = from;
    }

    public String getFromApp() {
        return fromApp;
    }

    public void setFromApp(String fromApp) {
        this.fromApp = fromApp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Tweet{" + "date=" + date + ", from=" + from + ", message=" + message + ", fromApp=" + fromApp + '}';
    }
}

package twilio.markup;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Sms")
public class Sms extends Verb {

    private String message;

    @XStreamAsAttribute
    private String from;

    @XStreamAsAttribute
    private String to;

    @XStreamAsAttribute
    private String action;

    @XStreamAsAttribute
    private String statusCallback;

    @XStreamAsAttribute
    private String httpMethod = Constants.DEFAULT_HTTP_METHOD;

    public Sms() {
        super();
    }

    public Sms(String msg) {
        this.setMessage(msg);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getStatusCallback() {
        return statusCallback;
    }

    public void setStatusCallback(String statusCallback) {
        this.statusCallback = statusCallback;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }
}

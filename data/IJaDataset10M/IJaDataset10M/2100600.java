package it.hakvoort.neuroclient.reply;

/**
 * 
 * @author Gido Hakvoort (gido@hakvoort.it)
 * 
 */
public class DefaultReply implements Reply {

    public ResponseCode response;

    public DefaultReply() {
    }

    public DefaultReply(ResponseCode response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return String.format("response: %s", response);
    }

    @Override
    public ResponseCode getResponseCode() {
        return response;
    }

    @Override
    public void setResponseCode(ResponseCode response) {
        this.response = response;
    }
}

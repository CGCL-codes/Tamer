package api.error;

public class UnknownResponseException extends Exception {

    public UnknownResponseException(String response) {
        super("Unknown Response Received: " + response);
    }
}

package utils.sender.response;

public class SenderResponse {

    public enum Result {

        SUCCESS, FAIL
    }

    ;

    private String message;

    private Result result;

    public SenderResponse() {
    }

    public SenderResponse(String message, Result result) {
        this.message = message;
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return result + ": " + message;
    }
}

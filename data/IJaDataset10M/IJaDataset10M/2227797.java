package backend.event.type;

public class GeneralError extends EventType {

    public GeneralError(String message) {
        super(message);
        super.desc = "An non-specified error occurred, see error message.";
    }
}

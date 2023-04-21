package javax.naming;

public class TimeLimitExceededException extends LimitExceededException {

    public TimeLimitExceededException() {
        super();
    }

    public TimeLimitExceededException(String msg) {
        super(msg);
    }
}

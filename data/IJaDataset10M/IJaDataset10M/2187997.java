package gnu.javax.rmi.CORBA;

public class GetDelegateInstanceException extends Exception {

    private Throwable next;

    public GetDelegateInstanceException(String msg) {
        super(msg);
    }

    public GetDelegateInstanceException(String msg, Throwable next) {
        super(msg, next);
    }
}

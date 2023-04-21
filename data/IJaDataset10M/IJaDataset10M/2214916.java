package uncertain.proc;

public interface IExceptionHandle {

    public static final int EXCEPTION_HANDLED = 1;

    public static final int EXCEPTION_GO_ON = 0;

    public boolean handleException(ProcedureRunner runner, Throwable exception);
}

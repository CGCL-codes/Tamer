package sol.admin.systemmanagement.base;

/**
 * This class was introduced, since an input might be ok,
 * but there should be an extra message comoing with it.
 * This is the best solution, and removes the distinction
 * between boolean and String Checker
 * @author Markus Hammori
 */
public class CheckResult {

    protected boolean _checkOk;

    protected String _message;

    public CheckResult(boolean checkOk, String message) {
        _checkOk = checkOk;
        _message = message;
    }

    public void setCheckOk(boolean checkOk) {
        _checkOk = checkOk;
    }

    public void setMessage(String message) {
        _message = message;
    }

    public boolean getCheckOk() {
        return _checkOk;
    }

    public String getMessage() {
        return _message;
    }
}

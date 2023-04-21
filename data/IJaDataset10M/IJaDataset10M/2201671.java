package gnu.classpath.jdwp.util;

/**
 * A class to wrap around values returned from a Method call in the VM.
 * 
 * @author Aaron Luchko <aluchko@redhat.com>
 */
public class MethodResult {

    private Object returnedValue;

    private Exception thrownException;

    public Object getReturnedValue() {
        return returnedValue;
    }

    public void setReturnedValue(Object returnedValue) {
        this.returnedValue = returnedValue;
    }

    public Exception getThrownException() {
        return thrownException;
    }

    public void setThrownException(Exception thrownException) {
        this.thrownException = thrownException;
    }
}

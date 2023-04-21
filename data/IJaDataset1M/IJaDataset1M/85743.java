package palus.trace;

/**
 * Represents the exit event of a static init block
 * 
 * @author saizhang@google.com (Sai Zhang)
 */
public class ClinitExitEvent extends TraceEvent {

    public ClinitExitEvent(int id, String className, String methodName, String methodDesc, Object thiz, Object[] params) {
        super(id, className, methodName, methodDesc, thiz, params);
    }

    @Override
    public boolean isEntryEvent() {
        return false;
    }

    @Override
    public String toString() {
        return "<Clinit> Exit:" + super.getMethodName() + ":" + super.getMethodDesc() + ":" + super.getId();
    }

    @Override
    public boolean isNonPublicMethod() {
        throw new RuntimeException("You can not tell Clinit<> is public or not");
    }

    @Override
    public boolean isStaticMethod() {
        return false;
    }
}

package hub.metrik.lang.eprovide;

import org.eclipse.core.expressions.PropertyTester;

public class ResetTester extends PropertyTester {

    @Override
    public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
        if (receiver instanceof IReset) {
            IReset myThread = (IReset) receiver;
            return myThread.canReset();
        }
        return false;
    }
}

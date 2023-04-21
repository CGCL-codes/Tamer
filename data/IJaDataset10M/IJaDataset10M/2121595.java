package tigerunit.util;

import java.lang.reflect.Method;

public interface Executor {

    void execute(Method method, Object target) throws Throwable;

    void reset();
}

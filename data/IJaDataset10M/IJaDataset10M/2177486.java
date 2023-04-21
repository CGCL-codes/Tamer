package uncertain.util.reflect;

import java.lang.reflect.Method;

/**
 *
 * @author  Administrator
 * @version 
 */
public interface MethodFilter {

    public boolean accepts(Class owner, Method m);
}

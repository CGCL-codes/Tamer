package test.runtime;

import net.sf.joyaop.AroundAdvice;
import java.io.Serializable;

/**
 * @author Shen Li
 */
public abstract class FooAroundAdvice implements AroundAdvice, Serializable {

    public Object around() throws Throwable {
        return "FooAroundAdvice.around()" + getParameter("arrow") + proceed();
    }
}

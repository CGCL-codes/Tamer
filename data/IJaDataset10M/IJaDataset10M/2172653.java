package com.gorillalogic.gython.glob;

import java.util.Map;
import com.gorillalogic.glob.GLException;
import com.gorillalogic.gython.glob.impl.GyFactory;

/**
 * Gy provides for evaluating GCL expressions from within Gython scripts and methods.
 * 
 * @author Stu
 * 
 */
public interface Gy extends Map {

    /**
	 * Retutrns the result of evaluating a gcl expression
	 * 
	 * @param gcl  the gcl expression to be evaluated
	 * 
	 * @return a {@link GLObject}, {@link GLObjectList}, or <code>String</code> representation of the expression result
	 *      
	 * @throws GLException if an error occurs while evaluating the expression
	 * 
	 */
    public Object eval(String gcl) throws GLException;

    /**
	 * Returns an object resulting from evaulating gcl expression.
	 * This provides for evaluating gcl using array notation,
	 * e.g., _gy["/Order"]
	 */
    public Object get(Object key);

    /**
	 * Provides for setting the current gosh context using a bean setter
	 * e.g., _gy.current = "/Order"
	 */
    public Object put(Object key, Object value);

    public static final GyFactory factory = new GyFactory();
}

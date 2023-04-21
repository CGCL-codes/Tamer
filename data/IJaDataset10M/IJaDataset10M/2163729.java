package org.powermock.api.mockito.verification;

import org.mockito.Mockito;
import java.lang.reflect.Method;

public interface PrivateMethodVerification {

    /**
	 * Verify calls to private methods without having to specify the method
	 * name. The method will be looked up using the parameter types (if
	 * possible).
	 * 
	 * @throws Exception
	 *             If something unexpected goes wrong.
	 */
    public void invoke(Object... arguments) throws Exception;

    /**
	 * Verify calls to the specific method.
	 * 
	 * @throws Exception
	 *             If something unexpected goes wrong.
	 */
    public WithOrWithoutVerifiedArguments invoke(Method method) throws Exception;

    /**
	 * Verify a private method call by specifying the method name of the method
	 * to verify.
	 * 
	 * @see {@link Mockito#invoke(Object)}
	 * @throws Exception
	 *             If something unexpected goes wrong.
	 */
    public void invoke(String methodToVerify, Object... arguments) throws Exception;
}

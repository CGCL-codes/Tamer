package net.sf.staccatocommons.lang.provider;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.util.concurrent.Callable;
import net.sf.staccatocommons.lang.SoftException;
import net.sf.staccatocommons.lang.provider.internal.CallableProvider;
import org.junit.Test;

/**
 * @author flbulgarelli
 * 
 */
public class CallableProviderUnitTest {

    /**
	 * Test method for
	 * {@link net.sf.staccatocommons.lang.provider.internal.CallableProvider#value()} when
	 * callable throws an exception.
	 */
    @Test(expected = SoftException.class)
    public void testValue_Exception() {
        new CallableProvider<String>(new Callable<String>() {

            @Override
            public String call() throws Exception {
                throw new IOException();
            }
        }).value();
    }

    /**
	 * Test method for
	 * {@link net.sf.staccatocommons.lang.provider.internal.CallableProvider#value()} when
	 * call succeeds.
	 */
    @Test
    public void testValue_OK() {
        assertEquals("Hello", new CallableProvider<String>(new Callable<String>() {

            @Override
            public String call() throws Exception {
                return "Hello";
            }
        }).value());
    }
}

package com.google.code.ssm.aop.counter;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import java.util.Arrays;
import java.util.Collection;
import org.hamcrest.CoreMatchers;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;
import com.google.code.ssm.api.ParameterValueKeyProvider;
import com.google.code.ssm.api.counter.IncrementCounterInCache;

/**
 * 
 * @author Jakub Białek
 * 
 */
public class IncrementCounterInCacheAdviceTest extends AbstractCounterTest<IncrementCounterInCacheAdvice> {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { { true, "incrCounter1", new Class[] { int.class }, new Object[] { 1 } }, { true, "incrCounter2", new Class[] { int.class, int.class }, new Object[] { 2, 5 } }, { false, "incrCounter20", new Class[] {}, new Object[] {} } });
    }

    public IncrementCounterInCacheAdviceTest(final boolean isValid, final String methodName, final Class<?>[] paramTypes, final Object[] params) {
        super(isValid, methodName, paramTypes, params, null);
    }

    @Before
    public void setUp() {
        super.setUp(new TestService());
    }

    @Test
    public void validIncrementCounterInCache() throws Throwable {
        Assume.assumeTrue(isValid);
        advice.incrementSingle(pjp);
        verify(cache).incr(cacheKey, 1, 1L);
    }

    @Test
    public void invalidIncrementCounterInCache() throws Throwable {
        Assume.assumeThat(isValid, CoreMatchers.is(false));
        advice.incrementSingle(pjp);
        verify(cache, never()).incr(cacheKey, 1, 1L);
    }

    @Override
    protected IncrementCounterInCacheAdvice createAdvice() {
        return new IncrementCounterInCacheAdvice();
    }

    @SuppressWarnings("unused")
    private static class TestService {

        @IncrementCounterInCache(namespace = NS)
        public void incrCounter1(@ParameterValueKeyProvider final int id1) {
        }

        @IncrementCounterInCache(namespace = NS)
        public int incrCounter2(@ParameterValueKeyProvider(order = 2) final int id1, @ParameterValueKeyProvider(order = 5) final int id2) {
            return 1;
        }

        @IncrementCounterInCache(namespace = NS)
        public void incrCounter20() {
        }
    }
}

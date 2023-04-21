package kite.test;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.lang.reflect.Method;
import java.sql.SQLException;
import kite.circuitbreaker.CircuitBreakerCallback;
import kite.circuitbreaker.CircuitBreakerTemplate;
import kite.throttle.ThrottleCallback;
import org.aopalliance.intercept.MethodInvocation;

;

/**
 * <p>
 * Object mother for testing Kite.
 * </p>
 * 
 * @version $Id: KiteObjectMother.java 64 2010-03-25 06:11:04Z willie.wheeler $
 * @author Willie Wheeler
 * @since 1.0
 */
public final class KiteObjectMother {

    private static KiteObjectMother mom = new KiteObjectMother();

    private CircuitBreakerTemplate breaker;

    private CircuitBreakerCallback<String> goodBreakerAction;

    private CircuitBreakerCallback<String> badBreakerAction;

    private CircuitBreakerCallback<String> breakerActionThatAlwaysThrowsSqlException;

    private ThrottleCallback<String> throttleAction;

    private ThrottleCallback<String> slowThrottleAction;

    private Method dummyMethod;

    private MethodInvocation invocation;

    private Exception serviceException = new RuntimeException("fail");

    public static KiteObjectMother instance() {
        return mom;
    }

    private KiteObjectMother() {
        try {
            initGenericStuff();
            initCircuitBreakerStuff();
            initThrottleStuff();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initGenericStuff() {
        this.dummyMethod = String.class.getMethods()[0];
        this.invocation = mock(MethodInvocation.class);
        when(invocation.getMethod()).thenReturn(dummyMethod);
        when(invocation.getThis()).thenReturn("dummyObject");
        try {
            when(invocation.proceed()).thenReturn("Winterlong");
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @SuppressWarnings("unchecked")
    private void initCircuitBreakerStuff() throws Exception {
        this.breaker = mock(CircuitBreakerTemplate.class);
        when(breaker.execute(isA(CircuitBreakerCallback.class))).thenReturn("Planet of Sound");
        this.goodBreakerAction = new CircuitBreakerCallback<String>() {

            public String doInCircuitBreaker() throws Exception {
                return "good";
            }
        };
        this.badBreakerAction = new CircuitBreakerCallback<String>() {

            public String doInCircuitBreaker() throws Exception {
                throw serviceException;
            }
        };
        this.breakerActionThatAlwaysThrowsSqlException = new CircuitBreakerCallback<String>() {

            public String doInCircuitBreaker() throws Exception {
                throw new SQLException();
            }
        };
    }

    private void initThrottleStuff() {
        this.throttleAction = new ThrottleCallback<String>() {

            public String doInThrottle() throws Exception {
                return "good";
            }
        };
        this.slowThrottleAction = new ThrottleCallback<String>() {

            public String doInThrottle() throws Exception {
                Thread.sleep(1000L);
                return "good";
            }
        };
    }

    public Method getDummyMethod() {
        return dummyMethod;
    }

    /**
	 * <p>
	 * Returns a method invocation that always returns the string "Winterlong"
	 * when calling <code>proceed()</code> with any argument.
	 * </p>
	 * 
	 * @return method invocation that returns the string "Winterlong" when
	 *         calling <code>proceed()</code>
	 */
    public MethodInvocation getMethodInvocation() {
        return invocation;
    }

    /**
	 * <p>
	 * Returns a circuit breaker that always returns the string "Planet of
	 * Sound" when calling <code>execute()</code> with any argument.
	 * </p>
	 * 
	 * @return method invocation that returns the string "Planet of Sound" when
	 *         calling <code>execute()</code>
	 */
    public CircuitBreakerTemplate getCircuitBreaker() {
        return breaker;
    }

    public CircuitBreakerCallback<String> getGoodCircuitBreakerAction() {
        return goodBreakerAction;
    }

    public CircuitBreakerCallback<String> getBadCircuitBreakerAction() {
        return badBreakerAction;
    }

    public CircuitBreakerCallback<String> getCircuitBreakerActionThatAlwaysThrowsSqlException() {
        return breakerActionThatAlwaysThrowsSqlException;
    }

    public ThrottleCallback<String> getThrottleAction() {
        return throttleAction;
    }

    public ThrottleCallback<String> getSlowThrottleAction() {
        return slowThrottleAction;
    }
}

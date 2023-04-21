package org.curjent.test.agent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.concurrent.atomic.AtomicReference;
import org.curjent.agent.Agent;
import org.curjent.agent.AgentCall;
import org.curjent.agent.CallSite;
import org.curjent.agent.CallState;
import org.curjent.agent.CallStateListener;
import org.curjent.agent.ExceptionHandler;
import org.curjent.agent.Reentrant;
import org.junit.Test;

/**
 * Tests for {@link CallStateListener}.
 * 
 * @see ConfigTest#testListenerConfig()
 * @see AgentCallTest
 * @see FutureTest
 */
public class ListenerTest extends AgentTest {

    interface ListenerTestAgent extends TestAgent {

        void m1();

        double m2(double n);

        long m3(long n);
    }

    static class ListenerTestTask extends TestTask {

        void m1() {
        }

        double m2(double n) {
            return n + 1;
        }

        @Reentrant
        long m3(long n) {
            return n + 1;
        }
    }

    ListenerTestAgent agent;

    CallSite m1, m2, m3;

    @Override
    protected TestAgent newTestAgent() {
        agent = Agent.newInstance(ListenerTestAgent.class, new ListenerTestTask());
        m1 = Agent.getConfig(agent).getCallSite("m1", void.class);
        m2 = Agent.getConfig(agent).getCallSite("m2", double.class, double.class);
        m3 = Agent.getConfig(agent).getCallSite("m3", long.class, long.class);
        return agent;
    }

    @Test
    public void testState() {
        final AtomicReference<CallState> current = new AtomicReference<CallState>();
        m1.setCallStateListener(new CallStateListener<Void>() {

            @Override
            public void callStateChanged(AgentCall<Void> call, CallState previous) throws Throwable {
                CallState state = call.getState();
                assertSame(current.get(), previous);
                switch(state) {
                    case STARTING:
                        assertNull(current.get());
                        break;
                    case ACCEPTED:
                        assertSame(CallState.STARTING, current.get());
                        break;
                    case EXECUTING:
                        assertSame(CallState.ACCEPTED, current.get());
                        break;
                    case FINISHED:
                        assertSame(CallState.EXECUTING, current.get());
                        break;
                    default:
                        fail();
                        break;
                }
                current.set(state);
            }
        });
        agent.m1();
    }

    @Test
    public void testFinishException() {
        m1.setCallStateListener(CallState.STARTING, new CallStateListener<Void>() {

            @Override
            public void callStateChanged(AgentCall<Void> call, CallState previous) throws Throwable {
                assertSame(CallState.STARTING, call.getState());
                call.finish();
                call.setException(new TestException());
            }
        });
        try {
            agent.m1();
            fail();
        } catch (TestException e) {
        }
    }

    @Test
    public void testFinishResult() {
        m2.setCallStateListener(CallState.STARTING, new CallStateListener<Double>() {

            @Override
            public void callStateChanged(AgentCall<Double> call, CallState previous) throws Throwable {
                call.finish();
                call.setResult((Double) call.getArgument(0) + 2);
            }
        });
        assertEquals((Double) 3.0, (Double) agent.m2(1));
    }

    @Test
    public void testListenerException() {
        m1.setCallStateListener(CallState.STARTING, new CallStateListener<Void>() {

            @Override
            public void callStateChanged(AgentCall<Void> call, CallState previous) throws Throwable {
                throw new TestException();
            }
        });
        final AtomicReference<Throwable> thrown = new AtomicReference<Throwable>();
        config.setUnhandledExceptionHandler(new ExceptionHandler() {

            @Override
            public void handle(Throwable exception) throws Throwable {
                thrown.set(exception);
            }
        });
        agent.m1();
        assertSame(TestException.class, thrown.get().getClass());
    }

    @Test
    public void testAwait() {
        m1.setCallStateListener(CallState.STARTING, new CallStateListener<Void>() {

            @Override
            public void callStateChanged(AgentCall<Void> call, CallState previous) throws Throwable {
                call.await(CallState.FINISHED);
            }
        });
        final AtomicReference<Throwable> thrown = new AtomicReference<Throwable>();
        config.setUnhandledExceptionHandler(new ExceptionHandler() {

            @Override
            public void handle(Throwable exception) throws Throwable {
                thrown.set(exception);
            }
        });
        agent.m1();
        assertSame(IllegalStateException.class, thrown.get().getClass());
    }

    @Test
    public void testReentry() {
        m1.setCallStateListener(CallState.EXECUTING, new CallStateListener<Void>() {

            @Override
            public void callStateChanged(AgentCall<Void> call, CallState previous) throws Throwable {
                assertEquals(7, agent.m3(6));
            }
        });
        m3.setCallStateListener(CallState.STARTING, new CallStateListener<Void>() {

            @Override
            public void callStateChanged(AgentCall<Void> call, CallState previous) throws Throwable {
                assertTrue(call.isReentry());
            }
        });
        agent.m1();
    }
}

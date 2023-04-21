package com.reserveamerica.elastica.test.ejb;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import com.reserveamerica.elastica.cluster.GlobalContext;
import com.reserveamerica.elastica.cluster.config.ConfigProperties;
import com.reserveamerica.elastica.server.Server;
import com.reserveamerica.elastica.server.ServerState;
import com.reserveamerica.elastica.test.ejb.cluster.factoryimpl.availability.TestAvailabilityPolicy;

/**
 * @ejb.bean name="TestSession"
 *           display-name="Name for TestSession"
 *           description="Description for TestSession"
 *           jndi-name="ejb/TestSession"
 *           type="Stateless"
 *           view-type="both"
 */
public class TestSessionBean implements SessionBean {

    private static final long serialVersionUID = -5714729681963069137L;

    private static final Random random = new Random(System.currentTimeMillis());

    private static final Map<String, String> cache = new ConcurrentHashMap<String, String>();

    private static final Object lock = new Object();

    private static final int DEFAULT_EXCEPTION_RATE = 20;

    private static int exceptionRate = DEFAULT_EXCEPTION_RATE;

    private static Map<String, Integer> delays = new ConcurrentHashMap<String, Integer>();

    private static volatile int defaultDelay = 0;

    private static volatile long startTime;

    private static volatile int numCalls;

    private static volatile int numExceptions;

    private static volatile int numExceptionsAtLastCheck;

    private static volatile int spinCount;

    static {
        startTime = System.currentTimeMillis();
        exceptionRate = ConfigProperties.getInstance().getProperty("com.reserveamerica.exception.rate", DEFAULT_EXCEPTION_RATE);
        System.out.println("TestSessionBean - Setting exception rate to " + exceptionRate);
    }

    public TestSessionBean() {
    }

    public void ejbCreate() {
    }

    public void ejbActivate() throws EJBException, RemoteException {
    }

    public void ejbPassivate() throws EJBException, RemoteException {
    }

    public void ejbRemove() throws EJBException, RemoteException {
    }

    public void setSessionContext(SessionContext ctx) throws EJBException, RemoteException {
    }

    public String execute(String method, String sessionId, Map<String, Object> properties) throws TestException {
        String data = null;
        if (sessionId != null) {
            data = cache.get(sessionId);
            if (data == null) {
                data = "Data for " + sessionId;
                cache.put(sessionId, data);
            }
        } else {
            System.out.println("Session ID is null.");
        }
        int randomSpinCount = 0;
        if (spinCount > 0) {
            randomSpinCount = random.nextInt(spinCount);
            for (int i = 0; i < randomSpinCount; i++) {
            }
        }
        int delay = getDelay(method);
        if (delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        long currentTime = System.currentTimeMillis();
        synchronized (lock) {
            numCalls++;
            if ((currentTime - startTime) >= 1000) {
                System.out.println("Num calls: " + numCalls);
                int exceptions = numExceptions;
                int numExceptionsForPeriod = exceptions - numExceptionsAtLastCheck;
                if (numExceptionsForPeriod > 0) {
                    GlobalContext.getInstance().setProperty(TestAvailabilityPolicy.EXCEPTION_COUNT, Integer.valueOf(numExceptionsForPeriod));
                }
                startTime = currentTime;
                numExceptionsAtLastCheck = exceptions;
            }
        }
        Long requestId = (Long) GlobalContext.getInstance().getProperty("some-changeable-value");
        GlobalContext.getInstance().setProperty("some-changeable-value", (requestId != null) ? new Long(requestId.longValue() + 1) : new Long(1));
        GlobalContext.getInstance().setProperty("some-added-value", random.nextLong());
        GlobalContext.getInstance().removeProperty("some-removable-value");
        return data;
    }

    public void clearCache() {
        System.out.println("clearCache[" + getName() + "] - Clearing [" + cache.size() + "] items in cache.");
        cache.clear();
    }

    public ServerState setServerState(ServerState state) {
        ServerState oldState = Server.getInstance().getState();
        Server.getInstance().setState(state);
        return oldState;
    }

    public String getName() {
        return "Test";
    }

    public int setDelay(String method, int maxDelayMs) {
        Integer prevValue = delays.put(method, new Integer(maxDelayMs));
        return (prevValue != null) ? prevValue.intValue() : defaultDelay;
    }

    public int getDelay(String method) {
        Integer maxDelay = delays.get(method);
        return (maxDelay != null) ? maxDelay.intValue() : defaultDelay;
    }

    public Map<String, Integer> getDelays() {
        return delays;
    }

    public int setDefaultDelay(int delay) {
        int prevValue = defaultDelay;
        defaultDelay = delay;
        return prevValue;
    }

    public int getDefaultDelay() {
        return defaultDelay;
    }

    public void clearDelays() {
        delays.clear();
    }

    public int setSpinCount(int count) {
        int prevValue = spinCount;
        spinCount = count;
        return prevValue;
    }

    public int getSpinCount() {
        return spinCount;
    }
}

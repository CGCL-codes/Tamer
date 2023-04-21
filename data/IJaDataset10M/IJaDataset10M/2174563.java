package ch.unibas.germa.debug;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.apache.log4j.Logger;

public class InvocationLogger implements java.lang.reflect.InvocationHandler {

    private static Logger logger = Logger.getLogger(InvocationLogger.class);

    private ArrayList<InvocationLogEntry> log;

    private Object obj;

    public static Object newInstance(Object obj) {
        return java.lang.reflect.Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), new InvocationLogger(obj));
    }

    private InvocationLogger(Object obj) {
        this.obj = obj;
    }

    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        Object result;
        try {
            logger.debug("Invocation of " + m.getName() + " with args " + args);
            InvocationLogEntry entry = new InvocationLogEntry();
            entry.o = proxy;
            entry.m = m;
            entry.args = args;
            log.add(entry);
            result = m.invoke(obj, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (Exception e) {
            throw new RuntimeException("unexpected invocation exception: " + e.getMessage());
        } finally {
            logger.debug(m.getName() + " returned!");
        }
        return result;
    }

    public ArrayList<InvocationLogEntry> getLog() {
        return log;
    }

    public void setLog(ArrayList<InvocationLogEntry> log) {
        this.log = log;
    }
}

class InvocationLogEntry {

    Object o;

    Method m;

    Object[] args;
}

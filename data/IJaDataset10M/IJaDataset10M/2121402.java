package com.cbsgmbh.xi.af.trace.helpers;

import java.util.Enumeration;

public interface Tracer {

    public void leaving();

    public void leaving(Object[] values);

    public void debug(String message);

    public void debug(String message, Object singleParameter);

    public void debug(String message, Object[] multiParameters);

    public void debug(String message, Enumeration enumeration);

    public void debug(String message, boolean bool);

    public void debug(String message, TracerDeferredEvaluator evaluator);

    public void info(String message);

    public void info(String message, Object singleParameter);

    public void info(String message, Object[] multiParameters);

    public void info(String message, Enumeration enumeration);

    public void info(String message, boolean bool);

    public void warn(String message);

    public void warn(String message, Object singleParameter);

    public void warn(String message, Object[] multiParameters);

    public void warn(String message, Enumeration enumeration);

    public void warn(String message, boolean bool);

    public void error(String message);

    public void error(String message, Object singleParameter);

    public void error(String message, Object[] multiParameters);

    public void error(String message, Enumeration enumeration);

    public void error(String message, boolean bool);

    public void fatal(String message);

    public void fatal(String message, Object singleParameter);

    public void fatal(String message, Object[] multiParameters);

    public void fatal(String message, Enumeration enumeration);

    public void fatal(String message, boolean bool);

    public void throwing(Throwable t);

    public void catched(Throwable t);

    void setBackend(TracerBackend backend, String signature);
}

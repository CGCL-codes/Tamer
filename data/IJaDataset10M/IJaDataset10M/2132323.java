package com.teliose.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

/**
 * 
 * @author Prabath Ariyarathna
 */
public class LoggingInterceptor implements MethodInterceptor {

    private static final int MAX_VALUE_LENGTH = 100;

    private static Logger logger = Logger.getLogger(LoggingInterceptor.class);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long beforeTime = 0;
        long execTime = 0;
        Object retVal = null;
        String className = invocation.getThis().getClass().getSimpleName();
        String methodName = invocation.getMethod().getName();
        Object[] args = invocation.getArguments();
        logger.info(createStartMessage(className, methodName, args));
        try {
            beforeTime = System.currentTimeMillis();
            retVal = invocation.proceed();
            execTime = System.currentTimeMillis() - beforeTime;
        } catch (Exception ex) {
            logger.error(createErrorMessage(className, methodName, ex));
            throw ex;
        } finally {
            logger.info(createEndMessage(className, methodName, execTime, retVal));
        }
        return retVal;
    }

    /**
	 * @param class name
	 * @param method
	 *            name
	 * @param method
	 *            parameters
	 * @return log message
	 */
    private String createStartMessage(String className, String methodName, Object[] args) {
        StringBuilder sb = new StringBuilder();
        String separeter = ", ";
        sb.append(className);
        sb.append(" ");
        sb.append(methodName);
        sb.append("() ");
        sb.append("START ");
        for (Object arg : args) {
            sb.append(arg);
            sb.append(separeter);
        }
        if (sb.lastIndexOf(separeter) == sb.length() - separeter.length()) {
            sb.delete(sb.length() - separeter.length(), sb.length());
        }
        return sb.toString();
    }

    /**
	 * @param class name
	 * @param method
	 *            name
	 * @param execute
	 *            time
	 * @param return value
	 * @return log message
	 */
    private String createEndMessage(String className, String methodName, long execTime, Object retVal) {
        StringBuilder builder = new StringBuilder();
        builder.append(className);
        builder.append(" ");
        builder.append(methodName);
        builder.append("() ");
        builder.append("END ");
        builder.append(execTime);
        builder.append("(ms) ");
        if (retVal != null) {
            String ret = retVal.toString();
            if (ret.length() > MAX_VALUE_LENGTH) {
                ret.substring(0, MAX_VALUE_LENGTH);
            }
            builder.append(ret);
        }
        return builder.toString();
    }

    /**
	 * @param class name
	 * @param method
	 *            name
	 * @param method
	 *            parameters
	 * @return log message
	 */
    private String createErrorMessage(String className, String methodName, Exception ex) {
        StringBuilder builder = new StringBuilder();
        builder.append(className);
        builder.append(" ");
        builder.append(methodName);
        builder.append("() ");
        builder.append("ERROR ");
        builder.append(ex.getMessage());
        return builder.toString();
    }
}

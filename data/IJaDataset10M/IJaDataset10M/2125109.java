package org.localstorm.tools.aop.runtime;

/**
 * @author Alexey Kuznetsov
 */
public class CallLogger {

    private static final CallLogger ctl = new CallLogger();

    private static final String CALL_PROCESSOR_PROPERTY = "org.localstorm.tools.aop.call";

    private CallProcessor cp;

    private CallLogger() {
        try {
            String dcpClass = System.getProperty(CALL_PROCESSOR_PROPERTY);
            if (dcpClass != null) {
                Class c = Class.forName(dcpClass);
                Object o = c.newInstance();
                if (o instanceof DeadlineCallProcessor) {
                    this.cp = (CallProcessor) o;
                } else {
                    System.err.println("Specified DeadlineCallProcessor [" + dcpClass + "] is invalid!");
                    this.cp = new DefaultCp();
                }
            } else {
                System.err.println("DeadlineCallProcessor was not specified. Using default implementation...");
                this.cp = new DefaultCp();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("DeadlineCallProcessor wasn't initiallized. Using default implementation");
            this.cp = new DefaultCp();
        }
    }

    public static CallLogger getInstance() {
        return ctl;
    }

    public void logTime(String className, String methodName, long actualTime) {
        this.cp.logTime(className, methodName, actualTime);
    }

    public CallProcessor getProcessor() {
        return this.cp;
    }

    private static final class DefaultCp implements CallProcessor {

        @Override
        public synchronized void logTime(String className, String methodName, long actualTime) {
            System.out.println("Timing: " + className + "." + methodName + " (" + actualTime + " ms.)");
        }
    }
}

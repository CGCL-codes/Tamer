package de.vbrehm.talend;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Volker Brehm
 *
 */
public class TalendJobWrapper implements ITalendJob {

    private Object tosScriptClass;

    private Method tosScriptStartMethod;

    private TalendContext jobContext;

    public String getJobName() {
        return "Wrapper";
    }

    public String getJobVersion() {
        return "0.0";
    }

    public String getTosVersion() {
        return "2.4";
    }

    public TalendJobWrapper(Class talendJob) throws TalendJobAccessorException {
        try {
            this.tosScriptClass = talendJob.newInstance();
        } catch (InstantiationException e) {
            throw new TalendJobAccessorException("Could not instantiate class", e);
        } catch (IllegalAccessException e) {
            throw new TalendJobAccessorException("Could not access class", e);
        }
        try {
            String[] parameters = {};
            this.tosScriptStartMethod = this.tosScriptClass.getClass().getMethod("runJobInTOS", parameters.getClass());
        } catch (NoSuchMethodException e) {
            throw new TalendJobAccessorException("Method to start the script not found", e);
        } catch (SecurityException e) {
            throw new TalendJobAccessorException("Could not access method to start script", e);
        }
    }

    public void setContext(TalendContext jobContext) {
        this.jobContext = jobContext;
    }

    public Integer execute(TalendContext jobContext) throws TalendJobException {
        this.setContext(jobContext);
        return this.execute();
    }

    public Integer execute() throws TalendJobException {
        try {
            return (Integer) this.tosScriptStartMethod.invoke(null, new Object[] { this.jobContext.getARGV() });
        } catch (IllegalAccessException e) {
            throw new TalendJobException("Could not access start method on class", e);
        } catch (IllegalArgumentException e) {
            throw new TalendJobException("Problem passing arguments to tos script", e);
        } catch (InvocationTargetException e) {
            throw new TalendJobException("Error invocing talend job", e);
        }
    }
}

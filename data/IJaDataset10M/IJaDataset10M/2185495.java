package mcujavasource.transformer.userclass;

import org.w3c.dom.*;
import mcujavasource.transformer.instance.SourceInstance;

/**
 *
 */
public class AbstractInstanceUser {

    private InstanceContext context;

    /** 
   *
   */
    public AbstractInstanceUser() {
    }

    public AbstractInstanceUser(InstanceContext context) {
        this.context = context;
    }

    public InstanceContext getContext() {
        return context;
    }

    public void setContext(InstanceContext context) {
        this.context = context;
    }

    protected void invoke(SourceInstance si, String methodName, Object... args) {
        context.invoke(si, methodName, args);
    }

    protected SourceInstance getSourceInstance(SourceInstance si, String methodName, Object... args) {
        return context.getSourceInstance(si, methodName, args);
    }

    protected void include(String name) {
        context.include(name);
    }

    protected void callFunction(String functionName, Object... args) {
        context.callFunction(functionName, args);
    }
}

package com.director.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Simone Ricciardi
 * Date: 30-mag-2010
 * Time: 12.00.06
 */
public class RemotingProvider extends AbstractProvider {

    private Map<String, DirectAction> actions = new HashMap<String, DirectAction>();

    protected RemotingProvider(String id, String namespace, ProviderType type, DirectConfiguration configuration) {
        super(id, namespace, type, configuration);
    }

    @Override
    public void registerMethod(String actionName, String methodName, Class actionClass, Method method) {
        DirectAction directAction = this.actions.get(actionName);
        if (directAction == null) {
            directAction = new DirectAction(this.getNamespace(), actionName, actionClass);
            this.actions.put(actionName, directAction);
        }
        directAction.addMethod(methodName, method);
    }

    @Override
    public boolean hasMethod(Method method, Class aClass) {
        for (DirectAction directAction : this.actions.values()) {
            if (directAction.getActionClass().equals(aClass) && directAction.hasMethod(method)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void doProcess() throws Throwable {
        RequestHandler requestHandler = RequestHandler.getInstance();
        DirectTransaction[] transactions = requestHandler.parse();
        for (DirectTransaction transaction : transactions) {
            transaction.execute(this);
            DirectContext.get().pushEvent(transaction);
        }
    }

    @Override
    public String formatForOutput() {
        RequestHandler requestHandler = RequestHandler.getInstance();
        return requestHandler.format();
    }

    public DirectAction[] getActions() {
        return this.actions.values().toArray(new DirectAction[this.actions.size()]);
    }

    public boolean containsAction(String name) {
        return this.actions.containsKey(name);
    }

    public DirectAction getAction(String name) {
        return this.actions.get(name);
    }
}

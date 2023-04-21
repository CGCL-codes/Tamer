package com.mangobop.impl.resource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import com.mangobop.functions.State;
import com.mangobop.functions.StateMachine;
import com.mangobop.impl.functions.StateMachineImpl;

/**
 * @author mangobop
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class StateInvocationHandler implements InvocationHandler {

    private StateMachineImpl machine;

    /**
	 * 
	 */
    public StateInvocationHandler(State proxied) {
        machine = new StateMachineImpl(proxied);
    }

    public void setProxy(State proxy) {
        machine.setProxy(proxy);
    }

    public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
        try {
            if (method.getName().equals("equals")) {
                return new Boolean(proxy == params[0]);
            }
            Method sm = machine.getClass().getMethod(method.getName(), method.getParameterTypes());
            return sm.invoke(machine, params);
        } catch (NoSuchMethodException e) {
            Method sm = machine.getNu().getClass().getMethod(method.getName(), method.getParameterTypes());
            return sm.invoke(machine.getNu(), params);
        }
    }

    public StateMachine getMachine() {
        return machine;
    }
}

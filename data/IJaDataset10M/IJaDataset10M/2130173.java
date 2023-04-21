package com.angel.architecture.event.synchronization;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import com.angel.architecture.event.transaction.TransactionListener;

/**
 * El objetivo de un objeto <tt>TransactionSynchronizationInterceptor</tt> es garantizar
 * que existe un listener activado al momento de comenzar una transacci�n de forma de
 * poder observar su ciclo de vida. Esto es porque spring requiere de la registraci�n
 * program�tica de listeners (no se pueden configurar declarativamente)
 */
public class TransactionSynchronizationInterceptor implements MethodInterceptor {

    @Autowired
    private TransactionListener listener;

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        listener.initialize();
        return methodInvocation.proceed();
    }
}

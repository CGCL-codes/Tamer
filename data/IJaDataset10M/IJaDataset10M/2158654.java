package org.apache.myfaces.trinidadinternal.share.expl;

import java.lang.reflect.InvocationTargetException;

/**
 * Function is the abstraction for an EL Function
 * <p>
 * @version $Name:  $ ($Revision: adfrt/faces/adf-faces-impl/src/main/java/oracle/adfinternal/view/faces/share/expl/Function.java#0 $) $Date: 10-nov-2005.19:00:13 $
 */
public abstract class Function {

    public abstract Object invoke(Object instance, Object[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException;

    public abstract Class[] getParameterTypes();

    public abstract Class<?> getReturnType();

    Function() {
    }
}

package org.crud4j.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import org.crud4j.core.annotation.CrudBeforeCreate;
import org.crud4j.core.annotation.CrudBeforeRemove;
import org.crud4j.core.annotation.CrudBeforeUpdate;

/**
 * {@link CrudListener} that invokes the bean callback methods
 */
public class CallbackMethodsCrudListener implements CrudListener {

    /**
	 * logger
	 */
    private static Logger log = Logger.getLogger(CallbackMethodsCrudListener.class);

    /**
	 * {@inheritDoc}
	 */
    public void onCreate(String user, Object bean) throws BeforeCreateException {
        for (Method m : bean.getClass().getMethods()) {
            if (m.isAnnotationPresent(CrudBeforeCreate.class)) {
                try {
                    m.invoke(bean, new Object[] {});
                } catch (IllegalArgumentException e) {
                    throw new BeforeCreateException("Unable to call before persist methods. " + e.getMessage(), e);
                } catch (IllegalAccessException e) {
                    throw new BeforeCreateException("Unable to call before persist methods. " + e.getMessage(), e);
                } catch (InvocationTargetException e) {
                    throw new BeforeCreateException(e.getTargetException().getMessage(), e);
                }
            }
        }
    }

    /**
	 * {@inheritDoc}
	 */
    public void onRemove(String user, Object bean) throws BeforeRemoveException {
        for (Method m : bean.getClass().getMethods()) {
            if (m.isAnnotationPresent(CrudBeforeRemove.class)) {
                try {
                    m.invoke(bean, new Object[] {});
                } catch (IllegalArgumentException e) {
                    throw new BeforeRemoveException("Unable to call before remove methods. " + e.getMessage(), e);
                } catch (IllegalAccessException e) {
                    throw new BeforeRemoveException("Unable to call before remove methods. " + e.getMessage(), e);
                } catch (InvocationTargetException e) {
                    throw new BeforeRemoveException(e.getTargetException().getMessage(), e);
                }
            }
        }
    }

    /**
	 * {@inheritDoc}
	 */
    public void onUpdate(String user, Object beanBefore, Object beanAfter) throws BeforeUpdateException {
        for (Method m : beanAfter.getClass().getMethods()) {
            if (m.isAnnotationPresent(CrudBeforeUpdate.class) && m.getParameterTypes().length == 0) {
                try {
                    m.invoke(beanAfter, new Object[] {});
                } catch (IllegalArgumentException e) {
                    throw new BeforeUpdateException("Unable to call before update methods. " + e.getMessage(), e);
                } catch (IllegalAccessException e) {
                    throw new BeforeUpdateException("Unable to call before update methods. " + e.getMessage(), e);
                } catch (InvocationTargetException e) {
                    throw new BeforeUpdateException(e.getTargetException().getMessage(), e);
                }
            } else if (m.isAnnotationPresent(CrudBeforeUpdate.class) && m.getParameterTypes().length == 1 && m.getParameterTypes()[0].equals(beanAfter.getClass())) {
                try {
                    m.invoke(beanAfter, new Object[] { beanBefore });
                } catch (IllegalArgumentException e) {
                    throw new BeforeUpdateException("Unable to call before update methods. " + e.getMessage(), e);
                } catch (IllegalAccessException e) {
                    throw new BeforeUpdateException("Unable to call before update methods. " + e.getMessage(), e);
                } catch (InvocationTargetException e) {
                    throw new BeforeUpdateException(e.getTargetException().getMessage(), e);
                }
            } else if (m.isAnnotationPresent(CrudBeforeUpdate.class)) {
                log.warn("CrudBeforeUpdate annotations in the method " + beanAfter.getClass().getName() + "." + m.getName() + " is not supported");
            }
        }
    }
}

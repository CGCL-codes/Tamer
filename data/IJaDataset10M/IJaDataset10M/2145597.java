package de.hsofttec.monitoring.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.configuration.Configuration;
import de.hsofttec.monitoring.escalation.IEscalation;
import de.hsofttec.monitoring.exception.EscalationNotFoundException;
import de.hsofttec.monitoring.plugin.IPluginContext;

/**
 * @author <a href="mailto:shomburg@hsofttec.com">shomburg</a>
 * @version $Id: EscalationUtils.java 2 2007-09-01 11:09:16Z shomburg $
 */
public class EscalationUtils {

    /**
     * instantiert ein Eskalationsobject.
     *
     * @param escalationConfiguration name of escalation class
     *
     * @return Eskalationsobject
     */
    public static IEscalation instantiateEscalation(ClassLoader classLoader, IPluginContext pluginContext, Configuration escalationConfiguration) {
        try {
            Class clasz = classLoader.loadClass(escalationConfiguration.getString("class"));
            Constructor constructor = clasz.getConstructor(IPluginContext.class, Configuration.class);
            return (IEscalation) constructor.newInstance(pluginContext, escalationConfiguration);
        } catch (ClassNotFoundException e) {
            throw new EscalationNotFoundException(e);
        } catch (IllegalAccessException e) {
            throw new EscalationNotFoundException(e);
        } catch (InstantiationException e) {
            throw new EscalationNotFoundException(e);
        } catch (NoSuchMethodException e) {
            throw new EscalationNotFoundException(e);
        } catch (InvocationTargetException e) {
            throw new EscalationNotFoundException(e);
        }
    }
}

package net.sf.crispy.extension.pico;

import java.util.Properties;
import net.sf.crispy.PropertiesLoader;
import net.sf.crispy.impl.ServiceManager;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.Parameter;
import org.picocontainer.PicoIntrospectionException;
import org.picocontainer.defaults.AssignabilityRegistrationException;
import org.picocontainer.defaults.ComponentAdapterFactory;
import org.picocontainer.defaults.NotConcreteRegistrationException;

public class CrispyComponentAdapterFactory implements ComponentAdapterFactory {

    private ServiceManager serviceManager = null;

    public ComponentAdapter createComponentAdapter(Object pvKey, Class pvComponentImplementation, Parameter[] pvParam) throws PicoIntrospectionException, AssignabilityRegistrationException, NotConcreteRegistrationException {
        if (serviceManager == null) {
            if (pvKey instanceof PropertiesLoader) {
                serviceManager = new ServiceManager((PropertiesLoader) pvKey);
            } else if (pvKey instanceof Properties) {
                serviceManager = new ServiceManager((Properties) pvKey);
            } else {
                throw new IllegalArgumentException("The key value must be a PropertyLoader or Properties and not: " + pvKey);
            }
        }
        return new CrispyComponentAdapter(pvKey, pvComponentImplementation, serviceManager);
    }
}

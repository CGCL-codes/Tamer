package net.sf.joyaop.picocontainer;

import net.sf.joyaop.ObjectFactory;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.Parameter;
import org.picocontainer.PicoIntrospectionException;
import org.picocontainer.defaults.AssignabilityRegistrationException;
import org.picocontainer.defaults.CachingComponentAdapter;
import org.picocontainer.defaults.ComponentAdapterFactory;
import org.picocontainer.defaults.NotConcreteRegistrationException;

/**
 * only for stateless components
 *
 * @author Shen Li
 */
public class AspectizedComponentAdapterFactory implements ComponentAdapterFactory {

    private ObjectFactory objectFactory;

    public AspectizedComponentAdapterFactory() {
        objectFactory = Context.getContext().getAspectRuntime();
    }

    public AspectizedComponentAdapterFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    public ComponentAdapter createComponentAdapter(Object componentKey, Class componentImplementation, Parameter[] parameters) throws PicoIntrospectionException, AssignabilityRegistrationException, NotConcreteRegistrationException {
        return new CachingComponentAdapter(new ConstructorInjectionAspectizedComponentAdapter(objectFactory, componentKey, componentImplementation, parameters));
    }
}

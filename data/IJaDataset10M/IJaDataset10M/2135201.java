package org.nexopenframework.core.management;

import javax.management.NotificationListener;
import javax.management.ObjectName;
import org.nexopenframework.core.Component;
import org.nexopenframework.core.CoreException;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Comment here</p>
 * 
 * @see org.nexopenframework.core.Component
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public interface MBeanRegistrationManager extends Component {

    String COMPONENT_NAME = "openfrwk.core.mbean_register";

    ObjectName registerComponent(final Object component, final String key) throws CoreException;

    ObjectName registerComponent(final Object component, final String key, final NotificationListener listener);

    void unregisterComponent(final ObjectName objName);
}

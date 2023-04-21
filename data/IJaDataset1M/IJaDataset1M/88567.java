package org.nexopenframework.ws.axis2;

import org.nexopenframework.core.CoreException;
import org.nexopenframework.core.ws.MessageEndpointExporter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Comment here</p>
 * 
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
public class Axis2MessageEndpointExporter implements MessageEndpointExporter {

    public String getName() {
        return "Axis2 message endpoint exporter";
    }

    public void process(String name, BeanDefinition definition, ConfigurableListableBeanFactory beanFactory) throws CoreException {
    }

    public boolean supports(Class component) {
        return false;
    }

    public int getOrder() {
        return 0;
    }
}

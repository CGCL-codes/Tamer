package br.com.linkcom.neo.core.flex;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import org.granite.config.flex.Destination;
import org.granite.context.GraniteContext;
import org.granite.messaging.service.ServiceException;
import org.granite.messaging.service.ServiceInvoker;
import org.granite.messaging.service.SimpleServiceFactory;
import org.granite.messaging.webapp.HttpGraniteContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import flex.messaging.messages.RemotingMessage;

/**
 * @author Igor SAZHNEV
 */
public class NeoServiceFactory extends SimpleServiceFactory {

    private static final long serialVersionUID = 1L;

    private ApplicationContext springContext = null;

    @Override
    public void configure(Map<String, Object> properties) throws ServiceException {
        super.configure(properties);
        GraniteContext context = GraniteContext.getCurrentInstance();
        ServletContext sc = ((HttpGraniteContext) context).getServletContext();
        springContext = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
    }

    @Override
    public ServiceInvoker<?> getServiceInstance(RemotingMessage request) throws ServiceException {
        String messageType = request.getClass().getName();
        String destinationId = request.getDestination();
        GraniteContext context = GraniteContext.getCurrentInstance();
        Destination destination = context.getServicesConfig().findDestinationById(messageType, destinationId);
        if (destination == null) throw new ServiceException("No matching destination: " + destinationId);
        Destination d = destination;
        if (destination.getProperties().get("source").equals("*")) {
            Map<String, Serializable> propertiesMap = new HashMap<String, Serializable>();
            propertiesMap.put("source", request.getSource());
            d = new Destination(destination.getId(), destination.getChannelRefs(), propertiesMap, destination.getRoles(), destination.getAdapter());
        }
        String beanName = (String) d.getProperties().get("source");
        try {
            Object bean = springContext.getBean(beanName);
            return new NeoServiceInvoker(destination, this, bean);
        } catch (NoSuchBeanDefinitionException nexc) {
            String msg = "Spring service named '" + beanName + "' does not exist.";
            ServiceException e = new ServiceException(msg, nexc);
            throw e;
        } catch (BeansException bexc) {
            String msg = "Unable to create Spring service named '" + beanName + "'";
            ServiceException e = new ServiceException(msg, bexc);
            throw e;
        }
    }

    @Override
    public String toString() {
        return toString("\n  springContext: " + springContext);
    }
}

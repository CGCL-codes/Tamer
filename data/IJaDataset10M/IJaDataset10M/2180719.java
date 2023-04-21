package net.sf.sunday.granite.messaging;

import org.granite.config.flex.Destination;
import org.granite.context.GraniteContext;
import org.granite.messaging.service.ServiceException;
import org.granite.messaging.service.ServiceFactory;
import org.granite.messaging.webapp.HttpGraniteContext;
import flex.messaging.messages.RemotingMessage;

public class MentawaiServiceFactory extends ServiceFactory {

    private HttpGraniteContext getHttpContext() {
        GraniteContext context = GraniteContext.getCurrentInstance();
        if (!(context instanceof HttpGraniteContext)) {
            throw new ServiceException("Unable to get ServletContext");
        }
        HttpGraniteContext httpContext = ((HttpGraniteContext) context);
        return httpContext;
    }

    @Override
    public MentawaiServiceInvoker getServiceInstance(RemotingMessage request) throws ServiceException {
        String messageType = request.getClass().getName();
        String destinationId = request.getDestination();
        HttpGraniteContext httpContext = getHttpContext();
        Destination destination = httpContext.getServicesConfig().findDestinationById(messageType, destinationId);
        return new MentawaiServiceInvoker(destination, this, httpContext);
    }
}

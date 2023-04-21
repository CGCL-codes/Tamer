package org.mentaflex.flex.granite;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.granite.config.flex.Destination;
import org.granite.messaging.service.ServiceException;
import org.granite.messaging.service.ServiceInvoker;
import org.granite.messaging.webapp.HttpGraniteContext;
import org.mentaflex.core.FlexController;
import org.mentaflex.flex.FlexInvoker;
import org.mentawai.core.Controller;

/**
 * 
 * @author Velo
 * @author Fabiano Frizzo (ffrizzo@gmail.com
 */
public class GraniteServiceInvoker extends ServiceInvoker<GraniteServiceFactory> {

    private static final long serialVersionUID = 1L;

    public GraniteServiceInvoker(Destination destination, GraniteServiceFactory factory, HttpGraniteContext context) throws ServiceException {
        super(destination, factory);
        ServletContext sc = context.getServletContext();
        Controller controller = (Controller) sc.getAttribute(FlexController.MENTA_CONTROLLER);
        HttpServletRequest req = context.getRequest();
        HttpServletResponse res = context.getResponse();
        super.invokee = new FlexInvoker(controller, req, res);
    }
}

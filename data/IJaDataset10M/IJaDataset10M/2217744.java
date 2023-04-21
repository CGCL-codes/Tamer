package org.riverock.module.web.dispatcher;

import java.io.IOException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletException;
import org.apache.log4j.Logger;
import org.riverock.module.web.request.ModuleRequest;
import org.riverock.module.web.response.ModuleResponse;
import org.riverock.module.exception.ModuleException;

/**
 * @author SMaslyukov
 *         Date: 26.04.2005
 *         Time: 15:37:08
 *         $Id: PortletModuleRequestDispatcherImpl.java,v 1.3 2006/06/05 19:19:11 serg_main Exp $
 */
public class PortletModuleRequestDispatcherImpl implements ModuleRequestDispatcher {

    private static final Logger log = Logger.getLogger(PortletModuleRequestDispatcherImpl.class);

    private PortletRequestDispatcher portletRequestDispatcher = null;

    public PortletModuleRequestDispatcherImpl(PortletRequestDispatcher portletRequestDispatcher) {
        this.portletRequestDispatcher = portletRequestDispatcher;
    }

    public void include(ModuleRequest moduleRequest, ModuleResponse moduleResponse) throws ModuleException, IOException {
        if (moduleRequest.getOriginRequest() instanceof RenderRequest && moduleResponse.getOriginResponse() instanceof RenderResponse) {
            try {
                portletRequestDispatcher.include((RenderRequest) moduleRequest.getOriginRequest(), (RenderResponse) moduleResponse.getOriginResponse());
            } catch (PortletException e) {
                String es = "Error include new resource";
                log.error(es, e);
                throw new ModuleException(es, e);
            }
        }
    }
}

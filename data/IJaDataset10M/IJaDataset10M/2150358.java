package org.impalaframework.web.spring.integration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.util.ObjectUtils;
import org.impalaframework.web.helper.WebServletUtils;
import org.impalaframework.web.integration.RequestModuleMapper;

/**
 * Extension of {@link org.impalaframework.web.integration.ModuleProxyServlet} which uses {@link RequestModuleMapper} retrieved from 
 * Spring application context to perform request to module mapping.
 * 
 * @author Phil Zoio
 */
public class ModuleProxyServlet extends org.impalaframework.web.integration.ModuleProxyServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected RequestModuleMapper newRequestModuleMapper(ServletConfig config) {
        final ServletContext servletContext = config.getServletContext();
        ModuleManagementFacade facade = WebServletUtils.getModuleManagementFacade(servletContext);
        if (facade.containsBean("requestModuleMapper")) {
            return ObjectUtils.cast(facade.getBean("requestModuleMapper"), RequestModuleMapper.class);
        }
        return super.newRequestModuleMapper(config);
    }
}

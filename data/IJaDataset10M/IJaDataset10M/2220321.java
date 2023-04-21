package fi.mmmtike.tiira.invoker.ws.server;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import fi.mmmtike.tiira.context.TiiraContext;
import fi.mmmtike.tiira.core.TiiraBeanUtils;
import fi.mmmtike.tiira.resolver.TiiraServletContextResolver;

public class TiiraWsServlet extends MessageDispatcherServlet {

    private TiiraServletContextResolver servletContextResolver = new TiiraServletContextResolver();

    public TiiraContext tiiraContext;

    public TiiraWsServlet() {
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        resolveTiiraContext(config);
        TiiraXmlBeansEndpointMapping endPointMapping = (TiiraXmlBeansEndpointMapping) this.getWebApplicationContext().getBean("tiiraEndpointMapping");
        endPointMapping.setTiiraContext(tiiraContext);
    }

    private void resolveTiiraContext(ServletConfig config) {
        if (this.getWebApplicationContext().containsBean("tiiraContext")) {
            tiiraContext = (TiiraContext) this.getWebApplicationContext().getBean("tiiraContext");
        } else {
            tiiraContext = servletContextResolver.resolveTiiraContext(config);
        }
    }
}

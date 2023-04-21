package net.disy.ogc.wps.v_1_0_0.proxy.servlet;

import java.util.Arrays;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import net.disy.ogc.wps.v_1_0_0.proxy.DefaultProxyWpsProcessFactory;
import net.disy.ogc.wps.v_1_0_0.proxy.GenericMarshallingHttpClient;
import net.disy.ogc.wps.v_1_0_0.proxy.HostPrefixStrategy;
import net.disy.ogc.wps.v_1_0_0.proxy.ProxyProcessAdder;
import net.disy.ogc.wps.v_1_0_0.proxy.ProxyWpsProcessFactory;
import net.disy.ogc.wps.v_1_0_0.util.WpsOperationsUtilities;
import org.apache.commons.lang.StringUtils;

public class WpsProxyConfigurationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public static final String CAPABILITIES_INIT_PARAMETER_NAME = "capabilities";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        final ProxyWpsProcessFactory proxyFactory = new DefaultProxyWpsProcessFactory(new GenericMarshallingHttpClient(WpsOperationsUtilities.getWpsProcessContext(config.getServletContext()).getJAXBContext()), new HostPrefixStrategy());
        String capabilitiesUrlsString = config.getInitParameter(CAPABILITIES_INIT_PARAMETER_NAME);
        if (capabilitiesUrlsString != null) {
            final String[] capabilitiesUrls = StringUtils.split(capabilitiesUrlsString, ',');
            try {
                new ProxyProcessAdder(WpsOperationsUtilities.getWpsProcessRegistry(config.getServletContext()), proxyFactory).addProxyProcesses(Arrays.asList(capabilitiesUrls));
            } catch (Exception e) {
                throw new ServletException("Could not create proxy processes.", e);
            }
        }
    }
}

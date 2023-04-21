package net.dataforte.canyon.spi.echo3.components;

import nextapp.echo.webcontainer.Service;
import nextapp.echo.webcontainer.WebContainerServlet;
import nextapp.echo.webcontainer.service.JavaScriptService;

/**
 * Common service for Echo Extras.
 * 
 * @author n.beekman
 */
public class CommonService {

    public static final Service INSTANCE = JavaScriptService.forResource("Canyon.Common", "net/dataforte/canyon/spi/echo3/components/resource/Canyon.js");

    static {
        WebContainerServlet.getServiceRegistry().add(INSTANCE);
    }

    private CommonService() {
        super();
    }
}

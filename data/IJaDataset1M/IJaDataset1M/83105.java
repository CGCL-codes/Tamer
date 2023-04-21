package purej.service.builder;

import org.dom4j.Node;
import purej.logging.Logger;
import purej.logging.LoggerFactory;
import purej.service.domain.WebServices;

/**
 * ������ ������ ����
 * 
 * @author Administrator
 * 
 */
public class WebServicesBuild extends XMLServiceBuilder {

    private static final Logger log = LoggerFactory.getLogger(WebServicesBuild.class, Logger.FRAMEWORK);

    private WebServices webServices;

    public WebServicesBuild(Node node) throws Exception {
        initialize(node);
    }

    public void initialize(Node node) throws Exception {
        webServices = new WebServices();
        try {
            webServices.setId(node.valueOf("@id"));
            webServices.setName(node.valueOf("@name"));
            webServices.setType(node.valueOf("@type"));
            webServices.setWebServicesEndpoint(node.valueOf("@webservices-endpoint"));
            webServices.setDescription(getDescription(node));
        } catch (Exception ex) {
            String msg = "WebServices Service type domain build from XML error : " + ex.getMessage();
            log.error(msg, ex);
            throw new ServiceBuildException(msg, ex);
        }
    }

    /**
     * @return the webServices
     */
    public WebServices getWebServices() {
        return webServices;
    }

    /**
     * @param webServices
     *                the webServices to set
     */
    public void setWebServices(WebServices webServices) {
        this.webServices = webServices;
    }
}

package gov.lanl.disseminator.service.toc;

import gov.lanl.disseminator.model.ContextObjectContainer;
import gov.lanl.disseminator.model.Entity;
import gov.lanl.disseminator.service.AbstractService;
import gov.lanl.util.resource.Resource;
import info.openurl.oom.config.ClassConfig;
import info.openurl.oom.config.OpenURLConfig;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import javax.xml.transform.TransformerException;
import org.apache.log4j.Logger;

public class REMService extends AbstractService {

    private static Logger logger = Logger.getLogger(REMService.class.getName());

    private String baseuri = null;

    private String openurl = null;

    /**
	 * Construct a Hello World web service class.
	 * 
	 * @param openURLConfig
	 * @param classConfig
	 * @throws TransformerException
	 */
    public REMService(OpenURLConfig openURLConfig, ClassConfig classConfig) throws TransformerException {
        super(openURLConfig, classConfig);
        this.baseuri = classConfig.getArg("baseuri");
        this.openurl = classConfig.getArg("openurl");
    }

    @Override
    public Resource serve(ContextObjectContainer co) throws Exception {
        co.getServiceType().setProperty("rem", "true");
        REMCreator rm = new REMCreator();
        Entity resolver = co.getResolver();
        String res_url;
        if (resolver.hasProperty("res_id")) {
            res_url = resolver.getProperty("res_id");
        } else {
            res_url = baseuri;
        }
        HashMap marcXML = getMetaData(co);
        rm.setMarcXML(marcXML);
        String output;
        String oflag;
        Entity service = co.getServiceType();
        if (service.hasProperty("svc.openurl")) {
            oflag = service.getProperty("svc.openurl");
            System.out.println("oflag:" + oflag);
        } else {
            oflag = openurl;
        }
        if (oflag.equals("true")) {
            rm.setFlag(true);
        } else {
            rm.setFlag(false);
        }
        rm.setBaseURI(res_url);
        output = rm.getAtomXML(co);
        Resource result = new Resource();
        result.setContentType("application/xml");
        result.setBytes(output.getBytes());
        return result;
    }

    public URI getServiceID() throws URISyntaxException {
        return null;
    }
}

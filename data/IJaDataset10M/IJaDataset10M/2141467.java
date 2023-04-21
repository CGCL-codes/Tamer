package jeeves.interfaces;

import jeeves.server.ServiceConfig;
import jeeves.server.context.ServiceContext;
import org.jdom.Element;

public interface Service {

    public void init(String appPath, ServiceConfig params) throws Exception;

    public Element exec(Element params, ServiceContext context) throws Exception;
}

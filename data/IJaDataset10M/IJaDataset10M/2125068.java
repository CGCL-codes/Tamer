package jeeves.server.dispatchers.guiservices;

import org.jdom.*;
import jeeves.constants.*;
import jeeves.interfaces.*;
import jeeves.server.*;
import jeeves.server.context.*;
import jeeves.server.dispatchers.*;
import jeeves.utils.*;

public class Call implements GuiService {

    private String name;

    private Service serviceObj;

    public Call(Element config, String pack, String appPath) throws Exception {
        name = Util.getAttrib(config, ConfigFile.Call.Attr.NAME);
        String clas = Util.getAttrib(config, ConfigFile.Call.Attr.CLASS);
        if (clas.startsWith(".")) clas = pack + clas;
        serviceObj = (Service) Class.forName(clas).newInstance();
        serviceObj.init(appPath, new ServiceConfig(config.getChildren()));
    }

    public Element exec(Element response, ServiceContext context) throws Exception {
        try {
            response = serviceObj.exec(response, context);
            context.getResourceManager().close();
            if (response != null) response.setName(name);
            return response;
        } catch (Exception e) {
            context.getResourceManager().abort();
            throw e;
        }
    }
}

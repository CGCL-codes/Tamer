package api.client.bpmModel.bpelCommands;

import api.client.bpmModel.Attribute;
import api.client.bpmModel.BPELCommand;
import api.client.bpmModel.Container;

/**
 * answer to open webservice call
 * 
 * @author Mirko
 * 
 */
public class Reply extends BPELCommand {

    /**
	 * creates a new instance of the Reply command
	 * 
	 * @param partnerLink
	 *            partnerLink for the reply command
	 * @param portType
	 *            porttype for the reply command
	 * @param operation
	 *            operation which will be executed
	 * @param variable
	 *            variable which will be used
	 * @param faultName
	 */
    public Reply(String partnerLink, String portType, String operation, String variable, String faultName, String ns, String url, String pName) {
        super("reply");
        this.myUtility.put("partnerLink", new Attribute("partnerLink", partnerLink));
        this.myUtility.put("portType", new Attribute("portType", ns + ":" + portType));
        this.myUtility.put("operation", new Attribute("operation", operation));
        this.myUtility.put("variable", new Attribute("variable", variable));
        Container.genInfra(partnerLink, operation, ns, url, pName);
    }
}

package onepoint.project.modules.custom_attribute;

import java.util.LinkedList;
import java.util.List;
import onepoint.persistence.OpBroker;
import onepoint.persistence.OpSiteObjectIfc;
import onepoint.project.OpProjectSession;
import onepoint.project.OpServiceBase;

/**
 * @author dfreis
 *
 */
public class OpCustomAttributeServiceImpl extends OpServiceBase {

    /**
    * The name of this service.
    */
    public static final String SERVICE_NAME = "CustomAttributeService";

    public OpCustomAttributeServiceImpl() {
        super(SERVICE_NAME);
    }

    /**
    * @param session
    * @pre
    * @post
    */
    public void init(OpProjectSession session) {
    }

    /**
    * @param session
    * @param broker
    * @param name
    * @param string
    * @param string2
    * @return
    * @pre
    * @post
    */
    public <O extends OpSiteObjectIfc> List<O> getObjects(OpProjectSession session, OpBroker broker, OpCustomAttribute attribute, Object attributeValue) {
        return new LinkedList<O>();
    }
}

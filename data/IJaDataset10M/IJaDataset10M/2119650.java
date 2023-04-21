package org.ez.messageServer;

import java.util.ArrayList;
import java.util.List;
import org.apache.camel.RecipientList;
import org.ez.messageGateway.EZMsgEnv;
import org.ez.messageGateway.IEZMsgEnv;

public class EZMsgNameRouter {

    private String jmsPrefix;

    @RecipientList
    public List<String> route(String body) {
        List<String> retVal = new ArrayList<String>();
        IEZMsgEnv msg = EZMsgEnv.newInstanceFromXml(body);
        String dest;
        dest = "mesGatJms:queue:" + jmsPrefix + "org.ez." + msg.getName();
        retVal.add(dest);
        return retVal;
    }

    public void setJmsPrefix(String jmsPrefix) {
        this.jmsPrefix = jmsPrefix;
    }
}

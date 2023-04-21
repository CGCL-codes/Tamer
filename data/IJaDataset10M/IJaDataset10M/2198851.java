package connex.plugins.filesharing;

import net.jxta.share.*;
import net.jxta.endpoint.EndpointAddress;
import net.jxta.endpoint.Message;
import net.jxta.protocol.ResolverQueryMsg;
import java.io.*;

public class CMService extends CMS {

    protected static CMService myCMS = null;

    public CMService() {
        myCMS = this;
    }

    protected void publishContent(Content c) {
        super.publishContent(c);
    }

    public void processIncomingMessage(Message message, EndpointAddress srcAddr, EndpointAddress dstAddr) {
        String messageType = null;
        try {
            messageType = popString(message, MESSAGE_TYPE);
        } catch (IOException ex) {
        }
        System.out.println("processIncomingMessage " + messageType);
        super.processIncomingMessage(message, srcAddr, dstAddr);
    }

    public int processQuery(ResolverQueryMsg query) {
        String queryStr = query.getQuery();
        System.out.println("processQuery received query: " + queryStr);
        return super.processQuery(query);
    }

    public static CMService getInstance() {
        if (myCMS == null) {
            return new CMService();
        } else {
            return myCMS;
        }
    }
}

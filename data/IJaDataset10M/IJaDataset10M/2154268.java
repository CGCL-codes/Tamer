package test.openmobster.device.agent.sync.server;

import org.openmobster.cloud.api.sync.ChannelInfo;
import org.openmobster.cloud.api.sync.MobileBean;

/**
 * @author openmobster@gmail.com
 */
@ChannelInfo(uri = "testServerBean.testMapping", mobileBeanClass = "test.openmobster.device.agent.sync.server.ServerRecord")
public class ServerConnectorMapping extends ServerConnector {

    protected ServerRecordController getServerController() {
        return ServerRecordControllerMapping.getInstance();
    }

    public String create(MobileBean object) {
        String recordId = null;
        ServerRecord serverRecord = (ServerRecord) object;
        ServerRecord newRecord = new ServerRecord();
        newRecord.setFrom(serverRecord.getFrom());
        newRecord.setTo(serverRecord.getTo());
        newRecord.setMessage(serverRecord.getMessage());
        newRecord.setSubject(serverRecord.getSubject());
        newRecord.setAttachment(serverRecord.getAttachment());
        recordId = this.getServerController().create(newRecord);
        return recordId;
    }
}

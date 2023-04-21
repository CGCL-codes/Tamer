package org.openmeetings.app.data.flvrecord.listener;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import org.apache.mina.core.buffer.IoBuffer;
import org.openmeetings.app.data.flvrecord.FlvRecordingMetaDataDaoImpl;
import org.openmeetings.app.data.flvrecord.FlvRecordingMetaDeltaDaoImpl;
import org.openmeetings.app.hibernate.beans.flvrecord.FlvRecordingMetaData;
import org.openmeetings.app.hibernate.beans.flvrecord.FlvRecordingMetaDelta;
import org.openmeetings.app.remote.red5.ScopeApplicationAdapter;
import org.red5.io.ITag;
import org.red5.io.flv.impl.Tag;
import org.red5.logging.Red5LoggerFactory;
import org.red5.server.api.IScope;
import org.red5.server.api.stream.IBroadcastStream;
import org.red5.server.api.stream.IStreamPacket;
import org.slf4j.Logger;

public class StreamScreenListener extends ListenerAdapter {

    private int startTimeStamp = -1;

    private long byteCount = 0;

    private Date startedSessionScreenTimeDate = null;

    private long initialDelta = 0;

    private static final Logger log = Red5LoggerFactory.getLogger(StreamScreenListener.class, "openmeetings");

    public StreamScreenListener(String streamName, IScope scope, Long flvRecordingMetaDataId, boolean isScreenData, boolean isInterview) {
        super(streamName, scope, flvRecordingMetaDataId, isScreenData, isInterview);
    }

    @Override
    public void packetReceived(IBroadcastStream broadcastStream, IStreamPacket streampacket) {
        try {
            if (this.startedSessionScreenTimeDate == null) {
                this.startedSessionScreenTimeDate = new Date();
                this.initialDelta = this.startedSessionScreenTimeDate.getTime() - this.startedSessionTimeDate.getTime();
                FlvRecordingMetaDataDaoImpl.getInstance().updateFlvRecordingMetaDataInitialGap(flvRecordingMetaDataId, this.initialDelta);
            }
            if (this.isClosed) {
                return;
            }
            if (streampacket.getTimestamp() <= 0) {
                log.warn("Negative TimeStamp");
                return;
            }
            IoBuffer data = streampacket.getData().asReadOnlyBuffer();
            if (data.limit() == 0) {
                return;
            }
            this.byteCount += data.limit();
            if (startTimeStamp == -1) {
                startTimeStamp = streampacket.getTimestamp();
            }
            if (writer == null) {
                File folder = new File(ScopeApplicationAdapter.webAppPath + File.separatorChar + "streams" + File.separatorChar + this.scope.getName());
                if (!folder.exists()) {
                    folder.mkdir();
                }
                String flvName = ScopeApplicationAdapter.webAppPath + File.separatorChar + "streams" + File.separatorChar + this.scope.getName() + File.separatorChar + this.streamName + ".flv";
                file = new File(flvName);
                init();
            }
            int timeStamp = streampacket.getTimestamp();
            timeStamp -= startTimeStamp;
            ITag tag = new Tag();
            tag.setDataType(streampacket.getDataType());
            tag.setBodySize(data.limit());
            tag.setTimestamp(timeStamp);
            tag.setBody(data);
            if (this.isInterview) {
                if (timeStamp <= 500) {
                    return;
                }
            }
            writer.writeTag(tag);
        } catch (IOException e) {
            log.error("[packetReceived]", e);
        } catch (Exception e) {
            log.error("[packetReceived]", e);
        }
    }

    @Override
    public void closeStream() throws Exception {
        if (writer != null && !this.isClosed) {
            try {
                FlvRecordingMetaData flvRecordingMetaData = FlvRecordingMetaDataDaoImpl.getInstance().getFlvRecordingMetaDataById(this.flvRecordingMetaDataId);
                flvRecordingMetaData.setRecordStart(new Date(flvRecordingMetaData.getRecordStart().getTime() + this.initialDelta));
                FlvRecordingMetaDataDaoImpl.getInstance().updateFlvRecordingMetaData(flvRecordingMetaData);
                writer.close();
                this.isClosed = true;
            } catch (Exception err) {
                log.error("[closeStream]", err);
            }
        }
    }
}

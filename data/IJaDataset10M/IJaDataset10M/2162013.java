package org.openmeetings.server.rtmp;

import java.io.IOException;
import org.red5.io.IStreamableFile;
import org.red5.io.ITag;
import org.red5.io.ITagWriter;
import org.red5.io.ITagReader;
import org.red5.io.flv.impl.FLVService;
import org.red5.io.flv.impl.FLV;
import org.red5.io.flv.impl.FLVReader;
import org.red5.io.flv.impl.Tag;
import org.red5.io.IoConstants;
import org.red5.io.utils.ObjectMap;
import org.red5.logging.Red5LoggerFactory;
import org.red5.server.api.event.IEvent;
import org.red5.server.api.event.IEventDispatcher;
import org.red5.server.api.service.IPendingServiceCall;
import org.red5.server.api.service.IPendingServiceCallback;
import org.red5.server.net.rtmp.Channel;
import org.red5.server.net.rtmp.RTMPClient;
import org.red5.server.net.rtmp.INetStreamEventHandler;
import org.red5.server.net.rtmp.RTMPConnection;
import org.red5.server.net.rtmp.ClientExceptionHandler;
import org.red5.server.net.rtmp.codec.RTMP;
import org.red5.server.net.rtmp.event.AudioData;
import org.red5.server.net.rtmp.event.IRTMPEvent;
import org.red5.server.net.rtmp.event.Notify;
import org.red5.server.net.rtmp.event.VideoData;
import org.red5.server.net.rtmp.message.Header;
import org.red5.server.net.rtmp.status.StatusCodes;
import org.red5.server.net.rtmp.event.SerializeUtils;
import org.red5.server.stream.AbstractClientStream;
import org.red5.server.stream.IStreamData;
import org.red5.server.stream.message.RTMPMessage;
import org.slf4j.Logger;

/**
 * @author sebastianwagner
 *
 */
public class ScreenClient extends RTMPClient implements INetStreamEventHandler, ClientExceptionHandler, IPendingServiceCallback {

    private static final Logger logger = Red5LoggerFactory.getLogger(ScreenClient.class, "openmeetings");

    public boolean createdPlayStream = false;

    public boolean startPublish = false;

    public Integer playStreamId;

    public Integer publishStreamId;

    private String publishName;

    private String playName;

    private RTMPConnection conn;

    private ITagWriter writer;

    private ITagReader reader;

    private int videoTs = 0;

    private int audioTs = 0;

    private int kt = 0;

    private int kt2 = 0;

    @Override
    public void connectionOpened(RTMPConnection conn, RTMP state) {
        logger.debug("connection opened");
        super.connectionOpened(conn, state);
        this.conn = conn;
    }

    @Override
    public void connectionClosed(RTMPConnection conn, RTMP state) {
        logger.debug("connection closed");
        super.connectionClosed(conn, state);
    }

    @Override
    protected void onInvoke(RTMPConnection conn, Channel channel, Header header, Notify notify, RTMP rtmp) {
        super.onInvoke(conn, channel, header, notify, rtmp);
        try {
            ObjectMap<String, String> map = (ObjectMap) notify.getCall().getArguments()[0];
            String code = map.get("code");
            if (StatusCodes.NS_PLAY_STOP.equals(code)) {
                logger.debug("onInvoke, code == NetStream.Play.Stop, disconnecting");
                disconnect();
            }
        } catch (Exception e) {
        }
    }

    public void startStream(String host, String app, int port, String publishName, String playName) {
        System.out.println("RTMPUser startStream");
        this.publishName = publishName;
        this.playName = playName;
        createdPlayStream = false;
        startPublish = false;
        videoTs = 0;
        audioTs = 0;
        kt = 0;
        kt2 = 0;
        try {
            connect(host, port, app, this);
            while (!startPublish) {
                Thread.yield();
            }
        } catch (Exception e) {
            logger.error("RTMPUser startStream exception " + e);
        }
    }

    public void stopStream() {
        System.out.println("RTMPUser stopStream");
        try {
            disconnect();
        } catch (Exception e) {
            logger.error("RTMPUser stopStream exception " + e);
        }
    }

    public void handleException(Throwable throwable) {
        logger.error("{}" + new Object[] { throwable.getCause() });
        System.out.println(throwable.getCause());
    }

    public void onStreamEvent(Notify notify) {
        logger.debug("onStreamEvent " + notify);
        ObjectMap map = (ObjectMap) notify.getCall().getArguments()[0];
        String code = (String) map.get("code");
        if (StatusCodes.NS_PUBLISH_START.equals(code)) {
            logger.debug("onStreamEvent Publish start");
            startPublish = true;
        }
    }

    public void resultReceived(IPendingServiceCall call) {
        logger.debug("service call result: " + call);
        if ("connect".equals(call.getServiceMethodName())) {
            createPlayStream(this);
        } else if ("createStream".equals(call.getServiceMethodName())) {
            if (createdPlayStream) {
                publishStreamId = (Integer) call.getResult();
                logger.debug("createPublishStream result stream id: " + publishStreamId);
                logger.debug("publishing video by name: " + publishName);
                publish(publishStreamId, publishName, "live", this);
            } else {
                playStreamId = (Integer) call.getResult();
                logger.debug("createPlayStream result stream id: " + playStreamId);
                logger.debug("playing video by name: " + playName);
                play(playStreamId, playName, -2000, -1000);
                createdPlayStream = true;
                createStream(this);
            }
        }
    }

    public void pushVideo() {
    }

    public void pushAudio(int len, byte[] audio, long ts, int codec) throws IOException {
    }

    private void createPlayStream(IPendingServiceCallback callback) {
        logger.debug("create play stream");
        IPendingServiceCallback wrapper = new CreatePlayStreamCallBack(callback);
        invoke("createStream", null, wrapper);
    }

    private class CreatePlayStreamCallBack implements IPendingServiceCallback {

        private IPendingServiceCallback wrapped;

        public CreatePlayStreamCallBack(IPendingServiceCallback wrapped) {
            this.wrapped = wrapped;
        }

        public void resultReceived(IPendingServiceCall call) {
            Integer streamIdInt = (Integer) call.getResult();
            if (conn != null && streamIdInt != null) {
                PlayNetStream stream = new PlayNetStream();
                stream.setConnection(conn);
                stream.setStreamId(streamIdInt.intValue());
                conn.addClientStream(stream);
            }
            wrapped.resultReceived(call);
        }
    }

    private class PlayNetStream extends AbstractClientStream implements IEventDispatcher {

        public void close() {
        }

        public void start() {
        }

        public void stop() {
        }

        public void dispatchEvent(IEvent event) {
            if (!(event instanceof IRTMPEvent)) {
                logger.debug("skipping non rtmp event: " + event);
                return;
            }
            IRTMPEvent rtmpEvent = (IRTMPEvent) event;
            if (logger.isDebugEnabled()) {
            }
            if (!(rtmpEvent instanceof IStreamData)) {
                logger.debug("skipping non stream data");
                return;
            }
            if (rtmpEvent.getHeader().getSize() == 0) {
                logger.debug("skipping event where size == 0");
                return;
            }
            if (rtmpEvent instanceof VideoData) {
            } else if (rtmpEvent instanceof AudioData) {
            }
        }
    }
}

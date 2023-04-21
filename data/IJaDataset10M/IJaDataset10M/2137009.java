package org.red5.server.plugin.shoutcast;

import java.util.Map;
import org.apache.mina.core.buffer.IoBuffer;

/**
 * @author Wittawas Nakkasem (vittee@hotmail.com)
 * @author Andy Shaules (bowljoman@hotmail.com)
 * 
 */
public interface IICYHandler {

    public void onConnected(String vidType, String audioType);

    public void onDisconnected();

    public void onRawData(int[] b);

    public void onAuxData(String fourCC, IoBuffer buffer);

    public void onAudioData(int[] data, int timestamp, int offset);

    public void onVideoData(int[] data, int timestamp, int offset);

    public void onMetaData(Map<String, Object> metaData);

    public void start();

    public void stop();

    public void reset(String content, String type);
}

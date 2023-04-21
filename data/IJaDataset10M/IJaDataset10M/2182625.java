package com.thebitstream.comserver.stream;

import org.red5.server.api.event.IEvent;
import org.red5.server.api.statistics.IClientBroadcastStreamStatistics;
import org.red5.server.api.stream.IBroadcastStream;
import org.red5.server.messaging.IPipeConnectionListener;
import org.red5.server.messaging.IProvider;
import org.red5.server.net.rtmp.event.IRTMPEvent;

/**
 * @author Andy Shaules
 * @version 1.0
 */
public interface IResourceStream extends IBroadcastStream, IProvider, IPipeConnectionListener, IClientBroadcastStreamStatistics {

    /**
	 * Dispatch event at exactly this mark in stream time by editing event timestamp.
	 * @param event
	 */
    void dispatchEvent(IEvent event);

    /**
	 * Dispatch event without editing event time stamp.
	 * TODO version 2.0
	 * 
	 * @param event
	 */
    void dispatchStreamEvent(IEvent event);

    public void setMetaDataEvent(IRTMPEvent event);
}

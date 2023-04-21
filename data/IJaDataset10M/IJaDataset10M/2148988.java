package org.gudy.azureus2.core3.tracker.host.impl;

import java.util.*;
import org.gudy.azureus2.core3.tracker.host.*;
import org.gudy.azureus2.core3.tracker.server.*;

public class TRHostTorrentRequestImpl implements TRHostTorrentRequest {

    protected TRHostTorrent torrent;

    protected TRHostPeer peer;

    protected TRTrackerServerRequest request;

    protected TRHostTorrentRequestImpl(TRHostTorrent _torrent, TRHostPeer _peer, TRTrackerServerRequest _request) {
        torrent = _torrent;
        peer = _peer;
        request = _request;
    }

    public TRHostPeer getPeer() {
        return (peer);
    }

    public TRHostTorrent getTorrent() {
        return (torrent);
    }

    public int getRequestType() {
        if (request.getType() == TRTrackerServerRequest.RT_ANNOUNCE) {
            return (RT_ANNOUNCE);
        } else if (request.getType() == TRTrackerServerRequest.RT_SCRAPE) {
            return (RT_SCRAPE);
        } else {
            return (RT_FULL_SCRAPE);
        }
    }

    public String getRequest() {
        return (request.getRequest());
    }

    public Map getResponse() {
        return (request.getResponse());
    }
}

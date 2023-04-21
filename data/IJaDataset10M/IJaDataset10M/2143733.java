package com.google.code.gwtosgi.server;

import net.zschech.gwt.comet.server.CometSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.atlassian.plugin.event.PluginEventListener;
import com.atlassian.plugin.event.events.PluginDisabledEvent;
import com.atlassian.plugin.event.events.PluginEnabledEvent;
import com.google.code.gwtosgi.plugins.WebbloxService;

public class BloxListener {

    private static final Log LOG = LogFactory.getLog(BloxListener.class);

    private WebbloxService webBloxService;

    private CometSession cometSession;

    public BloxListener(CometSession cometSession, WebbloxService webBloxService) {
        LOG.info("Creating new listener");
        this.cometSession = cometSession;
        this.webBloxService = webBloxService;
        cometSession.enqueue("New listener created");
    }

    @PluginEventListener
    public void onPluginRegistered(PluginEnabledEvent event) {
        LOG.info("OnPluginRegistered called:" + event);
        if (!this.cometSession.isValid()) {
            LOG.info("Comet session was no longer valid, unregistering");
            webBloxService.unregisterListener(this);
        }
        this.cometSession.enqueue("Plugin registered");
    }

    @PluginEventListener
    public void onPluginDeregistered(PluginDisabledEvent event) {
        LOG.info("OnPluginRegistered called:" + event);
        if (!this.cometSession.isValid()) {
            LOG.info("Comet session was no longer valid, unregistering");
            webBloxService.unregisterListener(this);
        }
        this.cometSession.enqueue("Plugin unregistered");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cometSession == null) ? 0 : cometSession.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BloxListener other = (BloxListener) obj;
        if (cometSession == null) {
            if (other.cometSession != null) {
                return false;
            }
        } else if (cometSession != other.cometSession) {
            return false;
        }
        return true;
    }
}

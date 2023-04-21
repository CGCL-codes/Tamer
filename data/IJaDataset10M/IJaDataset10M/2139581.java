package com.google.code.sagetvaddons.sagealert.server;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import com.google.code.sagetvaddons.sagealert.shared.CsvLogFileSettings;
import com.google.code.sagetvaddons.sagealert.shared.NotificationServerSettings;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent;

/**
 * @author dbattams
 *
 */
class CsvLogFileServer extends LogFileServer {

    private static final Logger LOG = Logger.getLogger(CsvLogFileServer.class);

    private static final Map<CsvLogFileSettings, CsvLogFileServer> SERVERS = new HashMap<CsvLogFileSettings, CsvLogFileServer>();

    static final synchronized CsvLogFileServer get(CsvLogFileSettings settings) {
        CsvLogFileServer srv = SERVERS.get(settings);
        if (srv == null) {
            srv = new CsvLogFileServer(settings);
            SERVERS.put(settings, srv);
            LOG.debug(SERVERS.size() + " server(s) now in cache.");
        }
        return srv;
    }

    static final synchronized void delete(CsvLogFileSettings settings) {
        if (SERVERS.remove(settings) != null) LOG.info("Removed '" + settings.getDisplayValue() + "' from cache!");
    }

    private static final SimpleDateFormat FMT = new SimpleDateFormat("dd MMM yyyy HH:mm:ss Z");

    private CsvLogFileSettings settings;

    private StringBuilder msg;

    /**
	 * @param target
	 */
    private CsvLogFileServer(CsvLogFileSettings settings) {
        super(new File(settings.getTarget()));
        this.settings = settings;
        msg = new StringBuilder();
    }

    public NotificationServerSettings getSettings() {
        return settings;
    }

    public synchronized void onEvent(SageAlertEvent e) {
        msg.setLength(0);
        msg.append(e.getSource() + ",");
        synchronized (FMT) {
            msg.append("\"" + FMT.format(new Date()) + "\",");
        }
        msg.append("\"" + e.getSubject().replaceAll("\"", "\"\"") + "\",");
        msg.append("\"" + e.getLongDescription().replaceAll("\"", "\"\"") + "\"\n");
        try {
            Writer w = getWriter();
            w.append(msg.toString());
            w.flush();
            LOG.info("Alert for '" + e.getSubject() + "' event written successfully to '" + getTarget() + "'");
        } catch (IOException x) {
            LOG.error("IO Error", x);
            LOG.error("Unable to write alert for '" + e.getSubject() + "' event to '" + getTarget() + "'");
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((settings == null) ? 0 : settings.hashCode());
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
        if (!(obj instanceof CsvLogFileServer)) {
            return false;
        }
        CsvLogFileServer other = (CsvLogFileServer) obj;
        if (settings == null) {
            if (other.settings != null) {
                return false;
            }
        } else if (!settings.equals(other.settings)) {
            return false;
        }
        return true;
    }

    public void setSettings(NotificationServerSettings settings) {
        throw new UnsupportedOperationException("CSV log file settings cannot be modified!");
    }

    @Override
    public void destroy() {
        delete(settings);
        super.destroy();
    }
}

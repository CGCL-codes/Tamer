package com.w20e.socrates.process;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Check for model's from http source.
 * @author dokter
 */
public class HttpModelUpToDateCheck implements ModelUpToDateCheck {

    /**
   * Initialize this class' logging.
   */
    private static final Logger LOGGER = Logger.getLogger(HttpModelUpToDateCheck.class.getName());

    /**
   * Map for holding timestamps.
   */
    private Map<URL, String> timestamps;

    /**
   * Offset of time info.
   */
    private static final int OFFSET = 5;

    /**
   * Create a new check instance.
   */
    public HttpModelUpToDateCheck() {
        this.timestamps = new HashMap<URL, String>();
    }

    /**
   * Check whether the model is up to date.
   * @param id URL id of model to check.
   * @return whether the model is up to date or not.
   */
    public final boolean isUpToDate(final URL id) {
        String urlStr = id.toString();
        urlStr = urlStr.substring(0, urlStr.length() - OFFSET) + "dateLastChanged";
        StringWriter out = new StringWriter();
        String timestamp = "";
        LOGGER.finer("Using url " + urlStr);
        try {
            URL url = new URL(urlStr);
            InputStream in = new BufferedInputStream(url.openStream());
            int j;
            while ((j = in.read()) != -1) {
                out.write(j);
            }
            in.close();
            out.flush();
            out.close();
            timestamp = out.toString().trim();
            LOGGER.finer("Got timestamp for model " + id + ": " + timestamp);
            if (this.timestamps.containsKey(id)) {
                if (timestamp.compareTo(this.timestamps.get(id)) > 0) {
                    this.timestamps.put(id, timestamp);
                    return false;
                }
            } else {
                this.timestamps.put(id, timestamp);
            }
        } catch (Exception e) {
            LOGGER.warning("Couldn't fetch timestamp for model " + id);
        }
        return true;
    }
}

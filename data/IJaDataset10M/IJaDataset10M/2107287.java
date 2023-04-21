package org.tamacat.httpd.monitor;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.tamacat.log.Log;
import org.tamacat.log.LogFactory;

/**
 * <p>Thread of HTTP Monitor for back end server.
 *
 * @param <T> target of {@code HealthCheckSupport}.
 */
public class HttpMonitor<T> implements Runnable {

    static final Log LOG = LogFactory.getLog("Monitor");

    private MonitorConfig config;

    private T target;

    private MonitorEvent<T> checkTarget;

    private boolean isNormal = true;

    private boolean isStarted;

    public void setHealthCheckTarget(MonitorEvent<T> checkTarget) {
        this.checkTarget = checkTarget;
    }

    public void setTarget(T target) {
        this.target = target;
    }

    public void setMonitorConfig(MonitorConfig config) {
        this.config = config;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (isStarted) {
                    synchronized (checkTarget) {
                        boolean result = check();
                        if (isNormal == true && result == false) {
                            checkTarget.removeTarget(target);
                            isNormal = false;
                            LOG.warn("check: " + config.getUrl() + " is down.");
                        } else if (isNormal == false && result == true) {
                            checkTarget.addTarget(target);
                            isNormal = true;
                            LOG.warn("check: " + config.getUrl() + " is up.");
                        }
                    }
                }
                Thread.sleep(config.getInterval());
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
                isStarted = false;
            }
        }
    }

    protected boolean check() {
        if (config == null) return true;
        HttpClient client = new DefaultHttpClient();
        boolean result = false;
        try {
            HttpResponse response = client.execute(new HttpGet(config.getUrl()));
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = true;
            }
        } catch (Exception e) {
            if (isNormal) LOG.error(e.getMessage());
        }
        return result;
    }

    public boolean isNormal() {
        return isNormal;
    }

    public void startMonitor() {
        isStarted = true;
    }

    public void stopMonitor() {
        isStarted = false;
    }
}

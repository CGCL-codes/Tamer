package controler.http;

import java.lang.Thread.*;
import java.net.MalformedURLException;

/**
 * Warss performs http requests through a
 * defined number of thread to ensure speed
 * and flexibility.
 * This is the transparent interface used by the
 * rest of the program to queue requests and
 * retrieve results.
 * Note that HttpFetch let you do GET requests only.
 */
public class HttpFetch {

    private long msTimeout;

    private String str;

    private ParallelHttp httpRequestsManager;

    /**
     * constructor
     * 
     * @param str url to the object to retrieve over http
     * @throws MalformedURLException if the url is not rfc 1738 compliant
     */
    public HttpFetch(String str) throws MalformedURLException {
        this.str = str;
        msTimeout = -1;
        httpRequestsManager = ParallelHttp.getInstance();
        httpRequestsManager.addTask(str);
    }

    /**
     * set timeout to retrieve object.
     * The timeout doesn't set a value before which give up the request but only
     * infers on the getBytes() method.
     * So if the timeout is reached, you can retry later.
     *
     * @param milliseconds number of milliseconds
     */
    public void setTimeout(long milliseconds) {
        if (milliseconds > 0) {
            msTimeout = milliseconds;
        }
    }

    /**
     * get the results of the requests
     * 
     * @return the bytes retrieved at url
     * (or null if timeout was set and reached)
     */
    public byte[] getBytes() {
        long rebours;
        if (msTimeout < 0) {
            rebours = 1;
        } else {
            rebours = msTimeout;
        }
        while (!httpRequestsManager.containsURL(str) && rebours >= 0) {
            try {
                Thread.sleep(50);
                if (msTimeout > 0) {
                    rebours -= 50;
                }
            } catch (InterruptedException e) {
                return null;
            }
        }
        if (rebours < 0) {
            return null;
        }
        byte[] res = httpRequestsManager.getContent(str);
        httpRequestsManager.removeEndedRequest(str);
        return res;
    }

    /**
     * get the URL currently retrieved
     *
     * @return the URL
     */
    public String getTarget() {
        return str;
    }

    /**
     * disable the current instance
     */
    public void destroy() {
        httpRequestsManager.removeEndedRequest(str);
        msTimeout = -1;
        str = null;
        httpRequestsManager = null;
    }
}

package org.openremote.android.console.net;

import java.io.InputStream;
import org.apache.http.HttpResponse;

/**
 * This declares all callback methods which ORConnection would notify.
 * 
 * @author handy 2010-04-27
 *
 */
public interface ORConnectionDelegate {

    /** This callback method is called in ORConnection fail condition. */
    public void urlConnectionDidFailWithException(Exception e);

    /** This callback method is involked while ORConnection getting http response (not 200). */
    public void urlConnectionDidReceiveResponse(HttpResponse httpResponse);

    /** This callback method is called while ORConnection receiving data included in http response (200). */
    public void urlConnectionDidReceiveData(InputStream data);
}

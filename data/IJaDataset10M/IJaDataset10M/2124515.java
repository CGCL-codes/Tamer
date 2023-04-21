package org.mobicents.media.server.ctrl.rtsp;

import java.util.concurrent.Callable;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.rtsp.RtspHeaders;
import org.jboss.netty.handler.codec.rtsp.RtspMethods;
import org.jboss.netty.handler.codec.rtsp.RtspResponseStatuses;
import org.jboss.netty.handler.codec.rtsp.RtspVersions;

/**
 * 
 * @author amit bhayani
 *
 */
public class OptionsAction implements Callable<HttpResponse> {

    private RtspController rtspController = null;

    private HttpRequest request = null;

    public static final String OPTIONS = RtspMethods.DESCRIBE.getName() + ", " + RtspMethods.SETUP.getName() + ", " + RtspMethods.TEARDOWN.getName() + ", " + RtspMethods.PLAY.getName() + ", " + RtspMethods.OPTIONS.getName();

    public OptionsAction(RtspController rtspController, HttpRequest request) {
        this.rtspController = rtspController;
        this.request = request;
    }

    public HttpResponse call() throws Exception {
        HttpResponse response = new DefaultHttpResponse(RtspVersions.RTSP_1_0, RtspResponseStatuses.OK);
        response.setHeader(HttpHeaders.Names.SERVER, RtspController.SERVER);
        response.setHeader(RtspHeaders.Names.CSEQ, this.request.getHeader(RtspHeaders.Names.CSEQ));
        response.setHeader(RtspHeaders.Names.PUBLIC, OPTIONS);
        return response;
    }
}

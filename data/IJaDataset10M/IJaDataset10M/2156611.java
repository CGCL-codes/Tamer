package jy.jrtsp.util;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Jiang Ying
 *
 */
public class RtspUrl {

    private URI rtspUrl;

    public RtspUrl(String host, int port, String file) throws URISyntaxException {
        String asset = file.startsWith("/") ? file : "/".concat(file);
        rtspUrl = new URI("rtsp", null, host, port, asset, null, null);
    }

    public RtspUrl(String url) throws URISyntaxException {
        rtspUrl = new URI(url);
    }

    public String toString() {
        return rtspUrl == null ? null : rtspUrl.toString();
    }

    public String getHost() {
        return rtspUrl.getHost();
    }

    public int getPort() {
        return rtspUrl.getPort();
    }

    public String getAsset() {
        return rtspUrl.getPath();
    }

    /**
	 * @param args
	 * @throws URISyntaxException 
	 */
    public static void main(String[] args) throws URISyntaxException {
        RtspUrl url = new RtspUrl("10.10.10.1", 5555, "/wuji");
        System.out.println(url.toString());
    }
}

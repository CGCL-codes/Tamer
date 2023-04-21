package org.zkoss.zk.au.out;

import org.zkoss.zk.au.AuResponse;
import org.zkoss.zk.ui.util.DeferredValue;

/**
 * A response to ask the client to download the specified URI.
 *
 * <p>data[0]: the URL to download the file from.
 * 
 * @author tomyeh
 * @since 3.0.0
 */
public class AuDownload extends AuResponse {

    /** Constructs with an encoded URL.
	 * To work with WebSphere 5.1 and some other old server, it is
	 * suggested to use {@link #AuDownload(DeferredValue)} and
	 * then invoke encodeURL in the rendering phase.
	 * @param url the URI of the file to download, never null.
	 */
    public AuDownload(String url) {
        super("download", new String[] { url });
    }

    /** Constructs with a deferred URI.
	 * To work with WebSphere 5.1 and some other old server, it is
	 * suggested to use {@link #AuDownload(DeferredValue)} and
	 * then invoke encodeURL in the rendering phase.
	 *
	 * @param deferredURI the URI but the value will be evaluated
	 * later.
	 * @since 3.5.1
	 */
    public AuDownload(DeferredValue deferredURI) {
        super("download", deferredURI);
    }
}

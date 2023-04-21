package org.zkoss.zk.au;

/**
 * A response to ask the client to download the specified URI.
 *
 * <p>data[0]: the URL to download the file from.
 * 
 * @author tomyeh
 */
public class AuDownload extends AuResponse {

    /**
	 * @param url the URI of the file to download, never null.
	 */
    public AuDownload(String url) {
        super("download", new String[] { url });
    }
}

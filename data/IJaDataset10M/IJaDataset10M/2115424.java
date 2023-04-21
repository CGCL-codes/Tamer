package com.ryanm.sage.handlers;

import java.io.IOException;
import java.net.URLConnection;
import com.ryanm.sage.ProcessUtil;
import com.ryanm.sage.ProcessUtil.Listener;
import com.ryanm.sage.handlers.URLGrabber.ContentGrabber;

/**
 * @author ryanm
 */
public class TorrentGrabber extends ContentGrabber {

    /***/
    public TorrentGrabber() {
        super("application/x-bittorrent");
    }

    @Override
    public void handle(final URLConnection con) throws IOException {
        ProcessUtil.execute(true, null, null, "transmission-remote", "-a", con.getURL().toString());
    }

    @Override
    public String getStatus() {
        final StringBuilder sb = new StringBuilder();
        try {
            ProcessUtil.execute(true, new Listener() {

                @Override
                public void line(final String line) {
                    sb.append(line).append("\n");
                }
            }, null, "transmission-remote", "-l");
        } catch (final IOException ioe) {
            sb.append(ioe.getMessage());
        }
        return sb.toString();
    }
}

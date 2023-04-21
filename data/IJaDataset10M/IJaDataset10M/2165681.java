package com.socialnetworkshirts.twittershirts.dataaccess.spreadshirt.http;

import com.socialnetworkshirts.twittershirts.renderer.PNGRenderer;
import com.socialnetworkshirts.twittershirts.renderer.model.Svg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * @author mbs
 */
public class SvgUploadCommand extends AbstractHttpCallCommand {

    private static final Logger log = LoggerFactory.getLogger(StreamHttpCallCommand.class);

    public SvgUploadCommand(HttpUrlConnectionFactory connectionFactory, String url, HttpMethod method, HttpMethod tunneledMethod, boolean apiKeyProtected, boolean sessionIdProtected, boolean headerAllowed) {
        super(connectionFactory, url, method, tunneledMethod, apiKeyProtected, sessionIdProtected, headerAllowed);
    }

    @Override
    protected void readOutput(HttpURLConnection connection) throws Exception {
    }

    @Override
    protected void writeInput(HttpURLConnection connection) throws Exception {
        OutputStream out = null;
        try {
            out = connection.getOutputStream();
            new PNGRenderer().renderPNG((Svg) input, out);
        } finally {
            if (out != null) out.close();
        }
    }
}

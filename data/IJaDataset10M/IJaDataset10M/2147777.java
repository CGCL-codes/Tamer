package net.sourceforge.jfreeplayer.httpserver.service.freeplayer.action;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.jfreeplayer.httpserver.request.HttpRequest;
import net.sourceforge.jfreeplayer.httpserver.response.HttpResponse;
import net.sourceforge.jfreeplayer.httpserver.response.HttpResponse.ResponseCode;
import net.sourceforge.jfreeplayer.httpserver.util.Header;

public class DiskBrowserAction implements Action {

    public static final String ACTION_NAME = "/diskbrowser.do";

    public static final String FILE_PARAMETER = "file";

    protected static final String LOG_NAME = DiskBrowserAction.class.getName();

    protected static final Logger logger = Logger.getLogger(LOG_NAME);

    HttpRequest request;

    public DiskBrowserAction() {
    }

    public HttpResponse generateResponse() {
        final String METHOD = "generateResponse";
        logger.entering(LOG_NAME, METHOD);
        HttpResponse response = new HttpResponse();
        response.setCode(HttpResponse.ResponseCode.Ok);
        String currentParameter = request.getParameterValue(FILE_PARAMETER);
        File current;
        String parent;
        if (currentParameter == null) {
            logger.info("no parameter, starting initial navigation");
            current = new File("/");
        } else {
            try {
                currentParameter = URLDecoder.decode(currentParameter, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                logger.log(Level.SEVERE, "unknown encoding format", e);
                response.setCode(ResponseCode.ServerError);
                return response;
            }
            logger.info("Navigation from " + currentParameter);
            current = new File(currentParameter);
        }
        if (current.getParent() == null) {
            parent = "/index.html";
        } else {
            try {
                parent = ACTION_NAME + "?" + FILE_PARAMETER + "=" + URLEncoder.encode(current.getParent(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                logger.log(Level.SEVERE, "unknown encoding format", e);
                response.setCode(ResponseCode.ServerError);
                return response;
            }
        }
        response.addHeader(Header.CONTENT_TYPE, "text/html; charset=UTF-8");
        StringBuffer buffer = new StringBuffer();
        buffer.append("<html><body text=\"#f0f0f03f\" link=\"#0000FFFF\" alink=\"#0099FF3f\" vlink=\"#cc00003f\"><table>");
        buffer.append("<tr><td height=\"35\" width=\"40\">&nbsp;</td><td>&nbsp;</td></tr>");
        buffer.append("<tr><td width=\"35\">&nbsp;</td><td>");
        buffer.append("<A href=\"").append(parent).append("\">..</A>");
        buffer.append("</td></tr>");
        try {
            File[] files = current.listFiles();
            for (int i = 0; i < files.length; i++) {
                File tmp = files[i];
                buffer.append("<tr><td width=\"20\">&nbsp;</td><td>");
                buffer.append("<A href=\"").append(ACTION_NAME).append("?").append(FILE_PARAMETER).append("=").append(URLEncoder.encode(tmp.getAbsolutePath(), "UTF-8")).append("\">");
                buffer.append(tmp.getName()).append("</A>");
                buffer.append("</td></tr>");
            }
        } catch (UnsupportedEncodingException e) {
            logger.log(Level.SEVERE, "unknown encoding format", e);
            response.setCode(ResponseCode.ServerError);
            return response;
        }
        buffer.append("</body></html></table>");
        response.addContent(buffer.toString());
        logger.exiting(LOG_NAME, METHOD);
        return response;
    }

    public boolean match(HttpRequest message) {
        final String METHOD = "match";
        logger.entering(LOG_NAME, METHOD);
        boolean match = false;
        String url = message.getUrl();
        if (ACTION_NAME.equals(url)) {
            logger.fine("match");
            match = true;
        } else {
            logger.finest("not match for " + url);
            match = false;
        }
        logger.exiting(LOG_NAME, METHOD);
        return match;
    }

    public void setMessage(HttpRequest message) {
        this.request = message;
    }
}

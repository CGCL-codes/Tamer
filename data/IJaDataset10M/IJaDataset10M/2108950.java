package de.fhg.igd.semoa.web.servlet;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.FilteredImageSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Acme.JPM.Encoders.GifEncoder;
import de.fhg.igd.logging.LogLevel;
import de.fhg.igd.logging.Logger;
import de.fhg.igd.logging.LoggerFactory;
import de.fhg.igd.util.Cache;
import de.fhg.igd.util.MemoryCache;

public abstract class GifServlet extends HttpServlet {

    /**
     * The <code>Logger</code> instance for this class 
     */
    private static Logger log_ = LoggerFactory.getLogger("network/web/servlet");

    protected Color transparentColor_;

    protected Cache cache_;

    protected long getCacheLifespan() {
        return -1;
    }

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        long cacheLifespan = getCacheLifespan();
        if (cacheLifespan >= 0) {
            cache_ = new MemoryCache(cacheLifespan * 1000);
            log_.debug("cachesize=" + (cacheLifespan * 1000));
        }
    }

    private String getQueryString(HttpServletRequest request) {
        String queryString = request.getQueryString();
        if (queryString == null) {
            queryString = "(null queryString)";
        }
        return queryString;
    }

    protected void setTransparentColor(Color transparentColor) {
        transparentColor_ = transparentColor;
    }

    protected Color getTransparentColor() {
        return transparentColor_;
    }

    private void writeGif(HttpServletRequest request, HttpServletResponse response, Image image) throws IOException {
        OutputStream gout;
        OutputStream out;
        GifEncoder encoder;
        byte[] gif;
        gout = new ByteArrayOutputStream();
        if (transparentColor_ != null) {
            encoder = new GifEncoder(new FilteredImageSource(image.getSource(), new TransparentFilter(transparentColor_)), gout);
        } else {
            encoder = new GifEncoder(image, gout);
        }
        encoder.encode();
        gif = ((ByteArrayOutputStream) gout).toByteArray();
        log_.debug("Created new GIF: size=" + gif.length);
        response.setContentType("image/gif");
        response.setContentLength(gif.length);
        if (cache_ != null) {
            log_.debug("Caching new GIF");
            cache_.add(getQueryString(request), gif);
        }
        out = response.getOutputStream();
        log_.info("Sending new GIF to browser");
        out.write(gif, 0, gif.length);
        out.flush();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer;
        OutputStream out;
        Image image;
        byte[] gif;
        try {
            if (cache_ != null) {
                gif = (byte[]) cache_.get(getQueryString(request));
                if (gif != null && !imageHasChanged(request)) {
                    log_.debug("Got GIF from cache: size=" + gif.length);
                    response.setContentType("image/gif");
                    response.setContentLength(gif.length);
                    out = response.getOutputStream();
                    log_.info("Sending cached GIF to browser");
                    out.write(gif, 0, gif.length);
                    out.flush();
                    return;
                }
            }
            image = createImage(request);
            writeGif(request, response, image);
        } catch (Exception e) {
            response.setStatus(500);
            response.setContentType("text/html");
            try {
                writer = response.getWriter();
                writer.println("An exception was encountered!<br><pre>");
                writer.println(e);
                writer.println("</pre>");
                writer.flush();
            } catch (IOException ignore) {
                log_.caught(ignore);
            }
            log_.caught(LogLevel.ERROR, "An exception was encountered: " + e.toString(), e);
        }
    }

    protected abstract Image createImage(HttpServletRequest request);

    protected abstract boolean imageHasChanged(HttpServletRequest request);
}

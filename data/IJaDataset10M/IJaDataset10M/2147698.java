package org.htw.osgi.renderservice;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import javax.imageio.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.log4j.*;
import org.htw.osgi.renderservice.essentials.*;

/**
 * <p>Der RenderService erstellt anhand von den Parametern aus dem 
 * HTTP-Request ein PNG Image und schickt dieses an den Client
 * zurück. Welche Parameter auf welche Art auf das Image gerendert
 * werden, hängt von den Interceptoren ab, die im Dispatcher des
 * RenderServices registriert sind.</p>
 * 
 * @author  Benjamin Friedrich (<a href="mailto:benjamin_friedrich@gmx.de">mailto:benjamin_friedrich@gmx.de</a>)
 * @version 1.0  Juni 2009
 */
@SuppressWarnings("serial")
public final class RenderService extends HttpServlet implements IServiceFramework {

    private final IDispatcher dispatcher;

    private HttpServletResponse servletResponse;

    private boolean errorOccured;

    private static final String PARAM_WIDTH = "width";

    private static final String PARAM_HEIGHT = "height";

    private static final String IMG_CONT_TYPE = "image/png";

    private static final String TXT_CONT_TYPE = "text/html";

    private static final String IMG_TYPE = "PNG";

    private static final int DEFAULT_WIDTH = 100;

    private static final int DEFAULT_HEIGHT = 100;

    private static final Logger LOG = Logger.getLogger(RenderService.class);

    private static final String LOG_PROP = "/config/Logger.properties";

    /**
	   * Konfiguriert den Logger und initialisiert den Dispatcher für
	   * die Interceptoren.
	   */
    public RenderService() {
        LOG.debug("Konstruiere RenderService");
        final URL loggerProperties = this.getClass().getResource(LOG_PROP);
        PropertyConfigurator.configure(loggerProperties);
        this.dispatcher = new Dispatcher();
    }

    @Override
    public void registerInterceptor(final IInterceptor interceptor) {
        LOG.info("Registriere Interceptor " + interceptor);
        this.dispatcher.registerInterceptor(interceptor);
    }

    @Override
    public void unregisterInterceptor(final IInterceptor interceptor) {
        LOG.info("Entferne Interceptor " + interceptor + " von Dispatcher");
        this.dispatcher.unregisterInterceptor(interceptor);
    }

    /**
	   * Erstellt anhand des HTTP Requests ein Image und sendet dieses an den Requestor als Ergebnis seiner
	   * Anfrage zurück. Welche Informationen im Request abgearbeitet werden und wie diese sich auf das
	   * Ergebnis Image auswirken, hängt von den registrierten Interceptoren ab.
	   */
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        LOG.info("Starte Bearbeitung von Request " + req.getQueryString());
        this.servletResponse = resp;
        this.errorOccured = false;
        final BufferedImage image = this.prepareImage(req);
        this.render(image, req);
        if (this.errorOccured) {
            LOG.warn("Ein Fehler ist während der Bearbeiung des Requests aufgetreten!");
            return;
        }
        final ServletOutputStream out = this.servletResponse.getOutputStream();
        try {
            LOG.info("Sende angefordertes Bild an Requestor");
            resp.setContentType(IMG_CONT_TYPE);
            ImageIO.write(image, IMG_TYPE, out);
        } catch (Exception e) {
            this.signalError(e.getMessage());
        } finally {
            out.close();
            this.servletResponse = null;
        }
    }

    /**
	   * Bereitet das Grund-Image vor, dessen Graphics Kontext über den
	   * RenderServiceContext an die Interceptoren zur weiteren Verarbeitung
	   * übergeben wird.
	   * 
	   * @param   req Request aus dem die Breit und Höhe des Grund-Images 
	   * 		  extrahiert werden
	   * @return  Grund-Image
	   */
    private BufferedImage prepareImage(final HttpServletRequest req) {
        LOG.debug("Bereite Image vor");
        final String width = req.getParameter(PARAM_WIDTH);
        final String height = req.getParameter(PARAM_HEIGHT);
        final BufferedImage image = new BufferedImage(width != null ? Integer.parseInt(width) : DEFAULT_WIDTH, height != null ? Integer.parseInt(height) : DEFAULT_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        return image;
    }

    /**
	   * Startet das Rendering druch die registrierten Interceptoren.
	   * 
	   * @param image  Grund-Image des Graphic Kontext von den Interceptoren bearbeitet
	   *               wird
	   * @param req    Request der an die Interceptor zur Ermittlung der für sie relevanten
	   *               Informationen weitergegeben wird
	   */
    private void render(final BufferedImage image, final HttpServletRequest req) {
        LOG.debug("Erstelle Kontext Objekt für Interceptoren");
        final RenderServiceContext context = new RenderServiceContext(req);
        final Graphics2D g = (Graphics2D) image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        context.setGraphics(g);
        context.setRenderService(this);
        LOG.debug("Rufe Interceptoren zur Bearbeitung der Render Anfrage auf");
        this.dispatcher.dispatch(context);
    }

    @Override
    public void signalError(String message) {
        try {
            LOG.debug("Teile Requestor folgende Error-Meldung mit: " + message);
            this.dispatcher.interrupt();
            this.servletResponse.setContentType(TXT_CONT_TYPE);
            this.servletResponse.getWriter().println("<h3>" + message + "</h3>");
            this.errorOccured = true;
        } catch (Exception e) {
            LOG.fatal(e.getMessage());
        }
    }
}

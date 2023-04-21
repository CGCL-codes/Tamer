package cssvar.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import cssvar.CSSVarContext;
import cssvar.filter.CSSVarFilter;

/**
 * 
 * A stand-alone Servlet for handling requests for .css files containing CSSVar
 * syntax
 * 
 * Upon initialization, a context-wide CSS variable context is loaded from a
 * default properties file.
 * 
 * On a request, the target css file is found and run through a CSSVarFilter,
 * resolving all variable references and outputting valid CSS which can be
 * streamed back to the client.
 * 
 * @author Alex Wyler
 * 
 */
public class CSSVarServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public static final String GLOBAL_CONTEXT_KEY = "cssvar.global.context";

    private static final String DEFAULT_CONFIG_PATH = "/WEB-INF/classes/cssvar.properties";

    private static final String EXT_CACHED = ".filtered";

    private static final Logger LOG = Logger.getLogger(CSSVarServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext ctx = req.getSession().getServletContext();
        CSSVarContext globalCSSContext = (CSSVarContext) ctx.getAttribute(GLOBAL_CONTEXT_KEY);
        resp.setContentType("text/css");
        String targetFilepath = getTargetFilePath(req);
        File filteredFile = new File(targetFilepath + EXT_CACHED);
        long lastFilterTime = (filteredFile.exists()) ? filteredFile.lastModified() : 0;
        if (lastFilterTime < getLastModified(req)) {
            LOG.debug("No up-to-date, cached version of " + filteredFile.getName() + " can be found.  The input will be parsed and cached.");
            CSSVarFilter filter = new CSSVarFilter();
            filter.filterCSS(new File(targetFilepath), filteredFile, globalCSSContext);
            for (String error : filter.getErrors()) {
                LOG.error(error);
            }
        } else {
            LOG.debug("Return cached, up-to-date version of " + filteredFile.getName() + ".");
        }
        if (filteredFile.exists()) {
            Scanner parsedOutputScanner = new Scanner(filteredFile);
            while (parsedOutputScanner.hasNextLine()) {
                String line = parsedOutputScanner.nextLine();
                resp.getWriter().write(line + "\r\n");
            }
        }
    }

    /**
	 * Initializes the global CSSVar context from a default configuration file.
	 * 
	 * TODO: allow other configuration options
	 */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext ctx = config.getServletContext();
        File defaultConfig = new File(ctx.getRealPath(DEFAULT_CONFIG_PATH));
        LOG.info("Loading global CSSVar context");
        try {
            ctx.setAttribute(CSSVarServlet.GLOBAL_CONTEXT_KEY, new CSSVarContext(defaultConfig));
        } catch (Exception e) {
            LOG.warn("Unable to load default config file \"" + DEFAULT_CONFIG_PATH + "\". CSS files will start with an empty global var context.");
        }
    }

    /**
	 * Returns the time of the most recent modification to either the
	 * servlet-wide CSSVar Context or the requested CSS file.
	 * 
	 * This is used to properly return status '304' on conditional-get requests.
	 */
    @Override
    protected long getLastModified(HttpServletRequest req) {
        long currTime = new Date().getTime();
        File targetFile = new File(getTargetFilePath(req));
        long cssModified = targetFile.exists() ? targetFile.lastModified() : currTime;
        ServletContext ctx = req.getSession().getServletContext();
        CSSVarContext globalCSSContext = (CSSVarContext) ctx.getAttribute(GLOBAL_CONTEXT_KEY);
        long cssvarContextModified = globalCSSContext != null ? globalCSSContext.getLastInit() : currTime;
        return Math.max(cssModified, cssvarContextModified);
    }

    /**
	 * Parses the target css filepath from the requst.
	 */
    private String getTargetFilePath(HttpServletRequest req) {
        ServletContext ctx = req.getSession().getServletContext();
        String relativePath = req.getRequestURL().toString().split(ctx.getContextPath())[1];
        return ctx.getRealPath(relativePath);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}

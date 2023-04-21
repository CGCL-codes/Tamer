package net.openkoncept.vroom;

import net.openkoncept.vroom.bean.Document;
import net.openkoncept.vroom.bean.VroomConfig;
import net.openkoncept.vroom.util.VroomUtilities;
import net.openkoncept.vroom.util.servlet.StringServletResponse;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * This is one of the core classes of this framework and is majorly used by
 * VroomFilter.
 * </p>
 *
 * @author Farrukh Ijaz (ijazfx)
 */
public class VroomProcessor {

    private static VroomProcessor instance = null;

    protected FilterConfig filterConfig;

    protected VroomConfig vroomConfig;

    public void loadVroomConfig() {
        String configFilePath = filterConfig.getInitParameter("config-file");
        File configFile = new File(getRealPath(configFilePath));
        if (configFile.exists()) {
            vroomConfig = VroomConfig.loadFromXML(configFile.getAbsolutePath());
        }
    }

    public static VroomProcessor getInstance(FilterConfig config) {
        if (instance == null) {
            instance = new VroomProcessor();
            instance.filterConfig = config;
            instance.loadVroomConfig();
        }
        return instance;
    }

    @SuppressWarnings(value = "unchecked")
    public boolean process(HttpServletRequest request, StringServletResponse response) throws ServletException {
        boolean success = false;
        String pagePath = request.getServletPath();
        StringBuffer sourceCode = null;
        try {
            sourceCode = new StringBuffer(new String(response.getString()));
            Document document = VroomUtilities.loadPageProcessor(getVroomConfig(), pagePath);
            if (document != null) {
                sourceCode = VroomUtilities.processPageSourceCode(sourceCode, document, request);
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
                response.setHeader("Expires", sdf.format(new Date()) + " GMT");
                response.setHeader("Last-Modified", sdf.format(new Date()) + " GMT");
                response.setHeader("Cache-Control", "no-cache, must-revalidate");
                response.setHeader("Pragma", "no-cache");
                response.getResponse().setContentLength(sourceCode.length());
                response.getResponse().getWriter().write(sourceCode.toString());
                success = true;
            } else {
                response.getResponse().setContentLength(sourceCode.length());
                response.getResponse().getWriter().write(sourceCode.toString());
                success = false;
            }
        } catch (IOException ex) {
            throw new ServletException(ex);
        }
        return success;
    }

    public String getRealPath(String path) {
        return filterConfig.getServletContext().getRealPath(path);
    }

    public VroomConfig getVroomConfig() {
        return vroomConfig;
    }
}

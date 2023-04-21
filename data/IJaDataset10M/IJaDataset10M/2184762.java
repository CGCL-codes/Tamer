package org.gbif.ecatws.servlet;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TikaServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(TikaServlet.class);

    private static final String PAGE_PARAM = "url";

    private static final String CHARSET = "UTF-8";

    public static String para(HttpServletRequest req, String parameter, String defaultValue) {
        Map<String, String> paramLookup = new HashMap<String, String>();
        Enumeration paramNames = req.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String param = (String) paramNames.nextElement();
            paramLookup.put(param.toLowerCase(), param);
        }
        String normedParam = paramLookup.get(parameter.toLowerCase());
        String p = null;
        if (normedParam != null) {
            p = req.getParameter(normedParam);
            if (p != null) {
                p = p.trim();
            }
        }
        if (p == null) {
            return defaultValue;
        }
        return p;
    }

    public static Integer paraAsInt(HttpServletRequest req, String parameter, Integer defaultValue) {
        String idx = para(req, parameter, null);
        if (idx != null) {
            try {
                Integer id = Integer.parseInt(idx);
                if (id != null) {
                    return id;
                }
            } catch (NumberFormatException e) {
            }
        }
        return defaultValue;
    }

    public static URL paraAsUrl(HttpServletRequest req, String parameter) {
        String idx = para(req, parameter, null);
        URL url = null;
        if (idx != null) {
            try {
                url = new URL(idx);
            } catch (Exception e) {
            }
        }
        return url;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=" + CHARSET);
        resp.setCharacterEncoding(CHARSET);
        Writer writer = new OutputStreamWriter(resp.getOutputStream(), CHARSET);
        URL source = paraAsUrl(req, PAGE_PARAM);
        if (source == null) {
            writeResource(writer, "help.html");
            resp.flushBuffer();
            return;
        }
        log.info("Extracting " + source.toString());
        Metadata metadata = new Metadata();
        metadata.set(Metadata.RESOURCE_NAME_KEY, source.toString());
        try {
            ContentHandler textHandler = TikaSaxWrapper.getXhmlContentHandler(writer);
            Parser parser = new AutoDetectParser();
            ParseContext ctx = new ParseContext();
            InputStream input = source.openStream();
            parser.parse(input, textHandler, metadata, ctx);
            input.close();
        } catch (IOException e) {
            log.error("IOException", e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "The provided url couldnt be read: " + e.getMessage());
        } catch (SAXException e) {
            log.error("SAXException", e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "The provided document couldnt be converted: " + e.getMessage());
        } catch (TikaException e) {
            log.error("TikaException", e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "The provided document couldnt be converted: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unknown Exception", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unkown error: " + e.getMessage());
        } finally {
            resp.flushBuffer();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private void writeResource(Writer writer, String resourceName) throws IOException {
        InputStream helpStream = classpathStream(resourceName);
        Scanner scanner = new Scanner(helpStream, "UTF-8");
        try {
            while (scanner.hasNextLine()) {
                writer.append(scanner.nextLine());
            }
        } finally {
            scanner.close();
            writer.flush();
        }
    }

    private InputStream classpathStream(String path) {
        InputStream in = null;
        URL url = getClass().getClassLoader().getResource(path);
        if (url != null) {
            try {
                in = url.openStream();
            } catch (IOException e) {
                log.warn("Cant open classpath input stream " + path, e);
            }
        }
        return in;
    }
}

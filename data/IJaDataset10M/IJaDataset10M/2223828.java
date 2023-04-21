package lv.odylab.evedb.servlet;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import lv.odylab.evedb.domain.IdNotFoundException;
import lv.odylab.evedb.domain.NameNotFoundException;
import lv.odylab.evedb.domain.TooShortPartialNameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public abstract class EveDbServlet extends PicoServlet {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private MemcacheService memcacheService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        logger.info("Initializing servlet: {}", getClass().getSimpleName());
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long start = System.currentTimeMillis();
        String acceptHeader = req.getHeader("accept");
        logger.info("Request ip address: {}, accept header: {}", req.getRemoteAddr(), acceptHeader);
        String pathInfo = req.getPathInfo() == null ? null : req.getPathInfo().substring(1);
        resp.setCharacterEncoding("utf-8");
        try {
            writeResponse(decodeString(pathInfo), acceptHeader, resp);
        } catch (NumberFormatException e) {
            logger.warn(e.getMessage());
            resp.sendError(400, e.getMessage());
        } catch (IdNotFoundException e) {
            logger.warn(e.getMessage());
            resp.sendError(400, e.getMessage());
        } catch (NameNotFoundException e) {
            logger.warn(e.getMessage());
            resp.sendError(400, e.getMessage());
        } catch (TooShortPartialNameException e) {
            logger.warn(e.getMessage());
            resp.sendError(400, e.getMessage());
        } catch (Exception e) {
            logger.error("Internal error", e);
            resp.sendError(500);
        }
        Long end = System.currentTimeMillis();
        logger.info("Execution took {}ms", end - start);
    }

    protected abstract void writeResponse(String pathInfo, String acceptHeader, HttpServletResponse resp) throws IOException, JAXBException;

    protected MemcacheService getMemcacheService() {
        if (memcacheService == null) {
            memcacheService = MemcacheServiceFactory.getMemcacheService();
        }
        return memcacheService;
    }

    private String decodeString(String string) {
        if (string == null) {
            return null;
        }
        try {
            return URLDecoder.decode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}

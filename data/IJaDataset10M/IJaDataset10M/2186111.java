package org.owasp.oss.ca;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.Certificate;
import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.Base64;
import org.owasp.oss.httpserver.HttpHandlerException;
import org.owasp.oss.httpserver.HttpRequest;
import org.owasp.oss.httpserver.HttpResponse;
import org.owasp.oss.httpserver.OSSHttpServer;
import org.owasp.oss.httpserver.HttpResponse.ErrorType;
import org.owasp.oss.httpserver.HttpResponse.MimeType;
import org.owasp.oss.utils.Utils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * This class handles Certificate Signing Requests. 
 */
public class CsrHandler implements HttpHandler {

    private static Logger log = Logger.getLogger(CsrHandler.class);

    private static final String STATIC_FILES_PATH = "www";

    private static final String DEFAULT_FILE = "index.html";

    public void handle(HttpExchange exchange) throws IOException {
        try {
            log.info("http request received");
            HttpRequest req = HttpRequest.create(exchange);
            HttpResponse resp = HttpResponse.create(exchange);
            if (req.isPOST()) {
                byte[] bytesToSign = null;
                String byteToSignStr = req.getParameterValue("csr");
                if (byteToSignStr == null) bytesToSign = req.getBodyBytes(); else bytesToSign = byteToSignStr.getBytes();
                if (bytesToSign == null) throw new HttpHandlerException("CSR request empty");
                String path = req.getPath();
                CertificationAuthority ca = CertificationAuthority.getInstance();
                Certificate cert = ca.processCsr(new ByteArrayInputStream(bytesToSign), "user1");
                resp.send(new ByteArrayInputStream(ca.certificateToPEM(cert)), MimeType.TEXT);
            } else {
                HttpResponse.sendErrorPage(ErrorType.FORBIDDEN, exchange);
            }
        } catch (HttpHandlerException e) {
            log.error("Error during porcessing CSR", e);
            HttpResponse.sendErrorPage(ErrorType.SERVICE_UNAVAILABLE, exchange);
        } catch (Exception e) {
            log.error("Error during porcessing CSR", e);
            HttpResponse.sendErrorPage(ErrorType.SERVICE_UNAVAILABLE, exchange);
        }
    }
}

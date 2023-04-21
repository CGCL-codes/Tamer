package hu.gbalage.owlforms.xforms;

import hu.gbalage.owlforms.xforms.work.IOWLForms;
import hu.gbalage.owlforms.xforms.work.XFormsCache;
import java.io.IOException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * @author balage
 *
 */
public class SzoctamHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange arg0) throws IOException {
        arg0.sendResponseHeaders(200, 0);
        XMLHelper.serialize(XFormsCache.generateXForms(IOWLForms.Form_Szoctam, null), arg0.getResponseBody());
        arg0.close();
    }
}

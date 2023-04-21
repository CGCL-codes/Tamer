package org.openkonnect.interceptor.openbravo.PrinterReports;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.openbravo.base.secureApp.VariablesSecureApp;
import org.openbravo.erpReports.RptC_Invoice;
import org.openkonnect.api.OpenKonnectException;
import org.openkonnect.api.model.Document;
import org.openkonnect.api.model.OpenKonnectEvent;
import org.openkonnect.interceptor.openbravo.IInterceptorHttpServlet;
import org.openkonnect.interceptor.openbravo.InterceptorHttpServletResponse;
import org.openkonnect.interceptor.openbravo.InterceptorJBossESBService;
import org.openkonnect.interceptor.openbravo.InterceptorService;
import org.openkonnect.interceptor.openbravo.InterceptorStompService;

public class RptC_InvoiceInterceptor extends RptC_Invoice implements IInterceptorHttpServlet {

    private InterceptorService busService;

    private static final String docNamePrefix = "RptC_Invoice";

    public void init() throws ServletException {
        super.init();
        try {
            busService = setBusService("Stomp");
            busService.monitor("InterceptorService RptC_Invoice started...");
        } catch (OpenKonnectException o) {
            o.printStackTrace();
        }
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        InterceptorHttpServletResponse interceptorResponse = getInterceptorHttpServletResponse(response);
        super.service(request, interceptorResponse);
        busService.monitor("Response length is " + interceptorResponse.getBaos().size());
        OpenKonnectEvent oke = createOpenKonnectEvent(new VariablesSecureApp(request), interceptorResponse);
        busService.monitor("Base64 length is " + ((Document) oke).getDocument().length);
        try {
            busService.postOpenKonnectEvent(oke);
        } catch (OpenKonnectException o) {
            o.printStackTrace();
        } finally {
            try {
                interceptorResponse.getOutputStream().closeAll();
            } catch (IOException e) {
            }
        }
    }

    public OpenKonnectEvent createOpenKonnectEvent(VariablesSecureApp okSession, InterceptorHttpServletResponse interceptorResponse) {
        Document okd = new Document("create");
        String reposLoc = "/" + okSession.getUserOrg() + "/" + okSession.getUserClient() + "/" + docNamePrefix;
        String documentURI = null;
        String mimetype;
        if (-1 == interceptorResponse.getContentType().indexOf(";")) {
            mimetype = interceptorResponse.getContentType();
        } else {
            mimetype = interceptorResponse.getContentType().substring(0, interceptorResponse.getContentType().indexOf(";"));
        }
        busService.monitor("Content Type is " + mimetype);
        okd.setDocumentName(docNamePrefix + "." + mimetype.substring(mimetype.indexOf("/") + 1));
        busService.monitor("Document name is " + okd.getDocumentName());
        busService.monitor("Document repository URI " + reposLoc);
        busService.monitor("Encoding is " + interceptorResponse.getCharacterEncoding());
        okd.setContentType(mimetype);
        okd.setCharacterEncoding(interceptorResponse.getCharacterEncoding());
        okd.setDocumentURI(documentURI);
        okd.setRepositoryLocation(reposLoc);
        Base64 encoder = new Base64();
        okd.setDocument(encoder.encode(interceptorResponse.getStreamAsByteArray()));
        return okd;
    }

    public InterceptorService setBusService(String busName) throws OpenKonnectException {
        InterceptorService srv = null;
        if (busName.equalsIgnoreCase("Stomp")) {
            srv = new InterceptorStompService();
        } else if (busName.equalsIgnoreCase("JBossESB")) {
            srv = new InterceptorJBossESBService();
        } else {
            throw new OpenKonnectException("InterceptorService: no bus type defined");
        }
        return srv;
    }

    public InterceptorHttpServletResponse getInterceptorHttpServletResponse(HttpServletResponse response) {
        return new InterceptorHttpServletResponse(response);
    }
}

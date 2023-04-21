package au.edu.diasb.annotation.danno;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.view.AbstractView;
import au.edu.diasb.annotation.danno.model.AnnoteaTypeException;
import au.edu.diasb.annotation.danno.model.HTTPMessage;
import au.edu.diasb.annotation.danno.model.RDFContainer;

/**
 * This view class renders an Annotea protocol response message.
 * 
 * @author scrawley
 */
public class AnnoteaResponseView extends AbstractView {

    @SuppressWarnings("unchecked")
    @Override
    protected void renderMergedOutputModel(Map map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RDFContainer responseRDF = (RDFContainer) map.get(AnnoteaResponse.RESPONSE_RDF_KEY);
        boolean isHttpMessage = (Boolean) map.get(AnnoteaResponse.HTTP_MESSAGE_KEY);
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            if (responseRDF != null) {
                if (isHttpMessage) {
                    unwrapHttpMessage(response, os, responseRDF);
                } else {
                    response.setContentType("application/xml+rdf");
                    responseRDF.serialize(os, RDFContainer.XML_RDF);
                }
            }
            os.flush();
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    private void unwrapHttpMessage(HttpServletResponse response, OutputStream os, RDFContainer responseRDF) throws AnnoteaTypeException, IOException {
        HTTPMessage resource = (HTTPMessage) responseRDF.getResource();
        String contentType = resource.getContentType();
        String body = resource.getBody();
        if (contentType != null) {
            contentType = contentType.trim();
            if (contentType.length() > 0) {
                response.setContentType(contentType);
            }
        }
        os.write(body.getBytes());
    }
}

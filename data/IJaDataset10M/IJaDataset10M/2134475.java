package at.riemers.zero.base.spring;

import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.view.AbstractView;
import org.w3c.dom.Document;

/**
 *
 * @author tobias
 */
public class XmlView extends AbstractView {

    private static final String CONTENT_TYPE = "text/xml";

    private static final Logger log = Logger.getLogger(XmlView.class);

    private Document document;

    public XmlView(Document document) {
        this.document = document;
    }

    @Override
    protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(CONTENT_TYPE);
        ServletOutputStream out = response.getOutputStream();
        javax.xml.transform.TransformerFactory tFactory = javax.xml.transform.TransformerFactory.newInstance();
        javax.xml.transform.Transformer transformer = tFactory.newTransformer();
        javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(document);
        javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(out);
        transformer.transform(source, result);
        out.flush();
    }
}

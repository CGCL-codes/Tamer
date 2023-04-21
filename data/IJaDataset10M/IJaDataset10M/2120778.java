package mecca.app;

import mecca.portal.XMLTransformer;
import org.apache.velocity.Template;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class XMLModule extends mecca.portal.velocity.VTemplate implements mecca.portal.XMLContainer {

    private String strUrl = "";

    private String strXsl = "";

    public void setXml(String strUrl) {
        this.strUrl = strUrl;
    }

    public void setXsl(String strXsl) {
        this.strXsl = strXsl;
    }

    public Template doTemplate() throws Exception {
        javax.servlet.http.HttpSession session = request.getSession();
        String s = XMLTransformer.transform(strXsl, strUrl);
        context.put("xmlcontent", s);
        Template template = engine.getTemplate("vtl/custom/xml.vm");
        return template;
    }
}

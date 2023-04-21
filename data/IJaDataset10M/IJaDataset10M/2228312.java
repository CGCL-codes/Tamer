package cn.vlabs.duckling.dct.services.dml.html2dml;

import java.io.IOException;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * Introduction Here.
 * @date 2010-3-8
 * @author 狄 diyanliang@cnic.cn
 */
public class ParseHtmlStrong extends AbstractParseHtmlElement {

    public ParseHtmlStrong() {
    }

    @Override
    public void printAttribute(Element e, Html2DmlEngine html2dmlengine) {
    }

    @Override
    public void printElement(Element e, Html2DmlEngine html2dmlengine) {
        html2dmlengine.getM_out().print("<strong");
        printAttribute(e, html2dmlengine);
        if (html2dmlengine.getPreType() > 0) {
            html2dmlengine.getM_out().print(">");
        } else {
            html2dmlengine.getM_out().println(">");
        }
        try {
            h2d.getChildren(e, html2dmlengine);
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (JDOMException e1) {
            e1.printStackTrace();
        }
        if (html2dmlengine.getPreType() > 0) {
            html2dmlengine.getM_out().print("</strong>");
        } else {
            html2dmlengine.getM_out().println("</strong>");
        }
    }
}

package cn.vlabs.duckling.dct.services.dml.html2dml;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * Introduction Here.
 * @date 2010-3-8
 * @author 狄 diyanliang@cnic.cn
 */
public class ParseHtmlPre extends AbstractParseHtmlElement {

    public ParseHtmlPre() {
    }

    @Override
    public void printAttribute(Element e, Html2DmlEngine html2dmlengine) {
        Map map = new ForgetNullValuesLinkedHashMap();
        map.put("style", e.getAttributeValue("style"));
        map.put("class", e.getAttributeValue("class"));
        map.put("width", e.getAttributeValue("width"));
        if (map.size() > 0) {
            for (Iterator ito = map.entrySet().iterator(); ito.hasNext(); ) {
                Map.Entry entry = (Map.Entry) ito.next();
                if (!entry.getValue().equals("")) {
                    html2dmlengine.getM_out().print(" " + entry.getKey() + "=\"" + entry.getValue() + "\"");
                }
            }
        }
    }

    @Override
    public void printElement(Element e, Html2DmlEngine html2dmlengine) {
        html2dmlengine.setPreType(html2dmlengine.getPreType() + 1);
        html2dmlengine.getM_out().print("<pre");
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
        html2dmlengine.setPreType(html2dmlengine.getPreType() - 1);
        if (html2dmlengine.getPreType() > 0) {
            html2dmlengine.getM_out().print("</pre>");
        } else {
            html2dmlengine.getM_out().println("</pre>");
        }
    }
}

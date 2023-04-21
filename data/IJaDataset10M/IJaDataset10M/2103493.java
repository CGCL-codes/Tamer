package org.wikiup.romulan.servlet.processor;

import java.util.Map;
import org.wikiup.core.inf.Document;
import org.wikiup.core.util.Documents;
import org.wikiup.servlet.ServletProcessorContext;
import org.wikiup.servlet.impl.processor.xml.XMLServletProcessor;
import org.wikiup.servlet.inf.ServletProcessor;

public class ProtalRefreshServletProcessor implements ServletProcessor {

    public void process(ServletProcessorContext context) {
        Document response = context.getResponseXML();
        Map<String, String> pam = (Map<String, String>) context.getAttribute("protal:pam");
        if (pam != null) {
            String param = context.getParameter("pid", null);
            String[] pids = param != null ? param.split(",") : pam.keySet().toArray(new String[pam.size()]);
            for (String pid : pids) {
                Document node = response.addChild("inner-html");
                Documents.setAttributeValue(node, "id", pid);
                node.setObject("<![CDATA[\n" + pam.get(pid) + "\n]]>");
            }
        }
        Documents.setChildValue(response, "result", "0");
        XMLServletProcessor p = new XMLServletProcessor();
        p.setEncode(false);
        p.process(context);
    }
}

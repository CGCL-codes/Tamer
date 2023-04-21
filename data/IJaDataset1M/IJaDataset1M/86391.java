package org.xfap.ams;

import java.io.*;
import java.util.*;
import org.kxml.*;
import org.kxml.io.*;
import org.kxml.kdom.*;
import org.xfap.*;
import org.xfap.sl.*;
import org.xfap.html.*;

public class Ams extends DefaultAgent {

    public void init(Platform platform, String[] args) {
        if (args.length != 0) throw new RuntimeException("Ams does not expect any arguments!");
        registerAms(platform, "ams");
    }

    /** Generates an HTML presentation of this AMS */
    protected void generateHtml(XmlWriter xw, Element http) throws IOException {
        String servletPath = http.getElement("servletPath").getText();
        String agentPath = http.getElement("agentPath").getText();
        String pathInfo = http.getElement("pathInfo").getText();
        generateHtmlHeader(xw, null, "Agent Management System (AMS)");
        if (pathInfo.length() <= 1) {
            xw.startTag("h2");
            xw.write("Registered Agents (White Pages)");
            xw.endTag();
            xw.startTag("table");
            xw.attribute("border", "true");
            xw.startTag("th");
            xw.write("Agent");
            xw.endTag();
            xw.startTag("th");
            xw.write("Addresses");
            xw.endTag();
            for (Enumeration e = platform.getAmsAgentDescriptions().elements(); e.hasMoreElements(); ) {
                AmsAgentDescription amsAD = (AmsAgentDescription) e.nextElement();
                xw.startTag("tr");
                xw.startTag("td");
                xw.startTag("a");
                xw.attribute("href", servletPath + "/" + amsAD.getName().getName());
                xw.write(amsAD.getName().getName());
                xw.endTag();
                xw.endTag();
                xw.startTag("td");
                Vector addresses = amsAD.getName().getAddresses();
                for (int i = 0; i < addresses.size(); i++) xw.write((String) addresses.elementAt(i));
                xw.endTag();
                xw.endTag();
            }
            xw.endTag();
            xw.startTag("h2");
            xw.write("Ap-Description");
            xw.endTag();
            xw.startTag("pre");
            xw.attribute("xml:space", "preserve");
            xw.write(getApDescription().toString());
            xw.endTag();
        }
        xw.startTag("h2");
        xw.write("Message Log");
        xw.endTag();
        HtmlGenerator hg = new HtmlGenerator(xw, agentPath);
        hg.write(platform.getMessageLog(), pathInfo);
        generateHtmlFooter(xw);
    }

    /** handles a message with known object serialization */
    public void processObjectMessage(Message message, Message reply, Object content) throws IOException {
        if (content instanceof Action && (((Action) content).action instanceof GetDescription)) {
            reply.setContent("(" + getApDescription() + ")");
            send(reply);
        } else super.processObjectMessage(message, reply, content);
    }

    public String getApDescription() {
        StringBuffer buf = new StringBuffer();
        buf.append("(ap-description \r\n" + " :name " + platform.getName() + "\r\n" + " :dynamic false \r\n" + " :mobility false \r\n" + " :transport-profile \r\n" + "  (ap-transport-description \r\n" + "   :available-mtps (set \r\n" + "    (mtp-description \r\n" + "      :mtp-name fipa.mts.mtp.http.std \r\n" + "      :addresses (sequence");
        for (Enumeration e = platform.getAccs().elements(); e.hasMoreElements(); ) {
            AccInterface ai = (AccInterface) e.nextElement();
            buf.append(" ");
            buf.append(ai.getAddress());
        }
        buf.append(")))))");
        return buf.toString();
    }
}

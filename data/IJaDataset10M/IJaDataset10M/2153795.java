package jimo.impl.common;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UtilXMLWriter {

    private UtilXMLWriter() {
    }

    public static void XMLWriterFactory(String typeName, String xmlFilename, Object entries) {
        UtilXMLWriter writer = new UtilXMLWriter();
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(xmlFilename));
            out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            if (typeName.equalsIgnoreCase("knowledgebase")) {
                out.write(writer.buildKnowledgeBase(xmlFilename, (List) entries));
            } else if (typeName.equalsIgnoreCase("messagebase")) {
                out.write(writer.buildMessageBase(xmlFilename, (List) entries));
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final String buildKnowledgeBase(String xmlFilename, List entryList) {
        StringBuffer sb = new StringBuffer();
        sb.append("<knowledgebase>\n");
        for (Iterator elIter = entryList.iterator(); elIter.hasNext(); ) {
            Map entry = (Map) elIter.next();
            if (entry.get("refkey") != null && !entry.get("refkey").equals("")) {
                sb.append("    <entry key=\"" + entry.get("key") + "\" refkey=\"" + entry.get("refkey") + "\">\n");
            } else {
                sb.append("    <entry key=\"" + entry.get("key") + "\">\n");
            }
            for (Iterator eIter = ((List) entry.get("value")).iterator(); eIter.hasNext(); ) {
                sb.append("        <value>" + eIter.next() + "</value>\n");
            }
            sb.append("    </entry>\n");
        }
        sb.append("</knowledgebase>\n");
        return sb.toString();
    }

    private final String buildMessageBase(String xmlFilename, List entryList) {
        StringBuffer sb = new StringBuffer();
        sb.append("<messagebase>\n");
        if (entryList != null) {
            for (Iterator iter = entryList.iterator(); iter.hasNext(); ) {
                Map entry = (Map) iter.next();
                sb.append("    <entry from=\"" + entry.get("from") + "\" status=\"" + "unread" + "\" timestamp=\"" + entry.get("timestamp") + "\">" + entry.get("message") + "</entry>\n");
            }
            sb.append("</messagebase>\n");
        }
        return sb.toString();
    }
}

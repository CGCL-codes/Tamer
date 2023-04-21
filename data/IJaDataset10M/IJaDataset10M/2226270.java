package com.loribel.commons.xml.csv;

import java.io.IOException;
import java.io.Writer;
import org.w3c.dom.Node;
import com.loribel.commons.exception.GB_XmlException;
import com.loribel.commons.xml.GB_XmlXPathTools;

/**
 * Writer for CSV file from XML Node.
 * <pre>
 * </pre>
 */
public class GB_XmlTxtWriter extends GB_Xml2TxtWriter {

    public GB_XmlTxtWriter(Writer a_writer) {
        super(a_writer);
    }

    protected String buildBloc(Node a_node) throws GB_XmlException {
        StringBuffer retour = new StringBuffer();
        int len = items.size();
        String l_xPath;
        for (int i = 0; i < len; i = i + 2) {
            String l_label = (String) items.get(i);
            l_xPath = (String) items.get(i + 1);
            String l_value = GB_XmlXPathTools.getXPathValueStr(a_node, l_xPath, "");
            if (i == 1) {
                verbose("    item: " + l_value);
            }
            retour.append("#" + l_label + " : " + l_value + LN);
        }
        return retour.toString();
    }

    public void writeHeader() throws IOException {
        writer.write("HEADER" + LN + LN);
    }

    public String writeNode(Node a_node) throws IOException, GB_XmlException {
        String retour = buildBloc(a_node);
        writer.write(retour + LN + LN);
        return retour;
    }
}

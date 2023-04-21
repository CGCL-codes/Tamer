package de.tuc.in.sse.weit.export.ootrans.transform;

import java.io.File;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import de.tuc.in.sse.weit.export.odtreport.MavenProperties;
import de.tuc.in.sse.weit.export.ootrans.xml.XMLInserter;

public class VMXTVersionHandler implements TransformCmdHandler {

    private String VERSION = new MavenProperties("com.foursoft.fourever", "fourever-vmodell").getVersionNumber() + " (fourever-vmodell)";

    private String cmdVersion = null;

    public XMLInserter handleCmd(Node cmd, XMLInserter insert, Transformer transformer, boolean lastCmd) {
        if (cmd.getNodeType() == Node.ELEMENT_NODE && cmd.getNodeName().equals(cmdVersion)) {
            insert.insertNewText(VERSION);
            return insert;
        }
        return null;
    }

    public void loadConfig(Element node, File parentPath) {
        cmdVersion = node.getAttribute("tag");
        if (cmdVersion == null || cmdVersion.equals("")) Logger.getLogger(getClass()).warn("Version handler configuration: Missing tag name!");
    }

    public void postprocess(Document doc, Object userData) {
    }

    public void preprocess(Document doc, Object userData) {
    }
}

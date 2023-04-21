package jmri.jmrit.display.layoutEditor;

import jmri.jmrit.XmlFile;
import java.io.File;
import jmri.BlockManager;
import jmri.Block;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Attribute;

/**
 * Handle saving/restoring block value information to XML files.
 * This class manipulates files conforming to the block_value DTD.
 *
 * @author      Dave Duchamp Copyright (C) 2008
 * @version     $Revision: 1.2 $
 */
public class BlockValueFile extends jmri.jmrit.XmlFile {

    public BlockValueFile() {
        super();
        blockManager = jmri.InstanceManager.blockManagerInstance();
    }

    private BlockManager blockManager = null;

    private static String defaultFileName = XmlFile.prefsDir() + "blockvalues.xml";

    private Document doc = null;

    private Element root = null;

    @SuppressWarnings("unchecked")
    public void readBlockValues() throws org.jdom.JDOMException, java.io.IOException {
        log.debug("entered readBlockValues");
        List<String> blocks = blockManager.getSystemNameList();
        if (checkFile(defaultFileName)) {
            root = rootFromName(defaultFileName);
            if ((root != null) && (blocks != null) && (blocks.size() > 0)) {
                Element blockvalues = root.getChild("blockvalues");
                if (blockvalues != null) {
                    List<Element> blockList = blockvalues.getChildren("block");
                    for (int i = 0; i < blockList.size(); i++) {
                        if ((blockList.get(i)).getAttribute("systemname") == null) {
                            log.warn("unexpected null in systemName " + blockList.get(i) + " " + blockList.get(i).getAttributes());
                            break;
                        }
                        String sysName = blockList.get(i).getAttribute("systemname").getValue();
                        Block b = blockManager.getBySystemName(sysName);
                        if (b != null) {
                            String v = blockList.get(i).getAttribute("value").getValue();
                            b.setValue(v);
                            int dd = jmri.Path.NONE;
                            Attribute a = blockList.get(i).getAttribute("dir");
                            if (a != null) {
                                try {
                                    dd = a.getIntValue();
                                } catch (org.jdom.DataConversionException e) {
                                    log.error("failed to convert direction attribute");
                                }
                            }
                            b.setDirection(dd);
                        }
                    }
                }
            }
        }
    }

    public void writeBlockValues() throws java.io.IOException {
        log.debug("entered writeBlockValues");
        List<String> blocks = blockManager.getSystemNameList();
        if (blocks.size() > 0) {
            root = new Element("block_values");
            doc = newDocument(root, dtdLocation + "block-values.dtd");
            boolean valuesFound = false;
            java.util.Map<String, String> m = new java.util.HashMap<String, String>();
            m.put("type", "text/xsl");
            m.put("href", xsltLocation + "blockValues.xsl");
            org.jdom.ProcessingInstruction p = new org.jdom.ProcessingInstruction("xml-stylesheet", m);
            doc.addContent(0, p);
            Element values = new Element("blockvalues");
            for (int i = 0; i < blocks.size(); i++) {
                String sname = blocks.get(i);
                Block b = blockManager.getBySystemName(sname);
                if (b != null) {
                    Object o = b.getValue();
                    if (o != null) {
                        Element val = new Element("block");
                        val.setAttribute("systemname", sname);
                        val.setAttribute("value", o.toString());
                        int v = b.getDirection();
                        if (v != jmri.Path.NONE) val.setAttribute("dir", "" + v);
                        values.addContent(val);
                        valuesFound = true;
                    }
                } else log.error("Block " + sname + " was not found.");
            }
            root.addContent(values);
            if (valuesFound) {
                try {
                    if (!checkFile(defaultFileName)) {
                        File file = new File(defaultFileName);
                        if (!file.createNewFile()) log.error("createNewFile failed");
                    }
                    writeXML(findFile(defaultFileName), doc);
                } catch (java.io.IOException ioe) {
                    log.error("IO Exception " + ioe);
                    throw (ioe);
                }
            }
        }
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BlockValueFile.class.getName());
}

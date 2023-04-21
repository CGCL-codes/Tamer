package gpsxml.io;

import gpsxml.MenuTreeNode;
import gpsxml.TagTreeNodeInterface;
import gpsxml.xml.EntrySet;
import gpsxml.xml.EntryTag;
import gpsxml.xml.MenuTag;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 *  Handles IO for menu files.
 * @author PLAYER, Keith Ralph
 */
public class MenuFileIO {

    private static final String SERVICEMAPPINGCONFIG = "ServiceMappingConfig";

    private static final String NAME = "name";

    private static final String CONTROL = "Control";

    private static final String GROUP = "Group";

    private static final String PROJECTS = "projects";

    private static final String ENTRYSET = "entryset";

    private static final String ENTRY = "entry";

    private static final String MENU = "menu";

    private static final String SOURCE = "src";

    private static final String URL = "url";

    private static final String TARGET = "target";

    private static final String USEPARAM = "useParam";

    private static final String MAPPING = "Mapping";

    private static final String TAGCLOSE = ">";

    private static final String STARTTAGOPEN = "<";

    private static final String ENDTAGOPEN = "</";

    private static final char[] INDENT = "\n         ".toCharArray();

    /** Creates a new instance of MenuFileIO */
    public MenuFileIO() {
    }

    /** Parses in the specified file and returns a TagTreeNodeInterface object.
     *
     *  
     *
     **/
    public TagTreeNodeInterface parseXML(String filePath) throws IOException, ParserConfigurationException, SAXException {
        File file = new File(filePath);
        SAXMenuParser handler;
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        handler = new SAXMenuParser();
        SAXParser saxParser = parserFactory.newSAXParser();
        saxParser.parse(file, handler);
        return handler.getInformation();
    }

    public static void writeXML(TagTreeNodeInterface treeNode, String filePath) throws Exception {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filePath);
            PrintWriter printWriter = new PrintWriter(fileOutputStream);
            StreamResult streamResult = new StreamResult(printWriter);
            SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
            TransformerHandler hd = tf.newTransformerHandler();
            Transformer serializer = hd.getTransformer();
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            hd.setResult(streamResult);
            hd.startDocument();
            AttributesImpl atts = new AttributesImpl();
            hd.startElement("", "", SERVICEMAPPINGCONFIG, atts);
            List<TagTreeNodeInterface> treeNodeList = treeNode.getChildList();
            for (TagTreeNodeInterface entrySet : treeNodeList) {
                EntrySet castEntrySet = (EntrySet) entrySet;
                String controlGroup = castEntrySet.getControlGroup();
                writeControlGroupStart(hd, atts, controlGroup);
                atts.clear();
                atts.addAttribute("", "", NAME, "CDATA", entrySet.getName());
                hd.startElement("", "", ENTRYSET, atts);
                if (castEntrySet.isProxyUsed() == true) {
                    atts.clear();
                    atts.addAttribute("", "", URL, "CDATA", castEntrySet.getSource());
                    hd.startElement("", "", MAPPING, atts);
                    hd.endElement("", "", MAPPING);
                }
                writeEntry(hd, entrySet);
                hd.endElement("", "", ENTRYSET);
                writeControlGroupEnd(hd, atts, controlGroup);
            }
            hd.endElement("", "", SERVICEMAPPINGCONFIG);
            hd.endDocument();
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }

    private static void writeEntry(TransformerHandler hd, TagTreeNodeInterface entrySet) throws Exception {
        AttributesImpl atts = new AttributesImpl();
        List<TagTreeNodeInterface> entryList = entrySet.getChildList();
        EntryTag entryTag;
        if (entryList == null) {
            return;
        }
        for (TagTreeNodeInterface entry : entryList) {
            MenuTreeNode menuTreeNode = (MenuTreeNode) entry;
            String controlGroup = menuTreeNode.getControlGroup();
            writeControlGroupStart(hd, atts, controlGroup);
            atts.clear();
            atts.addAttribute("", "", NAME, "CDATA", entry.getName());
            entryTag = (EntryTag) entry.getTagInterface();
            if (entryTag.isProxyUsed() == false) {
                if (entryTag.getSource() != null) {
                    atts.addAttribute("", "", SOURCE, "CDATA", entryTag.getSource());
                }
            }
            if (entryTag.getTarget() != null) {
                atts.addAttribute("", "", TARGET, "CDATA", entryTag.getTarget());
            }
            if (entryTag.isParamUsed() != null) {
                atts.addAttribute("", "", USEPARAM, "CDATA", entryTag.isParamUsed());
            }
            hd.startElement("", "", ENTRY, atts);
            if (entryTag.isProxyUsed() == true) {
                atts.clear();
                atts.addAttribute("", "", URL, "CDATA", entryTag.getSource());
                hd.startElement("", "", MAPPING, atts);
                hd.endElement("", "", MAPPING);
            }
            writeMenu(hd, entry);
            hd.endElement("", "", ENTRY);
            writeControlGroupEnd(hd, atts, controlGroup);
        }
    }

    private static void writeControlGroupStart(TransformerHandler hd, AttributesImpl atts, String controlGroup) throws Exception {
        if (controlGroup != null) {
            atts.clear();
            hd.startElement("", "", CONTROL, atts);
            atts.addAttribute("", "", PROJECTS, "CDATA", controlGroup);
            hd.startElement("", "", GROUP, atts);
        }
    }

    private static void writeControlGroupEnd(TransformerHandler hd, AttributesImpl atts, String controlGroup) throws Exception {
        if (controlGroup != null) {
            atts.clear();
            hd.endElement("", "", GROUP);
            hd.endElement("", "", CONTROL);
        }
    }

    private static void writeMenu(TransformerHandler hd, TagTreeNodeInterface entry) throws Exception {
        AttributesImpl atts = new AttributesImpl();
        List<TagTreeNodeInterface> menuList = entry.getChildList();
        MenuTag menuTag;
        if (menuList == null) {
            return;
        }
        for (TagTreeNodeInterface menu : menuList) {
            atts.clear();
            atts.addAttribute("", "", NAME, "CDATA", menu.getName());
            menuTag = (MenuTag) menu.getTagInterface();
            if (menuTag.isProxyUsed() == false) {
                if (menuTag.getSource() != null) {
                    atts.addAttribute("", "", SOURCE, "CDATA", menuTag.getSource());
                }
            }
            if (menuTag.getTarget() != null) {
                atts.addAttribute("", "", TARGET, "CDATA", menuTag.getTarget());
            }
            if (menuTag.isParamUsed() != null) {
                atts.addAttribute("", "", USEPARAM, "CDATA", menuTag.isParamUsed());
            }
            hd.startElement("", "", MENU, atts);
            if (menuTag.isProxyUsed() == true) {
                atts.clear();
                atts.addAttribute("", "", URL, "CDATA", menuTag.getSource());
                hd.startElement("", "", MAPPING, atts);
                hd.endElement("", "", MAPPING);
            }
            hd.endElement("", "", MENU);
        }
    }
}

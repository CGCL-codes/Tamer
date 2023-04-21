package net.jforum.util.bbcode;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import net.jforum.exceptions.ForumException;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Rafael Steil
 * @version $Id: BBCodeHandler.java,v 1.19 2007/07/28 14:17:09 rafaelsteil Exp $
 */
public class BBCodeHandler extends DefaultHandler implements Serializable {

    private Map bbMap = new LinkedHashMap();

    private Map alwaysProcessMap = new LinkedHashMap();

    private String tagName = "";

    private StringBuffer sb;

    private BBCode bb;

    public BBCodeHandler() {
    }

    public BBCodeHandler parse() {
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            BBCodeHandler bbParser = new BBCodeHandler();
            String path = SystemGlobals.getValue(ConfigKeys.CONFIG_DIR) + "/bb_config.xml";
            File fileInput = new File(path);
            if (fileInput.exists()) {
                parser.parse(fileInput, bbParser);
            } else {
                InputSource input = new InputSource(path);
                parser.parse(input, bbParser);
            }
            return bbParser;
        } catch (Exception e) {
            throw new ForumException(e);
        }
    }

    public void addBb(BBCode bb) {
        if (bb.alwaysProcess()) {
            this.alwaysProcessMap.put(bb.getTagName(), bb);
        } else {
            this.bbMap.put(bb.getTagName(), bb);
        }
    }

    public Collection getBbList() {
        return this.bbMap.values();
    }

    public Collection getAlwaysProcessList() {
        return this.alwaysProcessMap.values();
    }

    public BBCode findByName(String tagName) {
        return (BBCode) this.bbMap.get(tagName);
    }

    public void startElement(String uri, String localName, String tag, Attributes attrs) {
        if (tag.equals("match")) {
            this.sb = new StringBuffer();
            this.bb = new BBCode();
            String tagName = attrs.getValue("name");
            if (tagName != null) {
                this.bb.setTagName(tagName);
            }
            String removeQuotes = attrs.getValue("removeQuotes");
            if (removeQuotes != null && removeQuotes.equals("true")) {
                this.bb.enableRemoveQuotes();
            }
            String alwaysProcess = attrs.getValue("alwaysProcess");
            if (alwaysProcess != null && "true".equals(alwaysProcess)) {
                this.bb.enableAlwaysProcess();
            }
        }
        this.tagName = tag;
    }

    public void endElement(String uri, String localName, String tag) {
        if (tag.equals("match")) {
            this.addBb(this.bb);
        } else if (this.tagName.equals("replace")) {
            this.bb.setReplace(this.sb.toString().trim());
            this.sb.delete(0, this.sb.length());
        } else if (this.tagName.equals("regex")) {
            this.bb.setRegex(this.sb.toString().trim());
            this.sb.delete(0, this.sb.length());
        }
        this.tagName = "";
    }

    public void characters(char ch[], int start, int length) {
        if (this.tagName.equals("replace") || this.tagName.equals("regex")) this.sb.append(ch, start, length);
    }

    public void error(SAXParseException exception) throws SAXException {
        throw exception;
    }
}

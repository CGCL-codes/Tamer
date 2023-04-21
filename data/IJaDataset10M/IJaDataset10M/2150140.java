package net.sf.reactionlab;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class ReactorInfoParser extends DefaultHandler {

    private Environment env;

    private SimpleReactorInfo curInfo;

    private StringBuffer sb = new StringBuffer();

    ReactorInfoParser(Environment env) {
        this.env = env;
    }

    void parse(InputStream in) throws IOException, SAXException {
        try {
            SAXParserFactory.newInstance().newSAXParser().parse(in, this);
        } catch (ParserConfigurationException e) {
            throw new SAXException(e);
        }
    }

    public void startElement(String uri, String name, String qName, Attributes attrs) throws SAXException {
        try {
            if ("reactor".equals(qName)) {
                curInfo = new SimpleReactorInfo();
                curInfo.setId(attrs.getValue("id"));
                curInfo.setName(attrs.getValue("name"));
                curInfo.setAbbrev(attrs.getValue("abbrev"));
                curInfo.setNumInputs(parseCount(attrs, "in"));
                curInfo.setNumOutputs(parseCount(attrs, "out"));
                String reactorClass = attrs.getValue("type");
                curInfo.setReactorClass(Class.forName(reactorClass));
                String panelClass = attrs.getValue("panelClass");
                if (panelClass == null) panelClass = reactorClass + "Panel";
                try {
                    curInfo.setPanelClass(Class.forName(panelClass));
                } catch (ClassNotFoundException e) {
                }
            } else if ("network-listener".equals(qName)) {
                Class cls = Class.forName(attrs.getValue("class"));
                env.getNetwork().addNetworkListener((NetworkListener) Util.newInstance(cls));
            }
        } catch (ClassNotFoundException e) {
            throw new SAXException(e);
        } catch (ClassCastException e) {
            throw new SAXException(e);
        }
    }

    private static int parseCount(Attributes attrs, String name) {
        String value = attrs.getValue(name);
        return (value != null) ? Integer.parseInt(value) : 0;
    }

    public void endElement(String uri, String name, String qName) {
        if ("reactor".equals(qName)) {
            env.addReactorInfo(curInfo);
        } else if ("description".equals(qName)) {
            curInfo.setDescription(reformat(sb));
        }
        sb.setLength(0);
    }

    public void characters(char[] ch, int start, int length) {
        sb.append(ch, start, length);
    }

    private String reformat(StringBuffer in) {
        StringBuffer out = new StringBuffer();
        boolean first = true;
        boolean trimming = true;
        int len = in.length();
        int from = 0;
        while (from < len) {
            char ch = in.charAt(from++);
            if (trimming) {
                if (Character.isWhitespace(ch)) {
                    if (ch == '\n' && !first) out.append("\n\n");
                } else {
                    first = trimming = false;
                    out.append(ch);
                }
            } else if (ch == '\n') {
                out.append(' ');
                trimming = true;
            } else {
                out.append(ch);
            }
        }
        return new String(out);
    }
}

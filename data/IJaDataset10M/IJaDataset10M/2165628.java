package xmlutils;

import java.io.*;
import java.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import javax.xml.parsers.*;

public class XMLAutomataHandler extends DefaultHandler {

    public static enum Mode {

        AUTOMATA, NODE
    }

    CountingStack<XMLAutomata> as = new CountingStack<XMLAutomata>();

    XMLAutomata initial;

    CountingStack<XMLNode> nodes = new CountingStack<XMLNode>();

    Mode mode;

    public static boolean handle(String file, XMLAutomata a) {
        try {
            DefaultHandler h = new XMLAutomataHandler(a);
            SAXParser sp = SAXParserFactory.newInstance().newSAXParser();
            sp.parse(file, h);
        } catch (Throwable w) {
            w.printStackTrace();
            return false;
        }
        return true;
    }

    XMLAutomataHandler(XMLAutomata a) {
        initial = a;
        mode = Mode.AUTOMATA;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        Properties p = convert(attributes);
        if (mode == Mode.AUTOMATA) {
            if (as.count() == 0) as.push(initial); else as.push(as.peek().element(qName, p));
            if (as.peek() != null) {
                as.peek().setQName(qName);
                as.peek().setProps(p);
                as.peek().start();
            } else mode = Mode.NODE;
        }
        if (mode == Mode.NODE) {
            XMLNode n = new XMLNode(qName);
            n.setProps(p);
            if (nodes.count() > 0) nodes.peek().addNode(qName, n);
            nodes.push(n);
        }
    }

    public void characters(char[] ch, int start, int length) {
        String data = new String(ch, start, length);
        if (as.count() > 0) if (mode == Mode.AUTOMATA) as.peek().data(data); else nodes.peek().addData(data);
    }

    public void endElement(String uri, String localName, String qName) {
        if (mode == Mode.AUTOMATA) {
            as.peek().end();
            as.pop();
        } else {
            if (nodes.count() == 1) {
                as.pop();
                as.peek().node(nodes.pop());
                mode = Mode.AUTOMATA;
            } else nodes.pop();
        }
    }

    private Properties convert(Attributes a) {
        Properties p = new Properties();
        for (int i = 0; i < a.getLength(); i++) {
            p.setProperty(a.getQName(i), a.getValue(i));
        }
        return p;
    }
}

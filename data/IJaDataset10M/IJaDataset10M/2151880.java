package org.stanwood.media.store.mp4.atomicparsley;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.stanwood.media.store.mp4.IAtom;
import org.stanwood.media.store.mp4.MP4AtomKey;
import org.stanwood.media.store.mp4.MP4Exception;
import org.stanwood.media.xml.XMLParser;
import org.stanwood.media.xml.XMLParserException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class AtomicParsleyOutputParser extends XMLParser {

    private static final Log log = LogFactory.getLog(AtomicParsleyOutputParser.class);

    private Document doc;

    public AtomicParsleyOutputParser(String output) throws MP4Exception {
        try {
            doc = XMLParser.strToDom(output);
        } catch (XMLParserException e) {
            throw new MP4Exception("Unable to parse AtomicParsley output", e);
        }
    }

    public List<IAtom> listAtoms() throws MP4Exception {
        try {
            List<IAtom> atoms = new ArrayList<IAtom>();
            for (Node node : selectNodeList(doc, "AtomicParsley/atoms/*")) {
                IAtom atom = parseAtom((Element) node);
                if (atom != null) {
                    atoms.add(atom);
                }
            }
            return atoms;
        } catch (XMLParserException e) {
            throw new MP4Exception("Unable to parse AtomicParsley output", e);
        }
    }

    @SuppressWarnings("nls")
    private IAtom parseAtom(Element node) throws MP4Exception {
        String name = node.getAttribute("name");
        if (name.length() == 0) {
            throw new MP4Exception("Unable to get name of atom");
        }
        String rDnsDomain = node.getAttribute("reverseDNSdomain");
        String rDnsName = node.getAttribute("reverseDNSname");
        MP4AtomKey key;
        if (rDnsDomain != null && rDnsDomain.length() > 0 && rDnsName != null && rDnsName.length() > 0) {
            key = MP4AtomKey.fromRDNS(rDnsName, rDnsDomain);
        } else {
            key = MP4AtomKey.fromKey(name);
        }
        if (key == null) {
            log.warn(MessageFormat.format("Unable to find atom details with name ''{0}'', dns domain ''{1}'', dns name ''{2}''", name, rDnsDomain, rDnsName));
            return null;
        }
        if (node.getNodeName().equals("atomString")) {
            return new APAtomString(key, node.getTextContent());
        } else if (node.getNodeName().equals("atomRange")) {
            short count = (short) parseIntAttribute(node, name, "count");
            short max = 0;
            if (node.hasAttribute("max")) {
                max = (short) parseIntAttribute(node, name, "max");
            }
            return new APAtomRange(key, count, max);
        } else if (node.getNodeName().equals("atomNumber")) {
            return new APAtomNumber(key, parseLongAttribute(node, name, "value"));
        } else if (node.getNodeName().equals("atomBoolean")) {
            String value = node.getAttribute("value");
            if (value.equals("1") || value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("true")) {
                return new APAtomBoolean(key, true);
            } else {
                return new APAtomBoolean(key, false);
            }
        } else if (node.getNodeName().equals("atomArtwork")) {
            return new APAtomArtworkSummary(key, 1);
        }
        throw new MP4Exception(MessageFormat.format("Unsupported atom node {0}", node.getNodeName()));
    }

    protected int parseIntAttribute(Element node, String name, String attributeName) throws MP4Exception {
        String value = node.getAttribute(attributeName);
        if (value.length() == 0) {
            throw new MP4Exception(MessageFormat.format("Empty value for atom {0}", name));
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new MP4Exception(MessageFormat.format("Unable to parse number from value {0} for atom {1}", value, name));
        }
    }

    protected long parseLongAttribute(Element node, String name, String attributeName) throws MP4Exception {
        String value = node.getAttribute(attributeName);
        if (value.length() == 0) {
            throw new MP4Exception(MessageFormat.format("Empty value for atom {0}", name));
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new MP4Exception(MessageFormat.format("Unable to parse number from value {0} for atom {1}", value, name));
        }
    }
}

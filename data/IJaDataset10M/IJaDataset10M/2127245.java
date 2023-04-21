package nacad.lemm.parser;

import java.util.LinkedList;
import java.util.List;
import nacad.lemm.web.links.DomainLink;
import nacad.lemm.web.links.Link;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author jonas
 */
public class DomainListParser extends DefaultHandler {

    private List<Link> domains = new LinkedList<Link>();

    private DomainLink link;

    private String domainName;

    private String domainFile;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("domain")) {
            this.domainName = attributes.getValue("name");
            this.domainFile = attributes.getValue("file");
            link = new DomainLink(domainName, domainFile);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("domain")) {
            domains.add(link);
        }
    }

    public List<Link> getDomainList() {
        return domains;
    }
}

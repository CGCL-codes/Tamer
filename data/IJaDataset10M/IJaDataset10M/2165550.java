package de.annotatio.client;

import java.text.ParseException;
import java.util.Date;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author alex
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AnnotationBean implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -659932840348486329L;

    Element annoResource = null;

    Element annoBody = null;

    protected String annoNS = "http://www.w3.org/2000/10/annotation-ns#";

    protected String rdfNS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

    protected String dubNS = "http://purl.org/dc/elements/1.0/";

    protected String threadNS = "http://www.w3.org/2001/03/thread#";

    protected String htmlNS = "http://www.w3.org/1999/xx/http#";

    protected String alNS = "http://www.annotatio.de/al";

    public void setAnnotationResource(Element aResource) {
        this.annoResource = aResource;
    }

    public void setAnnotationBody(Element annoBody) {
        this.annoBody = annoBody;
    }

    public Node getAnnotationHTMLBodyNode() {
        NodeList nl = this.annoBody.getElementsByTagName("BODY");
        if (nl.getLength() > 0) return nl.item(0); else return null;
    }

    public Element getAnnotationElement() {
        return this.AnnotationElement();
    }

    ;

    private Element AnnotationElement() {
        if (annoResource.getLocalName().equals("Annotation")) {
            return this.annoResource;
        } else {
            System.out.println("Don't have an annotation element, I got " + annoResource.getLocalName());
            return null;
        }
    }

    public String getOrigSite() {
        Element annotatesn = (Element) AnnotationElement().getElementsByTagNameNS(annoNS, "annotates").item(0);
        return annotatesn.getAttributeNS(rdfNS, "resource");
    }

    public String getAnnoText() {
        NodeList contextinfolist = AnnotationElement().getElementsByTagNameNS(alNS, "context-element");
        if (contextinfolist.getLength() > 0) {
            Element contextinfo = (Element) contextinfolist.item(0);
            return contextinfo.getAttributeNS(alNS, "text");
        } else return null;
    }

    public String getAnnoAuthor() {
        return this.AnnotationElement().getAttributeNS(dubNS, "creator");
    }

    public Date getAnnoDate() {
        try {
            return ISO8601DateParser.parse(AnnotationElement().getAttributeNS(dubNS, "date"));
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date(0);
        }
    }

    public String getContextWords() {
        NodeList contextinfolist = AnnotationElement().getElementsByTagNameNS(alNS, "context-element");
        if (contextinfolist.getLength() > 0) {
            Element contextinfo = (Element) contextinfolist.item(0);
            return extractTextFromNode(contextinfo).replaceAll("\\s", "");
        } else return null;
    }

    public String getAnnoID() {
        return AnnotationElement().getAttributeNS(alNS, "id");
    }

    public String getAnnotationBodyURL() {
        NodeList annobodylist = AnnotationElement().getElementsByTagNameNS(annoNS, "body");
        if (annobodylist.getLength() > 0) {
            Element annobody = (Element) annobodylist.item(0);
            return (annobody.getAttributeNS(rdfNS, "resource"));
        } else {
            System.out.println("No Annotation Body Resource found.");
            return "";
        }
    }

    public String getAnnoContext() {
        return AnnotationElement().getAttributeNS(annoNS, "context");
    }

    /**
	 * Recursive funktion which returns the trimmed content of all text node which
	 * are below and on the same level as the passed node
	 * @param node 
	 * @return Text of all Text-Nodes
	 */
    private String extractTextFromNode(Node node) {
        String text = "";
        if (node.getNodeType() == Node.TEXT_NODE) {
            return (node.getNodeValue().trim());
        } else {
            NodeList childlist = node.getChildNodes();
            for (int i = 0; i < childlist.getLength(); i++) {
                text = text + " " + extractTextFromNode(childlist.item(i));
            }
        }
        return text;
    }
}

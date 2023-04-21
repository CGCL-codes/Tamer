package it.newinstance.util.xml.sax;

import java.util.HashMap;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Base SAX handler
 * 
 * @author Luigi R. Viggiano
 * @version $Id: BaseHandler.java 26 2006-11-15 22:43:38Z luigi.viggiano $
 */
public abstract class BaseHandler extends DefaultHandler {

    private Environment env;

    private Map tagHandlers;

    private Stack evaluatingTags;

    protected BaseHandler() {
        env = new Environment();
        evaluatingTags = new Stack();
        tagHandlers = new HashMap();
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        evaluatingTags.push(qName);
        TagHandler tag = (TagHandler) tagHandlers.get(qName);
        if (tag != null) tag.doStart(env, attributes);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        TagHandler tag = (TagHandler) tagHandlers.get(qName);
        if (tag != null) tag.doEnd(env);
        evaluatingTags.pop();
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        TagHandler tag = (TagHandler) tagHandlers.get(evaluatingTags.peek());
        if (tag != null) tag.characters(env, ch, start, length);
    }

    protected void addTagHandler(String tagName, TagHandler handler) {
        tagHandlers.put(tagName, handler);
    }

    protected TagHandler getTagHandler(String tagName) {
        return (TagHandler) tagHandlers.get(tagName);
    }

    public Environment getEnv() {
        return env;
    }
}

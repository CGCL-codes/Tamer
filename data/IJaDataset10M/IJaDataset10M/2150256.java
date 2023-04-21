package foursquare4j.xml.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import foursquare4j.types.Requests;
import foursquare4j.types.User;

public class RequestsHandler extends Handler<Requests> {

    private static enum E {

        _, USER
    }

    private E e;

    private Requests requests;

    private User user;

    @Override
    public void startDocument() throws SAXException {
        requests = new Requests();
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if ("requests".equals(qName) && e == null) e = E._; else if ("user".equals(qName) && e == E._) {
            e = E.USER;
            requests.add(user = new User());
        }
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        if ("requests".equals(qName) && e == E._) e = null; else if ("user".equals(qName) && e == E.USER) e = E._;
    }

    @Override
    protected void characters(final String text) throws SAXException {
        switch(e) {
            case _:
                break;
            case USER:
                set(user, tag, text);
                break;
        }
    }

    @Override
    public Requests getObject() {
        return requests;
    }
}

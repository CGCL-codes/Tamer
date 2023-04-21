package rtm.authentication;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import rtm.core.BaseResponseHandler;
import rtm.core.CommandFacade;
import rtm.core.UrlBuilder;

public class TokenHandler extends BaseResponseHandler {

    private String token;

    private boolean startedToken;

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (this.startedToken) {
            this.token = new String(ch, start, length);
        }
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, name, attributes);
        this.startedToken = "token".equals(name);
    }

    public String getToken() {
        return token;
    }
}

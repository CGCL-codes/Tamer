package be.roam.drest.service.twitter.xml;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import be.roam.drest.service.twitter.TwitterStatus;
import be.roam.drest.service.twitter.TwitterUser;
import be.roam.util.StringUtil;

public class TwitterStatusHandler extends DefaultHandler {

    private Set<TwitterStatus> set;

    private TwitterStatus status;

    private boolean inUser;

    private StringBuilder charBuffer;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("statuses".equals(localName)) {
            set = new HashSet<TwitterStatus>();
        } else if ("status".equals(localName)) {
            status = new TwitterStatus();
        } else if ("created_at".equals(localName)) {
            charBuffer = new StringBuilder();
        } else if ("id".equals(localName)) {
            charBuffer = new StringBuilder();
        } else if ("text".equals(localName)) {
            charBuffer = new StringBuilder();
        } else if ("relative_created_at".equals(localName)) {
            charBuffer = new StringBuilder();
        } else if ("user".equals(localName)) {
            inUser = true;
            status.setUser(new TwitterUser());
        } else if ("name".equals(localName)) {
            charBuffer = new StringBuilder();
        } else if ("screen_name".equals(localName)) {
            charBuffer = new StringBuilder();
        } else if ("location".equals(localName)) {
            charBuffer = new StringBuilder();
        } else if ("description".equals(localName)) {
            charBuffer = new StringBuilder();
        } else if ("profile_image_url".equals(localName)) {
            charBuffer = new StringBuilder();
        } else if ("url".equals(localName)) {
            charBuffer = new StringBuilder();
        }
    }

    public Set<TwitterStatus> getStatusSet() {
        return set;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("status".equals(localName)) {
            set.add(status);
            status = null;
        } else if ("created_at".equals(localName)) {
            status.setDateCreated(parseDate(cleanCharBuffer()));
        } else if ("id".equals(localName)) {
            if (inUser) {
                status.getUser().setId(StringUtil.parseLong(cleanCharBuffer()));
            } else {
                status.setId(StringUtil.parseLong(cleanCharBuffer()));
            }
        } else if ("text".equals(localName)) {
            status.setText(cleanCharBuffer());
        } else if ("relative_created_at".equals(localName)) {
            status.setRelativeCreated(cleanCharBuffer());
        } else if ("user".equals(localName)) {
            inUser = false;
        } else if ("name".equals(localName)) {
            status.getUser().setName(cleanCharBuffer());
        } else if ("screen_name".equals(localName)) {
            status.getUser().setScreenName(cleanCharBuffer());
        } else if ("location".equals(localName)) {
            status.getUser().setLocation(cleanCharBuffer());
        } else if ("description".equals(localName)) {
            status.getUser().setDescription(cleanCharBuffer());
        } else if ("profile_image_url".equals(localName)) {
            status.getUser().setProfileImageUrl(cleanCharBuffer());
        } else if ("url".equals(localName)) {
            status.getUser().setUrl(cleanCharBuffer());
        }
    }

    private Date parseDate(String string) {
        return null;
    }

    @Override
    public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
        if (charBuffer != null) {
            charBuffer.append(arg0, arg1, arg2);
        }
    }

    private String cleanCharBuffer() {
        if (charBuffer == null) {
            return null;
        }
        String buffer = charBuffer.toString();
        charBuffer = null;
        return buffer;
    }
}

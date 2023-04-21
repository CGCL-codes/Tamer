package org.monet.backmobile.model;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class HubPortUser {

    private String fullname;

    private StringBuilder roles = new StringBuilder();

    private String enviromentId;

    private String enviromentCode;

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setRoles(String roles) {
        this.roles.setLength(0);
        this.roles.append(roles);
    }

    public String getRoles() {
        return roles.toString();
    }

    public void setEnviromentId(String enviromentId) {
        this.enviromentId = enviromentId;
    }

    public String getEnviromentId() {
        return enviromentId;
    }

    public void setEnviromentCode(String enviromentCode) {
        this.enviromentCode = enviromentCode;
    }

    public String getEnviromentCode() {
        return enviromentCode;
    }

    public void deserialize(XmlPullParser parser) throws XmlPullParserException, IOException {
        int currentToken = parser.getEventType();
        do {
            switch(currentToken) {
                case XmlPullParser.START_TAG:
                    String tag = parser.getName();
                    if (tag.equals("username")) {
                        this.fullname = parser.nextText();
                    } else if (tag.equals("role")) {
                        this.roles.append(parser.nextText());
                        this.roles.append(", ");
                    } else if (tag.equals("environment")) {
                        this.enviromentId = parser.getAttributeValue("", "id");
                        this.enviromentCode = parser.getAttributeValue("", "code");
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
        } while ((currentToken = parser.next()) != XmlPullParser.END_DOCUMENT);
        if (this.roles.length() > 0) {
            this.roles.delete(this.roles.length() - 2, this.roles.length());
        }
    }
}

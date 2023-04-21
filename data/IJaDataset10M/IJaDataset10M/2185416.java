package edu.asu.quadriga.xml;

import java.util.ArrayList;
import java.util.List;
import edu.asu.quadriga.user.IUser;

public class XMLMessage {

    private List<String> entries;

    private List<String> dictionaries;

    public XMLMessage() {
        entries = new ArrayList<String>();
        dictionaries = new ArrayList<String>();
    }

    public void appendUsers(IUser[] entries) {
        for (IUser entry : entries) appendEntry(entry);
    }

    public void appendEntry(IUser entry) {
        StringBuffer sb = new StringBuffer();
        sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.USER + ">");
        sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.USER_ID + ">");
        sb.append(entry.getName());
        sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.USER_ID + ">");
        sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.FIRST_NAME + ">");
        sb.append(entry.getFirstName());
        sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.FIRST_NAME + ">");
        sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.LAST_NAME + ">");
        sb.append(entry.getLastName());
        sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.LAST_NAME + ">");
        sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.LAST_NAME + ">");
        sb.append(entry.getLastName());
        sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.LAST_NAME + ">");
        sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.USER_URI + ">");
        sb.append(XMLConstants.ID_NAMESPACE + entry.getName());
        sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.USER_URI + ">");
        sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.USER + ">");
        entries.add(sb.toString());
    }

    public String getXML() {
        StringBuffer sb = new StringBuffer();
        sb.append("<" + XMLConstants.QUADRIGA_ANSWER + " xmlns:" + XMLConstants.NAMESPACE_PREFIX + "=\"" + XMLConstants.NAMESPACE + "\">");
        for (String dict : dictionaries) {
            sb.append(dict);
        }
        for (String entry : entries) {
            sb.append(entry);
        }
        sb.append("</" + XMLConstants.QUADRIGA_ANSWER + ">");
        return sb.toString();
    }
}

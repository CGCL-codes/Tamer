package fr.albin.jmessagesend.message.generic;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import fr.albin.jmessagesend.conf.Configuration;
import fr.albin.jmessagesend.smiley.SmileyParser;
import fr.albin.jmessagesend.user.User;

public class MessageFormatter {

    public MessageFormatter() {
        this.dateFormat = new SimpleDateFormat(Configuration.getInstance().getDatePattern());
        this.smileyParser = new SmileyParser();
    }

    /**
	 * When sending message, sets a little protocol to specify 
	 * all the destinators of the same message.
	 * @param message
	 * @return A string used to send to one or several toUsers.
	 */
    public String asString(Message message) {
        String result = "Date : " + dateFormat.format(message.getDate()) + "\n";
        result += "Sender : " + message.getFromUser() + "\n";
        result += "To : " + this.getHumanReadableUsersList(message.getUsers()) + "\n";
        result += "Message : " + message.getMessage();
        return result;
    }

    /**
	 * When receiving message, interprets its content in HTML.
	 * @param message The message to represent.
	 * @return An HTML string representation of the message.
	 */
    public String asHtml(Message message) {
        String anchoredUrls = this.anchorUrls(message.getMessage());
        String messageWithSmileys = this.smileyParser.parse(anchoredUrls);
        String result = "<font size=\"3\">" + dateFormat.format(message.getDate()) + " - ";
        result += "de " + message.getFromUser() + "<br>";
        result += "A : " + this.getHumanReadableUsersList(message.getUsers()) + "<br>";
        result += "</font><font size=\"4\">";
        result += messageWithSmileys + "<br>";
        result += "</font>";
        return result;
    }

    private String getHumanReadableUsersList(List users) {
        String result = "";
        Iterator iterator = users.iterator();
        while (iterator.hasNext()) {
            User user = (User) iterator.next();
            if (result.equals("")) {
                result = user.getNetbiosName();
            } else {
                result += USER_SEPARATOR + user.getNetbiosName();
            }
        }
        return result;
    }

    private String anchorUrls(String text) {
        StringBuffer result = new StringBuffer();
        String regex = Configuration.getInstance().getAnchorableProtocols();
        LOGGER.debug("Regular expression computed : " + regex);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        LOGGER.debug("String compiled for search : " + text);
        while (matcher.find()) {
            String found = matcher.group();
            LOGGER.debug("String found : " + found);
            String anchoredString = "<a href=\"" + found + "\">" + found + "</a>";
            matcher.appendReplacement(result, anchoredString);
            LOGGER.debug("Replaced by : " + anchoredString);
        }
        matcher.appendTail(result);
        if (result.length() == 0) {
            return text;
        } else {
            return result.toString();
        }
    }

    private SimpleDateFormat dateFormat;

    private SmileyParser smileyParser;

    private static final String USER_SEPARATOR = ",";

    private static final Log LOGGER = LogFactory.getLog(MessageFormatter.class);
}

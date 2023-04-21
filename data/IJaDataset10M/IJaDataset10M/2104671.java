package jimo.spi.im.msn;

import jimo.spi.im.api.Message;
import jimo.spi.im.api.MessageComponent;
import jimo.spi.im.api.SmileyComponent;
import jimo.spi.im.api.TextComponent;
import jimo.spi.im.api.URLComponent;
import jimo.spi.im.api.net.ProxyInfo;
import jimo.spi.im.api.net.ssl.JIMOHttpsConnection;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Miscellaneous utility methods.
 */
abstract class Util {

    private static String[][] smileyTexts = { { "(Y)", "(y)" }, { "(N)", "(n)" }, { "(B)", "(b)" }, { "(D)", "(d)" }, { "(X)", "(x)" }, { "(Z)", "(z)" }, { "(6)" }, { ":-[", ":[" }, { "(})" }, { "({)" }, { "(M)", "(m)" }, { ":-)", ":)" }, { ":-D", ":d" }, { ":-O", ":o" }, { ":-P", ":p" }, { ";-)", ";)" }, { ":-(", ":(" }, { ":-S", ":s" }, { ":-|", ":|" }, { ":'(" }, { ":$", ":-$" }, { "(H)", "(h)" }, { ":-@", ":@" }, { "(A)", "(a)" }, { "(L)", "(l)" }, { "(U)", "(u)" }, { "(K)", "(k)" }, { "(G)", "(g)" }, { "(F)", "(f)" }, { "(W)", "(w)" }, { "(P)", "(p)" }, { "(~)" }, { "(T)", "(t)" }, { "(@)" }, { "(&)" }, { "(C)", "(c)" }, { "(I)", "(i)" }, { "(S)" }, { "(*)" }, { "(8)" }, { "(E)", "(e)" }, { "(^)" }, { "(O)", "(o)" } };

    private static String[] smileyNames = { "Thumbs up", "Thumbs down", "Beer mug", "Martini glass", "Girl", "Boy", "Devil", "Vampire bat", "Right hug", "Left hug", "MSN Messenger", "Smiley", "Big Grin", "Oh", "Tounge Out", "Wink", "Sad", "Crooked", "Stern", "Crying", "Embarassed", "Cool", "Angry", "Angel", "Red heart", "Broken heart", "Kiss", "Gift", "Red rose", "Wilted rose", "Camera", "Film strip", "Telephone", "Cat", "Dog", "Coffee", "Light bulb", "Half moon", "Star", "Music note", "Letter", "B'day cake", "Clock" };

    /**
	 * Array of all available MSN smileys.
	 */
    private static SmileyComponent[] smileys = new SmileyComponent[43];

    /**
	 * Mapping between status messages and status codes.
	 */
    private static Hashtable statusMap;

    static {
        statusMap = new Hashtable();
        statusMap.put("NLN", "Online");
        statusMap.put("IDL", "Idle");
        statusMap.put("AWY", "Away");
        statusMap.put("BSY", "Busy");
        statusMap.put("BRB", "Be Right Back");
        statusMap.put("PHN", "On the Phone");
        statusMap.put("LUN", "Out to Lunch");
    }

    /**
	 * Decode a URL-encoded string. Decoding is done as explained in
	 * RFC 1738, at least I hope so :-) .
	 *
	 * @param str the URL-encoded string.
	 * @return equivalent decoded string.
	 */
    static String urlDecode(String str) {
        char[] chars = str.toCharArray();
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '%' && i < chars.length - 2) {
                int digit1 = Character.digit(chars[i + 1], 16);
                int digit2 = Character.digit(chars[i + 2], 16);
                if (digit1 != -1 && digit2 != -1) {
                    ret.append((char) (digit1 * 16 + digit2));
                    i += 2;
                } else ret.append(chars[i]);
            } else ret.append(chars[i]);
        }
        return ret.toString();
    }

    /**
	 * Encode a string to URL-encoded form. Encoding is done as explained in
	 * RFC 1738, at least I hope so :-) .
	 *
	 * @param str the string to be encoded.
	 * @return equivalent encoded string.
	 */
    static String urlEncode(String str) {
        char[] chars = str.toCharArray();
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            if ((chars[i] >= 0x00 && chars[i] <= 0x1F) || (chars[i] >= 0x80 && chars[i] <= 0xFF) || chars[i] == '<' || chars[i] == '>' || chars[i] == '\"' || chars[i] == '#' || chars[i] == '%' || chars[i] == '{' || chars[i] == '}' || chars[i] == '|' || chars[i] == '\\' || chars[i] == '^' || chars[i] == '~' || chars[i] == '[' || chars[i] == ']' || chars[i] == '`' || chars[i] == ' ') {
                ret.append('%');
                int digit = chars[i] / 16;
                ret.append((char) (digit > 9 ? ('A' + digit - 10) : ('0' + digit)));
                digit = chars[i] % 16;
                ret.append((char) (digit > 9 ? ('A' + digit - 10) : ('0' + digit)));
            } else if (chars[i] >= 0x00 && chars[i] <= 0xFF) ret.append(chars[i]);
        }
        return ret.toString();
    }

    /**
	 * Authenticate via passport service. The passport authentication takes
	 * a challenge string sent by notification server as input. It returns a
	 * challenge response to be sent to notification server for approval.
	 *
	 * @param username the passport to connect.
	 * @param password the password of this passport.
	 * @param challenge the input challenge string.
	 * @param info the proxy information.
	 * @return the output string to be sent to notification server.
	 */
    static String passportAuthenticate(String username, String password, String challenge, ProxyInfo info) throws IOException {
        String url = getPassportLoginServer(info);
        return loginServerAuthenticate(url, info, challenge, username, password);
    }

    /**
	 * Connect to the passport login server and get the challenge response for
	 * notification server.
	 *
	 * @param url        URL to the passport login server
	 * @param info       proxy to be used for connection
	 * @param challenge  challenge string to be sent to login server as
	 *                   specified by notification server
	 * @param username   username for login
	 * @param password   password for login
	 * @return the challenge response to be sent to notification server.
	 */
    private static String loginServerAuthenticate(String url, ProxyInfo info, String challenge, String username, String password) throws IOException {
        JIMOHttpsConnection conn = new JIMOHttpsConnection(url, info);
        conn.setHeaderField("Authorization", "Passport1.4 " + "OrgVerb=GET," + "OrgURL=http%3A%2F%2Fmessenger%2Emsn%2Ecom," + "sign-in=" + urlEncode(username) + "," + "pwd=" + password + "," + challenge);
        conn.setHeaderField("User-Agent", "MSMSGS");
        conn.doRequest();
        String ret = conn.getHeaderField("Authentication-Info");
        int start = ret.indexOf("da-status=") + 10;
        int end = ret.indexOf(',', start);
        String status = ret.substring(start, end);
        if (!"success".equals(status)) return null;
        start = ret.indexOf("from-PP='") + 9;
        end = ret.indexOf("',", start);
        return ret.substring(start, end);
    }

    /**
	 * Returns the address of passport login server. This is retrieved from
	 * nexus.passport.com.
	 *
	 * @param info the proxy information.
	 * @return url of passport login server
	 */
    private static String getPassportLoginServer(ProxyInfo info) throws IOException {
        JIMOHttpsConnection conn = new JIMOHttpsConnection("https://nexus.passport.com/rdr/pprdr.asp", info);
        conn.doRequest();
        String header = conn.getHeaderField("PassportURLs");
        int start = header.indexOf("DALogin=") + 8;
        int end = header.indexOf(',', start);
        return "https://" + header.substring(start, end);
    }

    /**
	 * Returns the MD5 hash of a given string.
	 *
	 * @param str the input string.
	 * @return the MD5 hash of the input string.
	 */
    static String getMD5Hash(String str) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes());
        byte[] b = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            int v = (int) b[i];
            v = v < 0 ? 0x100 + v : v;
            String cc = Integer.toHexString(v);
            if (cc.length() == 1) sb.append('0');
            sb.append(cc);
        }
        return sb.toString();
    }

    /**
	 * Converts a verbose status message to the three character status code in
	 * MSN protocol. If null is passed as the verbose status message, this method
	 * returns "HDN". 
	 *
	 * @param status the verbose status message.
	 * @return its equivalent MSN status code, or null if it is not recognised by MSN. 
	 */
    static String getStatusCode(String status) {
        if (status == null) return "HDN"; else {
            Enumeration keys = statusMap.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                if (status.equals(statusMap.get(key))) return key;
            }
        }
        return null;
    }

    /**
	 * Converts an MSN status code to its equivalent verbose
	 * status message.
	 *
	 * @param code the three character MSN status code
	 * @return its equivalent verbose status message, or null if it is not recognised. 
	 */
    static String getVerboseStatus(String code) {
        return (String) statusMap.get(code);
    }

    /**
	 * Returns an array of status messages supported by MSN protocol.
	 *
	 * @return array of status messages supported by MSN protocol
	 */
    static String[] getSupportedStatusMessages() {
        return (String[]) statusMap.values().toArray(new String[0]);
    }

    /**
	 * Returns an array of all supported MSN smileys.
	 *
	 * @return array of all MSN smiley components.
	 */
    public static SmileyComponent[] loadSmileys() {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        for (int i = 1; i <= 43; i++) {
            URL url;
            if (i < 10) url = cl.getResource("jimo/spi/msn/images/0" + i + ".gif"); else url = cl.getResource("jimo/spi/msn/images/" + i + ".gif");
            if (url == null) smileys[i - 1] = new SmileyComponent(null, smileyTexts[i - 1], smileyNames[i - 1]); else {
                Icon icon = new ImageIcon(url);
                smileys[i - 1] = new SmileyComponent(icon, smileyTexts[i - 1], smileyNames[i - 1]);
            }
        }
        return smileys;
    }

    /**
	 * Construct a message object from a message string received from MSN.
	 *
	 * @param message the message string received from MSN.
	 * @return the equivalent message object.
	 */
    static Message parseMSNMessage(String message, Font font, Color color) {
        MessageComponent[] comp = parseForURLs(new TextComponent(message));
        comp = parseForSmileys(comp);
        Message ret = new Message();
        for (int i = 0; i < comp.length; i++) {
            if (comp[i] instanceof TextComponent) {
                TextComponent txt = (TextComponent) comp[i];
                txt.setFont(font);
                txt.setColor(color);
            }
            ret.addComponent(comp[i]);
        }
        return ret;
    }

    private static Pattern urlPattern = Pattern.compile("http://\\S+|ftp://\\S+|www\\.\\S+");

    /**
	 * Parses a character sequence from a given text component to URL components
	 * and text components representing the remaining sequence of characters.
	 * As of now, Any word begining with "http://", "ftp://", or "www." are
	 * considered as URLS. This may change in future versions of the library.
	 *
	 * @param comp the input text component.
	 * @return the sequence of resultant message components.
	 */
    private static MessageComponent[] parseForURLs(TextComponent comp) {
        StringBuffer sb = new StringBuffer();
        sb.append(comp.getSequence());
        List ret = new ArrayList();
        Matcher m = urlPattern.matcher(sb);
        int txtStart = 0;
        while (m.find()) {
            int urlStart = m.start();
            int urlEnd = m.end();
            if (txtStart <= urlStart - 1) ret.add(new TextComponent(sb.substring(txtStart, urlStart)));
            String linkText = sb.substring(urlStart, urlEnd);
            try {
                if (linkText.startsWith("www")) ret.add(new URLComponent(linkText, new URL("http://" + linkText))); else ret.add(new URLComponent(linkText, new URL(linkText)));
            } catch (MalformedURLException e) {
                ret.add(new TextComponent(linkText));
            }
            txtStart = urlEnd;
        }
        if (txtStart < sb.length()) ret.add(new TextComponent(sb.substring(txtStart)));
        return (MessageComponent[]) ret.toArray(new MessageComponent[0]);
    }

    /**
	 * Parse and get smiley components.
	 * <p>
	 * The input to this method is an array of message components, each of which
	 * may be either a text component or a url component. This method selects all
	 * the text components and splits them to text components and smiley components.
	 *
	 * @param comp the input array of message components
	 * @return the output array of message components.
	 */
    private static MessageComponent[] parseForSmileys(MessageComponent[] comp) {
        List ret = new ArrayList();
        for (int i = 0; i < comp.length; i++) {
            if (comp[i] instanceof TextComponent) {
                TextComponent txt = (TextComponent) comp[i];
                MessageComponent[] msgs = splitForSmileys(txt);
                for (int j = 0; j < msgs.length; j++) ret.add(msgs[j]);
            } else ret.add(comp[i]);
        }
        return (MessageComponent[]) ret.toArray(new MessageComponent[0]);
    }

    /**
	 * Split a text component to smileys and plain text.
	 *
	 * @param comp the text component to be split.
	 * @return an array of message components after split.
	 */
    private static MessageComponent[] splitForSmileys(TextComponent comp) {
        char[] chars = comp.getSequence();
        if (chars == null || chars.length == 0) return new MessageComponent[0];
        String text = new String(chars);
        List ret = new ArrayList();
        int foundPos = -1;
        int foundSmiley = -1;
        int foundLength = -1;
        for (int i = 0; i < smileyTexts.length; i++) {
            String[] oneSmiley = smileyTexts[i];
            for (int j = 0; j < oneSmiley.length; j++) {
                int len = oneSmiley[j].length();
                if (len <= foundLength) continue;
                int pos = text.indexOf(oneSmiley[j]);
                if (pos == -1) continue;
                foundPos = pos;
                foundSmiley = i;
                foundLength = len;
            }
        }
        if (foundPos != -1) {
            String left = text.substring(0, foundPos);
            String right = text.substring(foundPos + foundLength);
            MessageComponent[] leftComp = splitForSmileys(new TextComponent(left));
            MessageComponent[] rightComp = splitForSmileys(new TextComponent(right));
            for (int i = 0; i < leftComp.length; i++) ret.add(leftComp[i]);
            ret.add(smileys[foundSmiley]);
            for (int i = 0; i < rightComp.length; i++) ret.add(rightComp[i]);
        } else ret.add(new TextComponent(text));
        return (MessageComponent[]) ret.toArray(new MessageComponent[0]);
    }
}

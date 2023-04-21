package org.columba.mail.parser;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.columba.ristretto.io.CharSequenceSource;
import org.columba.ristretto.message.Address;
import org.columba.ristretto.message.BasicHeader;
import org.columba.ristretto.parser.HeaderParser;
import org.columba.ristretto.parser.ParserException;

public class MailUrlParser {

    /** JDK 1.4+ logging framework logger, used for logging. */
    private static final Logger LOG = Logger.getLogger("org.columba.mail.parser");

    private static final Pattern mailtoPattern = Pattern.compile("mailto:([^?]*)\\??(.*)", Pattern.CASE_INSENSITIVE);

    private static final Pattern headerPattern = Pattern.compile("&?([^=]+)=([^&]+)");

    public static Map parse(String in) throws ParserException {
        Hashtable result = new Hashtable();
        StringBuffer temp = new StringBuffer();
        Matcher matcher = mailtoPattern.matcher(in);
        if (matcher.matches()) {
            if (matcher.group(1) != null) {
                temp.append("To: ");
                temp.append(decodeHTML(matcher.group(1)));
                temp.append("\r\n");
            }
            if (matcher.group(2) != null) {
                matcher = headerPattern.matcher(matcher.group(2));
                while (matcher.find()) {
                    String key = matcher.group(1).toLowerCase();
                    if (key.equals("to") || key.equals("cc") || key.equals("bcc") || key.equals("subject")) {
                        temp.append(matcher.group(1));
                        temp.append(": ");
                        temp.append(decodeHTML(matcher.group(2)));
                        temp.append("\r\n");
                    } else if (key.equals("body")) {
                        result.put("body", decodeHTML(matcher.group(2)));
                    } else {
                        LOG.warning("Unsafe header in mailto-URL: " + matcher.group());
                    }
                }
            }
            BasicHeader header = new BasicHeader(HeaderParser.parse(new CharSequenceSource(temp)));
            Address[] addresses = header.getTo();
            List addressList = new ArrayList();
            for (int i = 0; i < addresses.length; i++) {
                addressList.add(addresses[i].toString());
            }
            result.put("to", addressList.toArray(new String[0]));
            addresses = header.getCc();
            if (addresses.length > 0) {
                addressList.clear();
                for (int i = 0; i < addresses.length; i++) {
                    addressList.add(addresses[i].toString());
                }
                result.put("cc", addressList.toArray(new String[0]));
            }
            addresses = header.getBcc();
            if (addresses.length > 0) {
                addressList.clear();
                for (int i = 0; i < addresses.length; i++) {
                    addressList.add(addresses[i].toString());
                }
                result.put("bcc", addressList.toArray(new String[0]));
            }
            if (header.getSubject() != null) {
                result.put("subject", header.getSubject());
            }
        }
        return result;
    }

    private static String decodeHTML(String in) {
        StringBuffer result = new StringBuffer(in.length());
        int pos = 0;
        int nextpos = in.indexOf('%', pos);
        while (nextpos != -1) {
            result.append(in.substring(pos, nextpos));
            result.append((char) Integer.parseInt(in.substring(nextpos + 1, nextpos + 3), 16));
            pos = nextpos + 3;
            nextpos = in.indexOf('%', pos);
        }
        result.append(in.substring(pos));
        return result.toString();
    }
}

package com.mrroman.linksender;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import com.mrroman.linksender.ioc.Log;
import com.mrroman.linksender.ioc.Name;
import com.mrroman.linksender.ioc.ObjectStore;
import com.mrroman.linksender.sender.Message;
import java.net.InetAddress;

/**
 *
 * @author mrozekon
 */
@Name("utils.MessageParser")
public class MessageParser {

    private static final Pattern titlePattern = Pattern.compile("<title>([^<]+)</title>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    private static final Pattern urlPattern = Pattern.compile("(ftp|http|https|smb):\\/\\/" + "[^\\s^<^>]" + "+", Pattern.CASE_INSENSITIVE);

    @Log
    private Logger logger;

    public String preParsingMessage(String message) {
        return message.replace("<", "&lt;").replace(">", "&gt;").replace("\n", "<br/>");
    }

    public String replaceSenderInMessage(Message message) {
        String sMsg = message.getMessage();
        InetAddress sender = message.getAddressOfSender();
        if (sMsg != null && sender != null) {
            sMsg = sMsg.replace("<sender>", sender.getHostAddress());
        }
        return sMsg;
    }

    public String parseMessage(Message message, boolean resolveTitles) {
        String sMsg = replaceSenderInMessage(message);
        Matcher m = urlPattern.matcher(sMsg);
        StringBuffer sb = new StringBuffer();
        int cp = 0;
        String append;
        while (m.find()) {
            String link = sMsg.substring(m.start(), m.end());
            String title;
            if (resolveTitles) {
                title = resolveTitle(link);
            } else {
                title = preParsingMessage(link);
            }
            append = preParsingMessage(sMsg.substring(cp, m.start()));
            sb.append(append);
            cp = m.end();
            sb.append(String.format("<a href=\"%s\">%s</a>", link, title));
        }
        append = preParsingMessage(sMsg.substring(cp, sMsg.length()));
        sb.append(append);
        return sb.toString();
    }

    private String resolveTitle(String link) {
        String result;
        if (!link.toLowerCase().startsWith("http:")) {
            logger.info("Not resolving titles when link class is not http");
            return link;
        }
        try {
            URLConnection conn = new URL(link).openConnection();
            String[] contentType = getContentType(conn);
            if (contentType == null) {
                logger.info("Not resolving titles - can not aquire content-type");
                return link;
            } else if (contentType[0].startsWith("image/")) {
                return "<img src=\"" + link + "\" width=\"50\">";
            } else if (!"text/html".equals(contentType[0])) {
                logger.info("Not resolving titles when content-type is different than text/html");
                return link;
            }
            InputStream content = (InputStream) conn.getContent();
            if ("gzip".equals(conn.getContentEncoding())) {
                content = new GZIPInputStream(content);
            }
            logger.info("Content-Type: " + Arrays.toString(contentType));
            BufferedReader reader = new BufferedReader(new InputStreamReader(content, (contentType[1] == null ? "UTF-8" : contentType[1])));
            StringBuilder sb = new StringBuilder();
            String line;
            int doNotReadTooMuch = 0;
            while ((line = reader.readLine()) != null) {
                if (doNotReadTooMuch++ > 20) {
                    break;
                }
                sb.append(line);
            }
            Matcher m = titlePattern.matcher(sb.toString());
            if (m.find()) {
                result = m.group(1);
            } else {
                result = link;
                logger.warning("Can't find title on page");
            }
            content.close();
        } catch (Exception ex) {
            result = link;
            logger.warning(ex.getMessage());
        }
        return result;
    }

    private String[] getContentType(URLConnection conn) {
        String[] result = new String[2];
        String contentType = conn.getContentType();
        if (contentType == null) {
            return null;
        }
        String[] splitted = contentType.split("\\s*;\\s*");
        result[0] = splitted[0];
        if ((splitted.length > 1) && splitted[1].startsWith("charset")) {
            String[] nameValue = splitted[1].split("\\s*=\\s*");
            if (nameValue[1].startsWith("\"") && nameValue[1].endsWith("\"")) {
                nameValue[1] = nameValue[1].substring(1, nameValue[1].length() - 1);
            }
            result[1] = nameValue[1];
        }
        return result;
    }

    public static void main(String[] args) {
        MessageParser mp = ObjectStore.getObject(MessageParser.class);
        String input = "tekścik 2>1.2\nhttp://www.filmweb.pl/f110868/Carniv%C3%A0le,2003\n ddd ftp://tp.net<br/>koniec\n\\\\moj\\komp aa\n1<2";
        System.out.println("-----" + input);
        System.out.println("-----" + mp.parseMessage(new Message("aaa", input), true));
    }
}

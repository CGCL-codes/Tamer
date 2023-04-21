package ch.ethz.dcg.spamato.filter.razor.hash;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author simon
 * This class removes quoted printable chars from any given message.
 */
public class QPRemover {

    private static final Pattern QPPATTERN = Pattern.compile("=([0-9a-fA-F]{2})");

    public static boolean isQP(String header) {
        return ((header != null) && (header.indexOf("Content-Transfer-Encoding: quoted-printable") != -1));
    }

    public static String qPStrip(String body) {
        String res = body.replaceAll("=\r?\n", "");
        Matcher m = QPPATTERN.matcher(res);
        int startPos;
        String temp;
        while (m.find()) {
            startPos = m.start();
            temp = res.substring(0, startPos);
            temp += String.valueOf((char) (Integer.valueOf(res.substring(startPos + 1, m.end()), 16).intValue()));
            temp += res.substring(m.end());
            res = temp;
            m = QPPATTERN.matcher(res);
        }
        return res;
    }
}

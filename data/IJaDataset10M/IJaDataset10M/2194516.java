package com.csaba.connector.otp.parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.TextExtractor;

public class LoginParser {

    private static final Pattern ERROR_INDICATOR_ERROR = Pattern.compile(".*<title.*hiba.*title>");

    private static final Pattern ERROR_INDICATOR_SMSLOGIN = Pattern.compile("<div[^>]*hibak.*>");

    private static final String SMS_INDICATOR_ERROR = "smsAzonosito";

    public static String parseLoginError(final Reader reader) throws IOException, ParseException {
        final Source source = new Source(reader);
        final StringBuilder message = new StringBuilder();
        final Element h2 = source.getNextElement(0, "h3");
        if (h2 != null) {
            message.append(new TextExtractor(h2).toString());
            final Element p = source.getNextElement(h2.getEnd(), "p");
            if (p != null) {
                message.append(" ");
                message.append(new TextExtractor(p).toString());
            }
        }
        if (message.length() == 0) {
            message.append("Unable to parse error message.");
        }
        return message.toString();
    }

    public static boolean isErrorOccured(final String result) {
        final Matcher matcher = ERROR_INDICATOR_ERROR.matcher(result);
        return matcher.find();
    }

    public static boolean isErrorOccuredSMSLogin(final String result) {
        final Matcher matcher = ERROR_INDICATOR_SMSLOGIN.matcher(result);
        return matcher.find();
    }

    public static boolean isSMSLogin(final String result) {
        return result.contains(SMS_INDICATOR_ERROR);
    }

    public static String extractProfileURL(final String result) {
        final int ajaxTestIndex = result.indexOf("ajaxTest");
        if (ajaxTestIndex < 0) return null;
        final int openning = result.indexOf('\'', ajaxTestIndex);
        if (openning < 0) return null;
        final int closing = result.indexOf('\'', openning + 1);
        if (closing < 0) return null;
        return result.substring(openning + 1, closing);
    }

    public static String parseSMSLoginError(final Reader reader) throws IOException {
        final Source source = new Source(reader);
        String message = null;
        final Element div = source.getFirstElementByClass("hibak");
        if (div != null) {
            message = new TextExtractor(div).toString();
        }
        if (message == null || message.length() == 0) {
            message = "Unable to parse error message.";
        }
        return message;
    }

    public static void main(final String[] args) throws Exception {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("../otp.source/smshiba.html")));
        System.out.println("Error: " + parseSMSLoginError(reader));
    }
}

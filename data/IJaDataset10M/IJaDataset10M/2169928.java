package org.elite.jdcbot.util;

import java.net.URLEncoder;
import java.util.regex.*;
import org.elite.jdcbot.framework.jDCBot;

/**
 * This class extends WebPageFetcher class and implements Runnable
 * It gets calculation string (e.g. 1+2*3) and returns result using google and WebPageFetcher
 * Reason for implementing Runnable is that it can gets stuck while WebPageFetcher finishes its work
 * Credits for idea goes to http://sourceforge.net/projects/phpdcbot !!!
 * 
 * @since 0.6
 * @author Milos Grbic, Kokanovic Branko
 * @version 0.6
 */
public class GoogleCalculation extends WebPageFetcher implements Runnable {

    private jDCBot _bot;

    public GoogleCalculation(jDCBot bot, String calc) throws Exception {
        String url = "http://www.google.com/search?q=";
        _bot = bot;
        calc = URLEncoder.encode(calc, "UTF-8");
        url = url + calc;
        SetURL(url);
    }

    private String GetCalculation() throws Exception {
        String keyword1 = "<font size=+1><b>";
        String keyword2 = "</b>";
        String returned = getPageContent();
        if (returned.contains(keyword1) == false) throw new Exception();
        String result = returned.substring(returned.indexOf(keyword1) + keyword1.length());
        if (result.contains(keyword2) == false) throw new Exception();
        result = result.substring(0, result.indexOf(keyword2));
        if ((result.length() == 0) || (result.length() > 1000)) throw new Exception();
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile("<font size=-2> </font>");
        matcher = pattern.matcher(result);
        result = matcher.replaceAll(",");
        pattern = Pattern.compile("&#215;");
        matcher = pattern.matcher(result);
        result = matcher.replaceAll("x");
        pattern = Pattern.compile("<sup>");
        matcher = pattern.matcher(result);
        result = matcher.replaceAll("^");
        pattern = Pattern.compile("</sup>");
        matcher = pattern.matcher(result);
        result = matcher.replaceAll("");
        return result;
    }

    public void run() {
        try {
            String res = this.GetCalculation();
            _bot.SendPublicMessage(res);
        } catch (Exception e) {
            try {
                _bot.SendPublicMessage("Sorry, I can't calculate this:/");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}

package net.sf.jwebstats.logs.Parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.jwebstats.logs.LogEntry;

public class CombinedLogParser implements ParserInterface {

    private static String pattern = "^([\\d.]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(.+?)\" (\\d{3}) (\\d+) \"([^\"]+)\" \"([^\"]+)\"";

    private static int IP_LOCATION = 1;

    private static int DATE_LOCATION = 4;

    private static int HTTPREQUEST_LOCATION = 5;

    private static int STATUS_LOCATION = 6;

    private static int BYTESSENT_LOCATION = 7;

    private static int REFERRER_LOCATION = 8;

    private static int BROWSER_LOCATION = 9;

    public LogEntry parseLine(String line) {
        LogEntry entry = new LogEntry();
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(line);
        if (!matcher.matches()) {
            return null;
        }
        entry.setIpAddress(matcher.group(IP_LOCATION));
        entry.setDateTime(matcher.group(DATE_LOCATION));
        String httprequest = matcher.group(HTTPREQUEST_LOCATION);
        int pos = httprequest.indexOf("HTTP/1.");
        String request = httprequest;
        if (pos >= 0) {
            request = httprequest.substring(0, pos - 1);
            pos = request.indexOf(" ");
            request = request.substring(pos + 1);
        }
        entry.setRequest(request);
        entry.setStatus(matcher.group(STATUS_LOCATION));
        int bytesSent = new Integer(matcher.group(BYTESSENT_LOCATION)).intValue();
        entry.setBytesSent(bytesSent);
        String referrer = matcher.group(REFERRER_LOCATION);
        if (referrer.equals("-")) entry.setReferrer(null); else entry.setReferrer(referrer);
        entry.setBrowser(matcher.group(BROWSER_LOCATION));
        return entry;
    }
}

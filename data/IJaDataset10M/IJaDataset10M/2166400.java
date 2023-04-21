package com.once.server.data.export;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatPatternUtils {

    public static String formatDatePatternForSQL(String datePattern) {
        String fmtPattern = quotePattern(datePattern);
        fmtPattern = fmtPattern.replaceAll("\\[yy\\]", "YY");
        fmtPattern = fmtPattern.replaceAll("\\[yyyy\\]", "YYYY");
        fmtPattern = fmtPattern.replaceAll("\\[m\\]", "FMMM");
        fmtPattern = fmtPattern.replaceAll("\\[mm\\]", "MM");
        fmtPattern = fmtPattern.replaceAll("\\[mmm\\]", "MON");
        fmtPattern = fmtPattern.replaceAll("\\[mmmm\\]", "FMMonth");
        fmtPattern = fmtPattern.replaceAll("\\[d\\]", "FMDD");
        fmtPattern = fmtPattern.replaceAll("\\[dd\\]", "DD");
        fmtPattern = fmtPattern.replaceAll("\\[ddd\\]", "DD");
        fmtPattern = fmtPattern.replaceAll("\\[dddd\\]", "FMDay");
        fmtPattern = fmtPattern.replaceAll("\\[p\\]", "am");
        fmtPattern = fmtPattern.replaceAll("\\[ap\\]", "am");
        fmtPattern = fmtPattern.replaceAll("\\[P\\]", "AM");
        fmtPattern = fmtPattern.replaceAll("\\[AP\\]", "AM");
        fmtPattern = fmtPattern.replaceAll("\\[PM\\]", "AM");
        fmtPattern = fmtPattern.replaceAll("\\[AMPM\\]", "AM");
        fmtPattern = fmtPattern.replaceAll("\\[h\\]", "FMHH");
        fmtPattern = fmtPattern.replaceAll("\\[hh\\]", "HH");
        fmtPattern = fmtPattern.replaceAll("\\[hhh\\]", "FMHH24");
        fmtPattern = fmtPattern.replaceAll("\\[hhhh\\]", "HH24");
        fmtPattern = fmtPattern.replaceAll("\\[M\\]", "FMMI");
        fmtPattern = fmtPattern.replaceAll("\\[MM\\]", "MI");
        fmtPattern = fmtPattern.replaceAll("\\[s\\]", "FMSS");
        fmtPattern = fmtPattern.replaceAll("\\[ss\\]", "SS");
        fmtPattern = fmtPattern.replaceAll("\\[\\]", "[");
        return fmtPattern;
    }

    public static String formatDatePattern(String datePattern) {
        String fmtPattern = quotePattern(datePattern);
        fmtPattern = fmtPattern.replaceAll("\\[yy\\]", "yy");
        fmtPattern = fmtPattern.replaceAll("\\[yyyy\\]", "yyyy");
        fmtPattern = fmtPattern.replaceAll("\\[m\\]", "M");
        fmtPattern = fmtPattern.replaceAll("\\[mm\\]", "MM");
        fmtPattern = fmtPattern.replaceAll("\\[mmm\\]", "MMM");
        fmtPattern = fmtPattern.replaceAll("\\[mmmm\\]", "MMMM");
        fmtPattern = fmtPattern.replaceAll("\\[d\\]", "d");
        fmtPattern = fmtPattern.replaceAll("\\[dd\\]", "dd");
        fmtPattern = fmtPattern.replaceAll("\\[ddd\\]", "dd");
        fmtPattern = fmtPattern.replaceAll("\\[dddd\\]", "EEEE");
        fmtPattern = fmtPattern.replaceAll("\\[p\\]", "a");
        fmtPattern = fmtPattern.replaceAll("\\[ap\\]", "a");
        fmtPattern = fmtPattern.replaceAll("\\[P\\]", "a");
        fmtPattern = fmtPattern.replaceAll("\\[AP\\]", "a");
        fmtPattern = fmtPattern.replaceAll("\\[PM\\]", "a");
        fmtPattern = fmtPattern.replaceAll("\\[AMPM\\]", "a");
        fmtPattern = fmtPattern.replaceAll("\\[h\\]", "K");
        fmtPattern = fmtPattern.replaceAll("\\[hh\\]", "KK");
        fmtPattern = fmtPattern.replaceAll("\\[hhh\\]", "H");
        fmtPattern = fmtPattern.replaceAll("\\[hhhh\\]", "HH");
        fmtPattern = fmtPattern.replaceAll("\\[M\\]", "m");
        fmtPattern = fmtPattern.replaceAll("\\[MM\\]", "mm");
        fmtPattern = fmtPattern.replaceAll("\\[s\\]", "s");
        fmtPattern = fmtPattern.replaceAll("\\[ss\\]", "ss");
        fmtPattern = fmtPattern.replaceAll("\\[\\]", "[");
        return fmtPattern;
    }

    public static String formatNumberPattern(String numberPattern) throws DataExportException {
        String fmtPattern = quotePattern(numberPattern.replaceAll("\\[\\?\\]", ""));
        Pattern ptn = Pattern.compile("(^.*?)\\[(.*?)\\](.*?);(.*?)\\[(.*?)\\](.*?\\z)", Pattern.DOTALL);
        Matcher match = ptn.matcher(fmtPattern);
        if (!match.find()) {
            throw new DataExportException("Invalid format pattern: " + numberPattern);
        }
        String positivePtn = match.group(2);
        String negativePtn = match.group(5);
        return match.group(1) + formatNumberPatternPart(positivePtn) + match.group(3) + ";" + match.group(4) + formatNumberPatternPart(negativePtn) + match.group(6);
    }

    protected static String formatNumberPatternPart(String numberPattern) {
        int numLen = 0;
        Pattern ptn = Pattern.compile("(t(\\-){0,1}(\\d*))(.*)");
        Matcher match = ptn.matcher(numberPattern);
        StringBuffer sb = new StringBuffer();
        if (match.find()) {
            if (match.group(3) != null && match.group(3).length() > 0) {
                numLen = Integer.parseInt(match.group(3));
            }
            boolean optional = "-".equals(match.group(2));
            String stPtn = optional ? "#" : "0";
            for (int i = 4; i < numLen; i++) {
                stPtn = optional ? stPtn + "#" : stPtn + "0";
            }
            match.appendReplacement(sb, stPtn + match.group(4));
        }
        match.appendTail(sb);
        String fmtPattern = sb.toString();
        int fractLen = 0;
        ptn = Pattern.compile("(.*)(\\.d(\\-){0,1}(\\d*))");
        match = ptn.matcher(fmtPattern);
        sb = new StringBuffer();
        if (match.find()) {
            if (match.group(4) != null && match.group(4).length() > 0) {
                fractLen = Integer.parseInt(match.group(4));
            }
            boolean optional = "-".equals(match.group(3));
            String stPtn = optional ? "#" : "0";
            for (int i = 1; i < fractLen; i++) {
                stPtn = optional ? stPtn + "#" : stPtn + "0";
            }
            match.appendReplacement(sb, match.group(1) + stPtn);
        }
        match.appendTail(sb);
        fmtPattern = sb.toString();
        fmtPattern = fmtPattern.replaceAll("t", "0");
        fmtPattern = fmtPattern.replaceAll("h", "0");
        fmtPattern = fmtPattern.replaceAll("u", "0");
        fmtPattern = fmtPattern.replaceAll("d", "0");
        return fmtPattern;
    }

    protected static String quotePattern(String pattern) {
        Pattern ptn = Pattern.compile("((?<=[^'][^\\['])|\\A)\\b(\\w+)\\b");
        Matcher match = ptn.matcher(pattern);
        StringBuffer sb = new StringBuffer();
        while (match.find()) {
            match.appendReplacement(sb, "'" + match.group(2) + "'");
        }
        match.appendTail(sb);
        return sb.toString();
    }
}

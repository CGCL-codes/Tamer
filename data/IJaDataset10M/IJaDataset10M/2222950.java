package pt.utl.ist.lucene.utils;

import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Strings {

    /*******************************************
     * Returns a list of Strings given a str width
     * spaces. If String == null returns null
     *
     * @param str to tokenize
     * @param delim delim string
     * @return list of Strings
     * */
    public static List<String> getListStrings(String str, String delim) {
        if (str == null) return null;
        List<String> strs = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(str, delim);
        while (st.hasMoreTokens()) {
            strs.add(st.nextToken());
        }
        return strs;
    }

    static Pattern yearPattern1 = Pattern.compile("[1-9][0-9][0-9?\\-*][0-9?\\-*]-[1-9][0-9][0-9?\\-*][0-9?\\-*]");

    static Pattern yearPattern2 = Pattern.compile("[1-9][0-9][0-9?\\-*][0-9?\\-*]");

    public static YearPattern findYears(String date) {
        Matcher myMatcher = yearPattern1.matcher(date);
        if (myMatcher.find()) {
            String found = date.substring(myMatcher.start(), myMatcher.end());
            String start = found.substring(0, 4);
            String end = found.substring(5, 9);
            start = start.replace('-', '0').replace('?', '0').replace('*', '0');
            end = end.replace('-', '9').replace('?', '9').replace('*', '9');
            return new YearPattern(-1, Integer.parseInt(start), Integer.parseInt(end));
        }
        myMatcher = yearPattern2.matcher(date);
        if (myMatcher.find()) {
            String found = date.substring(myMatcher.start(), myMatcher.end());
            found = found.replace('-', '5').replace('?', '5').replace('*', '5');
            return new YearPattern(Integer.parseInt(found), -1, -1);
        }
        return null;
    }

    public static String cleanSpacesTabsLineBreak(String txt) {
        return txt.replaceAll("[ \t\r\n]+", " ").trim();
    }

    public static String cleanEndSpaces(String txt) {
        if (txt == null) return txt;
        int spaces = 0;
        for (int i = txt.length() - 1; i >= 0 && txt.charAt(i) == ' '; i--) {
            if (txt.charAt(i) == ' ') spaces++;
        }
        return txt.substring(0, txt.length() - spaces);
    }

    /**
     * To represent years, if point is filled only point matters
     */
    public static class YearPattern {

        private int point;

        private int start;

        private int end;

        public YearPattern(int point, int start, int end) {
            this.point = point;
            this.end = end;
            this.start = start;
        }

        public int getPoint() {
            return point;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }
    }

    public static String getDigits(int number, int digits) {
        StringBuilder finalStr = new StringBuilder();
        int i;
        for (i = digits - 1; i > 0; i--) {
            int parcel = (int) Math.pow(10, i);
            if (number < parcel) finalStr.append('0'); else break;
        }
        if (i > 0) finalStr.append(number);
        return finalStr.toString();
    }

    public String getModDate(int year, int month, int day, int hour, int min, int second, int milisecond) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(getDigits(year, 4));
        strBuilder.append(getDigits(month, 2));
        strBuilder.append(getDigits(day, 2));
        strBuilder.append(getDigits(hour, 2));
        strBuilder.append(getDigits(min, 2));
        strBuilder.append(getDigits(second, 2));
        strBuilder.append(getDigits(milisecond, 3));
        return strBuilder.toString();
    }

    public static void main(String[] args) {
        System.out.println(findYears("1710-[1712").getStart());
        System.out.println(findYears("1710-[1712").getEnd());
        System.out.println(findYears("1710-1712").getStart());
        System.out.println(findYears("1710-1712").getEnd());
        System.out.println(findYears("17??-174-").getStart());
        System.out.println(findYears("17??-174-").getEnd());
        System.out.println(findYears("17**").getPoint());
        System.out.println(findYears("2332").getPoint());
    }
}

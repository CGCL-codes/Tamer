package org.goda.chronic;

import org.goda.chronic.handlers.Handler;
import org.goda.chronic.numerizer.Numerizer;
import org.goda.chronic.repeaters.Repeater;
import org.goda.chronic.tags.Grabber;
import org.goda.chronic.tags.Ordinal;
import org.goda.chronic.tags.Pointer;
import org.goda.chronic.tags.Pointer.PointerType;
import org.goda.chronic.tags.Scalar;
import org.goda.chronic.tags.Separator;
import org.goda.chronic.tags.Tag.Scanner;
import org.goda.chronic.tags.TimeZone;
import org.goda.chronic.utils.Time;
import org.goda.chronic.utils.Token;
import org.goda.time.MutableDateTime;
import org.goda.time.MutableInterval;
import java.util.LinkedList;
import java.util.List;

public class Chronic {

    public static final String VERSION = "0.2.3";

    private Chronic() {
    }

    public static MutableInterval parse(String text) {
        return Chronic.parse(text, new Options());
    }

    /**
     * Parses a string containing a natural language date or time. If the parser
     * can find a date or time, either a Time or Chronic::MutableInterval will be returned
     * (depending on the value of <tt>:guess</tt>). If no date or time can be found,
     * +nil+ will be returned.
     *
     * Options are:
     *
     * [<tt>:context</tt>]
     *     <tt>:past</tt> or <tt>:future</tt> (defaults to <tt>:future</tt>)
     *
     *     If your string represents a birthday, you can set <tt>:context</tt> to <tt>:past</tt>
     *     and if an ambiguous string is given, it will assume it is in the
     *     past. Specify <tt>:future</tt> or omit to set a future context.
     *
     * [<tt>:now</tt>]
     *     Time (defaults to Time.now)
     *
     *     By setting <tt>:now</tt> to a Time, all computations will be based off
     *     of that time instead of Time.now
     *
     * [<tt>:guess</tt>]
     *     +true+ or +false+ (defaults to +true+)
     *
     *     By default, the parser will guess a single point in time for the
     *     given date or time. If you'd rather have the entire time MutableInterval returned,
     *     set <tt>:guess</tt> to +false+ and a Chronic::MutableInterval will be returned.
     *
     * [<tt>:ambiguous_time_range</tt>]
     *     Integer or <tt>:none</tt> (defaults to <tt>6</tt> (6am-6pm))
     *
     *     If an Integer is given, ambiguous times (like 5:00) will be
     *     assumed to be within the range of that time in the AM to that time
     *     in the PM. For example, if you set it to <tt>7</tt>, then the parser will
     *     look for the time between 7am and 7pm. In the case of 5:00, it would
     *     assume that means 5:00pm. If <tt>:none</tt> is given, no assumption
     *     will be made, and the first matching instance of that time will
     *     be used.
     */
    @SuppressWarnings("unchecked")
    public static MutableInterval parse(String text, Options options) {
        String normalizedText = Chronic.preNormalize(text);
        if (options.isDebug()) {
            System.out.println("Prenormalized:\n" + normalizedText);
        }
        if (normalizedText.matches("\\d?\\d:\\d\\d")) {
            return doMilitaryTime(normalizedText, options);
        }
        if (normalizedText.matches("[01]?\\d\\d\\d")) {
            return doMilitaryTime(normalizedText.replaceAll("([01]?\\d)(\\d\\d)", "$1:$2"), options);
        }
        List<Token> tokens = Chronic.baseTokenize(normalizedText);
        List<Scanner> optionScannerClasses = new LinkedList<Scanner>();
        optionScannerClasses.add(Repeater.SCANNER);
        for (Scanner optionScannerClass : optionScannerClasses) {
            try {
                tokens = optionScannerClass.scan(tokens, options);
            } catch (Throwable e) {
                throw new RuntimeException("Failed to scan tokens.", e);
            }
        }
        List<Scanner> scannerClasses = new LinkedList<Scanner>();
        scannerClasses.add(Grabber.SCANNER);
        scannerClasses.add(Pointer.SCANNER);
        scannerClasses.add(Scalar.SCANNER);
        scannerClasses.add(Ordinal.SCANNER);
        scannerClasses.add(Separator.SCANNER);
        scannerClasses.add(TimeZone.SCANNER);
        for (Scanner scannerClass : scannerClasses) {
            try {
                tokens = scannerClass.scan(tokens, options);
            } catch (Throwable e) {
                throw new RuntimeException("Failed to scan tokens.", e);
            }
        }
        List<Token> taggedTokens = new LinkedList<Token>();
        for (Token token : tokens) {
            if (token.isTagged()) {
                taggedTokens.add(token);
            }
        }
        tokens = taggedTokens;
        if (options.isDebug()) {
            System.out.println("Chronic.parse: " + tokens);
        }
        MutableInterval MutableInterval = Handler.tokensToMutableInterval(tokens, options);
        if (options.isGuess()) {
            MutableInterval = guess(MutableInterval);
        }
        return MutableInterval;
    }

    /**
     * Split the text on spaces and convert each word into
     * a Token
     */
    protected static List<Token> baseTokenize(String text) {
        String[] words = text.split(" ");
        List<Token> tokens = new LinkedList<Token>();
        for (String word : words) {
            tokens.add(new Token(word));
        }
        return tokens;
    }

    protected static MutableInterval guess(MutableInterval mutableInterval) {
        if (mutableInterval == null) {
            return null;
        }
        long guessValue;
        if (Time.getWidth(mutableInterval) > 1) {
            guessValue = mutableInterval.getStart().getMillis() + (Time.getWidth(mutableInterval) / 2);
        } else {
            guessValue = mutableInterval.getStart().getMillis();
        }
        MutableInterval guess = new MutableInterval(guessValue, guessValue);
        return guess;
    }

    /**
     * Convert number words to numbers (three => 3)
     */
    protected static String numericizeNumbers(String text) {
        return Numerizer.numerize(text);
    }

    /**
     * Convert ordinal words to numeric ordinals (third => 3rd)
     */
    protected static String numericizeOrdinals(String text) {
        return text;
    }

    /**
     * Clean up the specified input text by stripping unwanted characters,
     * converting idioms to their canonical form, converting number words
     * to numbers (three => 3), and converting ordinal words to numeric
     * ordinals (third => 3rd)
     */
    protected static String preNormalize(String text) {
        String normalizedText = text.toLowerCase();
        normalizedText = replaceMilitaryTime(normalizedText);
        normalizedText = normalizedText.replaceAll("(\\d)(a)[m]?(\\b)", "$1 $2m$3");
        normalizedText = normalizedText.replaceAll("(\\d)(p)[m]?(\\b)", "$1 $2m$3");
        normalizedText = normalizedText.replaceAll("(^| )([01]\\d)(\\d\\d) (pm|am|p|a|\\W)", "$1$2:$3$4");
        normalizedText = normalizedText.replaceAll("\\b(\\d\\d)(\\d\\d)(\\d\\d\\d\\d)\\b", "$1/$2/$3");
        normalizedText = normalizedText.replaceAll("(\\d?\\d:\\d\\d [p|a]m)\\W(\\d?\\d/\\d?\\d/\\d\\d\\d?\\d?)", "$2 $1");
        System.out.println("PRE COMMA " + normalizedText);
        normalizedText = Chronic.commaCheck(normalizedText);
        System.out.println("POST COMMA " + normalizedText);
        normalizedText = Chronic.numericizeNumbers(normalizedText);
        normalizedText = normalizedText.replaceAll("['\"]", "");
        normalizedText = normalizedText.replaceAll("([/\\-,@])", " $1 ");
        normalizedText = normalizedText.replaceAll("\\btoday\\b", "this day");
        normalizedText = normalizedText.replaceAll("\\btomm?orr?ow\\b", "next day");
        normalizedText = normalizedText.replaceAll("\\byesterday\\b", "last day");
        normalizedText = normalizedText.replaceAll("\\bnoon\\b", "12:00");
        normalizedText = normalizedText.replaceAll("\\bmidnight\\b", "24:00");
        normalizedText = normalizedText.replaceAll("\\bbefore now\\b", "past");
        normalizedText = normalizedText.replaceAll("\\bnow\\b", "this second");
        normalizedText = normalizedText.replaceAll("\\b(ago|before)\\b", "past");
        normalizedText = normalizedText.replaceAll("\\bthis past\\b", "last");
        normalizedText = normalizedText.replaceAll("\\bthis last\\b", "last");
        normalizedText = normalizedText.replaceAll("\\b(?:in|during) the (morning)\\b", "$1");
        normalizedText = normalizedText.replaceAll("\\b(?:in the|during the|at) (afternoon|evening|night)\\b", "$1");
        normalizedText = normalizedText.replaceAll("\\btonight\\b", "this night");
        normalizedText = normalizedText.replaceAll("(?=\\w)([ap]m|oclock)\\b", " $1");
        normalizedText = normalizedText.replaceAll("\\b(hence|after|from)\\b", "future");
        normalizedText = Chronic.numericizeOrdinals(normalizedText);
        return normalizedText.trim();
    }

    private static MutableInterval doMilitaryTime(String text, Options options) {
        MutableDateTime time = new MutableDateTime();
        String[] values = text.split(":");
        time.setHourOfDay(Integer.parseInt(values[0]));
        time.setMinuteOfHour(Integer.parseInt(values[1]));
        if (time.isAfterNow() && (options.getContext() == PointerType.PAST)) {
            time.addDays(-1);
        }
        MutableInterval result = new MutableInterval(time, time);
        result.setStart(time);
        result.setEnd(time);
        return result;
    }

    private static String commaCheck(String input) {
        String[] monthish = new String[] { "jan", "january", "feb", "february", "mar", "march", "apr", "april", "may", "jun", "june", "jul", "july", "aug", "august", "sep", "sept", "september", "oct", "october", "nov", "november", "dec", "december" };
        for (String s : monthish) {
            if (input.matches(".*(" + s + ") (\\d?\\d) (\\d\\d\\d\\d).*")) {
                input = input.replaceAll("(.*)(" + s + ") (\\d?\\d) (\\d\\d\\d\\d)", "$3 $2 $4 $1");
                if (input.matches(".*\\d\\d \\d?\\d:\\d\\d am.*")) {
                    input = input.replaceAll("\\d\\d:\\d\\d am", "at $0");
                }
                break;
            }
        }
        return input;
    }

    private static String replaceMilitaryTime(String input) {
        if ((input.indexOf("am") != -1) || (input.indexOf("pm") != -1) || input.matches(".*\\d[a|p]\\b.*")) {
            return input;
        }
        String replaced = input.replaceAll("([0|1]\\d):(\\d\\d)", "###$1:$2###");
        if (replaced.indexOf("###") != -1) {
            int start = replaced.indexOf("###") + 3;
            String time = replaced.substring(start, replaced.indexOf("###", start));
            String[] split = time.split(":");
            int hours = Integer.parseInt(split[0]);
            if (hours >= 12) {
                return replaced.replaceAll("###.*###", (((hours - 12) == 0) ? 12 : (hours - 12)) + ":" + split[1] + " pm");
            } else {
                return replaced.replaceAll("###.*###", hours + ":" + split[1] + " am");
            }
        }
        return replaced;
    }
}

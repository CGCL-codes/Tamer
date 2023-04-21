package de.ddb.conversion.util;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import de.ddb.charset.EightBitCharset;
import de.ddb.conversion.format.Format;

public class RecordsSeperator {

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(RecordsSeperator.class);

    private static final Charset IDENTITY_CHARSET = new EightBitCharset();

    public static List splitRecords(String stringInput, Format format) {
        return splitRecords(stringInput, format, false);
    }

    public static List splitRecords(byte[] input, Format format) {
        return splitRecords(input, format, false);
    }

    public static List splitRecords(byte[] input, Format format, boolean appendRest) {
        String stringInput = IDENTITY_CHARSET.decode(ByteBuffer.wrap(input)).toString();
        if (format.getRecordPattern() != null) {
            return splitRecordsUsingRecordPattern(stringInput, format.getRecordPattern(), IDENTITY_CHARSET, appendRest);
        }
        if (format.getRecordDelimiterPattern() != null) {
            return splitRecordsUsingRecordDelimiterPattern(stringInput, format.getRecordDelimiterPattern(), IDENTITY_CHARSET, appendRest);
        }
        if (format.getStartOfRecordPattern() != null) {
            return splitRecordsUsingStartOfRecordPattern(stringInput, format.getStartOfRecordPattern(), IDENTITY_CHARSET);
        }
        if (format.getEndOfRecordPattern() != null) {
            return splitRecordsUsingEndOfRecordPattern(stringInput, format.getEndOfRecordPattern(), IDENTITY_CHARSET, appendRest);
        }
        return Collections.singletonList(input);
    }

    public static List splitRecords(String stringInput, Format format, boolean appendRest) {
        if (format.getRecordPattern() != null) {
            return splitRecordsUsingRecordPattern(stringInput, format.getRecordPattern(), null, appendRest);
        }
        if (format.getRecordDelimiterPattern() != null) {
            return splitRecordsUsingRecordDelimiterPattern(stringInput, format.getRecordDelimiterPattern(), null, appendRest);
        }
        if (format.getStartOfRecordPattern() != null) {
            return splitRecordsUsingStartOfRecordPattern(stringInput, format.getStartOfRecordPattern(), null);
        }
        if (format.getEndOfRecordPattern() != null) {
            return splitRecordsUsingEndOfRecordPattern(stringInput, format.getEndOfRecordPattern(), null, appendRest);
        }
        return Collections.singletonList(stringInput);
    }

    public static List splitRecordsUsingRecordPattern(String input, Pattern recordPattern, Charset outputCharset, boolean appendRest) {
        ArrayList records = new ArrayList();
        Matcher recordMatcher = recordPattern.matcher(input);
        int lastPatternEnd = 0;
        while (recordMatcher.find()) {
            String record = input.substring(recordMatcher.start(), recordMatcher.end());
            lastPatternEnd = recordMatcher.end();
            addRecord(record, records, outputCharset);
        }
        if (appendRest && input.length() > lastPatternEnd) {
            String record = input.substring(lastPatternEnd);
            addRecord(record, records, outputCharset);
        }
        return records;
    }

    public static List splitRecordsUsingEndOfRecordPattern(String input, Pattern pattern, Charset outputCharset, boolean appendRest) {
        ArrayList records = new ArrayList();
        Matcher matcher = pattern.matcher(input);
        int recordStart = 0;
        int recordEnd = 0;
        int lastPatternEnd = 0;
        while (matcher.find()) {
            recordStart = recordEnd;
            recordEnd = matcher.end();
            lastPatternEnd = matcher.end();
            int recordLength = recordEnd - recordStart;
            if (recordLength > 0) {
                String record = input.substring(recordStart, recordEnd);
                addRecord(record, records, outputCharset);
            }
        }
        if (appendRest && input.length() > lastPatternEnd) {
            String record = input.substring(lastPatternEnd);
            addRecord(record, records, outputCharset);
        }
        return records;
    }

    public static List splitRecordsUsingStartOfRecordPattern(String input, Pattern pattern, Charset outputCharset) {
        ArrayList records = new ArrayList();
        Matcher matcher = pattern.matcher(input);
        if (!matcher.find()) {
            throw new IllegalArgumentException();
        }
        int recordStart = matcher.start();
        int recordEnd = input.length();
        while (matcher.find()) {
            recordEnd = matcher.start();
            int recordLength = recordEnd - recordStart;
            if (recordLength > 0) {
                String record = input.substring(recordStart, recordEnd);
                addRecord(record, records, outputCharset);
            }
            recordStart = recordEnd;
        }
        if (recordEnd < input.length()) {
            String record = input.substring(recordEnd);
            addRecord(record, records, outputCharset);
        }
        return records;
    }

    private static List splitRecordsUsingRecordDelimiterPattern(String input, Pattern pattern, Charset outputCharset, boolean appendRest) {
        ArrayList records = new ArrayList();
        Matcher matcher = pattern.matcher(input);
        int recordStart = 0;
        int recordEnd = 0;
        int lastPatternEnd = 0;
        while (matcher.find()) {
            recordStart = lastPatternEnd;
            recordEnd = matcher.start() - 1;
            lastPatternEnd = matcher.end();
            int recordLength = recordEnd - recordStart;
            if (recordLength > 0) {
                String record = input.substring(recordStart, recordEnd);
                addRecord(record, records, outputCharset);
            }
        }
        if (appendRest && input.length() > lastPatternEnd) {
            String record = input.substring(lastPatternEnd);
            addRecord(record, records, outputCharset);
        }
        return records;
    }

    private static void addRecord(String record, List list, Charset charset) {
        if (charset != null) {
            list.add(charset.encode(record).array());
        } else {
            list.add(record);
        }
    }
}

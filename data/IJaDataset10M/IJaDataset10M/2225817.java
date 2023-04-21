package edu.washington.mysms.server.sample.starbus;

import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.washington.mysms.Message;
import edu.washington.mysms.coding.ImproperEncodingException;
import edu.washington.mysms.coding.QueryEncoder;
import edu.washington.mysms.server.extend.DDFRepresentation;

public class RussianMLUserQueryDecoder extends UserQueryDecoder {

    public static final String ENGLISH = "English";

    public static final String RUSSIAN = "Russian";

    public static final String LATIN = "ASCII";

    public static final String CYRILLIC = "UTF-16";

    public static final Pattern ENGLISH_PREDICT_PATTERN = Pattern.compile("^(.+?) " + "to (.+)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    public static final Pattern RUSSIAN_LATIN_PREDICT_PATTERN = Pattern.compile("^(.+?) " + "v (.+)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    public static final Pattern RUSSIAN_CYRILLIC_PREDICT_PATTERN = Pattern.compile("^(.+?) " + "в (.+)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    public static final Pattern ENGLISH_UPDATE_PATTERN = Pattern.compile("^(.+?) " + "to (.+) update", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    public static final Pattern RUSSIAN_LATIN_UPDATE_PATTERN = Pattern.compile("^(.+?) " + "v (.+) obnovleniye", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    public static final Pattern RUSSIAN_CYRILLIC_UPDATE_PATTERN = Pattern.compile("^(.+?) " + "в (.+) обновление", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    public static final Pattern ENGLISH_UPDATE_ABSOLUTE_PATTERN = Pattern.compile("^(.+?) " + "to (.+) update absolute", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    public static final Pattern ENGLISH_STORE_PATTERN = Pattern.compile("^store (.+?) " + "as (.+)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    public static final Pattern RUSSIAN_LATIN_STORE_PATTERN = Pattern.compile("^soxranit' (.+?) " + "kak (.+)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    public static final Pattern RUSSIAN_CYRILLIC_STORE_PATTERN = Pattern.compile("^сохранить (.+?) " + "как (.+)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    public static final Pattern ENGLISH_SHOW_LOCATIONS_PATTERN = Pattern.compile("^show locations", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    public static final Pattern LOCATION_PATTERN = Pattern.compile("(.+?):" + "(.+)");

    @Override
    public String decode(Message<?> message, ByteBuffer buffer) throws Exception {
        Matcher matcher;
        String query = TriageQueryDecoder.decodeMultilingualBuffer(buffer).trim();
        matcher = ENGLISH_UPDATE_ABSOLUTE_PATTERN.matcher(query);
        if (matcher.find()) {
            return "SUBSCRIBE TO " + parseUpdateAbsolute(message.getAddress(), matcher, ENGLISH, LATIN).toString();
        }
        matcher = ENGLISH_UPDATE_PATTERN.matcher(query);
        if (matcher.find()) {
            return "SUBSCRIBE TO " + parseUpdate(message.getAddress(), matcher, ENGLISH, LATIN).toString();
        }
        matcher = RUSSIAN_LATIN_UPDATE_PATTERN.matcher(query);
        if (matcher.find()) {
            return "SUBSCRIBE TO " + parseUpdate(message.getAddress(), matcher, RUSSIAN, LATIN).toString();
        }
        matcher = RUSSIAN_CYRILLIC_UPDATE_PATTERN.matcher(query);
        if (matcher.find()) {
            return "SUBSCRIBE TO " + parseUpdate(message.getAddress(), matcher, RUSSIAN, CYRILLIC).toString();
        }
        matcher = ENGLISH_PREDICT_PATTERN.matcher(query);
        if (matcher.find()) {
            return parsePredict(message.getAddress(), matcher, ENGLISH, LATIN).toString();
        }
        matcher = RUSSIAN_LATIN_PREDICT_PATTERN.matcher(query);
        if (matcher.find()) {
            return parsePredict(message.getAddress(), matcher, RUSSIAN, LATIN).toString();
        }
        matcher = RUSSIAN_CYRILLIC_PREDICT_PATTERN.matcher(query);
        if (matcher.find()) {
            return parsePredict(message.getAddress(), matcher, RUSSIAN, CYRILLIC).toString();
        }
        matcher = ENGLISH_STORE_PATTERN.matcher(query);
        if (matcher.find()) {
            return parseStore(message, matcher, ENGLISH, LATIN).toString();
        }
        matcher = RUSSIAN_LATIN_STORE_PATTERN.matcher(query);
        if (matcher.find()) {
            return parseStore(message, matcher, RUSSIAN, LATIN).toString();
        }
        matcher = RUSSIAN_CYRILLIC_STORE_PATTERN.matcher(query);
        if (matcher.find()) {
            return parseStore(message, matcher, RUSSIAN, CYRILLIC).toString();
        }
        matcher = ENGLISH_SHOW_LOCATIONS_PATTERN.matcher(query);
        if (matcher.find()) {
            return parseShow(message, matcher, ENGLISH, LATIN).toString();
        }
        throw new ImproperEncodingException("Given query is not a valid user query: " + query);
    }

    private DDFRepresentation.Values parseUpdate(String address, Matcher matcher, String language, String charset) {
        DDFRepresentation.Values ddf;
        DecodedGPSPoint coordinates = GPSPointCoder.coordinateCheck(matcher.group(2));
        if (coordinates == null) {
            ddf = new DDFRepresentation.Values("predictSeries");
            ddf.addArgument(address);
            ddf.addArgument(language);
            ddf.addArgument(charset);
            ddf.addArgument(matcher.group(1));
            ddf.addArgument(matcher.group(2));
        } else {
            ddf = new DDFRepresentation.Values("predictSeries_wGPS");
            ddf.addArgument(address);
            ddf.addArgument(language);
            ddf.addArgument(charset);
            ddf.addArgument(matcher.group(1));
            ddf.addArgument(Double.toString(coordinates.getLatitude()));
            ddf.addArgument(Double.toString(coordinates.getLongitude()));
            ddf.addArgument(Integer.toString(coordinates.getOriginalCoding().ordinal()));
        }
        return ddf;
    }

    private DDFRepresentation.Values parseUpdateAbsolute(String address, Matcher matcher, String language, String charset) {
        DDFRepresentation.Values ddf;
        DecodedGPSPoint coordinates = GPSPointCoder.coordinateCheck(matcher.group(2));
        if (coordinates == null) {
            ddf = new DDFRepresentation.Values("predictSeriesAbsolute");
            ddf.addArgument(address);
            ddf.addArgument(language);
            ddf.addArgument(charset);
            ddf.addArgument(matcher.group(1));
            ddf.addArgument(matcher.group(2));
        } else {
            ddf = new DDFRepresentation.Values("predictSeriesAbsolute_wGPS");
            ddf.addArgument(address);
            ddf.addArgument(language);
            ddf.addArgument(charset);
            ddf.addArgument(matcher.group(1));
            ddf.addArgument(Double.toString(coordinates.getLatitude()));
            ddf.addArgument(Double.toString(coordinates.getLongitude()));
            ddf.addArgument(Integer.toString(coordinates.getOriginalCoding().ordinal()));
        }
        return ddf;
    }

    private DDFRepresentation.Values parsePredict(String address, Matcher matcher, String language, String charset) {
        DDFRepresentation.Values ddf;
        DecodedGPSPoint coordinates = GPSPointCoder.coordinateCheck(matcher.group(2));
        if (coordinates == null) {
            ddf = new DDFRepresentation.Values("predict");
            ddf.addArgument(address);
            ddf.addArgument(language);
            ddf.addArgument(charset);
            ddf.addArgument(matcher.group(1));
            ddf.addArgument(matcher.group(2));
        } else {
            ddf = new DDFRepresentation.Values("predict_wGPS");
            ddf.addArgument(address);
            ddf.addArgument(language);
            ddf.addArgument(charset);
            ddf.addArgument(matcher.group(1));
            ddf.addArgument(Double.toString(coordinates.getLatitude()));
            ddf.addArgument(Double.toString(coordinates.getLongitude()));
            ddf.addArgument(Integer.toString(coordinates.getOriginalCoding().ordinal()));
        }
        return ddf;
    }

    private DDFRepresentation.Values parseStore(Message<?> message, Matcher matcher, String language, String charset) {
        DDFRepresentation.Values ddf;
        DecodedGPSPoint coordinates = GPSPointCoder.coordinateCheck(matcher.group(1));
        if (coordinates == null) {
            ddf = new DDFRepresentation.Values("storeLocation");
            ddf.addArgument(message.getAddress());
            ddf.addArgument(language);
            ddf.addArgument(charset);
            ddf.addArgument(Long.toString(super.convertNetworkTimeToUTC(message.getInitialTime()).getTime()));
            ddf.addArgument(matcher.group(1));
        } else {
            ddf = new DDFRepresentation.Values("storeLocation_wGPS");
            ddf.addArgument(message.getAddress());
            ddf.addArgument(language);
            ddf.addArgument(charset);
            ddf.addArgument(Double.toString(coordinates.getLatitude()));
            ddf.addArgument(Double.toString(coordinates.getLongitude()));
            ddf.addArgument(Integer.toString(coordinates.getOriginalCoding().ordinal()));
        }
        String locationName = matcher.group(2);
        matcher = LOCATION_PATTERN.matcher(locationName);
        if (matcher.find()) {
            ddf.addArgument(matcher.group(1).trim());
            ddf.addArgument(matcher.group(2).trim());
        } else {
            ddf.addArgument(locationName.trim());
        }
        return ddf;
    }

    private DDFRepresentation.Values parseShow(Message<?> message, Matcher matcher, String language, String charset) {
        DDFRepresentation.Values ddf = new DDFRepresentation.Values("showLocations");
        ddf.addArgument(message.getAddress());
        ddf.addArgument(language);
        ddf.addArgument(charset);
        return ddf;
    }

    @Override
    public QueryEncoder getEncoder() {
        return null;
    }

    @Override
    public byte getEncodingType() {
        return 0;
    }
}

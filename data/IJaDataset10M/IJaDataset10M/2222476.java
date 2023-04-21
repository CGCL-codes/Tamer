package opl.textc.parser;

public abstract class FeedParserFactory {

    static String feedUrl = "http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a=ttc&r=506";

    public static StopParser getParser() {
        return getParser(ParserType.ANDROID_SAX);
    }

    public static StopParser getParser(ParserType type) {
        switch(type) {
            case ANDROID_SAX:
                return new AndroidSaxFeedParser(feedUrl);
            default:
                return null;
        }
    }
}

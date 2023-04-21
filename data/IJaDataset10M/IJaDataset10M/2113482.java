package in.raam.twsh.util;

import java.io.File;

/**
 * Generic constants class to be used across the application
 * @author raam
 *
 */
public final class Constants {

    public static final String TWITTER_REQUEST_TOKEN_URL = "https://api.twitter.com/oauth/request_token";

    public static final String TWITTER_ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";

    public static final String TWITTER_AUTHOROIZE_URL = "https://api.twitter.com/oauth/authorize";

    public static final String HOME_TIMELINE_URL = "http://api.twitter.com/1/statuses/home_timeline.json";

    public static final String TWITTER_UPDATE_URL = "http://api.twitter.com/1/statuses/update.json";

    public static final String TWITTER_FOLLOWERS_URL = "http://api.twitter.com/1/statuses/followers.json";

    public static final String TWITTER_FOLLOWING_URL = "http://api.twitter.com/1/statuses/friends.json";

    public static final String TWITTER_MENTIONS_URL = "http://api.twitter.com/1/statuses/mentions.json";

    public static final String TWITTER_RT_OF_ME_URL = "http://api.twitter.com/1/statuses/retweets_of_me.json";

    public static final String TWITTER_RT_BY_ME_URL = "http://api.twitter.com/1/statuses/retweeted_by_me.json";

    public static final String TWEET_DESTROY_URL = "http://api.twitter.com/1/statuses/destroy/${id}.json";

    public static final String RETWEET_URL = "http://api.twitter.com/1/statuses/retweet/${id}.json";

    public static final String FOLLOW_URL = "http://api.twitter.com/1/friendships/create/${id}.json";

    public static final String DONT_FOLLOW_URL = "http://api.twitter.com/1/friendships/destroy/${id}.json";

    public static final String FS = File.separator;

    public static final String LS = System.getProperty("line.separator");
}

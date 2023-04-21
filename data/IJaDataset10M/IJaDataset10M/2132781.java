package au.edu.ausstage.tweetgatherer;

import au.edu.ausstage.utils.*;
import com.crepezzi.tweetstream4j.*;
import com.crepezzi.tweetstream4j.types.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A class the handles incoming Tweets and other messages from Twitter
 */
public class TweetHandler implements TwitterStreamHandler {

    private LinkedBlockingQueue<STweet> newTweets;

    private LinkedBlockingQueue<SDeletion> newDeletes;

    /**
	 * A constructor for this class
	 *
	 * @param tweets  a blocking queue used to store new messages
	 * @param deletes a blocking queue used to store new deletion requests
	 */
    public TweetHandler(LinkedBlockingQueue<STweet> tweets, LinkedBlockingQueue<SDeletion> deletes) {
        newTweets = tweets;
        newDeletes = deletes;
    }

    /**
	 * A method to process a new tweet message from the stream
	 *
	 * @param tweet the new Tweet
	 */
    public void addTweet(STweet tweet) {
        try {
            newTweets.put(tweet);
        } catch (InterruptedException ex) {
            System.err.println("ERROR: An error occured while attempting to add a tweet to the queue");
            System.err.println("       " + tweet.toString());
        }
    }

    /**
	 * A method to process a deletion message from the stream
	 *
	 * @param deletion Incoming deletion request
	 */
    public void addDeletion(SDeletion deletion) {
        try {
            newDeletes.put(deletion);
        } catch (InterruptedException ex) {
            System.err.println("ERROR: An error occured while attempting to add a deletion to the queue");
            System.err.println("       " + deletion.toString());
        }
    }

    /**
	 * A method to process a new limit message from the stream
	 *
	 * From the docs: http://dev.twitter.com/pages/streaming_api_concepts
	 * Track streams may also contain limitation notices, where the integer track is an enumeration of statuses that, since the start of the connection, matched the track predicate but were rate limited.
	 * @param limit Incoming limit message
	 */
    public void addLimit(SLimit limit) {
        System.out.println("INFO: New Limit Notice Recieved: " + limit.toString());
    }

    /**
	 * What to do when this handler has been request to stop
	 */
    public void stop() {
        System.out.println("INFO: Tweet handler has stopped.");
    }
}

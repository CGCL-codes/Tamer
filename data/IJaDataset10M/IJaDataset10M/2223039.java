package net.onlinepresence.opos.dataManager.mediators.twitter.util;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class TwitterStuff {

    private Twitter twitter;

    private User twitterUser;

    private Status twitterStatus;

    public TwitterStuff(Twitter twitter) throws TwitterException {
        this.twitter = twitter;
        twitterUser = twitter.verifyCredentials();
        twitterStatus = twitterUser.getStatus();
    }

    /**
	 * @return the twitter
	 */
    public Twitter getTwitter() {
        return twitter;
    }

    /**
	 * @param twitter the twitter to set
	 */
    public void setTwitter(Twitter twitter) {
        this.twitter = twitter;
    }

    /**
	 * @return the twitterUser
	 */
    public User getTwitterUser() {
        return twitterUser;
    }

    /**
	 * @param twitterUser the twitterUser to set
	 */
    public void setTwitterUser(User twitterUser) {
        this.twitterUser = twitterUser;
    }

    /**
	 * @return the twitterStatus
	 */
    public Status getTwitterStatus() {
        return twitterStatus;
    }

    /**
	 * @param twitterStatus the twitterStatus to set
	 */
    public void setTwitterStatus(Status twitterStatus) {
        this.twitterStatus = twitterStatus;
    }

    @Override
    public String toString() {
        return "twitter: " + twitter + ", twitterUser: " + twitterUser + ", twitterStatus: " + twitterStatus;
    }
}

package com.google.gdata.client.appsforyourdomain;

import com.google.gdata.client.Query;
import java.net.URL;
import java.util.List;

/**
 * The AppsForYourDomainQuery class extends the base GData Query class to
 * define convenient APIs for AppsForYourDomain custom query parameters.
 */
public class AppsForYourDomainQuery extends Query {

    public static final String USERNAME = "username";

    public static final String RECIPIENT = "recipient";

    public static final String START_EMAILLIST_NAME = "startEmailListName";

    public static final String START_NICKNAME = "startNickname";

    public static final String START_RECIPIENT = "startRecipient";

    public static final String START_USERNAME = "startUsername";

    /**
   * Constructs a new AppsForYourDomainQuery object that targets a feed.  The
   * initial state of the query contains no parameters, meaning all entries
   * in the feed would be returned if the query was executed immediately
   * after construction.
   *
   * @param feedUrl the URL of the feed against which queries will be
   *                executed.
   */
    public AppsForYourDomainQuery(URL feedUrl) {
        super(feedUrl);
    }

    /**
   * Sets the username to restrict the retrieval of NicknameFeed entries.
   *
   * @param username The username for we wish to obtain the nicknames.
   */
    public void setUsername(String username) {
        setParameter(USERNAME, username);
    }

    /**
   * @return the username that will be found in all NicknameFeed entries.
   *         A value of {@code null} indicates that username based querying
   *         is disabled.
   */
    public String getUsername() {
        return getParameter(USERNAME);
    }

    /**
   * Sets the recipient for which we want to conduct email list searches.
   *
   * @param recipient The recipient for which we want to obtain email list
   * 		      subscription.
   */
    public void setRecipient(String recipient) {
        setParameter(RECIPIENT, recipient);
    }

    /**
   * @return the recipient that will be found in all EmailListFeed entries.
   *         A value of {@code null} indicates that recipient based querying
   *         is disabled.
   */
    public String getRecipient() {
        return getParameter(RECIPIENT);
    }

    /**
   * Sets the startUsername for which we want to conduct user retrievals.
   *
   * @param startUsername The username at which we want to start our user
   * retrieval.
   */
    public void setStartUsername(String startUsername) {
        setParameter(START_USERNAME, startUsername);
    }

    /**
   * @return the starting username of the UserFeed we are retrieving.  All
   * users returned will have usernames occuring at or after this value.  A
   * value of {@code null} indicates that startUsername based querying
   * is disabled.
   */
    public String getStartUsername() {
        return getParameter(START_USERNAME);
    }

    /**
   * Sets the startEmailListName for which we want to conduct email list
   * retrievals.
   *
   * @param startEmailListName The emailList name at which we want to start our
   * email list retrieval.
   */
    public void setStartEmailListName(String startEmailListName) {
        setParameter(START_EMAILLIST_NAME, startEmailListName);
    }

    /**
   * @return the starting emailList name of the EmailListFeed we are
   * retrieving.  All emailLists returned will have names occuring at or after
   * this value.  A value of {@code null} indicates that recipient based querying
   * is disabled.
   */
    public String getStartEmailListName() {
        return getParameter(START_EMAILLIST_NAME);
    }

    /**
   * Sets the startNickname for which we want to conduct nickname retrievals. 
   *
   * @param startNickname The nickname at which we want to start our nickname
   * retrieval.
   */
    public void setStartNickname(String startNickname) {
        setParameter(START_NICKNAME, startNickname);
    }

    /**
   * @return the starting nickname of the NicknameFeed we are retrieving.  All
   * nicknames returned will have names occuring at or after this value. A
   * value of {@code null} indicates that recipient based querying is disabled.
   */
    public String getStartNickname() {
        return getParameter(START_NICKNAME);
    }

    /**
   * Sets the startRecipient for which we want to conduct email list recipient
   * retrievals.
   *
   * @param startRecipient The recipient at which we want to start our email
   * list recipient retrieval.
   */
    public void setStartRecipient(String startRecipient) {
        setParameter(START_RECIPIENT, startRecipient);
    }

    /**
   * @return the starting recipient of the EmailListRecipientFeed we are
   * retrieving.  All recipients returned will have email addresses occuring at
   * or after this value.  A value of {@code null} indicates that recipient
   * based querying is disabled.
   */
    public String getStartRecipient() {
        return getParameter(START_RECIPIENT);
    }

    protected void setParameter(String parameterKey, String parameterValue) {
        List<CustomParameter> customParams = getCustomParameters();
        for (CustomParameter existingValue : getCustomParameters(parameterKey)) {
            customParams.remove(existingValue);
        }
        if (parameterValue != null) {
            customParams.add(new CustomParameter(parameterKey, parameterValue));
        }
    }

    protected String getParameter(String parameterKey) {
        List<CustomParameter> params = getCustomParameters(parameterKey);
        return (params.size() > 0) ? params.get(0).getValue() : null;
    }
}

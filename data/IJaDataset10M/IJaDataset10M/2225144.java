package org.encuestame.persistence.dao;

import java.util.List;
import org.encuestame.persistence.domain.AccessRate;
import org.encuestame.persistence.domain.HashTag;
import org.encuestame.persistence.domain.Hit;
import org.encuestame.persistence.domain.security.UserAccount;
import org.encuestame.persistence.domain.survey.Poll;
import org.encuestame.persistence.domain.survey.Survey;
import org.encuestame.persistence.domain.tweetpoll.TweetPoll;
import org.encuestame.persistence.domain.tweetpoll.TweetPollSavedPublishedStatus;
import org.encuestame.utils.enums.TypeSearchResult;

/**
 * Implementation for FrontEnd Dao.
 * @author Picado, Juan juanATencuestame.org
 * @since Oct 16, 2010 10:53:48 PM
 * @version $Id:$
 */
public interface IFrontEndDao extends IBaseDao {

    /**
     * Get TweetPoll Last 30 Days
     * @param maxResults max results
     * @return list of tweetPoll.
     */
    List<TweetPoll> getTweetPollFrontEndLast30Days(final Integer start, final Integer maxResults);

    /**
     * Get TweetPoll Last 7 Days
     * @param maxResults max results
     * @return list of tweetPoll.
     */
    List<TweetPoll> getTweetPollFrontEndLast7Days(final Integer start, final Integer maxResults);

    /**
     * Get TweetPoll Last 24 Hours.
     * @param maxResults max results
     * @return list of tweetPoll.
     */
    List<TweetPoll> getTweetPollFrontEndLast24(final Integer start, final Integer maxResults);

    /**
     * Get Poll Last 24 Hours.
     * @param maxResults max results
     * @return list of tweetPoll.
     */
    List<Poll> getPollFrontEndLast24(final Integer start, final Integer maxResults);

    /**
     * Get Poll Last 7 Days
     * @param maxResults max results
     * @return list of tweetPoll.
     */
    List<Poll> getPollFrontEndLast7Days(final Integer start, final Integer maxResults);

    /**
     * Get Poll Last 30 Days
     * @param maxResults max results
     * @return list of tweetPoll.
     */
    List<Poll> getPollFrontEndLast30Days(final Integer start, final Integer maxResults);

    /**
     * Get Poll on All Time.
     * @param maxResults max results
     * @return list of tweetPoll.
     */
    List<Poll> getPollFrontEndAllTime(final Integer start, final Integer maxResults);

    /**
     * Get TweetPoll all time.
     * @param maxResults max results
     * @return list of tweetPoll.
     */
    List<TweetPoll> getTweetPollFrontEndAllTime(final Integer start, final Integer maxResults);

    /**
     * Get hits by ip and type.
     * @param ipAddress
     * @param id
     * @param searchHitby
     * @return
     */
    List<Hit> getHitsByIpAndType(final String ipAddress, final Long id, final TypeSearchResult searchHitby);

    /**
     * Get hits by type.
     * @param id
     * @param searchtype
     * @return
     */
    Long getTotalHitsbyType(final Long id, final TypeSearchResult searchtype);

    /**
     * Get access rate by item.
     * @param ipAddress
     * @param itemId
     * @param searchHitby
     * @return
     */
    List<AccessRate> getAccessRatebyItem(final String ipAddress, final Long itemId, final TypeSearchResult searchHitby);

    /**
     * Get Survey Last 30 Days.
     * @param start
     * @param maxResults
     * @return
     */
    List<Survey> getSurveyFrontEndLast30Days(final Integer start, final Integer maxResults);

    /**
     * Get Survey Last 7 Days.
     * @param start
     * @param maxResults
     * @return
     */
    List<Survey> getSurveyFrontEndLast7Days(final Integer start, final Integer maxResults);

    /**
     * Get Survey Last 24 Hours.
     * @param start
     * @param maxResults
     * @return
     */
    List<Survey> getSurveyFrontEndLast24(final Integer start, final Integer maxResults);

    /**
     * Get Survey on All Time.
     * @param start
     * @param maxResults
     * @return
     */
    List<Survey> getSurveyFrontEndAllTime(final Integer start, final Integer maxResults);

    /**
     * Get Links by Home Item.
     * @param tweetPoll
     * @param survey
     * @param poll
     * @param itemType
     * @return
     */
    List<TweetPollSavedPublishedStatus> getLinksByHomeItem(final HashTag hashTag, final UserAccount userAccount, final TweetPoll tweetPoll, final Survey survey, final Poll poll, final TypeSearchResult itemType);
}

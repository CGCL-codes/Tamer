package org.encuestame.utils.json;

import java.io.Serializable;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.encuestame.utils.web.QuestionAnswerBean;
import org.encuestame.utils.web.TweetPollResultsBean;

/**
 * Represents tweetpoll answer switch.
 * @author Picado, Juan juanATencuestame.org
 * @since Apr 15, 2011
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TweetPollAnswerSwitchBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5463311757853384293L;

    @JsonProperty(value = "id")
    private Long id;

    @JsonIgnore
    public TweetPollBean tweetPollBean;

    @JsonProperty(value = "tweet_poll_id")
    public Long tweetPollId;

    @JsonProperty(value = "answer")
    public QuestionAnswerBean answerBean;

    @JsonProperty(value = "short_url")
    public String shortUrl;

    @JsonProperty(value = "results")
    public TweetPollResultsBean resultsBean;

    /**
     * @return the tweetPollBean
     */
    @JsonIgnore
    public TweetPollBean getTweetPollBean() {
        return tweetPollBean;
    }

    /**
     * @param tweetPollBean
     *            the tweetPollBean to set
     */
    public void setTweetPollBean(final TweetPollBean tweetPollBean) {
        this.tweetPollBean = tweetPollBean;
    }

    /**
     * @return the answerBean
     */
    @JsonIgnore
    public QuestionAnswerBean getAnswerBean() {
        return answerBean;
    }

    /**
     * @param answerBean
     *            the answerBean to set
     */
    public void setAnswerBean(final QuestionAnswerBean answerBean) {
        this.answerBean = answerBean;
    }

    /**
     * @return the shortUrl
     */
    @JsonIgnore
    public String getShortUrl() {
        return shortUrl;
    }

    /**
     * @param shortUrl
     *            the shortUrl to set
     */
    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    /**
     * @return the tweetPollId
     */
    @JsonIgnore
    public Long getTweetPollId() {
        return tweetPollId;
    }

    /**
     * @param tweetPollId the tweetPollId to set
     */
    public void setTweetPollId(Long tweetPollId) {
        this.tweetPollId = tweetPollId;
    }

    /**
     * @return the id
     */
    @JsonIgnore
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the resultsBean
     */
    @JsonIgnore
    public TweetPollResultsBean getResultsBean() {
        return resultsBean;
    }

    /**
     * @param resultsBean the resultsBean to set
     */
    public void setResultsBean(TweetPollResultsBean resultsBean) {
        this.resultsBean = resultsBean;
    }
}

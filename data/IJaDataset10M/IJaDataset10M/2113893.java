package org.encuestame.social.api;

import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.encuestame.social.AbstractSocialAPISupport;
import org.encuestame.social.api.support.IdentiCaProfile;
import org.encuestame.social.api.support.IdenticaAPIOperations;
import org.encuestame.social.api.support.IdenticaStatusDetails;
import org.encuestame.utils.TweetPublishedMetadata;
import org.encuestame.utils.social.SocialUserProfile;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Identica API template.
 * @author Picado, Juan juanATencuestame.org
 * @since
 */
public class IdenticaAPITemplate extends AbstractSocialAPISupport implements IdenticaAPIOperations {

    static final int DEFAULT_RESULTS_PER_PAGE = 50;

    static final String API_URL_BASE = "http://identi.ca/api/";

    static final String VERIFY_CREDENTIALS_URL = API_URL_BASE + "account/verify_credentials.json";

    static final String USER_PROFILE_URL = API_URL_BASE + "users/show.json";

    static final String FRIENDS_STATUSES_URL = API_URL_BASE + "statuses/friends.json?screen_name={screen_name}";

    static final String TWEET_URL = API_URL_BASE + "statuses/update.json";

    static final String RETWEET_URL = API_URL_BASE + "/statuses/retweet/{tweet_id}.json";

    static final String MENTIONS_URL = API_URL_BASE + "statuses/mentions.json";

    static final String DIRECT_MESSAGES_URL = API_URL_BASE + "direct_messages.json";

    static final String SEND_DIRECT_MESSAGE_URL = API_URL_BASE + "direct_messages/new.json";

    static final String PUBLIC_TIMELINE_URL = API_URL_BASE + "statuses/public_timeline.json";

    static final String HOME_TIMELINE_URL = API_URL_BASE + "statuses/home_timeline.json";

    static final String FRIENDS_TIMELINE_URL = API_URL_BASE + "statuses/friends_timeline.json";

    static final String USER_TIMELINE_URL = API_URL_BASE + "statuses/user_timeline.json";

    /**
     * Log.
     */
    private Log log = LogFactory.getLog(this.getClass());

    /**
     * Constructor.
     * @param apiKey
     * @param apiSecret
     * @param accessToken
     * @param accessTokenSecret
     */
    public IdenticaAPITemplate(String apiKey, String apiSecret, String accessToken, String accessTokenSecret) {
        setRestTemplate(org.encuestame.oauth1.support.ProtectedResourceClientFactory.create(apiKey, apiSecret, accessToken, accessTokenSecret));
    }

    public String getProfileName() {
        Map<?, ?> response = getRestTemplate().getForObject(VERIFY_CREDENTIALS_URL, Map.class);
        return (String) response.get("screen_name");
    }

    public String getProfileId() {
        Map<?, ?> response = getRestTemplate().getForObject(VERIFY_CREDENTIALS_URL, Map.class);
        return response.get("id").toString();
    }

    public IdentiCaProfile getUserProfile(long userId) {
        Map<?, ?> response = getRestTemplate().getForObject(USER_PROFILE_URL + "?user_id={userId}", Map.class, userId);
        return getProfileFromResponseMap(response);
    }

    public IdentiCaProfile getUserProfile() {
        return this.getUserProfile(Long.valueOf(this.getProfileId()));
    }

    public TweetPublishedMetadata updateStatus(String message) {
        log.debug("Identica updateStatus 2 " + message);
        return this.updateStatus(message, new IdenticaStatusDetails());
    }

    public TweetPublishedMetadata updateStatus(final String message, final IdenticaStatusDetails details) {
        log.debug("Identica updateStatus 1 " + message);
        log.debug("Identica updateStatus 1 " + details);
        final MultiValueMap<String, Object> tweetParams = new LinkedMultiValueMap<String, Object>();
        tweetParams.add("status", message);
        tweetParams.setAll(details.toParameterMap());
        final ResponseEntity<Map> response = getRestTemplate().postForEntity(TWEET_URL, tweetParams, Map.class);
        log.debug("Identica updateStatus " + response.getBody());
        log.debug("Identica updateStatus " + response.getHeaders());
        log.debug("Identica updateStatus " + response.getStatusCode());
        final Map body = response.getBody();
        final TweetPublishedMetadata status = createStatus(message);
        status.setTweetId(body.get("id").toString());
        return status;
    }

    private void handleIdentiCaResponseErrors(ResponseEntity<Map> response) {
    }

    /**
     *
     * @param response
     * @return
     */
    private IdentiCaProfile getProfileFromResponseMap(Map<?, ?> response) {
        IdentiCaProfile profile = new IdentiCaProfile();
        profile.setId(Long.valueOf(String.valueOf(response.get("id"))).longValue());
        profile.setScreenName(String.valueOf(response.get("screen_name")));
        profile.setName(String.valueOf(response.get("name")));
        profile.setDescription(String.valueOf(response.get("description")));
        profile.setLocation(String.valueOf(response.get("location")));
        profile.setUrl(String.valueOf(response.get("url")));
        profile.setProfileImageUrl(String.valueOf(response.get("profile_image_url")));
        profile.setCreatedDate(toDate(String.valueOf(response.get("created_at")), timelineDateFormat));
        return profile;
    }

    /**
     *
     * @return
     */
    @Override
    public SocialUserProfile getProfile() {
        IdentiCaProfile profile = this.getUserProfile();
        final SocialUserProfile socialUserProfile = new SocialUserProfile();
        socialUserProfile.setDescription(profile.getDescription());
        socialUserProfile.setCreatedAt(profile.getCreatedDate());
        socialUserProfile.setId(String.valueOf(profile.getId()));
        socialUserProfile.setLocation(profile.getLocation());
        socialUserProfile.setProfileImageUrl(profile.getProfileImageUrl());
        socialUserProfile.setProfileUrl(profile.getProfileUrl());
        socialUserProfile.setScreenName(profile.getScreenName());
        socialUserProfile.setUsername(profile.getScreenName());
        socialUserProfile.setUrl(profile.getUrl());
        return socialUserProfile;
    }
}

package com.volantis.xml.pipeline.sax.drivers.googledocs;

import org.xml.sax.Attributes;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.pipeline.localization.LocalizationFactory;
import java.util.HashMap;

/**
 * Simple container for storing Google Docs user authentication data
 */
class AuthData {

    static final String GDOCS_USER_DATA = "gdocs_user_data";

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER = LocalizationFactory.createLogger(AuthData.class);

    private static final ExceptionLocalizer EXCEPTION_LOCALIZER = LocalizationFactory.createExceptionLocalizer(AuthData.class);

    /**
     * Immutable, cannot be null
     */
    private String userId;

    private String password;

    private String authToken;

    private String captchaToken;

    private String captchaKey;

    private boolean needsRequest = false;

    AuthData(String userId) {
        if (userId == null) {
            throw new NullPointerException();
        }
        this.userId = userId;
    }

    AuthData(Attributes attributes) {
        this(attributes.getValue(AuthenticateRule.USER_ID_ATTRIBUTE));
        setPassword(attributes.getValue(AuthenticateRule.PASSWORD_ATTRIBUTE));
        setCaptchaTokenAndKey(attributes.getValue(AuthenticateRule.CAPTCHA_VALUE_ATTRIBUTE), attributes.getValue(AuthenticateRule.CAPTCHA_KEY_ATTRIBUTE));
    }

    void storeInContext(XMLPipelineContext context) {
        context.setProperty(GDOCS_USER_DATA, this, false);
    }

    void clear(XMLPipelineContext context) {
        context.setProperty(GDOCS_USER_DATA, null, false);
    }

    /**
     * Stores this object in cache.
     * If it contains no authentication token, do not store it.
     * @param context
     */
    void storeInCache(XMLPipelineContext context) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Storing authentication data: " + toString());
        }
        getCache(context).put(this.getUserId(), this);
    }

    /**
     * Retrieves authData object from contex, and then looks for object with given user-id in cache.
     * If found,returns it, if not, returns obejct from context.
     * 
     * @param context
     * @return null, if nothing set. context data, if data found in cachee, cahced data otherwise  
     */
    static AuthData retrieve(XMLPipelineContext context) {
        AuthData aData = (AuthData) context.getProperty(GDOCS_USER_DATA);
        if (aData != null) {
            aData.fillWithCachedData(context);
        }
        return aData;
    }

    /**
     * Gets authentication token and from cache.
     *
     * @param context
     */
    private void fillWithCachedData(XMLPipelineContext context) {
        AuthData cData = getCache(context).get(this.getUserId());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Authentication data retrieved from cache: " + cData);
        }
        if (cData == null) {
            setNeedsRequest(true);
            return;
        }
        if (cData.getAuthToken() != null) {
            setAuthToken(cData.getAuthToken());
        } else {
            setNeedsRequest(true);
        }
    }

    private static HashMap<String, AuthData> getCache(XMLPipelineContext context) {
        TokenConfiguration gtc = (TokenConfiguration) context.getPipelineConfiguration().retrieveConfiguration(TokenConfiguration.class);
        if (gtc == null) {
            gtc = new TokenConfiguration();
            context.getPipelineConfiguration().storeConfiguration(TokenConfiguration.class, gtc);
        }
        return gtc.getCache();
    }

    String getUserId() {
        return userId;
    }

    String getPassword() {
        return password;
    }

    void setPassword(String password) {
        this.password = password;
    }

    String getAuthToken() {
        return authToken;
    }

    void setAuthToken(String authToken) {
        this.authToken = authToken;
        setNeedsRequest(authToken == null);
    }

    void setCaptchaTokenAndKey(String captchaToken, String captchaKey) {
        if ((captchaToken == null && captchaKey != null) || (captchaToken != null && captchaKey == null)) {
            throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format("gdocs-arguments-both-filled", new String[] { "CaptchaToken", "CaptchaKey" }));
        }
        this.captchaToken = captchaToken;
        this.captchaKey = captchaKey;
    }

    String getCaptchaKey() {
        return captchaKey;
    }

    String getCaptchaToken() {
        return captchaToken;
    }

    boolean isCaptchaSet() {
        return captchaKey != null;
    }

    public boolean needsRequest() {
        return needsRequest;
    }

    public void setNeedsRequest(boolean needsRefresh) {
        this.needsRequest = needsRefresh;
    }

    public String toString() {
        return "UserId: " + getUserId() + ", Password: [masked]" + ", AuthToken: " + getAuthToken() + ", CaptchaKey: " + getCaptchaKey() + ", CaptchaToken: " + getCaptchaToken();
    }
}

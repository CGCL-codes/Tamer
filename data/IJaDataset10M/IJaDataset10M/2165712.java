package com.quikj.ace.web.client;

import java.util.HashMap;

/**
 * @author amit
 *
 */
public class SessionInfo extends HashMap<String, Object> {

    private static final long serialVersionUID = 5991427161297445884L;

    private static SessionInfo instance = null;

    public static String CHAT_LIST = "chatList";

    public static final String USER_INFO = "userInfo";

    public static final String AUDIO_SETTINGS = "audioSettings";

    public static final String CHAT_SETTINGS = "chatSettings";

    public static final String CANNED_MESSAGES = "cannedMessages";

    public static final String EMAIL_TRANSCRIPT_INFO = "emailTranscriptInfo";

    private SessionInfo() {
    }

    public static SessionInfo getInstance() {
        if (instance == null) {
            instance = new SessionInfo();
        }
        return instance;
    }
}

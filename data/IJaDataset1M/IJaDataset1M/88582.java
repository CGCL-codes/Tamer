package net.woodstock.rockapi.message;

import net.woodstock.rockapi.util.MessageBundle;

public abstract class CoreMessage {

    public static final String CORE_MESSAGES = "core-messages";

    private static MessageBundle messages = MessageBundle.getBundle(CoreMessage.CORE_MESSAGES);

    private CoreMessage() {
        super();
    }

    public static String getMessage(String key) {
        return CoreMessage.messages.getString(key);
    }

    public static String getMessage(String key, Object... arguments) {
        return CoreMessage.messages.getString(key, arguments);
    }
}

package org.t2framework.confeito.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * <#if locale="en">
 * <p>
 * Simple message formatter.
 * 
 * </p>
 * <#else>
 * <p>
 * 
 * </p>
 * </#if>
 * 
 * @author shot
 * 
 */
public class MessageFormatter {

    private static final String MESSAGES = "Messages";

    private MessageFormatter() {
    }

    public static String getMessage(String messageCode, Object[] args) {
        if (messageCode == null) {
            messageCode = "";
        }
        return "[" + messageCode + "]" + getSimpleMessage(messageCode, args);
    }

    /**
	 * <#if locale="en">
	 * <p>
	 * get message from message resource without using any caches.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param messageCode
	 * @param arguments
	 * @return
	 */
    public static String getSimpleMessageNoCache(String messageCode, Object[] arguments) {
        return getSimpleMessage0(messageCode, arguments, false);
    }

    /**
	 * <#if locale="en">
	 * <p>
	 * get message from message resource.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param messageCode
	 * @param arguments
	 * @return
	 */
    public static String getSimpleMessage(String messageCode, Object[] arguments) {
        return getSimpleMessage0(messageCode, arguments, true);
    }

    private static String getSimpleMessage0(String messageCode, Object[] arguments, boolean cacheable) {
        try {
            String pattern = getPattern(messageCode, cacheable);
            if (pattern != null) {
                return MessageFormat.format(pattern, arguments);
            }
        } catch (Throwable ignore) {
        }
        return getNoPatternMessage(arguments);
    }

    protected static String getPattern(final String messageCode, final boolean cacheable) {
        ResourceBundle resourceBundle = getMessages(getSystemName(messageCode), cacheable);
        if (resourceBundle != null) {
            return resourceBundle.getString(messageCode);
        }
        return null;
    }

    protected static String getSystemName(String messageCode) {
        if (messageCode.length() <= 4) {
            throw new IllegalStateException("messageCode length is more than 4. Format is [System code(D|I|E|T|W, 1char)][Message file name(any char length)][Message code name(4char)]");
        }
        return messageCode.substring(1, messageCode.length() - 4);
    }

    private static ResourceBundle getMessages(String systemName, boolean cacheable) {
        final String baseName = systemName + MESSAGES;
        if (cacheable) {
            return ResourceBundle.getBundle(baseName);
        } else {
            return ResourceBundleUtil.getBundleNoCache(baseName);
        }
    }

    private static String getNoPatternMessage(Object[] args) {
        if (args == null || args.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            builder.append(args[i] + ", ");
        }
        builder.setLength(builder.length() - 2);
        return builder.toString();
    }
}

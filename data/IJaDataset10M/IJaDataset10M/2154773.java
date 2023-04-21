package org.equanda.t5gui.services;

/**
 * Message Translator
 *
 * @author <a href="mailto:vladimir.tkachenko@gmail.com">Vladimir Tkachenko</a>
 */
public interface EquandaMessagesTranslator {

    /**
     * Gets translation for specified key
     *
     * @param language language
     * @param strKey message key
     * @param messagesGroups array of message groups
     * @return translated message
     */
    String getTranslation(String language, String strKey, String[] messagesGroups);

    /**
     * Gets translation for specified key
     *
     * @param language language
     * @param strKey message key
     * @param messagesGroups array of message groups
     * @return translated message
     */
    boolean hasTranslation(String language, String strKey, String[] messagesGroups);
}

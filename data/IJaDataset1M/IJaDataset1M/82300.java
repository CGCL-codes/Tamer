package org.webthree.dictionary.message;

import java.util.Locale;
import org.webthree.dictionary.DictionaryObject;

/**
 * @author michael.gerzabek@gmx.net
 * 
 */
public interface Message extends DictionaryObject {

    final String ROLE = Message.class.getName();

    /**
     * Get Messagetype.
     * @return
     */
    String getType();

    /**
     * Get message by Locale and Lengthtype.
     * @param locale
     * @param type
     * @return
     */
    String getMessage(Locale locale, String type);

    String getMessage(String type);

    String getMessage();
}

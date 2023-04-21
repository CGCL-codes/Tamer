package sk.naive.talker.message;

import java.util.*;

/**
 * MessageFactory.
 * <p>
 *
 * @author <a href="mailto:virgo@naive.deepblue.sk">Richard "Virgo" Richter</a>
 * @version $Revision: 1.3 $ $Date: 2004/09/05 18:45:24 $
 */
public interface MessageFactory {

    public Message createMessage(String key, Map ctx);

    public String getString(String key, Map ctx, Locale l);

    public String getString(String key, Map ctx);

    public String getString(String key, Map ctx, String lang);

    public Set getAllLanguages();

    public String getDefaultLanguage();

    public Locale getLocale(String lang);
}

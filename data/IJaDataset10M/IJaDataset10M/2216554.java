package org.gnomekr.potron.statistics;

import java.io.Serializable;
import org.apache.commons.lang.NullArgumentException;
import org.gnomekr.potron.data.User;

/**
 * TranslatorStatus.java
 * @author Xavier Cho
 * @version $Revision 1.1 $ $Date: 2005/07/22 07:25:49 $
 */
public class TranslatorStatus implements Serializable {

    private static final long serialVersionUID = 1615957317200315458L;

    private User translator;

    private long translatedEntries;

    /**
     * @param user
     * @param translator
     */
    public TranslatorStatus(User translator, long translatedEntries) {
        if (translator == null) {
            throw new NullArgumentException("translator");
        }
        this.translatedEntries = translatedEntries;
        this.translator = translator;
    }

    /**
     * @return translator
     */
    public User getTranslator() {
        return translator;
    }

    /**
     * @return Returns the translatedEntries.
     */
    public long getTranslatedEntries() {
        return translatedEntries;
    }
}

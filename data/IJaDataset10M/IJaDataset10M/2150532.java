package org.columba.mail.spam.rules;

import org.columba.mail.folder.IMailbox;

/**
 * Handcrafted rules which can be added to the spam filter engine.
 * <p>
 * See <a href="http://www.spamassassin.org/tests.html">spamassassin.org</a> for
 * many nice tests.
 * @author fdietz
 */
public interface Rule {

    /**
     * Get score of specific rule.
     * 
     * @param folder		selected folder
     * @param uid			selected message uid
     * @return				message score, meaning likelihood this message is spam
     * 						(0. - 1.0)
     * @throws Exception
     */
    float score(IMailbox folder, Object uid) throws Exception;

    String getName();
}

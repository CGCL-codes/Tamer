package de.malowa.suggestions.pass;

import de.malowa.greylister.MailHistory;
import de.malowa.greylister.SMTPInformation;
import de.malowa.rules.pass.PassRule;

/**
 * Lets pass mails if this is minimum the 6th attempt and the attempt is half an
 * hour after first attempt.
 */
public class InsaneMailserverRule implements PassRule {

    /**
	 * @see de.malowa.rules.pass.PassRule#shouldBeWhitelisted(de.malowa.greylister.SMTPInformation, de.malowa.greylister.MailHistory)
	 */
    public boolean shouldBeWhitelisted(SMTPInformation info, MailHistory history) {
        if (history.getBlockedmailcount() > 4 && history.getPassedSeconds() > 1800) return true;
        return false;
    }
}

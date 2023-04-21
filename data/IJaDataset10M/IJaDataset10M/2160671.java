package de.malowa.suggestions.delay;

import de.malowa.greylister.MailHistory;
import de.malowa.greylister.SMTPInformation;
import de.malowa.rules.delay.DelayRule;

/**
 * Adds ten minutes of delay time for all mails which do not have a dot (.) in
 * the HELO string.
 * 
 * @author Marcel Lohmann
 */
public class HeloWithoutDotRule implements DelayRule {

    /**
	 * @see de.malowa.rules.delay.DelayRule#getDelayTime(de.malowa.greylister.SMTPInformation)
	 */
    public int getDelayTime(SMTPInformation mail) {
        if (mail.getHelo().contains(".")) return 0;
        return 10;
    }

    /**
	 * @see de.malowa.rules.delay.DelayRule#getDelayTimeWithHistory(de.malowa.greylister.SMTPInformation,
	 *      de.malowa.greylister.MailHistory)
	 */
    public int getDelayTimeWithHistory(SMTPInformation info, MailHistory history) {
        return getDelayTime(info);
    }

    /**
	 * @see de.malowa.rules.delay.DelayRule#update()
	 */
    public void update() {
    }
}

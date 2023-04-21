package de.malowa.suggestions.black;

import de.malowa.greylister.SMTPInformation;
import de.malowa.rules.black.BlacklistRule;

/**
 * Blocks with a HELO pretending to come from top level domain .CC and having
 * two occurrences of the string 'mail' in the HELO. This is a really strong
 * indication of a spammer.
 * 
 * @author Marcel Lohmann
 */
public class BadCocoNutsRule implements BlacklistRule {

    /**
	 * @see de.malowa.rules.black.BlacklistRule#shouldBeBlacklisted(de.malowa.greylister.SMTPInformation)
	 */
    public boolean shouldBeBlacklisted(SMTPInformation info) {
        if (info.getHelo().matches("mail.*mail.*\\.cc") || info.getHelo().equals("mail.netwatcher.cc")) return true;
        return false;
    }
}

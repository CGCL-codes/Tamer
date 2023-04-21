package de.malowa.greylister.trap;

import de.malowa.greylister.SMTPInformation;

/**
 * Concrete implementation of a spam trap. This spam trap is not a real spam
 * trap and works as if no spam trap is defined.
 * 
 * @author Marcel Lohmann
 */
public final class NoSpamTrap extends SpamTrap {

    /**
	 * @see de.malowa.greylister.trap.SpamTrap#cleanUp()
	 */
    @Override
    public void cleanUp() {
    }

    /**
	 * @see de.malowa.greylister.trap.SpamTrap#sentSpamLately(de.malowa.greylister.SMTPInformation)
	 */
    @Override
    public boolean sentSpamLately(SMTPInformation recipient) {
        return false;
    }

    /**
	 * @see de.malowa.greylister.trap.SpamTrap#isSpamLover(java.lang.String)
	 */
    @Override
    public boolean isSpamLover(String recipient) {
        return false;
    }

    /**
	 * @see de.malowa.greylister.trap.SpamTrap#getAction(java.lang.String)
	 */
    @Override
    public String getAction(String recipient) {
        return "DUNNO";
    }
}

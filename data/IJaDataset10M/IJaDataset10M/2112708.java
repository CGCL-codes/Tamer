package com.hs.mail.imap.message.search;

import javax.mail.Flags;
import javax.mail.Flags.Flag;

/**
 * This class implements search-criteria for Message Flag.
 * 
 * @author Won Chul Doh
 * @since Jan 30, 2010
 * 
 */
public final class FlagKey extends SearchKey {

    /**
	 * Indicates whether to test for the presence or absence of the specified
	 * Flag. If <code>true</code>, then test whether the specified flag is
	 * present, else test whether the specified flag is absent.
	 */
    protected boolean set;

    /**
	 * Flag object containing the flag to test.
	 */
    protected Flags.Flag flag;

    public FlagKey(Flag flag, boolean set) {
        this.flag = flag;
        this.set = set;
    }

    public Flags.Flag getFlag() {
        return flag;
    }

    public boolean isSet() {
        return set;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof FlagKey)) return false;
        FlagKey fk = (FlagKey) obj;
        return fk.set == this.set && fk.flag.equals(this.flag);
    }

    public int hashCode() {
        return set ? flag.hashCode() : ~flag.hashCode();
    }

    @Override
    public boolean isComposite() {
        return false;
    }
}

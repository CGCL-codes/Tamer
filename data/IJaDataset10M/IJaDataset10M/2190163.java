package com.tagish.auth.win32.typed;

import com.tagish.auth.win32.*;

/**
 * A NTDomainPrincipal is a <code>Class</code> level Principal that represents a
 * NT Domain's SID.
 *
 * @author Jerry L Smith II, <A HREF="mailto:jsmith@sedonaesolutions.com">jsmith@sedonaesolutions.com</A>
 * @version 1.0.3
 */
public class NTDomainSIDPrincipal extends NTPrincipal {

    /** Creates a new instance of NTDomainPrincipal */
    public NTDomainSIDPrincipal(String name) {
        super(name, NTPrincipal.DOMAIN);
    }
}

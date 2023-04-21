package com.cube42.util.logging;

import com.cube42.util.system.SystemCode;

/**
 * System code that has been stored in the Syslog
 *
 * @author  Matt Paulin
 * @version $Id: StoredSystemCode.java,v 1.2 2003/01/15 06:52:12 zer0wing Exp $
 */
public class StoredSystemCode extends SystemCode {

    /**
     * Constructs the StoredSystemCode
     *
     * @param   type    The type of system code
     * @param   message The message that was stored
     */
    public StoredSystemCode(String type, String message) {
        super();
        this.setType(type);
        this.setMessage(message);
    }
}

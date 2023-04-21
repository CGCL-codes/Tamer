package com.objectwave.transactSecurity;

import java.util.*;

/**
 *  Used to indicate that an Object is being accessed.
 *
 * @author  dhoag
 * @version  $Id: ObjectAccessListener.java,v 2.0 2001/06/11 16:00:04 dave_hoag Exp $
 */
public interface ObjectAccessListener extends java.util.EventListener {

    /**
	 * @param  event
	 * @exception  SecurityException
	 */
    public void objectAccessed(ObjectAccessEvent event) throws SecurityException;
}

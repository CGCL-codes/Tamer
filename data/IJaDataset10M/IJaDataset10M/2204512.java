package org.roosster;

import java.net.URL;

/**
 *
 * @author <a href="mailto:benjamin@roosster.org">Benjamin Reitzammer</a>
 * @version $Id: DuplicateEntryException.java,v 1.1 2004/12/03 14:30:15 firstbman Exp $
 */
public class DuplicateEntryException extends RuntimeException {

    public DuplicateEntryException(URL url) {
        super("Entry with URL " + url + " is already stored");
    }
}

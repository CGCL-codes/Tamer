package org.jazzteam.jpatterns.patterns.mediator.storage;

import java.util.Random;
import org.apache.log4j.Logger;

/**
 * Class just for supporting different types of client keys.<br/>
 * 
 * $Author:: $<br/>
 * $Rev:: $<br/>
 * $Date:: $<br/>
 */
public class ClientKey {

    /**
	 * Logger instance.
	 */
    public static final Logger LOG = Logger.getLogger(ClientKey.class);

    /**
	 * inner presentation of key.
	 */
    long key;

    /**
	 * Default constructor of ClientKey. Generate random long number.
	 */
    public ClientKey() {
        final Random random = new Random();
        key = random.nextLong();
    }

    /**
	 * Constructor that allow set own inner presentation of key
	 * 
	 * @param someKey
	 */
    public ClientKey(final long someKey) {
        this.key = someKey;
    }

    /**
	 * Presentation of inner view of key
	 * 
	 * @return long [resentation of key.
	 */
    public long getLongView() {
        return this.key;
    }
}

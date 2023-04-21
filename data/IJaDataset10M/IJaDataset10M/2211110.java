package ch.arpage.collaboweb.services.impl;

import ch.arpage.collaboweb.services.KeyGenerator;

/**
 * Implementation of the Key Generator Service, which creates ID on the basis
 * of the current timestamp.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class KeyGeneratorImpl implements KeyGenerator {

    private int counter;

    /**
	 * Generates the a key with following format: CURRENT_TIMESTAMP + COUNTER,
	 * where counter is a 3 digit number, starting from 000 and ending with
	 * 999.
	 * @see ch.arpage.collaboweb.services.KeyGenerator#getNextId()
	 */
    public synchronized long getNextId() {
        return (System.currentTimeMillis() * 1000 + ((counter++) % 1000));
    }
}

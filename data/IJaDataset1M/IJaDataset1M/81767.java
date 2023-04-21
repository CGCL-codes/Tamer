package ch.arpage.collaboweb.services;

/**
 * Service used to generate new Id for the resources Table
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public interface KeyGenerator {

    /**
	 * Generates and returns the next free Id.
	 * @return the next free Id
	 */
    long getNextId();
}

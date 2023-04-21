package net.sf.evemsp.model;

/**
 * Stores Character information
 * 
 * @author Jaabaa
 */
public class CharacterStore extends AbstractRmsStore {

    private static final String CHAR_STORE = "EVEMSPCS";

    /**
	 * Constructor
	 */
    CharacterStore() {
    }

    protected AbstractRmsRecord createEmptyRecord() {
        return new CharRecord();
    }

    protected String getStoreName() {
        return CHAR_STORE;
    }
}

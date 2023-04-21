package org.gromurph.javascore.exception;

import org.gromurph.javascore.model.Entry;
import org.gromurph.javascore.model.Race;

/**
 * Covering class for scoring related exceptions
**/
public class ScoringException extends Exception {

    Entry fEntry;

    Race fRace;

    public ScoringException(String msg) {
        this(msg, null, null);
    }

    public ScoringException(String msg, Race race, Entry entry) {
        super(msg);
        fEntry = entry;
        fRace = race;
    }

    public Entry getEntry() {
        return fEntry;
    }

    public Race getRace() {
        return fRace;
    }

    public String toString() {
        return super.toString() + ", entry=" + fEntry + ", race" + fRace;
    }
}

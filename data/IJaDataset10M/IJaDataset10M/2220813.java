package org.jfonia.harmony2;

/**
 * 
 * @author Wijnand
 *
 */
public class ChordLabel {

    int rootPitch40;

    String chordQuality;

    Alteration alterations;

    int bassPitch40;
}

enum AlterationType {

    NO_INFO, ADD, LOWER, RAISE, OMIT, RAISE_AND_LOWER
}

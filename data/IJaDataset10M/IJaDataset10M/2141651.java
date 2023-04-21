package org.jfonia.harmony;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.jfonia.util.JFoniaException;

/**
 * Class used to parse Chords and/or ChordTypes from String,
 * such as "A7(#5)/C#" 
 * 
 * @author Wijnand
 *
 */
public class ChordParser {

    ResourceBundle chordTypeProperties;

    public ChordParser(String bundleFileName) throws MissingResourceException {
        chordTypeProperties = ResourceBundle.getBundle(bundleFileName);
    }

    /**
	 * 
	 * @param kind String indicating chord-type, e.g. "m7" or "sus"
	 * @return ChordType or null if "no chord"
	 * @throws JFoniaException if not recognized
	 */
    public ChordType determineChordType(String kind) throws JFoniaException {
        if (kind == null) throw new NullPointerException();
        if ("none".equals(kind) || "N.C.".equals(kind)) return null;
        if ("".equals(kind)) return ChordType.getMajorTriad();
        try {
            String typeCode = chordTypeProperties.getString(kind);
            if ("-".equals(typeCode)) typeCode = "";
            return ChordType.convertFromCode(typeCode);
        } catch (Exception e) {
            throw new JFoniaException("HARMONY_UNKNOWN: " + kind);
        }
    }

    /**
	 * 
	 * @param kind String indicating chord, e.g. "Am7/C" or "Bb(#5)"
	 * @return Chord or null if "no chord"
	 * @throws JFoniaException if not recognized
	 */
    public Chord determineChord(String kind) throws JFoniaException {
        return null;
    }
}

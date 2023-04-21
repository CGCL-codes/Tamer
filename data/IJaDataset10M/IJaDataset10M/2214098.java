package org.jsresources.apps.esemiso.data;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** Represents the properties of an instrument.
 *
 * <p>An instrument has different stroke types and different pitches.
 * There is a fixed number of possible pitch and stroke types (see
 * constants PitchType.* and StrokeType.*). However, an instrument is not
 * required to support all pitches and strokes. Rather, is has a
 * notation of the available types.</p>
 *
 * <p>Pitch and stroke types are orthogonal. The available ones form a
 * matrix.  Each element of this matrix has a key value, which is the
 * MIDI key number that needs to be played in order to render the
 * instrument with the given pitch and stroke.</p>
 *
 * <p>Currently, only the standard GM drum map is used. In the future,
 * support for different soundbanks may be added. In this case, it is
 * expected that there will also be a patch/bank number for each
 * element of the pitch/stroke matrix or for the instrument as a whole.</p>
 *
 * <p>Instrument instances are typically part of an InstrumentSet. The
 * InstrumentSet class also contains methods to handle persitence of
 * the instrument descriptions.</p>
 *
 * @author Matthias Pfisterer
 * @see InstrumentSet
 */
public class Instrument {

    public static enum PitchType {

        /** Pitch value for a unique tone.
		 *
		 * This value should be used if the instrument does not have
		 * different pitches. A typical example is the base drum, which
		 * has only one pitch.
		 */
        DEFAULT("default"), /** Pitch value for a high tone.
		 *
		 * This values should be used for high tones if the instrument is
		 * capable of producing tones in different pitches. An instrument
		 * that uses this pitch values typcially also uses PITCH_LOW (and
		 * maybe PITCH_MIDDLE). On the other hand, it typically does not
		 * use PITCH_DEFAULT.
		 */
        HIGH("high"), /** Pitch value for a middle tone.
		 *
		 * This values should be used for middle tones if the instrument
		 * is capable of producing tones in different pitches. An
		 * instrument that uses this pitch values typcially also uses
		 * PITCH_LOW and PITCH_HIGH. On the other hand, it typically does
		 * not use PITCH_DEFAULT.
		 */
        MIDDLE("middle"), /** Pitch value for a low tone.
		 *
		 * This values should be used for low tones if the instrument is
		 * capable of producing tones in different pitches. An instrument
		 * that uses this pitch values typcially also uses PITCH_HIGH (and
		 * maybe PITCH_MIDDLE). On the other hand, it typically does not
		 * use PITCH_DEFAULT.
		 */
        LOW("low");

        private String m_name;

        private PitchType(String name) {
            m_name = name;
        }

        /** Returns a string representation of a pitch type.
		 *
		 * This method converts a pitch type value to its string
		 * representation. For instance, for 1 (PITCH_HIGH), "high" is
		 * returned. The returned string is always in lowercase. If the
		 * pitch value if illegal (lower than zero or greater or equal to
		 * PITCH_MAX), <code>null</code> is returned.
		 *
		 * @param nPitch the pitch type value to convert. To get a valid
		 * result, the value has to be between 0 (included) and PITCH_MAX
		 * (excluded).
		 *
		 * @return the string representation of the pitch, or
		 * <code>null</code> if an invalid pitch value has been passed.
		 */
        public String getName() {
            return m_name;
        }

        public static PitchType getPitchType(String strPitch) {
            if (strPitch == null) {
                return null;
            }
            strPitch = strPitch.trim().toLowerCase();
            PitchType type = null;
            for (PitchType value : values()) {
                if (value.getName().equals(strPitch)) type = value;
            }
            return type;
        }
    }

    public static class StrokeType {

        /**
		 * Name of the stroke type.
		 * 
		 * @see #getName()
		 */
        private String m_name;

        /**
		 * The notehead type associated with this stroke type.
		 * 
		 * @see #getNoteheadType()
		 */
        private NoteheadType m_noteheadType;

        /**
		 * Constructor.
		 *
		 * @param name the name of the stroke type
		 * @param noteheadType the notehead type associated with this stroke
		 * type
		 */
        public StrokeType(String name, NoteheadType noteheadType) {
            m_name = name;
            m_noteheadType = noteheadType;
        }

        /**
		 * Obtains the name of this stroke type
		 * @return the name
		 */
        public String getName() {
            return m_name;
        }

        /**
		 * Obtains the notehead type associated with this stroke type.
		 * 
		 * @return the notehead type
		 */
        public NoteheadType getNoteheadType() {
            return m_noteheadType;
        }
    }

    /**
	 * Key class for MIDI keys.
	 *
	 * Instances of this class are used internally as keys to the
	 * hashtable of MIDI keys.
	 *
	 * @see #m_keys
	 */
    private static class KeyIndex {

        private PitchType m_pitchType;

        private StrokeType m_strokeType;

        public KeyIndex(PitchType pitchType, StrokeType strokeType) {
            m_pitchType = pitchType;
            m_strokeType = strokeType;
        }

        private PitchType getPitchType() {
            return m_pitchType;
        }

        private StrokeType getStrokeType() {
            return m_strokeType;
        }

        public boolean equals(Object obj) {
            if (obj instanceof KeyIndex) {
                KeyIndex key = (KeyIndex) obj;
                return getPitchType() == key.getPitchType() && getStrokeType() == key.getStrokeType();
            } else {
                return false;
            }
        }

        public int hashCode() {
            int nResult = 17;
            nResult = 37 * nResult + getPitchType().hashCode();
            nResult = 37 * nResult + getStrokeType().hashCode();
            return nResult;
        }
    }

    /** "Unknown" key value.
	 *
	 * This values is used to initialize all elements of the keys
	 * array in the first place. It is also the values that will be
	 * returned by {@link #getKey(int, int) getKey} if the instrument
	 * has no key for a specific combination of pitch and stroke.
	 */
    public static final int KEY_NONE = -1;

    /** The name of the instrument.  This holds the name that is set
	 *	by the constructor.  The name can be retrieved with {@link #getName()}.
	 *
	 * @see #Instrument(String)
	 * @see #getName()
	 */
    private String m_strName;

    /** The pitch types available for this instrument.
	 *
	 * This is an array of dimension PITCH_MAX. For each type of
	 * pitch, the array records if the pitch is available for this
	 * instrument.
	 *
	 * @see #setPitches
	 * @see #getPitches
	 * @see #hasPitch
	 */
    private EnumSet<PitchType> m_pitches;

    /** The stroke types available for this instrument.
	 *
	 * This is an array of dimension STROKE_MAX. For each type of
	 * stroke, the array records if the stroke is available for this
	 * instrument.
	 *
	 * @see #setStrokes
	 * @see #getStrokes
	 * @see #hasStroke
	 */
    private Set<StrokeType> m_strokes;

    /** Matrix of MIDI key values.
	 *
	 * This is a two-dimensional array of dimensions PITCH_MAX and
	 * STROKE_MAX.
	 *
	 * @see #getKey
	 * @see #setKey
	 */
    private Map<KeyIndex, Integer> m_keys;

    /**
	 * Matrix of icon type names.
	 * 
	 * @see #getIconTypeName(PitchType, StrokeType)
	 * @see #setIconTypeName(PitchType, StrokeType, String)
	 */
    private Map<KeyIndex, String> m_aIconTypeNames;

    /** Constructor.
		@param strName the name of the instrument
	 */
    public Instrument(String strName) {
        m_strName = strName;
        m_pitches = EnumSet.noneOf(PitchType.class);
        m_strokes = new HashSet<StrokeType>();
        m_keys = new HashMap<KeyIndex, Integer>();
        m_aIconTypeNames = new HashMap<KeyIndex, String>();
    }

    public void setPitchTypes(EnumSet<PitchType> pitches) {
        m_pitches = pitches.clone();
    }

    public EnumSet<PitchType> getPitchTypes() {
        return m_pitches.clone();
    }

    public boolean hasPitchType(PitchType pitch) {
        return m_pitches.contains(pitch);
    }

    public void setStrokeTypes(Set<StrokeType> strokes) {
        m_strokes = new HashSet<StrokeType>(strokes);
    }

    public Set<StrokeType> getStrokeTypes() {
        return Collections.unmodifiableSet(m_strokes);
    }

    public boolean hasStrokeType(StrokeType stroke) {
        return m_strokes.contains(stroke);
    }

    public StrokeType getStrokeType(String strStrokeTypeName) {
        if (strStrokeTypeName == null) {
            return null;
        }
        strStrokeTypeName = strStrokeTypeName.trim().toLowerCase();
        StrokeType type = null;
        for (StrokeType value : m_strokes) {
            if (value.getName().equals(strStrokeTypeName)) type = value;
        }
        return type;
    }

    /** Obtain the name of the instrument.
	 *
	 * This returns the name as set in the constructor.
	 *
	 * @see #m_strName
	 * @see #Instrument(String)
	 */
    public String getName() {
        return m_strName;
    }

    /** Sets the MIDI key for a specific pitch and stroke type.
	 *
	 * @param pitch the pitch type for which to set the key
	 * @param stroke the stroke type for which to set the key
	 * @param nKey the key value to set
	 *
	 * @see #getKey(PitchType, StrokeType)
	 */
    public void setKey(PitchType pitch, StrokeType stroke, int nKey) {
        m_keys.put(new KeyIndex(pitch, stroke), new Integer(nKey));
    }

    /** Obtains the MIDI key for a specific pitch and stroke type.
	 *
	 * @param pitch the pitch type for which to obtain the key
	 * @param stroke the stroke type for which to obtain the key
	 * @return the key value for the given pitch and stroke type
	 *
	 * @see #setKey(PitchType, StrokeType, int)
	 */
    public int getKey(PitchType pitch, StrokeType stroke) {
        Integer key = m_keys.get(new KeyIndex(pitch, stroke));
        return (key != null) ? key.intValue() : KEY_NONE;
    }

    /** Sets the MIDI key for a specific pitch and stroke type.
	 *
	 * @param pitch the pitch type for which to set the key
	 * @param stroke the stroke type for which to set the key
	 * @param nKey the key value to set
	 *
	 * @see #getKey(PitchType, StrokeType)
	 */
    public void setIconTypeName(PitchType pitch, StrokeType stroke, String strIconTypeName) {
        m_aIconTypeNames.put(new KeyIndex(pitch, stroke), strIconTypeName);
    }

    /** Obtains the icon type name for a specific pitch and stroke type.
	 *
	 * @param pitch the pitch type for which to obtain the icon type name
	 * @param stroke the stroke type for which to obtain the icon type name
	 * @return the icon type name
	 *
	 * @see #getIconTypeName(PitchType, StrokeType)
	 */
    public String getIconTypeName(PitchType pitch, StrokeType stroke) {
        if (pitch != null && stroke != null) {
            return m_aIconTypeNames.get(new KeyIndex(pitch, stroke));
        } else {
            return null;
        }
    }
}

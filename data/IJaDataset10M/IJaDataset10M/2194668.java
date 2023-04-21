package org.sunspotworld.demo;

/**
 * A class representing a note that could be played, including rests. Another
 * special "note" type is a measure marker that gets treated as having no length
 * but which has special significance in the code.
 */
public class Note {

    private int m_pitchIndex;

    private int m_length;

    private int m_onTime;

    public static final int PitchType_Rest = -1;

    public static final int PitchType_Marker = -2;

    public Note() {
        m_pitchIndex = 1;
        m_length = 0;
        m_onTime = 100;
    }

    public Note(int pitch, int length, int onTime) {
        this();
        setPitchIndex(pitch);
        setLength(length);
        setOnTime(onTime);
    }

    public int getPitchIndex() {
        return m_pitchIndex;
    }

    public void setPitchIndex(int pitch) {
        m_pitchIndex = pitch;
    }

    public int getLength() {
        return m_length;
    }

    public void setLength(int length) {
        m_length = length;
    }

    public int getOnTime() {
        return m_onTime;
    }

    public void setOnTime(int onTime) {
        m_onTime = onTime;
    }

    public boolean isRest() {
        return m_pitchIndex == PitchType_Rest;
    }

    public void setToRest() {
        m_pitchIndex = PitchType_Rest;
    }

    public boolean isMarker() {
        return m_pitchIndex == PitchType_Marker;
    }

    public void setToMarker() {
        m_pitchIndex = PitchType_Marker;
    }
}

package pitchDetector;

import misc.InstrumentInfo;
import misc.InstrumentInfo.*;

public class PitchAnalyzer {

    private static final int PITCH_RANGE = 200;

    private static double[] m_pitchFreqMap;

    private static String[] m_pitchNameMap;

    private PitchSample m_currentTuneNote = null;

    static {
        m_pitchFreqMap = new double[PITCH_RANGE];
        m_pitchNameMap = new String[PITCH_RANGE];
        String basePitchNameMap[] = new String[12];
        basePitchNameMap[0] = "A";
        basePitchNameMap[1] = "Bb";
        basePitchNameMap[2] = "B";
        basePitchNameMap[3] = "C";
        basePitchNameMap[4] = "C#";
        basePitchNameMap[5] = "D";
        basePitchNameMap[6] = "Eb";
        basePitchNameMap[7] = "E";
        basePitchNameMap[8] = "F";
        basePitchNameMap[9] = "F#";
        basePitchNameMap[10] = "G";
        basePitchNameMap[11] = "Ab";
        for (int i = 0; i < PITCH_RANGE; i++) {
            m_pitchFreqMap[i] = 440.0f * Math.pow(2.0f, (((double) i) - 69.0f) / 12.0f);
            m_pitchNameMap[i] = basePitchNameMap[(i + 3) % 12];
            System.out.println("Name: " + m_pitchNameMap[i] + " Num: " + i + " Freq: " + m_pitchFreqMap[i]);
        }
    }

    public PitchAnalyzer() {
    }

    public PitchSample analyze(double sampleValue) {
        double semitone_difference, pitchErrorNormalized, pitch, pitchErrorHz;
        int nameIndex;
        pitch = 69.0f + 12.0f * (Math.log10(sampleValue / 440.0f) / Math.log10(2.0f));
        nameIndex = (int) Math.round(pitch);
        if (nameIndex < 0 || nameIndex >= PITCH_RANGE) {
            nameIndex = 0;
        }
        if (m_currentTuneNote == null) {
            pitchErrorHz = sampleValue - m_pitchFreqMap[nameIndex];
        } else {
            pitchErrorHz = sampleValue - m_pitchFreqMap[m_currentTuneNote.getPitchNum()];
        }
        if (nameIndex >= 1) {
            semitone_difference = m_pitchFreqMap[nameIndex] - m_pitchFreqMap[nameIndex - 1];
        } else {
            semitone_difference = m_pitchFreqMap[nameIndex + 1] - m_pitchFreqMap[nameIndex];
        }
        pitchErrorNormalized = 100.0 * pitchErrorHz / semitone_difference;
        return new PitchSample(m_pitchNameMap[nameIndex], nameIndex, sampleValue, pitchErrorHz, pitchErrorNormalized);
    }

    public double compareHz(PitchSample a, PitchSample b) {
        return 0;
    }

    public double compareNormalized(PitchSample a, PitchSample b) {
        return 0;
    }

    public void setCurrentTuneNote(PitchSample current) {
        m_currentTuneNote = current;
    }

    public void resetCurrentTuneNote() {
        m_currentTuneNote = null;
    }

    public boolean currentTuneNoteIsSet() {
        if (m_currentTuneNote == null) {
            return false;
        }
        return true;
    }

    public double getPitchFreq(int pitchNum) {
        return m_pitchFreqMap[pitchNum];
    }

    public static double[] getPitchFreqMap() {
        return m_pitchFreqMap;
    }

    public static String[] getPitchNameMap() {
        return m_pitchNameMap;
    }
}

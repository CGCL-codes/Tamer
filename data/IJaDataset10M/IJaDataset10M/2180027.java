package arb.soundciphermod;

import java.util.Random;

/**
 * The <code>SCUtilties<code> class contains a collection of helper methods
 * for users of the SoundCipher package. In particular the utilities provide
 * support for common musical and mathematical functions as a convenience
 * for the algorithmic musician.<br>
 * <br>
 * The SCUtilities class is not designed to be instantiated directly, but 
 * can be accessed via the SoundCipher or SCScore classes which inherit the methods.
 * For example, given the declaration <br>
 * <pre>
 * SoundCipher sc = new SoundCipher(this);
 * </pre>
 * then utilities can be accessed using dot syntax such as,
 * <pre>
 * float frequency = sc.midiToFreq(60);
 * </pre>
 *
 * @author <a href="http://www.explodingart.com/arb/">Andrew R. Brown</a>
 */
public class SCUtilities {

    private static final Random RNG = new Random();

    /**
     * Convert a MIDI pitch into a frequency in hertz.
     * Assumes pitch 69 (A4) = 440.0 hz and equal tempered tuning.
     *
     * @param midiPitch The MIDI pitch number to convert (e.g., 60 = middle C)
     * @return The equivalent frequency in hertz (cycles per second).
     */
    public float midiToFreq(float midiPitch) {
        return this.midiToFreq((double) midiPitch);
    }

    /**
     * Convert a MIDI pitch into a frequency in hertz.
     * Assumes pitch 69 (A4) = 440.0 hz and equal tempered tuning.
     *
     * @param midiPitch The MIDI pitch number to convert (e.g., 60 = middle C)
     * @return The equivalent frequency in hertz (cycles per second).
     */
    public float midiToFreq(double midiPitch) {
        return (float) (6.875 * Math.pow(2.0, ((3 + midiPitch) / 12.0)));
    }

    /** 
     * Returns a psudorandom number dispersed by a gaussian distribution 
     * around the mean specified.  
     * About 68% of the pitches returned will be no greater than the mean 
     * plus the standard deviation and no less than the mean minus 
     * the standard deviation. Around 95% of pitches will be within twice this
     * range.
     * 
     * @param mean    the centre value that should be most commonly returned 
     *                     by this method.
     * @param stdDeviation the breadth of variation eaither side of the mean.
     */
    public float gaussian(final double mean, final double stdDeviation) {
        return (float) (RNG.nextGaussian() * stdDeviation + mean);
    }

    /**
     * Select a constrained pitch from a pitch class set with the specified range.
     * A pitch class set is a float array of scale degree values in the range
     * from 0 to 11. There are some pitch class sets defined in the PitchClassSets
     * interface.
     *
     * @param lower The lowest MIDI pitch that can be selected
     * @param upper The highest MIDI pitch that can be selected
     * @param pitchClassSet The scale or mode that the selected pitch must belong too
     *
     * @return The randomly chosen pitch that meets all constriants.
     */
    public float pcRandom(int lower, int upper, float[] pitchClassSet) {
        return this.pcRandom((float) lower, (float) upper, pitchClassSet);
    }

    /**
     * Select a constrained pitch from a pitch class set with the specified range.
     * A pitch class set is a float array of scale degree values in the range
     * from 0 to 11. There are some pitch class sets defined in the PitchClassSets
     * interface.
     *
     * @param lower The lowest MIDI pitch that can be selected
     * @param upper The highest MIDI pitch that can be selected
     * @param pitchClassSet The scale or mode that the selected pitch must belong too
     *
     * @return The randomly chosen pitch that meets all constriants.
     */
    public float pcRandom(float lower, float upper, float[] pitchClassSet) {
        float p;
        int cnt = 0;
        do {
            p = (float) Math.random() * (upper - lower) + lower;
            cnt++;
        } while ((!isPCMember(Math.round(p), pitchClassSet) || p < 0 || p > 127) && cnt < 1000);
        return Math.round(p);
    }

    /**
     * Select a constrained MIDI pitch from a pitch class that is poximate to
     * the starting MIDI pitch value.
     * A pitch class set is a float array of scale degree values in the range
     * from 0 to 11. There are some pitch class sets for common scales and modes
     * are defined in the PitchClassSets interface.
     *
     * @param startVal The initial MIDI pitch
     * @param stepMax The largest allowable interval away from the initial pitch
     * @param pitchClassSet The scale or mode that the selected pitch must belong too
     *
     * @return The randomly chosen pitch that meets all constriants.
     */
    public float pcRandomWalk(int startVal, int stepMax, float[] pitchClassSet) {
        return this.pcRandomWalk((float) startVal, (float) stepMax, pitchClassSet);
    }

    /**
     * Select a constrained MIDI pitch from a pitch class that is poximate to
     * the starting MIDI pitch value.
     * A pitch class set is a float array of scale degree values in the range
     * from 0 to 11. There are some pitch class sets for common scales and modes
     * are defined in the PitchClassSets interface.
     *
     * @param startVal The initial MIDI pitch
     * @param stepMax The largest allowable interval away from the initial pitch
     * @param pitchClassSet The scale or mode that the selected pitch must belong too
     *
     * @return The randomly chosen pitch that meets all constriants.
     */
    public float pcRandomWalk(float startVal, float stepMax, float[] pitchClassSet) {
        float p;
        int cnt = 0;
        do {
            p = startVal + (float) Math.random() * (stepMax * 2) - stepMax;
            cnt++;
        } while ((!isPCMember(Math.round(p), pitchClassSet) || p < 0 || p > 127) && cnt < 1000);
        return Math.round(p);
    }

    /**
     * Select a constrained pitch from a pitch class with the specified range
     * around a specificed value.
     * A pitch class set is a float array of scale degree values in the range
     * from 0 to 11. There are some pitch class sets for common scales and modes
     * are defined in the PitchClassSets interface.
     *
     * @param startVal The median point around which values will centre
     * @param stdDeviation The standard deviation from the centre/median point
     * @param pitchClassSet The scale or mode that the selected pitch must belong too
     *
     * @return The randomly chosen pitch that meets all constriants.
     */
    public float pcGaussianWalk(float startVal, float stdDeviation, float[] pitchClassSet) {
        float p;
        int cnt = 0;
        do {
            p = gaussian(startVal, stdDeviation);
            cnt++;
        } while ((!isPCMember(Math.round(p), pitchClassSet) || p < 0 || p > 127) && cnt < 1000);
        return Math.round(p);
    }

    /**
     * A helper class for the pitch class methods that determins if a pitch belongs
     * to a specified pitch class set or not.
     * A pitch class set is a float array of scale degree values in the range
     * from 0 to 11. There are some pitch class sets for common scales and modes
     * are defined in the PitchClassSets interface.
     *
     * @param val The MIDI pich value to be tested
     * @param pitchClassSet The scale/mode that the value is tested against
     *
     * @return Whether or not the pitch belongs to the pich class set.
     */
    private boolean isPCMember(float val, float[] pitchClassSet) {
        boolean b = false;
        for (int i = 0; i < pitchClassSet.length; i++) {
            if (Math.round(val) % 12 == pitchClassSet[i]) b = true;
        }
        return b;
    }
}

package alesis.fusion.chunks;

/**
 *
 * @author jam
 * @todo define a different ModDestTypeEnum for each program type
 */
public abstract class ModDestType {

    /**
     *
     */
    public static enum AlogProgram {

        /**
         * 
         */
        NONE(0), /**
         *
         */
        PITCH(1), /**
         * 
         */
        VOLUME(2), /**
         *
         */
        PAN(3), /**
         * 
         */
        PORTAMENTO_TIME(0x12), /**
         *
         */
        AMOUNT(4), /**
         * 
         */
        CURVE(5), /**
         *
         */
        SH_RATE(6), /**
         * 
         */
        SMOOTHING(7), /**
         *
         */
        ENV_DELAY(8), /**
         * 
         */
        ATTACK(9), /**
         *
         */
        DECAY(0x0a), /**
         * 
         */
        SUSTAIN_LEVEL(0x0b), /**
         *
         */
        SUSTAIN_DECAY(0x13), /**
         * 
         */
        RELEASE(0x0c), /**
         *
         */
        ENV_TIME(0x11), /**
         * 
         */
        LFO_DELAY(0x0d), /**
         *
         */
        LFO_RAMP(0x0e), /**
         * 
         */
        LFO_RATE(0x0f), /**
         *
         */
        LFO_SHAPE(0x10), /**
         * 
         */
        OSC_FREQUENCY(0), /**
         *
         */
        OSC_FM_AMOUNT(0), /**
         * 
         */
        OSC_RANDOM_TUNE(0), /**
         *
         */
        OSC_SHAPE(0), /**
         * 
         */
        OSC_VOLUME(0), /**
         *
         */
        OSC_PAN(0), /**
         *
         */
        BREATH_PRESSURE(0x18), /**
         * 
         */
        BREATH_NOISE(0x19), /**
         *
         */
        REED_THRESHOLD_OR_MOUTH_JET(0x1a), /**
         *
         */
        REED_SLOPE_OR_MOUTH_CURVE(0x1b), /**
         * 
         */
        REED_CURVE_OR_MOUTH_OFFSET(0x1c), /**
         *
         */
        BORE_FREQUENCY(0x1d), /**
         *
         */
        BORE_MIX(0x1e), /**
         * 
         */
        BORE_GAIN(0x1f), /**
         *
         */
        BORE_FILTER(0x20), /**
         * 
         */
        FILTER_CUTOFF(0x21), /**
         *
         */
        FILTER_RESONANCE(0x22);

        AlogProgram(int dummy) {
        }
    }

    /**
     *
     */
    public static enum SampProgram {
    }

    /**
     *
     */
    public static enum DrumProgram {
    }

    /**
     * 
     */
    public static enum FmProgram {
    }

    /**
     *
     */
    public static enum ReedProgram {
    }

    /**
     *
     */
    public static enum FlutProgram {
    }
}

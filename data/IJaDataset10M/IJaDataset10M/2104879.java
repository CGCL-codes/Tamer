package joshua.decoder.ff.lm;

/**
 * Represents a single n-gram line 
 * from an ARPA language model file.
 * 
 * @author Lane Schwartz
 */
public class ArpaNgram {

    /** Indicates an invalid probability value. */
    public static final float INVALID_VALUE = Float.NaN;

    /** Default backoff value. */
    public static final float DEFAULT_BACKOFF = 0.0f;

    private final int word;

    private final int[] context;

    private final float value;

    private final float backoff;

    public ArpaNgram(int word, int[] context, float value, float backoff) {
        this.word = word;
        this.context = context;
        this.value = value;
        this.backoff = backoff;
    }

    public int order() {
        return context.length + 1;
    }

    public int getWord() {
        return word;
    }

    public int[] getContext() {
        return context;
    }

    public float getValue() {
        return value;
    }

    public float getBackoff() {
        return backoff;
    }
}

package oss.jthinker.util;

/**
 * Inverts application of mixer's arguments.
 * 
 * @author iappel
 * @param T type of state to operate
 */
public class MixerInvertor<T> implements Mixer<T> {

    private final Mixer<T> mixer;

    /**
     * Creates a new inverted mixer instance out of given mixer.
     * 
     * @param mixer mixer to invert
     */
    public MixerInvertor(Mixer<T> mixer) {
        this.mixer = mixer;
    }

    /** {@inheritDoc} */
    public T mix(T arg1, T arg2) {
        return mixer.mix(arg2, arg1);
    }
}

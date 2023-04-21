package javax.sound.midi;

/**
 * The abstract base class for all MIDI instruments.
 * 
 * @author Anthony Green (green@redhat.com)
 * @since 1.3
 *
 */
public abstract class Instrument extends SoundbankResource {

    private Patch patch;

    /**
   * Create a new Instrument.
   * 
   * @param soundbank the Soundbank containing the instrument.
   * @param patch the patch for this instrument
   * @param name the name of this instrument
   * @param dataClass the class used to represent sample data for this instrument
   */
    protected Instrument(Soundbank soundbank, Patch patch, String name, Class dataClass) {
        super(soundbank, name, dataClass);
        this.patch = patch;
    }

    /**
   * Get the patch for this instrument.
   * 
   * @return the patch for this instrument
   */
    public Patch getPatch() {
        return patch;
    }
}

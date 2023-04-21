package q.zik.lang;

/**
 * A Note.
 * 
 * @author ruyantq
 */
public class Note extends TimeUnit {

    public static final Note SILENCE = new Note();

    /**
   * 
   */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a note from the frequency.
     * 
     * @param _frequency
     */
    public Note(final double _note) {
        this(_note, 0);
    }

    /**
     * Constructs a note.
     * 
     * @param _note
     * @param _octave
     */
    public Note(final double _note, final int _octave) {
        super(_note, _octave, AudioSettings.INSTANCE.getSampleRate());
    }

    /**
     * Default constructor for silence note.
     */
    private Note() {
        this(-1);
    }

    @Override
    public String toString() {
        return noteToString();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Note) {
            final Note otherNote = (Note) obj;
            return otherNote.getNoteValue() == getNoteValue();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return intValue();
    }

    @Override
    public double doubleValue() {
        return getNoteValue();
    }

    @Override
    public float floatValue() {
        return (float) getNoteValue();
    }

    @Override
    public int intValue() {
        return (int) getNoteValue();
    }

    @Override
    public long longValue() {
        return (long) getNoteValue();
    }
}

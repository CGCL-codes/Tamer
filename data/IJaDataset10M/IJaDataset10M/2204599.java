package name.angoca.db2sa.ui.api;

/**
 * Interface that defines the structure of a reader.
 * <p>
 * <b>Control Version</b>
 * <p>
 * <ul>
 * <li>0.0.1 Class creation.</li>
 * <li>1.0.0 Moved to version 1.</li>
 * <li>1.1.0 Exception hierarchy changed.</li>
 * </ul>
 * 
 * @author Andres Gomez Casanova <a
 *         href="mailto:a n g o c a at y a h o o dot c o m">(AngocA)</a>
 * @version 1.1.0 2009-08-16
 */
public interface InputReader {

    /**
     * Reads the phrases written by the user.
     * 
     * @return Phrase written by the user.
     * @throws AbstractUIException
     *             If some problem occurs with the user interaction.
     */
    String readString() throws AbstractUIException;
}

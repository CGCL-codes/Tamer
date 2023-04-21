package name.angoca.zemucan.grammarReader.api;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import name.angoca.zemucan.AbstractZemucanException;

/**
 * Represents the grammar read done in two phases. First one is to detect nodes,
 * and the second one to establish relations between them.
 * <p>
 * <b>Control Version</b>
 * <p>
 * <ul>
 * <li>1.0.0 Class creation</li>
 * </ul>
 *
 * @author Andres Gomez Casanova <a
 *         href="mailto:a n g o c a at y a h o o dot c o m" >(AngocA)</a>
 * @version 1.0.0 2010-10-26
 */
public abstract class AbstractTwoPhaseGrammarReader extends AbstractGrammarReader {

    /**
     * File descriptor.
     */
    protected String fileDescriptor;

    /**
     * Constructor.
     */
    public AbstractTwoPhaseGrammarReader() {
        super();
    }

    /**
     * Scans the document where the nodes are described, and creates the objects
     * that represents the nodes according to its type.
     * <p>
     * This phase only creates independent nodes. The second phase creates the
     * relations between them.
     *
     * @throws AbstractZemucanException
     */
    protected abstract void firstPhase() throws AbstractZemucanException;

    /**
     * Retrieves the set of file names described in the descriptor.
     *
     * @return Set of file names.
     * @throws GeneralGrammarReaderProblemException
     *             If there is a problem interpreting the URL that represents
     *             the file names.
     */
    protected String[] getFileNamesFromDescriptor() throws GeneralGrammarReaderProblemException {
        assert this.fileDescriptor != null;
        String[] files = new String[0];
        final ClassLoader loader = ClassLoader.getSystemClassLoader();
        if (loader != null) {
            final URL url = loader.getResource(this.fileDescriptor);
            if ((url != null) && url.getProtocol().equals("file")) {
                File file = null;
                try {
                    file = new File(url.toURI());
                } catch (final URISyntaxException exception) {
                    throw new GeneralGrammarReaderProblemException(exception);
                }
                if (file.isDirectory()) {
                    files = file.list();
                }
            }
        } else {
            assert false;
        }
        return files;
    }

    /**
     * Second phase of the process where the relations between nodes are
     * established.
     *
     * @throws AbstractZemucanException
     *             If there is a problem while regarding the nodes or
     */
    protected abstract void secondPhase() throws AbstractZemucanException;
}

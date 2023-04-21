package bioweka.core.converters.sequence;

import org.biojava.bio.seq.io.SequenceFormat;
import bioweka.core.mappers.SwissProtSequenceMapper;

/**
 * Saver for Swiss-Prot files.
 * @author <a href="mailto:Martin.Szugat@GMX.net">Martin Szugat</a>
 * @version $Revision: 1.3 $
 */
public class SwissProtSequenceSaver extends AbstractSequenceSaver {

    /**
     * Unique class identifier.
     */
    private static final long serialVersionUID = 3258410616841318448L;

    /**
     * The SWISSPROT format.
     */
    public static final SequenceFormat SWISSPROT_SEQUENCE_FORMAT = SwissProtSequenceLoader.SWISSPROT_SEQUENCE_FORMAT;

    /**
     * The file extension for Swiss-Prot files.
     */
    public static final String SWISSPROT_FILE_EXTENSION = SwissProtSequenceLoader.SWISSPROT_FILE_EXTENSION;

    /**
     * The description of the Swiss-Prot sequence saver.
     */
    public static final String SWISSPROT_SEQUENCE_SAVER_GLOBAL_INFO = "Saves a file in the Swiss-Prot format.";

    /**
     * The description of the Swiss-Prot file format.
     */
    public static final String SWISSPROT_FILE_DESCRIPTION = SwissProtSequenceLoader.SWISSPROT_FILE_DESCRIPTION;

    /**
     * The main entry point for the Swiss-Prot sequence saver.
     * @param args the command line arguments for the Swiss-Prot sequence saver.
     * @throws Exception if the Swiss-Prot sequence saver could not be 
     * instantiated.
     */
    public static void main(String[] args) throws Exception {
        System.exit(doMain(new SwissProtSequenceSaver(), args));
    }

    /**
     * Intializes the SWISSPROT sequence saver.
     * @throws Exception if the SWISSPROT sequence saver could not be instantiated.
     */
    public SwissProtSequenceSaver() throws Exception {
        super(SwissProtSequenceLoader.SWISSPROT_DEFAULT_ALPHABET, new SwissProtSequenceMapper());
    }

    /**
     * {@inheritDoc}
     */
    public SequenceFormat sequenceFormat() {
        return SWISSPROT_SEQUENCE_FORMAT;
    }

    /**
     * {@inheritDoc}
     */
    public String globalInfo() {
        return SWISSPROT_SEQUENCE_SAVER_GLOBAL_INFO;
    }

    /**
     * {@inheritDoc}
     */
    protected String fileExtension() {
        return SWISSPROT_FILE_EXTENSION;
    }

    /**
     * {@inheritDoc}
     */
    protected String fileDescription() {
        return SWISSPROT_FILE_DESCRIPTION;
    }
}

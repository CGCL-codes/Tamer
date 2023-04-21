package au.com.cahaya.asas.util.cli;

/**
 * <p>This class creates a output file option.</p>
 *
 * @author Mathew Pole
 * @since   November 2004
 * @version $Revision:$
 *
 * @see java.io.File
 */
public class OutFileOption extends FileOption {

    public static final String cValue = "outFile";

    /**
   *
   */
    public OutFileOption() {
        super(cValue, cValue, "output file");
    }
}

package au.com.cahaya.hubung.file.util.cli;

import org.apache.commons.cli.Option;

/**
 *<p>This class creates a option indicating that a recursive algorithm
 * should be used.</p>
 *
 * @author Mathew Pole
 * @since 05/01/2008
 * @version $Revision$
 */
public class EnableFileDetailOption extends Option {

    public static final String cValue = "enableFileDetail";

    /**
   *
   */
    public EnableFileDetailOption() {
        super(cValue, false, "Enable lookups using the file detail table");
    }
}

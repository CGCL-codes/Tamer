package au.com.cahaya.asas.util.cli;

import org.apache.commons.cli.Option;

/**
 * <p>This class creates an persistence option. The value of the option
 * should match the value of the name attribute of a persistence-unit entity
 * in the persistence.xml file.</p>
 *
 * @author Mathew Pole
 * @since May 2008
 * @version $Revision$
 */
public class PersistenceOption extends Option {

    public static final String cValue = "persistence";

    /**
   * Constructor
   */
    public PersistenceOption() {
        super(cValue, true, "set the name of the persistence unit");
    }
}

package au.com.cahaya.asas.util.cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * <p>This class creates a property file option.</p>
 *
 * @author Mathew Pole
 * @since   November 2004
 * @version $Revision:$
 *
 * @see java.io.File
 */
public class PropertyFileOption extends FileOption {

    public static final String cValue = "propertyFile";

    /**
   *
   */
    public PropertyFileOption() {
        super(cValue, cValue, "output file");
    }

    /**
   * @throws IOException 
   * 
   */
    public Properties load() throws IOException {
        Properties p = new Properties();
        BufferedReader r = new BufferedReader(new FileReader(new File(getValue())));
        p.load(r);
        r.close();
        return p;
    }
}

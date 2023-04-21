package net.sf.dub.application.test;

import java.io.File;
import net.sf.dub.application.data.Configuration;
import net.sf.dub.application.data.ConfigurationLoader;
import junit.framework.TestCase;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class ConfigurationLoaderTest extends TestCase {

    public void testLoad() throws Exception {
        Configuration config = new Configuration();
        ConfigurationLoader loader = new ConfigurationLoader();
        File file = new File(this.getClass().getResource("testdata/dub.jar").getFile());
        loader.loadConfiguration(file, config);
    }
}

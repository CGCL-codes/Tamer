package net.sourceforge.harness.xml.factory;

import java.util.Properties;
import net.sourceforge.harness.xml.jaxb.Os;
import net.sourceforge.harness.xml.util.OsPropertyType;
import org.apache.log4j.Logger;

/**
 * XML 'java' element factory.
 *
 * <p><b>History:</b>
 * <ul>
 *   <li>20020408, Initial file</li>
 * </ul>
 *
 * <p><b>CVS Information:</b><br>
 * <i>
 * $Date: 2002/10/09 13:11:32 $<br>
 * $Revision: 1.2 $<br>
 * $Author: mgl $<br>
 * </i>
 *
 * @author <a href="mailto:mgl@users.sourceforge.net">Marcel Schepers</a>
 */
public class OsFactory implements Factory {

    private static final String CLASSNAME = OsFactory.class.getName();

    private static final Logger LOGGER = Logger.getLogger(CLASSNAME);

    private static OsFactory factory = null;

    private OsFactory() {
    }

    /**
   * Creates a new instance of JavaFactory. Using this method ensures
   * singleton behaviour.
   */
    public static OsFactory getInstance() {
        if (factory == null) {
            factory = new OsFactory();
        }
        return factory;
    }

    /**
   * creates a new validate XML element.
   *
   * @return a new created XML element.
   */
    public Object createElement() {
        LOGGER.debug("createElement() [IN ]");
        Properties properties = System.getProperties();
        Os os = new Os();
        os.setArch(properties.getProperty(OsPropertyType.ARCH.toString()));
        os.setName(properties.getProperty(OsPropertyType.NAME.toString()));
        os.setVersion(properties.getProperty(OsPropertyType.VERSION.toString()));
        LOGGER.debug("createElement() [OUT]");
        LOGGER.debug("  return: " + os);
        return os;
    }
}

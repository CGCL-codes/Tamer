package au.org.emii.portal.util;

import java.util.Properties;

/**
 *
 * @author geoff
 */
public interface PropertiesWriter {

    boolean write(String filename, Properties props, String portalUsername);
}

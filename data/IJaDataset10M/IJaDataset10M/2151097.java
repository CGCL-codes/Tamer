package pl.org.waff;

import javax.servlet.http.*;
import java.util.Properties;

/**
 *
 * @author greg
 */
public interface SecurityManager {

    public void init(Properties properties, String mobileDeviceHandling, String standardDevicePrefix, String mobileDevicePrefix);

    public boolean pathAccessible(String pathName, HttpServletRequest req, SiteNavigator navigator);

    public boolean objectAccessible(String objectName, boolean accessibleByDefault, HttpServletRequest req);

    public boolean objectMethodAccessible(String objectName, String methodName, boolean accessibleByDefault, HttpServletRequest req);
}

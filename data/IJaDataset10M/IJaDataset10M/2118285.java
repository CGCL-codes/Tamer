package pulpcore.platform.applet;

import java.applet.Applet;
import pulpcore.CoreSystem;

/**
 * Provide public access to the Java Applet that PulpCore creates and uses internally.
 * The PulpCore AppletAppContext class has a reference to the Applet,
 * but the reference is private and the getter, getApplet(), is package-private.
 * AppletAccessor is in the same package as AppletAppContext,
 * so it can call the AppletAppContext getApplet() method.
 * @author <a href="mailto:ken@primordion.com">Ken Webb</a>
 * @see <a href="http://www.primordion.com/Xholon">Xholon Project website</a>
 * @since 0.8.1 (Created on January 13, 2011)
 */
public class AppletAccessor {

    /**
	 * Get a reference to the Applet instance that PulpCore creates and uses internally.
	 * @return An Applet instance.
	 */
    public static Applet getApplet() {
        Applet applet = ((AppletAppContext) CoreSystem.getThisAppContext()).getApplet();
        return applet;
    }
}

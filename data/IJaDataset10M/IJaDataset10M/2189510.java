package org.xito.launcher.applet;

import java.util.logging.*;
import org.xito.boot.*;

/**
 *
 * @author  Deane
 */
public class AppletClassLoader extends AppClassLoader {

    public AppletClassLoader(AppletDesc appletDesc, ClassLoader parent) throws ServiceNotFoundException {
        super(appletDesc, parent);
    }

    public AppletDesc getAppletDesc() {
        return (AppletDesc) super.getAppDesc();
    }
}

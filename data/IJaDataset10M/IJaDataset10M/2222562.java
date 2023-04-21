package net.sf.moviekebab.service.implementation.swing;

import org.apache.commons.lang.SystemUtils;
import net.sf.moviekebab.service.SwingFactory;

/**
 * @author Laurent Caillette
 */
public final class SwingFactoryHelper {

    private static final String FORCE_JGOODIES_LOOKS_SYSTEMPROPERTY = "moviekebab.forcejgoodieslooks";

    private SwingFactoryHelper() {
    }

    /**
   *
   * @return a non-null object.
   */
    public static SwingFactory createSwingFactory() {
        final boolean jgoodiesLooks = "true".equalsIgnoreCase(System.getProperty(FORCE_JGOODIES_LOOKS_SYSTEMPROPERTY)) || !SystemUtils.IS_OS_MAC_OSX;
        if (jgoodiesLooks) {
            return new JgoodiesLooksSwingFactory();
        } else {
            return new MacosxQuaquaSwingFactory();
        }
    }
}

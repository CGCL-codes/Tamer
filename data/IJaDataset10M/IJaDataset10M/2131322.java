package net.entropysoft.jmx.plugin.mbeanview;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "net.entropysoft.jmx.plugin.mbeanview.messages";

    public static String JmxManagedBeanView_AttributesSection;

    public static String JmxManagedBeanView_JmxBeanAttributes;

    public static String JmxManagedBeanView_JmxBeanNotifications;

    public static String JmxManagedBeanView_JmxBeanOperations;

    public static String JmxManagedBeanView_KeepMonitored;

    public static String JmxManagedBeanView_NoJmxBeanSelected;

    public static String JmxManagedBeanView_NotificationsSection;

    public static String JmxManagedBeanView_OperationsSection;

    public static String JmxManagedBeanView_UrlAndStatus;

    public static String JmxManagedBeanViewPreferenceDialog_DelayBetweenEachUpdate;

    public static String JmxManagedBeanViewPreferenceDialog_SetUpdateDelay;

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}

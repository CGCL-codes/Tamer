package net.sourceforge.eclipsetrader.internal.ui.dialogs;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "net.sourceforge.eclipsetrader.internal.ui.dialogs.messages";

    private Messages() {
    }

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    public static String CreateSecurityGroupDialog_Description;

    public static String CreateSecurityGroupDialog_DialogTitle;

    public static String CreateSecurityGroupDialog_EmptyNameMessage;

    public static String CreateSecurityGroupDialog_ExistingNameMessage;

    public static String CreateSecurityGroupDialog_GroupName;

    public static String CreateSecurityGroupDialog_SelectParentGroup;

    public static String CreateSecurityGroupDialog_ShellTitle;
}

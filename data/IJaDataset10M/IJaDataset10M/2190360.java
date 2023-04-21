package tr.view;

import javax.swing.ImageIcon;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import tr.util.ResourceUtils;

/**
 * View resources.
 *
 * @author Jeremy Moore (jimoore@netspace.net.au)
 */
public class Resources {

    private static final boolean isOSMac = Utilities.getOperatingSystem() == Utilities.OS_MAC;

    public static final String ADD_TOOLTIP = (isOSMac) ? NbBundle.getMessage(Resources.class, "Add_ToolTip_Mac") : NbBundle.getMessage(Resources.class, "Add_ToolTip");

    public static final String DELETE_TOOLTIP = (isOSMac) ? NbBundle.getMessage(Resources.class, "Delete_ToolTip_Mac") : NbBundle.getMessage(Resources.class, "Delete_ToolTip");

    public static final String PROCESS_TOOLTIP = (isOSMac) ? NbBundle.getMessage(Resources.class, "Process_ToolTip_Mac") : NbBundle.getMessage(Resources.class, "Process_ToolTip");
}

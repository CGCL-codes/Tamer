package mipt.aaf.swing.resources;

import javax.swing.SwingUtilities;
import mipt.gui.UIUtils;

/**
 * Sets LookAndFeel. Uses actionCommand = specification.toString()
 *  as short LAF name
 * @author Evdokimov
 */
public class SetLookAndFeel extends SwingApplicationCommand {

    /**
	 * @see mipt.aaf.command.Command#execute(java.lang.Object)
	 */
    public void execute(Object specification) {
        if (specification == null) return;
        setLookAndFeel(specification.toString());
        SwingUtilities.updateComponentTreeUI(getApplView().getRootView().getRootComponent());
    }

    protected void setLookAndFeel(String laf) {
        UIUtils.getInstance().setLookAndFeel(laf);
    }
}

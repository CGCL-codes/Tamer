package net.sf.traser.configtool;

import javax.swing.JPanel;
import net.sf.traser.common.LabelManager;

/**
 * The general operations which can be applied on the
 * TraSer config panels.
 * @author karnokd, 2008.01.22.
 * @version $Revision 1.0$
 */
public interface TraserConfigPanel {

    /**
	 * @return The editor panel, cannot be null
	 */
    JPanel getPanel();

    /**
	 * @return The title of the panel
	 */
    String getTitle();

    /**
	 * Apply the changes made on the panel.
	 */
    void apply();

    /**
	 * Revert to the current config file values.
	 */
    void revert();

    /**
	 * Sets the labelmanager on this panel.
	 * @param labels the LabelManager
	 */
    void setLabels(LabelManager labels);

    /**
	 * Sets the entire config file on this panel.
	 * @param config the config file manager
	 */
    void setConfig(TraserConfigFileManager config);

    /**
	 * Set the alias of the advanced panels.
	 * @param alias the alias to set, can be null if it is a simple panel
	 */
    void setAlias(String alias);

    /**
	 * Release GUI resources allocated by the panel.
	 */
    void done();

    /**
	 * Set the apply/revert notification callback.
	 * @param callback the non-null callback to set
	 */
    void setApplyRevertCallback(ApplyRevertCallback callback);
}

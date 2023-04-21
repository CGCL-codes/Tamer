package org.gudy.azureus2.ui.swt.plugins;

import org.eclipse.swt.widgets.Composite;
import org.gudy.azureus2.plugins.ui.config.ConfigSection;

/**
 * 
 * @since 2.3.0.5
 */
public interface UISWTConfigSection extends ConfigSection {

    /**
	   * Create your own configuration panel here.  It can be anything that inherits
	   * from SWT's Composite class.
	   * Please be mindful of small screen resolutions
	   *
	   * @param parent The parent of your configuration panel
	   * @return your configuration panel
	   */
    public Composite configSectionCreate(Composite parent);

    /**
	   * Indicate if additional options are available to display a hint to the users
	   * 
	   * @return the highest user mode that reveals additional options (0 = Beginner, 1 = Intermediate, 2 = Advanced)
	   */
    public int maxUserMode();
}

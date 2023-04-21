package org.columba.core.gui.frame.api;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import org.columba.api.plugin.IExtensionInterface;

/**
 * A little box containing useful information which resides in the contextual area.
 * 
 * @author frd
 */
public interface IComponentBox extends IExtensionInterface {

    /**
	 * Returns technical name. Should be unique.
	 * @return
	 */
    public String getTechnicalName();

    /**
	 * Return provider human-readable name
	 * @return
	 */
    public String getName();

    /**
	 * Return provider human-readable description
	 * @return
	 */
    public String getDescription();

    /**
	 * Return provider icon
	 * @return
	 */
    public ImageIcon getIcon();

    public JComponent getView();
}

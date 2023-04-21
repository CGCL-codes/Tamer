package com.inetmon.jn.statistic.distribution.ui;

import java.net.URL;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.Bundle;
import com.inetmon.jn.statistic.distribution.DistributionPlugin;
import com.inetmon.jn.statistic.distribution.views.VLANBase2View;
import com.inetmon.jn.statistic.distribution.views.VLANProtocolView;

/**
 * @author   Administrator
 */
public class VLANChangeColorAction extends Action {

    /**
	 * The view that is associated with this listener
	 */
    private VLANBase2View view;

    private VLANProtocolView view2;

    /**
	 * Icon to use for the button image 
	 */
    public static final String ICON_FILE = "icons/colors.gif";

    /**
	 * Construct a change color action that is associated with view
	 * 
	 * @param view
	 *            The associated view
	 *  
	 */
    public VLANChangeColorAction(VLANBase2View view) {
        super();
        this.view = view;
        Bundle bundle = Platform.getBundle(DistributionPlugin.PLUGIN_ID);
        Path path = new Path(ICON_FILE);
        URL fileURL = Platform.find(bundle, path);
        this.setImageDescriptor(ImageDescriptor.createFromURL(fileURL));
        this.setToolTipText("Change the graph color preferences");
    }

    public VLANChangeColorAction(VLANProtocolView view2) {
        super();
        this.view2 = view2;
        Bundle bundle = Platform.getBundle(DistributionPlugin.PLUGIN_ID);
        Path path = new Path(ICON_FILE);
        URL fileURL = Platform.find(bundle, path);
        this.setImageDescriptor(ImageDescriptor.createFromURL(fileURL));
        this.setToolTipText("Change the graph color preferences");
    }
}

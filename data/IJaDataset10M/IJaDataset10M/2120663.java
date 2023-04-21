package com.inetmon.jn.statistic.distribution.ui;

import java.net.URL;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.Bundle;
import com.inetmon.jn.statistic.distribution.DistributionPlugin;
import com.inetmon.jn.statistic.distribution.views.BaseView;
import com.inetmon.jn.statistic.distribution.views.VLANBase2View;
import com.inetmon.jn.statistic.distribution.views.VLANIPv4ProtocolView;
import com.inetmon.jn.statistic.distribution.views.VLANProtocolView;

/**
 * @author   Nicolas Parcollet  Action used to change the type of the shown graph
 */
public class VLANChangeTypeAction2 extends Action {

    /**
	 * The view that is associated with this listener
	 */
    private VLANIPv4ProtocolView view;

    /**
	 * The type of graph to set when clicked
	 */
    private int type;

    /**
	 * Construct a distribution view listener that is associated with view
	 * 
	 * @param view
	 *            The associated view
	 * 
	 * @param type
	 *            The type of graph to be set when event is fired
	 */
    public VLANChangeTypeAction2(VLANIPv4ProtocolView view, int type) {
        super();
        this.view = view;
        this.type = type;
        Bundle bundle = Platform.getBundle(DistributionPlugin.PLUGIN_ID);
        Path path = new Path("icons/" + BaseView.TYPE_LABELS[type].toLowerCase().replace(' ', '_') + ".gif");
        URL fileURL = Platform.find(bundle, path);
        this.setImageDescriptor(ImageDescriptor.createFromURL(fileURL));
        this.setToolTipText(BaseView.TYPE_LABELS[type]);
    }

    public void run() {
        view.changeType(type);
    }
}

package org.eclipse.mylyn.internal.java.ui.actions;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.debug.internal.ui.views.launch.LaunchView;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.mylyn.context.ui.AbstractFocusViewAction;
import org.eclipse.mylyn.context.ui.InterestFilter;
import org.eclipse.ui.IViewPart;

/**
 * @author Mik Kersten
 */
public class FocusDebugViewAction extends AbstractFocusViewAction {

    public FocusDebugViewAction() {
        super(new InterestFilter(), true, true, false);
    }

    @Override
    public List<StructuredViewer> getViewers() {
        List<StructuredViewer> viewers = new ArrayList<StructuredViewer>();
        IViewPart view = super.getPartForAction();
        if (view instanceof LaunchView) {
            LaunchView launchView = (LaunchView) view;
            viewers.add((StructuredViewer) launchView.getViewer());
        }
        return viewers;
    }
}

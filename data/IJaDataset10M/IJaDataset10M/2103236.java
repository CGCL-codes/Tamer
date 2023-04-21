package com.anaxima.eslink.internal.ui;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.mylyn.context.ui.InterestFilter;
import org.eclipse.mylyn.resources.ui.FocusCommonNavigatorAction;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.navigator.CommonNavigator;

/**
 * @author vatert
 * 
 */
public class SessionViewFocusAction extends FocusCommonNavigatorAction {

    public SessionViewFocusAction() {
        super(new InterestFilter(), true, true, true);
    }

    protected SessionViewFocusAction(InterestFilter filter) {
        super(filter, true, true, true);
    }

    @Override
    public List<StructuredViewer> getViewers() {
        List<StructuredViewer> viewers = new ArrayList<StructuredViewer>();
        IViewPart view = super.getPartForAction();
        if (view instanceof CommonNavigator) {
            CommonNavigator navigator = (CommonNavigator) view;
            viewers.add(navigator.getCommonViewer());
        }
        return viewers;
    }
}

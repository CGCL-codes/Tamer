package com.dfruits.queries.ui.binding.viewers.tooltips;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.Viewer;
import com.dfruits.queries.ui.QueryObject;
import com.dfruits.queries.ui.binding.viewers.AbstractViewerInitializer;

public class TooltipSupportHookinInitializer extends AbstractViewerInitializer {

    public boolean canInitialize(QueryObject queryObject) {
        return super.canInitialize(queryObject) && doGetViewer(queryObject) instanceof ColumnViewer;
    }

    public boolean initialize(QueryObject queryObject) {
        ColumnViewerToolTipSupport.enableFor((ColumnViewer) doGetViewer(queryObject), ToolTip.NO_RECREATE);
        return true;
    }

    protected void doSpecializeViewer(Viewer viewer, QueryObject queryObject) {
    }
}

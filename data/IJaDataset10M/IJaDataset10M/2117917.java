package com.safi.workshop.timeBasedRouting.pak.factory;

import org.eclipse.gmf.runtime.diagram.ui.view.factories.BasicNodeViewFactory;
import com.safi.workshop.timeBasedRouting.pak.editpart.TimeBasedRoutingEditPart;
import com.safi.workshop.timeBasedRouting.pak.editpart.TimeBasedRoutingNameEditPart;
import com.safi.workshop.timeBasedRouting.pak.editpart.TimeItemEditPart;
import com.safi.workshop.timeBasedRouting.pak.editpart.TimeItemLabelTextEditPart;
import com.safi.workshop.timeBasedRouting.pak.editpart.TimeItemPanelEditPart;
import com.safi.workshop.view.factories.ActionstepViewFactoryFactory;
import com.safi.workshop.view.factories.ActionstepNameLabelViewFactory;

public class TimeBasedViewFactory implements ActionstepViewFactoryFactory {

    public TimeBasedViewFactory() {
    }

    @Override
    public BasicNodeViewFactory createShapeViewFactory(int id) {
        if (id == TimeItemEditPart.VISUAL_ID) return new TimeItemViewFactory(); else if (id == TimeBasedRoutingNameEditPart.VISUAL_ID) return new ActionstepNameLabelViewFactory(); else if (id == TimeItemLabelTextEditPart.VISUAL_ID) return new ActionstepNameLabelViewFactory(); else if (id == TimeBasedRoutingEditPart.VISUAL_ID) {
            return new TimeBasedRoutingViewFactory();
        } else if (id == TimeItemPanelEditPart.VISUAL_ID) {
            return new TimeItemPanelViewFactory();
        }
        return null;
    }
}

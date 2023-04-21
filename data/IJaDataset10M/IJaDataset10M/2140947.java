package net.entropysoft.jmx.plugin.jmxdashboard.outline;

import net.entropysoft.jmx.plugin.jmxdashboard.model.Dashboard;
import net.entropysoft.jmx.plugin.jmxdashboard.model.DashboardElement;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

/**
 * factory that creates parts for dashboard elements for the outline page
 * 
 * @author cedric
 *
 */
public class DashboardTreeEditPartFactory implements EditPartFactory {

    public EditPart createEditPart(EditPart context, Object model) {
        if (model instanceof Dashboard) return new DashboardTreeEditPart((Dashboard) model);
        if (model instanceof DashboardElement) return new DashboardElementTreeEditPart((DashboardElement) model);
        return null;
    }
}

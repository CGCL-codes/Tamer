package net.entropysoft.dashboard.plugin.dashboard.outline;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import net.entropysoft.dashboard.plugin.dashboard.gef.policy.DashboardElementComponentEditPolicy;
import net.entropysoft.dashboard.plugin.dashboard.model.DashboardElement;
import net.entropysoft.dashboard.plugin.dashboard.utils.DashboardElementIconFactory;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.swt.graphics.Image;

/**
 * TreeEditPart used in outline to handle dashboard elements 
 *  
 * @author cedric
 */
public class DashboardElementTreeEditPart extends AbstractTreeEditPart implements PropertyChangeListener {

    public DashboardElementTreeEditPart(DashboardElement component) {
        super(component);
    }

    public void activate() {
        if (!isActive()) {
            super.activate();
            ((DashboardElement) getModel()).addPropertyChangeListener(this);
        }
    }

    public void deactivate() {
        if (isActive()) {
            super.deactivate();
            ((DashboardElement) getModel()).removePropertyChangeListener(this);
        }
    }

    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new DashboardElementComponentEditPolicy());
    }

    protected String getText() {
        return DashboardElementIconFactory.getLabel(getModel().getClass());
    }

    protected Image getImage() {
        return DashboardElementIconFactory.getIconImage(getModel().getClass());
    }

    public void propertyChange(PropertyChangeEvent evt) {
        refreshVisuals();
    }
}

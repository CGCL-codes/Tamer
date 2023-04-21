package org.km.xplane.airports.actions;

import org.eclipse.jface.wizard.IWizard;
import org.km.xplane.airports.gui.wizards.CreateWindsockWizard;
import org.km.xplane.airports.math.Position;

/**
 * @author kmeier
 *
 */
public class CreateWindsockAction extends CreateAirportPartAction {

    @Override
    protected IWizard createWizard(Position positions[]) {
        return new CreateWindsockWizard(window, this.selectedAirport, positions);
    }

    @Override
    public int neededPoints() {
        return 1;
    }

    @Override
    public String getMessage(int n) {
        return "Select Windsock Position";
    }
}

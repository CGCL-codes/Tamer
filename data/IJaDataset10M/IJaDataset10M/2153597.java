package org.km.xplane.airports.gui.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbenchWindow;
import org.km.xplane.airports.math.Position;
import org.km.xplane.airports.model.Airport;

/**
 * @author kmeier
 *
 */
public class CreateLightBeaconWizard extends Wizard {

    private LightBeaconWizardPage _page;

    public CreateLightBeaconWizard(IWorkbenchWindow window, Airport selectedAirport, Position[] positions) {
        _page = new LightBeaconWizardPage("Create Light Beacon", selectedAirport, positions);
        addPage(_page);
        _page.setSelectedAirport(selectedAirport);
        setWindowTitle("Create New Light Beacon");
    }

    @Override
    public boolean performFinish() {
        _page.createLightBeacon();
        return true;
    }
}

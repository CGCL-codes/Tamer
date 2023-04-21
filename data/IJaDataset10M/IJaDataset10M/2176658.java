package net.sf.mzmine.modules.peaklistmethods.identification.adductsearch;

import net.sf.mzmine.parameters.parametertypes.AdductsComponent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * An action to handle resetting the adducts list.
 *
 * @author $Author: cpudney $
 * @version $Revision: 3103 $
 */
public class DefaultAdductsAction extends AbstractAction {

    /**
     * Create the action.
     */
    public DefaultAdductsAction() {
        super("Reset");
        putValue(SHORT_DESCRIPTION, "Reset adduct choices to default set");
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final AdductsComponent parent = (AdductsComponent) SwingUtilities.getAncestorOfClass(AdductsComponent.class, (Component) e.getSource());
        if (parent != null) {
            parent.setChoices(AdductType.getDefaultValues());
        }
    }
}

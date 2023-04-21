package net.sf.freecol.client.gui.action;

import java.awt.event.ActionEvent;
import net.sf.freecol.client.FreeColClient;
import net.sf.freecol.client.gui.GUI;
import net.sf.freecol.common.option.OptionGroup;

/**
 * An action for displaying the Map Generator Options.
 */
public class ShowMapGeneratorOptionsAction extends FreeColAction {

    public static final String id = "mapGeneratorOptionsAction";

    /**
     * Creates a new <code>ShowMapGeneratorOptionsAction</code>.
     *
     * @param freeColClient The main controller object for the client.
     * @param gui 
     */
    ShowMapGeneratorOptionsAction(FreeColClient freeColClient, GUI gui) {
        super(freeColClient, gui, id);
    }

    /**
     * Applies this action.
     *
     * @param e The <code>ActionEvent</code>.
     */
    public void actionPerformed(ActionEvent e) {
        OptionGroup mgo = freeColClient.getGame().getMapGeneratorOptions();
        gui.showMapGeneratorOptionsDialog(mgo, false, false);
    }
}

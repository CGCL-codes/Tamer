package net.sf.vorg.routecalculator.models;

import java.util.logging.Logger;
import net.sf.vorg.core.VORGConstants;
import net.sf.vorg.core.models.WindCell;
import net.sf.vorg.routecalculator.core.GeoTimeLocation;

public class RouteCell2 extends RouteCell {

    private static Logger logger = Logger.getLogger("net.sf.vorg.routecalculator.models");

    /**
	 * Load the cell data with the required wind information and the entry and exit locations. Update the time
	 * on the exit location based on the current cell calculations and evaluation.
	 */
    public RouteCell2(final WindCell windCell, GeoTimeLocation entry, GeoTimeLocation exit) {
        cell = windCell;
        entryLocation = entry;
        exitLocation = exit;
        calculateCell();
        exit.addTTC(entry, getTTC() * VORGConstants.H2S);
    }
}

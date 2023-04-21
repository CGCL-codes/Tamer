package org.xptools.xpairports.gui.widgets.airportpane;

import java.util.ArrayList;
import java.util.Collection;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Image;
import org.xptools.xpairports.gui.widgets.AirportPane;
import org.xptools.xpairports.gui.widgets.airportdata.DrawType;

/**
 * @author kmeier
 *
 */
public class SimpleAirportDrawData extends AbstractAirportPaneDrawData {

    /**
	 * @param pane
	 * @param e
	 * @param data
	 */
    public SimpleAirportDrawData(AirportPane pane, PaintEvent e, AirportPaneData data) {
        super(pane, e, data);
    }

    @Override
    protected void registerLayers() {
        super.registerLayer(DrawType.BACKGROUND);
        super.registerLayer(DrawType.DRAWING);
    }

    @Override
    protected Collection<Integer> getRedrawLayers() {
        ArrayList<Integer> layers = new ArrayList<Integer>(super.getRedrawLayers());
        layers.add(DrawType.DRAWING);
        return layers;
    }
}

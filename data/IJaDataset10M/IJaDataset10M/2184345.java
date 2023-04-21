package com.od.jtimeseries.ui.timeserious.rootcontext;

import com.od.jtimeseries.context.TimeSeriesContext;
import com.od.jtimeseries.identifiable.Identifiable;
import com.od.jtimeseries.ui.config.DesktopConfiguration;
import com.od.jtimeseries.ui.config.ExportableConfig;
import com.od.jtimeseries.ui.identifiable.DesktopContext;
import com.od.jtimeseries.ui.uicontext.ImportItem;
import com.od.jtimeseries.ui.util.CascadeLocationCalculator;
import javax.swing.*;
import java.awt.*;

/**
* Created by IntelliJ IDEA.
* User: Nick
* Date: 03/05/11
* Time: 11:10
*
*/
class DesktopExportableConfigImportUtility extends ExportableConfigImportUtility {

    private static final int DESKTOP_IMPORT_WIDTH = 800;

    private static final int DESKTOP_IMPORT_HEIGHT = 600;

    private CascadeLocationCalculator cascadeLocationCalculator = new CascadeLocationCalculator(50, 50);

    private Rectangle lastDesktopImportLocation;

    private TimeSeriesContext rootContext;

    DesktopExportableConfigImportUtility(TimeSeriesContext rootContext) {
        this.rootContext = rootContext;
    }

    protected void reset() {
        lastDesktopImportLocation = null;
    }

    protected Identifiable getRealTargetContext(Identifiable targetContext) {
        return rootContext;
    }

    protected ImportItem doGetImportDetails(Component component, ExportableConfig s, Identifiable target) {
        Rectangle parentWindowBounds = component instanceof Window ? component.getBounds() : SwingUtilities.getWindowAncestor(component).getBounds();
        if (lastDesktopImportLocation == null) {
            lastDesktopImportLocation = parentWindowBounds;
        }
        DesktopConfiguration c = (DesktopConfiguration) s;
        c.setShown(true);
        c.setFrameExtendedState(JFrame.NORMAL);
        lastDesktopImportLocation = cascadeLocationCalculator.getNextLocation(lastDesktopImportLocation, parentWindowBounds, DESKTOP_IMPORT_WIDTH, DESKTOP_IMPORT_HEIGHT);
        c.setFrameLocation(lastDesktopImportLocation);
        return new ImportItem(target.getPath() + Identifiable.NAMESPACE_SEPARATOR + c.getTitle(), c.getTitle(), DesktopContext.class, c);
    }

    public boolean handlesOwnImport() {
        return false;
    }
}

package org.primordion.xholon.tutorials.StupidModel;

import org.primordion.xholon.app.Application;
import org.primordion.xholon.base.XholonTime;

/**
	StupidModel5 application - Xholon Java
	<p>Xholon 0.5 http://www.primordion.com/Xholon</p>
*/
public class AppStupidModel5 extends Application {

    public AppStupidModel5() {
        super();
    }

    public double getMaxConsumptionRate() {
        return BugStupidModel5.maxConsumptionRate;
    }

    public double getMaxFoodProductionRate() {
        return HabitatCellStupidModel5.maxFoodProductionRate;
    }

    public void setMaxConsumptionRate(double maxConsumptionRate) {
        BugStupidModel5.maxConsumptionRate = maxConsumptionRate;
    }

    public void setMaxFoodProductionRate(double maxFoodProductionRate) {
        HabitatCellStupidModel5.maxFoodProductionRate = maxFoodProductionRate;
    }

    protected void step() {
        root.act();
        XholonTime.sleep(getTimeStepInterval());
        if (getUseGridViewer()) {
            for (int vIx = 0; vIx < gridViewers.size(); vIx++) {
                GridViewerDetails gvd = (GridViewerDetails) gridViewers.get(vIx);
                if (gvd.useGridViewer) {
                    gvd.gridFrame.setInfoLabel("Time step: " + timeStep);
                    gvd.gridPanel.paintComponent(gvd.gridPanel.getGraphics());
                }
            }
        }
    }

    public void wrapup() {
        if (getUseGridViewer()) {
            for (int vIx = 0; vIx < gridViewers.size(); vIx++) {
                GridViewerDetails gvd = (GridViewerDetails) gridViewers.get(vIx);
                if (gvd.useGridViewer) {
                    gvd.gridFrame.setInfoLabel("Time step: " + timeStep);
                    gvd.gridPanel.paintComponent(gvd.gridPanel.getGraphics());
                }
            }
        }
        super.wrapup();
    }

    /** main
 * @param args
 */
    public static void main(String[] args) {
        appMain(args, "org.primordion.xholon.tutorials.StupidModel.AppStupidModel5", "./config/StupidModel/StupidModel5/StupidModel5_xhn.xml");
    }
}

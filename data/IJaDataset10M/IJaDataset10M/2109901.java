package org.primordion.user.app.XBar;

import org.primordion.xholon.app.Application;
import org.primordion.xholon.base.XholonTime;
import org.primordion.xholon.exception.XholonConfigurationException;

/**
	XBar application - Xholon Java
	<p>Xholon 0.7 http://www.primordion.com/Xholon</p>
*/
public class AppXBar_ex1 extends Application {

    public AppXBar_ex1() {
        super();
    }

    public void initialize(String configFileName) throws XholonConfigurationException {
        super.initialize(configFileName);
    }

    protected void step() {
        root.preAct();
        if (getUseDataPlotter()) {
            chartViewer.capture(timeStep);
        }
        root.act();
        root.postAct();
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
        root.preAct();
        if (getUseDataPlotter()) {
            chartViewer.capture(getTimeStep());
        }
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

    protected boolean shouldBePlotted(org.primordion.xholon.base.IXholon modelNode) {
        if ((modelNode.getXhType() & org.primordion.xholon.base.IXholonClass.XhtypePurePassiveObject) == org.primordion.xholon.base.IXholonClass.XhtypePurePassiveObject) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        appMain(args, "org.primordion.user.app.AppXBar_ex1", "./config/user/XBar/XBar_xhn.xml");
    }
}

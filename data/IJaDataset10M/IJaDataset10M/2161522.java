package org.primordion.user.app.climatechange.model04;

import org.primordion.xholon.app.Application;
import org.primordion.xholon.base.IPortInterface;
import org.primordion.xholon.base.PortInterface;
import org.primordion.xholon.base.XholonTime;
import org.primordion.xholon.exception.XholonConfigurationException;

/**
	model04 application - Xholon Java
	<p>Xholon 0.8.1 http://www.primordion.com/Xholon</p>
*/
public class Appmodel04 extends Application {

    public Appmodel04() {
        super();
    }

    public void initialize(String configFileName) throws XholonConfigurationException {
        super.initialize(configFileName);
        if (getUseInteractions()) {
            interaction.setMaxNameLen(100);
            interaction.setMaxDataLen(6);
            IPortInterface providedRequiredInterface = new PortInterface();
            providedRequiredInterface.setInterface(Xhmodel04.getSignalIDs());
            providedRequiredInterface.setInterfaceNames(Xhmodel04.getSignalNames());
            interaction.setProvidedRequiredInterface(providedRequiredInterface);
        }
    }

    protected void step() {
        root.act();
        if (getUseDataPlotter()) {
            chartViewer.capture(timeStep);
        }
        root.postAct();
        if (shouldStepView) {
            view.act();
        }
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
        appMain(args, "org.primordion.user.app.climatechange.model04.Appmodel04", "/org/primordion/user/app/climatechange/model04/_xhn.xml");
    }
}

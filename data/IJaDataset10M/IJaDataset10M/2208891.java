package org.primordion.user.app.Chameleon;

import org.primordion.xholon.app.Application;
import org.primordion.xholon.base.XholonTime;
import org.primordion.xholon.exception.XholonConfigurationException;

/**
	Chameleon application - Xholon Java
	<p>Xholon 0.8.1 http://www.primordion.com/Xholon</p>
*/
public class AppChameleon extends Application {

    /** how many time step intervals to chart */
    private int chartInterval = 1;

    public AppChameleon() {
        super();
    }

    public int getChartInterval() {
        return chartInterval;
    }

    public void setChartInterval(int chartInterval) {
        this.chartInterval = chartInterval;
    }

    public boolean setParam(String pName, String pValue) {
        if ("TimeStepMultiplier".equals(pName)) {
            XhChameleon.setTimeStepMultiplier(Integer.parseInt(pValue));
            return true;
        } else if ("ChartInterval".equals(pName)) {
            setChartInterval(Integer.parseInt(pValue));
            return true;
        }
        return super.setParam(pName, pValue);
    }

    public void initialize(String configFileName) throws XholonConfigurationException {
        super.initialize(configFileName);
    }

    protected void step() {
        root.preAct();
        for (int i = 0; i < XhChameleon.timeStepMultiplier; i++) {
            if (getUseDataPlotter() && (chartViewer != null) && ((i % chartInterval) == 0)) {
                chartViewer.capture((((double) i / XhChameleon.timeStepMultiplier)) + timeStep);
            }
            root.act();
        }
        if (shouldStepView) {
            view.act();
        }
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
        if (getUseDataPlotter() && (chartViewer != null)) {
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
        appMain(args, "org.primordion.user.app.Chameleon.AppChameleon", "/org/primordion/user/app/Chameleon/Chameleon_xhn.xml");
    }
}

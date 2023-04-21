package gov.sns.apps.mpx;

import java.util.*;
import java.io.File;
import gov.sns.xal.smf.AcceleratorSeq;
import gov.sns.xal.smf.AcceleratorNode;
import gov.sns.xal.smf.impl.ProfileMonitor;
import gov.sns.ca.*;
import gov.sns.xal.model.mpx.ModelProxy;
import gov.sns.xal.model.ModelException;
import gov.sns.tools.apputils.wirescan.*;

public class WsData {

    double[][] xData;

    double[][] yData;

    String[] wsNames;

    ModelProxy theMP;

    /**
	 * for live WS PVs
	 * @param accSeq Accelerator Sequence in use
	 * @param mp ModelProxy in use
	 */
    public WsData(AcceleratorSeq accSeq, ModelProxy mp) {
        theMP = mp;
        List<AcceleratorNode> wss = accSeq.getAllNodesOfType("WS");
        wss = AcceleratorSeq.filterNodesByStatus(wss, true);
        ArrayList<AcceleratorNode> allWSs = new ArrayList<AcceleratorNode>(wss);
        xData = new double[2][allWSs.size()];
        yData = new double[2][allWSs.size()];
        wsNames = new String[allWSs.size()];
        for (int i = 0; i < allWSs.size(); i++) {
            try {
                xData[0][i] = mp.stateForElement(((ProfileMonitor) allWSs.get(i)).getId()).getPosition();
                yData[0][i] = xData[0][i];
                wsNames[i] = ((ProfileMonitor) allWSs.get(i)).getId();
            } catch (ModelException e) {
                xData[0][i] = 0.;
                yData[0][i] = 0.;
                System.out.println(e);
            }
            try {
                xData[1][i] = ((ProfileMonitor) allWSs.get(i)).getHSigmaF();
                yData[1][i] = ((ProfileMonitor) allWSs.get(i)).getVSigmaF();
            } catch (ConnectionException e) {
                xData[1][i] = 0.;
                yData[1][i] = 0.;
                System.out.println(e);
            } catch (GetException e) {
                xData[1][i] = 0.;
                yData[1][i] = 0.;
                System.out.println(e);
            }
        }
    }

    public WsData(AcceleratorSeq accSeq, ModelProxy mp, File file) {
        ArrayList allWSs = (ArrayList) accSeq.getAllNodesOfType("WS");
        xData = new double[2][allWSs.size()];
        yData = new double[2][allWSs.size()];
        wsNames = new String[allWSs.size()];
        WireDataFileParser wdfp = new WireDataFileParser();
        ArrayList wires = wdfp.readFile(file);
        HashMap wireMap = wdfp.getWireMap();
        for (int i = 0; i < allWSs.size(); i++) {
            try {
                wsNames[i] = ((ProfileMonitor) allWSs.get(i)).getId();
                xData[0][i] = mp.stateForElement(((ProfileMonitor) allWSs.get(i)).getId()).getPosition();
                yData[0][i] = xData[0][i];
                WireData wd = (WireData) wireMap.get(wsNames[i]);
                xData[1][i] = wd.getZFitSigma();
                yData[1][i] = wd.getXFitSigma();
            } catch (ModelException e) {
                xData[0][i] = 0.;
                ;
                yData[0][i] = 0.;
                ;
                xData[1][i] = 0.;
                ;
                yData[1][i] = 0.;
                ;
                System.out.println(e);
            }
        }
    }

    protected double[][] getXData() {
        return xData;
    }

    protected double[][] getYData() {
        return yData;
    }

    protected String[] getNames() {
        return wsNames;
    }
}

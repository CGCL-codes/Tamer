package vademecum.commandline.tests;

import java.awt.Color;
import vademecum.core.experiment.ExperimentNode;
import vademecum.data.IDataGrid;
import vademecum.data.test.TestGrids;
import vademecum.extensionPoint.DefaultDataNode;
import vademecum.extensionPoint.IDataNode;
import vademecum.io.reader.Lrn;
import vademecum.ui.visualizer.VisualizerFrame;
import vademecum.ui.visualizer.panel.XplorePanel;
import vademecum.ui.visualizer.vgraphics.VGraphics;
import vademecum.ui.visualizer.vgraphics.D3.D3JMTAdapter;
import vademecum.ui.visualizer.vgraphics.D3.scatter.ScatterPlot3D;
import vademecum.ui.visualizer.vgraphics.box.text.VGTextBox;

public class Testing_3DScatterWithENode {

    public static void main(String[] args) {
        int frameWidth = 700;
        int frameHeight = 500;
        D3JMTAdapter canv3D = new D3JMTAdapter();
        ScatterPlot3D scatterPlot = new ScatterPlot3D(canv3D);
        scatterPlot.setBounds(30, 30, frameWidth - 60, frameHeight - 90);
        scatterPlot.updateCanvasSize();
        Lrn lrn = new Lrn();
        lrn.setProperty("file", "/home/kreed/coding/projectspace8/vademecum/data/samples/Tetra.lrn");
        lrn.init();
        while (!lrn.hasFinished()) {
            lrn.iterate();
        }
        IDataGrid grid = (IDataGrid) lrn.getOutput(IDataGrid.class);
        ExperimentNode enode = new ExperimentNode();
        enode.setMethod(lrn);
        scatterPlot.setDataGrid(grid, 0, 1, 2, Color.red);
        VGraphics vgTextBox = new VGTextBox("3DScatter");
        vgTextBox.setBounds(170, 15, 200, 22);
        vgTextBox.setZWeight(76);
        scatterPlot.connectFigure(vgTextBox);
        XplorePanel xPanel = new XplorePanel(enode);
        xPanel.addViewable(scatterPlot);
        scatterPlot.setBackgroundVisible(true);
        scatterPlot.setBackgroundColor(Color.white);
        scatterPlot.setBorderVisible(true);
        vgTextBox.setBackgroundVisible(false);
        vgTextBox.setBorderVisible(true);
        vgTextBox.setBorderColor(Color.red);
        VisualizerFrame vf = VisualizerFrame.getInstance();
        vf.initXplorePanel(xPanel);
        vf.initActionPanel();
        vf.validate();
        vf.pack();
        vf.setSize(frameWidth, frameHeight);
        vf.setVisible(true);
    }
}

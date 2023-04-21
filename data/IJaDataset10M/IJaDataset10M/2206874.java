package org.modss.facilitator.ui.result.test;

import org.modss.facilitator.ui.result.*;
import org.modss.facilitator.model.v1.*;
import org.modss.facilitator.model.v1.xml.*;
import org.modss.facilitator.shared.init.test.*;
import org.modss.facilitator.util.collection.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * A test program for both graphs sharing a selection model.
 *
 * @author John Farrell
 * @version 1.0
 */
public class BothGraphsUnitTest {

    public static void main(String args[]) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: BothGraphsUnitTest filename.dss");
            System.exit(1);
        }
        InitUtil.initFramework(new String[] { "org.modss.facilitator.ui.result.barGraph" }, new String[] { "org/modss/facilitator/ui/result" });
        JFrame pf = new JFrame("TEST BOTH GRAPHS - polar");
        JFrame bf = new JFrame("TEST BOTH GRAPHS - bar");
        pf.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt) {
                System.exit(0);
            }
        });
        bf.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt) {
                System.exit(0);
            }
        });
        AlternativeSelectionModel selectionModel = new AlternativeSelectionModel();
        BarGraph bg = new BarGraph(new SortOrder(true, SortOrder.MAXIMUM));
        PolarGraph pg = new PolarGraph();
        Analysis analysis = ModelDOMFactory.createAnalysis(new FileInputStream(args[0]));
        Cycle cycle = (Cycle) analysis.getCycles().elementAt(0);
        Run run = (Run) cycle.getRuns().elementAt(0);
        RankingNode rankNode = run.getResult();
        ResultNode resultNode = (ResultNode) rankNode.getAttributeObject();
        bg.setModel(resultNode, selectionModel);
        pg.setModel(resultNode, selectionModel);
        JScrollPane bscroll = new JScrollPane(bg);
        bf.getContentPane().add("Center", bscroll);
        bf.show();
        JScrollPane pscroll = new JScrollPane(pg);
        pf.getContentPane().add("Center", pscroll);
        pf.show();
        Dimension d = bg.getPreferredSize();
        if (d.height > 800) d.height = 800;
        d.width += bscroll.getVerticalScrollBar().getPreferredSize().width + 5;
        bscroll.setPreferredSize(d);
        bf.pack();
        d = pg.getPreferredSize();
        if (d.height > 800) d.height = 800;
        d.width += pscroll.getVerticalScrollBar().getPreferredSize().width + 5;
        pscroll.setPreferredSize(d);
        pf.pack();
        Rectangle bb = bf.getBounds();
        pf.setLocation(new Point(bb.x + bb.width + 10, bb.y));
    }
}

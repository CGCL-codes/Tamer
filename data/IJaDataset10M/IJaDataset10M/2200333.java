package org.sf.jspread.gui.plotter;

import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import org.sf.jspread.utils.*;
import org.sf.jspread.objects.*;

public class ExportGraphAdapter implements ActionListener {

    private SpreadPlotter plotter;

    private Spread spread;

    public ExportGraphAdapter(SpreadPlotter plotter) {
        this.plotter = plotter;
    }

    public void actionPerformed(ActionEvent e) {
        spread = SpreadManager.getSelectedSpread();
        if (spread != null) {
            JFileChooser exportFileChooser = new JFileChooser(SpreadProperties.getProperty("DATDIR"));
            exportFileChooser.setDialogTitle("Export Graph To File");
            int result = exportFileChooser.showSaveDialog(SpreadManager.getSpreadGUI());
            System.out.println(exportFileChooser.getSelectedFile() + spread.toString());
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    spread.getLog().info("Export Graph [" + spread.getSpreadID() + "] To File : " + exportFileChooser.getSelectedFile());
                    FileOutputStream outStream = new FileOutputStream(exportFileChooser.getSelectedFile());
                    PrintWriter printer = new PrintWriter(outStream);
                    printer.println("Spread plot - (short) " + spread.getSellSideTotQty() + " of " + spread.getSellSideRIC() + " (long) " + spread.getBuySideTotQty() + " of " + spread.getBuySideRIC() + " when spread >= " + spread.getSpreadValue());
                    printer.println("Time,SpreadValue," + spread.getSellSideRIC() + ",," + spread.getBuySideRIC() + ",");
                    printer.println(",,BidQty,Bid,Ask,AskQty");
                    Vector data_points = plotter.getSpreadGraph().getData().getDataPoints();
                    for (int i = 0; i < data_points.size(); i++) {
                        GraphPoint point = (GraphPoint) data_points.elementAt(i);
                        printer.println(point.getFormattedTime() + "," + point.getSpreadVal() + "," + point.getShortBidQty() + "," + point.getShortBid() + "," + point.getLongAsk() + "," + point.getLongAskQty());
                    }
                    printer.close();
                    outStream.close();
                } catch (Exception ex) {
                    SpreadManager.getLog().warn(ex.getMessage());
                    ex.printStackTrace(SpreadManager.getLog().getPrinter());
                }
            }
        }
    }
}

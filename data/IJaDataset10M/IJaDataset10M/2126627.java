package org.tzi.use.gui.views;

import java.awt.*;
import java.util.Random;
import javax.swing.*;

/** 
 * @version     $ProjectVersion: 0.393 $
 * @author  Mark Richters
 */
public class LineChartView_test {

    public static void main(String args[]) {
        JFrame f = new JFrame("LineChartView_test");
        Color[] colors = { Color.red, Color.blue };
        LineChartView lcv = new LineChartView(50, 2, colors);
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(lcv), BorderLayout.CENTER);
        f.setContentPane(contentPane);
        f.pack();
        f.setVisible(true);
        int[] values = new int[2];
        Random rand = new Random();
        try {
            for (int n = 0; n < 1000; n++) {
                Thread.sleep(100);
                for (int i = 0; i < values.length; i++) {
                    values[i] = rand.nextInt(100);
                    System.out.println("n = " + n + " value = " + values[i]);
                }
                lcv.addValues(values);
            }
        } catch (InterruptedException ex) {
        }
    }
}

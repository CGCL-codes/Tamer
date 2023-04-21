package jat.plot;

import ptolemy.plot.*;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JFrame;

/**
 * <P>
 * The ThreePlots Class provides a way to create a page with three plots
 * using Ptplot.
 *
 * @author 
 * @version 1.0
 */
public class ThreePlots extends JFrame {

    /** the top plot */
    public Plot topPlot = new Plot();

    /** the bottom plot */
    public Plot bottomPlot = new Plot();

    /** the middle plot */
    public Plot middlePlot = new Plot();

    /** Default constructor.
     */
    public ThreePlots() {
        setSize(560, 700);
        topPlot.setSize(350, 300);
        topPlot.setButtons(true);
        middlePlot.setSize(350, 300);
        middlePlot.setButtons(true);
        bottomPlot.setSize(350, 300);
        bottomPlot.setButtons(true);
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        getContentPane().setLayout(gridbag);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        gridbag.setConstraints(topPlot, c);
        getContentPane().add(topPlot);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        gridbag.setConstraints(middlePlot, c);
        getContentPane().add(middlePlot);
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        gridbag.setConstraints(bottomPlot, c);
        getContentPane().add(bottomPlot);
    }
}

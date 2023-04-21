package org.poker.prophecy.simulation.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.poker.prophecy.ImagesManager;
import org.poker.prophecy.PropertiesManager;
import org.poker.prophecy.simulation.Table;

/**
 * Stellt den Ticker zu einem Tisch in einem Fenster dar.<br>
 * Die Meldungen in dem Fenster kommen nur aus dem Ticker zum besagten Tisch.
 * @author bg
 */
public class TableTicker extends JFrame {

    private int frameMinimumWidth = 640;

    private int frameMinimumHeight = 480;

    private JTextArea tickerArea = null;

    public TableTicker(Table table) {
        super("The Prophecy (v." + PropertiesManager.getVersionString() + ") - View on Table \"" + table.getName() + "\"");
        this.setIconImage(ImagesManager.getImage("rss_feed_16x16.jpg"));
        this.setSize(new Dimension(frameMinimumWidth, frameMinimumHeight));
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(ImagesManager.POKERGREEN);
        this.getContentPane().setLayout(new java.awt.FlowLayout());
        this.setAlwaysOnTop(true);
        tickerArea = new JTextArea();
        tickerArea.setFont(new Font("Courier", Font.PLAIN, 14));
        tickerArea.setBackground(ImagesManager.POKERGREEN);
        tickerArea.setOpaque(true);
        tickerArea.setBackground(new Color(246, 246, 230));
        JScrollPane tickerAreaScrollPane = new JScrollPane(tickerArea);
        Dimension tickerAreaScrollPaneDimension = new Dimension(frameMinimumWidth - 10, frameMinimumHeight - 10);
        tickerAreaScrollPane.setMinimumSize(tickerAreaScrollPaneDimension);
        tickerAreaScrollPane.setPreferredSize(tickerAreaScrollPaneDimension);
        this.getContentPane().add(tickerAreaScrollPane);
    }

    public JTextArea getTickerArea() {
        return tickerArea;
    }
}

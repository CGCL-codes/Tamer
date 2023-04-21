package jebl.evolution.align.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 *
 * @author Alexei Drummond
 *
 * @version $Id: TracebackPanel.java 185 2006-01-23 23:03:18Z rambaut $
 */
public class TracebackPanel extends JPanel {

    public TracebackPanel(final TracebackPlot plot) {
        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.add(Box.createGlue());
        panel.add(new JButton(new AbstractAction("Clear plot") {

            public void actionPerformed(ActionEvent e) {
                plot.clear();
            }
        }));
        add(plot, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);
    }
}

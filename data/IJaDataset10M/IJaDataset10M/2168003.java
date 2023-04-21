package net.sf.doolin.jbars;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SimpleBarsApp {

    public static void main(String[] args) {
        BarsModel<Object> model = new SampleBarsModel();
        JBars<Object> bars = new JBars<Object>();
        bars.setBackground(Color.BLACK);
        bars.setForeground(Color.WHITE);
        bars.setModel(model);
        JPanel content = new JPanel(new BorderLayout());
        content.add(bars, BorderLayout.CENTER);
        JFrame frame = new JFrame("Simple Bars");
        frame.setContentPane(content);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}

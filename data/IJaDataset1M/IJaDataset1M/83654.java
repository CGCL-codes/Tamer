package net.sf.doolin.jbars;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import net.sf.doolin.jbars.renderer.DefaultBarRenderer;

public class ColoredBarsApp {

    public static void main(String[] args) {
        BarsModel<Object> model = new SampleBarsModel();
        JBars<Object> bars = new JBars<Object>();
        bars.setBackground(Color.BLACK);
        bars.setForeground(Color.WHITE);
        bars.setModel(model);
        bars.setRenderer(new DefaultBarRenderer<Object>() {

            @Override
            public Color getBarColor(JBars<Object> bars, BarsModel<Object> model, int index) {
                float[] rgb = { 0, 0, 0 };
                float value = model.getBarValue(index).floatValue();
                value = 0.5f * (1f + value);
                rgb[index] = value;
                return new Color(rgb[0], rgb[1], rgb[2]);
            }
        });
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

package org.piccolo2d.examples;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import org.piccolo2d.extras.pswing.PSwing;
import org.piccolo2d.extras.pswing.PSwingCanvas;
import org.piccolo2d.extras.swing.PScrollPane;

/**
 * Tests a set of Sliders and Checkboxes in panels.
 * 
 * @author Martin Clifford
 */
public class SliderExample extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final PSwingCanvas canvas;

    private final PScrollPane scrollPane;

    private final JTabbedPane tabbedPane;

    private final PSwing swing;

    public SliderExample() {
        final JPanel mainPanel = new JPanel(false);
        tabbedPane = new JTabbedPane();
        tabbedPane.setPreferredSize(new Dimension(700, 700));
        mainPanel.add(tabbedPane);
        getContentPane().add(mainPanel);
        canvas = new PSwingCanvas();
        canvas.setPreferredSize(new Dimension(700, 700));
        scrollPane = new PScrollPane(canvas);
        tabbedPane.add("Tab 1", scrollPane);
        final JPanel tabPanel = new JPanel(false);
        tabPanel.setLayout(null);
        tabPanel.setPreferredSize(new Dimension(700, 700));
        JPanel panel;
        panel = createNestedPanel();
        panel.setSize(new Dimension(250, 250));
        panel.setLocation(0, 0);
        tabPanel.add(panel);
        panel = createNestedPanel();
        panel.setSize(new Dimension(250, 250));
        panel.setLocation(0, 350);
        tabPanel.add(panel);
        panel = createNestedPanel();
        panel.setSize(new Dimension(250, 250));
        panel.setLocation(350, 0);
        tabPanel.add(panel);
        panel = createNestedPanel();
        panel.setSize(new Dimension(250, 250));
        panel.setLocation(350, 350);
        tabPanel.add(panel);
        final JButton buttonPreset = new JButton("Zoom = 100%");
        buttonPreset.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                canvas.getCamera().setViewScale(1.0);
                canvas.getCamera().setViewOffset(0, 0);
            }
        });
        buttonPreset.setSize(new Dimension(120, 25));
        buttonPreset.setLocation(240, 285);
        tabPanel.add(buttonPreset);
        swing = new PSwing(tabPanel);
        swing.translate(0, 0);
        canvas.getLayer().addChild(swing);
        canvas.setPanEventHandler(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Slider Example");
        setResizable(true);
        setBackground(null);
        pack();
        setVisible(true);
    }

    private JPanel createNestedPanel() {
        JPanel panel;
        JLabel label;
        panel = new JPanel(false);
        panel.setLayout(new BorderLayout());
        label = new JLabel("A Panel within a panel");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.white);
        final JLabel label2 = new JLabel("A Panel within a panel");
        label2.setHorizontalAlignment(SwingConstants.CENTER);
        final JSlider slider = new JSlider();
        final JCheckBox cbox1 = new JCheckBox("Checkbox 1");
        final JCheckBox cbox2 = new JCheckBox("Checkbox 2");
        final JPanel panel3 = new JPanel(false);
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.PAGE_AXIS));
        panel3.setBorder(new EmptyBorder(3, 3, 3, 3));
        panel3.add(label2);
        panel3.add(slider);
        panel3.add(cbox1);
        panel3.add(cbox2);
        final JPanel panel2 = new JPanel(false);
        panel2.setBackground(Color.blue);
        panel.setBorder(new EmptyBorder(1, 1, 1, 1));
        panel2.add(label);
        panel2.add(panel3);
        panel.setBackground(Color.red);
        panel.setSize(new Dimension(250, 250));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.add(panel2, "Center");
        panel.revalidate();
        return panel;
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                new SliderExample();
            }
        });
    }
}

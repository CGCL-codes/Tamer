package test.samples.lafwidget.clientprop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.BusinessBlackSteelSkin;

/**
 * Test application that shows the use of the {@link LafWidget#AUTO_SCROLL}
 * client property.
 * 
 * @author Kirill Grouchnikov
 * @see LafWidget#AUTO_SCROLL
 */
public class AutoScroll extends JFrame {

    /**
	 * Creates the main frame for <code>this</code> sample.
	 */
    public AutoScroll() {
        super("Auto scroll");
        this.setLayout(new BorderLayout());
        JPanel samplePanel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D graphics = (Graphics2D) g.create();
                graphics.setPaint(new GradientPaint(0, 0, new Color(100, 100, 255), getWidth(), getHeight(), new Color(255, 100, 100)));
                graphics.fillRect(0, 0, getWidth(), getHeight());
                graphics.dispose();
            }
        };
        samplePanel.setPreferredSize(new Dimension(800, 400));
        samplePanel.setSize(this.getPreferredSize());
        samplePanel.setMinimumSize(this.getPreferredSize());
        final JScrollPane scrollPane = new JScrollPane(samplePanel);
        this.add(scrollPane, BorderLayout.CENTER);
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        final JCheckBox hasAutoScroll = new JCheckBox("has auto scroll");
        hasAutoScroll.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        scrollPane.putClientProperty(LafWidget.AUTO_SCROLL, hasAutoScroll.isSelected() ? Boolean.TRUE : null);
                        repaint();
                    }
                });
            }
        });
        controls.add(hasAutoScroll);
        this.add(controls, BorderLayout.SOUTH);
        this.setSize(400, 200);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
	 * The main method for <code>this</code> sample. The arguments are ignored.
	 * 
	 * @param args
	 *            Ignored.
	 */
    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                SubstanceLookAndFeel.setSkin(new BusinessBlackSteelSkin());
                new AutoScroll().setVisible(true);
            }
        });
    }
}

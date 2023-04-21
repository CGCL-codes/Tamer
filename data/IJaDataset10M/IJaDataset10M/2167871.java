package org.ttalbott.mytelly;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 *
 * @author  Tom Talbott
 * @version 
 */
public class SplashScreen extends JWindow {

    JLabel jLabelText;

    JLabel jLabelBackground;

    /** Creates new SplashScreen */
    public SplashScreen(Frame f, final String VERSION) {
        super(f);
        getContentPane().setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints1;
        jLabelText = new javax.swing.JLabel();
        jLabelBackground = new javax.swing.JLabel();
        jLabelText.setForeground(java.awt.Color.black);
        jLabelText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelText.setFont(new java.awt.Font("Arial", 0, 12));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(230, 0, 0, 0);
        getContentPane().add(jLabelText, gridBagConstraints1);
        URL resource = getClass().getResource("splash.jpg");
        jLabelBackground.setIcon(new javax.swing.ImageIcon(resource) {

            public void paintIcon(Component c, Graphics g, int x, int y) {
                super.paintIcon(c, g, x, y);
                Color saveColor = g.getColor();
                Font saveFont = g.getFont();
                g.setFont(new Font(saveFont.getName(), saveFont.getStyle(), 14));
                FontMetrics fm = g.getFontMetrics();
                int verOffset = fm.stringWidth(VERSION) / 2;
                g.setColor(Color.ORANGE);
                g.drawString(VERSION, c.getWidth() / 2 - verOffset, c.getHeight() / 5);
                g.setColor(saveColor);
                g.setFont(saveFont);
            }
        });
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.gridheight = 2;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 0);
        getContentPane().add(jLabelBackground, gridBagConstraints1);
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension labelSize = jLabelBackground.getPreferredSize();
        setLocation(screenSize.width / 2 - (labelSize.width / 2), screenSize.height / 2 - (labelSize.height / 2));
        setVisible(true);
        screenSize = null;
        labelSize = null;
    }

    public void setText(String inText) {
        final String text = inText;
        Runnable setTextFieldText = new Runnable() {

            public void run() {
                jLabelText.setText(text);
                jLabelText.repaint();
            }
        };
        try {
            SwingUtilities.invokeAndWait(setTextFieldText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        setVisible(false);
        dispose();
    }
}

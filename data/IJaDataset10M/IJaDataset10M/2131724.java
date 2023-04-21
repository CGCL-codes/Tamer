package netblend.master;

import static netblend.NetBlendSystem.VERSION;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * A simple cover panel to introduce the master application interface.
 * 
 * @author Ian Thompson
 * 
 */
@SuppressWarnings("serial")
public class AboutPanel extends TabPanel {

    public static final String ABOUT_TEXT = "<html>NetBlend v" + VERSION + ",<br>Copyright (C) 2006 Ian P. Thompson<br><br>" + "NetBlend comes with ABSOLUTELY NO WARRANTY;<br>" + "for details see the License tab below.<br><br>" + "This is free software, and you are welcome to<br>" + "redistribute it under certain conditions;<br>" + "see the License tab below for details.";

    private Color background = new Color(0, 0, 0, 128);

    private Color foreground = new Color(192, 192, 192);

    private Insets insets = new Insets(8, 24, 8, 24);

    /**
	 * Creates a new <code>CoverPanel</code> to welcome the user.
	 */
    public AboutPanel() {
        super();
        this.setLayout(new GridBagLayout());
        JPanel panel = new JPanel();
        panel.setBorder(new CompoundBorder(new LineBorder(foreground), new EmptyBorder(insets)));
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        panel.setBackground(background);
        JLabel titleLabel = new JLabel("NetBlend Master");
        titleLabel.setFont(new Font(null, 0, 32));
        titleLabel.setForeground(foreground);
        JLabel text = new JLabel(ABOUT_TEXT);
        text.setFocusable(false);
        text.setForeground(foreground);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.ipady = 32;
        layout.setConstraints(titleLabel, constraints);
        panel.add(titleLabel);
        constraints = new GridBagConstraints();
        constraints.gridy = 1;
        constraints.ipady = 32;
        layout.setConstraints(text, constraints);
        panel.add(text);
        this.add(panel, new GridBagConstraints(0, 0, 1, 1, 1, 0.2, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth(), h = getHeight();
        g.setColor(new Color(64, 64, 64));
        g.fillRect(0, 0, w, h);
        g.setColor(new Color(96, 96, 96));
        int m = w > h ? w : h;
        int i = 0;
        for (int xy = 0; xy < m; xy += 32) {
            g.drawLine(xy, 0, xy - 32 + i, h);
            g.drawLine(0, xy, w, xy - 32 + i);
        }
    }
}

package vaspgui.poscarFiles;

import vaspgui.poscarFiles.PoscarGuided;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import vaspgui.Mymath;

/**
 * This panel is displayed when the 2D Viewer button is pressed under the
 * Poscar tab.  It displays a two-dimensional view of whatever is currently
 * in the guided view.
 * @author Tim Hecht
 *
 */
public class PoscarView2d extends JPanel {

    /**
	 * This is the UID.
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * This panel will actually visualize the system.
	 */
    private JPanel viewPanel;

    /**
	 * This panel will provide tools to manipulate the view of the system.
	 */
    private JPanel toolbox;

    /**
	 * Button to set view to XY mode.
	 */
    private JButton xyButton;

    /**
	 * Button to set view to XZ mode.
	 */
    private JButton xzButton;

    /**
	 * Button to set view to YZ mode.
	 */
    private JButton yzButton;

    /**
	 * True if XY mode, false otherwise.
	 */
    private boolean xy;

    /**
	 * True if XZ mode, false otherwise.
	 */
    private boolean xz;

    /**
	 * True if YZ mode, false otherwise.
	 */
    private boolean yz;

    /**
	 * Sets up the view panel and toolbox and locates them onscreen.
	 */
    public PoscarView2d() {
        final int black1 = 0;
        final int black2 = 0;
        final int black3 = 0;
        final int white1 = 255;
        final int white2 = 255;
        final int white3 = 255;
        final int toolboxWidth = 50;
        xy = true;
        xz = false;
        yz = false;
        setLayout(new GridBagLayout());
        GridBagConstraints v = new GridBagConstraints();
        GridBagConstraints t = new GridBagConstraints();
        v.fill = GridBagConstraints.BOTH;
        v.gridwidth = 1;
        v.gridx = 0;
        v.weightx = 1.0;
        t.weighty = 1;
        t.fill = GridBagConstraints.BOTH;
        t.gridwidth = 1;
        t.gridx = 1;
        t.ipadx = toolboxWidth;
        t.weightx = 0;
        t.weighty = 1;
        viewPanel = new ViewPanel();
        viewPanel.setBackground(new Color(black1, black2, black3));
        toolbox = new JPanel();
        xyButton = new JButton("XY View");
        xzButton = new JButton("XZ View");
        yzButton = new JButton("YZ View");
        xyButton.addActionListener(new ButtonListener());
        xzButton.addActionListener(new ButtonListener());
        yzButton.addActionListener(new ButtonListener());
        toolbox.setBackground(new Color(white1, white2, white3));
        toolbox.add(xyButton);
        toolbox.add(xzButton);
        toolbox.add(yzButton);
        add(viewPanel, v);
        add(toolbox, t);
        repaint();
    }

    /**
	 * This is an extension of JPanel that displays the view screen.
	 * @author Tim Hecht
	 *
	 */
    class ViewPanel extends JPanel {

        /**
		 *
		 */
        private static final long serialVersionUID = 1L;

        /**
		 * Constructs the View Panel for the 2D viewer.
		 */
        ViewPanel() {
        }

        /**
		 * Called with repaint, and updates the view panel.
		 * This calls the getPos function in PoscarGuided, and redraws the
		 * positions every time that redraw gets called.
		 * @param g Graphics component that is redrawn.
		 */
        public void paintComponent(final Graphics g) {
            super.paintComponent(g);
            ArrayList<Double> pos = PoscarGuided.getPos();
            if (pos == null) {
                return;
            }
            ArrayList<Double> posx = new ArrayList<Double>();
            ArrayList<Double> posy = new ArrayList<Double>();
            ArrayList<Double> posz = new ArrayList<Double>();
            for (int i = 0; i < pos.size(); i++) {
                posx.add(pos.get(i));
                i++;
                posy.add(pos.get(i));
                i++;
                posz.add(pos.get(i));
            }
            double xmin = Mymath.min(posx);
            double xmax = Mymath.max(posx);
            double ymin = Mymath.min(posy);
            double ymax = Mymath.max(posy);
            double zmin = Mymath.min(posz);
            double zmax = Mymath.max(posz);
            final double spacing = 0.1;
            List<Integer> screenx = new ArrayList<Integer>();
            List<Integer> screeny = new ArrayList<Integer>();
            if (xy) {
                for (int i = 0; i < posx.size(); i++) {
                    screenx.add(Mymath.scale(xmin, xmax, spacing, viewPanel.getWidth(), posx.get(i)));
                    screeny.add(Mymath.scale(ymin, ymax, spacing, viewPanel.getHeight(), posy.get(i)));
                }
            } else if (xz) {
                for (int i = 0; i < posx.size(); i++) {
                    screenx.add(Mymath.scale(xmin, xmax, spacing, viewPanel.getWidth(), posx.get(i)));
                    screeny.add(Mymath.scale(zmin, zmax, spacing, viewPanel.getHeight(), posz.get(i)));
                }
            } else if (yz) {
                for (int i = 0; i < posx.size(); i++) {
                    screenx.add(Mymath.scale(ymin, ymax, spacing, viewPanel.getWidth(), posy.get(i)));
                    screeny.add(Mymath.scale(zmin, zmax, spacing, viewPanel.getHeight(), posz.get(i)));
                }
            }
            final int red = 255;
            final int radius = 15;
            for (int i = 0; i < screenx.size(); i++) {
                g.setColor(new Color(red, 0, 0));
                g.fillOval(screenx.get(i), screeny.get(i), radius, radius);
            }
        }
    }

    /**
	 * ActionListener implementation for the 2D viewer.
	 * @author Tim Hecht
	 *
	 */
    class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (e.getSource().equals(xyButton)) {
                xy = true;
                xz = false;
                yz = false;
                repaint();
            }
            if (e.getSource().equals(xzButton)) {
                xy = false;
                xz = true;
                yz = false;
                repaint();
            }
            if (e.getSource().equals(yzButton)) {
                xy = false;
                xz = false;
                yz = true;
                repaint();
            }
        }
    }
}

package edu.washington.assist.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import edu.washington.assist.gui.stateview.ActivityConstants;
import edu.washington.assist.gui.stateview.DefaultActivityDisplayPreferences;
import edu.washington.assist.gui.stateview.DisplayPreferences;
import edu.washington.assist.report.Activity;

@SuppressWarnings("serial")
public class LegendPanel extends JPanel {

    private static final Dimension ICON_SIZE = new Dimension(21, 21);

    protected static final int ICON_HEIGHT = 10;

    protected static final int ICON_WIDTH = 10;

    public static final int LEGEND_WIDTH = 2;

    public static final Font LEGEND_FONT = new Font("Default", Font.PLAIN, 9);

    public LegendPanel(Map<String, Paint> entries) {
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border titled = BorderFactory.createTitledBorder(loweredetched, "Legend");
        Border emptySpace = BorderFactory.createEmptyBorder(5, 10, 10, 10);
        Border cmpd = BorderFactory.createCompoundBorder(emptySpace, titled);
        emptySpace = BorderFactory.createEmptyBorder(5, 10, 5, 10);
        cmpd = BorderFactory.createCompoundBorder(cmpd, emptySpace);
        this.setBorder(cmpd);
        GridLayout grid = new GridLayout(0, LEGEND_WIDTH);
        grid.setVgap(5);
        grid.setHgap(5);
        this.setLayout(grid);
        this.invalidate();
        for (String str : entries.keySet()) {
            Paint paint = entries.get(str);
            this.add(makeLegendEntry(str.toUpperCase() + "_false.jpg", paint));
            this.add(makeLegendEntry(str.toUpperCase() + "_true.jpg", paint));
        }
        this.add(makeLegendEntry("CONVERSATION_.gif", this.getBackground()));
        this.revalidate();
    }

    public LegendPanel(ArrayList<Activity> entries) {
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border titled = BorderFactory.createTitledBorder(loweredetched, "Legend");
        Border emptySpace = BorderFactory.createEmptyBorder(5, 10, 10, 10);
        Border cmpd = BorderFactory.createCompoundBorder(emptySpace, titled);
        emptySpace = BorderFactory.createEmptyBorder(5, 10, 5, 5);
        cmpd = BorderFactory.createCompoundBorder(cmpd, emptySpace);
        this.setBorder(cmpd);
        GridLayout grid = new GridLayout(0, LEGEND_WIDTH);
        grid.setVgap(5);
        grid.setHgap(5);
        this.setLayout(grid);
        DisplayPreferences<Activity> displayPrefs = new DefaultActivityDisplayPreferences();
        this.invalidate();
        for (Activity act : entries) {
            if (act.isIndoor()) {
                String file = ActivityConstants.getCanonicalName(new Activity(act.getMotionState()));
                file = file.toUpperCase() + "_true.jpg";
                this.add(makeLegendEntry(file, displayPrefs.getPaint(act)));
            } else {
                this.add(makeLegendEntry(ActivityConstants.getCanonicalName(act).toUpperCase() + "_false.jpg", displayPrefs.getPaint(act)));
            }
        }
        this.add(makeLegendEntry("CONVERSATION_.gif", this.getBackground()));
        this.revalidate();
    }

    private static Component makeLegendEntry(String name, Paint paint) {
        assert (paint != null);
        DumbBox icon = new DumbBox(name, paint);
        JLabel nameLabel = new JLabel(name.substring(0, name.indexOf("_")));
        JLabel locLabel = null;
        if (name.contains("true")) {
            locLabel = new JLabel("INDOOR");
        }
        if (name.contains("false")) {
            locLabel = new JLabel("OUTDOOR");
        }
        nameLabel.setFont(LEGEND_FONT);
        if (locLabel != null) {
            locLabel.setFont(LEGEND_FONT);
        }
        JPanel panel = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 0, 0);
        panel.setLayout(gridbag);
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1;
        c.gridx = 1;
        c.gridy = 0;
        panel.add(nameLabel, c);
        if (locLabel != null) {
            c.gridx = 1;
            c.gridy = 1;
            panel.add(locLabel, c);
        }
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(0, 0, 0, 5);
        c.gridx = 0;
        if (locLabel != null) {
            c.gridheight = 2;
        }
        c.gridy = 0;
        c.weightx = 0;
        panel.add(icon, c);
        return panel;
    }

    public static void main(String[] args) {
        ArrayList<Activity> list = new ArrayList<Activity>();
        list.add(Activity.BASIC_ACTIVITIES[0]);
        list.add(Activity.BASIC_ACTIVITIES[1]);
        LegendPanel p = new LegendPanel(list);
        JFrame f = new JFrame();
        GridLayout grid = new GridLayout(0, 2);
        f.getContentPane().setLayout(grid);
        f.getContentPane().add(p);
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    private static class DumbBox extends JPanel {

        private Paint paint;

        Image icon;

        public DumbBox(String name, Paint paint) {
            this.paint = paint;
            Toolkit t = Toolkit.getDefaultToolkit();
            try {
                icon = t.getImage(this.getClass().getResource("/icon/" + name));
                icon = icon.getScaledInstance((int) ICON_SIZE.getWidth() - 6, (int) ICON_SIZE.getHeight() - 6, Image.SCALE_SMOOTH);
            } catch (Exception e) {
                System.err.println("Could not load icon: " + name);
            }
            this.setMinimumSize(ICON_SIZE);
            this.setPreferredSize(ICON_SIZE);
        }

        @Override
        public void paint(Graphics gg) {
            Graphics2D g = (Graphics2D) gg;
            g.setPaint(paint);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.drawImage(icon, 3, 3, null);
        }
    }
}

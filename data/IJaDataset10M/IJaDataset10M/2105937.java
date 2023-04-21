package com.griddev.d2.demo;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;

/**
 * GeneralPath star shape rendered with BasicStroke choices.
 */
public class LineTest extends Applet implements AdjustmentListener, ItemListener, ActionListener {

    static final int WIDTH = 0;

    static final int SKEW = 1;

    static final int SCALE = 2;

    static final int ROTATE = 3;

    static String labels[] = { "Line width:", "Skew factor:", "Scale:", "Rotation:" };

    static final int MIN = 0;

    static final int DEF = 1;

    static final int MAX = 2;

    static double limits[][] = { { 0.0, 4.0, 20.0 }, { -2.0, 0.0, 2.0 }, { 0.01, 0.8, 3.0 }, { -Math.PI, 0.0, Math.PI } };

    static String OpLabels[] = { "Stroke", "NZ Fill", "EO Fill" };

    static int Caps[] = { BasicStroke.CAP_BUTT, BasicStroke.CAP_ROUND, BasicStroke.CAP_SQUARE };

    static String CapsLabels[] = { "None", "Round", "Square" };

    static int Join[] = { BasicStroke.JOIN_ROUND, BasicStroke.JOIN_MITER, BasicStroke.JOIN_BEVEL };

    static String JoinLabels[] = { "Round", "Miter", "Bevel" };

    Button resetbutton;

    Checkbox doublebuftoggle;

    Checkbox antialiastoggle;

    Choice renderchoice;

    Choice capschoice;

    Choice joinchoice;

    TextField fields[] = new TextField[labels.length];

    Scrollbar sliders[] = new Scrollbar[labels.length];

    BezierCanvas canvas;

    public void init() {
        setLayout(new BorderLayout());
        setBackground(Color.lightGray);
        add("Center", canvas = new BezierCanvas(this));
        Panel p = new Panel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 0;
        doublebuftoggle = new Checkbox("Double Buffer", false);
        doublebuftoggle.addItemListener(this);
        gridbag.setConstraints(doublebuftoggle, c);
        p.add(doublebuftoggle);
        c.gridx++;
        antialiastoggle = new Checkbox("Antialias", false);
        antialiastoggle.addItemListener(this);
        gridbag.setConstraints(antialiastoggle, c);
        p.add(antialiastoggle);
        c.gridx = 0;
        c.gridy++;
        p.setLayout(gridbag);
        resetbutton = new Button("Reset");
        resetbutton.addActionListener(this);
        gridbag.setConstraints(resetbutton, c);
        p.add(resetbutton);
        c.gridy++;
        renderchoice = makeChoice("Operation:", p, gridbag, c, OpLabels);
        capschoice = makeChoice("Dash Ends:", p, gridbag, c, CapsLabels);
        joinchoice = makeChoice("Line Joins: ", p, gridbag, c, JoinLabels);
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        for (int i = 0; i < fields.length; i++) {
            Panel p2 = new Panel();
            GridBagLayout gb2 = new GridBagLayout();
            GridBagConstraints c2 = new GridBagConstraints();
            c2.gridx = 0;
            c2.gridy = 0;
            c2.anchor = GridBagConstraints.WEST;
            c2.weightx = 1.0;
            p2.setLayout(gb2);
            Label label = new Label(labels[i]);
            gb2.setConstraints(label, c2);
            p2.add(label);
            c2.gridx = 1;
            c2.anchor = GridBagConstraints.EAST;
            fields[i] = new TextField("", 6);
            gb2.setConstraints(fields[i], c2);
            p2.add(fields[i]);
            c2.gridx = 0;
            c2.gridy = 1;
            c2.gridwidth = 2;
            c2.fill = GridBagConstraints.HORIZONTAL;
            sliders[i] = new Scrollbar(Scrollbar.HORIZONTAL, 0, 20, 0, 120);
            sliders[i].addAdjustmentListener(this);
            gb2.setConstraints(sliders[i], c2);
            p2.add(sliders[i]);
            c.gridy++;
            gridbag.setConstraints(p2, c);
            p.add(p2);
        }
        add("West", p);
        resetValues();
    }

    Choice makeChoice(String labelstr, Panel p, GridBagLayout gridbag, GridBagConstraints c, String[] items) {
        Label label = new Label(labelstr);
        gridbag.setConstraints(label, c);
        p.add(label);
        c.gridx++;
        Choice choice = new Choice();
        choice.addItemListener(this);
        for (int i = 0; i < items.length; i++) {
            choice.add(items[i]);
        }
        gridbag.setConstraints(choice, c);
        p.add(choice);
        c.gridx = 0;
        c.gridy++;
        return choice;
    }

    public void resetValues() {
        renderchoice.select(0);
        canvas.stroke = 1;
        capschoice.select(0);
        canvas.caps = Caps[0];
        joinchoice.select(0);
        canvas.join = Join[0];
        for (int i = 0; i < limits.length; i++) {
            double def = limits[i][DEF];
            fields[i].setText(roundstr(def));
            sliders[i].setValue(sliderset(i, def));
            handleValue(i, def);
        }
        canvas.repaint();
    }

    public void handleValue(int index, double val) {
        switch(index) {
            case WIDTH:
                canvas.penwidth = val;
                break;
            case SKEW:
                canvas.skew = val;
                break;
            case SCALE:
                canvas.scale = val;
                break;
            case ROTATE:
                canvas.rotate = val;
                break;
        }
    }

    public String roundstr(double value) {
        return Double.toString(Math.round(value * 100.0) / 100.0);
    }

    public int sliderset(int fieldindex, double val) {
        return (int) (100.0 * ((val - limits[fieldindex][MIN]) / (limits[fieldindex][MAX] - limits[fieldindex][MIN])));
    }

    public double fieldval(int fieldindex) {
        TextField t = fields[fieldindex];
        String val = t.getText();
        double ret;
        try {
            ret = Double.valueOf(val).doubleValue();
        } catch (NumberFormatException e) {
            ret = limits[fieldindex][DEF];
        }
        ret = Math.max(limits[fieldindex][MIN], Math.min(limits[fieldindex][MAX], ret));
        t.setText(roundstr(ret));
        sliders[fieldindex].setValue(sliderset(fieldindex, ret));
        return ret;
    }

    public double sliderval(int fieldindex) {
        Scrollbar s = sliders[fieldindex];
        int val = s.getValue();
        double ret = (limits[fieldindex][MIN] + val / 100.0 * (limits[fieldindex][MAX] - limits[fieldindex][MIN]));
        fields[fieldindex].setText(roundstr(ret));
        return ret;
    }

    public void adjustmentValueChanged(AdjustmentEvent e) {
        Object target = e.getSource();
        for (int i = 0; i < sliders.length; i++) {
            if (target == sliders[i]) {
                handleValue(i, sliderval(i));
                canvas.repaint();
                break;
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        resetValues();
    }

    public void itemStateChanged(ItemEvent e) {
        e.getStateChange();
        if (e.getSource().equals(renderchoice)) {
            int sel = renderchoice.getSelectedIndex();
            canvas.stroke = (sel == 0 ? 1 : (sel == 3 ? 2 : 0));
            canvas.eofill = (sel == 2 ? GeneralPath.WIND_EVEN_ODD : GeneralPath.WIND_NON_ZERO);
        } else if (e.getSource().equals(capschoice)) {
            int sel = capschoice.getSelectedIndex();
            canvas.caps = Caps[sel];
        } else if (e.getSource().equals(joinchoice)) {
            int sel = joinchoice.getSelectedIndex();
            canvas.join = Join[sel];
        } else if (e.getSource().equals(antialiastoggle)) {
            canvas.antialias = antialiastoggle.getState();
        } else if (e.getSource().equals(doublebuftoggle)) {
            canvas.doublebuf = doublebuftoggle.getState();
        }
        canvas.repaint();
    }

    public static void main(String argv[]) {
        Frame f = new Frame("Java 2D Demo - LineTest");
        final LineTest demo = new LineTest();
        f.add(demo);
        f.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        f.pack();
        f.setSize(new Dimension(500, 400));
        demo.init();
        f.show();
    }
}

class BezierCanvas extends Canvas {

    LineTest applet;

    double scale;

    double rotate;

    double skew;

    double penwidth;

    int stroke;

    boolean antialias;

    boolean doublebuf;

    int eofill = GeneralPath.WIND_EVEN_ODD;

    int caps;

    int join;

    public BezierCanvas(LineTest app) {
        applet = app;
        setBackground(Color.lightGray);
    }

    public Dimension getPreferredSize() {
        return new Dimension(300, 300);
    }

    BufferedImage img;

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        Graphics2D g2;
        int width = getSize().width;
        int height = getSize().height;
        if (doublebuf) {
            boolean newimg = (img == null || img.getWidth() != width || img.getHeight() != height);
            if (newimg) {
                img = (BufferedImage) createImage(width, height);
            }
            g2 = img.createGraphics();
        } else {
            img = null;
            g2 = (Graphics2D) g;
        }
        g2.clearRect(0, 0, width, height);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, (antialias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF));
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, (antialias ? RenderingHints.VALUE_RENDER_QUALITY : RenderingHints.VALUE_RENDER_SPEED));
        g2.rotate(rotate);
        g2.translate(width / 2.0, height / 2.0);
        g2.scale(scale, scale);
        g2.shear(-skew, 0.0);
        GeneralPath p = new GeneralPath(eofill);
        p.moveTo(-width / 2.0f, -height / 8.0f);
        p.lineTo(+width / 2.0f, -height / 8.0f);
        p.lineTo(-width / 4.0f, +height / 2.0f);
        p.lineTo(+0.0f, -height / 2.0f);
        p.lineTo(+width / 4.0f, +height / 2.0f);
        p.closePath();
        if (stroke != 0) {
            BasicStroke bs;
            if (caps == BasicStroke.CAP_BUTT) {
                bs = new BasicStroke((float) penwidth, caps, join, 10.0f);
            } else {
                float dash[] = { width / 10.0f };
                bs = new BasicStroke((float) penwidth, caps, join, 10.0f, dash, 0.0f);
            }
            if (stroke != 1) {
                g2.setStroke(new BasicStroke(antialias ? (float) (1 / scale) : 0, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.draw(bs.createStrokedShape(p));
            } else {
                g2.setStroke(bs);
                g2.draw(p);
            }
        } else {
            g2.fill(p);
        }
        g2.dispose();
        if (doublebuf) {
            g.drawImage(img, 0, 0, null);
        }
    }
}

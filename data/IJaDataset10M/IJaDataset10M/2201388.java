package org.chernovia.sims.ca.fishbowl;

import java.awt.Choice;
import java.awt.Color;
import org.chernovia.lib.sims.ca.CA_Applet;
import org.chernovia.lib.sims.ca.CA_Canvas;
import org.chernovia.lib.sims.ca.CA_Engine;
import org.chernovia.lib.sims.ca.fishbowl.FishBowl2D;
import acme.MainFrame;

public class FishDemoApp extends CA_Applet {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public static final int CHK_ANGLES = 0, CHK_TRIADS = 1, CHK_BREAK = 2, CHK_GRAV = 3;

    public static final boolean DEBUG = false;

    public static final int LSB_NUMFISH = 0, LSB_RADFISH = 1, LSB_FISHSPD = 2, LSB_FISHTAIL = 3, LSB_MAXROW = 4, LSB_MAXPOLY = 5;

    public Choice CB_SONIC, CB_ORCH, CB_INT, CB_REPULSE;

    public static void main(String[] args) {
        MIN_WIDTH = 800;
        MIN_HEIGHT = 640;
        new MainFrame(new FishDemoApp(), MIN_WIDTH + 20, MIN_HEIGHT + 40);
    }

    @Override
    public void init() {
        MIN_HEIGHT = 640;
        super.init();
    }

    @Override
    protected CA_Engine newEngine() {
        return new FishBowl2D(getLSBValue(LSB_NUMFISH), getLSBValue(LSB_RADFISH), getLSBValue(LSB_FISHSPD), getLSBValue(LSB_FISHTAIL));
    }

    @Override
    protected CA_Canvas newCanvas(int width, int height) {
        System.out.println("Height: " + height);
        return new Fish2DDemo(width, height, (FishBowl2D) getEngine());
    }

    @Override
    protected String[] getCHKLabels() {
        String[] labels = { "Angles", "Triads", "Repel", "Gravity" };
        return labels;
    }

    @Override
    protected String[] getLSBDefaults() {
        String[] settings = { "Number of Fish", "8 1 3 16 1", "Fish Rad", "16 1 8 25 1", "Speed", "24 1 4 65 1", "Tail Size", "32 1 1 65 1", "Tone Row Size", "36 1 4 65 1", "Polyphony", "1 1 1 16 1" };
        return settings;
    }

    @Override
    protected void addControls() {
        super.addControls(Color.LIGHT_GRAY);
        Color c = Color.CYAN;
        setFrames(5, false);
        setCHKState(CHK_BREAK, true);
        CB_SONIC = new Choice();
        for (int i = 0; i < P2D_Sonifyer.SONIC_OPT.length; i++) {
            CB_SONIC.add(P2D_Sonifyer.SONIC_OPT[i]);
        }
        CB_SONIC.addItemListener(this);
        CB_SONIC.setBackground(c);
        add(CB_SONIC);
        CB_ORCH = new Choice();
        for (int i = 0; i < P2D_Sonifyer.ORCH_OPT.length; i++) {
            CB_ORCH.add(P2D_Sonifyer.ORCH_OPT[i]);
        }
        CB_ORCH.addItemListener(this);
        CB_ORCH.select(P2D_Sonifyer.ORCH_ORCH);
        CB_ORCH.setBackground(c);
        add(CB_ORCH);
        CB_INT = new Choice();
        for (int i = 0; i < P2D_Sonifyer.INT_OPT.length; i++) {
            CB_INT.add(P2D_Sonifyer.INT_OPT[i]);
        }
        CB_INT.addItemListener(this);
        CB_INT.select(P2D_Sonifyer.INT_ALL);
        CB_INT.setBackground(c);
        add(CB_INT);
        CB_REPULSE = new Choice();
        for (int i = 0; i < FishBowl2D.RepulseStyles.length; i++) {
            CB_REPULSE.add(FishBowl2D.RepulseStyles[i]);
        }
        CB_REPULSE.addItemListener(this);
        CB_REPULSE.select(FishBowl2D.REP_DEFLECT);
        CB_REPULSE.setBackground(c);
        add(CB_REPULSE);
        setLastComponent(CB_REPULSE);
        setBackground(Color.BLUE);
    }

    @Override
    public void updateVars() {
        super.updateVars();
        if (isRunning()) {
            FishBowl2D bowl = (FishBowl2D) getEngine();
            Fish2DDemo fishcan = (Fish2DDemo) getCanvas();
            bowl.GRAVITY = getCHKState(CHK_GRAV);
            bowl.setRepulsion(CB_REPULSE.getSelectedIndex());
            fishcan.getSonifyer().ANGLES = getCHKState(CHK_ANGLES);
            fishcan.getSonifyer().TRIADS = getCHKState(CHK_TRIADS);
            fishcan.getSonifyer().BREAK_COLL = getCHKState(CHK_BREAK);
            fishcan.getSonifyer().setRowSize(getLSBValue(LSB_MAXROW));
            fishcan.getSonifyer().setPoly(getLSBValue(LSB_MAXPOLY));
            fishcan.getSonifyer().setOrchestra(CB_ORCH.getSelectedIndex());
            fishcan.getSonifyer().setStyle(CB_SONIC.getSelectedIndex());
            fishcan.getSonifyer().setIntervals(CB_INT.getSelectedIndex());
        }
    }
}

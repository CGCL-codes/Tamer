package potd;

import java.applet.Applet;
import java.awt.*;

public class ChessPrb extends Applet {

    static final Color BG_COLOR;

    static final String LOAD_MSG = "Loading program . . .";

    protected int appWidth;

    protected int appHeight;

    private boolean startup;

    private boolean ready;

    public String dataOfDay;

    public String getAppletInfo() {
        return "ChessPrb v2.0 - Daily Chess Problem applet - Copyright (c) 2001 Robert Kirkland";
    }

    public void init() {
        appWidth = 500;
        appHeight = 430;
    }

    public AppMain getAppMain() {
        return appmain;
    }

    AppMain appmain;

    private void initialize() {
        appmain = new AppMain(this);
        appmain.initialize();
    }

    public void paint(Graphics g) {
        drawLoadMsg(g);
        if (startup) {
            startup = false;
            initialize();
            return;
        }
        if (ready) clearScreen(g);
    }

    private void drawLoadMsg(Graphics g) {
        Font font = new Font("Dialog", 1, 12);
        byte byte0 = 20;
        byte byte1 = 10;
        FontMetrics fontmetrics = g.getFontMetrics(font);
        int i = fontmetrics.stringWidth("Loading program . . .") + 2 * byte0;
        int j = fontmetrics.getAscent() + fontmetrics.getDescent() + 2 * byte1;
        int k = (appWidth - i) / 2;
        int l = (appHeight - j) / 2;
        g.setColor(BG_COLOR);
        g.fillRect(0, 0, appWidth, appHeight);
        g.setColor(Color.black);
        g.fillRect(k, l, i + 1, j + 1);
        g.setColor(Color.red);
        g.drawRect(k, l, i, j);
        g.setFont(font);
        g.setColor(Color.white);
        g.drawString("Loading program . . .", k + byte0, l + byte1 + fontmetrics.getAscent());
    }

    private void clearScreen(Graphics g) {
        g.setColor(BG_COLOR);
        g.fillRect(0, 0, appWidth, appHeight);
    }

    void setReady() {
        ready = true;
    }

    public ChessPrb() {
        startup = true;
        ready = false;
    }

    static {
        BG_COLOR = Color.blue;
    }
}

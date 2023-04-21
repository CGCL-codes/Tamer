package fr.inria.zvtm.demo;

import java.awt.AlphaComposite;
import java.awt.Toolkit;
import java.awt.GraphicsEnvironment;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.GradientPaint;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.ImageIcon;
import java.util.Vector;
import fr.inria.zvtm.engine.VirtualSpaceManager;
import fr.inria.zvtm.engine.VirtualSpace;
import fr.inria.zvtm.engine.View;
import fr.inria.zvtm.engine.EView;
import fr.inria.zvtm.engine.ViewPanel;
import fr.inria.zvtm.engine.Camera;
import fr.inria.zvtm.engine.Utilities;
import fr.inria.zvtm.engine.SwingWorker;
import fr.inria.zvtm.glyphs.VText;
import fr.inria.zvtm.glyphs.VRectangle;
import fr.inria.zvtm.glyphs.VImage;
import fr.inria.zvtm.glyphs.Glyph;
import fr.inria.zvtm.engine.ViewEventHandler;
import fr.inria.zvtm.glyphs.RImage;

public class FITSViewer {

    static int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;

    static int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

    static int VIEW_MAX_W = 1024;

    static int VIEW_MAX_H = 768;

    int VIEW_W, VIEW_H;

    int VIEW_X, VIEW_Y;

    int panelWidth, panelHeight;

    VirtualSpaceManager vsm;

    VirtualSpace mSpace, aboutSpace;

    EView mView;

    MainEventHandler eh;

    NavigationManager nm;

    Overlay ovm;

    VWGlassPane gp;

    public FITSViewer(boolean fullscreen, boolean opengl, boolean antialiased) {
        initGUI(fullscreen, opengl, antialiased);
    }

    void initGUI(boolean fullscreen, boolean opengl, boolean antialiased) {
        windowLayout();
        vsm = VirtualSpaceManager.INSTANCE;
        ovm = new Overlay(this);
        nm = new NavigationManager(this);
        mSpace = vsm.addVirtualSpace(Messages.mSpaceName);
        Camera mCamera = mSpace.addCamera();
        nm.ovCamera = mSpace.addCamera();
        aboutSpace = vsm.addVirtualSpace(Messages.aboutSpaceName);
        aboutSpace.addCamera();
        Vector cameras = new Vector();
        cameras.add(mCamera);
        nm.setCamera(mCamera);
        cameras.add(aboutSpace.getCamera(0));
        mView = (EView) vsm.addFrameView(cameras, Messages.mViewName, (opengl) ? View.OPENGL_VIEW : View.STD_VIEW, VIEW_W, VIEW_H, false, false, !fullscreen, (!fullscreen) ? ConfigManager.initMenu(this) : null);
        if (fullscreen) {
            GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow((JFrame) mView.getFrame());
        } else {
            mView.setVisible(true);
        }
        updatePanelSize();
        ovm.init();
        gp = new VWGlassPane(this);
        ((JFrame) mView.getFrame()).setGlassPane(gp);
        eh = new MainEventHandler(this);
        mView.setEventHandler(eh, 0);
        mView.setEventHandler(ovm, 1);
        mView.setNotifyMouseMoved(true);
        mView.setAntialiasing(antialiased);
        mView.setBackgroundColor(ConfigManager.BACKGROUND_COLOR);
        mView.getPanel().addComponentListener(eh);
        ComponentAdapter ca0 = new ComponentAdapter() {

            public void componentResized(ComponentEvent e) {
                updatePanelSize();
            }
        };
        mView.getFrame().addComponentListener(ca0);
        nm.createOverview();
    }

    void windowLayout() {
        if (Utilities.osIsWindows()) {
            VIEW_X = VIEW_Y = 0;
        } else if (Utilities.osIsMacOS()) {
            VIEW_X = 80;
            SCREEN_WIDTH -= 80;
        }
        VIEW_W = (SCREEN_WIDTH <= VIEW_MAX_W) ? SCREEN_WIDTH : VIEW_MAX_W;
        VIEW_H = (SCREEN_HEIGHT <= VIEW_MAX_H) ? SCREEN_HEIGHT : VIEW_MAX_H;
    }

    void updatePanelSize() {
        Dimension d = mView.getPanel().getSize();
        panelWidth = d.width;
        panelHeight = d.height;
        nm.updateOverviewLocation();
    }

    void exit() {
        System.exit(0);
    }

    public static void main(String[] args) {
        boolean fs = false;
        boolean ogl = false;
        boolean aa = true;
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                if (args[i].substring(1).equals("fs")) {
                    fs = true;
                } else if (args[i].substring(1).equals("opengl")) {
                    System.setProperty("sun.java2d.opengl", "true");
                    ogl = true;
                } else if (args[i].substring(1).equals("noaa")) {
                    aa = false;
                } else if (args[i].substring(1).equals("h") || args[i].substring(1).equals("-help")) {
                    Messages.printCmdLineHelp();
                    System.exit(0);
                }
            }
        }
        if (!fs && Utilities.osIsMacOS()) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        }
        System.out.println(Messages.H_4_HELP);
        new FITSViewer(fs, ogl, aa);
    }
}

class VWGlassPane extends JComponent {

    static final int BAR_WIDTH = 200;

    static final int BAR_HEIGHT = 10;

    static final AlphaComposite GLASS_ALPHA = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.65f);

    static final Color MSG_COLOR = Color.DARK_GRAY;

    GradientPaint PROGRESS_GRADIENT = new GradientPaint(0, 0, Color.ORANGE, 0, BAR_HEIGHT, Color.BLUE);

    String msg = Messages.EMPTY_STRING;

    int msgX = 0;

    int msgY = 0;

    int completion = 0;

    int prX = 0;

    int prY = 0;

    int prW = 0;

    FITSViewer application;

    VWGlassPane(FITSViewer app) {
        super();
        this.application = app;
        addMouseListener(new MouseAdapter() {
        });
        addMouseMotionListener(new MouseMotionAdapter() {
        });
        addKeyListener(new KeyAdapter() {
        });
    }

    public void setValue(int c) {
        completion = c;
        prX = application.panelWidth / 2 - BAR_WIDTH / 2;
        prY = application.panelHeight / 2 - BAR_HEIGHT / 2;
        prW = (int) (BAR_WIDTH * ((float) completion) / 100.0f);
        PROGRESS_GRADIENT = new GradientPaint(0, prY, Color.LIGHT_GRAY, 0, prY + BAR_HEIGHT, Color.DARK_GRAY);
        repaint(prX, prY, BAR_WIDTH, BAR_HEIGHT);
    }

    public void setLabel(String m) {
        msg = m;
        msgX = application.panelWidth / 2 - BAR_WIDTH / 2;
        msgY = application.panelHeight / 2 - BAR_HEIGHT / 2 - 10;
        repaint(msgX, msgY - 50, 400, 70);
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Rectangle clip = g.getClipBounds();
        g2.setComposite(GLASS_ALPHA);
        g2.setColor(Color.WHITE);
        g2.fillRect(clip.x, clip.y, clip.width, clip.height);
        g2.setComposite(AlphaComposite.Src);
        if (msg != Messages.EMPTY_STRING && msg.length() > 0) {
            g2.setColor(MSG_COLOR);
            g2.setFont(ConfigManager.GLASSPANE_FONT);
            g2.drawString(msg, msgX, msgY);
        }
        g2.setPaint(PROGRESS_GRADIENT);
        g2.fillRect(prX, prY, prW, BAR_HEIGHT);
        g2.setColor(MSG_COLOR);
        g2.drawRect(prX, prY, BAR_WIDTH, BAR_HEIGHT);
    }
}

class Overlay implements ViewEventHandler {

    FITSViewer application;

    boolean showingAbout = false;

    VRectangle fadeAbout;

    VImage insituLogo, inriaLogo;

    VText[] aboutLines;

    VRectangle fadedRegion;

    VText sayGlyph;

    Overlay(FITSViewer app) {
        this.application = app;
    }

    void init() {
        fadedRegion = new VRectangle(0, 0, 0, 10, 10, ConfigManager.FADE_REGION_FILL, ConfigManager.FADE_REGION_STROKE, 0.85f);
        application.aboutSpace.addGlyph(fadedRegion);
        fadedRegion.setVisible(false);
        sayGlyph = new VText(0, -10, 0, ConfigManager.SAY_MSG_COLOR, Messages.EMPTY_STRING, VText.TEXT_ANCHOR_MIDDLE);
        sayGlyph.setSpecialFont(ConfigManager.SAY_MSG_FONT);
        application.aboutSpace.addGlyph(sayGlyph);
        sayGlyph.setVisible(false);
    }

    void showAbout() {
        if (!showingAbout) {
            fadeAbout = new VRectangle(0, 0, 0, Math.round(application.panelWidth / 2.1), Math.round(application.panelHeight / 3), ConfigManager.FADE_REGION_FILL, ConfigManager.FADE_REGION_STROKE, 0.85f);
            aboutLines = new VText[4];
            aboutLines[0] = new VText(0, 150, 0, Color.WHITE, Messages.APP_NAME, VText.TEXT_ANCHOR_MIDDLE, 4.0f);
            aboutLines[1] = new VText(0, 110, 0, Color.WHITE, Messages.V + Messages.VERSION, VText.TEXT_ANCHOR_MIDDLE, 2.0f);
            aboutLines[2] = new VText(0, 40, 0, Color.WHITE, Messages.AUTHORS, VText.TEXT_ANCHOR_MIDDLE, 2.0f);
            RImage.setReflectionHeight(0.7f);
            inriaLogo = new RImage(-150, -40, 0, (new ImageIcon(this.getClass().getResource(ConfigManager.INRIA_LOGO_PATH))).getImage(), 1.0f);
            insituLogo = new RImage(200, -40, 0, (new ImageIcon(this.getClass().getResource(ConfigManager.INSITU_LOGO_PATH))).getImage(), 1.0f);
            aboutLines[3] = new VText(0, -200, 0, Color.WHITE, Messages.ABOUT_DEPENDENCIES, VText.TEXT_ANCHOR_MIDDLE, 2.0f);
            aboutLines[3].setSpecialFont(ConfigManager.MONOSPACE_ABOUT_FONT);
            application.aboutSpace.addGlyph(fadeAbout);
            application.aboutSpace.addGlyph(inriaLogo);
            application.aboutSpace.addGlyph(insituLogo);
            for (int i = 0; i < aboutLines.length; i++) {
                application.aboutSpace.addGlyph(aboutLines[i]);
            }
            showingAbout = true;
        }
        application.mView.setActiveLayer(1);
        if (application.nm.ovPortal.isVisible()) {
            application.nm.toggleOverview();
        }
    }

    void hideAbout() {
        if (showingAbout) {
            showingAbout = false;
            if (insituLogo != null) {
                application.aboutSpace.removeGlyph(insituLogo);
                insituLogo = null;
            }
            if (inriaLogo != null) {
                application.aboutSpace.removeGlyph(inriaLogo);
                inriaLogo = null;
            }
            if (fadeAbout != null) {
                application.aboutSpace.removeGlyph(fadeAbout);
                fadeAbout = null;
            }
            for (int i = 0; i < aboutLines.length; i++) {
                if (aboutLines[i] != null) {
                    application.aboutSpace.removeGlyph(aboutLines[i]);
                    aboutLines[i] = null;
                }
            }
        }
        application.mView.setActiveLayer(0);
        application.nm.ovPortal.setVisible(true);
    }

    void say(final String msg) {
        final SwingWorker worker = new SwingWorker() {

            public Object construct() {
                showMessage(msg);
                sleep(ConfigManager.SAY_DURATION);
                hideMessage();
                return null;
            }
        };
        worker.start();
    }

    void showMessage(String msg) {
        synchronized (this) {
            fadedRegion.setWidth(application.panelWidth / 2 - 1);
            fadedRegion.setHeight(50);
            sayGlyph.setText(msg);
            fadedRegion.setVisible(true);
            sayGlyph.setVisible(true);
        }
    }

    void hideMessage() {
        synchronized (this) {
            fadedRegion.setVisible(false);
            sayGlyph.setVisible(false);
        }
    }

    public void press1(ViewPanel v, int mod, int jpx, int jpy, MouseEvent e) {
    }

    public void release1(ViewPanel v, int mod, int jpx, int jpy, MouseEvent e) {
    }

    public void click1(ViewPanel v, int mod, int jpx, int jpy, int clickNumber, MouseEvent e) {
        hideAbout();
    }

    public void press2(ViewPanel v, int mod, int jpx, int jpy, MouseEvent e) {
    }

    public void release2(ViewPanel v, int mod, int jpx, int jpy, MouseEvent e) {
    }

    public void click2(ViewPanel v, int mod, int jpx, int jpy, int clickNumber, MouseEvent e) {
    }

    public void press3(ViewPanel v, int mod, int jpx, int jpy, MouseEvent e) {
    }

    public void release3(ViewPanel v, int mod, int jpx, int jpy, MouseEvent e) {
    }

    public void click3(ViewPanel v, int mod, int jpx, int jpy, int clickNumber, MouseEvent e) {
    }

    public void mouseMoved(ViewPanel v, int jpx, int jpy, MouseEvent e) {
    }

    public void mouseDragged(ViewPanel v, int mod, int buttonNumber, int jpx, int jpy, MouseEvent e) {
    }

    public void mouseWheelMoved(ViewPanel v, short wheelDirection, int jpx, int jpy, MouseWheelEvent e) {
    }

    public void enterGlyph(Glyph g) {
    }

    public void exitGlyph(Glyph g) {
    }

    public void Kpress(ViewPanel v, char c, int code, int mod, KeyEvent e) {
        hideAbout();
    }

    public void Ktype(ViewPanel v, char c, int code, int mod, KeyEvent e) {
    }

    public void Krelease(ViewPanel v, char c, int code, int mod, KeyEvent e) {
    }

    public void viewActivated(View v) {
    }

    public void viewDeactivated(View v) {
    }

    public void viewIconified(View v) {
    }

    public void viewDeiconified(View v) {
    }

    public void viewClosing(View v) {
        application.exit();
    }
}

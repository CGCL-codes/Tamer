package fr.inria.zuist.viewer;

import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.OverlayLayout;
import javax.swing.JLayeredPane;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Component;
import java.awt.Insets;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import fr.inria.zvtm.engine.ViewPanel;
import fr.inria.zvtm.engine.View;
import fr.inria.zvtm.engine.VirtualSpaceManager;
import fr.inria.zvtm.glyphs.VText;
import fr.inria.zvtm.glyphs.Glyph;
import fr.inria.zvtm.glyphs.VRectangle;
import fr.inria.zvtm.glyphs.VImage;
import fr.inria.zvtm.glyphs.RImage;
import fr.inria.zvtm.event.ViewListener;
import fr.inria.zvtm.widgets.TranslucentTextArea;

class OverlayManager implements ViewListener {

    static final Color FADE_REGION_FILL = Color.BLACK;

    static final Color FADE_REGION_STROKE = Color.WHITE;

    static final String INSITU_LOGO_PATH = "/images/insitu.png";

    static final String INRIA_LOGO_PATH = "/images/inria.png";

    Viewer application;

    JScrollPane consoleSP;

    TranslucentTextArea console;

    int[] consoleMarginsWES = { 10, 10, 10 };

    int[] consolePaddingWNES = { 5, 5, 5, 5 };

    OverlayManager(Viewer app) {
        this.application = app;
    }

    void initConsole() {
        JFrame f = (JFrame) application.mView.getFrame();
        JLayeredPane lp = f.getRootPane().getLayeredPane();
        console = new TranslucentTextArea("");
        console.setEditable(false);
        console.setMargin(new Insets(consolePaddingWNES[1], consolePaddingWNES[0], consolePaddingWNES[3], consolePaddingWNES[2]));
        consoleSP = new JScrollPane(console);
        lp.add(consoleSP, (Integer) (JLayeredPane.DEFAULT_LAYER + 33));
        updateConsoleBounds();
    }

    void updateConsoleBounds() {
        consoleSP.setBounds(consoleMarginsWES[0], Math.round(application.panelHeight * .8f), application.panelWidth - consoleMarginsWES[1] - consoleMarginsWES[0], Math.round(application.panelHeight * .2f - consoleMarginsWES[2]));
    }

    void toggleConsole() {
        consoleSP.setVisible(!consoleSP.isVisible());
        if (consoleSP.isVisible()) {
            application.consoleMI.setText(Messages.CONSOLE_HIDE);
        } else {
            application.consoleMI.setText(Messages.CONSOLE_SHOW);
        }
    }

    void sayInConsole(String text) {
        console.setText(console.getText() + text);
        console.setCaretPosition(console.getDocument().getLength());
    }

    boolean showingAbout = false;

    VRectangle fadeAbout;

    VImage insituLogo, inriaLogo;

    VText[] aboutLines;

    void showAbout() {
        if (!showingAbout) {
            fadeAbout = new VRectangle(0, 0, 0, Math.round(application.panelWidth / 1.05), Math.round(application.panelHeight / 1.5), FADE_REGION_FILL, FADE_REGION_STROKE, 0.85f);
            aboutLines = new VText[5];
            aboutLines[0] = new VText(0, 150, 0, Color.WHITE, "ZUIST Viewer", VText.TEXT_ANCHOR_MIDDLE, 4.0f);
            aboutLines[1] = new VText(0, 110, 0, Color.WHITE, "v" + Messages.VERSION, VText.TEXT_ANCHOR_MIDDLE, 2.0f);
            aboutLines[2] = new VText(0, 0, 0, Color.WHITE, "By Emmanuel Pietriga and Romain Primet", VText.TEXT_ANCHOR_MIDDLE, 2.0f);
            RImage.setReflectionHeight(0.7f);
            inriaLogo = new RImage(-150, -70, 0, (new ImageIcon(this.getClass().getResource(INRIA_LOGO_PATH))).getImage(), 1.0f);
            insituLogo = new RImage(200, -70, 0, (new ImageIcon(this.getClass().getResource(INSITU_LOGO_PATH))).getImage(), 1.0f);
            aboutLines[3] = new VText(0, -170, 0, Color.WHITE, "Based on the ZVTM toolkit", VText.TEXT_ANCHOR_MIDDLE, 2.0f);
            aboutLines[4] = new VText(0, -200, 0, Color.WHITE, "http://zvtm.sf.net", VText.TEXT_ANCHOR_MIDDLE, 2.0f);
            application.ovSpace.addGlyph(fadeAbout);
            application.ovSpace.addGlyph(inriaLogo);
            application.ovSpace.addGlyph(insituLogo);
            for (int i = 0; i < aboutLines.length; i++) {
                application.ovSpace.addGlyph(aboutLines[i]);
            }
            showingAbout = true;
        }
        application.mView.setActiveLayer(2);
    }

    void hideAbout() {
        if (showingAbout) {
            showingAbout = false;
            if (insituLogo != null) {
                application.ovSpace.removeGlyph(insituLogo);
                insituLogo = null;
            }
            if (inriaLogo != null) {
                application.ovSpace.removeGlyph(inriaLogo);
                inriaLogo = null;
            }
            if (fadeAbout != null) {
                application.ovSpace.removeGlyph(fadeAbout);
                fadeAbout = null;
            }
            for (int i = 0; i < aboutLines.length; i++) {
                if (aboutLines[i] != null) {
                    application.ovSpace.removeGlyph(aboutLines[i]);
                    aboutLines[i] = null;
                }
            }
        }
        application.mView.setActiveLayer(0);
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

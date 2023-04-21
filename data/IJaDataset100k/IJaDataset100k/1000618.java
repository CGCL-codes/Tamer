package fr.inria.zuist.app.ue;

import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;
import com.xerox.VTM.engine.SwingWorker;
import com.xerox.VTM.engine.View;
import com.xerox.VTM.engine.LongPoint;
import com.xerox.VTM.engine.AnimManager;
import com.xerox.VTM.glyphs.VText;
import com.xerox.VTM.glyphs.Glyph;
import com.xerox.VTM.glyphs.VRectangle;
import com.xerox.VTM.glyphs.VImage;
import com.xerox.VTM.glyphs.VRectangleST;
import net.claribole.zvtm.glyphs.VTextST;
import net.claribole.zvtm.glyphs.RImage;
import net.claribole.zvtm.engine.RepaintListener;
import net.claribole.zvtm.engine.RepaintAdapter;
import net.claribole.zvtm.engine.GlyphKillAction;
import fr.inria.zuist.engine.*;

public class OverlayManager {

    static final float[] LINK_ANIM_APPEAR = { 0, 0, 0, 0, 0, 0, 1 };

    static final float[] LINK_ANIM_DISAPPEAR = { 0, 0, 0, 0, 0, 0, -1 };

    static final float[] FADEREGION_ANIM_APPEAR = { 0, 0, 0, 0, 0, 0, 0.85f };

    static final float[] FADEREGION_ANIM_DISAPPEAR = { 0, 0, 0, 0, 0, 0, -0.85f };

    static final Color FADE_REGION_FILL = Color.BLACK;

    static final Color FADE_REGION_STROKE = Color.WHITE;

    static final Color LINK_COLOR = Color.WHITE;

    static final Color DISABLED_LINK_COLOR = Color.GRAY;

    static final Color SAY_MSG_COLOR = Color.LIGHT_GRAY;

    static final Font SAY_MSG_FONT = new Font("Arial", Font.PLAIN, 24);

    static final Color TITLE_MSG_COLOR = Color.LIGHT_GRAY;

    static final int MAX_NB_ITEMS_FOR_NORMAL_FONT = 70;

    static final float NORMAL_FONT_SCALE_FACTOR = 1.5f;

    static final float SMALL_FONT_SCALE_FACTOR = 1.0f;

    static final String INSITU_LOGO_PATH = "/images/insitu.png";

    boolean showingLinks = false;

    UISTExplorer application;

    VRectangleST fadedRegion;

    VRectangleST fadedRegionTitle;

    VTextST[] links;

    VText title;

    VText sayGlyph;

    long[] visibleRegion = new long[4];

    long[] padding = { 20, 20, 20, 20 };

    OverlayManager(UISTExplorer app) {
        this.application = app;
        fadedRegion = new VRectangleST(0, 0, 0, 10, 10, FADE_REGION_FILL, FADE_REGION_STROKE, 0.85f);
        application.vsm.addGlyph(fadedRegion, application.ovSpace);
        fadedRegion.setVisible(false);
        fadedRegionTitle = new VRectangleST(0, 0, 0, 10, 10, FADE_REGION_FILL, FADE_REGION_STROKE, 0.85f);
        application.vsm.addGlyph(fadedRegionTitle, application.ovSpace);
        fadedRegionTitle.setVisible(false);
        title = new VText(0, 0, 0, TITLE_MSG_COLOR, " ", VText.TEXT_ANCHOR_MIDDLE, NORMAL_FONT_SCALE_FACTOR);
        application.vsm.addGlyph(title, application.ovSpace);
        title.setVisible(false);
        sayGlyph = new VText(0, -10, 0, SAY_MSG_COLOR, " ", VText.TEXT_ANCHOR_MIDDLE);
        sayGlyph.setSpecialFont(SAY_MSG_FONT);
        application.vsm.addGlyph(sayGlyph, application.ovSpace);
        sayGlyph.setVisible(false);
    }

    void displayLinksP1(String[] labels, String[] IDs, String titleS) {
        application.mView.setActiveLayer(2);
        showingLinks = true;
        links = new VTextST[labels.length];
        for (int i = 0; i < links.length; i++) {
            VTextST t = new VTextST(0, 0, 0, (IDs[i] != null) ? LINK_COLOR : DISABLED_LINK_COLOR, labels[i], VText.TEXT_ANCHOR_START, 0, (links.length < MAX_NB_ITEMS_FOR_NORMAL_FONT) ? NORMAL_FONT_SCALE_FACTOR : SMALL_FONT_SCALE_FACTOR);
            t.setType(IDs[i]);
            application.vsm.addGlyph(t, application.ovSpace);
            links[i] = t;
        }
        title.setVisible(true);
        title.setText(titleS);
        waitForBounds(new LongPoint[links.length], new LongPoint[1]);
    }

    void waitForBounds(final LongPoint[] linkBounds, final LongPoint[] titleBounds) {
        boolean nullBounds = false;
        for (int i = 0; i < links.length; i++) {
            linkBounds[i] = links[i].getBounds(application.ovCamera.getIndex());
            if ((linkBounds[i].x == 0 || linkBounds[i].y == 0) && links[i].getText().length() > 0) {
                nullBounds = true;
                break;
            }
        }
        titleBounds[0] = title.getBounds(application.ovCamera.getIndex());
        if (nullBounds) {
            application.vsm.repaintNow(application.mView, new RepaintAdapter() {

                public void viewRepainted(View v) {
                    application.mView.removeRepaintListener();
                    waitForBounds(linkBounds, titleBounds);
                }
            });
        } else {
            displayLinksP2(linkBounds, titleBounds);
        }
    }

    static final long MIN_FADED_REGION_WIDTH = 200;

    void displayLinksP2(LongPoint[] linkBounds, LongPoint[] titleBounds) {
        application.mView.getVisibleRegion(application.ovCamera, visibleRegion);
        long xo = visibleRegion[0] + padding[0] + 20;
        long x = xo;
        long yo = visibleRegion[1] - padding[1] * 3 - linkBounds[0].y * 2;
        long y = yo;
        LongPoint[] transData = new LongPoint[links.length];
        long totalWidth = 0;
        long lowestY = yo;
        long maxWidthInColumn = 0;
        for (int i = 0; i < links.length; i++) {
            if (maxWidthInColumn < linkBounds[i].x) {
                maxWidthInColumn = linkBounds[i].x;
            }
            transData[i] = new LongPoint(x - links[i].vx, y - links[i].vy);
            y -= linkBounds[i].y * 2;
            if (y < lowestY) {
                lowestY = y;
            }
            if (y < visibleRegion[3] + padding[3]) {
                x += maxWidthInColumn * 1.1;
                y = yo;
                totalWidth += maxWidthInColumn * 1.1;
                maxWidthInColumn = 0;
            } else if (i == links.length - 1) {
                totalWidth += maxWidthInColumn * 1.1;
            }
        }
        long fittingWidth = (totalWidth > MIN_FADED_REGION_WIDTH) ? totalWidth : MIN_FADED_REGION_WIDTH;
        if (titleBounds[0].x > 0 && fittingWidth < titleBounds[0].x) {
            fittingWidth = Math.round(titleBounds[0].x * 1.2);
        }
        for (int i = 0; i < links.length; i++) {
            transData[i].translate((-2 * xo - fittingWidth) / 2, 0);
            application.vsm.animator.createGlyphAnimation(UISTExplorer.ANIM_MOV_LENGTH, AnimManager.GL_COLOR_LIN, LINK_ANIM_APPEAR, links[i].getID());
            application.vsm.animator.createGlyphAnimation(UISTExplorer.ANIM_MOV_LENGTH, AnimManager.GL_TRANS_SIG, transData[i], links[i].getID());
        }
        fadedRegion.setWidth(Math.round(fittingWidth / 1.8));
        fadedRegion.setHeight((visibleRegion[1] - visibleRegion[3] - padding[1] - padding[3]) / 2);
        fadedRegion.setVisible(true);
        fadedRegionTitle.moveTo(0, fadedRegion.getHeight() - 20);
        fadedRegionTitle.setWidth(Math.round(fittingWidth / 1.8));
        fadedRegionTitle.setHeight(20);
        fadedRegionTitle.setVisible(true);
        title.moveTo(0, fadedRegion.getHeight() - 24);
    }

    void hideLinks(boolean animate) {
        if (animate) {
            for (int i = 0; i < links.length; i++) {
                application.vsm.animator.createGlyphAnimation(UISTExplorer.ANIM_MOV_LENGTH, AnimManager.GL_COLOR_LIN, LINK_ANIM_DISAPPEAR, links[i].getID(), new GlyphKillAction(application.vsm));
            }
        } else {
            for (int i = 0; i < links.length; i++) {
                application.ovSpace.removeGlyph(links[i]);
            }
        }
        fadedRegion.setVisible(false);
        fadedRegionTitle.setVisible(false);
        title.setVisible(false);
        links = null;
        showingLinks = false;
    }

    void say(final String msg, final int duration) {
        final SwingWorker worker = new SwingWorker() {

            public Object construct() {
                showMessage(msg);
                sleep(duration);
                hideMessage();
                return null;
            }
        };
        worker.start();
    }

    void showMessage(String msg) {
        synchronized (this) {
            fadedRegion.setWidth(application.panelWidth / 2);
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

    boolean showingAbout = false;

    VRectangleST fadeAbout;

    VImage insituLogo;

    VText aboutLine1, aboutLine2, aboutLine3, aboutLine4, aboutLine5, aboutLine6;

    void showAbout() {
        if (!showingAbout) {
            fadeAbout = new VRectangleST(0, 0, 0, Math.round(application.panelWidth / 2.1), Math.round(application.panelHeight / 3), FADE_REGION_FILL, FADE_REGION_STROKE, 0.85f);
            aboutLine1 = new VText(0, 150, 0, Color.WHITE, "ZUIST", VText.TEXT_ANCHOR_MIDDLE, 4.0f);
            aboutLine2 = new VText(0, 80, 0, Color.WHITE, "By Emmanuel Pietriga (INRIA) & Michel Beaudouin-Lafon (Université Paris-Sud)", VText.TEXT_ANCHOR_MIDDLE, 2.0f);
            RImage.setReflectionHeight(0.7f);
            insituLogo = new RImage(0, 10, 0, (new ImageIcon(this.getClass().getResource(INSITU_LOGO_PATH))).getImage(), 1.0f);
            aboutLine3 = new VText(0, -100, 0, Color.WHITE, "Based on the ZVTM toolkit", VText.TEXT_ANCHOR_MIDDLE, 2.0f);
            aboutLine4 = new VText(0, -130, 0, Color.WHITE, "http://zvtm.sf.net", VText.TEXT_ANCHOR_MIDDLE, 2.0f);
            aboutLine4.setSpecialFont(UISTExplorer.KEYWORD_ATOM_FONT);
            aboutLine5 = new VText(0, -200, 0, Color.WHITE, "Thanks to: David Martin, Gerald Morrison & Walace Kroeker of Smart Technologies,", VText.TEXT_ANCHOR_MIDDLE, 1.5f);
            aboutLine6 = new VText(0, -220, 0, Color.WHITE, "Bernie Rous & Mark Mandelbaum of ACM and Jean-Daniel Fekete for coining the name ZUIST.", VText.TEXT_ANCHOR_MIDDLE, 1.5f);
            application.vsm.addGlyph(fadeAbout, application.ovSpace);
            application.vsm.addGlyph(aboutLine1, application.ovSpace);
            application.vsm.addGlyph(aboutLine2, application.ovSpace);
            application.vsm.addGlyph(insituLogo, application.ovSpace);
            application.vsm.addGlyph(aboutLine3, application.ovSpace);
            application.vsm.addGlyph(aboutLine4, application.ovSpace);
            application.vsm.addGlyph(aboutLine5, application.ovSpace);
            application.vsm.addGlyph(aboutLine6, application.ovSpace);
            showingAbout = true;
        }
    }

    void hideAbout() {
        if (showingAbout) {
            showingAbout = false;
            if (aboutLine1 != null) {
                application.ovSpace.removeGlyph(aboutLine1);
                aboutLine1 = null;
            }
            if (aboutLine2 != null) {
                application.ovSpace.removeGlyph(aboutLine2);
                aboutLine2 = null;
            }
            if (insituLogo != null) {
                application.ovSpace.removeGlyph(insituLogo);
                insituLogo = null;
            }
            if (aboutLine3 != null) {
                application.ovSpace.removeGlyph(aboutLine3);
                aboutLine3 = null;
            }
            if (aboutLine4 != null) {
                application.ovSpace.removeGlyph(aboutLine4);
                aboutLine4 = null;
            }
            if (aboutLine5 != null) {
                application.ovSpace.removeGlyph(aboutLine5);
                aboutLine5 = null;
            }
            if (aboutLine6 != null) {
                application.ovSpace.removeGlyph(aboutLine6);
                aboutLine6 = null;
            }
            if (fadeAbout != null) {
                application.ovSpace.removeGlyph(fadeAbout);
                fadeAbout = null;
            }
        }
    }
}

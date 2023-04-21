package org.gerhardb.jibs.viewer.shows.comic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.*;
import java.util.prefs.Preferences;
import javax.swing.*;
import org.gerhardb.jibs.viewer.IFrame;
import org.gerhardb.jibs.viewer.IRevert;
import org.gerhardb.jibs.viewer.PicInfoDialog;
import org.gerhardb.lib.scroller.ScrollerChangeEvent;
import org.gerhardb.lib.scroller.ScrollerListener;
import org.gerhardb.lib.scroller.ScrollerSlider;

/**
 * Full Screen Display!
 * ALWAYS SHRINKS!  Tried to do otherwise, but failed.
 * Don't think you EVER need full screen with scroll bars.
 */
public class ComicScreen implements ScrollerListener, IRevert {

    static final Preferences clsPrefs = Preferences.userRoot().node("/org/gerhardb/jibs/viewer/shows/comic");

    static final String DIVIDER_LOCATION = "DIVIDER_LOCATION";

    static final String DISPLAY_SLIDER = "DISPLAY_SLIDER";

    static final Color BACKGROUND_COLOR = Color.black;

    IFrame myParentFrame;

    private boolean iShowFullScreen;

    JFrame mySurfaceFrame;

    private JPanel mySurfacePanel = new JPanel(new BorderLayout());

    MagnifiedPanel myMagnifiedPanel = new MagnifiedPanel();

    private ThumbnailImage myThumbnailImage = new ThumbnailImage();

    private JPanel myBottomPanel = new JPanel(new BorderLayout());

    private GraphicsEnvironment myGE = GraphicsEnvironment.getLocalGraphicsEnvironment();

    GraphicsDevice myGD = this.myGE.getDefaultScreenDevice();

    JSplitPane mySplitPane;

    ComicMouseAdapter myComicMouseAdapter = new ComicMouseAdapter();

    public ComicScreen(IFrame mf) {
        this(mf, true);
    }

    public ComicScreen(IFrame mf, boolean showFullScreen) {
        this.myParentFrame = mf;
        this.iShowFullScreen = showFullScreen;
        this.myParentFrame.setWaitCursor(true);
        this.myParentFrame.getScroller().setEndMessages(true);
        this.mySplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.myThumbnailImage, this.myMagnifiedPanel);
        if (this.iShowFullScreen) {
            this.mySurfaceFrame = new JFrame(this.myGD.getDefaultConfiguration());
            this.mySurfaceFrame.setUndecorated(true);
        } else {
            this.mySurfaceFrame = new JFrame("JIBS Comic Viewer");
            this.mySurfaceFrame.setUndecorated(false);
        }
        this.mySurfaceFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        this.myBottomPanel.setVisible(clsPrefs.getBoolean(DISPLAY_SLIDER, true));
        ScrollerSlider slider = new ScrollerSlider(SwingConstants.HORIZONTAL, this.myParentFrame.getScroller());
        this.myBottomPanel.add(slider, BorderLayout.CENTER);
        this.mySurfacePanel.add(this.mySplitPane, BorderLayout.CENTER);
        this.mySurfacePanel.add(this.myBottomPanel, BorderLayout.SOUTH);
        this.mySurfaceFrame.setContentPane(this.mySurfacePanel);
        this.mySurfaceFrame.addKeyListener(new CompicKeyAdapater());
        this.mySurfaceFrame.addKeyListener(this.myParentFrame.getScroller().getScrollerKeyListener());
        this.myThumbnailImage.addMouseListener(this.myComicMouseAdapter);
        this.myMagnifiedPanel.myMagnifiedImage.addMouseListener(this.myComicMouseAdapter);
        scrollerChanged(null);
        this.myParentFrame.getScroller().addScrollerListener(this);
        this.mySplitPane.setDividerLocation(clsPrefs.getInt(DIVIDER_LOCATION, 200));
        try {
            if (this.iShowFullScreen) {
                this.myGD.setFullScreenWindow(this.mySurfaceFrame);
                this.myParentFrame.getScroller().setAutoFocus(this.mySurfaceFrame);
            } else {
                this.mySurfaceFrame.setBounds(new Rectangle(10, 10, 1200, 600));
                this.mySurfaceFrame.setVisible(true);
                this.mySurfaceFrame.addWindowListener(new WindowAdapter() {

                    @Override
                    public void windowClosing(WindowEvent evt) {
                        System.out.println("Comic setting prefs");
                        try {
                            clsPrefs.putInt(DIVIDER_LOCATION, ComicScreen.this.mySplitPane.getDividerLocation());
                            clsPrefs.flush();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
            this.mySurfaceFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            this.myMagnifiedPanel.myMagnifiedImage.requestFocus(true);
            this.mySurfaceFrame.requestFocus();
            this.mySurfaceFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            this.myParentFrame.setWaitCursor(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public JFrame getFrame() {
        return this.mySurfaceFrame;
    }

    @Override
    public void scrollerChanged(ScrollerChangeEvent ce) {
        if (!this.myParentFrame.getScroller().getValueIsAdjusting()) {
            BufferedImage image = null;
            try {
                image = this.myParentFrame.getScroller().getCurrentImage();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (image == null) {
                image = this.myParentFrame.getScroller().getBeyondBounds();
                if (image != null) {
                    this.myMagnifiedPanel.myMagnifiedImage.setImage(image);
                    this.myThumbnailImage.setImage(image);
                }
                return;
            }
            this.myMagnifiedPanel.myMagnifiedImage.setImage(image);
            this.myThumbnailImage.setImage(image);
        }
    }

    @Override
    public void revert() {
        if (!this.iShowFullScreen) {
            return;
        }
        this.myParentFrame.getScroller().stopSlideShow();
        this.myParentFrame.getScroller().removeScrollerListener(ComicScreen.this);
        System.out.println("Comic setting prefs");
        try {
            clsPrefs.putInt(DIVIDER_LOCATION, this.mySplitPane.getDividerLocation());
            clsPrefs.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.mySurfaceFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        this.myMagnifiedPanel.myMagnifiedImage.setBackground(ComicScreen.BACKGROUND_COLOR);
        this.myMagnifiedPanel.myMagnifiedImage.setImage(null);
        this.myThumbnailImage.setImage(null);
        Runnable runSwitch = new Runnable() {

            @Override
            public void run() {
                ComicScreen.this.myGD.setFullScreenWindow(null);
                ComicScreen.this.mySurfaceFrame.setVisible(false);
                ComicScreen.this.mySurfaceFrame.dispose();
                ComicScreen.this.myParentFrame.setWaitCursor(true);
                ComicScreen.this.myParentFrame.gotoRegularScreen();
                ComicScreen.this.myParentFrame.setWaitCursor(false);
            }
        };
        EventQueue.invokeLater(runSwitch);
    }

    @Override
    public boolean isFullScreen() {
        return this.iShowFullScreen;
    }

    void toggleBottom() {
        if (this.myBottomPanel.isShowing()) {
            this.myBottomPanel.setVisible(false);
            this.mySurfacePanel.revalidate();
            try {
                clsPrefs.putBoolean(DISPLAY_SLIDER, false);
                clsPrefs.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            this.myBottomPanel.setVisible(true);
            this.mySurfacePanel.revalidate();
            try {
                clsPrefs.putBoolean(DISPLAY_SLIDER, true);
                clsPrefs.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private class CompicKeyAdapater extends KeyAdapter {

        protected CompicKeyAdapater() {
        }

        @Override
        public void keyPressed(KeyEvent event) {
            switch(event.getKeyCode()) {
                case KeyEvent.VK_ENTER:
                    if ((event.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == InputEvent.CTRL_DOWN_MASK) {
                        revert();
                    }
                    if ((event.getModifiersEx() & InputEvent.ALT_DOWN_MASK) == InputEvent.ALT_DOWN_MASK) {
                        revert();
                    }
                    event.consume();
                    return;
                case KeyEvent.VK_UP:
                    ComicScreen.this.myMagnifiedPanel.myMagnifiedImage.up();
                    event.consume();
                    break;
                case KeyEvent.VK_DOWN:
                    ComicScreen.this.myMagnifiedPanel.myMagnifiedImage.down();
                    event.consume();
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent event) {
            switch(event.getKeyCode()) {
                case KeyEvent.VK_F1:
                    revert();
                    ComicScreen.this.myParentFrame.showHelp();
                    event.consume();
                    return;
                case KeyEvent.VK_ESCAPE:
                case KeyEvent.VK_F2:
                    revert();
                    event.consume();
                    return;
                case KeyEvent.VK_F3:
                    toggleBottom();
                    event.consume();
                    return;
                case KeyEvent.VK_UP:
                case KeyEvent.VK_DOWN:
                    event.consume();
                    break;
                case KeyEvent.VK_PAGE_UP:
                    ComicScreen.this.myParentFrame.getScroller().up();
                    event.consume();
                    break;
                case KeyEvent.VK_PAGE_DOWN:
                    ComicScreen.this.myParentFrame.getScroller().down();
                    event.consume();
                    break;
                case KeyEvent.VK_HOME:
                    ComicScreen.this.myMagnifiedPanel.myMagnifiedImage.allUp();
                    event.consume();
                    break;
                case KeyEvent.VK_END:
                    ComicScreen.this.myMagnifiedPanel.myMagnifiedImage.allDown();
                    event.consume();
                    break;
            }
        }
    }

    private class ComicMouseAdapter extends MouseAdapter {

        protected ComicMouseAdapter() {
        }

        @Override
        public void mousePressed(MouseEvent event) {
            System.out.println("COMIC MOUSE PRESSED");
            if (event.isPopupTrigger() || event.getButton() == MouseEvent.BUTTON2 || event.getButton() == MouseEvent.BUTTON3) {
                if (ComicScreen.this.myParentFrame.getScroller().isBeyond()) {
                    return;
                }
                int width = -1;
                int height = -1;
                try {
                    BufferedImage image = ComicScreen.this.myParentFrame.getScroller().getCurrentImage();
                    width = image.getWidth();
                    height = image.getHeight();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                new PicInfoDialog(ComicScreen.this.myParentFrame.getScroller().getCurrentFile(), ComicScreen.this.mySurfaceFrame, ComicScreen.this.myMagnifiedPanel.myMagnifiedImage, ComicScreen.this, ComicScreen.this.myParentFrame.getActions().getToolBarButton("file", "trash"), ComicScreen.this.myParentFrame.getActions().getToolBarButton("file", "park"), true, width, height, true, ComicScreen.this.myParentFrame.getScroller());
            } else {
                ComicScreen.this.myParentFrame.getScroller().down();
            }
        }
    }
}

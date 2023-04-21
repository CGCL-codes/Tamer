package org.xebra.client.gui.cine;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.UIManager;
import org.xebra.client.action.CineAction;
import org.xebra.client.events.EventDispatcher;
import org.xebra.client.events.ImageSelectionEvent;
import org.xebra.client.gui.ActivePanelTracker;
import org.xebra.client.gui.StudyPanel;
import org.xebra.client.gui.image.ImagePanel;
import org.xebra.client.image.DiagnosticImage;
import org.xebra.client.image.DiagnosticImage.ImageStatus;
import org.xebra.client.study.StudyTree;
import org.xebra.client.util.ApplicationProperties;
import org.xebra.client.util.GUIMessages;
import org.xebra.client.util.ImageGenerator;
import org.xebra.dcm.op.WindowLevelOp;
import org.xebra.dcm.util.GarbageCollector;

/**
 * A modal dialog to run the CINE feature.
 * 
 * @author Rafael Chargel
 * @version $Revision: 1.3 $
 */
public class CineModalDialog extends JDialog {

    private static final long serialVersionUID = 5341062621219940045L;

    private Dimension size;

    private CineAction action;

    private ImagePanel panel;

    private BufferedImage[] images;

    private LoaderLabel loader;

    private MovieLabel movie;

    private JPanel viewer;

    private TimeLine timeline;

    private Thread playThread;

    private boolean loop = true;

    private boolean bounce;

    private boolean playing;

    private int index = 0;

    private int fps = 20;

    private int add = 1;

    public CineModalDialog(CineAction action, Dimension size) {
        UIManager.put("Slider.background", Color.black);
        UIManager.put("Slider.foreground", Color.white);
        UIManager.put("Slider.highlight", Color.white);
        UIManager.put("Slider.shadow", new Color(0x33, 0x33, 0xff));
        UIManager.put("Slider.tickColor", new Color(0x33, 0x33, 0xcc));
        this.action = action;
        this.size = size;
        StudyPanel studyPanel = ActivePanelTracker.getActiveStudyPanel();
        if (studyPanel == null) {
            this.dispose();
            return;
        }
        GUIMessages messages = GUIMessages.getSingletonInstance();
        setTitle(messages.getText("message.gui.title.dialog.cine") + " " + size.width + "x" + size.height);
        if (!ApplicationProperties.getSingletonInstance().isMac()) {
            setIconImage(ImageGenerator.loadApplicationIcon());
        }
        setResizable(false);
        setModal(true);
        this.panel = studyPanel.getViewer().getPanel();
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                CineModalDialog.this.dispose();
            }
        });
        initUI();
        initImages();
        pack();
        setVisible(true);
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public void setBounce(boolean bounce) {
        this.bounce = bounce;
    }

    public void setFPS(int fps) {
        this.fps = fps;
    }

    public int getImageIndex() {
        return this.index;
    }

    private void showNext() {
        int index = getImageIndex() + add;
        if (index < 0) {
            if (this.loop) {
                this.add = 1;
                index += 2;
            } else {
                stop();
                setImageIndex(0);
                return;
            }
        }
        if (index == this.action.getNumImages()) {
            if (this.bounce) {
                this.add = -1;
                index -= 2;
            } else if (this.loop) {
                index = 0;
            } else {
                stop();
                setImageIndex(0);
                return;
            }
        }
        setImageIndex(index);
    }

    public void setImageIndex(int index) {
        this.index = index;
        this.timeline.setImageIndex(this.index);
        if (this.movie != null) {
            this.movie.repaint();
        }
    }

    public BufferedImage getImage() {
        return this.images[this.index];
    }

    public boolean isPlaying() {
        return this.playing;
    }

    public void play() {
        if (!isPlaying()) {
            this.add = 1;
            this.playing = true;
            this.playThread = new PlayThread(this);
            this.playThread.start();
            this.timeline.setPlaying(true);
        }
    }

    public void stop() {
        if (isPlaying()) {
            this.playThread.interrupt();
            this.playThread = null;
            this.add = 1;
            this.playing = false;
            this.timeline.setPlaying(false);
        }
    }

    public void dispose() {
        super.dispose();
        this.images = null;
        this.action.setPlaying(false);
        ImageSelectionEvent evt = new ImageSelectionEvent(this, this.action.getStudyUID(), this.action.getSeriesUID(), this.panel.getImageIndex());
        EventDispatcher.getSingletonInstance().fireImageSelectionChangedEvent(evt);
        try {
            GarbageCollector.runGC();
        } catch (Throwable t) {
        }
    }

    private void initImages() {
        final String seriesUID = this.action.getSeriesUID();
        final StudyTree tree = StudyTree.getInstance(this.action.getStudyUID());
        final int total = this.action.getNumImages();
        final double window = this.panel.getWindow();
        final double level = this.panel.getLevel();
        this.images = new BufferedImage[total];
        new Thread(new Runnable() {

            public void run() {
                int imgNum = 0;
                for (DiagnosticImage image : tree.getAllImages(seriesUID)) {
                    boolean wasLoaded = image.getStatus() == ImageStatus.FULLY_LOADED;
                    if (!wasLoaded) {
                        image.loadImage(true);
                    }
                    BufferedImage img = image.getImage();
                    Dimension displaySize = image.getDisplayableSize(CineModalDialog.this.size.width, CineModalDialog.this.size.height);
                    double s = displaySize.getWidth() / img.getWidth();
                    AffineTransform scale = AffineTransform.getScaleInstance(s, s);
                    AffineTransformOp op = new AffineTransformOp(scale, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                    img = op.filter(img, null);
                    image.purgeFromMemory();
                    if (img.getColorModel().getNumComponents() > 1) {
                        CineModalDialog.this.images[image.getImageIndex()] = img;
                        imgNum++;
                        float per = imgNum / (float) total;
                        CineModalDialog.this.loader.setPercent((int) (per * 100));
                        continue;
                    }
                    WindowLevelOp wlop = new WindowLevelOp(window, level);
                    wlop.setIntercept(image.getIntercept());
                    wlop.setSlope(image.getSlope());
                    wlop.setInverted(image.isInverted());
                    wlop.setUsesPadding(image.isPadded());
                    wlop.setPad(image.getPadding());
                    wlop.setSigned(image.isSigned());
                    img = wlop.filter(img, null);
                    CineModalDialog.this.images[image.getImageIndex()] = img;
                    imgNum++;
                    float per = imgNum / (float) total;
                    CineModalDialog.this.loader.setPercent((int) (per * 100));
                }
                try {
                    GarbageCollector.runGC();
                } catch (Throwable t) {
                }
                CineModalDialog.this.movie = new MovieLabel(CineModalDialog.this);
                CineModalDialog.this.viewer.removeAll();
                CineModalDialog.this.viewer.add(CineModalDialog.this.movie);
                CineModalDialog.this.viewer.validate();
            }
        }).start();
    }

    private void initUI() {
        getContentPane().setLayout(new BorderLayout());
        this.viewer = new JPanel();
        this.viewer.setMaximumSize(this.size);
        this.viewer.setMinimumSize(this.size);
        this.viewer.setPreferredSize(this.size);
        this.viewer.setLayout(new GridLayout(1, 1));
        this.loader = new LoaderLabel();
        this.viewer.add(this.loader);
        this.timeline = new TimeLine(this, this.action.getNumImages(), 1);
        getContentPane().add(this.viewer, BorderLayout.CENTER);
        getContentPane().add(new CineControlPanel(this), BorderLayout.EAST);
        getContentPane().add(this.timeline, BorderLayout.SOUTH);
    }

    private static class PlayThread extends Thread {

        private CineModalDialog dialog;

        public PlayThread(CineModalDialog dialog) {
            this.dialog = dialog;
        }

        public void run() {
            while (this.dialog.isPlaying()) {
                this.dialog.showNext();
                try {
                    Thread.sleep(1000 / this.dialog.fps);
                } catch (Throwable t) {
                }
            }
        }
    }
}

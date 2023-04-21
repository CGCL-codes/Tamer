package org.ladybug.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import org.ladybug.cfg.Configuration;
import org.ladybug.events.Event;
import org.ladybug.events.EventDispatcher;
import org.ladybug.events.EventGenerator;
import org.ladybug.gui.toolbox.Positioning;
import org.ladybug.gui.toolbox.icons.LadybugIconGenerator;
import org.ladybug.gui.toolbox.launchers.LauncherFactory;
import org.ladybug.gui.toolbox.progressbar.ProgressBar;
import org.ladybug.log.LogEngine;
import org.ladybug.utils.Constants;
import org.treeconf.TreeConfException;

/**
 * @author Aurelian Pop
 */
public class Window extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final Configuration CFG = Configuration.getInstance();

    private static final EventDispatcher ED = EventDispatcher.getInstance();

    private final SuperModel superModel = new SuperModel(this);

    private final ProgressBar progressBar = new ProgressBar(this);

    private final MyComponentListener componentListener = new MyComponentListener();

    private boolean fullScreenOn = false;

    public Window() {
        setMode("");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setFocusable(true);
        addKeyListener(new MyKeyListener());
        addWindowListener(new MyWindowListener());
        addComponentListener(componentListener);
        ED.addListener(progressBar);
        setIconImages(createIcons());
        setMinimumSize(Constants.MINIMUM_WINDOW_DIMENSION);
        setPreferredSize(Constants.PREFERRED_WINDOW_DIMENSION);
        setSize(Constants.PREFERRED_WINDOW_DIMENSION);
        Positioning.centerScreen(this);
        if (CFG.firstRun.toBoolean()) {
            final SplashPanel glassPane = new SplashPanel(getBounds());
            setGlassPane(glassPane);
            glassPane.showSplashScreen();
            try {
                CFG.firstRun.setValue("false");
                CFG.save();
            } catch (final FileNotFoundException e) {
                LogEngine.error("Problem saving the configuration", e);
            } catch (final TreeConfException e) {
                LogEngine.error("Problem saving the configuration", e);
            }
        }
        superModel.setLobbyPhase();
    }

    public void broadcastRequiredEvents() {
        componentListener.dispatchEvents();
    }

    /**
     * Sets the window title, according to the phase the software is in.
     * 
     * @param phase
     *            the name of the phase the software is in
     */
    public void setMode(final String phase) {
        String title = Constants.PRODUCT_NAME + " - " + Constants.PRODUCT_MOTTO;
        if (phase.length() > 0) {
            title = phase + " - " + title;
        }
        super.setTitle(title);
    }

    /**
     * Creates a list of images of 16x16, 32x32, 48x48, and 64x64 to be used as system icons
     */
    private static List<BufferedImage> createIcons() {
        final LadybugIconGenerator iconGenerator = new LadybugIconGenerator();
        final List<BufferedImage> icons = new ArrayList<BufferedImage>();
        for (int i = 16; i <= 64; i += 16) {
            icons.add(iconGenerator.generateImage(i, i));
        }
        return icons;
    }

    private void switchFullScreen() {
        if (Constants.GS.isFullScreenSupported()) {
            if (fullScreenOn) {
                Constants.GS.setFullScreenWindow(null);
            } else {
                Constants.GS.setFullScreenWindow(this);
                validate();
            }
            fullScreenOn = !fullScreenOn;
        }
    }

    private class MyKeyListener implements KeyListener {

        @Override
        public void keyTyped(final KeyEvent e) {
        }

        @Override
        public void keyPressed(final KeyEvent e) {
            switch(e.getKeyCode()) {
                case 112:
                    LauncherFactory.createHelpLauncher().launch();
                    break;
                case 122:
                    switchFullScreen();
                    break;
                default:
            }
        }

        @Override
        public void keyReleased(final KeyEvent e) {
        }
    }

    private static class MyWindowListener implements WindowListener {

        @Override
        public void windowOpened(final WindowEvent e) {
        }

        @Override
        public void windowClosing(final WindowEvent e) {
            ED.dispatchEventsAnonymously(Event.SHUTDOWN);
        }

        @Override
        public void windowClosed(final WindowEvent e) {
        }

        @Override
        public void windowIconified(final WindowEvent e) {
        }

        @Override
        public void windowDeiconified(final WindowEvent e) {
        }

        @Override
        public void windowActivated(final WindowEvent e) {
        }

        @Override
        public void windowDeactivated(final WindowEvent e) {
        }
    }

    private class MyComponentListener implements ComponentListener, EventGenerator {

        private final int midlowSwitchoverArea = computeArea(new Dimension(1120, 630));

        private final int midhighSwitchoverArea = computeArea(new Dimension(1920, 1080));

        private int currentArea;

        public MyComponentListener() {
            currentArea = computeArea(superModel.getWindow().getSize());
            dispatchEvents();
        }

        @Override
        public void componentResized(final ComponentEvent e) {
            final Component c = (Component) e.getSource();
            if (c instanceof Window) {
                final int prevArea = currentArea;
                currentArea = computeArea(c.getSize());
                if (shouldResize(currentArea, prevArea, midhighSwitchoverArea) || shouldResize(currentArea, prevArea, midlowSwitchoverArea)) {
                    dispatchEvents();
                    invalidate();
                }
            }
        }

        @Override
        public void componentMoved(final ComponentEvent e) {
        }

        @Override
        public void componentShown(final ComponentEvent e) {
        }

        @Override
        public void componentHidden(final ComponentEvent e) {
        }

        @Override
        public Object getEventDetails(final Event event) {
            return Integer.valueOf(computeIconSize(currentArea));
        }

        public void dispatchEvents() {
            ED.dispatchEvents(this, Event.RESIZE_ICONS_STARTED);
            ED.dispatchEvents(this, Event.RESIZE_ICONS_FINISHED);
        }

        private boolean shouldResize(final int myArea, final int myPrevArea, final int referenceArea) {
            return myArea < referenceArea && myPrevArea >= referenceArea || myArea >= referenceArea && myPrevArea < referenceArea;
        }

        private int computeArea(final Dimension d) {
            return d.height * d.width;
        }

        private int computeIconSize(final int area) {
            return area > midlowSwitchoverArea ? area > midhighSwitchoverArea ? 128 : 64 : 32;
        }
    }
}

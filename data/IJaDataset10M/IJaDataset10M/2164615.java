package com.plarpebu.plugins.karaoke.cdg.basicplayer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.Timer;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import com.plarpebu.basicplayer.BasicPlayer;
import com.plarpebu.plugins.karaoke.cdg.basicplayer.gui.CdgOptionsDialog;
import com.plarpebu.plugins.karaoke.cdg.basicplayer.gui.ChooseFullScreenModeDIalog;
import com.plarpebu.plugins.karaoke.cdg.instructions.CdgBorderPreset;
import com.plarpebu.plugins.karaoke.cdg.instructions.CdgLoadColorTable;
import com.plarpebu.plugins.karaoke.cdg.instructions.CdgMemoryPreset;
import com.plarpebu.plugins.karaoke.cdg.instructions.CdgScrollCopy;
import com.plarpebu.plugins.karaoke.cdg.instructions.CdgScrollPreset;
import com.plarpebu.plugins.karaoke.cdg.instructions.CdgTileBlock;
import com.plarpebu.plugins.karaoke.cdg.io.CdgDataChunk;
import com.plarpebu.plugins.karaoke.cdg.io.CdgFileObject;
import com.plarpebu.plugins.karaoke.cdg.lyricspanel.CdgGraphicBufferedImage;
import com.plarpebu.plugins.sdk.FramePlugin;

/**
 * Basic CDG player window
 * 
 * @author Michel Buffa (buffa@unice.fr)
 */
public class BasicPlayerWindow extends FramePlugin implements BasicPlayerListener, ActionListener {

    private long tempsMp3;

    private BasicPlayer controllerMp3;

    private Timer timer;

    private int current_row;

    private CdgDataChunk[] cdgDataChunksArray;

    private Color[] colormap = new Color[16];

    private Rectangle damagedRectangle;

    private CdgGraphicBufferedImage panelLyrics = new CdgGraphicBufferedImage();

    private boolean windowedMode = true;

    private boolean cdgFileLoaded = false;

    private boolean pausedPlay = false;

    private boolean seeking = false;

    private int nbCdgInstructions = 10;

    private DisplayMode userDisplayMode = null;

    private int oldWindowDecorationStyle;

    private static DisplayMode[] BEST_DISPLAY_MODES = new DisplayMode[] { new DisplayMode(640, 480, 32, 0), new DisplayMode(640, 480, 16, 0), new DisplayMode(640, 480, 8, 0) };

    private GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();

    private GraphicsDevice myDevice = env.getDefaultScreenDevice();

    private DisplayMode oldDisplayMode = myDevice.getDisplayMode();

    private JPopupMenu jPopupMenu1 = new JPopupMenu();

    private JMenuItem jMenuItemHelp = new JMenuItem();

    private JMenuItem jMenuItemFullScreensOptions = new JMenuItem();

    private JMenuItem jMenuItemLyricsSyncOptions = new JMenuItem();

    private ChooseFullScreenModeDIalog chooseFullScreenDialog;

    private CdgOptionsDialog cdgOptionsDialog;

    private JMenuItem jMenuItemFullScreenWindow = new JMenuItem();

    private JMenuItem jMenuItemOneX = new JMenuItem();

    private JMenuItem jMenuItemTwoX = new JMenuItem();

    private JMenuItem jMenuItemThreeX = new JMenuItem();

    private int defaultWidth = 369;

    private int defaultHeight = 299;

    private int oldPosX;

    private int oldPosY;

    private int oldWidth;

    private int oldHeight;

    private boolean hardwareAcceleratedFullScreenModeSupported = false;

    private JCheckBoxMenuItem jCheckBoxMenuItemUseHardwareFullScreenMode = new JCheckBoxMenuItem();

    /**
	 * Constructor
	 */
    public BasicPlayerWindow() {
        super("CDG Lyrics Display");
        readPreferences();
        setUndecorated(true);
        Container cp = getContentPane();
        cp.add(panelLyrics, BorderLayout.CENTER);
        pack();
        addMouseListener(new PopupListener());
        addMouseListener(new MyMouseListener());
        addKeyListener(new MyKeyListener());
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        chooseFullScreenDialog = new ChooseFullScreenModeDIalog(this, true);
        cdgOptionsDialog = new CdgOptionsDialog(this, "Cdg options", true);
        loadPreferences();
    }

    private void jbInit() throws Exception {
        jMenuItemLyricsSyncOptions.setText("Lyrics Sync Options");
        jMenuItemLyricsSyncOptions.addActionListener(this);
        jMenuItemOneX.setText("1x");
        jMenuItemOneX.addActionListener(this);
        jMenuItemTwoX.setText("2x");
        jMenuItemTwoX.addActionListener(this);
        jMenuItemThreeX.setText("3x");
        jMenuItemThreeX.addActionListener(this);
        jCheckBoxMenuItemUseHardwareFullScreenMode.setText("Use HW Full Screen Mode (win+linux only)");
        jCheckBoxMenuItemUseHardwareFullScreenMode.addActionListener(this);
        jMenuItemFullScreensOptions.setText("Choose Fullscreen Resolution");
        jMenuItemFullScreensOptions.addActionListener(this);
        jMenuItemFullScreenWindow.setText("Fullscreen/Window");
        jMenuItemFullScreenWindow.addActionListener(this);
        jMenuItemHelp.setText("Help");
        jPopupMenu1.add(jMenuItemLyricsSyncOptions);
        jPopupMenu1.addSeparator();
        jPopupMenu1.add(jMenuItemOneX);
        jPopupMenu1.add(jMenuItemTwoX);
        jPopupMenu1.add(jMenuItemThreeX);
        jPopupMenu1.addSeparator();
        jPopupMenu1.add(jCheckBoxMenuItemUseHardwareFullScreenMode);
        jPopupMenu1.add(jMenuItemFullScreensOptions);
        jPopupMenu1.add(jMenuItemFullScreenWindow);
        jPopupMenu1.addSeparator();
        jPopupMenu1.add(jMenuItemHelp);
    }

    /**
	 * We redefine the inherited method so that visibility is always set to false before saving the
	 * preferences
	 */
    public void savePreferences() {
        System.out.println("dans pref red�finie");
        setVisible(false);
        super.savePreferences();
    }

    /**
	 * Load prefs
	 * 
	 * @throws NumberFormatException
	 * @throws HeadlessException
	 */
    private void loadPreferences() throws NumberFormatException, HeadlessException {
        try {
            int width = Integer.parseInt(preferences.getProperty("width"));
            int height = Integer.parseInt(preferences.getProperty("height"));
            setSize(width, height);
            String stringValue = null;
            Boolean value = null;
            if ((stringValue = preferences.getProperty("nbCdgInstructions")) != null) {
                setNbCdgInstructions(Integer.parseInt(stringValue));
                cdgOptionsDialog.setCdgBufferSize(Integer.parseInt(stringValue));
            }
            if ((stringValue = preferences.getProperty("redrawAllFrame")) != null) {
                value = Boolean.valueOf(stringValue);
                panelLyrics.setForceDrawFullImage(value.booleanValue());
                cdgOptionsDialog.setRedrawFullImage(value.booleanValue());
            }
            if ((stringValue = preferences.getProperty("hardwareAcceleratedFullScreenModeSupported")) != null) {
                value = Boolean.valueOf(stringValue);
                hardwareAcceleratedFullScreenModeSupported = value.booleanValue();
                setFullScreenModeOption();
            }
            int fsWidth = -1, fsHeight = -1, fsDepth = -1, fsFreq = -1;
            if ((stringValue = preferences.getProperty("fsWidth")) != null) {
                fsWidth = Integer.parseInt(stringValue);
            }
            if ((stringValue = preferences.getProperty("fsHeight")) != null) {
                fsHeight = Integer.parseInt(stringValue);
            }
            if ((stringValue = preferences.getProperty("fsDepth")) != null) {
                fsDepth = Integer.parseInt(stringValue);
            }
            if ((stringValue = preferences.getProperty("fsFreq")) != null) {
                fsFreq = Integer.parseInt(stringValue);
            }
            if ((fsWidth != -1) && (fsHeight != -1) && (fsDepth != -1) && (fsFreq != -1)) {
                System.out.println("Read prefs for fs mode, plug " + getName() + " : (" + fsWidth + "," + fsHeight + "," + fsDepth + "," + fsFreq + ")");
                userDisplayMode = new DisplayMode(fsWidth, fsHeight, fsDepth, fsFreq);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return "KaraokeCdg";
    }

    public String getVersion() {
        return "V1.0 beta 21";
    }

    /**
	 * Stop CDG only
	 */
    public void stopCdgOnly() {
        if (timer != null) {
            setVisible(false);
            timer.stop();
            cdgFileLoaded = false;
        }
    }

    public void setNbCdgInstructions(int nbCdgInstructions) {
        this.nbCdgInstructions = nbCdgInstructions;
    }

    /**
	 * Loads a cdg file whose basename is taken from the mp3File parameter
	 * 
	 * @param mp3File :
	 *           the name of the mp3File, its basename will be used for getting the cdg file
	 *           associated
	 */
    public void loadCdgFile(File mp3File) {
        try {
            int length = mp3File.getAbsolutePath().length();
            String cdgFileName = mp3File.getAbsolutePath().substring(0, length - 4) + ".cdg";
            File f = new File(cdgFileName);
            if (f.exists()) {
                System.out.println("Opening cdg file : " + cdgFileName);
                CdgFileObject cdg = new CdgFileObject(cdgFileName);
                cdgDataChunksArray = cdg.getCdgDataChunksArray();
                cdgFileLoaded = true;
                setVisible(true);
            } else {
                cdgFileLoaded = false;
                setVisible(false);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            cdgFileLoaded = false;
            setVisible(false);
        }
    }

    private void startTimedPlay() throws NumberFormatException {
        if (timer != null) timer.stop();
        current_row = 0;
        final int delay = (int) (nbCdgInstructions * 3.33);
        System.out.println("---We launch timer with delay = " + (nbCdgInstructions * 3.33) + "---");
        timer = new Timer(delay, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!pausedPlay) {
                    for (int i = 0; i < nbCdgInstructions; i++) {
                        playCdgInstruction();
                        current_row++;
                    }
                    if (tempsMp3 != 0) {
                        if ((current_row * 3.33) > (tempsMp3 + 100)) {
                            if (timer.getDelay() < delay) {
                                timer.setDelay(delay);
                            } else {
                                timer.setDelay(timer.getDelay() + 100);
                            }
                        } else if ((current_row * 3.33) < (tempsMp3 - 100)) {
                            if (timer.getDelay() > delay) {
                                timer.setDelay(delay);
                            } else {
                                if (timer.getDelay() > 0) {
                                    timer.setDelay(timer.getDelay() - 1);
                                }
                            }
                        }
                    }
                }
            }
        });
        timer.start();
    }

    public void playCdgInstruction() {
        boolean ret = false;
        if (cdgDataChunksArray[current_row].getCdgInstruction() == CdgDataChunk.CDG_LOAD_COL_TABLE_LOW) {
            CdgLoadColorTable.setColormap(cdgDataChunksArray[current_row].getCdgData(), 0, colormap);
            panelLyrics.setColormapLow(colormap);
        } else if (cdgDataChunksArray[current_row].getCdgInstruction() == CdgDataChunk.CDG_LOAD_COL_TABLE_HIGH) {
            CdgLoadColorTable.setColormap(cdgDataChunksArray[current_row].getCdgData(), 8, colormap);
            panelLyrics.setColormapHigh(colormap);
        } else if (cdgDataChunksArray[current_row].getCdgInstruction() == CdgDataChunk.CDG_TILE_NORMAL) {
            damagedRectangle = CdgTileBlock.drawTile(cdgDataChunksArray[current_row].getCdgData(), panelLyrics.getPixels(), false);
            panelLyrics.pixelsChanged(damagedRectangle);
        } else if (cdgDataChunksArray[current_row].getCdgInstruction() == CdgDataChunk.CDG_TILE_XOR) {
            damagedRectangle = CdgTileBlock.drawTile(cdgDataChunksArray[current_row].getCdgData(), panelLyrics.getPixels(), true);
            panelLyrics.pixelsChanged(damagedRectangle);
        } else if (cdgDataChunksArray[current_row].getCdgInstruction() == CdgDataChunk.CDG_MEMORY_PRESET) {
            if (CdgMemoryPreset.clearScreen(cdgDataChunksArray[current_row].getCdgData(), panelLyrics.getPixels())) {
                panelLyrics.pixelsChanged();
            }
        } else if (cdgDataChunksArray[current_row].getCdgInstruction() == CdgDataChunk.CDG_BORDER_PRESET) {
            CdgBorderPreset.drawBorder(cdgDataChunksArray[current_row].getCdgData(), panelLyrics.getPixels());
            panelLyrics.pixelsChanged();
        } else if (cdgDataChunksArray[current_row].getCdgInstruction() == CdgDataChunk.CDG_SCROLL_COPY) {
            panelLyrics.savePixels();
            ret = CdgScrollCopy.scroll(cdgDataChunksArray[current_row].getCdgData(), panelLyrics.getPixels());
            panelLyrics.pixelsChanged();
            if (!ret) panelLyrics.restorePixels();
        } else if (cdgDataChunksArray[current_row].getCdgInstruction() == CdgDataChunk.CDG_SCROLL_PRESET) {
            panelLyrics.savePixels();
            ret = CdgScrollPreset.scroll(cdgDataChunksArray[current_row].getCdgData(), panelLyrics.getPixels());
            panelLyrics.pixelsChanged();
            if (!ret) panelLyrics.restorePixels();
        }
    }

    /**
	 * Choose best disply mode. For full screen support.
	 */
    public static void chooseBestDisplayMode(GraphicsDevice device) {
        DisplayMode best = getBestDisplayMode(device);
        if (best != null) {
            device.setDisplayMode(best);
        }
    }

    /**
	 * Get best disply mode
	 * 
	 * @param device
	 * @return DisplayMode
	 */
    private static DisplayMode getBestDisplayMode(GraphicsDevice device) {
        for (int x = 0; x < BEST_DISPLAY_MODES.length; x++) {
            DisplayMode[] modes = device.getDisplayModes();
            for (int i = 0; i < modes.length; i++) {
                if (modes[i].getWidth() == BEST_DISPLAY_MODES[x].getWidth() && modes[i].getHeight() == BEST_DISPLAY_MODES[x].getHeight() && modes[i].getBitDepth() == BEST_DISPLAY_MODES[x].getBitDepth()) {
                    return BEST_DISPLAY_MODES[x];
                }
            }
        }
        return null;
    }

    /**
	 * Switch back and forth between full screen and window mode
	 */
    private void switchFullScreenWindowedMode() {
        if (windowedMode) {
            System.out.println("Go TO FULL SCREEN");
            try {
                goToFullScreen();
            } catch (Exception ex) {
                ex.printStackTrace();
                goToWindowedMode();
            }
        } else {
            goToWindowedMode();
        }
    }

    public void displayDecorations(boolean flag) {
        dispose();
        setUndecorated(!flag);
        pack();
    }

    /**
	 * Switch to full screen mode
	 */
    private void goToFullScreen() {
        if (!hardwareAcceleratedFullScreenModeSupported) return;
        oldPosX = getX();
        oldPosY = getY();
        oldWidth = getWidth();
        oldHeight = getHeight();
        if (windowedMode) {
            oldWindowDecorationStyle = getRootPane().getWindowDecorationStyle();
            getRootPane().setWindowDecorationStyle(JRootPane.NONE);
            myDevice.setFullScreenWindow(BasicPlayerWindow.this);
            if (myDevice.isDisplayChangeSupported()) {
                panelLyrics.setWindowedMode(false);
                if (userDisplayMode == null) {
                    chooseBestDisplayMode(myDevice);
                } else {
                    myDevice.setDisplayMode(userDisplayMode);
                }
            }
            controllerMp3.getPlayerUI().minimize();
            windowedMode = false;
        }
    }

    /**
	 * Switch to windowed mode
	 */
    private void goToWindowedMode() {
        if (!hardwareAcceleratedFullScreenModeSupported) return;
        if (!windowedMode) {
            myDevice.setDisplayMode(oldDisplayMode);
            myDevice.setFullScreenWindow(null);
            panelLyrics.setWindowedMode(true);
            getRootPane().setWindowDecorationStyle(oldWindowDecorationStyle);
            restorePositionAndSize();
            setVisible(true);
            controllerMp3.getPlayerUI().setToOriginalSize();
            windowedMode = true;
        }
    }

    private void restorePositionAndSize() {
        setLocation(oldPosX, oldPosY);
        setSize(oldWidth, oldHeight);
    }

    private class MyMouseListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) switchFullScreenWindowedMode();
        }
    }

    private class MyKeyListener extends KeyAdapter {

        /**
		 * Key pressed
		 */
        public void keyPressed(KeyEvent e) {
            System.out.println("Key Event: " + e.getKeyCode());
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) goToWindowedMode();
        }
    }

    private class PopupListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                jPopupMenu1.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    /**
	 * A handle to the BasicPlayer, plugins may control the player through the controller (play,
	 * stop, etc...)
	 * 
	 * @param controller :
	 *           a handle to the player
	 */
    public void setController(BasicController controller) {
        this.controllerMp3 = (BasicPlayer) controller;
    }

    /**
	 * Open callback, stream is ready to play. properties map includes audio format dependant
	 * features such as bitrate, duration, frequency, channels, number of frames, vbr flag, ...
	 * 
	 * @param stream
	 *           could be File, URL or InputStream
	 * @param properties
	 *           audio stream properties.
	 */
    public void opened(Object stream, Map properties) {
        if (!seeking) {
            String audiotype = null;
            if (stream != null) {
                if (stream instanceof File) {
                    System.out.println("File : " + ((File) stream).getAbsolutePath());
                    System.out.println("------------------");
                    System.out.println("Trying to Load cdg file...");
                    System.out.println("------------------");
                    loadCdgFile((File) stream);
                    if (!cdgFileLoaded) {
                        System.out.println("No Cdg file associated !");
                        return;
                    }
                    setVisible(true);
                } else if (stream instanceof URL) {
                    System.out.println("URL : " + ((URL) stream).toString());
                }
            }
            Iterator it = properties.keySet().iterator();
            StringBuffer jsSB = new StringBuffer();
            StringBuffer extjsSB = new StringBuffer();
            StringBuffer spiSB = new StringBuffer();
            if (properties.containsKey("audio.type")) {
                audiotype = ((String) properties.get("audio.type")).toLowerCase();
            }
            while (it.hasNext()) {
                String key = (String) it.next();
                Object value = properties.get(key);
                if (key.startsWith("audio")) {
                    jsSB.append(key + "=" + value + "\n");
                } else if (key.startsWith(audiotype)) {
                    spiSB.append(key + "=" + value + "\n");
                } else {
                    extjsSB.append(key + "=" + value + "\n");
                }
            }
            System.out.println(jsSB.toString());
            System.out.println(extjsSB.toString());
            System.out.println(spiSB.toString());
        }
    }

    /**
	 * Progress callback while playing. This method is called severals time per seconds while
	 * playing. properties map includes audio format features such as instant bitrate, microseconds
	 * position, current frame number, ...
	 * 
	 * @param bytesread
	 *           from encoded stream.
	 * @param microseconds
	 *           elapsed (<b>reseted after a seek !</b>).
	 * @param pcmdata
	 *           PCM samples.
	 * @param properties
	 *           audio stream parameters.
	 */
    public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties) {
        if (cdgFileLoaded) {
            tempsMp3 = microseconds / 1000;
        }
        if (seeking) {
            String value = "" + properties.get("mp3.position.microseconds");
            System.out.println(("---microseconds--- = " + value));
            tempsMp3 = Long.parseLong(value);
            tempsMp3 /= 1000;
            System.out.println("tempsMp3 = " + tempsMp3);
            System.out.println("currentRow avant " + current_row);
            current_row = (int) ((tempsMp3 / 3.33) + 0.5);
            seeking = false;
            System.out.println("-----");
            System.out.println("Current row recalcul�");
            System.out.println("currentRow apr�s " + current_row);
            System.out.println("-----");
        }
    }

    /**
	 * Notification callback of javazoom.jlgui.player.test state.
	 * 
	 * @param event
	 */
    public void stateUpdated(BasicPlayerEvent event) {
        System.out.println("RECU BASICPLAYEREVBNT = " + event.getCode());
        if (cdgFileLoaded) {
            if (event.getCode() == BasicPlayerEvent.PLAYING) {
                System.out.println("RECU BASICPLAYEREVBNT = PLAYING");
                if (!seeking) {
                    System.out.println("NOT SEEKING !");
                    startTimedPlay();
                }
            } else {
                if (event.getCode() == BasicPlayerEvent.STOPPED) {
                    System.out.println("RECU BASICPLAYEREVBNT STOPPED");
                    if (!seeking) {
                        System.out.println("ON ARRETE le cdg ! stopCdgOnly()");
                        stopCdgOnly();
                    }
                } else {
                    if (event.getCode() == BasicPlayerEvent.PAUSED) {
                        System.out.println("RECU BASICPLAYEREVBNT PAUSED");
                        pause();
                    } else if (event.getCode() == BasicPlayerEvent.RESUMED) {
                        System.out.println("RECU BASICPLAYEREVBNT RESUMED");
                        resume();
                    } else if (event.getCode() == BasicPlayerEvent.SEEKED) {
                        System.out.println("RECU BASICPLAYEREVBNT SEEKED");
                        seeking = true;
                    } else if (event.getCode() == BasicPlayerEvent.SEEKING) {
                        System.out.println("RECU BASICPLAYEREVBNT SEEKING");
                        seeking = true;
                    }
                }
            }
        }
    }

    private void pause() {
        pausedPlay = true;
    }

    private void resume() {
        pausedPlay = false;
    }

    /**
	 * getPlugin
	 * 
	 * @return BasicPlayerListener
	 */
    public BasicPlayerListener getPlugin() {
        return this;
    }

    private void setFullScreenModeOption() {
        preferences.setProperty("hardwareAcceleratedFullScreenModeSupported", "" + hardwareAcceleratedFullScreenModeSupported);
        jCheckBoxMenuItemUseHardwareFullScreenMode.setSelected(hardwareAcceleratedFullScreenModeSupported);
        jMenuItemFullScreensOptions.setEnabled(hardwareAcceleratedFullScreenModeSupported);
        jMenuItemFullScreenWindow.setEnabled(hardwareAcceleratedFullScreenModeSupported);
    }

    /**
	 * Popup actions
	 */
    public void actionPerformed(ActionEvent event) {
        if (event.getSource().equals(jMenuItemLyricsSyncOptions)) {
            goToWindowedMode();
            cdgOptionsDialog.setVisible(true);
            int nbCdgInstructions = cdgOptionsDialog.getCdgBufferSize();
            preferences.setProperty("nbCdgInstructions", "" + nbCdgInstructions);
            setNbCdgInstructions(nbCdgInstructions);
            panelLyrics.setForceDrawFullImage(cdgOptionsDialog.getRedrawFullImage());
            System.out.println("setting nbCdgInstruction to : " + nbCdgInstructions + ". Will take effect on next song played.");
            panelLyrics.redrawFullImage();
            preferences.setProperty("redrawAllFrame", "" + "" + cdgOptionsDialog.getRedrawFullImage());
        } else if (event.getSource().equals(jMenuItemOneX)) {
            goToWindowedMode();
            setSize(defaultWidth, defaultHeight);
            validate();
        } else if (event.getSource().equals(jMenuItemTwoX)) {
            goToWindowedMode();
            setSize(defaultWidth * 2, defaultHeight * 2);
            validate();
        } else if (event.getSource().equals(jMenuItemThreeX)) {
            goToWindowedMode();
            setSize(defaultWidth * 3, defaultHeight * 3);
            validate();
        } else if (event.getSource().equals(jCheckBoxMenuItemUseHardwareFullScreenMode)) {
            hardwareAcceleratedFullScreenModeSupported = !hardwareAcceleratedFullScreenModeSupported;
            setFullScreenModeOption();
        } else if (event.getSource().equals(jMenuItemFullScreensOptions)) {
            goToWindowedMode();
            chooseFullScreenDialog.setVisible(true);
            userDisplayMode = chooseFullScreenDialog.getSelectedDisplayMode();
            preferences.setProperty("fsWidth", "" + userDisplayMode.getWidth());
            preferences.setProperty("fsHeight", "" + userDisplayMode.getHeight());
            preferences.setProperty("fsDepth", "" + userDisplayMode.getBitDepth());
            preferences.setProperty("fsFreq", "" + userDisplayMode.getRefreshRate());
            panelLyrics.redrawFullImage();
        } else if (event.getSource().equals(jMenuItemFullScreenWindow)) {
            switchFullScreenWindowedMode();
        }
    }
}

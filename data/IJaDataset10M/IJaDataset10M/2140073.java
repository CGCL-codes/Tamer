package jshomeorg.simplytrain;

import com.l2fprod.common.swing.JTipOfTheDay;
import com.l2fprod.common.swing.TipModel;
import com.l2fprod.common.swing.tips.TipLoader;
import java.awt.*;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.BackingStoreException;
import javax.swing.JOptionPane;
import jshomeorg.simplytrain.service.*;
import jshomeorg.simplytrain.editor.*;
import jshomeorg.simplytrain.gui.*;
import jshomeorg.simplytrain.gui.panels.*;
import jshomeorg.event.*;
import jshomeorg.simplytrain.train.collection;

/**
 *
 * @author  js
 */
public class main extends javax.swing.JFrame {

    gamearea thegame = null;

    painter thepainter = null;

    collection alltraindata = null;

    boolean scrollerUsed = false;

    static boolean fullscreen = false;

    static final boolean doFullscreen = false;

    public static boolean isFullscreen() {
        return fullscreen;
    }

    /** Creates new form main
	 *
	 * @param _originalDM
	 */
    public main(DisplayMode _originalDM) {
        statics.setLookAndFeel("System", this);
        GregorianCalendar max = new GregorianCalendar(2011, 12, 31);
        if (max.before(new GregorianCalendar())) {
            JOptionPane.showMessageDialog(this, "Der Testzeitraum dieser Version von SimplyTrain ist abgelaufen!\nDas Programm wird nun beendet.\nBitte nach einer neuen Version auf http://www.js-home.org/SimplyTrain sehen.", "SimplyTrain: Testzeitraum abgelaufen!", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } else {
        }
        System.setProperty("sun.java2d.translaccel", "true");
        originalDM = _originalDM;
        initScreen();
        initComponents();
        if (fullscreen) {
            device.setFullScreenWindow(this);
            validate();
        }
        initAdditionalComponents();
        if (!fullscreen) {
            setSize(880, 500);
            setLocationRelativeTo(null);
        } else {
            setSize(new Dimension(originalDM.getWidth(), originalDM.getHeight()));
            validate();
        }
        dataCollector.collector.exceptionListeners.addListener(new AbstractListener() {

            @Override
            public void action(AbstractEvent e) {
                Exception ex = (Exception) e.getSource();
                System.out.println(ex.getMessage());
            }
        });
        setVisible(true);
        finishInit();
    }

    private void initComponents() {
        mainPanel = new javax.swing.JPanel();
        horiz_ScrollBar = new javax.swing.JScrollBar();
        vert_ScrollBar = new javax.swing.JScrollBar();
        toolPanel = new javax.swing.JPanel();
        sidePanel = new javax.swing.JPanel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconImage(statics.loadGUIImage("mainwindow.png"));
        setMinimumSize(new java.awt.Dimension(400, 139));
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        mainPanel.setLayout(new java.awt.BorderLayout());
        horiz_ScrollBar.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        horiz_ScrollBar.addAdjustmentListener(new java.awt.event.AdjustmentListener() {

            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {
                horiz_ScrollBarAdjustmentValueChanged(evt);
            }
        });
        mainPanel.add(horiz_ScrollBar, java.awt.BorderLayout.SOUTH);
        vert_ScrollBar.addAdjustmentListener(new java.awt.event.AdjustmentListener() {

            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {
                vert_ScrollBarAdjustmentValueChanged(evt);
            }
        });
        mainPanel.add(vert_ScrollBar, java.awt.BorderLayout.EAST);
        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);
        toolPanel.setLayout(new java.awt.BorderLayout());
        getContentPane().add(toolPanel, java.awt.BorderLayout.NORTH);
        sidePanel.setLayout(new java.awt.BorderLayout());
        sidePanel.setMinimumSize(new java.awt.Dimension(0, 0));
        getContentPane().add(sidePanel, java.awt.BorderLayout.EAST);
        pack();
    }

    private void vert_ScrollBarAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {
        scrollerUsed = true;
        thepainter.setY(vert_ScrollBar.getValue());
        scrollerUsed = false;
    }

    private void horiz_ScrollBarAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {
        scrollerUsed = true;
        thepainter.setX(horiz_ScrollBar.getValue());
        scrollerUsed = false;
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        dataCollector.collector.quitProgram();
    }

    MainBar mb;

    private void initAdditionalComponents() {
        dataCollector.collector.setMain(this);
        mb = new MainBar(getLayeredPane(), sidePanel);
        java.awt.event.ActionListener a = new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dataCollector.collector.setEditMode(mb.getSelected());
                dataCollector.collector.editorEvent(EditorEvent.TRACKOBJECT_UNSELECTED);
            }
        };
        thegame = new gamearea();
        mainPanel.add(thegame, java.awt.BorderLayout.CENTER);
        getContentPane().add(new StatusBar(), java.awt.BorderLayout.SOUTH);
        pack();
        alltraindata = new collection();
        thepainter = new painter(thegame);
        thegame.thepainter = thepainter;
        dataCollector.collector.setGame(thegame);
        dataCollector.collector.setPainter(thepainter);
        dataCollector.collector.setAllTrainData(alltraindata);
        mb.add("Datei", "File", new mainpanel_file(), new hidepanel_file(), a);
        mb.add("Gleise", "Edit Track", new mainpanel_edittrack(), new hidepanel_edittrack(), new sidepanel_edittrack(), a);
        thegame.addEditor("Edit Track", new trackeditor());
        mb.add("Gleisbl�cke", "Edit Trackgroup", new mainpanel_edittrackgroup(), new hidepanel_edittrackgroup(), a);
        thegame.addEditor("Edit Trackgroup", new trackgroupeditor());
        mb.add("Gleisobjekte", "Edit Trackobjects", new mainpanel_edittrackobjects(), new hidepanel_edittrackobjects(), MainBar.HIDEPTYPE_OVERLAY, new sidepanel_edittrackobjects(), a);
        thegame.addEditor("Edit Trackobjects", new trackobjectseditor());
        mb.add("Signale & Wege", "Edit Signal & Routes", new mainpanel_editsignal(), new hidepanel_editsignal(), MainBar.HIDEPTYPE_OVERLAY, new sidepanel_editsignal(), a);
        thegame.addEditor("Edit Signal & Routes", new patheditor());
        mb.add("Z�ge", "Edit Train", new mainpanel_edittrain(), null, new sidepanel_edittrain(), a);
        thegame.addEditor("Edit Train", new traineditor());
        mb.add("�ber", "About", new mainpanel_about(), new hidepanel_about(), a);
        mb.addFixed(new fixedpanel());
        toolPanel.add(mb);
        pack();
        dataCollector.collector.filenameListeners.addListener(new AbstractListener() {

            @Override
            public void action(AbstractEvent e) {
                String f = (String) e.getSource();
                if (f != null && f.length() > 0) {
                    setTitle("SimplyTrain: " + e.getSource());
                } else {
                    setTitle("SimplyTrain");
                }
            }
        });
        dataCollector.collector.setFilename("");
        dataCollector.collector.sizeOrPosChangedListeners.addListener(new AbstractListener() {

            @Override
            public void action(AbstractEvent e) {
                if (!scrollerUsed) {
                    horiz_ScrollBar.setValues(thepainter.getHSliderPos(), thepainter.getImgWidth(), thepainter.getHSliderMin(), thepainter.getHSliderMax());
                    horiz_ScrollBar.setUnitIncrement(thepainter.getTrackWidth() / 10);
                    vert_ScrollBar.setValues(thepainter.getVSliderPos(), thepainter.getImgHeight(), thepainter.getVSliderMin(), thepainter.getVSliderMax());
                    vert_ScrollBar.setUnitIncrement(thepainter.getTrackHeight() / 10);
                }
            }
        });
    }

    @Override
    public void pack() {
        super.pack();
    }

    private static GraphicsDevice device = null;

    private static DisplayMode originalDM = null;

    public static void recoverDisplay() {
        if (originalDM != null && fullscreen) {
            if (device.isDisplayChangeSupported()) {
                device.setDisplayMode(originalDM);
                device.setFullScreenWindow(null);
            }
        }
    }

    public GraphicsDevice getDevice() {
        return device;
    }

    private void initScreen() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = env.getScreenDevices();
        if (devices != null && devices.length > 0) {
            device = env.getDefaultScreenDevice();
            Thread shutdownthread = new Thread() {

                @Override
                public void run() {
                    main.recoverDisplay();
                }
            };
            if (originalDM == null) {
                originalDM = device.getDisplayMode();
            }
            Runtime.getRuntime().addShutdownHook(shutdownthread);
            if (doFullscreen) {
                boolean isFullScreen = device.isFullScreenSupported();
                fullscreen = isFullScreen && originalDM != null;
                setResizable(!fullscreen);
            }
        }
    }

    private void finishInit() {
        Timer tm = new Timer();
        tm.schedule(new TimerTask() {

            @Override
            public void run() {
                dataCollector.collector.startSim();
                dataCollector.collector.sizeOrPosChanged();
            }
        }, 1000);
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                alltraindata.load();
            }
        });
        thepainter.testIt();
        tipOfTheDay(false);
    }

    /**
	 * @param args the command line arguments
	 */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new main(null);
            }
        });
    }

    private javax.swing.JScrollBar horiz_ScrollBar;

    private javax.swing.JPanel mainPanel;

    private javax.swing.JPanel sidePanel;

    private javax.swing.JPanel toolPanel;

    private javax.swing.JScrollBar vert_ScrollBar;

    public void tipOfTheDay(boolean force) {
        Properties props = new Properties();
        try {
            props.load(getClass().getResourceAsStream("/jshomeorg/simplytrain/gui/resources/totd.properties"));
        } catch (IOException ex) {
            return;
        } catch (NullPointerException e) {
            dataCollector.collector.gotException(e);
        }
        TipModel tips = TipLoader.load(props);
        final JTipOfTheDay totd = new JTipOfTheDay(tips);
        int t = dataCollector.collector.prefs_totd.getInt("lasttip", 0);
        if ((t + 1) < totd.getModel().getTipCount()) {
            totd.setCurrentTip(t + 1);
        } else {
            totd.setCurrentTip(0);
        }
        if (force) {
            final JTipOfTheDay.ShowOnStartupChoice fake = new JTipOfTheDay.ShowOnStartupChoice() {

                @Override
                public boolean isShowingOnStartup() {
                    return totd.isShowingOnStartup(dataCollector.collector.prefs_totd);
                }

                @Override
                public void setShowingOnStartup(boolean showOnStartup) {
                    if (showOnStartup) {
                        totd.forceShowOnStartup(dataCollector.collector.prefs_totd);
                        try {
                            dataCollector.collector.prefs_totd.flush();
                        } catch (BackingStoreException e) {
                            dataCollector.collector.gotException(e);
                        }
                    }
                }
            };
            totd.showDialog(this, fake, true);
        } else {
            totd.showDialog(this, dataCollector.collector.prefs_totd);
        }
        dataCollector.collector.prefs_totd.putInt("lasttip", totd.getCurrentTip());
        try {
            dataCollector.collector.prefs_totd.flush();
        } catch (BackingStoreException e) {
            dataCollector.collector.gotException(e);
        }
    }
}

package javazoom.jlgui.player.amp;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Iterator;
import java.util.List;
import java.util.logging.LogManager;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.player.amp.skin.DragAdapter;
import javazoom.jlgui.player.amp.skin.Skin;
import javazoom.jlgui.player.amp.util.Config;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StandalonePlayer extends JFrame implements Loader {

    private static Log log = LogFactory.getLog(StandalonePlayer.class);

    private String initConfig = "jlgui.ini";

    private String initSong = null;

    private String showPlaylist = null;

    private String showEqualizer = null;

    private String showDsp = null;

    private String skinPath = null;

    private String skinVersion = "1";

    private boolean autoRun = false;

    private PlayerUI mp = null;

    private JPanel eqWin = null;

    private JPanel plWin = null;

    private int eqFactor = 2;

    private Config config = null;

    private int panelCount = 0;

    public StandalonePlayer() {
        super();
        try {
            LogManager.getLogManager().readConfiguration(this.getClass().getResourceAsStream("/javazoom/jlgui/player/amp/logging.properties"));
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        final StandalonePlayer player = new StandalonePlayer();
        player.parseParameters(args);
        player.loadUI();
        player.loadJS();
        player.loadPlaylist();
        player.boot();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                player.registerRemotePlayer();
            }

            ;
        });
    }

    private static void configurePLAF() {
        try {
            UIManager.setLookAndFeel(com.jgoodies.looks.windows.WindowsLookAndFeel.class.getName());
            com.jgoodies.looks.Options.setPopupDropShadowEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize the player front-end.
     * 
     * @param args
     */
    private void parseParameters(String[] args) {
        String currentArg = null;
        String currentValue = null;
        for (int i = 0; i < args.length; i++) {
            currentArg = args[i];
            if (currentArg.startsWith("-")) {
                if (currentArg.toLowerCase().equals("-init")) {
                    i++;
                    if (i >= args.length) {
                        usage("init value missing");
                    }
                    currentValue = args[i];
                    if (Config.startWithProtocol(currentValue)) {
                        initConfig = currentValue;
                    } else {
                        initConfig = currentValue.replace('\\', '/').replace('/', java.io.File.separatorChar);
                    }
                } else if (currentArg.toLowerCase().equals("-song")) {
                    i++;
                    if (i >= args.length) {
                        usage("song value missing");
                    }
                    currentValue = args[i];
                    if (Config.startWithProtocol(currentValue)) {
                        initSong = currentValue;
                    } else {
                        initSong = currentValue.replace('\\', '/').replace('/', java.io.File.separatorChar);
                    }
                } else if (currentArg.toLowerCase().equals("-start")) {
                    autoRun = true;
                } else if (currentArg.toLowerCase().equals("-showplaylist")) {
                    showPlaylist = "true";
                } else if (currentArg.toLowerCase().equals("-showequalizer")) {
                    showEqualizer = "true";
                } else if (currentArg.toLowerCase().equals("-disabledsp")) {
                    showDsp = "false";
                } else if (currentArg.toLowerCase().equals("-skin")) {
                    i++;
                    if (i >= args.length) {
                        usage("skin value missing");
                    }
                    currentValue = args[i];
                    if (Config.startWithProtocol(currentValue)) {
                        skinPath = currentValue;
                    } else {
                        skinPath = currentValue.replace('\\', '/').replace('/', java.io.File.separatorChar);
                    }
                } else if (currentArg.toLowerCase().equals("-v")) {
                    i++;
                    if (i >= args.length) {
                        usage("skin version value missing");
                    }
                    skinVersion = args[i];
                } else {
                    usage("Unknown parameter : " + currentArg);
                }
            } else {
                usage("Invalid parameter :" + currentArg);
            }
        }
    }

    public void boot() {
        if (autoRun == true) {
            mp.pressStart();
        }
    }

    /**
     * Instantiate low-level player.
     */
    public void loadJS() {
        BasicPlayer bplayer = new BasicPlayer();
        List mixers = bplayer.getMixers();
        if (mixers != null) {
            Iterator it = mixers.iterator();
            String mixer = config.getAudioDevice();
            boolean mixerFound = false;
            if ((mixer != null) && (mixer.length() > 0)) {
                while (it.hasNext()) {
                    if (((String) it.next()).equals(mixer)) {
                        bplayer.setMixerName(mixer);
                        mixerFound = true;
                        break;
                    }
                }
            }
            if (mixerFound == false) {
                it = mixers.iterator();
                if (it.hasNext()) {
                    mixer = (String) it.next();
                    bplayer.setMixerName(mixer);
                }
            }
        }
        bplayer.addBasicPlayerListener(mp);
        mp.setController(bplayer);
    }

    /**
     * Load playlist.
     */
    public void loadPlaylist() {
        if ((initSong != null) && (!initSong.equals(""))) {
            mp.loadPlaylist(initSong);
        } else {
            mp.loadPlaylist(config.getPlaylistFilename());
        }
        plWin.repaint();
    }

    /**
     * Load player front-end.
     */
    public void loadUI() {
        if (isWindowsOs()) {
            configurePLAF();
        }
        config = Config.getInstance();
        config.load(initConfig);
        config.setTopParent(this);
        if (showPlaylist != null) {
            if (showPlaylist.equalsIgnoreCase("true")) {
                config.setPlaylistEnabled(true);
            } else {
                config.setPlaylistEnabled(false);
            }
        }
        if (showEqualizer != null) {
            if (showEqualizer.equalsIgnoreCase("true")) {
                config.setEqualizerEnabled(true);
            } else {
                config.setEqualizerEnabled(false);
            }
        }
        if (config.isPlaylistEnabled()) {
            eqFactor = 2;
        } else {
            eqFactor = 1;
        }
        setTitle(Skin.TITLETEXT);
        ClassLoader cl = this.getClass().getClassLoader();
        URL iconURL = cl.getResource("javazoom/jlgui/player/amp/jlguiicon.png");
        if (iconURL != null) {
            ImageIcon jlguiIcon = new ImageIcon(iconURL);
            setIconImage(jlguiIcon.getImage());
            config.setIconParent(jlguiIcon);
        }
        setUndecorated(true);
        mp = new PlayerUI();
        if ((showDsp != null) && (showDsp.equalsIgnoreCase("false"))) {
            mp.getSkin().setDspEnabled(false);
        }
        if (skinPath != null) {
            mp.getSkin().setPath(skinPath);
        }
        mp.getSkin().setSkinVersion(skinVersion);
        mp.loadUI(this);
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        setContentPane(p);
        addUI(mp);
        eqWin = mp.getEqualizerUI();
        eqWin.setVisible(false);
        eqWin.setDoubleBuffered(true);
        Dimension dim = eqWin.getPreferredSize();
        eqWin.setPreferredSize(new Dimension(dim.width, dim.height + 2));
        if (config.isEqualizerEnabled()) {
            addUI(eqWin);
        }
        plWin = mp.getPlaylistUI();
        plWin.setVisible(false);
        plWin.setDoubleBuffered(true);
        if (config.isPlaylistEnabled()) {
            addUI(plWin);
        }
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                close();
            }
        });
        setKeyBoardShortcut();
        setLocation(config.getXLocation(), config.getYLocation());
        setVisible(true);
        if (config.isPlaylistEnabled()) {
            plWin.setVisible(true);
        }
        if (config.isEqualizerEnabled()) {
            eqWin.setVisible(true);
        }
    }

    /**
     * Install keyboard shortcuts.
     */
    public void setKeyBoardShortcut() {
        KeyStroke jKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_J, 0, false);
        KeyStroke ctrlPKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK, false);
        KeyStroke altSKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.ALT_MASK, false);
        KeyStroke vKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_V, 0, false);
        String searchID = "TAGSEARCH";
        String preferenceID = "PREFERENCES";
        String skinbrowserID = "SKINBROWSER";
        String stopplayerID = "STOPPLAYER";
        Action searchAction = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                if (mp != null) {
                    mp.processJumpToFile(e.getModifiers());
                }
            }
        };
        Action preferencesAction = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                if (mp != null) {
                    mp.processPreferences(e.getModifiers());
                }
            }
        };
        Action skinbrowserAction = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                if (mp != null) {
                    mp.processSkinBrowser(e.getModifiers());
                }
            }
        };
        Action stopplayerAction = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                if (mp != null) {
                    mp.processStop(MouseEvent.BUTTON1_MASK);
                }
            }
        };
        setKeyboardAction(searchID, jKeyStroke, searchAction);
        setKeyboardAction(preferenceID, ctrlPKeyStroke, preferencesAction);
        setKeyboardAction(skinbrowserID, altSKeyStroke, skinbrowserAction);
        setKeyboardAction(stopplayerID, vKeyStroke, stopplayerAction);
    }

    /**
     * Set keyboard key shortcut for the whole player.
     * 
     * @param id
     * @param key
     * @param action
     */
    public void setKeyboardAction(String id, KeyStroke key, Action action) {
        mp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(key, id);
        mp.getActionMap().put(id, action);
        mp.getPlaylistUI().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(key, id);
        mp.getPlaylistUI().getActionMap().put(id, action);
        mp.getEqualizerUI().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(key, id);
        mp.getEqualizerUI().getActionMap().put(id, action);
    }

    public void loaded() {
        DragAdapter dragAdapter = new DragAdapter(this);
        mp.getSkin().getAcTitleBar().addMouseListener(dragAdapter);
        mp.getSkin().getAcTitleBar().addMouseMotionListener(dragAdapter);
    }

    public void close(long delay) {
        try {
            Thread.currentThread().sleep(delay);
        } catch (InterruptedException ex) {
            log.error(ex);
        }
        close();
    }

    public void close() {
        log.info("Closing player");
        config.setLocation(getLocation().x, getLocation().y);
        config.save();
        dispose();
        exit(0);
    }

    public void togglePlaylist(boolean enabled) {
        if (plWin != null) {
            if (enabled) {
                if (config.isEqualizerEnabled()) {
                    eqFactor = 2;
                }
                addUI(plWin);
            } else {
                removeUI(plWin);
                if (config.isEqualizerEnabled()) {
                    eqFactor = 1;
                }
            }
        }
    }

    public void toggleEqualizer(boolean enabled) {
        if (eqWin != null) {
            if (enabled) {
                if (config.isPlaylistEnabled()) {
                    eqFactor = 2;
                } else {
                    eqFactor = 1;
                }
                addUI(eqWin);
            } else {
                removeUI(eqWin);
            }
        }
    }

    private void addUI(JPanel panel) {
        setSize(new Dimension(mp.getSkin().getMainWidth(), (++panelCount) * mp.getSkin().getMainHeight()));
        this.getContentPane().add(panel);
        panel.setVisible(true);
    }

    private void removeUI(JPanel panel) {
        panel.setVisible(false);
        setSize(new Dimension(mp.getSkin().getMainWidth(), (--panelCount) * mp.getSkin().getMainHeight()));
        this.getContentPane().remove(panel);
    }

    public void minimize() {
        setState(JFrame.ICONIFIED);
    }

    public void setLocation(int x, int y) {
        super.setLocation(x, y);
    }

    public Point getLocation() {
        return super.getLocation();
    }

    /**
     * Kills the player.
     * 
     * @param status
     */
    public void exit(int status) {
        System.exit(status);
    }

    /**
     * Displays usage.
     * 
     * @param msg
     */
    protected static void usage(String msg) {
        System.out.println(Skin.TITLETEXT + " : " + msg);
        System.out.println("");
        System.out.println(Skin.TITLETEXT + " : Usage");
        System.out.println("              java javazoom.jlgui.player.amp.Player [-skin skinFilename] [-song audioFilename] [-start] [-showplaylist] [-showequalizer] [-disabledsp] [-init configFilename] [-v skinversion]");
        System.out.println("");
        System.out.println("              skinFilename   : Filename or URL to a Winamp Skin2.x");
        System.out.println("              audioFilename  : Filename or URL to initial song or playlist");
        System.out.println("              start          : Starts playing song (from the playlist)");
        System.out.println("              showplaylist   : Show playlist");
        System.out.println("              showequalizer  : Show equalizer");
        System.out.println("              disabledsp     : Disable spectrum/time visual");
        System.out.println("");
        System.out.println("              Advanced parameters :");
        System.out.println("              skinversion    : 1 or 2 (default 1)");
        System.out.println("              configFilename : Filename or URL to jlGui initial configuration (playlist,skin,parameters ...)");
        System.out.println("                               Initial configuration won't be overriden by -skin and -song arguments");
        System.out.println("");
        System.out.println("Homepage    : http://www.javazoom.net");
        System.exit(0);
    }

    public void registerRemotePlayer() {
        try {
            String name = "player";
            RemotePlayerImpl stub = new RemotePlayerImpl(this, mp);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            log.info("********Remoteplayer bound*********");
        } catch (Exception e) {
            log.error("Remoteplayer exception", e);
        }
    }

    public static boolean isWindowsOs() {
        return System.getProperty("os.name").toLowerCase().startsWith("windows");
    }

    public static boolean isLinuxOs() {
        return System.getProperty("os.name").toLowerCase().startsWith("linux");
    }
}

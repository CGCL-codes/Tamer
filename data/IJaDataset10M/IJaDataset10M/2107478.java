package com.plarpebu;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import com.plarpebu.basicplayer.BasicPlayer;
import com.plarpebu.basicplayer.CompositePlayer;
import com.plarpebu.plugins.basic.info.InfoPlugin;
import com.plarpebu.plugins.basic.pan_gain.PanGainPlugin;
import com.plarpebu.plugins.basic.stop_play_seek.StopPlaySeekPlugin;
import com.plarpebu.plugins.sdk.FramePlugin;
import com.plarpebu.plugins.sdk.Iconifiable;
import com.plarpebu.plugins.sdk.JFrameWithPreferences;
import com.plarpebu.plugins.sdk.PanelPlugin;
import com.plarpebu.plugins.sdk.PlayerPlugin;
import com.plarpebu.util.ExitListenerSecurityManager;
import fr.unice.plugin.PluginManager;

/**
 * Main Player
 * 
 * @author Pepino, Arnaud
 * @version $Id
 */
public class Player extends JFrameWithPreferences implements DropTargetListener, MouseMotionListener, ComponentListener, Iconifiable {

    /************************************************************************************************
	 * un tableau compose des noms des plugins que l'on veut afficher a l'origine et dont on souhaite
	 * leur affichage permanent
	 ***********************************************************************************************/
    public static final ArrayList<String> origine = new ArrayList<String>();

    static {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        origine.add(PanGainPlugin.NAME);
        origine.add(StopPlaySeekPlugin.NAME);
        origine.add(InfoPlugin.NAME);
    }

    SplashScreen splashScreen = new SplashScreen();

    private PluginManager pluginManager;

    private PlayerPlugin[] plugins;

    private PluginMenuItemFactory pluginMenuItemFactory;

    /** le ContentPane de l'interface principale */
    private Container pane;

    /** la JMenuBar de l'interface principale */
    private JMenuBar mb = new JMenuBar();

    private FramePlugin playlist;

    private ArrayList listComponent = new ArrayList();

    private BasicPlayer controller = null;

    private CompositePlayer bplayer = null;

    private JMenu menuPlugins = null;

    private static boolean playlistActivate = false;

    private ExitListenerSecurityManager sm;

    /**
	 * La fonction main : point d'entree du lecteur. Elle lance le Player.
	 */
    public static void main(String[] args) throws MalformedURLException {
        try {
            Player player = new Player();
            player.setVisible(true);
        } catch (Throwable ex) {
            System.out.println("Error in main: " + ex.toString());
            ex.printStackTrace();
        }
    }

    /**
	 * L'unique constructeur de Player. Il se charge de construire l'interface principale de notre
	 * lecteur.
	 * 
	 * @throws MalformedUrlException
	 */
    public Player() throws MalformedURLException {
        super("Plarpebu v1.0 Final");
        setPreferencesFileNames("preferences", "Plaperbu.properties", "defaultPlaperbu.properties");
        readPreferences();
        SkinMgr.getInstance().setSkin(preferences.getProperty("lastSkin"));
        System.setSecurityManager(sm = new ExitListenerSecurityManager());
        sm.addSystemExitListener(this);
        splashScreen.showSplash();
        Logger.getLogger("fr.unice.plugin").setLevel(Level.ALL);
        pluginManager = PluginManager.getPluginManager();
        pluginManager.addPluginManagerListener(splashScreen);
        pluginManager.addJarURLsInDirectories(new URL[] { new URL("file:plugins") });
        pluginManager.loadPlugins();
        plugins = (PlayerPlugin[]) pluginManager.getPluginInstances(PlayerPlugin.class);
        pane = this.getContentPane();
        pane.setLayout(new GridLayout(0, 1));
        preparationPourQuitter();
        addMouseMotionListener(this);
        addComponentListener(this);
        setJMenuBar(mb);
        bplayer = new CompositePlayer();
        controller = bplayer;
        buildPluginMenu();
        buildChargementMenu();
        buildSkinMenu();
        buildHelpMenu();
        buildUI();
        new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this, true);
        splashScreen.close();
        activationOrigine();
        restoreVisibility();
    }

    /**
	 * Simple procedure preparant notre interface a l'operation de fermeture de la fenetre
	 * principale.<br>
	 * Affiche un une fenetre de dialogue pour confirmer le souhait de l'utilisateur a quitter
	 * l'application.<br>
	 * Ne provoque rien si l'utilisateur clique sur NON.<br>
	 * Provoque la fin de l'application impliquant la sauvegarde de la derniere configuration s'il
	 * clique sur OUI
	 */
    private void preparationPourQuitter() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                Object[] options = { "YES", "NO" };
                int rep = JOptionPane.showOptionDialog(null, "Do you really want to quit ?", "Quit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (rep == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }

    /**
	 * activationOrigine va permettre d'afficher des le lancement les plugins correspondant au
	 * tableau prive static origine. Elle met egalement a jour la liste des cases de plugins actives
	 * dans le menu. Notons que ceux-ci seront en affichage permanent puisque l'on bloque ensuite la
	 * possibilite de cacher le plugin. L'utilisateur desirant en rajouter d'autres en affichage
	 * permanent n'aurait simplement qu'a modifier dans le code le tableau origine en rajoutant la
	 * String correspondant au plugin . Par exemple en rajoutant origine.add("PlayList") dans le bloc
	 * static, la playliste deviendrait un plugin automatiquement lance a chaque session sans avoir
	 * la possibilite de decocher la case correspondante.
	 */
    private void activationOrigine() {
        bplayer.setPlayerUI(this);
        JMenu men = pluginMenuItemFactory.getMenu();
        bplayer.setPlayerUI(this);
        if (playlist == null) playlist = (FramePlugin) searchPlugin("PlayList");
        for (int i = 0; i < plugins.length; i++) {
            bplayer.addBasicPlayerListener((BasicPlayerListener) plugins[i]);
            plugins[i].setController(bplayer);
            if (plugins[i] instanceof FramePlugin) {
                sm.addSystemExitListener((FramePlugin) plugins[i]);
                ((FramePlugin) (plugins[i])).restoreVisibility();
            }
        }
        String namePlug;
        for (int i = 0; i < origine.size(); i++) {
            namePlug = (String) origine.get(i);
            PlayerPlugin pp = searchPlugin(namePlug);
            if (!(namePlug.equals(PanGainPlugin.NAME) || namePlug.equals(StopPlaySeekPlugin.NAME) || namePlug.equals(InfoPlugin.NAME))) {
                if (pp instanceof FramePlugin) {
                    ((FramePlugin) pp).setLocation(getX(), getY() + getHeight());
                    ((FramePlugin) pp).setVisible(true);
                } else if (pp instanceof PanelPlugin) {
                    pane.add((Component) pp);
                    pane.validate();
                }
            }
            ((JCheckBoxMenuItem) men.getItem(getPluginIndex(pp))).setState(true);
            ((JCheckBoxMenuItem) men.getItem(getPluginIndex(pp))).setEnabled(false);
        }
    }

    public int getPluginIndex(PlayerPlugin pp) {
        for (int i = 0; i < plugins.length; i++) {
            if (plugins[i] == pp) return i;
        }
        return -1;
    }

    /**
	 * La fonction searchPlugin est une fonction privee a Player. Elle va permettre de retrouver un
	 * playerPugin en fonction de son nom.
	 * 
	 * @param name
	 *           La String correspondant au nom du plugin.
	 * @return le playerPlugin correspondant.
	 */
    private PlayerPlugin searchPlugin(String name) {
        for (int i = 0; i < plugins.length; i++) {
            if (plugins[i].getName().equals(name)) return plugins[i];
        }
        return null;
    }

    /**
	 * construit le menu liee aux skins si le repertoire skins est present et qu'il n'y a pas de pbs.
	 */
    private void buildSkinMenu() {
        try {
            ActionListener skinListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    SkinMgr.getInstance().setSkin(e.getActionCommand());
                    preferences.setProperty("lastSkin", e.getActionCommand());
                }
            };
            JMenu menuSkins = new JMenu("Skins");
            List<String> skinNames = SkinMgr.getInstance().getSkinNames();
            for (String skinName : skinNames) {
                JMenuItem menuItem = new JMenuItem(skinName);
                menuItem.addActionListener(skinListener);
                menuSkins.add(menuItem);
            }
            mb.add(menuSkins);
        } catch (Exception ex) {
            System.out.println("Error building skins menu" + ex.toString());
        }
    }

    /**
	 * Build about menu.
	 */
    private void buildHelpMenu() {
        final JMenu menuHelp = new JMenu("Help");
        try {
            ActionListener aboutListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(menuHelp, "           Open Source Java Karaoke Player\n\nhttp://miageprojet.unice.fr/twiki/bin/view/Fun/JavaKarPlayer\n\nCurrent Developers:\n   Michel Buffa (micbuffa@gmail.com)\n   Kevin Schmidt (kevin.m.schmidt@fuse.net)\n\nLicense: GNU General Public License (GPL)", "About Plarpebu", JOptionPane.PLAIN_MESSAGE);
                }
            };
            JMenuItem menuItem = new JMenuItem("About");
            menuItem.addActionListener(aboutListener);
            menuHelp.add(menuItem);
            mb.add(menuHelp);
        } catch (Exception ex) {
            System.out.println("Error building skins menu" + ex.toString());
        }
    }

    private void buildPluginMenu() {
        menuPlugins = new JMenu("Plugins");
        ActionListener listener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JCheckBoxMenuItem cb = (JCheckBoxMenuItem) e.getSource();
                String nameCommand = cb.getActionCommand();
                System.out.println(" : " + nameCommand);
                PlayerPlugin pp = searchPlugin(nameCommand);
                if (cb.getState() == true) {
                    bplayer.addBasicPlayerListener(pp.getPlugin());
                    pp.setController(controller);
                    if (pp instanceof FramePlugin) {
                        ((FramePlugin) pp).setVisible(true);
                    } else if (pp instanceof PanelPlugin) {
                        pane.add((Component) pp);
                        pane.validate();
                    }
                } else {
                    if (pp instanceof FramePlugin) {
                        ((FramePlugin) pp).setVisible(false);
                    } else if (pp instanceof PanelPlugin) {
                        pane.remove((Component) pp);
                        pane.validate();
                    }
                }
            }
        };
        if (pluginMenuItemFactory == null) {
            pluginMenuItemFactory = new PluginMenuItemFactory(menuPlugins, pluginManager, listener);
        }
        buildPluginMenuEntries();
        mb.add(menuPlugins);
        startTimedPluginMenyUpdate();
    }

    private void startTimedPluginMenyUpdate() {
        javax.swing.Timer timer = new javax.swing.Timer(500, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < plugins.length; i++) {
                    if (plugins[i] instanceof FramePlugin) {
                        PluginMenuItem item = (PluginMenuItem) menuPlugins.getItem(i);
                        item.setState(((FramePlugin) plugins[i]).isVisible());
                    }
                }
            }
        });
        timer.start();
    }

    private void buildPluginMenuEntries() {
        pluginMenuItemFactory.buildMenu(null);
    }

    /**
	 * Cette procedure construit le menu pour recharger les plugins.
	 */
    private void buildChargementMenu() {
        JMenu menuCharg = new JMenu("Reload");
        JMenuItem menuItem = new JMenuItem("Reload Plugins");
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                pluginManager.loadPlugins();
                plugins = (PlayerPlugin[]) pluginManager.getPluginInstances(PlayerPlugin.class);
                buildPluginMenuEntries();
                activationOrigine();
            }
        });
        menuCharg.add(menuItem);
        mb.add(menuCharg);
    }

    private void buildUI() {
        InfoPlugin listenerInfo = (InfoPlugin) searchPlugin(InfoPlugin.NAME);
        listenerInfo.setController(controller);
        bplayer.addBasicPlayerListener(listenerInfo.getPlugin());
        pane.add(listenerInfo);
        pane.validate();
        Thread t = new Thread(listenerInfo);
        t.start();
        Box panelUI = Box.createHorizontalBox();
        StopPlaySeekPlugin listenerSPS = (StopPlaySeekPlugin) searchPlugin(StopPlaySeekPlugin.NAME);
        listenerSPS.setController(bplayer);
        bplayer.addBasicPlayerListener(listenerSPS.getPlugin());
        PanGainPlugin listenerPG = (PanGainPlugin) searchPlugin(PanGainPlugin.NAME);
        listenerPG.setController(controller);
        bplayer.addBasicPlayerListener(listenerPG.getPlugin());
        panelUI.add(listenerSPS);
        panelUI.add(listenerPG);
        pane.add(panelUI);
    }

    public void displayComponent() {
        int taille = listComponent.size();
        pane.removeAll();
        for (int i = 0; i < taille; i++) {
            pane.add((JComponent) listComponent.get(i));
            ((JComponent) listComponent.get(i)).revalidate();
            repaint();
        }
    }

    /**
	 * Fonction impl�ment�e pour l'interface mais non utile ici.
	 * 
	 * @param e
	 *           un evenement engendr� par la souris.
	 */
    public void mouseDragged(MouseEvent e) {
    }

    /**
	 * Fonction impl�ment�e pour l'interface mais non utile ici.
	 * 
	 * @param e
	 *           un evenement engendr� par la souris.
	 */
    public void mouseMoved(MouseEvent e) {
    }

    /**
	 * dragEnter
	 * 
	 * @param e
	 *           DropTargetDragEvent
	 */
    public void dragEnter(DropTargetDragEvent e) {
        if (isDragOk(e) == false) {
            e.rejectDrag();
            return;
        }
    }

    /**
	 * dragOver
	 * 
	 * @param e
	 *           DropTargetDragEvent
	 */
    public void dragOver(DropTargetDragEvent e) {
        if (isDragOk(e) == false) {
            e.rejectDrag();
            return;
        }
    }

    /**
	 * Fonction impl�ment�e pour l'interface mais non utile ici.
	 * 
	 * @param e
	 *           un DropTargetEvent.
	 */
    public void dragExit(DropTargetEvent e) {
    }

    /**
	 * dropActionChanged
	 * 
	 * @param e
	 *           DropTargetDragEvent
	 */
    public void dropActionChanged(DropTargetDragEvent e) {
        if (isDragOk(e) == false) {
            e.rejectDrag();
            return;
        }
    }

    /**
	 * ajoutes les fichiers � la playlist lors du drop nous pouvons ajouter un ou plusieurs fichier
	 * venant de l'exterieur ainsi que des playlist
	 * 
	 * @param e
	 *           DropTargetDropEvent
	 */
    public void drop(DropTargetDropEvent e) {
        DataFlavor[] dfs = e.getCurrentDataFlavors();
        DataFlavor tdf = null;
        for (int i = 0; i < dfs.length; i++) {
            if (DataFlavor.javaFileListFlavor.equals(dfs[i]) || DataFlavor.stringFlavor.equals(dfs[i])) {
                tdf = dfs[i];
                break;
            }
        }
        if (tdf != null) {
            if ((e.getSourceActions() & DnDConstants.ACTION_COPY_OR_MOVE) != 0) {
                e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
            } else {
                return;
            }
            try {
                Transferable t = e.getTransferable();
                Object data = t.getTransferData(tdf);
                if (data instanceof java.util.List) {
                    java.util.List al = (java.util.List) data;
                    if (al.size() > 0) {
                        File file = null;
                        ListIterator li = al.listIterator();
                        while (li.hasNext()) {
                            file = (File) li.next();
                            if (file != null && controller.isFileSupported(file)) {
                            }
                        }
                    }
                } else if (data instanceof java.lang.String) {
                    System.out.println("on essaie d'ajouter ");
                }
            } catch (IOException ioe) {
                e.dropComplete(false);
                return;
            } catch (UnsupportedFlavorException ufe) {
                e.dropComplete(false);
                return;
            } catch (Exception ex) {
                e.dropComplete(false);
                return;
            }
            e.dropComplete(true);
        }
    }

    /**
	 * isDragOk : v�rifie si le DnD est possible il est possible si on essaie de "droper" soit un
	 * fichier soit une String
	 * 
	 * @param e
	 *           DropTargetDragEvent
	 * @return boolean
	 */
    private boolean isDragOk(DropTargetDragEvent e) {
        DataFlavor[] dfs = e.getCurrentDataFlavors();
        DataFlavor tdf = null;
        for (int i = 0; i < dfs.length; i++) {
            if (DataFlavor.javaFileListFlavor.equals(dfs[i]) || DataFlavor.stringFlavor.equals(dfs[i])) {
                tdf = dfs[i];
                break;
            }
        }
        if (tdf != null) {
            if ((e.getSourceActions() & DnDConstants.ACTION_COPY_OR_MOVE) != 0) {
                return true;
            }
            return false;
        }
        return false;
    }

    /**
	 * Fonction impl�ment�e pour l'interface mais non utile ici.
	 * 
	 * @param e
	 *           un ComponentEvent.
	 */
    public void componentHidden(ComponentEvent e) {
    }

    /**
	 * Fonction impl�ment�e pour l'interface mais non utile ici.
	 * 
	 * @param e
	 *           un ComponentEvent.
	 */
    public void componentMoved(ComponentEvent e) {
    }

    /**
	 * componentResized
	 * 
	 * @param e
	 *           ComponentEvent
	 */
    public void componentResized(ComponentEvent e) {
    }

    /**
	 * Fonction impl�ment�e pour l'interface mais non utile ici.
	 * 
	 * @param e
	 *           un ComponentEvent.
	 */
    public void componentShown(ComponentEvent e) {
    }

    public static boolean isActivate() {
        return playlistActivate;
    }

    public void minimize() {
        System.out.println("---hide()---");
        setVisible(false);
        if (playlist != null) {
            System.out.println("We hide the playlist");
            playlist.setVisible(false);
        }
    }

    public void setToOriginalSize() {
        System.out.println("---show()---");
        setVisible(true);
        if (playlist != null) playlist.setVisible(true);
    }
}

package bump3;

import bump3.*;
import java.io.File;
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.EventListener;
import java.net.URISyntaxException;

/** the visual side of the program*/
public class Gui extends JFrame implements Runnable, WindowListener, ActionListener, EventListener, ListSelectionListener {

    public static boolean SONGS = false;

    public Color FGCOLOR = Color.white;

    public Color BGCOLOR = null;

    public static JTextField txtArtist = null;

    public static JTextField txtTitle = null;

    public static JButton btnSearch = null;

    public static JButton btnStop = null;

    public static JProgressBar progBar = null;

    public static JLabel lblQueue = null;

    public static DefaultListModel lstModel = null;

    public static JList lstQueue = null;

    public static JButton btnQueueRemove = null;

    public static JButton btnQueueClear = null;

    public static JMenuBar menuBar = null;

    JMenuItem mnuEngineSelectAll = null;

    JMenuItem mnuEngineSelectNone = null;

    public static JCheckBoxMenuItem[] mnuEngines = null;

    public static JMenuItem mnuFilesize = null;

    public static JCheckBoxMenuItem mnuNoSpaces = null;

    public static JCheckBoxMenuItem mnuNoDupes = null;

    public static JMenuItem mnuSaveDir = null;

    public static JMenuItem mnuSaveDirChange = null;

    public static JCheckBoxMenuItem mnuAutoplayOn = null;

    public static JMenuItem mnuAutoplayWait = null;

    public static JMenuItem mnuAutoplayString = null;

    public static JMenuItem mnuTimeoutConnect = null;

    public static JMenuItem mnuTimeoutRead = null;

    public static JCheckBoxMenuItem mnuSkin = null;

    public static JMenu mnuRecent = null;

    public static JMenuItem mnuRecentClear = null;

    public static JMenuItem mnuRecents[] = null;

    public static JMenuItem mnuHelpFAQ = null;

    public static JMenuItem mnuHelpUpdate = null;

    public static JMenuItem mnuHelpHomepage = null;

    public static JMenuItem mnuHelpAbout = null;

    public ImageIcon icon = null;

    public JPanel panel = null;

    public Font FONT = new Font("Default", Font.BOLD, 14);

    public boolean THEME = false;

    /** default constructor, builds the form
	 *	@param title the name of the window
	 */
    public Gui() {
        super("BuMP3");
        THEME = Main.THEME;
        this.setSize(205, 160);
        this.setFont(FONT);
        setLocationRelativeTo(null);
        setResizable(false);
        addWindowListener(this);
        if (BGCOLOR != null) setBackground(BGCOLOR);
        if (FGCOLOR != null) setForeground(FGCOLOR);
        if (THEME) {
            icon = new ImageIcon(this.getClass().getResource("bump3.png"));
            panel = new JPanel() {

                protected void paintComponent(Graphics g) {
                    g.drawImage(icon.getImage(), 0, 0, null);
                    super.paintComponent(g);
                }
            };
            panel.setOpaque(false);
            panel.setPreferredSize(new Dimension(180, 200));
            getContentPane().add(panel);
            try {
                FONT = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("uni0553.TTF"));
                FONT = FONT.deriveFont(16.0f);
            } catch (FontFormatException ffe) {
                ffe.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } else {
            setLayout(new FlowLayout());
        }
        setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("icon.png")));
        menuBar = buildMenu();
        setJMenuBar(menuBar);
        buildControls();
        addWindowListener(new WindowAdapter() {

            public void windowOpened(WindowEvent e) {
                txtArtist.requestFocus();
            }

            public void windowActivated(WindowEvent e) {
                txtArtist.requestFocus();
            }
        });
    }

    /** we need run() in order for the threading to work properly
	 */
    public void run() {
        doLayout();
        setVisible(true);
    }

    /** builds the entire menu system (submenus included)
	 *  @return the program's menu
	 */
    public JMenuBar buildMenu() {
        Color MenuFGColor, MenuBGColor;
        if (THEME) {
            MenuFGColor = Color.WHITE;
            MenuBGColor = Color.DARK_GRAY;
            FONT = FONT.deriveFont(12.0f);
            UIManager.put("MenuBar.font", FONT);
            UIManager.put("Menu.font", FONT);
            UIManager.put("CheckBoxMenuItem.font", FONT);
            UIManager.put("MenuItem.font", FONT);
            UIManager.put("PopupMenu.font", FONT);
            UIManager.put("MenuBar.background", MenuBGColor);
            UIManager.put("MenuBar.foreground", MenuFGColor);
            UIManager.put("MenuBar.selectionBackground", MenuFGColor);
            UIManager.put("MenuBar.selectionForeground", MenuBGColor);
            UIManager.put("Menu.background", MenuBGColor);
            UIManager.put("Menu.foreground", MenuFGColor);
            UIManager.put("Menu.selectionBackground", MenuFGColor);
            UIManager.put("Menu.selectionForeground", MenuBGColor);
            UIManager.put("CheckBoxMenuItem.background", MenuBGColor);
            UIManager.put("CheckBoxMenuItem.foreground", MenuFGColor);
            UIManager.put("CheckBoxMenuItem.selectionBackground", MenuFGColor);
            UIManager.put("CheckBoxMenuItem.selectionForeground", MenuBGColor);
            UIManager.put("MenuItem.selectionBackground", MenuFGColor);
            UIManager.put("MenuItem.selectionForeground", MenuBGColor);
            UIManager.put("MenuItem.background", MenuBGColor);
            UIManager.put("MenuItem.foreground", MenuFGColor);
            UIManager.put("PopupMenu.selectionBackground", MenuFGColor);
            UIManager.put("PopupMenu.selectionForeground", MenuBGColor);
            UIManager.put("PopupMenu.background", MenuBGColor);
            UIManager.put("PopupMenu.foreground", MenuFGColor);
            FONT = FONT.deriveFont(16.0f);
        } else {
            MenuFGColor = Color.DARK_GRAY;
            MenuBGColor = Color.LIGHT_GRAY;
        }
        menuBar = new JMenuBar();
        menuBar.setBorderPainted(false);
        JMenu menu = new JMenu("engines");
        menu.setMnemonic(KeyEvent.VK_E);
        menu.getAccessibleContext().setAccessibleDescription("search engines used by the program");
        menuBar.add(menu);
        mnuEngineSelectAll = new JMenuItem("select all");
        mnuEngineSelectAll.setMnemonic(KeyEvent.VK_A);
        menu.add(mnuEngineSelectAll);
        mnuEngineSelectAll.addActionListener(this);
        mnuEngineSelectNone = new JMenuItem("select none");
        mnuEngineSelectNone.setMnemonic(KeyEvent.VK_N);
        menu.add(mnuEngineSelectNone);
        mnuEngineSelectNone.addActionListener(this);
        menu.addSeparator();
        String[] e = new String[] { "dilandau.eu", "mp3skull.com", "pepperoni-mp3.com", "dreammedia.ru", "seekasong.com", "google.com", "emp3world.com", "findmp3s.com", "oth.net", "espew.net", "mp3-center.org", "downloads.nl", "4shared.com", "prostopleer.com", "myzuka.ru" };
        mnuEngines = new JCheckBoxMenuItem[e.length];
        for (int i = 0; i < e.length; i++) {
            if (i < Main.ENGINES.length) {
                mnuEngines[i] = new JCheckBoxMenuItem(e[i], Main.ENGINES[i]);
                menu.add(mnuEngines[i]);
            }
        }
        menu = new JMenu("settings");
        menu.setMnemonic(KeyEvent.VK_S);
        menu.getAccessibleContext().setAccessibleDescription("program settings and user preferences");
        menuBar.add(menu);
        mnuFilesize = new JMenuItem("min. filesize: " + Methods.bytesToSize(Main.MIN_FILESIZE));
        mnuFilesize.setMnemonic(KeyEvent.VK_F);
        menu.add(mnuFilesize);
        mnuFilesize.addActionListener(this);
        mnuNoSpaces = new JCheckBoxMenuItem("remove spaces", Main.NOSPACES);
        mnuNoSpaces.setMnemonic(KeyEvent.VK_R);
        menu.add(mnuNoSpaces);
        mnuNoSpaces.addActionListener(this);
        mnuNoDupes = new JCheckBoxMenuItem("no duplicates", Main.NODUPES);
        mnuNoDupes.setMnemonic(KeyEvent.VK_R);
        menu.add(mnuNoDupes);
        mnuNoDupes.addActionListener(this);
        JMenu mnuAutoplay = new JMenu("autoplay");
        mnuAutoplay.setMnemonic(KeyEvent.VK_A);
        menu.add(mnuAutoplay);
        mnuAutoplayOn = new JCheckBoxMenuItem("autoplay songs", Main.AUTOPLAY);
        mnuAutoplayOn.setMnemonic(KeyEvent.VK_A);
        mnuAutoplayOn.addActionListener(this);
        mnuAutoplay.add(mnuAutoplayOn);
        mnuAutoplayWait = new JMenuItem("when song is " + Main.AUTOPLAY_WAIT + "% complete");
        mnuAutoplayWait.setMnemonic(KeyEvent.VK_W);
        mnuAutoplayWait.setEnabled(Main.AUTOPLAY);
        mnuAutoplayWait.addActionListener(this);
        mnuAutoplay.add(mnuAutoplayWait);
        mnuAutoplay.addSeparator();
        mnuAutoplayString = new JMenuItem("command: " + Main.PLAYSTRING);
        mnuAutoplayString.setMnemonic(KeyEvent.VK_C);
        mnuAutoplayString.addActionListener(this);
        mnuAutoplay.add(mnuAutoplayString);
        menu.addSeparator();
        JMenu mnuSaveDirSub = new JMenu("save directory");
        mnuSaveDirSub.setMnemonic(KeyEvent.VK_S);
        menu.add(mnuSaveDirSub);
        mnuSaveDir = new JMenuItem(Main.SAVE_DIR);
        mnuSaveDir.addActionListener(this);
        mnuSaveDirSub.add(mnuSaveDir);
        mnuSaveDirChange = new JMenuItem("Change save folder...");
        mnuSaveDirChange.addActionListener(this);
        mnuSaveDirSub.add(mnuSaveDirChange);
        JMenu mnuTimeout = new JMenu("timeouts");
        mnuTimeout.setMnemonic(KeyEvent.VK_T);
        menu.add(mnuTimeout);
        mnuTimeoutConnect = new JMenuItem("connection timeout: " + Methods.msToSec(Main.CONNECT_TIMEOUT) + "s");
        mnuTimeoutConnect.setMnemonic(KeyEvent.VK_C);
        mnuTimeoutConnect.addActionListener(this);
        mnuTimeout.add(mnuTimeoutConnect);
        mnuTimeoutRead = new JMenuItem("read timeout: " + Methods.msToSec(Main.READ_TIMEOUT) + "s");
        mnuTimeoutRead.setMnemonic(KeyEvent.VK_R);
        mnuTimeoutRead.addActionListener(this);
        mnuTimeout.add(mnuTimeoutRead);
        menu.addSeparator();
        mnuSkin = new JCheckBoxMenuItem("GUI theme", THEME);
        mnuSkin.setMnemonic(KeyEvent.VK_G);
        mnuSkin.addActionListener(this);
        menu.add(mnuSkin);
        mnuRecent = new JMenu("*");
        mnuRecentClear = new JMenuItem("clear recently downloaded files");
        mnuRecentClear.setMnemonic(KeyEvent.VK_C);
        mnuRecentClear.addActionListener(this);
        reloadRecents();
        menuBar.add(mnuRecent);
        menu = new JMenu("help");
        menu.setMnemonic(KeyEvent.VK_H);
        menu.getAccessibleContext().setAccessibleDescription("get information on program features");
        menuBar.add(menu);
        mnuHelpFAQ = new JMenuItem("FAQ");
        mnuHelpFAQ.setMnemonic(KeyEvent.VK_F);
        mnuHelpFAQ.addActionListener(this);
        mnuHelpUpdate = new JMenuItem("check for updates");
        mnuHelpUpdate.setMnemonic(KeyEvent.VK_U);
        mnuHelpUpdate.addActionListener(this);
        mnuHelpHomepage = new JMenuItem("visit project homepage");
        mnuHelpHomepage.setMnemonic(KeyEvent.VK_H);
        mnuHelpHomepage.addActionListener(this);
        mnuHelpAbout = new JMenuItem("about BuMP3");
        mnuHelpAbout.setMnemonic(KeyEvent.VK_A);
        mnuHelpAbout.addActionListener(this);
        menu.add(mnuHelpFAQ);
        menu.addSeparator();
        menu.add(mnuHelpUpdate);
        menu.add(mnuHelpHomepage);
        menu.addSeparator();
        menu.add(mnuHelpAbout);
        return menuBar;
    }

    /** reloads the recently-downloaded files
	 *  uses the string array Main.RECENTS for the files to add
	 */
    public void reloadRecents() {
        JMenu menu = mnuRecent;
        menu.getAccessibleContext().setAccessibleDescription("recently downloaded files");
        menu.removeAll();
        menu.add(mnuRecentClear);
        menu.addSeparator();
        mnuRecents = new JMenuItem[Main.RECENTS.length];
        if (Main.RECENTS.length == 0) {
            mnuRecents = new JMenuItem[1];
            Main.RECENTS = new String[] { "" };
        }
        if (Main.RECENTS.length == 1 && Main.RECENTS[0].equals("")) {
            mnuRecents[0] = new JMenuItem("none");
            mnuRecents[0].setEnabled(false);
            menu.add(mnuRecents[0]);
        }
        for (int i = 0; i < Main.RECENTS.length; i++) {
            if (Main.RECENTS[i].equals("")) continue;
            mnuRecents[i] = new JMenuItem(Main.RECENTS[i].substring(Main.RECENTS[i].lastIndexOf(Main.PATHSEP) + 1));
            mnuRecents[i].setEnabled(true);
            mnuRecents[i].setName(Main.RECENTS[i]);
            mnuRecents[i].addActionListener(this);
            menu.add(mnuRecents[i]);
        }
    }

    /** builds all of the components seen on the Gui window
	 *  the textboxes, buttons, list, and labels are built here
	*/
    public void buildControls() {
        Font f = FONT;
        if (!THEME) {
            BGCOLOR = null;
            FGCOLOR = null;
        } else {
            UIManager.put("TextField.caretForeground", FGCOLOR);
        }
        JLabel lblArtist = new JLabel("artist:");
        lblArtist.setFont(f);
        if (BGCOLOR != null) lblArtist.setBackground(BGCOLOR);
        if (FGCOLOR != null) lblArtist.setForeground(FGCOLOR);
        lblArtist.setPreferredSize(new Dimension(60, 20));
        lblArtist.setHorizontalAlignment(JLabel.RIGHT);
        (THEME ? panel : this).add(lblArtist);
        txtArtist = new JTextField();
        txtArtist.setFont(f);
        if (BGCOLOR != null) txtArtist.setBackground(BGCOLOR);
        if (FGCOLOR != null) txtArtist.setForeground(FGCOLOR);
        txtArtist.setPreferredSize(new Dimension(110, 20));
        txtArtist.requestFocusInWindow();
        (THEME ? panel : this).add(txtArtist);
        txtArtist.addActionListener(this);
        if (THEME) {
            txtArtist.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
            txtArtist.setOpaque(false);
        }
        JLabel lblTitle = new JLabel("title:");
        lblTitle.setFont(f);
        if (BGCOLOR != null) lblTitle.setBackground(BGCOLOR);
        if (FGCOLOR != null) lblTitle.setForeground(FGCOLOR);
        lblTitle.setPreferredSize(new Dimension(60, 20));
        lblTitle.setHorizontalAlignment(JLabel.RIGHT);
        (THEME ? panel : this).add(lblTitle);
        txtTitle = new JTextField();
        txtTitle.setFont(f);
        if (BGCOLOR != null) txtTitle.setBackground(BGCOLOR);
        if (FGCOLOR != null) txtTitle.setForeground(FGCOLOR);
        txtTitle.setPreferredSize(new Dimension(110, 20));
        txtTitle.requestFocusInWindow();
        (THEME ? panel : this).add(txtTitle);
        txtTitle.addActionListener(this);
        if (THEME) {
            txtTitle.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
            txtTitle.setOpaque(false);
        }
        btnSearch = new JButton("search");
        btnSearch.setFont(f);
        if (BGCOLOR != null) btnSearch.setBackground(BGCOLOR);
        if (FGCOLOR != null) btnSearch.setForeground(FGCOLOR);
        btnSearch.setPreferredSize(new Dimension(95, 24));
        (THEME ? panel : this).add(btnSearch);
        btnSearch.addActionListener(this);
        if (THEME) {
            btnSearch.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
            btnSearch.setOpaque(false);
        }
        btnStop = new JButton("stop");
        btnStop.setFont(f);
        btnStop.setEnabled(false);
        if (BGCOLOR != null) btnStop.setBackground(BGCOLOR);
        if (FGCOLOR != null) btnStop.setForeground(FGCOLOR);
        btnStop.setPreferredSize(new Dimension(73, 24));
        (THEME ? panel : this).add(btnStop);
        btnStop.addActionListener(this);
        if (THEME) {
            btnStop.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
            btnStop.setOpaque(false);
        }
        if (THEME) {
            UIManager.put("ProgressBar.selectionBackground", Color.LIGHT_GRAY);
            UIManager.put("ProgressBar.selectionForeground", Color.BLACK);
        }
        progBar = new JProgressBar(0, 100);
        progBar.setValue(0);
        progBar.setFont(f);
        progBar.setPreferredSize(new Dimension(173, 23));
        if (BGCOLOR != null) progBar.setBackground(BGCOLOR);
        if (FGCOLOR != null) progBar.setForeground(FGCOLOR);
        if (THEME) {
            progBar.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
            progBar.setOpaque(false);
            progBar.setForeground(Color.DARK_GRAY);
        }
        progBar.setStringPainted(true);
        progBar.setString("inactive");
        (THEME ? panel : this).add(progBar);
        lblQueue = new JLabel("queue:");
        lblQueue.setFont(FONT);
        if (FGCOLOR != null) lblQueue.setForeground(FGCOLOR);
        lblQueue.setPreferredSize(new Dimension(170, 23));
        lblQueue.setHorizontalAlignment(JLabel.LEFT);
        (THEME ? panel : this).add(lblQueue);
        lstModel = new DefaultListModel();
        lstQueue = new JList(lstModel);
        f = f.deriveFont(12.0f);
        lstQueue.setFont(f);
        f = f.deriveFont(16.0f);
        if (BGCOLOR != null) lstQueue.setBackground(BGCOLOR);
        if (FGCOLOR != null) lstQueue.setForeground(FGCOLOR);
        lstQueue.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstQueue.addListSelectionListener(this);
        lstQueue.setMinimumSize(new Dimension(170, 50));
        JScrollPane listScrollPane = new JScrollPane(lstQueue);
        listScrollPane.setPreferredSize(new Dimension(175, 80));
        if (THEME) {
            lstQueue.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
            lstQueue.setOpaque(false);
            lstQueue.setSelectionBackground(BGCOLOR);
            listScrollPane.setOpaque(false);
            listScrollPane.getViewport().setOpaque(false);
        }
        (THEME ? panel : this).add(lstQueue);
        (THEME ? panel : this).add(listScrollPane);
        listScrollPane.getViewport().add(lstQueue);
        f = f.deriveFont(14.0f);
        btnQueueRemove = new JButton("remove");
        btnQueueRemove.setFont(f);
        if (BGCOLOR != null) btnQueueRemove.setBackground(BGCOLOR);
        if (FGCOLOR != null) btnQueueRemove.setForeground(FGCOLOR);
        btnQueueRemove.setPreferredSize(new Dimension(95, 23));
        (THEME ? panel : this).add(btnQueueRemove);
        btnQueueRemove.addActionListener(this);
        if (THEME) {
            btnQueueRemove.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
            btnQueueRemove.setOpaque(false);
        }
        btnQueueClear = new JButton("clear");
        btnQueueClear.setFont(f);
        if (BGCOLOR != null) btnQueueClear.setBackground(BGCOLOR);
        if (FGCOLOR != null) btnQueueClear.setForeground(FGCOLOR);
        btnQueueClear.setPreferredSize(new Dimension(75, 23));
        (THEME ? panel : this).add(btnQueueClear);
        btnQueueClear.addActionListener(this);
        if (THEME) {
            btnQueueClear.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
            btnQueueClear.setOpaque(false);
        }
    }

    /** list selection event, called when user clicks the list
	 *  @param e LSE object holding information on the list select event
	 */
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
            if (lstQueue.getSelectedIndex() == -1) {
                btnQueueRemove.setEnabled(false);
                btnQueueClear.setEnabled(false);
            } else {
                btnQueueRemove.setEnabled(true);
                btnQueueClear.setEnabled(true);
            }
        }
    }

    /** actionperformed event, called when user interacts with
	 *  a component that has addActionListener(this)
	 *  the menu clicks and button presses are handled within this method
	 *  @param event object holding information on the action event
	 */
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == txtArtist) {
            String txt = event.getActionCommand();
            if (!txt.equals("")) {
                txtTitle.requestFocus();
            }
        } else if (event.getSource() == txtTitle) {
            String txt = event.getActionCommand();
            if (!txt.equals("") && !txtArtist.getText().equals("")) {
                search();
            }
        } else if (event.getSource() == btnSearch) {
            search();
        } else if (event.getSource() == btnStop) {
            stop();
        } else if (event.getSource() == mnuEngineSelectAll) {
            for (int i = 0; i < mnuEngines.length && mnuEngines[i] != null; i++) if (!mnuEngines[i].getText().equals("espew.net")) mnuEngines[i].setState(true);
        } else if (event.getSource() == mnuEngineSelectNone) {
            for (int i = 0; i < mnuEngines.length && mnuEngines[i] != null; i++) mnuEngines[i].setState(false);
        } else if (event.getSource() == mnuFilesize) {
            String answer = JOptionPane.showInputDialog(this, "buMP3 will not download mp3s \nsmaller than the minimum file size.\n\n" + "enter the minimum file size below:", "" + Main.MIN_FILESIZE);
            if (answer == null || answer.equals("")) return;
            int size = Main.MIN_FILESIZE;
            try {
                size = Integer.parseInt(answer);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "invalid filesize format. enter a number; no commas, periods, or letters", "BuMP3 - Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Main.MIN_FILESIZE = size;
            mnuFilesize.setText("min. filesize: " + Methods.bytesToSize(size));
        } else if (event.getSource() == mnuNoSpaces) {
            Main.NOSPACES = mnuNoSpaces.getState();
        } else if (event.getSource() == mnuNoDupes) {
            Main.NODUPES = mnuNoDupes.getState();
        } else if (event.getSource() == mnuAutoplayOn) {
            Main.AUTOPLAY = mnuAutoplayOn.getState();
            mnuAutoplayWait.setEnabled(Main.AUTOPLAY);
        } else if (event.getSource() == mnuAutoplayWait) {
            String answer = JOptionPane.showInputDialog(this, "at what percentage do you want BuMP3\nto play a downloading song?\n\n" + "enter the percentage (1-100) below:", "" + Main.AUTOPLAY_WAIT);
            if (answer == null || answer.equals("")) return;
            int size = Main.AUTOPLAY_WAIT;
            try {
                size = Integer.parseInt(answer);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "invalid percentage. enter a number; no commas, % signs, or letters", "BuMP3 - Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Main.AUTOPLAY_WAIT = size;
            mnuAutoplayWait.setText("when song is " + Main.AUTOPLAY_WAIT + "% complete");
        } else if (event.getSource() == mnuAutoplayString) {
            String answer = JOptionPane.showInputDialog(this, "Enter the commands you want to execute\nto autoplay the song.\n\n" + "Enter %s in place of the file path:", Main.PLAYSTRING);
            if (answer == null || answer.equals("")) return;
            Main.PLAYSTRING = answer;
            mnuAutoplayString.setText("play command: " + Main.PLAYSTRING + "");
        } else if (event.getSource() == mnuSaveDir) {
            Methods.launchDirectory(Main.SAVE_DIR);
        } else if (event.getSource() == mnuSaveDirChange) {
            JFileChooser fc = new JFileChooser(Main.SAVE_DIR);
            fc.setFileSelectionMode(1);
            int ret = fc.showDialog(this, "select MP3 download folder");
            if (ret == 1) return;
            String dir = "";
            try {
                dir = fc.getSelectedFile().getCanonicalPath();
            } catch (IOException ioe) {
                return;
            }
            if (!Methods.fileExists(dir)) {
                JOptionPane.showMessageDialog(null, "The selected folder does not exist.\n" + "Maybe you need to create a new folder first!", "BuMP3 - Save Directory Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!dir.substring(dir.length() - Main.PATHSEP.length()).equals(Main.PATHSEP)) dir += Main.PATHSEP;
            Main.SAVE_DIR = dir;
            mnuSaveDir.setText(dir);
        } else if (event.getSource() == mnuTimeoutConnect) {
            String answer = JOptionPane.showInputDialog(this, "how long do you want ot wait for a\npage to connect before moving on?\n\n" + "enter the time in MILLISECONDS (1000ms = 1s):", "" + Main.CONNECT_TIMEOUT);
            if (answer == null || answer.equals("")) return;
            int time = Main.CONNECT_TIMEOUT;
            try {
                time = Integer.parseInt(answer);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "invalid number. enter a number; no commas, minus signs, or letters", "BuMP3 - Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (time <= 0) return;
            Main.CONNECT_TIMEOUT = time;
            mnuTimeoutConnect.setText("connection timeout: " + Methods.msToSec(Main.CONNECT_TIMEOUT) + "s");
        } else if (event.getSource() == mnuTimeoutRead) {
            String answer = JOptionPane.showInputDialog(this, "how long do you want ot wait for a\npage to respond to a query before moving on?\n\n" + "enter the time in MILLISECONDS (1000ms = 1s):", "" + Main.READ_TIMEOUT);
            if (answer == null || answer.equals("")) return;
            int time = Main.READ_TIMEOUT;
            try {
                time = Integer.parseInt(answer);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "invalid number. enter a number; no commas, minus signs, or letters", "BuMP3 - Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (time <= 0) return;
            Main.READ_TIMEOUT = time;
            mnuTimeoutRead.setText("read timeout: " + Methods.msToSec(Main.READ_TIMEOUT) + "s");
        } else if (event.getSource() == mnuSkin) {
            String msg = "";
            THEME = mnuSkin.getState();
            Main.THEME = THEME;
            if (Main.THEME) msg = "the gui theme has been enabled.\n\n"; else msg = "the gui theme has been disabled.\n\n";
            msg += "the theme will not take effect until you restart the program,\n" + "do you want to restart now?";
            int answer = JOptionPane.showOptionDialog(null, msg, "BuMP3 - Restart?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[] { "yes", "no" }, "yes");
            if (answer == 0) {
                this.setVisible(false);
                Methods.saveSettings("");
                try {
                    String dir = "";
                    try {
                        dir = (new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI())).getPath();
                    } catch (URISyntaxException use) {
                        use.printStackTrace();
                        try {
                            dir = new File(".").getCanonicalPath();
                            if (!dir.substring(dir.length() - 1).equals(Main.PATHSEP)) dir += Main.PATHSEP;
                            dir += Main.FILE_NAME;
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    }
                    Process proc = Runtime.getRuntime().exec(new String[] { "java", "-jar", dir });
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                System.exit(0);
            }
        } else if (event.getSource() == mnuRecentClear) {
            Main.RECENTS = new String[] { "" };
            reloadRecents();
        } else if (event.getSource() == mnuHelpFAQ) {
            String msg = "";
            msg += "                frequently asked questions\n\n";
            msg += "  Q: is this illegal?\n";
            msg += "no. downloading mp3's is not illegal.\n" + "downloading copyrighted material, however, IS illegal.\n\n";
            msg += "  Q: did you get permission from the sites?\n";
            msg += "no.\n\n";
            msg += "  Q: why did you make this program?\n";
            msg += "because I like single songs. \n" + "why download a whole album when all you want is one song?\n" + "this program is great for one-hit-wonders and popular songs.\n\n";
            msg += "  Q: a song I want will not download!\n";
            msg += "this program is not perfect, and not every site\n" + "will have every song. you could try broadening your search.";
            JOptionPane.showMessageDialog(null, msg, "BuMP3 help", JOptionPane.INFORMATION_MESSAGE);
        } else if (event.getSource() == mnuHelpUpdate) {
            String changes = Methods.getLatestRevisionChanges();
            if (changes.equals("")) {
                JOptionPane.showMessageDialog(null, "unable to retrieve update information.\n\n" + "try again later", "BuMP3 upgrade check", JOptionPane.ERROR_MESSAGE);
            } else if (changes.equals("up2date")) {
                JOptionPane.showMessageDialog(null, "no updates are available at this time.\n\n" + "current revision:   r" + Main.REVISION + "\n" + "available revision: r" + Main.REVISION + "", "BuMP3 upgrade check", JOptionPane.INFORMATION_MESSAGE);
            } else {
                if (JOptionPane.showOptionDialog(null, "a new update is available!\n\n" + "changes in this new version:\n\n" + changes + "\n\n" + "do you wish to upgrade this program?", "BuMP3 - Upgrade!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[] { "yes!", "no" }, "yes") == 0) {
                    if (Methods.upgrayedd("http://bump3.googlecode.com/svn/trunk/bump3/bump3.jar")) Methods.status("upgrade complete"); else Methods.status("unable to upgr8");
                }
            }
        } else if (event.getSource() == mnuHelpHomepage) {
            Methods.openUrl("http://code.google.com/p/bump3/");
        } else if (event.getSource() == mnuHelpAbout) {
            JOptionPane.showMessageDialog(null, "    BuMP3\n\n" + "version v0.1 r" + Main.REVISION + "\n" + "written in Java\n", "BuMP3 about", JOptionPane.INFORMATION_MESSAGE);
        } else if (event.getSource() == btnQueueRemove) {
            for (int i = 0; i < lstModel.size(); i++) {
                if (lstQueue.isSelectedIndex(i) && !lstModel.get(i).toString().substring(0, 1).equals("*")) {
                    lstModel.remove(i);
                    i -= i;
                }
            }
        } else if (event.getSource() == btnQueueClear) {
            for (int i = 0; i < lstModel.size(); i++) {
                if (!lstModel.get(i).toString().substring(0, 1).equals("*")) {
                    lstModel.remove(i);
                    i -= i;
                }
            }
        } else {
            for (int i = 0; i < mnuRecents.length; i++) {
                if (event.getSource() == mnuRecents[i]) {
                    Methods.playSong(mnuRecents[i].getName());
                    break;
                }
            }
        }
    }

    /** checks if there are no engines selected in the mnuEngines array
	 *  @return true if no engiens are selected, false otherwise
	 */
    public static boolean noEngines() {
        for (int i = 0; i < mnuEngines.length; i++) {
            if (mnuEngines[i].getState()) return false;
        }
        return true;
    }

    /** this method is ran whenever a user clicks search
	 *  it makes sure the conditions are correct for search to commence
	 *  also adds a song to the list if need be
	 */
    public void search() {
        if (noEngines()) {
            JOptionPane.showMessageDialog(null, "You have no search engines selected.\n" + "Please select at least one search engine.\n", "BuMP3 - Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean dontadd = false;
        txtArtist.setText(txtArtist.getText().trim());
        txtTitle.setText(txtTitle.getText().trim());
        if (txtArtist.getText().equals("") || txtTitle.getText().equals("")) {
            if (lstModel.getSize() == 0 || btnStop.isEnabled()) {
                JOptionPane.showMessageDialog(null, "You need both an artist and a title to perform a search.", "BuMP3 - Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                dontadd = true;
            }
        }
        if (!dontadd) {
            lstModel.addElement(txtArtist.getText().replaceAll("-", "") + " - " + txtTitle.getText().replaceAll("-", ""));
            txtArtist.setText("");
            txtTitle.setText("");
        }
        if (lstModel.getSize() == 1 || !btnStop.isEnabled()) {
            SONGS = false;
            btnStop.setEnabled(true);
            this.setSize(205, 300);
            checkQueue();
        } else {
            SONGS = true;
        }
        txtArtist.requestFocusInWindow();
    }

    /** checks the lstQueue to see if we have more songs to download
	 *  if we do, it starts downloading the next song
	 *  otherwise, it stops and hides the queue
	 */
    public static void checkQueue() {
        if (lstModel.getSize() > 0 && btnStop.isEnabled()) {
            String item = lstModel.getElementAt(0).toString();
            if (!item.substring(0, 1).equals("*")) {
                GuiSearch gs = new GuiSearch(item.substring(0, item.indexOf(" - ")), item.substring(item.indexOf(" - ") + 3));
                lstModel.set(0, "*" + item);
                gs.t.start();
            }
        } else {
            btnStop.setEnabled(false);
            if (lstModel.getSize() == 0) Main.theGUI.setSize(205, 160);
        }
    }

    /** method called when the user clicks 'stop'
	*/
    public void stop() {
        GuiSearch.STOP = true;
        progBar.setString("stopping...");
        btnStop.setEnabled(false);
    }

    /** various window events that have to be overloaded because we inherit JFrame
	 */
    public void windowOpened(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    /** window event ; when the window is exiting
	 *  this hides the Gui window, saves the settings, and disposes of the window
	 *  @param e windowevent object, tells us about the event
	*/
    public void windowClosing(WindowEvent e) {
        this.setVisible(false);
        Methods.saveSettings("");
        dispose();
        System.exit(0);
    }
}

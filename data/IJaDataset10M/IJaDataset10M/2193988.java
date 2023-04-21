package megamek.client.ui.AWT;

import java.awt.Button;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Label;
import java.awt.MediaTracker;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import megamek.MegaMek;
import megamek.client.Client;
import megamek.client.bot.BotClient;
import megamek.client.bot.TestBot;
import megamek.client.bot.ui.AWT.BotGUI;
import megamek.client.ui.IMegaMekGUI;
import megamek.client.ui.Messages;
import megamek.client.ui.AWT.widget.BackGroundDrawer;
import megamek.client.ui.AWT.widget.BufferedPanel;
import megamek.common.IGame;
import megamek.common.MechSummaryCache;
import megamek.common.Player;
import megamek.common.options.GameOptions;
import megamek.common.options.IBasicOption;
import megamek.common.options.IOption;
import megamek.common.preference.PreferenceManager;
import megamek.server.ScenarioLoader;
import megamek.server.Server;

public class MegaMekGUI implements IMegaMekGUI {

    public Frame frame;

    public Client client = null;

    public Server server = null;

    private CommonAboutDialog about = null;

    private CommonHelpDialog help = null;

    private GameOptionsDialog optdlg = null;

    private CommonSettingsDialog setdlg = null;

    public void start(String[] args) {
        createGUI();
    }

    /**
     * Contruct a MegaMek, and display the main menu in the specified frame.
     */
    private void createGUI() {
        frame = new Frame("MegaMek");
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                quit();
            }
        });
        frame.setBackground(SystemColor.menu);
        frame.setForeground(SystemColor.menuText);
        List<Image> iconList = new ArrayList<Image>();
        iconList.add(frame.getToolkit().getImage("data/images/misc/megamek-icon-16x16.png"));
        iconList.add(frame.getToolkit().getImage("data/images/misc/megamek-icon-32x32.png"));
        iconList.add(frame.getToolkit().getImage("data/images/misc/megamek-icon-48x48.png"));
        iconList.add(frame.getToolkit().getImage("data/images/misc/megamek-icon-256x256.png"));
        frame.setIconImages(iconList);
        CommonMenuBar menuBar = new CommonMenuBar();
        menuBar.addActionListener(actionListener);
        frame.setMenuBar(menuBar);
        showMainMenu();
        Dimension screenSize = frame.getToolkit().getScreenSize();
        frame.pack();
        frame.setLocation(screenSize.width / 2 - frame.getSize().width / 2, screenSize.height / 2 - frame.getSize().height / 2);
        MechSummaryCache.getInstance();
        frame.setVisible(true);
        if (GUIPreferences.getInstance().getNagForReadme()) {
            ConfirmDialog confirm = new ConfirmDialog(frame, Messages.getString("MegaMek.welcome.title") + MegaMek.VERSION, Messages.getString("MegaMek.welcome.message"), true);
            confirm.setVisible(true);
            if (!confirm.getShowAgain()) {
                GUIPreferences.getInstance().setNagForReadme(false);
            }
            if (confirm.getAnswer()) {
                showHelp();
            }
        }
    }

    /**
     * Display the main menu.
     */
    public void showMainMenu() {
        Button hostB, connectB, botB, editB, scenB, loadB, quitB;
        Label labVersion = new Label();
        labVersion.setText(Messages.getString("MegaMek.Version") + MegaMek.VERSION);
        hostB = new Button(Messages.getString("MegaMek.hostNewGame.label"));
        hostB.setActionCommand("fileGameNew");
        hostB.addActionListener(actionListener);
        scenB = new Button(Messages.getString("MegaMek.hostScenario.label"));
        scenB.setActionCommand("fileGameScenario");
        scenB.addActionListener(actionListener);
        loadB = new Button(Messages.getString("MegaMek.hostSavedGame.label"));
        loadB.setActionCommand("fileGameOpen");
        loadB.addActionListener(actionListener);
        connectB = new Button(Messages.getString("MegaMek.Connect.label"));
        connectB.setActionCommand("fileGameConnect");
        connectB.addActionListener(actionListener);
        botB = new Button(Messages.getString("MegaMek.ConnectAsBot.label"));
        botB.setActionCommand("fileGameConnectBot");
        botB.addActionListener(actionListener);
        editB = new Button(Messages.getString("MegaMek.MapEditor.label"));
        editB.setActionCommand("fileBoardNew");
        editB.addActionListener(actionListener);
        quitB = new Button(Messages.getString("MegaMek.Quit.label"));
        quitB.setActionCommand("quit");
        quitB.addActionListener(actionListener);
        Image imgSplash = frame.getToolkit().getImage("data/images/misc/megamek-splash.jpg");
        MediaTracker tracker = new MediaTracker(frame);
        tracker.addImage(imgSplash, 0);
        try {
            tracker.waitForID(0);
        } catch (InterruptedException e) {
        }
        BufferedPanel panTitle = new BufferedPanel();
        BackGroundDrawer bgdTitle = new BackGroundDrawer(imgSplash);
        panTitle.addBgDrawer(bgdTitle);
        panTitle.setPreferredSize(imgSplash.getWidth(null), imgSplash.getHeight(null));
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        frame.setLayout(gridbag);
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(4, 4, 1, 1);
        c.ipadx = 10;
        c.ipady = 5;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridwidth = 1;
        c.gridheight = 8;
        addBag(panTitle, gridbag, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = .05;
        c.weighty = 1.0;
        c.gridx = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        addBag(labVersion, gridbag, c);
        c.gridy++;
        addBag(hostB, gridbag, c);
        c.gridy++;
        addBag(loadB, gridbag, c);
        c.gridy++;
        addBag(scenB, gridbag, c);
        c.gridy++;
        addBag(connectB, gridbag, c);
        c.gridy++;
        addBag(botB, gridbag, c);
        c.gridy++;
        addBag(editB, gridbag, c);
        c.gridy++;
        addBag(quitB, gridbag, c);
        frame.validate();
    }

    /**
     * Display the game options dialog.
     */
    public void showGameOptions() {
        GameOptions options = new GameOptions();
        options.initialize();
        options.loadOptions();
        if (optdlg == null) {
            optdlg = new GameOptionsDialog(frame, options);
        }
        optdlg.update(options);
        optdlg.setVisible(true);
    }

    /**
     * Display the board editor.
     */
    public void showEditor() {
        BoardEditor editor = new BoardEditor();
        launch(editor.getFrame());
        editor.boardNew();
    }

    /**
     * Display the board editor and open an "open" dialog.
     */
    public void showEditorOpen() {
        BoardEditor editor = new BoardEditor();
        launch(editor.getFrame());
        editor.boardLoad();
    }

    /**
     * Start instances of both the client and the server.
     */
    public void host() {
        HostDialog hd;
        hd = new HostDialog(frame);
        hd.setVisible(true);
        if ((hd.name == null) || (hd.serverPass == null) || (hd.port == 0)) {
            return;
        }
        boolean foundValid = false;
        char[] nameChars = hd.name.toCharArray();
        for (int loop = 0; !foundValid && (loop < nameChars.length); loop++) {
            if (!Character.isWhitespace(nameChars[loop])) {
                foundValid = true;
            }
        }
        if (!foundValid) {
            new AlertDialog(frame, Messages.getString("MegaMek.PlayerNameAlert.title"), Messages.getString("MegaMek.PlayerNameAlert.message")).setVisible(true);
            return;
        }
        megamek.common.Compute.d6();
        try {
            server = new Server(hd.serverPass, hd.port);
        } catch (IOException ex) {
            System.err.println("could not create server socket on port " + hd.port);
            StringBuffer error = new StringBuffer();
            error.append("Error: could not start server at localhost").append(":").append(hd.port).append(" (").append(ex.getMessage()).append(").");
            new AlertDialog(frame, Messages.getString("MegaMek.HostGameAllert.title"), error.toString()).setVisible(true);
            return;
        }
        client = new Client(hd.name, "localhost", hd.port);
        ClientGUI gui = new ClientGUI(client);
        gui.initialize();
        if (!client.connect()) {
            StringBuffer error = new StringBuffer();
            error.append("Error: could not connect to server at localhost").append(":").append(hd.port).append(".");
            new AlertDialog(frame, Messages.getString("MegaMek.HostGameAllert.title"), error.toString()).setVisible(true);
            frame.setVisible(false);
            client.die();
        }
        launch(gui.getFrame());
        optdlg = null;
    }

    public void loadGame() {
        FileDialog fd = new FileDialog(frame, Messages.getString("MegaMek.SaveGameDialog.title"), FileDialog.LOAD);
        fd.setDirectory("savegames");
        fd.setFilenameFilter(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return ((null != name) && name.endsWith(".sav"));
            }
        });
        fd.setVisible(true);
        if (fd.getFile() == null) {
            return;
        }
        HostDialog hd = new HostDialog(frame);
        hd.setVisible(true);
        if ((hd.name == null) || (hd.serverPass == null) || (hd.port == 0)) {
            return;
        }
        boolean foundValid = false;
        char[] nameChars = hd.name.toCharArray();
        for (int loop = 0; !foundValid && (loop < nameChars.length); loop++) {
            if (!Character.isWhitespace(nameChars[loop])) {
                foundValid = true;
            }
        }
        if (!foundValid) {
            new AlertDialog(frame, Messages.getString("MegaMek.PlayerNameAlert1.title"), Messages.getString("MegaMek.PlayerNameAlert1.message")).setVisible(true);
            return;
        }
        megamek.common.Compute.d6();
        try {
            server = new Server(hd.serverPass, hd.port);
        } catch (IOException ex) {
            System.err.println("could not create server socket on port " + hd.port);
            StringBuffer error = new StringBuffer();
            error.append("Error: could not start server at localhost").append(":").append(hd.port).append(" (").append(ex.getMessage()).append(").");
            new AlertDialog(frame, Messages.getString("MegaMek.HostGameAllert.title"), error.toString()).setVisible(true);
            return;
        }
        if (!server.loadGame(new File(fd.getDirectory(), fd.getFile()))) {
            new AlertDialog(frame, Messages.getString("MegaMek.LoadGameAlert.title"), Messages.getString("MegaMek.LoadGameAlert.message")).setVisible(true);
            server.die();
            server = null;
            return;
        }
        client = new Client(hd.name, "localhost", hd.port);
        ClientGUI gui = new ClientGUI(client);
        gui.initialize();
        if (!client.connect()) {
            StringBuffer error = new StringBuffer();
            error.append("Error: could not connect to server at localhost").append(":").append(hd.port).append(".");
            new AlertDialog(frame, Messages.getString("MegaMek.HostGameAllert.title"), error.toString()).setVisible(true);
            frame.setVisible(false);
            client.die();
        }
        optdlg = null;
        launch(gui.getFrame());
    }

    /**
     * Host a game constructed from a scenario file
     */
    public void scenario() {
        FileDialog fd = new FileDialog(frame, Messages.getString("MegaMek.SelectScenarioDialog.title"), FileDialog.LOAD);
        fd.setDirectory("data" + File.separatorChar + "scenarios");
        FilenameFilter ff = new FilenameFilter() {

            public boolean accept(File f, String s) {
                return s.endsWith(".mms");
            }
        };
        fd.setFilenameFilter(ff);
        fd.setFile("*.mms");
        fd.setVisible(true);
        if (fd.getFile() == null) {
            return;
        }
        ScenarioLoader sl = new ScenarioLoader(new File(fd.getDirectory(), fd.getFile()));
        IGame g = null;
        try {
            g = sl.createGame();
        } catch (Exception e) {
            new AlertDialog(frame, Messages.getString("MegaMek.HostScenarioAllert.title"), Messages.getString("MegaMek.HostScenarioAllert.message") + e.getMessage()).setVisible(true);
            return;
        }
        GameOptionsDialog god = new GameOptionsDialog(frame, g.getOptions());
        god.update(g.getOptions());
        god.setEditable(true);
        god.setVisible(true);
        for (IBasicOption opt : god.getOptions()) {
            IOption orig = g.getOptions().getOption(opt.getName());
            orig.setValue(opt.getValue());
        }
        god = null;
        Player[] pa = new Player[g.getPlayersVector().size()];
        g.getPlayersVector().copyInto(pa);
        ScenarioDialog sd = new ScenarioDialog(frame, pa);
        sd.setVisible(true);
        if (!sd.bSet) {
            return;
        }
        HostDialog hd = new HostDialog(frame);
        boolean hasSlot = false;
        if (!(sd.localName.equals(""))) {
            hasSlot = true;
        }
        hd.yourNameF.setText(sd.localName);
        hd.setVisible(true);
        if ((hd.name == null) || (hd.serverPass == null) || (hd.port == 0)) {
            return;
        }
        sd.localName = hd.name;
        boolean foundValid = false;
        char[] nameChars = hd.name.toCharArray();
        for (int loop = 0; !foundValid && (loop < nameChars.length); loop++) {
            if (!Character.isWhitespace(nameChars[loop])) {
                foundValid = true;
            }
        }
        if (!foundValid) {
            new AlertDialog(frame, Messages.getString("MegaMek.HostScenarioAllert1.title"), Messages.getString("MegaMek.HostScenarioAllert1.message")).setVisible(true);
            return;
        }
        megamek.common.Compute.d6();
        try {
            server = new Server(hd.serverPass, hd.port);
        } catch (IOException ex) {
            System.err.println("could not create server socket on port " + hd.port);
            StringBuffer error = new StringBuffer();
            error.append("Error: could not start server at localhost").append(":").append(hd.port).append(" (").append(ex.getMessage()).append(").");
            new AlertDialog(frame, Messages.getString("MegaMek.HostGameAllert.title"), error.toString()).setVisible(true);
            return;
        }
        server.setGame(g);
        sl.applyDamage(server);
        ClientGUI gui = null;
        if (sd.localName != "") {
            client = new Client(hd.name, "localhost", hd.port);
            gui = new ClientGUI(client);
            gui.initialize();
            if (!client.connect()) {
                StringBuffer error = new StringBuffer();
                error.append("Error: could not connect to server at localhost").append(":").append(hd.port).append(".");
                new AlertDialog(frame, Messages.getString("MegaMek.HostGameAllert.title"), error.toString()).setVisible(true);
                frame.setVisible(false);
                client.die();
            }
        }
        optdlg = null;
        server.calculatePlayerBVs();
        for (int x = 0; x < pa.length; x++) {
            if (sd.playerTypes[x] == ScenarioDialog.T_BOT) {
                BotClient c = new TestBot(pa[x].getName(), "localhost", hd.port);
                c.game.addGameListener(new BotGUI(c));
                if (!c.connect()) {
                }
                c.retrieveServerInfo();
            }
        }
        if (!hasSlot) {
            Enumeration<Player> pE = server.getGame().getPlayers();
            while (pE.hasMoreElements()) {
                Player tmpP = pE.nextElement();
                if (tmpP.getName().equals(sd.localName)) {
                    tmpP.setObserver(true);
                }
            }
        }
        if (gui != null) {
            launch(gui.getFrame());
        }
    }

    /**
     * Connect to to a game and then launch the chat lounge.
     */
    public void connect() {
        ConnectDialog cd;
        cd = new ConnectDialog(frame);
        cd.setVisible(true);
        if ((cd.name == null) || (cd.serverAddr == null) || (cd.port == 0)) {
            return;
        }
        boolean foundValid = false;
        char[] nameChars = cd.name.toCharArray();
        for (int loop = 0; !foundValid && (loop < nameChars.length); loop++) {
            if (!Character.isWhitespace(nameChars[loop])) {
                foundValid = true;
            }
        }
        if (!foundValid) {
            new AlertDialog(frame, Messages.getString("MegaMek.ConnectAllert.title"), Messages.getString("MegaMek.ConnectAllert.message")).setVisible(true);
            return;
        }
        client = new Client(cd.name, cd.serverAddr, cd.port);
        ClientGUI gui = new ClientGUI(client);
        gui.initialize();
        if (!client.connect()) {
            StringBuffer error = new StringBuffer();
            error.append("Error: could not connect to server at ").append(cd.serverAddr).append(":").append(cd.port).append(".");
            new AlertDialog(frame, Messages.getString("MegaMek.HostGameAllert.title"), error.toString()).setVisible(true);
            frame.setVisible(false);
            client.die();
        }
        launch(gui.getFrame());
    }

    public void connectBot() {
        ConnectDialog cd;
        cd = new ConnectDialog(frame);
        cd.setVisible(true);
        if ((cd.name == null) || (cd.serverAddr == null) || (cd.port == 0)) {
            return;
        }
        boolean foundValid = false;
        char[] nameChars = cd.name.toCharArray();
        for (int loop = 0; !foundValid && (loop < nameChars.length); loop++) {
            if (!Character.isWhitespace(nameChars[loop])) {
                foundValid = true;
            }
        }
        if (!foundValid) {
            new AlertDialog(frame, Messages.getString("MegaMek.ConnectGameAllert.title"), Messages.getString("MegaMek.ConnectGameAllert.message")).setVisible(true);
            return;
        }
        client = new TestBot(cd.name, cd.serverAddr, cd.port);
        client.game.addGameListener(new BotGUI((BotClient) client));
        ClientGUI gui = new ClientGUI(client);
        gui.initialize();
        if (!client.connect()) {
            StringBuffer error = new StringBuffer();
            error.append("Error: could not connect to server at ").append(cd.serverAddr).append(":").append(cd.port).append(".");
            new AlertDialog(frame, Messages.getString("MegaMek.HostGameAllert.title"), error.toString()).setVisible(true);
            frame.setVisible(false);
            client.die();
        }
        launch(gui.getFrame());
        client.retrieveServerInfo();
    }

    private void addBag(Component comp, GridBagLayout gridbag, GridBagConstraints c) {
        gridbag.setConstraints(comp, c);
        frame.add(comp);
    }

    /**
     * Called when the user selects the "Help->About" menu item.
     */
    void showAbout() {
        if (about == null) {
            about = new CommonAboutDialog(frame);
        }
        about.setVisible(true);
    }

    /**
     * Called when the user selects the "Help->Contents" menu item.
     */
    void showHelp() {
        if (help == null) {
            help = showHelp(frame, "readme");
        }
        help.setVisible(true);
    }

    /**
     * display the filename in a CommonHelpDialog
     */
    private static CommonHelpDialog showHelp(Frame frame, String filename) {
        Locale l = Locale.getDefault();
        File helpfile = new File(filename + "-" + l.getDisplayLanguage(Locale.ENGLISH) + ".txt");
        if (!helpfile.exists()) {
            helpfile = new File(filename + ".txt");
        }
        return new CommonHelpDialog(frame, helpfile);
    }

    /**
     * Called when the user selects the "View->Client Settings" menu item.
     */
    void showSettings() {
        if (setdlg == null) {
            setdlg = new CommonSettingsDialog(frame);
        }
        setdlg.setVisible(true);
    }

    /**
     * Called when the quit buttons is pressed or the main menu is closed.
     */
    void quit() {
        PreferenceManager.getInstance().save();
        System.exit(0);
    }

    /**
     * Hides this window for later. Listens to the frame until it closes, then
     * calls unlaunch().
     */
    private void launch(Frame launched) {
        launched.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                unlaunch();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                unlaunch();
            }
        });
        frame.setVisible(false);
    }

    /**
     * Un-hides the main menu and tries to clean up the client or server.
     */
    void unlaunch() {
        if (server != null) {
            server.die();
            server = null;
        }
        frame.setVisible(true);
        client = null;
        System.gc();
        System.runFinalization();
    }

    protected ActionListener actionListener = new ActionListener() {

        public void actionPerformed(ActionEvent ev) {
            if (ev.getActionCommand().equalsIgnoreCase("fileBoardNew")) {
                showEditor();
            }
            if (ev.getActionCommand().equalsIgnoreCase("fileBoardOpen")) {
                showEditorOpen();
            }
            if (ev.getActionCommand().equalsIgnoreCase("fileGameNew")) {
                host();
            }
            if (ev.getActionCommand().equalsIgnoreCase("fileGameScenario")) {
                scenario();
            }
            if (ev.getActionCommand().equalsIgnoreCase("fileGameConnect")) {
                connect();
            }
            if (ev.getActionCommand().equalsIgnoreCase("fileGameConnectBot")) {
                connectBot();
            }
            if (ev.getActionCommand().equalsIgnoreCase("fileGameOpen")) {
                loadGame();
            }
            if (ev.getActionCommand().equalsIgnoreCase("viewGameOptions")) {
                showGameOptions();
            }
            if (ev.getActionCommand().equalsIgnoreCase("helpAbout")) {
                showAbout();
            }
            if (ev.getActionCommand().equalsIgnoreCase("helpContents")) {
                showHelp();
            }
            if (ev.getActionCommand().equalsIgnoreCase("viewClientSettings")) {
                showSettings();
            }
            if (ev.getActionCommand().equalsIgnoreCase("quit")) {
                quit();
            }
        }
    };
}

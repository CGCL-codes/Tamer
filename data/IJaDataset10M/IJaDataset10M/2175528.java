package com.l2jserver.gsregistering;

import java.awt.HeadlessException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Map.Entry;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.l2jserver.Config;
import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.Server;
import com.l2jserver.i18n.LanguageControl;
import com.l2jserver.loginserver.GameServerTable;
import com.l2jserver.util.Util;

/**
 * @author KenM
 */
public abstract class BaseGameServerRegister {

    private boolean _loaded = false;

    private ResourceBundle _bundle;

    public static void main(String[] args) {
        Locale locale = null;
        boolean gui = true;
        boolean interactive = true;
        boolean force = false;
        boolean fallback = false;
        BaseTask task = null;
        ResourceBundle bundle = null;
        try {
            locale = Locale.getDefault();
            bundle = ResourceBundle.getBundle("gsregister.GSRegister", locale, LanguageControl.INSTANCE);
        } catch (Throwable t) {
            System.out.println("FATAL: Failed to load default translation.");
            System.exit(666);
        }
        String arg;
        for (int i = 0; i < args.length; i++) {
            arg = args[i];
            if (arg.equals("-c") || arg.equals("--cmd")) {
                gui = false;
            } else if (arg.equals("-f") || arg.equals("--force")) {
                force = true;
            } else if (arg.equals("-b") || arg.equals("--fallback")) {
                fallback = true;
            } else if (arg.equals("-r") || arg.equals("--register")) {
                gui = false;
                interactive = false;
                int id = Integer.parseInt(args[++i]);
                String dir = args[++i];
                task = new RegisterTask(id, dir, force, fallback);
            } else if (arg.equals("-u") || arg.equals("--unregister")) {
                gui = false;
                interactive = false;
                String gsId = args[++i];
                if (gsId.equalsIgnoreCase("all")) {
                    task = new UnregisterAllTask();
                } else {
                    try {
                        int id = Integer.parseInt(gsId);
                        task = new UnregisterTask(id);
                    } catch (NumberFormatException e) {
                        System.out.printf(bundle.getString("wrongUnregisterArg") + '\n', gsId);
                        System.exit(1);
                    }
                }
            } else if (arg.equals("-l") || arg.equals("--language")) {
                String loc = args[++i];
                Locale[] availableLocales = Locale.getAvailableLocales();
                Locale l;
                for (int j = 0; j < availableLocales.length && locale == null; j++) {
                    l = availableLocales[j];
                    if (l.toString().equals(loc)) {
                        locale = l;
                    }
                }
                if (locale == null) {
                    System.out.println("Specified locale '" + loc + "' was not found, using default behaviour.");
                } else {
                    try {
                        bundle = ResourceBundle.getBundle("gsregister.GSRegister", locale, LanguageControl.INSTANCE);
                    } catch (Throwable t) {
                        System.out.println("Failed to load translation ''");
                    }
                }
            } else if (arg.equals("-h") || arg.equals("--help")) {
                gui = false;
                interactive = false;
                BaseGameServerRegister.printHelp(bundle);
            }
        }
        try {
            if (gui) {
                BaseGameServerRegister.startGUI(bundle);
            } else {
                if (interactive) {
                    BaseGameServerRegister.startCMD(bundle);
                } else {
                    if (task != null) {
                        task.setBundle(bundle);
                        task.run();
                    }
                }
            }
        } catch (HeadlessException e) {
            BaseGameServerRegister.startCMD(bundle);
        }
    }

    private static void printHelp(ResourceBundle bundle) {
        String[] help = { bundle.getString("purpose"), "", bundle.getString("options"), "-b, --fallback\t\t\t\t" + bundle.getString("fallbackOpt"), "-c, --cmd\t\t\t\t" + bundle.getString("cmdOpt"), "-f, --force\t\t\t\t" + bundle.getString("forceOpt"), "-h, --help\t\t\t\t" + bundle.getString("helpOpt"), "-l, --language\t\t\t\t" + bundle.getString("languageOpt"), "-r, --register <id> <hexid_dest_dir>\t" + bundle.getString("registerOpt1"), "\t\t\t\t\t" + bundle.getString("registerOpt2"), "\t\t\t\t\t" + bundle.getString("registerOpt3"), "", "-u, --unregister <id>|all\t\t" + bundle.getString("unregisterOpt"), "", bundle.getString("credits"), bundle.getString("bugReports") + " http://www.l2jserver.com" };
        for (String str : help) {
            System.out.println(str);
        }
    }

    private static void startGUI(final ResourceBundle bundle) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                GUserInterface gui = new GUserInterface(bundle);
                gui.getFrame().setVisible(true);
            }
        });
    }

    private static void startCMD(final ResourceBundle bundle) {
        GameServerRegister cmdUi = new GameServerRegister(bundle);
        try {
            cmdUi.consoleUI();
        } catch (IOException e) {
            cmdUi.showError("I/O exception trying to get input from keyboard.", e);
        }
    }

    public BaseGameServerRegister(ResourceBundle bundle) {
        setBundle(bundle);
    }

    public void load() {
        try {
            this.loadImp();
        } catch (Exception e) {
            this.showError(this.getBundle().getString("gsListRetrieveError"), e);
        }
    }

    /**
	 * Loads Configs and SQL.<BR>
	 * This method must be invoked manually as to allow the given interface 
	 * to do in the most convenient way.<BR>
	 * 
	 * @throws Exception
	 */
    protected void loadImp() throws Exception {
        Server.serverMode = Server.MODE_LOGINSERVER;
        Config.load();
        GameServerTable.load();
        _loaded = true;
    }

    public boolean isLoaded() {
        return _loaded;
    }

    /**
     * @param bundle The bundle to set.
     */
    public void setBundle(ResourceBundle bundle) {
        _bundle = bundle;
    }

    /**
     * @return Returns the bundle.
     */
    public ResourceBundle getBundle() {
        return _bundle;
    }

    public abstract void showError(String msg, Throwable t);

    /**
     * @param id
	 * @throws SQLException 
     */
    public static void unregisterGameServer(int id) throws SQLException {
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = L2DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement("DELETE FROM gameservers WHERE server_id = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
            GameServerTable.getInstance().getRegisteredGameServers().remove(id);
            statement.close();
        } finally {
            L2DatabaseFactory.close(con);
        }
    }

    public static void unregisterAllGameServers() throws SQLException {
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = L2DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement("DELETE FROM gameservers");
            statement.executeUpdate();
            GameServerTable.getInstance().getRegisteredGameServers().clear();
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
            }
            if (con != null) {
                L2DatabaseFactory.close(con);
            }
        }
    }

    public static void registerGameServer(int id, String outDir) throws IOException {
        byte[] hexId = Util.generateHex(16);
        GameServerTable.getInstance().registerServerOnDB(hexId, id, "");
        Properties hexSetting = new Properties();
        File file = new File(outDir, "hexid.txt");
        file.createNewFile();
        OutputStream out = new FileOutputStream(file);
        hexSetting.setProperty("ServerID", String.valueOf(id));
        hexSetting.setProperty("HexID", new BigInteger(hexId).toString(16));
        hexSetting.store(out, "The HexId to Auth into LoginServer");
        out.close();
    }

    public static int registerFirstAvailable(String outDir) throws IOException {
        for (Entry<Integer, String> e : GameServerTable.getInstance().getServerNames().entrySet()) {
            if (!GameServerTable.getInstance().hasRegisteredGameServerOnId(e.getKey())) {
                BaseGameServerRegister.registerGameServer(e.getKey(), outDir);
                return e.getKey();
            }
        }
        return -1;
    }

    abstract static class BaseTask implements Runnable {

        private ResourceBundle _bundle;

        /**
         * @param bundle The bundle to set.
         */
        public void setBundle(ResourceBundle bundle) {
            _bundle = bundle;
        }

        /**
         * @return Returns the bundle.
         */
        public ResourceBundle getBundle() {
            return _bundle;
        }

        public void showError(String msg, Throwable t) {
            String title;
            if (this.getBundle() != null) {
                title = this.getBundle().getString("error");
                msg += '\n' + this.getBundle().getString("reason") + ' ' + t.getLocalizedMessage();
            } else {
                title = "Error";
                msg += "\nCause: " + t.getLocalizedMessage();
            }
            System.out.println(title + ": " + msg);
        }
    }

    static class RegisterTask extends BaseTask {

        private final int _id;

        private final String _outDir;

        private boolean _force;

        private boolean _fallback;

        public RegisterTask(int id, String outDir, boolean force, boolean fallback) {
            _id = id;
            _outDir = outDir;
            _force = force;
            _fallback = fallback;
        }

        public void setActions(boolean force, boolean fallback) {
            _force = force;
            _fallback = fallback;
        }

        /**
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            try {
                if (_id < 0) {
                    int registeredId = BaseGameServerRegister.registerFirstAvailable(_outDir);
                    if (registeredId < 0) {
                        System.out.println(getBundle().getString("noFreeId"));
                    } else {
                        System.out.printf(getBundle().getString("registrationOk") + '\n', registeredId);
                    }
                } else {
                    System.out.printf(getBundle().getString("checkingIdInUse") + '\n', _id);
                    if (GameServerTable.getInstance().hasRegisteredGameServerOnId(_id)) {
                        System.out.println(getBundle().getString("yes"));
                        if (_force) {
                            System.out.printf(getBundle().getString("forcingRegistration") + '\n', _id);
                            BaseGameServerRegister.unregisterGameServer(_id);
                            BaseGameServerRegister.registerGameServer(_id, _outDir);
                            System.out.printf(getBundle().getString("registrationOk") + '\n', _id);
                        } else if (_fallback) {
                            System.out.println(getBundle().getString("fallingBack"));
                            int registeredId = BaseGameServerRegister.registerFirstAvailable(_outDir);
                            if (registeredId < 0) {
                                System.out.println(getBundle().getString("noFreeId"));
                            } else {
                                System.out.printf(getBundle().getString("registrationOk") + '\n', registeredId);
                            }
                        } else {
                            System.out.println(getBundle().getString("noAction"));
                        }
                    } else {
                        System.out.println(getBundle().getString("no"));
                        BaseGameServerRegister.registerGameServer(_id, _outDir);
                    }
                }
            } catch (SQLException e) {
                this.showError(getBundle().getString("sqlErrorRegister"), e);
            } catch (IOException e) {
                this.showError(getBundle().getString("ioErrorRegister"), e);
            }
        }
    }

    static class UnregisterTask extends BaseTask {

        private final int _id;

        public UnregisterTask(int id) {
            _id = id;
        }

        /**
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            System.out.printf(getBundle().getString("removingGsId") + '\n', _id);
            try {
                BaseGameServerRegister.unregisterGameServer(_id);
            } catch (SQLException e) {
                this.showError(getBundle().getString("sqlErrorRegister"), e);
            }
        }
    }

    static class UnregisterAllTask extends BaseTask {

        /**
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            try {
                BaseGameServerRegister.unregisterAllGameServers();
            } catch (SQLException e) {
                this.showError(getBundle().getString("sqlErrorUnregisterAll"), e);
            }
        }
    }
}

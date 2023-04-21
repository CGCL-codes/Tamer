package fr.jbrunet.win.ndriveconnector.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import fr.jbrunet.logger.JLog;
import fr.jbrunet.win.ndriveconnector.commons.Drive;
import fr.jbrunet.win.ndriveconnector.commons.User;
import fr.jbrunet.win.ndriveconnector.utils.Constants;

/**
 * @author Julien
 *
 */
public class Config {

    private static final Logger log = JLog.getLogger(Config.class);

    private static Config instance__ = null;

    private static Locale locale = Locale.getDefault();

    private InputStream configFile = null;

    private InputStream driveFile = null;

    private InputStream authFile = null;

    private Properties properties = null;

    private static final String PROP_NAS_IP = "nas.ip";

    private static final String PROP_PING_TIMEOUT = "ping.timeout";

    private static final String PROP_PING_INTERVAL = "ping.interval";

    private static final String PROP_CHECK_PINGS = "check.pings";

    private static final String PROP_CHECK_INTERVAL_DISCONNECTED = "check.interval.disconnected";

    private static final String PROP_CHECK_INTERVAL_CONNECTED = "check.interval.connected";

    private static final String PROP_TRAY_ICON_CONNECTED = "tray.icon.connected";

    private static final String PROP_TRAY_ICON_DISCONNECTED = "tray.icon.disconnected";

    private static final String PROP_TRAY_ICON_CHECKING = "tray.icon.checking";

    private String nasIp = "192.168.1.10";

    private int pingTimeout = 2000;

    private int pingInterval = 1000;

    private int checkPings = 5;

    private int checkIntervalDisconnected = 15000;

    private int checkIntervalConnected = 120000;

    private String trayIconConnected = "img/Smiley1.gif";

    private String trayIconDisconnected = "img/Smiley2.gif";

    private String trayIconChecking = "img/Smiley2.gif";

    List<Drive> drives = new ArrayList<Drive>();

    User user = new User();

    List<String> commands = new ArrayList<String>();

    /**
	 * @throws Exception 
	 * 
	 */
    public Config() throws Exception {
        log.debug("Chargement de la configuration...");
        loadConfiguration();
        loadSecurity();
        loadDrives();
        log.debug("Fin du chargement de la configuration.");
    }

    public Locale getLocale() {
        return locale;
    }

    /**
	 * Chargement du login / mdp de connection au NAS
	 * @throws Exception
	 */
    private void loadSecurity() throws Exception {
        log.info("Chargement du fichier de sécurité...");
        authFile = new FileInputStream(new File(Constants.AUTH_FILE));
        if (authFile == null) {
            log.fatal("Impossible de charger le fichier de sécurité '" + Constants.AUTH_FILE + "'");
            throw new Exception("Can't open " + Constants.AUTH_FILE);
        }
        InputStreamReader ipsr = new InputStreamReader(authFile);
        BufferedReader br = new BufferedReader(ipsr);
        String line = br.readLine();
        String encryptPassword = "";
        if (line == null || StringUtils.trim(line).equals("")) {
            log.fatal("Fichier de sécurité est vide");
            throw new Exception("Fichier de sécurité est vide");
        } else {
            String[] splittedLine = StringUtils.split(line, Constants.DRIVE_FILE_COL_SEPARATOR);
            if (splittedLine.length > 0) user.setName(splittedLine[0]);
            if (splittedLine.length > 1) encryptPassword = splittedLine[1];
        }
        user.setPassword(encryptPassword);
        log.info("Fichier de sécurité chargé! Utilisteur de connexion = '" + user + "'");
    }

    /**
	 * Charge la liste des lecteurs reseau a mapper
	 * @throws Exception
	 */
    private void loadDrives() throws Exception {
        log.info("Chargement du fichier de définition des disques réseau...");
        driveFile = new FileInputStream(new File(Constants.DRIVE_FILE));
        if (driveFile == null) {
            log.fatal("Impossible de charger le fichier des disques réseau '" + Constants.DRIVE_FILE + "'");
            throw new Exception("Can't open " + Constants.DRIVE_FILE);
        }
        commands.add(Constants.CMD_NETWORK_DRIVE_CLEANUP);
        InputStreamReader ipsr = new InputStreamReader(driveFile);
        BufferedReader br = new BufferedReader(ipsr);
        String line;
        String myCmd = "";
        Drive drive = null;
        String[] splittedLine;
        int i = 1;
        while ((line = br.readLine()) != null) {
            if (!StringUtils.startsWith(line, "#") && !StringUtils.trim(line).equals("")) {
                splittedLine = StringUtils.split(line, Constants.DRIVE_FILE_COL_SEPARATOR);
                drive = new Drive();
                if (splittedLine.length > 0) drive.setBindedAddr(splittedLine[0]);
                if (splittedLine.length > 1) drive.setLetter(splittedLine[1]);
                if (splittedLine.length > 2) drive.setName(splittedLine[2]);
                String error = "Erreur dans fichier des disques réseau '" + Constants.DRIVE_FILE + "' : ";
                if (drive.getLetter() == null || drive.getLetter().equals("")) {
                    error += "Ligne " + i + ", lettre du lecteur non définie";
                    log.fatal(error);
                    throw new Exception(error);
                }
                if (drive.getBindedAddr() == null || drive.getBindedAddr().equals("")) {
                    error += "Ligne " + i + ", adresse réseau non définie";
                    log.fatal(error);
                    throw new Exception(error);
                }
                StringUtils.replace(drive.getBindedAddr(), "\\", "\\\\");
                drives.add(drive);
                myCmd = Constants.CMD_MAP_TEMPLATE;
                myCmd = StringUtils.replace(myCmd, "{driveLetter}", drive.getLetter());
                myCmd = StringUtils.replace(myCmd, "{networkDrive}", drive.getBindedAddr());
                myCmd = StringUtils.replace(myCmd, "{user}", user.getName());
                myCmd = StringUtils.replace(myCmd, "{password}", user.getPassword());
                log.debug("Ajout de la commande : '" + myCmd + "'");
                commands.add(myCmd);
            }
            i++;
        }
        br.close();
    }

    /**
	 * Charge la configuration du fichier configuration.ini
	 * @throws Exception 
	 */
    private void loadConfiguration() throws Exception {
        log.info("Chargement du fichier de configuration...");
        configFile = new FileInputStream(new File(Constants.CONFIGURATION_FILE));
        properties = new Properties();
        if (configFile != null) {
            try {
                properties.load(configFile);
            } catch (IOException e) {
                log.fatal("Can't read configuration file : " + Constants.CONFIGURATION_FILE, e);
                throw e;
            } catch (Exception e) {
                log.fatal("Erreur pendant le chargemement du fichier ini : " + Constants.CONFIGURATION_FILE, e);
                throw e;
            }
        } else {
            log.error("Impossible de lire le fichier '" + Constants.CONFIGURATION_FILE + "'");
            log.warn("Chargemement de la configuration par défaut...");
        }
        nasIp = properties.getProperty(PROP_NAS_IP, "192.168.1.10");
        pingTimeout = Integer.parseInt(StringUtils.deleteWhitespace(properties.getProperty(PROP_PING_TIMEOUT, "2000")));
        pingInterval = Integer.parseInt(StringUtils.deleteWhitespace(properties.getProperty(PROP_PING_INTERVAL, "1000")));
        checkPings = Integer.parseInt(StringUtils.deleteWhitespace(properties.getProperty(PROP_CHECK_PINGS, "5")));
        checkIntervalDisconnected = Integer.parseInt(StringUtils.deleteWhitespace(properties.getProperty(PROP_CHECK_INTERVAL_DISCONNECTED, "15000")));
        checkIntervalConnected = Integer.parseInt(StringUtils.deleteWhitespace(properties.getProperty(PROP_CHECK_INTERVAL_CONNECTED, "45000")));
        trayIconConnected = properties.getProperty(PROP_TRAY_ICON_CONNECTED, "img/Smiley1.gif");
        trayIconDisconnected = properties.getProperty(PROP_TRAY_ICON_DISCONNECTED, "img/Smiley2.gif");
        trayIconChecking = properties.getProperty(PROP_TRAY_ICON_CHECKING, "img/Smiley2.gif");
    }

    /**
	 * @return the instance__
	 * @throws Exception 
	 */
    public static Config getInstance() throws Exception {
        if (instance__ == null) {
            instance__ = new Config();
        }
        return instance__;
    }

    /**
	 * @return the nasIp
	 */
    public String getNasIp() {
        return nasIp;
    }

    /**
	 * @return the pingTimeout
	 */
    public int getPingTimeout() {
        return pingTimeout;
    }

    /**
	 * @return the pingInterval
	 */
    public int getPingInterval() {
        return pingInterval;
    }

    /**
	 * @return the checkPings
	 */
    public int getCheckPings() {
        return checkPings;
    }

    /**
	 * @return the checkIntervalDisconnected
	 */
    public int getCheckIntervalDisconnected() {
        return checkIntervalDisconnected;
    }

    /**
	 * @return the checkIntervalConnected
	 */
    public int getCheckIntervalConnected() {
        return checkIntervalConnected;
    }

    /**
	 * @return the trayIconConnected
	 */
    public String getTrayIconConnected() {
        return trayIconConnected;
    }

    /**
	 * @return the trayIconCisconnected
	 */
    public String getTrayIconDisconnected() {
        return trayIconDisconnected;
    }

    /**
	 * @return the trayIconChecking
	 */
    public String getTrayIconChecking() {
        return trayIconChecking;
    }

    /**
	 * @return the commands
	 */
    public List<String> getCommands() {
        return commands;
    }

    /**
	 * @return
	 */
    public String[] getUsedLetterList() {
        String[] usedLetterList = {};
        return usedLetterList;
    }

    /**
	 * @return the user
	 */
    public User getUser() {
        return user;
    }

    /**
	 * @return the drives
	 */
    public List<Drive> getDrives() {
        return drives;
    }
}

package edu.cps.messa;

import java.security.Security;
import javax.swing.UIManager;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import edu.cps.messa.gui.BootStrapDialog;
import edu.cps.messa.gui.LoginDialog;
import edu.cps.messa.gui.LoginProgress;
import edu.cps.messa.im.IMApp;
import edu.cps.messa.imclient.LoginData;
import edu.cps.messa.p2p.BootStrapData;
import edu.cps.messa.p2p.NodeIPsList;
import edu.cps.messa.p2p.P2PApp;
import edu.cps.messa.p2p.P2PAppConfig;
import edu.cps.messa.p2p.P2PAppFactory;

/**
 * 
 * This is the launcher/driver of the Messa system. The main procedure will
 * parse the command line arguments, create a new node that joins an existing
 * network or starts a new ring, and create and associated P2PApp.
 * Then, a test will start, and a message with Random destination ID will be
 * sent to the net every 3 seconds.
 * 
 * @author Alvaro J. Iradier Muro
 **/
public class Messa {

    private static BootStrapData bootstrap = null;

    private static LoginData logindata = null;

    private static boolean usegui = true;

    /**
     * Show the program command line help
     */
    private static void showHelp() {
        System.out.println("\nMessa args:");
        System.out.println("  -email       Email address");
        System.out.println("  -pass        Password");
        System.out.println("  -boothost    Host to bootstrap from");
        System.out.println("  -bootport    Port on bootstrap host");
        System.out.println("  -listenport  Port to listen for bootstrap requests");
        System.out.println("");
        System.out.println("  -nogui       Disable GUI (run text only)");
        System.out.println("  -initialnode Use this node as initial network node (no bootstrap required)");
        System.out.println("");
    }

    /**
     * Process the command line and extract the options
     * @param args The command line arguments
     */
    private static void processArgs(String[] args) {
        logindata = new LoginData();
        bootstrap = new BootStrapData();
        for (int i = 0; i < args.length; i++) {
            if (args[i].compareTo("-nogui") == 0) {
                usegui = false;
            } else if (args[i].compareTo("-initialnode") == 0) {
                bootstrap.setInitialNode(true);
            } else if (args[i].compareTo("-help") == 0 || args[i].compareTo("-h") == 0 || args[i].compareTo("--help") == 0 || args[i].compareTo("-?") == 0) {
                logindata = null;
                bootstrap = null;
                showHelp();
                return;
            } else {
                if (i + 1 >= args.length) {
                    logindata = null;
                    bootstrap = null;
                    showHelp();
                    return;
                } else if (args[i].compareTo("-email") == 0) {
                    logindata.setEmail(args[i + 1]);
                } else if (args[i].compareTo("-pass") == 0) {
                    logindata.setPassword(args[i + 1]);
                } else if (args[i].compareTo("-boothost") == 0) {
                    bootstrap.getBootstrapNode().setHost(args[i + 1]);
                } else if (args[i].compareTo("-bootport") == 0) {
                    bootstrap.getBootstrapNode().setPort(Integer.parseInt(args[i + 1]));
                } else if (args[i].compareTo("-listenport") == 0) {
                    bootstrap.setListenPort(Integer.parseInt(args[i + 1]));
                } else {
                    Logger.errorMessage("Wrong arg: " + args[i]);
                    logindata = null;
                    bootstrap = null;
                    showHelp();
                    return;
                }
                i++;
            }
        }
    }

    /**
     * Print the boot information
     */
    private static void printBootdata() {
        Logger.infoMessage("MESSA STARTING");
        Logger.infoMessage("==============");
        Logger.infoMessage("eMail: " + logindata.getEmail());
        Logger.infoMessage("Password: " + logindata.getPassword());
        Logger.infoMessage("-");
        Logger.infoMessage("Listening port: " + bootstrap.getListenPort());
        Logger.infoMessage("-");
        Logger.infoMessage("BootStrap host: " + bootstrap.getBootstrapNode().getHost());
        Logger.infoMessage("BootStrap port: " + bootstrap.getBootstrapNode().getPort());
        Logger.infoMessage("-");
    }

    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());
        Preferences.loadPreferences();
        System.setProperty("java.awt.Window.locationByPlatform", "true");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            ;
        } catch (Exception ex) {
            System.out.println("Unable to load native look and feel");
        }
        I18n.init(Preferences.getString("country"), Preferences.getString("variant"));
        Logger.setDebugLevel(5);
        NodeIPsList nodelist = Preferences.getNodeList();
        if (args.length == 0) {
            logindata = LoginDialog.getLoginData();
            if (logindata == null) System.exit(0);
            bootstrap = new BootStrapDialog().getBootStrapData(nodelist);
        } else {
            processArgs(args);
        }
        if (logindata == null || bootstrap == null) System.exit(0);
        printBootdata();
        Preferences.savePreferences();
        P2PAppConfig p2pappconfig = new P2PAppConfig(bootstrap, nodelist);
        LoginProgress.setProgress(LoginProgress.BOOTSTRAP_STEP);
        P2PApp myp2p = P2PAppFactory.getP2PApp(p2pappconfig);
        if (myp2p == null) {
            Logger.errorMessage("Error creating P2P app");
            System.exit(-1);
        }
        Logger.debugMessage("P2P app created", 1);
        IMApp imapp = new IMApp(myp2p, logindata, bootstrap.isInitialNode());
        if (!bootstrap.isInitialNode()) {
            nodelist.successfulNode(bootstrap.getBootstrapNode());
        }
        Preferences.savePreferences();
    }
}

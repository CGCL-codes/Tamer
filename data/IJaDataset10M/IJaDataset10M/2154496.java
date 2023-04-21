package wsl.mdn.server;

import wsl.fw.datasource.DataManager;
import wsl.fw.datasource.RemoteDataManagerServant;
import wsl.fw.remote.LocalServerFactory;
import wsl.fw.remote.RmiServer;
import wsl.fw.remote.SecureRegistry;
import wsl.fw.remote.SecureRegistryServer;
import wsl.fw.remote.TerminatorServant;
import wsl.fw.resource.ResId;
import wsl.fw.resource.ResourceManager;
import wsl.fw.util.CKfw;
import wsl.fw.util.Config;
import wsl.fw.util.Log;
import wsl.fw.util.Util;
import wsl.licence.ActivationKey;
import wsl.licence.LicenceKey;
import wsl.mdn.common.MdnAdminConst;
import wsl.mdn.common.MdnDataManager;
import wsl.mdn.common.MdnResourceManager;
import wsl.mdn.dataview.MdnDataCache;
import wsl.mdn.licence.MdnLicenceManager;

/**
 * Server for MDN to provide remote data access.
 */
public class MdnServer extends RmiServer {

    public static final String VERSION_NUMBER = "2.09";

    public static final ResId ERR_DATA_MANAGER = new ResId("MdnServer.error.DataManager"), TXT_TERMINATE = new ResId("MdnServer.txt.terminate"), TEXT_STARTING = new ResId("MdnServer.txt.starting"), TEXT_VERSION = new ResId("mdn.versionText"), DEBUG_CLOSING_DATA_MANAGER = new ResId("MdnServer.debug.ClosingDataManager"), DEBUG_RMI_SERVER_EXITING = new ResId("MdnServer.debug.RmiServerExiting");

    public static final long MS_TERMINATE_TIME = 6050;

    /**
	 * Constructor, inits based on the Config and command line args.
	 * @param args, command line args passed from main ().
	 */
    public MdnServer(String args[]) {
        super(args);
        if (System.getProperty("os.name").startsWith("Windows")) {
            try {
                System.loadLibrary("mdndcl");
                Log.debug("MDNDCL loaded");
            } catch (Throwable t) {
                Log.error("Failed M$ support: " + t.getMessage());
            }
        }
    }

    /**
	 * Register the servants for this server. Called by superclass.
	 */
    protected void registerServants() {
        registerServant(new RemoteDataManagerServant());
        registerServant(new RemoteLicenseManagerServant());
        registerServant(new TerminatorServant(this));
    }

    /**
	 * Main entrypoint.
	 */
    public static void main(String args[]) {
        ResourceManager.set(new MdnResourceManager());
        Log.log(TEXT_STARTING.getText() + " " + TEXT_VERSION.getText() + " " + VERSION_NUMBER);
        Config.setSingleton(MdnAdminConst.MDN_CONFIG_FILE, true);
        if (Util.getArg(args, "-stop") != null) {
            Config.getSingleton().addContext(args, CKfw.RMISERVER_CONTEXT);
            LocalServerFactory.setArgs(args);
            try {
                TerminatorServant.remoteTerminate(MdnServer.class, MS_TERMINATE_TIME);
                System.out.println(TXT_TERMINATE.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.exit(0);
        }
        SecureRegistry secReg = SecureRegistryServer.startSecureRegistryServer(args, false);
        if (secReg != null) {
            MdnServer server = new MdnServer(args);
            DataManager.setDataManager(new MdnDataManager());
            DataManager.getDataManager().setVersionCache(new MdnServerVersionCache());
            MdnDataCache.setCache(new MdnDataCache(false));
            int sleepTime = Config.getProp(MdnAdminConst.SCHEDULE_MANAGER_PERIOD, 60000);
            ScheduleManager scheduleManager = new ScheduleManager(sleepTime);
            Thread scheduleManagerThread = new Thread(scheduleManager);
            if (Config.getProp(MdnAdminConst.SCHEDULE_MANAGER_START, 1) == 1) scheduleManagerThread.start();
            server.runServer();
            scheduleManagerThread.interrupt();
            try {
                Log.debug(DEBUG_CLOSING_DATA_MANAGER.getText());
                DataManager.closeAll();
            } catch (Exception e) {
                Log.error(ERR_DATA_MANAGER.getText(), e);
            }
            SecureRegistryServer.stopSecureRegistryServer(secReg);
        }
        Log.debug("RmiServer [" + MdnServer.class.getName() + "] " + DEBUG_RMI_SERVER_EXITING.getText());
    }

    /**
	 * @return the version number.
	 */
    public static String getVersionNumber() {
        return VERSION_NUMBER;
    }
}

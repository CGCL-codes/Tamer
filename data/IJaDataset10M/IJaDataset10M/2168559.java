package uk.ac.lkl.migen.system;

import java.io.File;
import javax.swing.*;
import uk.ac.lkl.common.util.config.ConfigurationException;
import uk.ac.lkl.common.util.config.ConfigurationOutOfSyncException;
import uk.ac.lkl.common.util.config.MiGenConfiguration;
import uk.ac.lkl.common.util.restlet.RestletException;
import uk.ac.lkl.migen.system.cdst.ui.TeacherToolContainerView;
import uk.ac.lkl.migen.system.server.MiGenServerCommunicator;
import uk.ac.lkl.migen.system.util.MiGenUtilities;

/**
 * The main point of entry for the Teacher Assistance Tools. 
 * 
 * @author sergut
 */
public class TeacherToolsLauncher extends MiGenLauncher {

    public TeacherToolsLauncher(String configFilename) throws RestletException, ConfigurationException {
        super();
        if (configFilename != null) {
            processConfiguration(configFilename);
        }
        String serverName = MiGenConfiguration.getServerName();
        int serverPort = MiGenConfiguration.getServerPort();
        if (!MiGenContext.isServerCommunicatorEnabled()) {
            MiGenServerCommunicator serverCommunicator = new MiGenServerCommunicator(serverName, serverPort);
            MiGenContext.setServerCommunicator(serverCommunicator);
        }
        MiGenUtilities.initialiseFactoryRepositoryForNonGwtClient();
        TeacherToolContainerView mainPanel = new TeacherToolContainerView();
        JFrame frame = new JFrame("MiGen Teacher Tools");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private static void errorShutdown(Exception e, String msg, ExitStatus exitStatus) {
        if (e != null) e.printStackTrace();
        JOptionPane.showMessageDialog(null, msg, "Server Error", JOptionPane.ERROR_MESSAGE);
        shutdown(exitStatus);
    }

    /**
     * This method exits the server cleanly (e.g. freeing the ports, etc). 
     * 
     * All exit paths should call this method instead of System.exit() to 
     * ensure that everything is in order before closing the application. 
     * 
     * @param exitStatus the exit status to be returned to the operating system.
     */
    public static void shutdown(ExitStatus exitStatus) {
        System.out.println("MiGen server halted.");
        System.exit(exitStatus.toInt());
    }

    private static void usage() {
        System.out.println("USAGE: TeacherToolsLauncher [<configFile.ini>]");
        shutdown(ExitStatus.WRONG_CMD_LINE_ARG);
    }

    public static void main(String[] args) throws Exception {
        if (args.length > 1) usage();
        try {
            switch(args.length) {
                case 0:
                    new TeacherToolsLauncher("migen.ini");
                    break;
                case 1:
                    File configFile = new File(args[0]);
                    if (!configFile.exists() || !configFile.isFile()) errorShutdown(null, "File '" + args[0] + "' does not exist\nSystem will stop now.", ExitStatus.WRONG_CMD_LINE_ARG);
                    new TeacherToolsLauncher(args[0]);
                    break;
                default:
                    usage();
            }
        } catch (ConfigurationOutOfSyncException e) {
            errorShutdown(e, "Configuration out of sync", ExitStatus.CONFIG_ERROR);
        }
    }
}

package org.jecars.client.scripts;

/**
 *
 * @author weert
 */
public class JCS_scriptClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: jecarsClient <initJeCARS|addUser|suspendUser|listUsers> [-s=servername] [-u=loginname] [-p=loginpassword]");
            System.exit(-1);
        }
        try {
            if ("initJeCARS".equals(args[0])) {
                JCS_initJeCARS init = new JCS_initJeCARS();
                JCS_defaultScript ds = new JCS_defaultScript();
                ds.parseArguments(args);
                init.mJeCARSServer = ds.mJeCARSServer;
                init.mUsername = ds.mUsername;
                init.mPassword = ds.mPassword;
                init.startInit();
            }
            if ("addUser".equals(args[0])) {
                JCS_addUser jif = new JCS_addUser(args);
                jif.create();
            }
            if ("suspendUser".equals(args[0])) {
                JCS_suspendUser jif = new JCS_suspendUser(args);
                jif.suspend();
            }
            if ("listUsers".equals(args[0])) {
                JCS_listUsers jif = new JCS_listUsers(args);
                jif.list();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

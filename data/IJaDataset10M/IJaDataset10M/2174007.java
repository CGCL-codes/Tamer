package jtmsmon;

import jtmsctl.server.TMServer;
import jtmslib.challenge.TMChallenge;
import jtmsmon.db.DatabaseConfiguration;
import jtmsmon.db.JTMSDB;
import jtmsmon.javadb.NetworkServer;
import jtmsmon.rmi.LocalhostClientSocketFactory;
import jtmsmon.rmi.LocalhostServerSocketFactory;
import jtmsmon.rmi.RMIServer;
import jtmsmon.server.Configuration;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author th
 */
public class Main {

    public static final String VERSION = "0.7.0";

    private static TMServer tmServer;

    /**
   * Method description
   *
   *
   * @param args
   */
    public static void main(String args[]) {
        VoteMonitor voteMonitor = null;
        try {
            JTMSDB db = DatabaseConfiguration.getDatabase();
            db.init(true);
            int port = DatabaseConfiguration.getRMIPort();
            RMIServer rmiServer = new RMIServer(db);
            Registry registry = LocateRegistry.createRegistry(port, new LocalhostClientSocketFactory(), new LocalhostServerSocketFactory());
            registry.rebind("JTMSMON", rmiServer);
            System.out.println("Ready to serve RMI requests. (localhost:" + port + ")");
            Statistics.doUpdateAllRanks(db);
            Statistics.doUpdateAllScores(db);
            if ((args.length >= 1) && "-dbonly".equals(args[0])) {
                System.out.println("Starting JTMSMON in database daemon mode only.");
                if ((args.length == 2) && "-daemon".equals(args[1])) {
                    System.out.println("Starting in daemon mode");
                    new Main().goIntoDaemonMode(tmServer);
                } else {
                    System.out.println("Type [exit] to quit JTMSMON");
                    String line;
                    while (true) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                        line = reader.readLine();
                        if ("exit".equals(line)) {
                            break;
                        }
                    }
                }
            } else {
                tmServer = new TMServer(Configuration.getHost(), Configuration.getPort());
                tmServer.authenticate(Configuration.getSuperAdminUser(), Configuration.getSuperAdminPassword());
                rmiServer.init(db, tmServer);
                System.out.print("Updating current challenges ... ");
                System.out.flush();
                TMChallenge[] challenges = tmServer.getChallengeList(-1, 0);
                for (TMChallenge challenge : challenges) {
                    db.updateChallenge(challenge);
                }
                System.out.println("done.");
                RaceHandler.init(tmServer, db, tmServer.getCurrentChallengeInfo());
                voteMonitor = new VoteMonitor(tmServer);
                ChatCommands.init(tmServer, db);
                ChatPrinter.init(tmServer);
                DisplayPrinter.init(tmServer);
                tmServer.addServerListener(new ServerEventListener(tmServer, db));
                tmServer.addPlayerListener(new PlayerEventListener(tmServer, db));
                tmServer.addRaceListener(new RaceEventListener(tmServer, db));
                tmServer.enableCallbacks(true);
                System.out.println("Event handling started.");
                if ((args.length == 1) && "-daemon".equals(args[0])) {
                    System.out.println("Starting in daemon mode");
                    new Main().goIntoDaemonMode(tmServer);
                } else {
                    System.out.println("Type [exit] to quit JTMSMON");
                    String line;
                    while (true) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                        line = reader.readLine();
                        if ("exit".equals(line)) {
                            break;
                        }
                    }
                }
                tmServer.close();
                NetworkServer.shutdown();
            }
            if (voteMonitor != null) {
                voteMonitor.die();
            }
            db.close();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (voteMonitor != null) {
                voteMonitor.die();
            }
            System.exit(-1);
        }
    }

    /**
   * Method description
   *
   *
   * @param tmServer
   */
    public synchronized void goIntoDaemonMode(final TMServer tmServer) {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                System.out.println("JTMSMON is shutting down ...");
                System.out.flush();
                if (tmServer != null) {
                    try {
                        tmServer.close();
                    } catch (Exception e) {
                    }
                }
                NetworkServer.shutdown();
            }
        });
        while (true) {
            try {
                wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

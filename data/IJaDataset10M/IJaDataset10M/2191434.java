package client;

import game.boat.BoatCharacteristics;
import java.util.Collection;
import java.util.Date;
import junit.framework.TestCase;
import log.ApplicationLogger;
import server.ExceptionBadUserNameAlreadyRegistered;
import server.interfaces.IBoat;
import server.interfaces.IRace;
import chrono.Sleep;

public class ClientTests extends TestCase {

    public void testFullSession() {
        final Client cli = Client.singleton;
        try {
            cli.connectToServer("127.0.0.1", "OpenRegattaServer");
            try {
                try {
                    cli.registerUser("esppat", "password", "Patrice Espi�");
                } catch (final ExceptionBadUserNameAlreadyRegistered e) {
                }
                try {
                    cli.login("esppat", "password");
                    try {
                        final Collection<BoatCharacteristics> bc = cli.world.rmiGetBoatsCharacteristics(cli.loginToken);
                        if ((bc != null) && !bc.isEmpty()) {
                            cli.world.rmiBuyBoat(cli.loginToken, bc.iterator().next().getClassName(), "SHUSS");
                            try {
                                final Collection<String> boatNames = cli.world.rmiGetMyBoatsNames(cli.loginToken);
                                if ((boatNames != null) && !boatNames.isEmpty()) {
                                    final String boatName = boatNames.iterator().next();
                                    final IBoat boat = (IBoat) cli.registryLookup("boat " + boatName);
                                    final Collection<String> raceRMINames = cli.world.rmiGetRacesNames(cli.loginToken);
                                    if ((raceRMINames != null) && !raceRMINames.isEmpty()) {
                                        final String raceRMIName = raceRMINames.iterator().next();
                                        final IRace race = (IRace) cli.registryLookup("race " + raceRMIName);
                                        boat.rmiSetRace(cli.loginToken, race.rmiGetName(cli.loginToken));
                                        try {
                                            ApplicationLogger.info("Waiting for race start");
                                            Date lastDateForMessage = new Date();
                                            Collection<String> msgs = race.rmiGetMessageSince(cli.loginToken, null);
                                            for (final String msg : msgs) {
                                                ApplicationLogger.info("Race messages ==> " + msg);
                                            }
                                            while (!race.rmiIsStarted(cli.loginToken)) {
                                                Sleep.sleepInterruptibly(100);
                                                msgs = race.rmiGetMessageSince(cli.loginToken, lastDateForMessage);
                                                lastDateForMessage = new Date();
                                                for (final String msg : msgs) {
                                                    ApplicationLogger.info("Race messages ==> " + msg);
                                                }
                                            }
                                            ApplicationLogger.info("Race started !");
                                            while (race.rmiIsStarted(cli.loginToken)) {
                                                Sleep.sleepInterruptibly(100);
                                                msgs = race.rmiGetMessageSince(cli.loginToken, lastDateForMessage);
                                                lastDateForMessage = new Date();
                                                for (final String msg : msgs) {
                                                    ApplicationLogger.info("Race messages ==> " + msg);
                                                }
                                            }
                                            ApplicationLogger.info("Race done !");
                                        } finally {
                                            boat.rmiSetRace(cli.loginToken, null);
                                        }
                                        ApplicationLogger.info("TEST DONE");
                                    }
                                }
                            } finally {
                                cli.world.rmiImmmediateSellBoat(cli.loginToken, "SHUSS");
                            }
                        }
                    } finally {
                        cli.logout();
                    }
                } finally {
                    cli.unregisterUser("esppat", "password", "Patrice Espi�");
                }
            } finally {
                cli.disconnectFromServer();
            }
        } catch (final Exception e) {
            e.printStackTrace();
            return;
        }
    }
}

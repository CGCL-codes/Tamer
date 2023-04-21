package de.rauchhaupt.games.poker.holdem.lib.remoteplayer.client;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.Random;
import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import org.apache.log4j.Logger;
import com.sun.net.httpserver.HttpServer;
import de.rauchhaupt.games.poker.holdem.lib.remoteplayer.WebTools;
import de.rauchhaupt.games.poker.holdem.lib.remoteplayer.client.services.WSClientHoldemPlayer;
import de.rauchhaupt.games.poker.holdem.lib.remoteplayer.client.wsstubs.RemotePlayerServer;
import de.rauchhaupt.games.poker.holdem.lib.remoteplayer.client.wsstubs.WSRemotePlayerServerService;
import de.rauchhaupt.games.poker.holdem.lib.remoteplayer.util.WebServiceMonitor;
import de.rauchhaupt.games.poker.holdem.players.ThimosFirstPlayer;
import de.volkerraum.pokerbot.tableengine.holdem.HoldemPlayer;

public class RemoteHoldemRunner {

    HoldemPlayer theHoldemPlayer = null;

    HttpServer server = null;

    static Logger stdlog = Logger.getLogger(RemoteHoldemRunner.class);

    Endpoint serviceEndpoint = null;

    private static int PORT_OFFSET = 8687;

    int infoPort = 0;

    int servicePort = 0;

    String serverHostName = "localhost";

    int serverServicePort = 8686;

    String sid = "(NOT CONNECTED)";

    boolean doNotTearDown = false;

    RemotePlayerServer theRemotePlayerServer = null;

    WSClientHoldemPlayer theWSClientHoldemPlayer = null;

    public RemoteHoldemRunner(HoldemPlayer aHoldemPlayer, boolean doNotTearDown) throws IOException {
        this.doNotTearDown = doNotTearDown;
        theHoldemPlayer = aHoldemPlayer;
        establishServersAt(80);
        if (server == null) {
            Random tempRandom = new Random();
            for (int i = 2; i < 11; i++) {
                int tempPort = PORT_OFFSET + tempRandom.nextInt(500);
                establishServersAt(tempPort);
                if (server != null) {
                    stdlog.info("Succeeded in establishing at port " + tempPort + "/" + (tempPort + 1) + " at " + i + " attempt");
                    break;
                }
            }
        }
        if (server == null) {
            throw new BindException("Could not bind to a port.");
        }
        loginToServer();
    }

    public RemoteHoldemRunner(HoldemPlayer aHoldemPlayer) throws IOException {
        this(aHoldemPlayer, false);
    }

    public RemoteHoldemRunner(HoldemPlayer aHoldemPlayer, int aPort) throws Exception {
        theHoldemPlayer = aHoldemPlayer;
        establishServersAt(aPort);
        loginToServer();
    }

    private void loginToServer() {
        URL serverServiceURL = null;
        try {
            theHoldemPlayer.init();
            serverServiceURL = new URL("http://" + serverHostName + ":" + serverServicePort + "/services?wsdl");
            if (server == null) {
                stdlog.error("Refuse to login, server is not up");
                return;
            }
            stdlog.info("Connecting to server '" + serverServiceURL + "'");
            WSRemotePlayerServerService tempRemoteServerService = new WSRemotePlayerServerService(serverServiceURL, new QName("http://services.server.remoteplayer.lib.holdem.poker.games.rauchhaupt.de/", "WSRemotePlayerServerService"));
            theRemotePlayerServer = tempRemoteServerService.getRemotePlayerServerPort();
            String name = theHoldemPlayer.getName();
            String hostAdress = InetAddress.getLocalHost().getHostAddress().toString();
            String gameType = theHoldemPlayer.getGameType().toString();
            String info = WebTools.nz(theHoldemPlayer.getInfo());
            sid = theRemotePlayerServer.loginPlayer(name, "aUserName", "aPassword", hostAdress, servicePort, gameType, info);
            if (sid.startsWith("REFUSE")) {
                stdlog.info("Server refused me because of :'" + sid + "'");
                tearDown();
            } else stdlog.info("Succedded in logging in (" + sid + ")");
        } catch (Exception e) {
            stdlog.info("Could not login to '" + serverServiceURL + "'", e);
            tearDown();
        }
    }

    private void tearDown() {
        if (doNotTearDown) return;
        stdlog.info("Stopping client services");
        if (server != null) {
            server.stop(0);
            server = null;
        }
        if (serviceEndpoint != null) {
            serviceEndpoint.stop();
            serviceEndpoint = null;
        }
    }

    public void sendBetToServer(long aAmount) {
        theRemotePlayerServer.placeBetForPlayer(sid, aAmount);
    }

    private void establishServersAt(int aPort) {
        HttpServer testServer1 = null;
        HttpServer testServer2 = null;
        try {
            server = HttpServer.create(new InetSocketAddress(aPort + 1), 0);
            server.createContext("/info", new PublicInfoDataHandler(this));
            server.start();
            int tempPort = aPort;
            if (System.getProperty("WS_DEBUG") != null) {
                testServer1 = HttpServer.create(new InetSocketAddress(aPort - 1), 0);
                testServer2 = HttpServer.create(new InetSocketAddress(aPort), 0);
                if (testServer1 != null) {
                    testServer1.stop(0);
                    testServer1 = null;
                }
                if (testServer2 != null) {
                    testServer2.stop(0);
                    testServer2 = null;
                }
                tempPort--;
                new WebServiceMonitor(aPort, "localhost", tempPort);
            }
            theWSClientHoldemPlayer = new WSClientHoldemPlayer(theHoldemPlayer, this);
            serviceEndpoint = Endpoint.publish("http://localhost:" + tempPort + "/services", theWSClientHoldemPlayer);
            infoPort = aPort + 1;
            servicePort = aPort;
        } catch (Exception e) {
            stdlog.warn("Failed to establish at service port " + aPort + " because of '" + e.getMessage() + "'");
            if (testServer1 != null) {
                testServer1.stop(0);
                testServer1 = null;
            }
            if (testServer2 != null) {
                testServer2.stop(0);
                testServer2 = null;
            }
            if (server != null) {
                server.stop(0);
                server = null;
            } else stdlog.warn("Failed to establish at info port " + (aPort + 1));
            return;
        }
    }

    public static void main(String[] args) {
        try {
            new RemoteHoldemRunner(new ThimosFirstPlayer());
        } catch (Exception e) {
            stdlog.info("Unable to create a RemoteHoldemRunner");
        }
    }
}

package server;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * De ServerController is de controller van het netwerk van ons programma.
 * @author David Covemaeker, Maarten Minnebo, Tim Van Thuyne, Toon Kint
 */
public class ServerController {

    private Socket socket;

    private ServerReader sreader;

    private ServerWriter swriter;

    /**
	 * De constructor vraagt een Socket aan, aan de klasse Connector.
	 * Het maakt de ServerReader en -Writer aan en aan beide wordt de Socket meegegeven.
	 * De ServerReader wordt in een Thread uitgevoerd!
	 * @param server De gekozen server
	 * @param login De gekozen loginnaam
	 * @param port De gekozen poort
	 */
    public ServerController(String server, String login, int port) throws IOException {
        socket = Connector.connection(server, login, port);
        if (socket != null) {
            sreader = new ServerReader(socket);
            swriter = new ServerWriter(socket);
            ExecutorService threadExecutorService = Executors.newSingleThreadExecutor();
            threadExecutorService.execute(sreader);
            threadExecutorService.shutdown();
        }
    }

    /**
	 * Roept de writeLine methode van de ServerWriter op.
	 * Wordt opgeroepen om een speciale lijn door te geven. (Zoals bijv. een PING)
	 * @param line De weg te schrijven lijn
	 */
    public void writeLine(String line) throws IOException {
        swriter.writeLine(line);
    }

    /**
	 * Roept de sayLine methode van de ServerWriter op.
	 * Geeft een gewoon chatbericht door.
	 * @param channel De naam van het kanaal
	 * @param line
	 */
    public void sayLine(String channel, String line) throws IOException {
        swriter.sayLine(channel, line);
    }

    /**
	 * De methode getServerReader wordt opgeroepen in de IRCController wanneer er wordt geconnecteerd met het netwerk.
	 * De IRCController maakt van de ServerReader een observer.
	 * @return De ServerReader
	 */
    public ServerReader getServerReader() {
        return sreader;
    }

    public Socket getSocket() {
        return socket;
    }

    public void closeSocket() throws IOException {
        Connector.closeSocket();
    }
}

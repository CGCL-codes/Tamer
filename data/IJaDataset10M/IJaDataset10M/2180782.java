package studchatserver;

import java.sql.SQLException;

/**
 * Startet nötige Threads für den StudChatServer, überwacht den Status des StudChatServers und beendet gegebenenfalls die Threads des StudChatServers
 * @author Fabian Krueger
 */
public class StudChatStatusOrganizer extends Thread {

    /**
     * Verweis auf den Thread, der für den aufbau neuer Verbindungen mit den Clients verantwortlich ist
     */
    private StudChatConnector connector;

    /**
     * Verweis auf den Thread, der für die Organisation der bestehenden Verbindungen verantwortlich ist
     */
    private StudChatOrganizer organizer;

    /**
     * Zum sicheren beenden des Threads erstellter Verweis auf sich selbst. Sobald dieser Nicht mehr auf diese Instanz zeigt wird der Thread beendet.
     */
    private Thread me;

    /**
     * Initialisiert die internen Variablen mit den gegebenen Daten
     * @param connector Objekt, dass zum Aufbau neuer Verbindungen verwendet werden soll
     * @param organizer Objekt, dass zu Organisieren bestehender Verbindungen verwendet werden soll
     */
    public StudChatStatusOrganizer(StudChatConnector connector, StudChatOrganizer organizer) {
        super("StudChatStatusOrganizer");
        this.connector = connector;
        this.organizer = organizer;
    }

    /**
     * Startet die für den StudChatServer nötigen Prozesse und setzt den ServerStatus in der Datenbank auf running.<br/>
     * Außerdem wird die Überwachung des Status, der in der Datenbank eingetragen ist gestartet.
     */
    public void startStudChatServer() {
        try {
            StudChatDB.setServerStatus(true);
            connector.startThread();
            organizer.startThread();
        } catch (SQLException e) {
            System.out.print("Datenbankfehler ");
        }
        this.startThread();
    }

    /**
     * Setzt den Stauts des StudChatServers in der Datenbank auf "not running". Wodurch in den nächsten Sekunden der Server beendet werden sollte.
     */
    public static void stopStudChatServer() {
        try {
            StudChatDB.setServerStatus(false);
        } catch (SQLException e) {
            System.out.print("Datenbankfehler ");
        }
    }

    /**
     * Überprüft alle 5 Sekunden den Status des StudChatServers in der Datenbank.<br/>
     * Sollte der Status in der Datenbank "not running" sein werden laufende Prozesse des StudChatServers und folgend dieser Prozess beendet.
     */
    @Override
    public void run() {
        try {
            while (StudChatDB.isServerRunning()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
            }
            connector.stopThread();
            organizer.stopThread();
        } catch (SQLException e) {
        }
    }

    /**
     * Startet diesen Thread
     */
    private void startThread() {
        me = this;
        this.start();
    }
}

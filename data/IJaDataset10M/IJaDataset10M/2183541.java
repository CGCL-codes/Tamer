package de.boardgamesonline.bgo2.labyrinth.server;

/**
 * Klasse, die alle für die Clients notwendigen Daten enthält 
 * 
 * @author johannesthew, jojo
 *
 */
public class Status {

    /**
	 * Wird bei int getStatus() verwendet
	 */
    public static final int INIT = 0;

    /**
	 * Wird bei int getStatus() verwendet
	 */
    public static final int RUNNING = 1;

    /**
	 * Wird bei int getStatus() verwendet
	 */
    public static final int FINISHED = 2;

    /**
	 * aktuelles Spielbrett
	 */
    private LabyrinthCard[] board;

    /**
	 * Breite und Höhe des Spielfeldes
	 */
    private int width, height;

    /**
	 * Die aktuelle SchiebeKarte
	 */
    private LabyrinthCard pushCard;

    /**
	 * Array mit allen aktiven Spielern
	 * Größe = Anzahl Spieler
	 */
    private Avatar[] players;

    /**
	 * Spielernummer des Spielers, der dran ist
	 */
    private int activePlayer;

    /**
	 * Spielernummer des Gewinners, -1 falls der Gewinner noch nicht feststeht
	 */
    private int winner;

    /**
     *  hat der Spieler sein Spiel schon beendet?
	 */
    private boolean finished;

    /**
	 * Status des Spiels FINISHED/RUNNING
	 */
    private int status;

    /**
	 * Item, das als naechstes eingesammelt werden muss (Spielerabhängig)
	 */
    private Item nextTask;

    /**
	 *	Speichert für jeden Spieler die Task-Items
	 *	Und zwar hinereinander weg (damit per webservice übertragber)
	 *  D.h. die Größe ist immer 24 und als erstes kommen die Task-Items
	 *  vom Spieler mit playerID 0, dann die vom Spieler mit playerID 1 etc.
	 *  (ist komplett Spielerunabhängig, das nächste aufzusammelnde Item
	 *  für den Spieler, der den Status abfragt, steht in nextTask)
         *  
	 *  Es sind alle Items, die noch nicht eingesammelt wurden 'null'
	 *  (Da sie für keinen Spieler sichtbar sein dürfen)
	 *  es stehen die eingesammelten Items vor den 'null's im Array
	 */
    private Item[] items;

    /**
         * 0, falls die letzte Aktion ein push war. 1, falls die letzte Aktion
         * ein move war. -1, bevor die erste Aktion stattgefunden hat.
         */
    private int lastActionType;

    /**
     * Enthält Start- und Endpunkt des letzten Zuges.
     */
    private Point[] lastMove;

    /**
     * Punkt, an dem als letztes eine Spielkarte in das Spielfeld geschoben
     * wurde.
     */
    private Point lastPush;

    /**
     * Die Platzierungen der Spieler. An Position 0 steht Platzierung von
     * Spieler 1, an Position 1 die von Spieler 2 usw. -1, falls Spieler 
     * noch nicht fertig.
     */
    private int[] ranking;

    /**
     * Konstruktor f�r StatusObjekt mit aktuellen Spieldaten
     * 
     * @param pushCard
     *            aktuelle Schiebekarte
     * @param board
     *            aktuelles Spielbrettarray
     * @param players
     *            Array mit allen aktiven Spielern
     * @param activePlayer
     *            Spielernummer des Spielers, der dran ist
     * @param winner
     *            Spielernummer des Gewinners
     * @param ranking 
     *            Platzierungen der Spieler
     * @param hasFinished
     *            false, wenn dieser Spieler noch am Spielen ist true, wenn
     *            dieser Spieler gewonnen hat
     * @param status
     *            Status des Spiels FINISHED/RUNNING
     * @param nextTask
     *            Gegenstand, der als naechstes eingesammelt werden muss
     * @param items
     *            speichert von allen Spielern die Task-Items
     * @param lastActionType
     *            Typ der letzten Aktion (Push,Move,None)
     * @param lastPush
     *            Punkt an dem zuletzt geschoben wurde
     * @param lastMove
     *            Start- und Endpunkt des letzten Zuges
     * 
     */
    public Status(LabyrinthCard[] board, LabyrinthCard pushCard, Avatar[] players, int activePlayer, int winner, int[] ranking, boolean hasFinished, int status, Item nextTask, Item[] items, int lastActionType, Point[] lastMove, Point lastPush) {
        this.board = board;
        this.pushCard = pushCard;
        this.players = players;
        this.activePlayer = activePlayer;
        this.winner = winner;
        this.nextTask = nextTask;
        this.items = items;
        this.status = status;
        this.lastActionType = lastActionType;
        this.lastMove = lastMove;
        this.lastPush = lastPush;
        this.finished = hasFinished;
        this.ranking = ranking;
    }

    /**
	 * Standardkonstruktor, wird im Ausnahmefall vom GameServer
	 * verwendet.
	 */
    public Status() {
    }

    ;

    /**
	 * Gibt das aktuelle GameBoard zurueck
	 * @return s.o.
	 */
    public LabyrinthCard[] getGameBoard() {
        return board;
    }

    /**
	 * Gibt die aktuelle Schiebekarte zurueck
	 * @return s.o.
	 */
    public LabyrinthCard getPushCard() {
        return pushCard;
    }

    /**
	 * Gibt die Anzahl der aktiven Spieler zurueck
	 * @return s.o.
	 */
    public int getNumberOfPlayers() {
        return players.length;
    }

    /**
	 * Gibt die Spielernummer des Gewinners zur�ck
	 * @return null, solange Spiel noch l�uft
	 *         int Spielernummer des gewinners, sonst
	 */
    public int getWinner() {
        return winner;
    }

    /**
	 * Gibt aktuellen Spielstatus zurueck
	 * @return RUNNING, wenn Spiel noch l�uft
	 *         FINISHED, sonst
	 */
    public int getStatus() {
        return status;
    }

    /**
	 * Gibt Spielernummer des Spielers der an der Reihe ist zurueck
	 * @return s.o.
	 */
    public int getActivePlayer() {
        return activePlayer;
    }

    /**
	 * Gibt das Item zurueck, das als naechstes eingesammelt werden muss
	 * 
	 * @return Item das als naechstes eingesammelt werden muss
	 */
    public Item getNextTask() {
        return nextTask;
    }

    /**
	 * 
	 * @return gibt die Avatare als Array zurück
	 */
    public Avatar[] getPlayers() {
        return players;
    }

    /**
	 * Gibt die Task-Items des Spielers playerID zurück
	 * 
	 * @param playerID		0 bis 3
	 * 
	 * @return Array of Items
	 */
    public Item[] getItems(int playerID) {
        int numPlayers = getNumberOfPlayers();
        int numItemsPerPlayer = 24 / numPlayers;
        Item[] temp = new Item[numItemsPerPlayer];
        for (int i = 0; i < numItemsPerPlayer; i++) {
            temp[i] = this.items[playerID * numItemsPerPlayer + i];
        }
        return temp;
    }

    /**
     * @return the finished
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * @return the lastActionType
     */
    public int getLastActionType() {
        return lastActionType;
    }

    /**
     * @return the lastMove
     */
    public Point[] getLastMove() {
        return lastMove;
    }

    /**
     * @return the lastPush
     */
    public Point getLastPush() {
        return lastPush;
    }

    /**
     * @return the ranking
     */
    public int[] getRanking() {
        return ranking;
    }
}

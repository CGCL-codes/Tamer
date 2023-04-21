package WebService;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Vector;
import engine.Continent;
import engine.Map;
import engine.Country;
import engine.Player;

/**
 * @author mathias
 */
public class GameState implements Serializable {

    /**
	 * gen. serialVersionUID
	 */
    private static final long serialVersionUID = 1L;

    /** stateless * */
    public static final int NONE_STATE = -1;

    /** Initialisierungsphase * */
    public static final int INIT_STATE = 0;

    /** Tausch der Länderkarten * */
    public static final int CHANGE_COUNTRYCARDS_STATE = 1;

    /** Armeen verteilen * */
    public static final int ASSIGNEMENT_ARMIES_STATE = 2;

    /** Angriffsphase * */
    public static final int ATTACK_STATE = 3;

    /** Verteilen der Armeen * */
    public static final int REGROUP_ARMIES_STATE = 4;

    /**
	 * ID zur Identifizierung des Spiels
	 */
    private int gameID;

    /**
	 * Name des Spiels
	 */
    private String gameName;

    /**
	 * Spielphase
	 */
    private int gameState;

    /**
	 * Zähler, der erhöht wird, wenn sich etwas am Spielstatus geändert hat
	 * und das Spielfeld bei allen Clients neu aufgebaut werden soll
	 */
    private int counter = 0;

    /**
	 * Liste der Chatnachrichten
	 */
    private String[] messages = new String[25];

    /**
	 * Pos. der letzten Nachricht
	 */
    private int lastMsgPos = -1;

    /**
	 * Spielrunde (wird nach jedem Spielerwechsel erhöht)
	 * anz. wirklicher Spielrunden also gameRounde / anzSpieler
	 */
    private int gameRound = 0;

    /**
	 * SpielerID des Verteidiger(nur gesetzt im Status "Verteidigen")
	 */
    private int defenderID;

    /**
	 * CountryID des Landes, aus dem angegriffen wird (nur gesetzt im Status
	 * "Angriff")
	 */
    private int attackerCountry;

    /**
	 * CountryID des Landes, dass angegriffen wird (nur gesetzt im Status
	 * "Verteidigen")
	 */
    private int attackedCountry;

    /**
	 * Anzahl an Spielern
	 */
    private int numberOfPlayers;

    /**
	 * Array mit Informationen über die Spieler
	 */
    private Player[] players;

    /**
	 * ID des aktiven Spielers
	 */
    private int activePlayer;

    /**
	 * ID des Gewinners
	 */
    private int winnerID;

    /**
	 * wieviel Würfel wählt Angreifer
	 */
    private int attackerChosenCube;

    /**
	 * 1, wenn Angriff gelückt, 0, wenn verloren
	 */
    private int attackwon;

    /** Würfel für Angreifer, max. 3 */
    private int[] attackerCube = new int[3];

    /** Würfel für Verteidiger, max. 2 */
    private int[] defenderCube = new int[2];

    /** Anzahl der Einheiten, mit denen angegriffen wird */
    private int numberOfAttackerArmies;

    /**
	 * Anzahl der Einheiten, mit denen verteidigt wird Beim Verteidiger auch
	 * gleich Anzahl der gewählten Würfel
	 */
    private int numberOfDefenderArmies;

    /**
	 * In der letzten Angriffsphase eingenommene Länder; Wichtig für
	 * Truppenverschiebung
	 */
    private Vector newCountryIDs = new Vector();

    /**
	 * Illustration, welche Augenzahl beim Würfel welche schlägt 1: Angreifer
	 * schlägt Verteidiger 2: Verteidiger schlägt Angreifer 0: weniger Würfel
	 * gewählt
	 */
    private int[] whoBeatWho = new int[3];

    /**
	 * Zeigt an, wenn eine Verteidigung abgeschlossen ist und somit der gesamte
	 * Angriffsvorgang abgeschlossen ist
	 */
    private boolean defendFinish = false;

    /**
	 * Anzahl der eingetauschten Länderkarten (number of exchanged country
	 * cards)
	 */
    private int numOfExchangedCountryCards = 0;

    /**
	 * Verteilte Länderkarten an die einzelnen Spieler
	 */
    private Vector distributedCountryCards = new Vector();

    /**
	 * Instanz der Klasse für das Spielbrett ( Missions-, L�nder- und
	 * Kontinentsinformationen sind in der Klasse enthalten)
	 */
    private Map gameMap;

    /**
	 * Erstellungszeitpunkt des Spiels
	 */
    private Calendar startDate;

    /**
	 * Soll der Chat im MainWnd aktualisiert werden
	 */
    private boolean chatActivated;

    /**
	 * leerer Konstruktor laut Standart
	 */
    public GameState() {
    }

    /**
	 * Konstruktor zum Erstellen eines Spielstatus -- wird nur von WSRisiko
	 * aufgerufen, deswegen Erstellung mit Konstruktor m�glich --
	 * 
	 * @param gameID
	 *            Identifiziert das Spiel
	 * @param numberOfPlayers
	 *            Anzahl der Spieler
	 * @param players
	 *            Informationen über die Spieler
	 */
    public GameState(int gameID, int numberOfPlayers, Player[] players) {
        this.gameID = gameID;
        this.numberOfPlayers = numberOfPlayers;
        this.players = players;
        this.gameState = NONE_STATE;
        System.out.println("changeState: NONE_STATE");
        this.activePlayer = 0;
        this.attackerCube = null;
        this.defenderCube = null;
    }

    /**
	 * Liefert den Spieler anhand seiner ID WICHTIG: Nicht über den WebService
	 * aufrufen
	 * 
	 * @param playerID
	 *            0 bis n
	 * @return the player with the playerID
	 */
    public Player getPlayerFromID(int playerID) {
        for (int i = 0; i < players.length; i++) {
            if (players[i].getPlayerID() == playerID) {
                return players[i];
            }
        }
        return null;
    }

    /**
	 * Liefert den Kontinent anhand seiner ID WICHTIG: Nicht über den WebService
	 * aufrufen
	 * 
	 * @param continentID
	 *            0 bis n
	 * @return the Continent with the continentID
	 */
    public Continent getContinentFromID(int continentID) {
        for (int i = 0; i < gameMap.getContinents().length; i++) {
            if (gameMap.getContinents()[i].getId() == continentID) {
                return gameMap.getContinents()[i];
            }
        }
        return null;
    }

    /**
	 * Liefert das Land anhand seiner ID WICHTIG: Nicht über den WebService
	 * aufrufen
	 * 
	 * @param countryID
	 *            0 bis n
	 * @return the Continent with the continentID
	 */
    public Country getCountryFromID(int countryID) {
        for (int i = 0; i < gameMap.getCountries().length; i++) {
            if (gameMap.getCountries()[i].getId() == countryID) {
                return gameMap.getCountries()[i];
            }
        }
        return null;
    }

    /**
	 * Setzt den Wert an die Stelle stelle des Angreifer-Cubes
	 * @param attackerCube Wert
	 * @param stelle Stelle
	 */
    public void setAttackerCubeAtPosition(int attackerCube, int stelle) {
        this.attackerCube[stelle] = attackerCube;
    }

    /**
	 * liefert den Wert an der Stelle stelle urück
	 * @param stelle Die Stelle, an der der gesuchte Wert steht
	 * @return Wert des Würfels
	 */
    public int getAttackerCubeAtPosition(int stelle) {
        return attackerCube[stelle];
    }

    /**
	 * setzt den Verteidiger-Cube an der Stelle stelle
	 * @param defenderCube zu setzender Wert
	 * @param stelle zu setzende Stelle
	 */
    public void setDefenderCubeAtPosition(int defenderCube, int stelle) {
        this.defenderCube[stelle] = defenderCube;
    }

    /**
	 * 
	 * @param stelle gewünschte Stelle
	 * @return liefert den Wert an Stelle stelle zurück 
	 */
    public int getDefenderCubeAtPosition(int stelle) {
        return defenderCube[stelle];
    }

    /**
	 * Gibt den Status als String zurück
	 * 
	 * @param iState
	 *            Status als int
	 * @return Status als String
	 */
    public String stateToString(int iState) {
        switch(iState) {
            case NONE_STATE:
                return "NONE_STATE";
            case INIT_STATE:
                return "INIT_STATE";
            case ASSIGNEMENT_ARMIES_STATE:
                return "ASSIGNEMENT_ARMIES_STATE";
            case ATTACK_STATE:
                return "ATTACK_STATE";
            case REGROUP_ARMIES_STATE:
                return "REGROUP_ARMIES_STATE";
            default:
                return null;
        }
    }

    /**
	 * 
	 * @return Beginn des Spiels zur�ck
	 */
    public Calendar getStartDate() {
        return startDate;
    }

    /**
	 * Setzt Wert des Spielbeginns
	 * 
	 * @param startDate
	 *            Zeit
	 */
    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    /**
	 * @return the numberOfAttackerArmies
	 */
    public int getNumberOfAttackerArmies() {
        return numberOfAttackerArmies;
    }

    /**
	 * @param numberOfAttackerArmies
	 *            the numberOfAttackerArmies to set
	 */
    public void setNumberOfAttackerArmies(int numberOfAttackerArmies) {
        this.numberOfAttackerArmies = numberOfAttackerArmies;
    }

    /**
	 * @return the attackedCountry
	 */
    public int getAttackedCountry() {
        return attackedCountry;
    }

    /**
	 * @param attackedCountry
	 *            the attackedCountry to set
	 */
    public void setAttackedCountry(int attackedCountry) {
        this.attackedCountry = attackedCountry;
    }

    /**
	 * @return the attackwon
	 */
    public int getAttackwon() {
        return attackwon;
    }

    /**
	 * @param attackwon
	 *            the attackwon to set
	 */
    public void setAttackwon(int attackwon) {
        this.attackwon = attackwon;
    }

    /**
	 * @return the numberOfDefenderArmies
	 */
    public int getNumberOfDefenderArmies() {
        return numberOfDefenderArmies;
    }

    /**
	 * @param numberOfDefenderArmies
	 *            the numberOfDefenderArmies to set
	 */
    public void setNumberOfDefenderArmies(int numberOfDefenderArmies) {
        this.numberOfDefenderArmies = numberOfDefenderArmies;
    }

    /**
	 * @return the attackerCountry
	 */
    public int getAttackerCountry() {
        return attackerCountry;
    }

    /**
	 * @param attackerCountry
	 *            the attackerCountry to set
	 */
    public void setAttackerCountry(int attackerCountry) {
        this.attackerCountry = attackerCountry;
    }

    /**
	 * 
	 * @param whoBeatWho
	 *            Wert, wer gewonnen hat
	 * @param stelle
	 *            Da muß der Wert hin
	 */
    public void setWhoBeatWhoAtPos(int whoBeatWho, int stelle) {
        this.whoBeatWho[stelle] = whoBeatWho;
    }

    /**
	 * 
	 * @param stelle welche Stelle im Array soll zurückgegeben werden
	 * @return Stelle aus Array
	 */
    public int getWhoBeatWhoAtPosition(int stelle) {
        return this.whoBeatWho[stelle];
    }

    /**
	 * 
	 * @return whoBeatWho
	 */
    public int[] getWhoBeatWho() {
        return this.whoBeatWho;
    }

    /**
	 * @return the activePlayer
	 */
    public int getActivePlayer() {
        return activePlayer;
    }

    /**
	 * @param activePlayer
	 *            the activePlayer to set
	 */
    public void setActivePlayer(int activePlayer) {
        this.activePlayer = activePlayer;
    }

    /**
	 * @return the attackerChosenCube
	 */
    public int getAttackerChosenCube() {
        return attackerChosenCube;
    }

    /**
	 * @param attackerChosenCube
	 *            the attackerChosenCube to set
	 */
    public void setAttackerChosenCube(int attackerChosenCube) {
        this.attackerChosenCube = attackerChosenCube;
    }

    /**
	 * @return the attackerCube
	 */
    public int[] getAttackerCube() {
        return attackerCube;
    }

    /**
	 * @param attackerCube
	 *            the attackerCube to set
	 */
    public void setAttackerCube(int[] attackerCube) {
        this.attackerCube = attackerCube;
    }

    /**
	 * @return the defenderCube
	 */
    public int[] getDefenderCube() {
        return defenderCube;
    }

    /**
	 * @param defenderCube
	 *            the defenderCube to set
	 */
    public void setDefenderCube(int[] defenderCube) {
        this.defenderCube = defenderCube;
    }

    /**
	 * @return the defenderID
	 */
    public int getDefenderID() {
        return defenderID;
    }

    /**
	 * @param defenderID
	 *            the defenderID to set
	 */
    public void setDefenderID(int defenderID) {
        this.defenderID = defenderID;
    }

    /**
	 * @return the gameID
	 */
    public int getGameID() {
        return gameID;
    }

    /**
	 * @param gameID
	 *            the gameID to set
	 */
    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    /**
	 * @return the gameMap
	 */
    public Map getGameMap() {
        return gameMap;
    }

    /**
	 * @param gameMap
	 *            the gameMap to set
	 */
    public void setGameMap(Map gameMap) {
        this.gameMap = gameMap;
    }

    /**
	 * @return the gameState
	 */
    public int getGameState() {
        return gameState;
    }

    /**
	 * @param gameState
	 *            the gameState to set
	 */
    public void setGameState(int gameState) {
        this.gameState = gameState;
    }

    /**
	 * @return the numberOfPlayers
	 */
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /**
	 * @param numberOfPlayers
	 *            the numberOfPlayers to set
	 */
    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    /**
	 * @return the numOfExcangedCountryCards
	 */
    public int getNumOfExchangedCountryCards() {
        return numOfExchangedCountryCards;
    }

    /**
	 * @param numOfExchangedCountries
	 *            the numOfExchangedCountryCards to set
	 */
    public void setNumOfExchangedCountryCards(int numOfExchangedCountries) {
        this.numOfExchangedCountryCards = numOfExchangedCountries;
    }

    /**
	 * @return the players
	 */
    public Player[] getPlayers() {
        return players;
    }

    /**
	 * @param players
	 *            the players to set
	 */
    public void setPlayers(Player[] players) {
        this.players = players;
    }

    /**
	 * @return the winnerID
	 */
    public int getWinnerID() {
        return winnerID;
    }

    /**
	 * Leert newCountryIDs
	 */
    public void removeAllNewCountryIDs() {
        newCountryIDs.removeAllElements();
    }

    /**
	 * liefert aktuelle Länge des Vektors
	 * 
	 * @return Integer
	 */
    public int getNewCountryIDsLength() {
        return newCountryIDs.size();
    }

    /**
	 * Liefert Inhalt des Vektors mit den neuen LänderIDs als Integerarray
	 * zurück
	 * 
	 * @return Array
	 */
    public int[] getNewCountryIDsArray() {
        int[] temp = new int[newCountryIDs.size()];
        for (int i = 0; i < newCountryIDs.size(); i++) {
            temp[i] = Integer.parseInt(newCountryIDs.elementAt(i).toString());
        }
        return temp;
    }

    /**
	 * Fügt eine CountryID zum Vector NewCountryIDs hinzu
	 * 
	 * @param id ID die hinzugefügt werden soll
	 */
    public void addNewCountryID(int id) {
        newCountryIDs.add(id);
    }

    /**
	 * @param winnerID
	 *            the winnerID to set
	 */
    public void setWinnerID(int winnerID) {
        this.winnerID = winnerID;
    }

    /**
	 * @return the gameRound
	 */
    public int getGameRound() {
        return gameRound;
    }

    /**
	 * @param gameRound
	 *            the gameRound to set
	 */
    public void setGameRound(int gameRound) {
        this.gameRound = gameRound;
    }

    /**
	 * @return the defendFinish
	 */
    public boolean isDefendFinish() {
        return defendFinish;
    }

    /**
	 * @param defendFinish the defendFinish to set
	 */
    public void setDefendFinish(boolean defendFinish) {
        this.defendFinish = defendFinish;
    }

    /**
	 * @param whoBeatWho the whoBeatWho to set
	 */
    public void setWhoBeatWho(int[] whoBeatWho) {
        this.whoBeatWho = whoBeatWho;
    }

    /**
	 * @return the counter
	 */
    public int getCounter() {
        return counter;
    }

    /**
	 * @param counter the counter to set
	 */
    public void setCounter(int counter) {
        this.counter = counter;
    }

    /**
	 * Erhöht den Änderungszähler
	 * @return aktuelle Zählerstand nach Erhöhung
	 */
    public int incrementCounter() {
        this.counter++;
        return this.counter;
    }

    /**
	 * Senden einer Nachricht
	 * @param msg Nachricht
	 */
    public void sendMessage(String msg) {
        lastMsgPos = ++lastMsgPos % 25;
        messages[lastMsgPos] = msg;
    }

    /**
	 * liefert die Nachrichten-Liste
	 * @return messages
	 */
    public String[] getMessages() {
        return messages;
    }

    /**
	 * liefert Position der letzten Nachricht
	 * @return lastMsgCount
	 */
    public int getLastMsgPos() {
        return lastMsgPos;
    }

    /**
	 * @return the chatActivated
	 */
    public boolean isChatActivated() {
        return chatActivated;
    }

    /**
	 * @param chatActivated the chatActivated to set
	 */
    public void setChatActivated(boolean chatActivated) {
        this.chatActivated = chatActivated;
    }

    /**
	 * Fügt eine CountryCardID zum Vector distributedCountryCards hinzu
	 * 
	 * @param id ID die hinzugefügt werden soll
	 */
    public void addDistributedCountryCards(int id) {
        distributedCountryCards.add(id);
    }

    /**
	 * Entfernt ein Element aus dem Vektor distributedCountryCards
	 * @param id id der CountryCard
	 */
    public void removeIdFromDistributedCountryCards(int id) {
        distributedCountryCards.removeElement(id);
    }

    /**
	 * Liefert Inhalt des Vektors mit KontinentIDs als Integerarray
	 * @return Array
	 */
    public int[] getDistributedCountryCardsArray() {
        int[] temp = new int[distributedCountryCards.size()];
        for (int i = 0; i < distributedCountryCards.size(); i++) {
            temp[i] = Integer.parseInt(distributedCountryCards.elementAt(i).toString());
        }
        return temp;
    }

    /**
	 * @return the gameName
	 */
    public String getGameName() {
        return gameName;
    }

    /**
	 * @param gameName the gameName to set
	 */
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}

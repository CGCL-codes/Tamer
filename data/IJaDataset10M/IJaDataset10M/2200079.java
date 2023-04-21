package com.javagl4kids.learning.ct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The main manager for this game.
 * Implemented rules
 * 
 * 
 * @author alaloop
 *
 */
public class GameManager {

    public static final boolean DEBUG = false;

    public static final int NUMBER_OF_TACTICAL_CARDS = 4;

    public static final int NUMBER_OF_CARDS = 40;

    public static final int NUMBER_OF_TURNS = 10;

    public static final int NUMBER_OF_ATTACK_ARMIES = 100;

    public static final int NUMBER_OF_DEFENSE_ARMIES = 100;

    public static final String STATKEY_NBTURNS = "STATKEY_NBTURNS";

    public static final String STATKEY_WINNING_TEAM = "STATKEY_WINNING_TEAM";

    public static final String STATKEY_ATTACK_ARMIES = "STATKEY_ATTACK_ARMIES";

    public static final String STATKEY_DEFENSE_ARMIES = "STATKEY_DEFENSE_ARMIES";

    /**
	 * The game city
	 */
    private City city;

    /**
	 * The defense team
	 */
    private Team defenseTeam = null;

    /**
	 * The attack team
	 */
    private Team attackTeam = null;

    /**
	 * The strategic attack cards stack
	 */
    private Stack strategicAttackStack = null;

    /**
	 * The strategic defense cards stack
	 */
    private Stack strategicDefenseStack = null;

    /**
	 * The common card stack
	 */
    private Stack commonStack = null;

    /**
	 * The turn manager
	 */
    private TurnManager turnManager = null;

    /**
	 * Internal flag used to mark the end of the game
	 */
    private boolean gameOver = false;

    /**
	 * The ending message
	 */
    private String gameOverMsg = null;

    /**
	 * The winning team
	 */
    private int gameOverWinningTeam = 0;

    /**
	 * Game run statistics
	 */
    private HashMap<String, Object> stats = null;

    /**
	 * To know if it's first gameManager run
	 */
    private boolean firstRun = true;

    /**
	 * Private constructor for singleton pattern 
	 */
    public GameManager() {
        if (DEBUG) System.out.println("**************************************");
    }

    /**
	 * Init phasis
	 */
    public void init() throws Exception {
        stats = new HashMap<String, Object>();
        gameOver = false;
        gameOverMsg = null;
        if (firstRun) {
            if (DEBUG) System.out.println(">>> Create city");
            createCity();
            if (DEBUG) System.out.println(city.toString());
            if (DEBUG) System.out.println(">>> Create teams");
            createTeams();
            turnManager = new TurnManager(this);
            firstRun = false;
        } else {
            city.resetCursors();
            attackTeam.reset();
            defenseTeam.reset();
            turnManager.resetSteps();
        }
        if (DEBUG) System.out.println(">>> Create stacks");
        createStacks();
        if (DEBUG) System.out.println("Stack for attack with " + strategicAttackStack.getSize() + " cards");
        if (DEBUG) System.out.println("Stack for defense with " + strategicDefenseStack.getSize() + " cards");
        if (DEBUG) System.out.println("Stack for common military actions with " + commonStack.getSize() + " cards");
        if (DEBUG) System.out.println(">>> Distribute cards");
        distributeCards();
        if (DEBUG) System.out.println(attackTeam.toString());
        if (DEBUG) System.out.println(defenseTeam.toString());
    }

    /**
	 * Start
	 */
    public void start() throws Exception {
        int turns = 1;
        if (DEBUG) System.out.println("**************************************");
        if (DEBUG) System.out.println("############## Turn " + turns + " ##############");
        while (turns <= NUMBER_OF_TURNS && !isGameOver() && turnManager.manageTurn(turns)) {
            if (DEBUG) System.out.println(">>> Summary");
            if (DEBUG) System.out.println(attackTeam.toString());
            if (DEBUG) System.out.println(defenseTeam.toString());
            if (DEBUG) System.out.println(city.toString());
            turns++;
            if (DEBUG) System.out.println("############## Turn " + turns + " ##############");
        }
        if (DEBUG) System.out.println("**************************************");
        if (DEBUG) System.out.println("**************************************");
        if (isGameOver()) {
            if (DEBUG) System.out.println("Game over at turn " + turns + " : ");
            if (DEBUG) System.out.println(">>> " + getGameOverMsg());
            if (DEBUG) System.out.println("Attack team has " + attackTeam.getArmies() + " left");
            if (DEBUG) System.out.println("Defense team has " + defenseTeam.getArmies() + " left");
        } else {
            setGameOverWinningTeam(Team.TEAM_ROLE_DEFENSE);
            if (DEBUG) System.out.println("Normal ending");
            if (DEBUG) System.out.println("Attack team has " + attackTeam.getArmies() + " left");
            if (DEBUG) System.out.println("Defense team has " + defenseTeam.getArmies() + " left");
        }
        stats.put(STATKEY_NBTURNS, new Integer(turns));
        stats.put(STATKEY_ATTACK_ARMIES, new Integer(attackTeam.getArmies()));
        stats.put(STATKEY_DEFENSE_ARMIES, new Integer(defenseTeam.getArmies()));
        stats.put(STATKEY_WINNING_TEAM, getGameOverWinningTeam());
        if (DEBUG) System.out.println("____________________________________________");
    }

    /**
	 * Create the city
	 */
    private void createCity() {
        ArrayList<Cursor> cursors = new ArrayList<Cursor>();
        cursors.add(Cursor.FORT);
        cursors.add(Cursor.FEU);
        cursors.add(Cursor.INV);
        cursors.add(Cursor.STOCK);
        cursors.add(Cursor.MAL);
        cursors.add(Cursor.MORAL);
        city = new City("Carcassonne", cursors);
    }

    /**
	 * Create the two teams and their players
	 */
    private void createTeams() {
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(new Player("Defense1"));
        players.add(new Player("Defense2"));
        defenseTeam = new Team(Team.TEAM_ROLE_DEFENSE, NUMBER_OF_DEFENSE_ARMIES, players);
        players = new ArrayList<Player>();
        players.add(new Player("Attack1"));
        players.add(new Player("Attack2"));
        attackTeam = new Team(Team.TEAM_ROLE_ATTACK, NUMBER_OF_ATTACK_ARMIES, players);
    }

    /**
	 * Create all stacks for game
	 */
    private void createStacks() {
        strategicAttackStack = new Stack("Strategic cards for attack");
        strategicAttackStack.addCard(Card.CARD_AT_FORT_SAPE);
        strategicAttackStack.addCard(Card.CARD_AT_FORT_SAPE);
        strategicAttackStack.addCard(Card.CARD_AT_FORT_BELIER);
        strategicAttackStack.addCard(Card.CARD_AT_FORT_BELIER);
        strategicAttackStack.addCard(Card.CARD_AT_FORT_BELIER);
        strategicAttackStack.addCard(Card.CARD_AT_FORT_PROJ_REMP);
        strategicAttackStack.addCard(Card.CARD_AT_FORT_PROJ_REMP);
        strategicAttackStack.addCard(Card.CARD_AT_FEU_PROJ);
        strategicAttackStack.addCard(Card.CARD_AT_FEU_PROJ);
        strategicAttackStack.addCard(Card.CARD_AT_FEU_PYRO);
        strategicAttackStack.addCard(Card.CARD_AT_FEU_PYRO);
        strategicAttackStack.addCard(Card.CARD_AT_FEU_PYRO);
        strategicAttackStack.addCard(Card.CARD_AT_FEU_FLECHE);
        strategicAttackStack.addCard(Card.CARD_AT_FEU_FLECHE);
        strategicAttackStack.addCard(Card.CARD_AT_INV_ECH);
        strategicAttackStack.addCard(Card.CARD_AT_INV_ECH);
        strategicAttackStack.addCard(Card.CARD_AT_INV_ECH);
        strategicAttackStack.addCard(Card.CARD_AT_INV_ECH);
        strategicAttackStack.addCard(Card.CARD_AT_INV_TOUR);
        strategicAttackStack.addCard(Card.CARD_AT_INV_TOUR);
        strategicAttackStack.addCard(Card.CARD_AT_INV_TOUR);
        strategicAttackStack.addCard(Card.CARD_AT_STOCK_POISON);
        strategicAttackStack.addCard(Card.CARD_AT_STOCK_POISON);
        strategicAttackStack.addCard(Card.CARD_AT_STOCK_POISON);
        strategicAttackStack.addCard(Card.CARD_AT_STOCK_FEU);
        strategicAttackStack.addCard(Card.CARD_AT_STOCK_FEU);
        strategicAttackStack.addCard(Card.CARD_AT_STOCK_FEU);
        strategicAttackStack.addCard(Card.CARD_AT_MAL_CORPS);
        strategicAttackStack.addCard(Card.CARD_AT_MAL_CORPS);
        strategicAttackStack.addCard(Card.CARD_AT_MAL_CORPS);
        strategicAttackStack.addCard(Card.CARD_AT_MAL_RATS);
        strategicAttackStack.addCard(Card.CARD_AT_MAL_RATS);
        strategicAttackStack.addCard(Card.CARD_AT_MAL_RATS);
        strategicAttackStack.addCard(Card.CARD_AT_MORAL_DESTAB);
        strategicAttackStack.addCard(Card.CARD_AT_MORAL_DESTAB);
        strategicAttackStack.addCard(Card.CARD_AT_MORAL_RUMEUR);
        strategicAttackStack.addCard(Card.CARD_AT_MORAL_RUMEUR);
        strategicAttackStack.addCard(Card.CARD_AT_MORAL_SOUDOIE);
        strategicAttackStack.addCard(Card.CARD_AT_MORAL_SOUDOIE);
        strategicAttackStack.addCard(Card.CARD_AT_MORAL_RECRUTE_ADVERSE);
        strategicAttackStack.randomMix();
        strategicDefenseStack = new Stack("Strategic cards for defense");
        strategicDefenseStack.addCard(Card.CARD_DE_FORT_REPARE_REMPARTS);
        strategicDefenseStack.addCard(Card.CARD_DE_FORT_REPARE_REMPARTS);
        strategicDefenseStack.addCard(Card.CARD_DE_FORT_REPARE_PORTE);
        strategicDefenseStack.addCard(Card.CARD_DE_FORT_REPARE_PORTE);
        strategicDefenseStack.addCard(Card.CARD_DE_FORT_HUILE.adjustArmyDamage(10));
        strategicDefenseStack.addCard(Card.CARD_DE_FORT_HUILE.adjustArmyDamage(10));
        strategicDefenseStack.addCard(Card.CARD_DE_FORT_CONTRE_MINE.adjustArmyDamage(10));
        strategicDefenseStack.addCard(Card.CARD_DE_FEU_POMPIER);
        strategicDefenseStack.addCard(Card.CARD_DE_FEU_POMPIER);
        strategicDefenseStack.addCard(Card.CARD_DE_FEU_POMPIER);
        strategicDefenseStack.addCard(Card.CARD_DE_FEU_CHAINE);
        strategicDefenseStack.addCard(Card.CARD_DE_FEU_CHAINE);
        strategicDefenseStack.addCard(Card.CARD_DE_FEU_CHATEAU_DEAU);
        strategicDefenseStack.addCard(Card.CARD_DE_FEU_CHATEAU_DEAU);
        strategicDefenseStack.addCard(Card.CARD_DE_INV_FOURCHE.adjustArmyDamage(10));
        strategicDefenseStack.addCard(Card.CARD_DE_INV_FOURCHE.adjustArmyDamage(10));
        strategicDefenseStack.addCard(Card.CARD_DE_INV_HUILE.adjustArmyDamage(10));
        strategicDefenseStack.addCard(Card.CARD_DE_INV_HUILE.adjustArmyDamage(10));
        strategicDefenseStack.addCard(Card.CARD_DE_INV_HUILE.adjustArmyDamage(10));
        strategicDefenseStack.addCard(Card.CARD_DE_INV_FEU_SIEGE.adjustArmyDamage(10));
        strategicDefenseStack.addCard(Card.CARD_DE_INV_FEU_SIEGE.adjustArmyDamage(10));
        strategicDefenseStack.addCard(Card.CARD_DE_STOCK_RATION);
        strategicDefenseStack.addCard(Card.CARD_DE_STOCK_SURVEIL);
        strategicDefenseStack.addCard(Card.CARD_DE_STOCK_SURVEIL);
        strategicDefenseStack.addCard(Card.CARD_DE_STOCK_ANIMAUX);
        strategicDefenseStack.addCard(Card.CARD_DE_STOCK_FORAGE);
        strategicDefenseStack.addCard(Card.CARD_DE_STOCK_FORAGE);
        strategicDefenseStack.addCard(Card.CARD_DE_MAL_DESINF);
        strategicDefenseStack.addCard(Card.CARD_DE_MAL_DESINF);
        strategicDefenseStack.addCard(Card.CARD_DE_MAL_RENVOI);
        strategicDefenseStack.addCard(Card.CARD_DE_MAL_RENVOI);
        strategicDefenseStack.addCard(Card.CARD_DE_MAL_PREVENTION);
        strategicDefenseStack.addCard(Card.CARD_DE_MAL_PREVENTION);
        strategicDefenseStack.addCard(Card.CARD_DE_MORAL_MOTIV);
        strategicDefenseStack.addCard(Card.CARD_DE_MORAL_MOTIV);
        strategicDefenseStack.addCard(Card.CARD_DE_MORAL_SURVEIL);
        strategicDefenseStack.addCard(Card.CARD_DE_MORAL_SURVEIL);
        strategicDefenseStack.addCard(Card.CARD_DE_MORAL_SOUDOIE);
        strategicDefenseStack.addCard(Card.CARD_DE_MORAL_SOUDOIE);
        strategicDefenseStack.addCard(Card.CARD_DE_MORAL_RECRUTE_ADVERSE);
        strategicDefenseStack.randomMix();
        commonStack = new Stack("Common cards");
        for (int i = 1; i <= 10; i++) commonStack.addCard(Card.CARD_COMMON_VOID);
        commonStack.addCard(Card.CARD_COMMON_PROJ);
        commonStack.addCard(Card.CARD_COMMON_PROJ);
        commonStack.addCard(Card.CARD_COMMON_PROJ);
        commonStack.addCard(Card.CARD_COMMON_PROJ);
        commonStack.addCard(Card.CARD_COMMON_PROJ);
        commonStack.addCard(Card.CARD_COMMON_PROJ);
        commonStack.addCard(Card.CARD_COMMON_PROJ.adjustArmyDamage(2));
        commonStack.addCard(Card.CARD_COMMON_PROJ.adjustArmyDamage(2));
        commonStack.addCard(Card.CARD_COMMON_PROJ.adjustArmyDamage(2));
        commonStack.addCard(Card.CARD_COMMON_PROJ.adjustArmyDamage(2));
        commonStack.addCard(Card.CARD_COMMON_PROJ.adjustArmyDamage(2));
        commonStack.addCard(Card.CARD_COMMON_PROJ.adjustArmyDamage(2));
        commonStack.addCard(Card.CARD_COMMON_ARCHER);
        commonStack.addCard(Card.CARD_COMMON_ARCHER);
        commonStack.addCard(Card.CARD_COMMON_ARCHER);
        commonStack.addCard(Card.CARD_COMMON_ARCHER);
        commonStack.addCard(Card.CARD_COMMON_ARCHER);
        commonStack.addCard(Card.CARD_COMMON_ARCHER);
        commonStack.addCard(Card.CARD_COMMON_ARCHER.adjustArmyDamage(2));
        commonStack.addCard(Card.CARD_COMMON_ARCHER.adjustArmyDamage(2));
        commonStack.addCard(Card.CARD_COMMON_ARCHER.adjustArmyDamage(2));
        commonStack.addCard(Card.CARD_COMMON_ARCHER.adjustArmyDamage(2));
        commonStack.addCard(Card.CARD_COMMON_ARCHER.adjustArmyDamage(2));
        commonStack.addCard(Card.CARD_COMMON_ARCHER.adjustArmyDamage(2));
        commonStack.addCard(Card.CARD_COMMON_ARBA);
        commonStack.addCard(Card.CARD_COMMON_ARBA);
        commonStack.addCard(Card.CARD_COMMON_ARBA);
        commonStack.addCard(Card.CARD_COMMON_ARBA);
        commonStack.addCard(Card.CARD_COMMON_ARBA);
        commonStack.addCard(Card.CARD_COMMON_ARBA);
        commonStack.addCard(Card.CARD_COMMON_ARBA.adjustArmyDamage(2));
        commonStack.addCard(Card.CARD_COMMON_ARBA.adjustArmyDamage(2));
        commonStack.addCard(Card.CARD_COMMON_ARBA.adjustArmyDamage(2));
        commonStack.addCard(Card.CARD_COMMON_ARBA.adjustArmyDamage(2));
        commonStack.addCard(Card.CARD_COMMON_ARBA.adjustArmyDamage(2));
        commonStack.addCard(Card.CARD_COMMON_ARBA.adjustArmyDamage(2));
        commonStack.randomMix();
    }

    /**
	 * We have to distribute NUMBER_OF_CARDS cards to each team from its stack
	 * @throws Exception
	 */
    private void distributeCards() throws Exception {
        ArrayList<Card> setOfCards = null;
        ArrayList<Card> playerCards = new ArrayList<Card>();
        int nb = 0;
        int min = 0;
        int rest = 0;
        if (strategicAttackStack.getSize() < NUMBER_OF_CARDS) throw new Exception("Stack " + strategicAttackStack.getName() + " has not enough cards to distribute " + NUMBER_OF_CARDS);
        if (strategicDefenseStack.getSize() < NUMBER_OF_CARDS) throw new Exception("Stack " + strategicDefenseStack.getName() + " has not enough cards to distribute " + NUMBER_OF_CARDS);
        nb = attackTeam.getNumberOfPlayers();
        min = NUMBER_OF_CARDS / nb;
        rest = NUMBER_OF_CARDS % nb;
        setOfCards = strategicAttackStack.getRandomCards(NUMBER_OF_CARDS);
        for (int i = 0; i < nb; i++) {
            int numberOfCards = min;
            if (i < rest) numberOfCards++;
            playerCards = new ArrayList<Card>();
            for (int j = 1; j <= numberOfCards; j++) playerCards.add(setOfCards.remove(0));
            attackTeam.getPlayers().get(i).setStrategicCards(playerCards);
        }
        nb = defenseTeam.getNumberOfPlayers();
        min = NUMBER_OF_CARDS / nb;
        rest = NUMBER_OF_CARDS % nb;
        setOfCards = strategicDefenseStack.getRandomCards(NUMBER_OF_CARDS);
        for (int i = 0; i < nb; i++) {
            int numberOfCards = min;
            if (i < rest) numberOfCards++;
            playerCards = new ArrayList<Card>();
            for (int j = 1; j <= numberOfCards; j++) playerCards.add(setOfCards.remove(0));
            defenseTeam.getPlayers().get(i).setStrategicCards(playerCards);
        }
    }

    /**
	 * Order city's cursors from bigger army damages to less
	 * @return
	 * @throws Exception
	 */
    public List<Cursor> getOrderedCursors() throws Exception {
        return Cursor.getOrderedCursors(city.getCursors());
    }

    public Team getDefenseTeam() {
        return defenseTeam;
    }

    public void setDefenseTeam(Team defenseTeam) {
        this.defenseTeam = defenseTeam;
    }

    public Team getAttackTeam() {
        return attackTeam;
    }

    public void setAttackTeam(Team attackTeam) {
        this.attackTeam = attackTeam;
    }

    public Stack getStrategicAttackStack() {
        return strategicAttackStack;
    }

    public void setStrategicAttackStack(Stack strategicAttackStack) {
        this.strategicAttackStack = strategicAttackStack;
    }

    public Stack getStrategicDefenseStack() {
        return strategicDefenseStack;
    }

    public void setStrategicDefenseStack(Stack strategicDefenseStack) {
        this.strategicDefenseStack = strategicDefenseStack;
    }

    public Stack getCommonStack() {
        return commonStack;
    }

    public void setCommonStack(Stack commonStack) {
        this.commonStack = commonStack;
    }

    public TurnManager getTurnManager() {
        return turnManager;
    }

    public void setTurnManager(TurnManager turnManager) {
        this.turnManager = turnManager;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public String getGameOverMsg() {
        return gameOverMsg;
    }

    public void setGameOverMsg(String gameOverMsg) {
        this.gameOverMsg = gameOverMsg;
    }

    public int getGameOverWinningTeam() {
        return gameOverWinningTeam;
    }

    public void setGameOverWinningTeam(int gameOverWinningTeam) {
        this.gameOverWinningTeam = gameOverWinningTeam;
    }

    public HashMap<String, Object> getStats() {
        return stats;
    }

    public void setStats(HashMap<String, Object> stats) {
        this.stats = stats;
    }

    public boolean isFirstRun() {
        return firstRun;
    }

    public void setFirstRun(boolean firstRun) {
        this.firstRun = firstRun;
    }

    /**
	 * Main access to game simulation
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            GameManager manager = new GameManager();
            int totalTurn = 0;
            int attackArmies = 0;
            int defenseArmies = 0;
            int attackWins = 0;
            int nbRuns = 10000;
            for (int i = 1; i <= nbRuns; i++) {
                System.out.println("Run : " + i);
                manager.init();
                manager.start();
                HashMap<String, Object> stats = manager.getStats();
                totalTurn += (Integer) stats.get(STATKEY_NBTURNS);
                int whoWins = (Integer) stats.get(STATKEY_WINNING_TEAM);
                if (whoWins == Team.TEAM_ROLE_ATTACK) {
                    attackWins++;
                    attackArmies += (Integer) stats.get(STATKEY_ATTACK_ARMIES);
                } else defenseArmies += (Integer) stats.get(STATKEY_DEFENSE_ARMIES);
            }
            System.out.println("[[[[[[[[[[[[ FINAL STATS ]]]]]]]]]]]]]]]]]");
            System.out.println("Nb runs : " + nbRuns);
            System.out.println("Number of victories of attack  : " + attackWins);
            System.out.println("Number of victories of defense : " + (nbRuns - attackWins));
            System.out.println("Average ending turn : " + (totalTurn / nbRuns));
            if (attackWins > 0) System.out.println("Average ending attack number of armies  : " + (attackArmies / attackWins));
            if ((nbRuns - attackWins) > 0) System.out.println("Average ending defense number of armies : " + (defenseArmies / (nbRuns - attackWins)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

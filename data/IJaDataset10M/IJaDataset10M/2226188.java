package logic.common.game;

import java.util.ArrayList;
import java.util.List;
import logic.common.player.Player;
import logic.common.team.Fraction;
import logic.common.team.Team;
import logic.ships.hunter.Hunter;
import main.GameCycle;
import map.Map;
import gameStates.GameTimeListener;
import gameStates.absGamesStates.AbsIngameState;

public class Game {

    private Map map;

    private GameSetup setup;

    private Team teamA, teamB;

    private boolean isRunning;

    private Player currentPlayer;

    private int respawnTime, coundDown, currentSeconds;

    private GameCycle gameCycle;

    public Game(Map map, Team teamA, Team teamB, GameCycle gameCycle) {
        this.map = map;
        this.teamA = teamA;
        this.teamB = teamB;
        this.gameCycle = gameCycle;
        respawnTime = gameCycle.getRespawnTime();
        coundDown = gameCycle.getEndCountDown();
        teamA.setGame(this);
        teamB.setGame(this);
        addGameChangeListener();
        setup = gameCycle.getGameMode().getGameSetup(this);
    }

    private void addGameChangeListener() {
        teamA.getIngameState().addGameTimeListener(new GameTimeListener() {

            @Override
            public void respawnTimeUpdate() {
            }

            @Override
            public void gameTimeUpdate(Integer newGameTime) {
                currentSeconds = newGameTime.intValue();
            }

            @Override
            public void endTimeUpdate(Integer newEndTime) {
            }
        });
    }

    public void setupGame(AbsIngameState ingameState) {
        setup.setupGame(ingameState);
    }

    public void addStandardMissions() {
        addStandardMissions(teamA, teamB);
        addStandardMissions(teamB, teamA);
    }

    private void addStandardMissions(Team team, Team enemyTeam) {
        setup.addStandardMissions(team, enemyTeam);
    }

    public void initEndConditions() {
        setup.initEndConditions();
    }

    public void initSpawnStations(AbsIngameState ingameState) {
        setup.initSpawnStations(ingameState);
    }

    public GameSetup getGameSetup() {
        return setup;
    }

    public Team getWinnerTeam() {
        if (teamA.getKills() > teamB.getKills()) return teamA; else if (teamA.getKills() < teamB.getKills()) return teamB; else if (teamA.getDeaths() < teamB.getDeaths()) return teamA; else if (teamA.getDeaths() > teamB.getDeaths()) return teamB;
        return teamA;
    }

    public List<Hunter> getAllHunters() {
        List<Hunter> hunters = new ArrayList<Hunter>();
        initPlayers(teamA, hunters);
        initPlayers(teamB, hunters);
        return hunters;
    }

    private void initPlayers(Team team, List<Hunter> ships) {
        for (Player pilot : team.getBots()) {
            ships.add(pilot.getHunter());
        }
    }

    public int getCurrentSeconds() {
        return currentSeconds;
    }

    public int getCoundDown() {
        return coundDown;
    }

    public int getRespawnTime() {
        return respawnTime;
    }

    public void startGame() {
        isRunning = true;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void endGame() {
        isRunning = false;
    }

    public GameCycle getGameCycle() {
        return gameCycle;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player player) {
        currentPlayer = player;
    }

    public boolean canSwitchTeam(Player player) {
        if (!gameCycle.hasBalancedTeams()) return true;
        return player.getTeam().getAllPlayers().size() > player.getEnemyTeam().getAllPlayers().size();
    }

    public Team getTeamA() {
        return teamA;
    }

    public Team getTeamB() {
        return teamB;
    }

    public Team getTeam(Fraction fraction) {
        return (teamA.getFraction().equals(fraction) ? teamA : teamB);
    }

    public Team getEnemyTeam(Fraction fraction) {
        return (teamA.getFraction() == fraction ? teamB : teamA);
    }

    public Map getMap() {
        return map;
    }
}

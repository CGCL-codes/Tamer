package logic.common.game;

import java.util.ArrayList;
import java.util.List;
import interfaces.hud.BasicHUD;
import org.fenggui.StandardWidget;
import fileHandling.language.LanguageLoader;
import fileHandling.language.interfaces.HUDText;
import gameStates.absGamesStates.AbsIngameState;
import logic.common.missions.MissionFactory;
import logic.common.player.Player;
import logic.common.team.Team;
import logic.common.team.TeamController;
import logic.ships.drone.Drone;
import logic.ships.mothership.MotherShip;
import main.GameCycle;
import main.InitGame;
import map.spawnStation.SpawnPositionController;
import map.spawnStation.SpawnStation;
import map.spawnStation.stationListeners.ConquerHelpedEvent;
import map.spawnStation.stationListeners.SpawnStationListener;
import map.spawnStation.stationListeners.StationConqueredEvent;
import ai.AILevel;
import com.jme.math.Vector3f;

public abstract class GameSetup {

    protected Game game;

    protected boolean spawnStationsDone;

    protected List<Player> playerStationsList;

    public GameSetup(Game game) {
        this.game = game;
        playerStationsList = new ArrayList<Player>();
    }

    public abstract void setupGame(AbsIngameState ingameState);

    public abstract void addStandardMissions(Team team, Team enemyTeam);

    public abstract void initEndConditions();

    public abstract StandardWidget getTopHUDElement(BasicHUD hud);

    public abstract boolean hasMissions();

    public abstract boolean hasMotherShips();

    public abstract boolean hasTeamStatusWindow();

    protected void initTeamControllers(AbsIngameState ingameState) {
        if (InitGame.get().isServer()) {
            ingameState.getThreadPool().registerController(new TeamController(game.getTeamA()));
            ingameState.getThreadPool().registerController(new TeamController(game.getTeamB()));
        }
    }

    public void initSpawnStations(AbsIngameState ingameState) {
        game.getMap().generateSpawns(ingameState);
    }

    protected void registerPlayerToSpawns(Player player) {
        for (SpawnStation station : game.getMap().getSpawnStations()) {
            new SpawnPositionController(station, player);
        }
        if (hasMotherShips()) {
            addMotherShipSpawnPosController(game.getTeamA(), player);
            addMotherShipSpawnPosController(game.getTeamB(), player);
        }
    }

    protected void addMotherShipSpawnPosController(Team team, Player player) {
        MotherShip ms = team.getMotherShip();
        assert (ms != null);
        new SpawnPositionController(ms, player);
    }

    public void spawnStationsDone() {
        spawnStationsDone = true;
        for (Player p : playerStationsList) {
            registerPlayerToSpawns(p);
        }
        playerStationsList.clear();
    }

    public void registerPlayer(Player player) {
        if (spawnStationsDone) registerPlayerToSpawns(player); else playerStationsList.add(player);
    }

    protected void initSpawnStationMissions(final Team team, final Team enemyTeam) {
        for (final SpawnStation station : game.getMap().getSpawnStations()) {
            team.addMission(MissionFactory.createConquerMission(team, station));
            station.addSpawnStationListener(new SpawnStationListener() {

                @Override
                public void conquered(StationConqueredEvent event) {
                    if (event.getConquerer().getTeam() == enemyTeam) team.addMission(MissionFactory.createConquerMission(team, station));
                }

                @Override
                public void helpedConquer(ConquerHelpedEvent event) {
                }

                @Override
                public void stationNeutralized() {
                }
            });
        }
    }

    protected void addDrones(Team team, AbsIngameState ingameState) {
        if (!InitGame.get().isServer()) return;
        for (int i = 0; i < 4; i++) {
            Drone drone = team.addNewDrone();
            Vector3f droneLoc = team.getMotherShip().getRandomDroneSpawn(drone);
            if (droneLoc != null) drone.getLocalTranslation().set(droneLoc);
            ingameState.addNode(drone);
        }
    }

    protected void initAI() {
        GameCycle gameCycle = game.getGameCycle();
        if (InitGame.get().isServer() && gameCycle.botsEnabled()) {
            AILevel aiLevel = gameCycle.getAILevel();
            setUpBots(game.getTeamA(), aiLevel);
            setUpBots(game.getTeamB(), aiLevel);
        }
    }

    protected void setUpBots(Team team, AILevel aiLevel) {
        for (Player bot : team.getBots()) {
            bot.setUpBot(aiLevel);
        }
    }

    protected void attachSpawnStations(final AbsIngameState ingameState) {
        for (final SpawnStation station : game.getMap().getSpawnStations()) {
            ingameState.addNode(station);
            station.addSpawnStationListener(new SpawnStationListener() {

                @Override
                public void conquered(StationConqueredEvent event) {
                    String letter = station.getStationLetter();
                    String fraction = station.getTeam().getFraction().toString();
                    String hasConquered = LanguageLoader.get(HUDText.has_conquered_SpawnStation);
                    String text = "Team " + fraction + " " + hasConquered + " " + letter;
                    boolean goodEvent = ingameState.getPlayer().getTeam() == station.getTeam();
                    ingameState.getHUDState().getCurrentHUD().showEventText(text, goodEvent);
                }

                @Override
                public void stationNeutralized() {
                    String letter = station.getStationLetter();
                    String fraction = station.getOldTeam().getFraction().toString();
                    String lost = LanguageLoader.get(HUDText.lost_SpawnStation);
                    String text = "Team " + fraction + " " + lost + " " + letter;
                    boolean goodEvent = ingameState.getPlayer().getTeam() != station.getOldTeam();
                    ingameState.getHUDState().getCurrentHUD().showEventText(text, goodEvent);
                }

                @Override
                public void helpedConquer(ConquerHelpedEvent event) {
                }
            });
        }
    }

    protected void attachMotherShips(AbsIngameState ingameState) {
        ingameState.addNode(game.getTeamA().getMotherShip());
        ingameState.addNode(game.getTeamB().getMotherShip());
    }
}

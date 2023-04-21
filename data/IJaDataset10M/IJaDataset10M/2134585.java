package logic;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import scenario.spawnStation.SpawnStation;
import logic.ships.mothership.MotherShip;
import logic.weapons.WeaponProperties;
import logic.weapons.projectileWeapons.ProjectileWeaponProperties;
import com.jme.math.Vector3f;
import factories.ShipFactory;
import factories.WeaponFactory;
import main.InitGame;

public class Team {

    private Fraction fraction;

    private List<Player> allPlayers, bots, humanPlayers;

    private List<SpawnStation> spawnStations;

    private Player commander = null;

    private MotherShip motherShip;

    private Vector3f motherShipSpawn;

    private List<String> availableHunterNames, energyWeaponNames, projectileWeaponNames;

    private int currentTier;

    public Team(Fraction fraction) {
        currentTier = 0;
        this.fraction = fraction;
        assert (fraction != null);
        allPlayers = new LinkedList<Player>();
        bots = new LinkedList<Player>();
        humanPlayers = new LinkedList<Player>();
        spawnStations = new LinkedList<SpawnStation>();
        setWeaponNames();
        setHunterNames();
    }

    private void setHunterNames() {
        availableHunterNames = new LinkedList<String>();
        try {
            String shipsPath = "data/fractions/" + fraction.toString().toLowerCase() + "/ships";
            File f = new File(shipsPath);
            File[] fileArray = f.listFiles();
            for (int i = 0; i < fileArray.length; i++) {
                if (fileArray[i].isDirectory() && !fileArray[i].getPath().contains(".") && !fileArray[i].getName().equals("mothership")) {
                    availableHunterNames.add(fileArray[i].getName());
                }
            }
        } catch (Exception e) {
            String message = "Could not read all available Wraiths for the " + fraction.toString() + "'s team!";
            InitGame.killGame(message, "Unable to read available Wraiths", e.getMessage());
        }
    }

    private void setWeaponNames() {
        energyWeaponNames = new LinkedList<String>();
        projectileWeaponNames = new LinkedList<String>();
        try {
            String weaponsPath = "data/fractions/" + fraction.toString().toLowerCase() + "/weapons";
            File f = new File(weaponsPath);
            File[] fileArray = f.listFiles();
            String path = "data/fractions/" + fraction.toString().toLowerCase() + "/weapons/";
            for (int i = 0; i < fileArray.length; i++) {
                if (fileArray[i].isDirectory() && !fileArray[i].getPath().contains(".")) {
                    WeaponProperties prop = WeaponFactory.getWeaponProperties(path + fileArray[i].getName() + "/");
                    if (prop instanceof ProjectileWeaponProperties) projectileWeaponNames.add(fileArray[i].getName()); else energyWeaponNames.add(fileArray[i].getName());
                }
            }
        } catch (Exception e) {
            String message = "Could not read all available Weapons for the " + fraction.toString() + "'s team!";
            InitGame.killGame(message, "Unable to read available Weapons", e.getMessage());
        }
    }

    public void setMotherShip(MotherShip ship, Vector3f motherShipSpawn) {
        assert (ship != null && motherShipSpawn != null);
        motherShip = ship;
        this.motherShipSpawn = motherShipSpawn;
        motherShip.getLocalTranslation().set(motherShipSpawn);
    }

    public void addPlayer(Player newPlayer) {
        if (!allPlayers.contains(newPlayer)) {
            allPlayers.add(newPlayer);
            if (newPlayer.isHuman()) humanPlayers.add(newPlayer); else bots.add(newPlayer);
        }
    }

    public void removePlayer(Player player) {
        assert (player != null);
        allPlayers.remove(player);
        if (player.isHuman()) humanPlayers.remove(player); else bots.remove(player);
    }

    public Fraction getFraction() {
        return fraction;
    }

    public List<Player> getSortedPlayers() {
        Collections.sort(allPlayers, new PlayerComparator());
        return allPlayers;
    }

    public List<Player> getAllPlayers() {
        return allPlayers;
    }

    public List<Player> getBots() {
        return bots;
    }

    public List<Player> getHumanPlayers() {
        return humanPlayers;
    }

    public boolean hasPlayer(Player player) {
        assert (player != null);
        if (player == commander) return true;
        return allPlayers.contains(player);
    }

    public void kickBot() {
        removePlayer(bots.get(0));
    }

    public int getPlayerNumber() {
        return allPlayers.size();
    }

    public int getScore() {
        int score = 0;
        for (Player player : allPlayers) {
            score += player.getScore();
        }
        return score;
    }

    public int getDeaths() {
        int deaths = 0;
        for (Player player : allPlayers) {
            deaths += player.getDeaths();
        }
        return deaths;
    }

    public Player getCommander() {
        return commander;
    }

    public void setCommander(Player commander) {
        if (this.commander != null) {
            if (this.commander.isHuman()) this.commander.spectate(); else this.commander.becomePilot();
        }
        motherShip.removeCommanderController();
        this.commander = commander;
    }

    public void setRandomBotCommander() {
        if (bots.isEmpty()) return;
        Player bot = bots.get(new Random().nextInt(bots.size()));
        bot.becomeCommander();
    }

    public MotherShip getMotherShip() {
        return motherShip;
    }

    public Vector3f getMotherShipSpawn() {
        return motherShipSpawn;
    }

    public List<String> getAvailableHunters() {
        return availableHunterNames;
    }

    public List<String> getEnergyWeaponNames() {
        return energyWeaponNames;
    }

    public List<String> getProjectileWeaponNames() {
        return projectileWeaponNames;
    }

    public void setCurrentTier(int newTier) {
        currentTier = newTier;
    }

    public int getCurrentTier() {
        return currentTier;
    }

    public void addSpawnStation(SpawnStation station) {
        spawnStations.add(station);
    }

    public void removeSpawnStation(SpawnStation station) {
        spawnStations.remove(station);
    }

    public List<SpawnStation> getSpawnStations() {
        return spawnStations;
    }

    public int getMaxEnergyWeaponSlots() {
        int maxSlots = 0;
        String fractionName = fraction.toString().toLowerCase();
        for (String wraithName : availableHunterNames) {
            String wraithPath = "data/fractions/" + fractionName + "/ships/" + wraithName + "/";
            int slots = ShipFactory.getWeaponSlots(wraithPath, ShipFactory.ENERGY).size();
            if (slots > maxSlots) maxSlots = slots;
        }
        return maxSlots;
    }

    public int getMaxProjectileWeaponSlots() {
        int maxSlots = 0;
        String fractionName = fraction.toString().toLowerCase();
        for (String wraithName : availableHunterNames) {
            String wraithPath = "data/fractions/" + fractionName + "/ships/" + wraithName + "/";
            int slots = ShipFactory.getWeaponSlots(wraithPath, ShipFactory.PROJECTILE).size();
            if (slots > maxSlots) maxSlots = slots;
        }
        return maxSlots;
    }

    @Override
    public int hashCode() {
        return allPlayers.hashCode();
    }
}

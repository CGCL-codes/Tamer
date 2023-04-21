package main;

import java.util.List;

public class GameCycle {

    private List<String> scenarios;

    private int currentScenario;

    private int respawn;

    private int countDown;

    private int maxPlayers;

    private boolean bots;

    public GameCycle(List<String> scens, int respawn, int countDown, int maxPlayers, boolean bots) {
        this.respawn = respawn;
        this.countDown = countDown;
        this.maxPlayers = maxPlayers;
        this.bots = bots;
        scenarios = scens;
        currentScenario = 0;
    }

    public String getNextScenario() {
        if (currentScenario == scenarios.size()) {
            currentScenario = 0;
            return scenarios.get(currentScenario);
        }
        currentScenario++;
        return scenarios.get(currentScenario - 1);
    }

    public int getRespawnTime() {
        return respawn;
    }

    public int getEndCountDown() {
        return countDown;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public boolean botsEnabled() {
        return bots;
    }
}

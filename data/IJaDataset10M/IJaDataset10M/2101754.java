package org.jcrpg.world.intelligence.history;

import org.jcrpg.threed.J3DCore;

public class HistoryGenerator {

    J3DCore core = null;

    public HistoryGenerator(J3DCore core) {
        this.core = core;
    }

    public void generateHistory(int numberOfRounds) {
        for (int i = 0; i < numberOfRounds; i++) {
            generateHistoryNextRound();
        }
    }

    public void generateHistoryNextRound() {
        System.out.println("############# ROUND " + core.getGameState().getEngine().getNumberOfTurn());
        core.getGameState().getEcology().doTurn(true);
        core.getGameState().getEngine().numberOfTurn++;
    }
}

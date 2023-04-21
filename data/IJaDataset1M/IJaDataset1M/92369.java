package net.sf.doolin.app.sc.common.turn;

import java.util.List;
import net.sf.doolin.app.sc.common.model.Planet;
import net.sf.doolin.app.sc.common.model.Player;
import net.sf.doolin.app.sc.common.model.SCGame;
import net.sf.doolin.app.sc.turn.Turn;

public class PopulationPlayerTurnExecutor extends PerPlayerTurnExecutor {

    private long[] populationSteps = { 100000, 200000, 500000, 1000000, 2000000, 5000000, 10000000, 20000000, 50000000, 100000000 };

    @Override
    protected void playTurn(Turn turn, Player player, SCGame game) {
        long oldPopulation = player.getTotalPopulation();
        long totalPopulation = 0;
        List<Planet> planetList = game.getUniverse().getPlanetList();
        for (Planet planet : planetList) {
            if (planet.getOwner() == player) {
                totalPopulation += planet.getPopulation();
            }
        }
        for (long populationStep : this.populationSteps) {
            if (oldPopulation < populationStep && totalPopulation >= populationStep) {
                player.getHistory().add(new GameHistoryItem(player.getLocale(), game.getYear(), GameHistoryType.POPULATION_STEP, populationStep));
                break;
            }
        }
        player.setTotalPopulation(totalPopulation);
    }
}

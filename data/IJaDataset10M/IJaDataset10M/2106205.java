package net.slashie.expedition.world.agents;

import java.util.Calendar;
import java.util.List;
import net.slashie.expedition.domain.Expedition;
import net.slashie.expedition.domain.Town;
import net.slashie.expedition.game.ExpeditionGame;
import net.slashie.expedition.world.FoodConsumer;
import net.slashie.serf.action.Action;
import net.slashie.serf.action.ActionSelector;
import net.slashie.serf.action.Actor;

@SuppressWarnings("serial")
public class HourShiftAgent extends Actor {

    public static final int TICKS_PER_HOUR = 12;

    protected static final Action BEAT = new Action() {

        private static final long serialVersionUID = 1L;

        @Override
        public void execute() {
            Calendar currentTime = ExpeditionGame.getCurrentGame().getGameTime();
            int day = currentTime.get(Calendar.DAY_OF_YEAR);
            int month = currentTime.get(Calendar.MONTH);
            currentTime.add(Calendar.HOUR, 1);
            if (currentTime.get(Calendar.DAY_OF_YEAR) != day) {
                if (currentTime.get(Calendar.MONTH) > month) {
                    ExpeditionGame.getCurrentGame().monthChange();
                }
                List<FoodConsumer> foodConsumers = ExpeditionGame.getCurrentGame().getFoodConsumers();
                for (int i = 0; i < foodConsumers.size(); i++) {
                    foodConsumers.get(i).consumeFood();
                }
                for (Town town : ExpeditionGame.getCurrentGame().getExpedition().getTowns()) {
                    town.gatherResources();
                }
                Expedition expedition = ExpeditionGame.getCurrentGame().getExpedition();
                expedition.dayShift();
            }
        }

        @Override
        public String getID() {
            return null;
        }

        @Override
        public int getCost() {
            return TICKS_PER_HOUR;
        }
    };

    private static final ActionSelector SELECTOR = new ActionSelector() {

        private static final long serialVersionUID = 1L;

        public ActionSelector derive() {
            return null;
        }

        public String getID() {
            return null;
        }

        public Action selectAction(Actor who) {
            return BEAT;
        }
    };

    @Override
    public String getClassifierID() {
        return "HOUR_SHIFT";
    }

    @Override
    public String getDescription() {
        return "Hour Shift";
    }

    @Override
    public ActionSelector getSelector() {
        return SELECTOR;
    }
}

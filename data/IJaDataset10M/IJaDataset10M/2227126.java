package emil.poker.test.testPlayers;

import emil.poker.Decision;
import emil.poker.SimpleConsolePlayer;

public class CheckOrFoldPlayer extends SimpleConsolePlayer {

    public CheckOrFoldPlayer() {
        super();
    }

    public CheckOrFoldPlayer(String name) {
        super(name);
    }

    @Override
    public Decision makeDecision(Decision[] list) {
        displayInfo();
        for (Decision decision : list) {
            if (decision.equals(Decision.CHECK)) {
                return check();
            }
        }
        return fold();
    }

    @Override
    public String getName() {
        if (super.getName() != null) {
            return super.getName();
        } else {
            return this.getClass().getName();
        }
    }
}

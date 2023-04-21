package net.sourceforge.strategema.games.actions;

import net.sourceforge.strategema.games.Game;
import net.sourceforge.strategema.games.GameAction;
import net.sourceforge.strategema.games.GameActionHandler;
import net.sourceforge.strategema.games.GameEquipment;
import net.sourceforge.strategema.games.VolatileGameState;

public class PlaceTilesAction extends GameAction {

    @Override
    public void execute(final Game<?> game, final GameEquipment<?> equipment, final VolatileGameState state) {
    }

    @Override
    public Object[] getObjects() {
        return null;
    }

    @Override
    public boolean request(final GameActionHandler handler, final VolatileGameState state) {
        return handler.placeTile(this, state);
    }
}

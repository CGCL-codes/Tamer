package org.perfectday.logicengine.model.state.event;

import java.util.List;
import org.apache.log4j.Logger;
import org.perfectday.logicengine.core.Game;
import org.perfectday.logicengine.model.command.Command;
import org.perfectday.logicengine.model.state.State;
import org.perfectday.logicengine.model.unittime.UnitTime;

/**
 * Evento que en los estados de tipo activos implica la ejecuciónde su acción
 * @author Miguel Angel Lopez Montellano ( alakat@gmail.com )
 */
public class StateActivationAccident extends StateAccident {

    private static final Logger logger = Logger.getLogger(StateActivationAccident.class);

    public StateActivationAccident(State state, UnitTime mUnitTime) {
        super(state, mUnitTime);
    }

    @Override
    public void doAccident(List<Command> commands) {
        this.getState().doState(null, commands);
    }

    @Override
    public void doAccident(List<Command> commands, Game game) throws Exception {
        logger.info("Se desapila un estado  " + this.toString());
        this.doAccident(commands);
        if (!this.getState().getMini().isAlive()) {
            commands.addAll(game.searchDead());
        }
    }
}

package frsf.cidisi.faia.examples.search.pacman.actions;

import frsf.cidisi.faia.examples.search.pacman.*;
import frsf.cidisi.faia.agent.search.SearchAction;
import frsf.cidisi.faia.agent.search.SearchBasedAgentState;
import frsf.cidisi.faia.state.AgentState;
import frsf.cidisi.faia.state.EnvironmentState;

public class GoDown extends SearchAction {

    /**
     * See comments in the Eat class.
     */
    @Override
    public SearchBasedAgentState execute(SearchBasedAgentState s) {
        PacmanAgentState pacmanState = (PacmanAgentState) s;
        pacmanState.increaseVisitedCellsCount();
        int row = pacmanState.getRowPosition();
        int col = pacmanState.getColumnPosition();
        if (row == 3) {
            row = 0;
        } else {
            row = row + 1;
        }
        pacmanState.setRowPosition(row);
        if (pacmanState.getWorldPosition(row, col) == PacmanPerception.UNKNOWN_PERCEPTION) {
            pacmanState.setWorldPosition(row, col, PacmanPerception.EMPTY_PERCEPTION);
        }
        return pacmanState;
    }

    /**
     * See comments in the Eat class.
     */
    @Override
    public EnvironmentState execute(AgentState ast, EnvironmentState est) {
        PacmanEnvironmentState environmentState = (PacmanEnvironmentState) est;
        PacmanAgentState pacmanState = ((PacmanAgentState) ast);
        pacmanState.increaseVisitedCellsCount();
        int row = environmentState.getAgentPosition()[0];
        int col = environmentState.getAgentPosition()[1];
        if (row == 3) {
            row = 0;
        } else {
            row = row + 1;
        }
        pacmanState.setRowPosition(row);
        environmentState.setAgentPosition(new int[] { row, col });
        return environmentState;
    }

    /**
     * See comments in the Eat class.
     */
    @Override
    public Double getCost() {
        return new Double(0);
    }

    /**
     * See comments in the Eat class.
     */
    @Override
    public String toString() {
        return "GoDown";
    }
}

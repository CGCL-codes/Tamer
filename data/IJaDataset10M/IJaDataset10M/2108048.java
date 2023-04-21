package frsf.cidisi.faia.examples.planning.cubes.actions;

import frsf.cidisi.faia.agent.planning.PlanningAction;
import frsf.cidisi.faia.examples.planning.cubes.CubesEnvironmentState;
import frsf.cidisi.faia.state.AgentState;
import frsf.cidisi.faia.state.EnvironmentState;

public class PutOn extends PlanningAction {

    private String cube1;

    private String cube2;

    public PutOn(String[] parameters) {
        this.cube1 = parameters[0];
        this.cube2 = parameters[1];
    }

    @Override
    public EnvironmentState execute(AgentState ast, EnvironmentState est) {
        CubesEnvironmentState environmentState = (CubesEnvironmentState) est;
        environmentState.on(this.cube1, this.cube2);
        return environmentState;
    }

    @Override
    public String toString() {
        return "putOn(" + this.cube1 + "," + this.cube2 + ")";
    }
}

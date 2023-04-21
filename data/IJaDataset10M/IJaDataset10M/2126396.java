package frsf.cidisi.faia.examples.situationcalculus.pacman;

import frsf.cidisi.faia.agent.Action;
import frsf.cidisi.faia.agent.ActionFactory;
import frsf.cidisi.faia.examples.situationcalculus.pacman.actions.*;

/**
 * When the Solver (Calculus in this case) ask the agent for the best action,
 * it receives from the knowledge base a string representation of it. However
 * the 'solve' method must return an Action object.
 * This class is used to turn an string representation of the action to an
 * Action object.
 */
public class PacmanLogicAccionFactory extends ActionFactory {

    private static PacmanLogicAccionFactory instance;

    private PacmanLogicAccionFactory() {
    }

    public static PacmanLogicAccionFactory getInstance() {
        if (instance == null) {
            instance = new PacmanLogicAccionFactory();
        }
        return instance;
    }

    @Override
    public Action stringToAction(String stringAction) {
        if (stringAction.equals("godown")) {
            return new GoDown();
        } else if (stringAction.equals("goup")) {
            return new GoUp();
        } else if (stringAction.equals("goright")) {
            return new GoRight();
        } else if (stringAction.equals("goleft")) {
            return new GoLeft();
        } else if (stringAction.equals("eat")) {
            return new Eat();
        } else if (stringAction.equals("fight")) {
            return new Fight();
        }
        return null;
    }

    @Override
    public String endActionString() {
        return "noAction";
    }
}

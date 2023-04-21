package model.states;

import model.planet.elements.Pooglin;
import model.*;

public abstract class State {

    public abstract void setup(Pooglin pooglin);

    public abstract void process(Pooglin pooglin);

    public abstract void dismiss(Pooglin pooglin);

    public abstract String getCode();

    public abstract boolean allowedNextState(int state);
}

package command;

import model.Ant;

public class Drop implements Command {

    int state;

    public Drop(int state) {
        this.state = state;
    }

    public void step(Ant a) {
        if (a.getHasFood()) {
            a.getPosition().setFood(a.getPosition().getFood() + 1);
            a.setHasFood(false);
        }
        a.setState(state);
    }

    public String toString() {
        return "Drop " + state;
    }
}

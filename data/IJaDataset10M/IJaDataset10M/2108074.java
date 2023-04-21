package nl.uva.lap.saf.ast.action;

import java.util.ArrayList;
import java.util.List;
import nl.uva.lap.saf.ast.Visitor;

public class Choose extends Action {

    private List<Action> actions = new ArrayList<Action>();

    public Choose(List<Action> actions) {
        this.actions = actions;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public List<Action> getOptions() {
        return actions;
    }

    @Override
    public boolean contains(List<String> elements) {
        for (Action action : actions) if (elements.contains(action)) return true;
        return false;
    }
}

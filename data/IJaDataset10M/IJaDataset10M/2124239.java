package nl.uva.saf.fdl.ast;

import java.util.ArrayList;
import nl.uva.saf.fdl.ITreeVisitor;

public class FightChoice extends Choice {

    public FightChoice(ArrayList<Action> actions) {
        super(actions);
    }

    @Override
    public void accept(ITreeVisitor visitor) {
        visitor.visit(this);
    }
}

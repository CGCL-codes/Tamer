package nl.tamasja.uva.saf.fighter.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import nl.tamasja.uva.saf.fighter.FighterBot;

public class Choose implements IBehaviourAction {

    private List<IBehaviourAction> actionList;

    public Choose() {
        this.actionList = new ArrayList<IBehaviourAction>();
    }

    public Choose(IBehaviourAction a1, IBehaviourAction a2) {
        this();
        AddChoice(a1);
        AddChoice(a2);
    }

    public void AddChoice(IBehaviourAction behaviourAction) {
        actionList.add(behaviourAction);
    }

    @Override
    public void execute(FighterBot self) {
        if (!actionList.isEmpty()) {
            Collections.shuffle(actionList);
            actionList.get(0).execute(self);
        }
    }
}

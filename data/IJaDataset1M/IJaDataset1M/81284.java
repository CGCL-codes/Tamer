package saf.fight.action;

import saf.ast.action.Action;
import saf.ast.action.ChooseAction;
import saf.ast.action.SingleAction;

public class FightAct extends FAction {

    public FightAct(String fileName) {
        img = readFighterImage(fileName);
        ;
    }

    public void setSAFFightAct(Action act) {
        if (act.getFightAction() instanceof ChooseAction) {
            singleFightAct = getRandomAction((ChooseAction) act.getFightAction());
            setFighterImage(singleFightAct.getSingleAct());
        } else {
            singleFightAct = (SingleAction) act.getFightAction();
            setFighterImage(singleFightAct.getSingleAct());
        }
    }

    private void setFighterImage(String fightActName) {
        String imageName = fightActName + "." + suffix.trim();
        img = readFighterImage(imageName);
    }
}

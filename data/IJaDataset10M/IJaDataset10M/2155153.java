package hokutonorogue.character.action;

import java.util.*;
import hokutonorogue.character.*;
import hokutonorogue.game.*;
import hokutonorogue.level.tile.*;
import hokutonorogue.object.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Alessio Carotenuto
 * @version 1.0
 */
public class UseAction extends Action implements ActionPerformer {

    public static final String USE_ACTION = "USE";

    protected CharacterModel at = null;

    protected double totalCost = 0;

    public UseAction(CharacterModel character, Integer index, boolean userVisible) {
        super(character, index, userVisible);
    }

    public boolean _canExecute(Tile target, List params) {
        return character.getInventory().containsUsableObjects() && target != null && target.getCharacter() != null;
    }

    protected void _execute(final Tile target, List params) throws ExecutionException {
        at = target.getCharacter();
        totalCost = 0;
        ChoiceMenu objectsMenu = new MultipleChoiceMenu(MainGame.getInstance().parent, this, MainGame.getInstance());
        objectsMenu.setMessage("CHOOSE OBJECT TO USE:");
        List<UsableHokutoObject> objects = character.getInventory().getUsableObjects();
        for (UsableHokutoObject obj : objects) {
            if (obj.hasInfo()) {
                objectsMenu.addChoice(new SimpleChoice(obj.getName() + " - " + obj.getInfo()[0].toString(), obj, obj.canBeUseOn(character, at)));
            } else {
                objectsMenu.addChoice(new SimpleChoice(obj.getName(), obj, obj.canBeUseOn(character, at)));
            }
        }
        objectsMenu.start();
        at = null;
    }

    protected void _execute(ActionTarget target, List params) throws ExecutionException {
        UsableHokutoObject object = (UsableHokutoObject) target;
        ActionTarget actionTarget = (ActionTarget) params.get(0);
        object.useOn(character, actionTarget);
        if (object.isConsumed() && character.getInventory().containsObject(object)) {
            character.getInventory().removeObject(object);
        }
        totalCost = object.getCost();
    }

    public boolean _canExecute(ActionTarget target, List params) {
        boolean ret = false;
        if (target != null && target instanceof UsableHokutoObject && params != null && params.size() > 0 && params.get(0) instanceof ActionTarget) {
            ret = ((UsableHokutoObject) target).canBeUseOn(character, (ActionTarget) params.get(0));
        }
        return ret;
    }

    public String getName() {
        return USE_ACTION;
    }

    public double getCost() {
        return totalCost;
    }

    public boolean requireSeparateThread() {
        return false;
    }

    public void performAction(ChoiceMenu choiceMenu, Choice choice) {
        UsableHokutoObject obj = (UsableHokutoObject) choice.getUserObject();
        boolean used = obj.useOn(character, at);
        if (used) {
            totalCost = totalCost + obj.getCost();
        }
        if (obj instanceof Stackable) {
            choice.setLabel(obj.getName());
        }
        if (used && obj.isConsumed() && character.getInventory().containsObject(obj)) {
            character.getInventory().removeObject(obj);
            choiceMenu.removeChoice(choice);
            if (choiceMenu.size() == 0) {
                choiceMenu.finish();
            }
        }
        for (Choice cc : choiceMenu.getChoices()) {
            obj = (UsableHokutoObject) cc.getUserObject();
            cc.setEnabled(obj.canBeUseOn(character, at));
            if (obj.hasInfo()) {
                cc.setLabel(obj.getName() + " - " + obj.getInfo()[0].toString());
            }
        }
        choiceMenu.resetCurrentChoice();
    }

    public FightingStyle getStyle() {
        return null;
    }

    @Override
    public float getMinRange() {
        return 0;
    }

    @Override
    public float getMaxRange() {
        return 1;
    }
}

package org.nakedobjects.viewer.skylark.basic;

import org.nakedobjects.NakedObjects;
import org.nakedobjects.object.NakedClass;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.reflect.Action;
import org.nakedobjects.viewer.skylark.MenuOption;
import org.nakedobjects.viewer.skylark.MenuOptionSet;

public class ClassOption {

    public static void menuOptions(NakedObjectSpecification specificaton, MenuOptionSet menuOptionSet) {
        NakedClass nakedClass = NakedObjects.getObjectManager().getNakedClass(specificaton);
        NakedObject classAdapter = NakedObjects.getObjectLoader().getAdapterForElseCreateAdapterForTransient(nakedClass);
        Action[] actions;
        actions = specificaton.getClassActions(Action.USER);
        for (int i = 0; i < actions.length; i++) {
            addOption(classAdapter, menuOptionSet, actions[i], MenuOptionSet.OBJECT);
        }
        actions = classAdapter.getSpecification().getObjectActions(Action.USER);
        for (int i = 0; i < actions.length; i++) {
            addOption(classAdapter, menuOptionSet, actions[i], MenuOptionSet.OBJECT);
        }
        actions = specificaton.getClassActions(Action.EXPLORATION);
        if (actions.length > 0) {
            for (int i = 0; i < actions.length; i++) {
                addOption(classAdapter, menuOptionSet, actions[i], MenuOptionSet.EXPLORATION);
            }
        }
        actions = classAdapter.getSpecification().getObjectActions(Action.EXPLORATION);
        if (actions.length > 0) {
            for (int i = 0; i < actions.length; i++) {
                addOption(classAdapter, menuOptionSet, actions[i], MenuOptionSet.EXPLORATION);
            }
        }
    }

    private static void addOption(NakedObject cls, MenuOptionSet menuOptionSet, Action action, int type) {
        MenuOption option;
        if (action.parameters().length == 0) {
            option = ImmediateObjectOption.createOption(action, cls);
        } else {
            option = DialogedObjectOption.createOption(action, cls);
        }
        if (option != null) {
            menuOptionSet.add(type, option);
        }
    }
}

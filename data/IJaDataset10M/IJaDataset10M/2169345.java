package engine;

import library.StandardPackage.INTEGER;

public class Case {

    private int value;

    public Case(INTEGER what) {
        value = what.getValue();
    }

    public boolean when(Choice... choices) {
        for (Choice choice : choices) {
            if (choice.covers(value)) {
                return true;
            }
        }
        return false;
    }
}

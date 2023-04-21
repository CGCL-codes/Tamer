package jme3clogic.basic.conditions;

import jme3clogic.Condition;

public class Switch extends Condition {

    private boolean state;

    public Switch(boolean initialState) {
        state = initialState;
    }

    public void switchValue() {
        state = !state;
    }

    @Override
    public boolean holds(float timePerFrame) {
        return state;
    }
}

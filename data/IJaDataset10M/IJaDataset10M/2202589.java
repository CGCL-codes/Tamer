package org.rollinitiative.d20.encounter;

public interface Action {

    public double calculate(Combatant combatant);

    public void execute();
}

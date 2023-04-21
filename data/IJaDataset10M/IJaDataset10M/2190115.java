package net.bpfurtado.tas.model.combat;

import net.bpfurtado.tas.model.Player;
import org.apache.log4j.Logger;

public class AttackResult {

    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(AttackResult.class);

    private int dice1;

    private int dice2;

    private Fighter f;

    boolean instantKill = false;

    private AttackResultType type;

    public AttackResult(int dice1, int dice2, Fighter f) {
        this.f = f;
        this.dice1 = dice1;
        this.dice2 = dice2;
        if (dice1 == dice2 && dice1 == 1) {
            instantKill = true;
        }
    }

    public int sum() {
        return dice1 + dice2 + f.getCombatSkillLevel();
    }

    public String toString() {
        int sum = dice1 + dice2 + f.getCombatSkillLevel();
        return "[" + dice1 + "] + [" + dice2 + "] + " + f.getCombatSkillLevel() + " = " + sum;
    }

    public String roundInfoToString(int round) {
        String roundStr = "[" + round + "] ";
        String nameAndDice = f.getName() + ": " + toString();
        String stamina = ", stamina = " + f.getStamina();
        return roundStr + nameAndDice + stamina;
    }

    public void defineType(AttackResult enemy) {
        if (sum() > enemy.sum() || isInstantKill()) {
            setType(AttackResultType.won);
            enemy.setType(AttackResultType.loose);
        } else if (sum() < enemy.sum()) {
            setType(AttackResultType.loose);
            enemy.setType(AttackResultType.won);
        } else {
            setType(AttackResultType.draw);
            enemy.setType(AttackResultType.draw);
        }
    }

    public void setType(AttackResultType type) {
        this.type = type;
    }

    public AttackResultType getType() {
        return type;
    }

    public boolean isPlayer() {
        return f instanceof Player;
    }

    public boolean isInstantKill() {
        return instantKill;
    }
}

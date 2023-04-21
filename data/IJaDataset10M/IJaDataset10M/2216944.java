package saf.data;

import java.util.Random;

public class Action {

    private Move move;

    private Attack attack;

    public Action(Move move, Attack attack) {
        this.move = move;
        this.attack = attack;
    }

    public static Action getRandom(Random random) {
        return new Action(Move.getRandom(random), Attack.getRandom(random));
    }

    public String toString() {
        return move + " " + attack;
    }

    public Move getMove() {
        return move;
    }

    public Attack getAttack() {
        return attack;
    }
}

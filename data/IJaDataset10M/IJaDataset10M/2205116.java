package ch.unisi.inf.pfii.teamblue.jark.model.bonus;

public abstract class PlayerBonus extends Bonus {

    public PlayerBonus(final int bonusClass) {
        super(bonusClass);
    }

    @Override
    public abstract String toString();
}

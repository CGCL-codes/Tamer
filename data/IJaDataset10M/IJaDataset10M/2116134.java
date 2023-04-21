package mrusanov.fantasyruler.player.diplomacy.effects;

public abstract class PermanentEffect extends DiplomacyEffect {

    public PermanentEffect(DiplomacyEffectCategory effectCategory) {
        super(effectCategory);
    }

    @Override
    public boolean isExpired(int currentTurn) {
        return false;
    }
}

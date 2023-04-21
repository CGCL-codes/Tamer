package saf.attacks;

import saf.Bot;

public class KickHigh extends BotAttack {

    public KickHigh(Bot b) {
        super(b);
    }

    protected void setAttackProp() {
        bot.setKickHigh(true);
    }

    protected void calculateDamage() {
        damage = bot.getKickPower();
    }

    protected boolean affectsOpponent() {
        return bot.isOpponentWithinKickReach() && !bot.opponentBlockHigh();
    }

    public String toString() {
        return "KickHigh";
    }
}

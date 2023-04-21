package hokutonorogue.character.technique;

import java.util.*;
import com.golden.gamedev.util.*;
import hokutonorogue.character.*;
import hokutonorogue.character.action.*;
import hokutonorogue.game.*;
import hokutonorogue.level.*;
import hokutonorogue.level.tile.*;

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
public abstract class Technique extends CharacterAbility implements Hintable {

    public enum Techniques {

        HOKUTO_NISHI_SHINKU_HA(new HokutoNishiShinkuHa()), MUSO_TENSEI(new MusoTensei()), KAZAN_KOGAI_KOHO(new KazanKogaiKoho()), SUIEISHIN(new Suieishin()), GOSHI_RETSU_DAN(new GoshiRetsuDan());

        private final Technique technique;

        Techniques(Technique technique) {
            this.technique = technique;
        }

        public Technique technique() {
            return technique;
        }

        public static Techniques random() {
            int i = Utility.getRandom(0, Techniques.values().length - 1);
            return Techniques.values()[i];
        }
    }

    protected TechinqueLevelExperience experience = new TechinqueLevelExperience(10, this);

    protected Technique() {
    }

    public Technique(CharacterModel character, Integer index, boolean userVisible) {
        super(character, index, userVisible);
    }

    /**
     * @todo usare questo metodo!
     * @param source Tile
     * @param params List
     * @return boolean
     */
    public boolean canActivate(Tile source, List params) {
        boolean ret = true;
        if (character.getConcentration().freeSlots() < requiredConcentration()) {
            ret = false;
        } else {
            ret = _canActivate(source, params);
        }
        return ret;
    }

    public boolean canExecute(Tile source, List params) {
        boolean ret = true;
        if (!canExecute()) {
            ret = false;
        } else {
            ret = _canExecute(source, params);
        }
        return ret;
    }

    private boolean canExecute() {
        boolean ret = true;
        if (character.getKi().getValue() < requiredKi()) {
            ret = false;
        } else if (!canExecuteWithWeaponEquipped() && character.getEquippedWeapon() != null) {
            ret = false;
        }
        return ret;
    }

    public int onHit(Attack attack, int hitFactor) throws InterruptedAttackException {
        Integer ret = hitFactor;
        if (isOnHit() && canExecute() && canExecuteOn(attack)) {
            managePoints();
            ret = _onHit(attack, hitFactor);
        }
        return ret;
    }

    public int onDamage(Attack attack, int wounds) throws InterruptedAttackException {
        Integer ret = wounds;
        if (isOnDamage() && canExecute() && canExecuteOn(attack)) {
            managePoints();
            ret = _onDamage(attack, wounds);
        }
        return ret;
    }

    public int onSoaking(Attack attack, int wounds) throws InterruptedAttackException {
        Integer ret = wounds;
        if (isOnSoaking() && canExecute() && canExecuteOn(attack)) {
            managePoints();
            ret = _onSoaking(attack, wounds);
        }
        return ret;
    }

    private void managePoints() {
        if (character.isHero()) {
            Log.getInstance().addMessage(new LogMessage(character.getName().toUpperCase() + " HAS USED " + this.getName().toUpperCase() + " TECHNIQUE", LogMessage.POSITIVE));
            experience.addXp(1);
        }
        int kiUsed = requiredKi();
        if (kiUsed > 0) {
            character.getKi().decreaseCurrentValue(kiUsed);
            System.out.println(character.getName() + " has used " + kiUsed + " points of ki.");
        }
    }

    public TechinqueLevelExperience getExperience() {
        return experience;
    }

    protected abstract Integer _onHit(Attack attack, int hitFactor) throws InterruptedAttackException;

    protected abstract Integer _onDamage(Attack attack, int wounds) throws InterruptedAttackException;

    protected abstract Integer _onSoaking(Attack attack, int effectiveWounds) throws InterruptedAttackException;

    protected abstract boolean _canActivate(Tile target, List params);

    protected abstract boolean _canExecute(Tile target, List params);

    protected abstract boolean canExecuteOn(Attack attack);

    public abstract int requiredConcentration();

    public abstract int requiredKi();

    public abstract boolean canExecuteWithWeaponEquipped();

    protected abstract boolean isOnHit();

    protected abstract boolean isOnSoaking();

    protected abstract boolean isOnDamage();

    public abstract String getHint();
}

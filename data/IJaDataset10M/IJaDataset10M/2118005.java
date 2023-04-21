package hokutonorogue.object;

import java.util.*;
import com.golden.gamedev.util.*;
import hokutonorogue.character.*;
import hokutonorogue.character.action.*;
import hokutonorogue.game.*;
import hokutonorogue.level.*;

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
public class HokutoNishiShinkuHaTechnique extends MissileWeapon {

    public HokutoNishiShinkuHaTechnique() {
        this.name = "NISHI SHINKU HA TECHNIQUE";
    }

    public int calculateWounds(AttackTarget target, int hitFactor) {
        int wounds = character.getAgility().getValue() + hitFactor;
        wounds = Utility.getRandom(wounds / 2, wounds);
        System.out.println(character.getName() + " inflict wounds before soaking: " + wounds);
        return wounds;
    }

    public boolean _canExecute(AttackTarget target, List params) {
        return true;
    }

    public List<AttackReport> execute(Attack attack, AttackTarget at, List params) throws ExecutionException {
        List<AttackReport> ret = new ArrayList<AttackReport>();
        CharacterModel gameCharacter = at.getCharacter();
        if (gameCharacter != null) {
            Coordinates to = at.getCoordinates();
            MainGame.getInstance().getIsometricLevelRenderer().playMissileEffect(character.getSprite().getX() + character.getSprite().getWidth() / 2, character.getSprite().getY() + character.getSprite().getHeight() / 2, gameCharacter.getSprite().getX() + gameCharacter.getSprite().getWidth() / 2, gameCharacter.getSprite().getY() + gameCharacter.getSprite().getHeight() / 2);
            try {
                ret.add(attack.attack(at));
            } catch (InterruptedAttackException ex) {
            }
        }
        return ret;
    }

    public int requiredConcentration() {
        return 0;
    }

    public void updateGame(double elapsedTime) {
    }

    public float getMinRange() {
        return 2f;
    }

    public float getMaxRange() {
        return 5f;
    }

    public Class getMissileType() {
        return null;
    }

    public void playHitEffect(AttackTarget target) {
        VolatileEffectSprite blood = VolatileEffectSprite.buildBloodSprite(target.getTile().getX(), target.getTile().getY());
        MainGame.getInstance().getIsometricLevelRenderer().playEffect(blood);
        MainGame.getInstance().bsSound.play("resources/sound/cut.wav");
    }
}

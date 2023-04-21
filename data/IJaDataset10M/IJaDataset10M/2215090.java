package hokutonorogue.character.action;

import java.util.*;
import hokutonorogue.character.*;
import hokutonorogue.game.*;

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
public class NantoSenshuRyuGeki extends NantoKen {

    public static final String NANTO_SENSHU_RYU_GEKI = "NANTO SENSHU RYU GEKI";

    public NantoSenshuRyuGeki() {
        super();
    }

    public NantoSenshuRyuGeki(CharacterModel character, Integer index, boolean userVisible) {
        super(character, index, userVisible);
    }

    protected List<AttackReport> __execute(AttackTarget target, List params) throws ExecutionException {
        List<AttackReport> ret = new ArrayList<AttackReport>();
        CharacterModel gameCharacter = target.getCharacter();
        try {
            if (HokutoNoRogue.isVideoEnabled()) {
                VideoAnimation.getInstance().play(MainGame.getInstance(), "NantoSenshuRyuGeki");
            } else {
                MainGame.getInstance().bsSound.play("resources/video/NantoSenshuRyuGeki.wav");
            }
            for (int i = 0; i < 5 + experience.getLevel(); i++) {
                character.getCharacterSprite().punch();
                AttackTarget at = null;
                if (gameCharacter != null) {
                    at = gameCharacter.getRandomUpperBodyPart();
                } else {
                    at = target;
                }
                AttackReport ar = attack(at, null);
                instantCut(target, ar);
                ret.add(ar);
            }
        } catch (InterruptedAttackException ex) {
        }
        return ret;
    }

    protected boolean _canExecute(AttackTarget target, List params) {
        return true;
    }

    public String getName() {
        return NANTO_SENSHU_RYU_GEKI;
    }

    public int requiredConcentration() {
        return 3;
    }

    public boolean canExecuteWithWeaponEquipped() {
        return false;
    }

    public int requiredKi() {
        return 10;
    }

    public float getMinRange() {
        return 1f;
    }

    public float getMaxRange() {
        return 1f;
    }

    public double getCost() {
        return 1d;
    }

    public boolean isLocalizable() {
        return false;
    }

    public int attackBonus() {
        return 0;
    }

    public int woundsBonus() {
        return 0;
    }

    public boolean isHandAttack() {
        return true;
    }

    @Override
    public boolean isFootAttack() {
        return false;
    }

    public String getHint() {
        return "A MULTIPLE HIT ATTACK. A SERIE OF LIGHTNING-QUICK PIERCING STRIKES";
    }
}

package hokutonorogue.character;

import hokutonorogue.character.action.*;
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
public class TsuboZusetsu extends Tsubo {

    public TsuboZusetsu() {
        super();
    }

    public TsuboZusetsu(BodyPart bodyPart) {
        super(bodyPart);
    }

    public String getName() {
        return "ZUSETSU";
    }

    public String getDescription() {
        return "ERASE MEMORY";
    }

    public void updateGame(double elapsedTime) {
        if (pressed) {
            if (bodyPart.getCharacter().getBrain() instanceof AIBrain) {
                AIBrain aiBrain = (AIBrain) bodyPart.getCharacter().getBrain();
                aiBrain.setKarma(Brain.NEUTRAL_KARMA);
                aiBrain.setCurrentEnemy(null);
                bodyPart.getCharacter().getHostileCharacters().remove(origin);
                origin.getHostileCharacters().remove(bodyPart.getCharacter());
            }
            if (bodyPart.getCharacter().getAttacks().size() > 3) {
                Attack lost = bodyPart.getCharacter().getAttacks().pollLast();
                if (bodyPart.getCharacter().isHero()) {
                    if (bodyPart.getCharacter().getDefaultAttack().equals(lost)) {
                        bodyPart.getCharacter().setDefaultAttack(bodyPart.getCharacter().getAttacks().first());
                    }
                }
                Log.getInstance().addMessage(new LogMessage(bodyPart.getCharacter().getName().toUpperCase() + " FORGETS ATTACK " + lost.getName(), LogMessage.NEGATIVE));
            }
            pressed = false;
        }
    }

    public boolean isPositiveEffectTsubo() {
        return true;
    }
}

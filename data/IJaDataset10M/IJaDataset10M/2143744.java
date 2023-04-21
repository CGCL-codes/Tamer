package competition.gic2010.turing.sergeykarakovskiy;

import ch.idsia.agents.Agent;
import ch.idsia.agents.controllers.BasicMarioAIAgent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: 12/15/10
 * Time: 1:47 AM
 * Package: competition.gic2010.temp
 */
public class SergeyKarakovskiy_ForwardAgent extends BasicMarioAIAgent implements Agent {

    int trueJumpCounter = 0;

    int trueSpeedCounter = 0;

    public SergeyKarakovskiy_ForwardAgent() {
        super("SergeyKarakovskiy_ForwardAgent");
        reset();
    }

    public void reset() {
        action = new boolean[Environment.numberOfKeys];
        action[Mario.KEY_RIGHT] = true;
        action[Mario.KEY_SPEED] = true;
        trueJumpCounter = 0;
        trueSpeedCounter = 0;
    }

    private boolean DangerOfAny() {
        if ((getReceptiveFieldCellValue(marioEgoRow + 2, marioEgoCol + 1) == 0 && getReceptiveFieldCellValue(marioEgoRow + 1, marioEgoCol + 1) == 0) || getReceptiveFieldCellValue(marioEgoRow, marioEgoCol + 1) != 0 || getReceptiveFieldCellValue(marioEgoRow, marioEgoCol + 2) != 0 || getEnemiesCellValue(marioEgoRow, marioEgoCol + 1) != 0 || getEnemiesCellValue(marioEgoRow, marioEgoCol + 2) != 0) return true; else return false;
    }

    public boolean[] getAction() {
        if (DangerOfAny() && getReceptiveFieldCellValue(marioEgoRow, marioEgoCol + 1) != 1) {
            if (isMarioAbleToJump || (!isMarioOnGround && action[Mario.KEY_JUMP])) {
                action[Mario.KEY_JUMP] = true;
            }
            ++trueJumpCounter;
        } else {
            action[Mario.KEY_JUMP] = false;
            trueJumpCounter = 0;
        }
        if (trueJumpCounter > 16) {
            trueJumpCounter = 0;
            action[Mario.KEY_JUMP] = false;
        }
        return action;
    }
}

package src.objects;

import java.util.Scanner;
import java.io.File;
import src.engine.PICAIUAnimation;

/**
* A tank unit. Can be either on the red team or the blue team. Code specific to the Tanks will
* be here, or else any code general to all units will be in the PICAIUGameUnit superclass.
*/
public class PICAIUGameUnit_Tank extends PICAIUGameUnit implements PICAIUCollidable, PICAIUSelectable {

    PICAIUAnimation moving;

    PICAIUAnimation still;

    public PICAIUGameUnit_Tank(boolean redTeamIn, double xIn, double yIn, long idIn) {
        position.setX(xIn);
        position.setY(yIn);
        width = 24;
        height = 27;
        tMiddle = 28;
        selectable = true;
        setTeam(redTeam = redTeamIn);
        id = idIn;
    }

    public void setTeam(boolean redTeam) {
        if (redTeam) {
            collisionBounds.add(new PICAIURectBounds(0, 20, 45, 18));
            selectionBounds.add(new PICAIURectBounds(12, 0, 13, 7));
            selectionBounds.add(new PICAIURectBounds(9, 8, 19, 10));
            selectionBounds.add(new PICAIURectBounds(1, 18, 45, 18));
            selectionBounds.add(new PICAIURectBounds(7, 36, 39, 6));
            still = new PICAIUAnimation("tank_red_still.png", -1, -1);
            moving = new PICAIUAnimation("tank_red_roll.png", 3, 100);
        } else {
            collisionBounds.add(new PICAIURectBounds(0, 20, 45, 18));
            selectionBounds.add(new PICAIURectBounds(14, 0, 13, 7));
            selectionBounds.add(new PICAIURectBounds(11, 8, 19, 10));
            selectionBounds.add(new PICAIURectBounds(0, 18, 45, 18));
            selectionBounds.add(new PICAIURectBounds(0, 36, 39, 6));
            still = new PICAIUAnimation("tank_blue_still.png", -1, -1);
            moving = new PICAIUAnimation("tank_blue_roll.png", 3, 100);
            xFlip = true;
        }
        animation = still;
    }

    /**
  * Returns the animation for this unit when they're moving
  */
    public PICAIUAnimation getMovingAnimation() {
        return still;
    }

    /**
  * Updates the current object and performs its routines each frame
  */
    @Override
    public void update(long deltaT) {
        if (alive) {
            setAnimation();
            animation.update(deltaT);
        }
    }

    /**
   * Set's the animation of this unit
   */
    private void setAnimation() {
        if (velocity.getX() != 0 || velocity.getY() != 0) {
            animation = moving;
        } else {
            animation = still;
        }
        if (velocity.getX() > 0) {
            xFlip = true;
        } else if (velocity.getX() < 0) {
            xFlip = false;
        }
    }

    /**
  *  Returns where the target cursor should be located for this object
  */
    @Override
    public int getTargetLockX() {
        if (redTeam) {
            return (int) position.getX() + 6;
        } else {
            return (int) position.getX() + 10;
        }
    }

    /**
  *  Returns where the target cursor should be located for this object
  */
    @Override
    public int getTargetLockY() {
        if (redTeam) {
            return (int) position.getY() - 5;
        } else {
            return (int) position.getY() - 5;
        }
    }

    /**
  * returns the offset required to line up this object in the selection veiwer
  */
    @Override
    public int getViewerOffsetX() {
        if (redTeam) {
            return 27;
        } else {
            return 25;
        }
    }

    /**
  * returns the offset required to line up this object in the selection veiwer
  */
    @Override
    public int getViewerOffsetY() {
        return 14;
    }

    public float getCenterX() {
        int xtra = 12;
        if (this.redTeam) {
            xtra = 4;
        }
        return (float) (position.getX() + width / 2) + xtra;
    }

    public float getCenterY() {
        return (float) (position.getY() + height / 2) + 8;
    }
}

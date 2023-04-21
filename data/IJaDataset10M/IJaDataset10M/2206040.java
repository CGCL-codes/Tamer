package com.braids.coffeebombermen.client.shrink;

import com.braids.coffeebombermen.client.gamecore.control.GameCoreHandler;
import com.braids.coffeebombermen.options.Shrinkers;
import com.braids.coffeebombermen.options.OptConsts.Walls;
import com.braids.coffeebombermen.options.model.ServerOptions;
import com.braids.coffeebombermen.utils.MathHelper;

public class DefaultShrinkPerformer extends AbstractShrinkPerformer {

    private static final int MAX_SPEEDUP_STEPS = 12;

    private static final int SPEEDUP_RATIO = 30;

    private static final float SPEEDUP_POSSIBILITY = 0.5f;

    private static final int PRE_SPEEDUP_TICKS = 2;

    private ShrinkDirection lastShrinkDirection;

    private int lastNewWallX;

    private int lastNewWallY;

    private int shrinkMinX;

    private int shrinkMinY;

    private int shrinkMaxX;

    private int shrinkMaxY;

    private ShrinkType shrinkType;

    private int speedupSteps;

    private int preSpeedupWarn;

    private enum ShrinkDirection {

        RIGHT, DOWN, LEFT, UP
    }

    private static enum ShrinkType {

        CLOCKWISE_SPIRAL, ANTICLOCKWISE_SPIRAL
    }

    public DefaultShrinkPerformer(GameCoreHandler gameCoreHandler) {
        super(gameCoreHandler);
    }

    public Shrinkers getType() {
        return Shrinkers.Default;
    }

    protected void initNextRoundImpl() {
        lastShrinkDirection = ShrinkDirection.RIGHT;
        lastNewWallX = 0;
        lastNewWallY = 0;
        int shrinkTypeRandom = MathHelper.nextInt(2);
        if (shrinkTypeRandom == 0) {
            shrinkType = ShrinkType.CLOCKWISE_SPIRAL;
        } else if (shrinkTypeRandom == 1) {
            shrinkType = ShrinkType.ANTICLOCKWISE_SPIRAL;
        }
        speedupSteps = -1;
        preSpeedupWarn = -1;
    }

    protected void nextIterationImpl() {
        ServerOptions gso = getGlobalServerOptions();
        if (preSpeedupWarn > 0) {
            if ((getTick() & 2) == 0) {
                getGameCoreHandler().setWall(lastNewWallX, lastNewWallY, Walls.DEATH_WARN);
            } else {
                getGameCoreHandler().setWall(lastNewWallX, lastNewWallY, Walls.DEATH);
            }
        }
        if (isTimeToShrink()) {
            if (isTimeToFirstShrink() || isTimeToNextShrink((gso.getGameCycleFrequency() / (speedupSteps > 0 ? SPEEDUP_RATIO : 1)))) {
                if (!isTimeToFirstShrink() && (speedupSteps <= 0) && (preSpeedupWarn <= 0) && (lastNewWallX != -1) && MathHelper.checkRandomEvent(SPEEDUP_POSSIBILITY)) {
                    preSpeedupWarn = PRE_SPEEDUP_TICKS;
                }
                if (preSpeedupWarn > 0) {
                    preSpeedupWarn--;
                    if (preSpeedupWarn == 0) {
                        speedupSteps = MAX_SPEEDUP_STEPS;
                    }
                    setLastShrinkOperationAt();
                    return;
                }
                if (speedupSteps > 0) {
                    speedupSteps--;
                }
                int newWallX = lastNewWallX;
                int newWallY = lastNewWallY;
                if (isTimeToFirstShrink()) {
                    switch(shrinkType) {
                        case CLOCKWISE_SPIRAL:
                            newWallX = 0;
                            newWallY = 0;
                            shrinkMinX = 0;
                            shrinkMinY = 1;
                            shrinkMaxX = getWidth() - 1;
                            shrinkMaxY = getHeight() - 1;
                            lastShrinkDirection = ShrinkDirection.RIGHT;
                            break;
                        case ANTICLOCKWISE_SPIRAL:
                            newWallX = 0;
                            newWallY = 0;
                            shrinkMinX = 1;
                            shrinkMinY = 0;
                            shrinkMaxX = getWidth() - 1;
                            shrinkMaxY = getHeight() - 1;
                            lastShrinkDirection = ShrinkDirection.DOWN;
                            break;
                    }
                } else {
                    if ((shrinkMaxX <= shrinkMinX) && (shrinkMaxY <= shrinkMinY)) {
                        newWallX = -1;
                    } else {
                        if (shrinkType == ShrinkType.CLOCKWISE_SPIRAL) {
                            switch(lastShrinkDirection) {
                                case RIGHT:
                                    newWallX++;
                                    if (newWallX == shrinkMaxX) {
                                        lastShrinkDirection = ShrinkDirection.DOWN;
                                        shrinkMaxX--;
                                    }
                                    break;
                                case DOWN:
                                    newWallY++;
                                    if (newWallY == shrinkMaxY) {
                                        lastShrinkDirection = ShrinkDirection.LEFT;
                                        shrinkMaxY--;
                                    }
                                    break;
                                case LEFT:
                                    newWallX--;
                                    if (newWallX == shrinkMinX) {
                                        lastShrinkDirection = ShrinkDirection.UP;
                                        shrinkMinX++;
                                    }
                                    break;
                                case UP:
                                    newWallY--;
                                    if (newWallY == shrinkMinY) {
                                        lastShrinkDirection = ShrinkDirection.RIGHT;
                                        shrinkMinY++;
                                    }
                                    break;
                            }
                        } else if (shrinkType == ShrinkType.ANTICLOCKWISE_SPIRAL) {
                            switch(lastShrinkDirection) {
                                case RIGHT:
                                    newWallX++;
                                    if (newWallX == shrinkMaxX) {
                                        lastShrinkDirection = ShrinkDirection.UP;
                                        shrinkMaxX--;
                                    }
                                    break;
                                case DOWN:
                                    newWallY++;
                                    if (newWallY == shrinkMaxY) {
                                        lastShrinkDirection = ShrinkDirection.RIGHT;
                                        shrinkMaxY--;
                                    }
                                    break;
                                case LEFT:
                                    newWallX--;
                                    if (newWallX == shrinkMinX) {
                                        lastShrinkDirection = ShrinkDirection.DOWN;
                                        shrinkMinX++;
                                    }
                                    break;
                                case UP:
                                    newWallY--;
                                    if (newWallY == shrinkMinY) {
                                        lastShrinkDirection = ShrinkDirection.LEFT;
                                        shrinkMinY++;
                                    }
                                    break;
                            }
                        }
                    }
                }
                if ((newWallX >= 0) && (newWallX < getWidth()) && (newWallY >= 0) && (newWallY < getHeight())) {
                    addDeathWall(newWallX, newWallY);
                }
                setLastShrinkOperationAt();
                lastNewWallX = newWallX;
                lastNewWallY = newWallY;
            }
        }
    }
}

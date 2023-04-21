package com.braids.coffeebombermen.client.shrink;

import com.braids.coffeebombermen.client.gamecore.control.GameCoreHandler;
import com.braids.coffeebombermen.options.OptConsts.Items;
import com.braids.coffeebombermen.options.Shrinkers;
import com.braids.coffeebombermen.utils.MathHelper;

public class SpiderBombShrinkPerformer extends AbstractShrinkPerformer {

    private float chance;

    private static final int GAME_CYCLE_FREQUENCY_MULTIPLIER = 2;

    private static final float PLACE_SPIDER_CHANCE = 0.05f;

    public SpiderBombShrinkPerformer(GameCoreHandler gameCoreHandler) {
        super(gameCoreHandler);
    }

    public Shrinkers getType() {
        return Shrinkers.SpiderBomb;
    }

    protected void initNextRoundImpl() {
        chance = PLACE_SPIDER_CHANCE;
    }

    protected void nextIterationImpl() {
        if (isTimeToShrink()) {
            if (isTimeToFirstShrink() || isTimeToNextShrink(getGlobalServerOptions().getGameCycleFrequency() * GAME_CYCLE_FREQUENCY_MULTIPLIER)) {
                for (int i = 1; i < getWidth() - 1; i++) {
                    for (int j = 1; j < getHeight() - 1; j++) {
                        if (MathHelper.checkRandomEvent(chance)) {
                            setItem(i, j, Items.SPIDER_BOMB);
                        }
                    }
                }
                if ((chance < 1.0f) && MathHelper.checkRandomEvent(0.002f)) {
                    chance = Math.min(chance + PLACE_SPIDER_CHANCE, 1.0f);
                }
                setLastShrinkOperationAt();
            }
        }
    }
}

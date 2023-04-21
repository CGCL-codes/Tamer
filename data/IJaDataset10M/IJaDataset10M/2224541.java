package org.openaion.gameserver.model.templates.stats;

import org.openaion.gameserver.model.PlayerClass;
import org.openaion.gameserver.utils.stats.ClassStats;

/**
 * @author ATracer
 *
 */
public class CalculatedPlayerStatsTemplate extends PlayerStatsTemplate {

    private PlayerClass playerClass;

    public CalculatedPlayerStatsTemplate(PlayerClass playerClass) {
        this.playerClass = playerClass;
    }

    @Override
    public int getAccuracy() {
        return ClassStats.getAccuracyFor(playerClass);
    }

    @Override
    public int getAgility() {
        return ClassStats.getAgilityFor(playerClass);
    }

    @Override
    public int getHealth() {
        return ClassStats.getHealthFor(playerClass);
    }

    @Override
    public int getKnowledge() {
        return ClassStats.getKnowledgeFor(playerClass);
    }

    @Override
    public int getPower() {
        return ClassStats.getPowerFor(playerClass);
    }

    @Override
    public int getWill() {
        return ClassStats.getWillFor(playerClass);
    }

    @Override
    public float getAttackSpeed() {
        return ClassStats.getAttackSpeedFor(playerClass) / 1000f;
    }

    @Override
    public int getBlock() {
        return ClassStats.getBlockFor(playerClass);
    }

    @Override
    public int getEvasion() {
        return ClassStats.getEvasionFor(playerClass);
    }

    @Override
    public float getFlySpeed() {
        return ClassStats.getFlySpeedFor(playerClass);
    }

    @Override
    public int getMagicAccuracy() {
        return ClassStats.getMagicAccuracyFor(playerClass);
    }

    @Override
    public int getMainHandAccuracy() {
        return ClassStats.getMainHandAccuracyFor(playerClass);
    }

    @Override
    public int getMainHandAttack() {
        return ClassStats.getMainHandAttackFor(playerClass);
    }

    @Override
    public int getMainHandCritRate() {
        return ClassStats.getMainHandCritRateFor(playerClass);
    }

    @Override
    public int getMaxHp() {
        return ClassStats.getMaxHpFor(playerClass, 10);
    }

    @Override
    public int getMaxMp() {
        return 1000;
    }

    @Override
    public int getParry() {
        return ClassStats.getParryFor(playerClass);
    }

    @Override
    public float getRunSpeed() {
        return ClassStats.getSpeedFor(playerClass);
    }

    @Override
    public float getWalkSpeed() {
        return 1.5f;
    }
}

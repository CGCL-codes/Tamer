package org.activision.content.combat;

import org.activision.model.Entity;

public class CombatHitDefinitions {

    public CombatHitDefinitions(Entity target, int weaponId, int maxDamage, boolean isSpecialOn, short[] bonuses, boolean meleeDeflectPray, boolean rangeDeflectPray, boolean protectPray, boolean soulSplitPray, boolean sapWarrior, boolean sapRanger, boolean sapSpirit, boolean leechAttack, boolean leechRanged, boolean leechDefence, boolean leechStrength, boolean turmoil) {
        this.setTarget(target);
        this.setWeaponId(weaponId);
        this.setMaxDamage(maxDamage);
        this.setSpecialOn(isSpecialOn);
        this.setBonuses(bonuses);
        this.setMeleeDeflectPray(meleeDeflectPray);
        this.setRangeDeflectPray(rangeDeflectPray);
        this.setProtectPray(protectPray);
        this.setSoulSplitPray(soulSplitPray);
        this.setSapWarrior(sapWarrior);
        this.setSapRanger(sapRanger);
        this.setSapSpirit(sapSpirit);
        this.setLeechAttack(leechAttack);
        this.setLeechRanged(leechRanged);
        this.setLeechDefence(leechDefence);
        this.setLeechStrength(leechStrength);
        this.setTurmoil(turmoil);
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    public Entity getTarget() {
        return target;
    }

    public void setWeaponId(int weaponId) {
        this.weaponId = weaponId;
    }

    public int getWeaponId() {
        return weaponId;
    }

    public void setMaxDamage(int maxDamage) {
        this.maxDamage = maxDamage;
    }

    public int getMaxDamage() {
        return maxDamage;
    }

    public void setMeleeDeflectPray(boolean meleeDeflectPray) {
        this.meleeDeflectPray = meleeDeflectPray;
    }

    public boolean isMeleeDeflectPray() {
        return meleeDeflectPray;
    }

    public void setRangeDeflectPray(boolean rangeDeflectPray) {
        this.rangeDeflectPray = rangeDeflectPray;
    }

    public boolean isRangeDeflectPray() {
        return rangeDeflectPray;
    }

    public void setProtectPray(boolean protectPray) {
        this.protectPray = protectPray;
    }

    public boolean isProtectPray() {
        return protectPray;
    }

    public void setSoulSplitPray(boolean soulSplitPray) {
        this.soulSplitPray = soulSplitPray;
    }

    public boolean isSoulSplitPray() {
        return soulSplitPray;
    }

    public void setSapWarrior(boolean sapWarrior) {
        this.sapWarrior = sapWarrior;
    }

    public boolean isSapWarrior() {
        return sapWarrior;
    }

    public void setSapRanger(boolean sapRanger) {
        this.sapRanger = sapRanger;
    }

    public boolean isSapRanger() {
        return sapRanger;
    }

    public void setSapSpirit(boolean sapSpirit) {
        this.sapSpirit = sapSpirit;
    }

    public boolean isSapSpirit() {
        return sapSpirit;
    }

    public void setLeechAttack(boolean leechAttack) {
        this.leechAttack = leechAttack;
    }

    public boolean isLeechAttack() {
        return leechAttack;
    }

    public void setLeechRanged(boolean leechRanged) {
        this.leechRanged = leechRanged;
    }

    public boolean isLeechRanged() {
        return leechRanged;
    }

    public void setLeechDefence(boolean leechDefence) {
        this.leechDefence = leechDefence;
    }

    public boolean isLeechDefence() {
        return leechDefence;
    }

    public void setLeechStrength(boolean leechStrength) {
        this.leechStrength = leechStrength;
    }

    public boolean isLeechStrength() {
        return leechStrength;
    }

    public void setTurmoil(boolean turmoil) {
        this.turmoil = turmoil;
    }

    public boolean isTurmoilPray() {
        return turmoil;
    }

    public void setSpecialOn(boolean isSpecialOn) {
        this.isSpecialOn = isSpecialOn;
    }

    public boolean isSpecialOn() {
        return isSpecialOn;
    }

    public void setBonuses(short[] bonuses) {
        this.bonuses = bonuses;
    }

    public short[] getBonuses() {
        return bonuses;
    }

    private Entity target;

    private int weaponId;

    private int maxDamage;

    private boolean isSpecialOn;

    private short[] bonuses;

    private boolean meleeDeflectPray;

    private boolean rangeDeflectPray;

    private boolean protectPray;

    private boolean soulSplitPray;

    private boolean sapWarrior;

    private boolean sapRanger;

    private boolean sapSpirit;

    private boolean leechAttack;

    private boolean leechRanged;

    private boolean leechDefence;

    private boolean leechStrength;

    private boolean turmoil;
}

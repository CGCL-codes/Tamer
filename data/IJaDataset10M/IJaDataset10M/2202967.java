package src.backend.wad.physics;

import java.io.Serializable;

public class TriggerDefinition implements Serializable {

    private short roundsPerMagazine = 0;

    private short ammunitionType = 0;

    private short ticksPerRound = 0;

    private short recoveryTicks = 0;

    private short chargingTicks = 0;

    private short recoilMagnitude = 0;

    private short firingSound = 0;

    private short clickSound = 0;

    private short chargingSound = 0;

    private short shellCasingSound = 0;

    private short reloadingSound = 0;

    private short chargedSound = 0;

    private short projectileType = 0;

    private short thetaError = 0;

    private short dx = 0;

    private short dz = 0;

    private short shellCasingType = 0;

    private short burstCount = 0;

    public short getAmmunitionType() {
        return ammunitionType;
    }

    public void setAmmunitionType(short ammunitionType) {
        this.ammunitionType = ammunitionType;
    }

    public short getBurstCount() {
        return burstCount;
    }

    public void setBurstCount(short burstCount) {
        this.burstCount = burstCount;
    }

    public short getChargedSound() {
        return chargedSound;
    }

    public void setChargedSound(short chargedSound) {
        this.chargedSound = chargedSound;
    }

    public short getChargingSound() {
        return chargingSound;
    }

    public void setChargingSound(short chargingSound) {
        this.chargingSound = chargingSound;
    }

    public short getChargingTicks() {
        return chargingTicks;
    }

    public void setChargingTicks(short chargingTicks) {
        this.chargingTicks = chargingTicks;
    }

    public short getClickSound() {
        return clickSound;
    }

    public void setClickSound(short clickSound) {
        this.clickSound = clickSound;
    }

    public short getDx() {
        return dx;
    }

    public void setDx(short dx) {
        this.dx = dx;
    }

    public short getDz() {
        return dz;
    }

    public void setDz(short dz) {
        this.dz = dz;
    }

    public short getFiringSound() {
        return firingSound;
    }

    public void setFiringSound(short firingSound) {
        this.firingSound = firingSound;
    }

    public short getProjectileType() {
        return projectileType;
    }

    public void setProjectileType(short projectileType) {
        this.projectileType = projectileType;
    }

    public short getRecoilMagnitude() {
        return recoilMagnitude;
    }

    public void setRecoilMagnitude(short recoilMagnitude) {
        this.recoilMagnitude = recoilMagnitude;
    }

    public short getRecoveryTicks() {
        return recoveryTicks;
    }

    public void setRecoveryTicks(short recoveryTicks) {
        this.recoveryTicks = recoveryTicks;
    }

    public short getReloadingSound() {
        return reloadingSound;
    }

    public void setReloadingSound(short reloadingSound) {
        this.reloadingSound = reloadingSound;
    }

    public short getRoundsPerMagazine() {
        return roundsPerMagazine;
    }

    public void setRoundsPerMagazine(short roundsPerMagazine) {
        this.roundsPerMagazine = roundsPerMagazine;
    }

    public short getShellCasingSound() {
        return shellCasingSound;
    }

    public void setShellCasingSound(short shellCasingSound) {
        this.shellCasingSound = shellCasingSound;
    }

    public short getShellCasingType() {
        return shellCasingType;
    }

    public void setShellCasingType(short shellCasingType) {
        this.shellCasingType = shellCasingType;
    }

    public short getThetaError() {
        return thetaError;
    }

    public void setThetaError(short thetaError) {
        this.thetaError = thetaError;
    }

    public short getTicksPerRound() {
        return ticksPerRound;
    }

    public void setTicksPerRound(short ticksPerRound) {
        this.ticksPerRound = ticksPerRound;
    }
}

package com.jmedemos.stardust.scene.actions;

import com.jme.math.Vector3f;
import com.jme.util.Timer;
import com.jmedemos.stardust.scene.PlayerShip;
import com.jmedemos.stardust.scene.projectile.ProjectileFactory;
import com.jmedemos.stardust.scene.projectile.ProjectileFactory.ProjectileType;
import com.jmedemos.stardust.sound.SoundUtil;

/**
 * fires projectiles created by the ProjectilFactory.
 */
public class ShipWeaponAction implements Runnable {

    /** min time between playing the sound */
    private float sfxTimeout = 0.1f;

    /** last played */
    private float lastSFxPlayed = 0;

    /** reference to player ship. */
    private PlayerShip ship = null;

    /**
     * constructor.
     * @param ship reference to the players ship.
     */
    public ShipWeaponAction(final PlayerShip ship) {
        this.ship = ship;
    }

    /**
     * creates a new projectile.
     * Dependant on the current type set in the ProjectileFactory, 
     * bullets or Missiles are created.
     */
    public final void run() {
        Vector3f direction = ship.getNode().getLocalRotation().getRotationColumn(2);
        ship.getNode().updateWorldVectors();
        ship.getUpperLeftWeapon().updateWorldVectors();
        ship.getUpperRightWeapon().updateWorldVectors();
        ship.getLowerLeftWeapon().updateWorldVectors();
        ship.getLowerRightWeapon().updateWorldVectors();
        ProjectileFactory.get().createProjectile(ProjectileType.BULLET).fire(direction, ship.getUpperLeftWeapon().getWorldTranslation(), ship.getNode().getWorldRotation());
        ProjectileFactory.get().createProjectile(ProjectileType.BULLET).fire(direction, ship.getUpperRightWeapon().getWorldTranslation(), ship.getNode().getWorldRotation());
        ProjectileFactory.get().createProjectile(ProjectileType.BULLET).fire(direction, ship.getLowerLeftWeapon().getWorldTranslation(), ship.getNode().getLocalRotation());
        ProjectileFactory.get().createProjectile(ProjectileType.BULLET).fire(direction, ship.getLowerRightWeapon().getWorldTranslation(), ship.getNode().getWorldRotation());
        float current = Timer.getTimer().getTimeInSeconds();
        if (lastSFxPlayed + sfxTimeout < current) {
            SoundUtil.get().playSFX(SoundUtil.BG_BULLET_SHOT);
            lastSFxPlayed = current;
        }
    }
}

;

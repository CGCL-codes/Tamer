package org.jfge.api.fighter;

import java.util.List;
import org.jfge.api.effect.CollisionEffect;
import org.jfge.api.fsm.StateMachine;
import org.jfge.api.projectile.Projectile;
import org.jfge.api.sprite.Sprite;
import org.jfge.spi.graphics.Image;

/**
 * The Interface Fighter.
 */
public interface Fighter extends Sprite, StateMachine<FighterState> {

    /**
	 * Adds the collision effect.
	 *
	 * @param effect the effect
	 */
    public void addCollisionEffect(CollisionEffect effect);

    /**
	 * Removes the collision effect.
	 *
	 * @param effect the effect
	 */
    public void removeCollisionEffect(CollisionEffect effect);

    /**
	 * Adds the projectile.
	 *
	 * @param projectile the projectile
	 */
    public void addProjectile(Projectile projectile);

    /**
	 * Removes the projectile.
	 *
	 * @param projectile the projectile
	 */
    public void removeProjectile(Projectile projectile);

    /**
	 * Gets the projectiles.
	 *
	 * @return the projectiles
	 */
    public List<Projectile> getProjectiles();

    /**
	 * Gets the health.
	 *
	 * @return the health
	 */
    public int getHealth();

    /**
	 * Sets the health.
	 *
	 * @param health the new health
	 */
    public void setHealth(int health);

    /**
	 * End state.
	 */
    public void endState();

    /**
	 * In victory state.
	 *
	 * @return true, if successful
	 */
    public boolean inVictoryState();

    /**
	 * Victory state.
	 */
    public void victoryState();

    /**
	 * Gets the portrait.
	 *
	 * @return the portrait
	 */
    public Image getPortrait();
}

package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Input;
import com.googlecode.jumpnevolve.graphics.world.Jumping;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Ein Soldat, der immer wieder so hoch springt, wie er vor dem Fallen war
 * 
 * @author Erik Wagner
 * 
 */
public class SpringingSoldier extends Soldier implements Jumping {

    private static final long serialVersionUID = -4562665300367627101L;

    private float maxVelocityY = 0.0f;

    /**
	 * @param world
	 * @param position
	 */
    public SpringingSoldier(World world, Vector position) {
        super(world, position);
    }

    @Override
    protected void specialSettingsPerRound(Input input) {
        super.specialSettingsPerRound(input);
        this.maxVelocityY = Math.max(Math.abs(this.getVelocity().y), this.maxVelocityY);
    }

    @Override
    public float getJumpingHeight() {
        return this.maxVelocityY;
    }

    @Override
    public boolean wantJump() {
        return true;
    }
}

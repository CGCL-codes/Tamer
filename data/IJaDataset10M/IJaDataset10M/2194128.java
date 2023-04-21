package game.particleEffects;

import engine.GUI;

/**
 * A smoke particle that grows and shrinks according to its time and time to live
 * @author P. Curet
 *
 */
public class SmokeParticle extends Particle {

    /**
	 * Constructor for the smokeParticle
	 **/
    public SmokeParticle(int xPos, int yPos, double maximumSize, int birthToLive, float spreadXDir, float spreadYDir) {
        super(xPos, yPos, maximumSize, birthToLive);
        this.spreadX = (float) ((Math.random() * spreadXDir * 2) - spreadXDir);
        this.spreadY = spreadYDir;
    }

    /**
	 * Updates the particle
	 */
    public void run() {
        super.run();
        x = (int) (xBirth + (this.timeDiff) * spreadX);
        y = (int) (yBirth - (this.timeDiff) * spreadY);
    }

    /**
	 * Draw function
	 */
    public void draw(boolean drawnInMenu) {
        GUI.getInstance().drawSmokeParticle(this, drawnInMenu);
    }
}

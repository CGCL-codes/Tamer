package ge.lowlevel;

import ge.modules.logics.GeomCollideFigure;

/**
 * 
 * @author Abel
 *	This vector class it's used for represent the collide's vector in the CollideEngine
 */
public class Vector {

    public Double x = 0d;

    public Double y = 0d;

    public boolean colision = false;

    public GeomCollideFigure figure = null;

    /**
	 * 	
	 * @return True if one of the figures is inside the other
	 */
    public boolean isNeutral() {
        return this.x == 0 && this.y == 0;
    }

    /**
	 * 	Module of the vector since (0,0) to (x,y)
	 * @return the module of the vector
	 */
    public double module() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    /**
	 * 	
	 * @return True if collision is only on the way top
	 */
    public boolean collideUp() {
        return x == 0 && y < 0;
    }

    /**
	 * 	
	 * @return True if collision is only on the way down
	 */
    public boolean collideDown() {
        return x == 0 && y > 0;
    }

    /**
	 * 	
	 * @return True if collision is only on the way left
	 */
    public boolean collideLeft() {
        return x < 0 && y == 0;
    }

    /**
	 * 	
	 * @return True if collision is only on the way right
	 */
    public boolean collideRight() {
        return x > 0 && y == 0;
    }
}

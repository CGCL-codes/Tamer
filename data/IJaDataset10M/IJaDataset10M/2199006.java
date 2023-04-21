package net.sourceforge.minigolf.simulator;

/**
 * This is a "solid component" which reflects the ball at its edges.  As with
 * Ground this is only the abstract super-class used by the simulator, concrete
 * Solids must be implemented elsewhere.
 */
public abstract class Solid {

    /**
  * "Apply" this solid-object to the Ball.
  * @param b The Ball to apply to.
  * @return True if it matched.
  */
    public final boolean apply(Ball b) {
        Reflection r = getReflection(b.getPosition(), b.getRadius());
        if (r == null) return false;
        b.move(r.getMove());
        r.adaptVelocity(b.getVelocity(), getReflectionFactor());
        return true;
    }

    /**
  * Ask for Reflection; returns null if no one applies.
  * @param pos	The current ball-position.
  * @param r	The ball's radius.
  * @return The Reflection object to apply or null.
  */
    public abstract Reflection getReflection(Position pos, double r);

    /**
  * Return the factor which gives the velocity after reflection.
  * @return The factor to use.
  */
    protected abstract double getReflectionFactor();
}

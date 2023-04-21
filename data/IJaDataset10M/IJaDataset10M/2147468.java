package avoca;

import avoca.Entity.Actor.Actor;

/**
 *
 * @author RobertLT
 */
public interface Combatable {

    /**
     * This method will tell an implicit actor to attack the explicit actor
     * though how useful this will be will be determined by how we decide to
     * make the game play, other methods with different attacks should follow
     * the same logic as a basic attack.
     * @param a
     */
    public void attack(Actor a);

    /**
     * Again, defend may, or may not, be used depending on decisions to come.
     */
    public void defend();

    /**
     * This method might me used at low hp. Some monsters might flee in search
     * of help near death.
     */
    public void run();

    /**
     * The point of this method is vauge as it is. I can only assume that it might
     * be used to turn on and off AI scipts? Maybe the method could be passed a
     * 'script' that should be executed by the combatable entity?
     */
    public void invokeAI();
}

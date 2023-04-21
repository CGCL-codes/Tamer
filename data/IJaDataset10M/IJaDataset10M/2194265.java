package entensim.behaviour.fly;

/**
 * Specifies all methods an AbstractDuck needs to implement in order to "fly"
 * 
 * @author Michael Langowski
 *
 */
public interface IFlyBehaviour {

    /**
	 * Generic fly method - concrete implementations to be provided by subclasses
	 */
    public void fly();
}

package anon.example;

/**
 * This is a description of the AbstractClass class.
 *
 *
 */
public abstract class AbstractClass {

    private int commonInteger;

    /**
	 * Abstract Method Description
	 * @param i
	 */
    public abstract void abstractMethod(int amFpOne) throws AbstractException;

    /**
	 * a concrete method
	 */
    public boolean concreteMethod() {
    }
}

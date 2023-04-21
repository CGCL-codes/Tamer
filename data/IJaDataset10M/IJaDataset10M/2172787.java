package loengud.kuues;

/**
 * Abstract class to define a general structure and behavior of 
 * students. "Abstract class" means there can be abstract methods in the class 
 * - no method body (in {} brackets).<p/>
 * 
 * Static {@link #nextId} is seen by all instances/object of the class and 
 * keeps track of the ID/counter, given to each instance.<p/>
 * 
 * @author A
 *
 */
public abstract class Student {

    /**
	 * A static helper to keep track of unique ID-s assigned 
	 * to objects constructed from {@link Student}-s descendants. Remember 
	 * to increment after use!
	 */
    static int nextId = 0;

    /**
	 * Everyone has a name "getter".
	 * 
	 * @return {@link String}
	 */
    abstract String getNimi();
}

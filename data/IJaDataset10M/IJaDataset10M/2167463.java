package simple.http.load;

import simple.util.Resolver;
import simple.util.Match;

/**
 * The <code>Profile</code> contains the methods that represent
 * the state if the <code>LoaderEngine</code>. This is used so
 * that objects within the system can determine the state of the
 * engine without having to deal with the methods of different
 * objects. This is primarly used as a means for the engine to
 * pass its state to the <code>Processor</code>.
 * 
 * @author Niall Gallagher
 */
final class Profile {

    /**
    * This is the <code>Registry</code> used by the engine.
    */
    private Registry registry;

    /**
    * This is the <code>Resolver</code> used by the engine.
    */
    private Resolver resolver;

    /**
    * Constructor for the <code>Profile</code> requires the
    * <code>LoaderEngine</code> that is is to represent. This is
    * the engine that this profile instance will use to acquire
    * state. This uses the registry and resolver of the engine.
    *
    * @param engine this is the <code>LoaderEngine</code> that
    * the profile instance represents.
    */
    public Profile(LoaderEngine engine) {
        this.registry = engine.registry;
        this.resolver = engine.resolver;
    }

    /**
    * This is used to retrieve the links that have been made with
    * the <code>LoaderEngine</code>. This contains the pattern
    * and name matches made. The order of the array is represents
    * to the order in which they were entered into the engine.
    *
    * @return an ordered array of <code>Match</code> objects that
    * contain the links made    
    */
    public Match[] getMatches() {
        return resolver.getMatches();
    }

    /**
    * This is used to retrieve the fully qualified class names of
    * the resources loaded by the <code>LoaderEngine</code>. This
    * contains the class names that match by index the names of
    * the resources retrieved with <code>getNames</code>.
    *
    * @return an array of strings that is parallel to the names
    * retrieved from the <code>getNames</code> method
    */
    public String[] getClassNames() {
        return registry.getClassNames();
    }

    /**
    * This is used to retrieve the unique names of resource
    * instances loaded by the <code>LoaderEngine</code>. The list
    * of names returns matches by index the class type of the
    * instance as retrieved by <code>getClassNames</code>.
    *
    * @return an array of strings that is parallel to the class
    * names retrieved from the <code>getClassNames</code> method    
    */
    public String[] getNames() {
        return registry.getNames();
    }
}

package lunify;

import java.util.Collection;

/**
 * This is a "factory" of LProcessor objects to be used. I recommend wrapping
 * it around a private enum (see dunify.dictionary for an example of this).
 * @author katie
 */
public abstract class Source {

    /**
     * Gets the LProcessor object that's associated with this name. Can either be
     * a singleton, to make this an actual factory class, or just a new instantiation.
     * It shouldn't actually matter with the basic functionality of LUnify, in which this is called twice
     * - once from toString() and once to actually do stuff.
     * @param name
     * @return null iff the name is invalid
     */
    public abstract LProcessor processor(String name);

    /**
     * Lists all valid names of available processors
     * @return 
     */
    public abstract Collection<String> options();

    /**
     * Prints out the list of valid names (in whatever order options() provides)
     * and the toString() for the provided LProcessor. For example, for Dictionary, this looks like this:
     * <pre>OMEGA = omega (Omegawiki database @ http://www.omegawiki.org/)
UNI = uni (Universal dictionary database @ http://dicsts.info)
WIK = wik (Wiktionary @ http://en.wiktionary.org/)
WIKI = wiki (wikipedia @ http://www.wikipedia.org/)</pre>
     * @return nicely formatted text of all the options (ends with a newline)
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String s : options()) {
            sb.append(s).append(" = ").append(processor(s)).append("\n");
        }
        return sb.toString();
    }
}

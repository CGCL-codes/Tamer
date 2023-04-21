package util.runtime;

public class ArgumentUtility {

    protected static ArgumentUtility INST = new ArgumentUtility();

    protected ArgumentUtility() {
    }

    public static ArgumentUtility getInstance() {
        return INST;
    }

    /**
     * Utility method for determining if any in the argument list are null.
     * 
     * @param args The arguments in question.
     * @return Yes or no.
     */
    public boolean areNull(final Object... args) {
        boolean hasNull = false;
        for (Object object : args) {
            hasNull &= (object == null);
        }
        return hasNull;
    }
}

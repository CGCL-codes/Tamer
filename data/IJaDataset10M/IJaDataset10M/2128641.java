package ezsudoku;

/**
 * Indicates that a post condition has failed.
 *
 * @author Cedric Chantepie (cchantepie@corsaire.fr)
 */
public class PostConditionError extends Error {

    /**
     */
    public PostConditionError(String message) {
        super(message);
    }
}

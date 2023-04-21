package uk.co.weft.fisherman.entities;

/**
 * An Action is a step in the configurable workflow for licences, moving an
 * individual licence from one status to another.
 */
public abstract class Action extends Entity {

    /** the key field in my table */
    public static final String KEYFN = "action_id";

    /** the name of my type field */
    public static final String TYPEFN = ActionType.KEYFN;

    /** the name of my description field */
    public static final String DESCFN = "a_desc";

    /** the name of my table */
    public static final String TABLENAME = "ACTION";
}

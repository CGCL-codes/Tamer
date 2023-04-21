package de.fhg.igd.jhsm;

/**
 * This is the base interface for all parts of the hsm hierarchy.
 *
 * @author Jan Haevecker
 * @version "$Id: Component.java 1913 2007-08-08 02:41:53Z jpeters $"
 */
public interface Component {

    /**
     * Performs the component's action in a specified <code>context</code>.
     * This is the hook for the interpreter.
     *
     * @param context Used to publish and retrieve data for/from other actions.
     */
    void action(Context context);

    /** 
     * Set's the name of the component. This is for debugging purposes only, 
     * the name is not part of the hsm semantic.
     */
    void setName(String name);

    /** 
     * Returns the name of the component. This is for debugging purposes only,
     * the name is not part of the hsm semantic.
     */
    String getName();

    /**
     * Returns the action associated with this component.
     */
    Action getAction();

    /**
     * Assiociates an action with the component.
     *
     * @param action The action that should be executed by the component.
     */
    void setAction(Action action);

    /**
     * Returns the parent component.
     */
    Component getParent();

    /**
     * Sets the parent component.
     */
    void setParent(Component parent);
}

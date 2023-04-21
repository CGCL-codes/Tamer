package java.awt;

/**
 * Conditional is used by the EventDispatchThread's message pumps to
 * determine if a given pump should continue to run, or should instead exit
 * and yield control to the parent pump.
 *
 * @version 1.3 02/02/00
 * @author David Mendenhall
 */
interface Conditional {

    boolean evaluate();
}

package action.menu;

/**
 * A single element of a menu. An action can be {@link Runnable#run() run}.
 * @author Andr�s B�ni
 *
 */
public interface Action extends MenuItem, Runnable {
}

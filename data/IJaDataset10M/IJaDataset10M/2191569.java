package src.exceptions;

/**
 * Exception that is thrown when the engine tries to spawn an enemy,
 * but there are no enemies defined to spawn.
 * @author Darren Watts
 *
 */
public class EnemySpawnNotDefinedException extends Exception {

    private static final long serialVersionUID = src.Constants.serialVersionUID;
}

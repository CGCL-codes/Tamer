package jbomberman.game;

/**
 * Every computer player is controlled by an instance of a class
 * which implements this interface. It requires the implementation
 * of just one method, with which the command for the next
 * simulation cycle is retrieved.
 *
 * @author Lukas Zebedin
 * @version 1.0
 * @see Board
 * @see RuleSet
 */
public abstract class BotAI {

    /**
   * This method is called every simulation cycle to get a
   * command for a computer player.
   *
   * @param Board - is the state of the game.
   * @param RuleSet - is the system which describes the simulation mechanics.
   */
    public abstract Command getCommand(Board board, RuleSet ruleset);

    /**
   * This method is used to get the name of the bot. Every instance
   * can return the same name, select it by random or read it from
   * a file...etc.
   *
   * @return String - name of this instance of the ai.
   */
    public abstract String getName();

    /**
   * Returns a human readable description of the bot ai, the algorithm,
   * the behaviour, the expected ruleset etc...
   *
   * @return String in human readable form.
   */
    public String toString() {
        return "<no description given>";
    }
}

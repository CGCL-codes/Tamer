package hailmary.dicebags;

import hailmary.network.Connection;

/**
 * A dice bag which uses the Nufflebots present on
 * some IRC services (for instance AfterNet).
 * @author Corvass
 */
public class BotDiceBag implements DiceBag {

    /** The default bot names */
    public static final String[] DEFAULT_BOTS = { "nb", "pb", "qb" };

    private Connection connection;

    private String bot;

    /**
   * Constructs a dice bag for the given connection, using
   * the default bot names.
   * @param connection the connection to use
   */
    public BotDiceBag(Connection connection) {
        this(connection, DEFAULT_BOTS);
    }

    /**
   * Constructs a dice bag for the given connection, using
   * the specified bot names.
   * @param connection the connection to use
   * @param bots the bot names to use
   * @throws IllegalArgumentException if <code>bots</code> is empty
   */
    public BotDiceBag(Connection connection, String[] bots) {
        this.connection = connection;
        if (bots.length == 0) throw new IllegalArgumentException("No bots supplied");
        connection.removeAllDiceBotHacks();
        bot = null;
        for (int i = 0; i < bots.length; i++) {
            connection.addDiceBotHack(bots[i]);
            if (bot == null && connection.nickExists(bots[i])) bot = bots[i];
        }
    }

    public void roll(String dice) {
        if (bot != null) connection.say(null, bot + " " + dice);
    }
}

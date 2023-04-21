package free.jin.console.icc;

import javax.swing.text.BadLocationException;
import free.jin.Connection;
import free.jin.Preferences;
import free.jin.console.Console;
import free.jin.console.ConsoleTextField;
import free.jin.console.ConsoleTextPane;

/**
 * An extension of free.jin.console.Console which adds some ICC specific
 * features.
 */
public class ChessclubConsole extends Console {

    /**
   * Creates a new Console with the given connection and preferences.
   */
    public ChessclubConsole(Connection conn, Preferences prefs) {
        super(conn, prefs);
    }

    /**
   * Creates the <code>ConsoleTextField</code> in which the user can input
   * commands to be sent to the server. Overrides
   * <code>Console.createInputComponent()</code> since we need a
   * special <code>ConsoleTextField</code> for ICC.
   */
    protected ConsoleTextField createInputComponent() {
        return new ChessclubConsoleTextField(this);
    }

    /**
   * Overrides <code>Console.createOutputComponent()</code> since we need a
   * special <code>ConsoleTextPane</code> for ICC.
   */
    protected ConsoleTextPane createOutputComponent() {
        return new ChessclubConsoleTextPane(this);
    }

    /**
   * Works around the issue with specially layed out finger noted, such as
   * "finger Live" by splitting lines with lots of spaces followed by a ':'.
   * See http://sourceforge.net/tracker/index.php?func=detail&aid=675197&group_id=50386&atid=459537
   * for more information.
   */
    protected void addToOutputImpl(String text, String textType) throws BadLocationException {
        super.addToOutputImpl(text, textType);
    }
}

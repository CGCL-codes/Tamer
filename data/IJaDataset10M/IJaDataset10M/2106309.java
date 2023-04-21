package sk.naive.talker.command;

import sk.naive.talker.*;
import sk.naive.talker.message.DefaultMessageFactory;

/**
 * BoardHelper.
 *
 * @author <a href="mailto:virgo@naive.deepblue.sk">Richard "Virgo" Richter</a>
 * @version $Revision: 1.7 $ $Date: 2004/10/04 21:58:01 $
 */
public class BoardHelper {

    private Board board;

    private User user;

    private String name;

    private CommandDispatcher commandDispatcher;

    public BoardHelper(CommandDispatcher commandDispatcher, User user, String s) {
        this.commandDispatcher = commandDispatcher;
        this.user = user;
        if (s != null && s.length() != 0) {
            this.name = s;
        }
        if (name == null) {
            board = getDefaultBoard(user);
        } else {
            board = commandDispatcher.boardFinder().findBoardByName(name);
        }
    }

    private Board getDefaultBoard(User user) {
        Location loc = new LocationHelper(commandDispatcher).locationForUser(user);
        if (loc == null) {
            return null;
        }
        return commandDispatcher.boardFinder().findBoardByName(loc.getName());
    }

    public Board getBoard() {
        return board;
    }

    public String getWarningMessageKey() {
        if (name == null) {
            return "board.defaultNotFound";
        }
        user.getProperties().put(DefaultMessageFactory.CTXKEY_VAL, name);
        return "board.notFound";
    }
}

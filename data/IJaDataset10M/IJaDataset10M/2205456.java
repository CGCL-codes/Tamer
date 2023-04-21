package sk.naive.talker.command;

import sk.naive.talker.*;
import sk.naive.talker.message.DefaultMessageFactory;
import sk.naive.talker.util.Utils;
import sk.naive.talker.persistence.*;
import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;

/**
 *
 * @author <a href="mailto:virgo@naive.deepblue.sk">Richard "Virgo" Richter</a>
 * @version $Revision: 1.21 $ $Date: 2005/02/16 19:27:21 $
 */
public class Wipe extends AbstractCommand {

    public void exec() throws CommandException, RemoteException {
        try {
            String[] sa = Utils.splitWords(params, -1);
            if (sa.length > 2 || sa.length < 1) {
                sendMisusageWarning();
                return;
            }
            BoardHelper bh;
            String rangeSpec;
            if (sa.length == 2) {
                bh = new BoardHelper(commandDispatcher, user, sa[0]);
                rangeSpec = sa[1];
            } else {
                bh = new BoardHelper(commandDispatcher, user, null);
                rangeSpec = sa[0];
            }
            Board board = bh.getBoard();
            if (bh.getBoard() == null) {
                sendHelper().sendMessage(user, bh.getWarningMessageKey());
                return;
            }
            List texts = board.texts();
            if (texts.size() == 0) {
                ctxSet(DefaultMessageFactory.CTXKEY_VAL, board.getProperties());
                sendHelper().sendMessage(user, "board.empty");
                return;
            }
            BitSet deleted = new BitSetSelector().create(rangeSpec, board.texts().size());
            int deletedCount = 0;
            int leftCount = 0;
            int counter = 1;
            TextPersistence tp = commandDispatcher.textPersistence();
            BoardMessageRights messageRights = new BoardMessageRights(tp);
            for (Iterator i = texts.iterator(); i.hasNext(); ) {
                Text text = (Text) i.next();
                if (messageRights.isWipeable(text, user, board) && deleted.get(counter)) {
                    i.remove();
                    board.remove(text);
                    deletedCount++;
                } else {
                    leftCount++;
                }
                counter++;
            }
            user.getProperties().put(DefaultMessageFactory.CTXKEY_VAL, String.valueOf(deletedCount));
            user.getProperties().put(DefaultMessageFactory.CTXKEY_VAL + 1, String.valueOf(leftCount));
            sendHelper().sendMessage(user, "wipe.deleted");
            logger().log(Level.FINE, "User {0} wiped out {1} message(s) from board {2}.", new Object[] { user.getLogin(), String.valueOf(deletedCount), board.getName() });
        } catch (NumberFormatException e) {
            sendMisusageWarning();
        } catch (PersistenceException e) {
            throw new CommandException(e);
        }
    }
}

package sk.naive.talker;

import sk.naive.talker.util.Utils;
import sk.naive.talker.persistence.*;
import java.rmi.RemoteException;

/**
 *
 * @author <a href="mailto:virgo@naive.deepblue.sk">Richard "Virgo" Richter</a>
 * @version $Revision: 1.3 $ $Date: 2005/01/10 22:11:24 $
 */
public class BoardMessageRights {

    public static final String PERM_ALL_BOARD_MESSAGES = "allBoardMessages";

    private TextPersistence persistence;

    public BoardMessageRights(TextPersistence persistence) {
        this.persistence = persistence;
    }

    public boolean isWipeable(Text text, User user, Board board) throws RemoteException, PersistenceException {
        if (Utils.ownsPermission(user, PERM_ALL_BOARD_MESSAGES)) {
            return true;
        }
        if (board.isEditor(user)) {
            return true;
        }
        Integer authorId = text.getAuthorId(persistence);
        if (authorId != null && authorId.equals(user.getId())) {
            return true;
        }
        return false;
    }
}

package sk.naive.talker.command;

import sk.naive.talker.*;
import sk.naive.talker.persistence.*;
import java.util.*;
import java.rmi.RemoteException;

/**
 *
 * @author <a href="mailto:richter@bgs.sk">Richard Richter</a>
 * @version $Revision: 1.31 $ $Date: 2005/02/22 21:49:05 $
 */
public class Profile extends AbstractCommand {

    public void exec() throws CommandException, RemoteException {
        if (params != null && params.length() > 0 && CommandKeywords.REMOVE.startsWith(params.toLowerCase())) {
            try {
                DbTransaction.begin();
                TextPersistence tp = commandDispatcher.textPersistence();
                for (Text text : tp.loadTexts(user.getId(), TextRelation.USER_PROFILE, null)) {
                    text.remove();
                    for (TextRelation textRelation : tp.loadTextRelations(text)) {
                        tp.removeTextRelation(textRelation);
                    }
                }
            } catch (PersistenceException e) {
                DbTransaction.setRollbackOnly(e.getMessage());
                throw new CommandException(e);
            } finally {
                DbTransaction.finishWithoutException();
            }
            sendHelper().sendMessage(user, "profile.removed");
            return;
        }
        new EditorHelper(this).start((PersistentObject) user);
    }

    private class EditorHelper extends AbstractEditorHelper {

        public EditorHelper(AbstractCommand comm) {
            super(Profile.this.user, comm);
        }

        protected Text getOriginalText(PersistentObject object, TextPersistence db) throws PersistenceException {
            List<Text> texts = db.loadTexts(object.getId(), textType(), "");
            if (texts.isEmpty()) {
                return null;
            }
            return texts.get(0);
        }

        protected int textType() {
            return TextRelation.USER_PROFILE;
        }

        protected String getUserKeyBegin() {
            return "profile.begin";
        }

        protected String getUserKeySend() {
            return "profile.successful";
        }

        protected String getUserKeyCancel() {
            return "profile.cancelled";
        }

        protected Text createNewText(TextPersistence tp, String s) throws PersistenceException {
            return new Text(tp, s);
        }
    }
}

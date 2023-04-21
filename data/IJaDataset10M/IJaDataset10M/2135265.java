package larpplanner.gui;

import java.util.List;
import org.eclipse.swt.widgets.Display;
import larpplanner.database.CharUniverse;
import larpplanner.database.Parser;
import larpplanner.database.Player;
import larpplanner.database.Quest;
import larpplanner.logic.LARPManager;
import larpplanner.logic.LARPManagerImp;
import larpplanner.logic.QuestSummary;

public class GUISafeLARPManager {

    private LARPManager mngr = LARPManagerImp.get();

    public static GUISafeLARPManager instance = new GUISafeLARPManager();

    private GUISafeLARPManager() {
    }

    public static GUISafeLARPManager get() {
        return instance;
    }

    interface Action<T> {

        T doWork();
    }

    <T> void safeExec(final Action<T> action, final SafeCallback<T> cb) {
        new Thread(new Runnable() {

            public void run() {
                final T result = action.doWork();
                Display.getDefault().asyncExec(new Runnable() {

                    public void run() {
                        if (cb != null) cb.onReturn(result);
                    }
                });
            }
        }).start();
    }

    public <T> void add(final Class<T> cls, final T obj, final SafeCallback<Long> cb) {
        safeExec(new Action<Long>() {

            public Long doWork() {
                return mngr.add(cls, obj);
            }
        }, cb);
    }

    public <T> void del(final Class<T> cls, final long id, final SafeCallback<Boolean> cb) {
        safeExec(new Action<Boolean>() {

            public Boolean doWork() {
                return mngr.del(cls, id);
            }
        }, cb);
    }

    public void deleteAll(final SafeCallback<Boolean> cb) {
        safeExec(new Action<Boolean>() {

            public Boolean doWork() {
                return mngr.deleteAll();
            }
        }, cb);
    }

    public <T> void get(final Class<T> cls, final long id, final SafeCallback<T> cb) {
        safeExec(new Action<T>() {

            public T doWork() {
                return mngr.get(cls, id);
            }
        }, cb);
    }

    public <T> void getAll(final Class<T> cls, final SafeCallback<List<T>> cb) {
        safeExec(new Action<List<T>>() {

            public List<T> doWork() {
                return mngr.getAll(cls);
            }
        }, cb);
    }

    public void getCharacters(final String universeName, final SafeCallback<List<CharUniverse>> cb) {
        safeExec(new Action<List<CharUniverse>>() {

            public List<CharUniverse> doWork() {
                return mngr.getCharacters(universeName);
            }
        }, cb);
    }

    public void getQuests(final SafeCallback<List<QuestSummary>> cb) {
        safeExec(new Action<List<QuestSummary>>() {

            public List<QuestSummary> doWork() {
                return mngr.getQuests();
            }
        }, cb);
    }

    public void signIn(final String email, final String password, final SafeCallback<Long> cb) {
        safeExec(new Action<Long>() {

            public Long doWork() {
                return mngr.signIn(email, password);
            }
        }, cb);
    }

    public void signUp(final Player player, final SafeCallback<Boolean> cb) {
        safeExec(new Action<Boolean>() {

            public Boolean doWork() {
                return mngr.signUp(player);
            }
        }, cb);
    }

    public void summaryFromQuest(final Quest quest, final SafeCallback<QuestSummary> cb) {
        safeExec(new Action<QuestSummary>() {

            public QuestSummary doWork() {
                return mngr.summaryFromQuest(quest);
            }
        }, cb);
    }

    public <T> void update(final Class<T> cls, final T obj, final SafeCallback<Boolean> cb) {
        safeExec(new Action<Boolean>() {

            public Boolean doWork() {
                return mngr.update(cls, obj);
            }
        }, cb);
    }

    public void updateDB(final SafeCallback<Boolean> cb) {
        safeExec(new Action<Boolean>() {

            public Boolean doWork() {
                return mngr.updateDBfromFreebase();
            }
        }, cb);
    }

    public void addParticipant(final long questID, final long playerID, final long characterID, final SafeCallback<Long> cb) {
        safeExec(new Action<Long>() {

            public Long doWork() {
                return mngr.addParticipant(questID, playerID, characterID);
            }
        }, cb);
    }

    public void removeParticipant(final long questID, final long playerID, final SafeCallback<Boolean> cb) {
        safeExec(new Action<Boolean>() {

            public Boolean doWork() {
                return mngr.removeParticipant(questID, playerID);
            }
        }, cb);
    }
}

interface SafeCallback<T> {

    void onReturn(T result);
}

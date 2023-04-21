package alt.djudge.frontend.server.models.scores;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.criterion.Order;
import alt.djudge.frontend.server.Deployment;
import alt.djudge.frontend.server.datatypes.UserEntry;
import alt.djudge.frontend.server.datatypes.scores.UserScoreEntry;
import alt.djudge.frontend.server.models.AbstractBigtableModelCore;
import alt.djudge.frontend.server.models.AbstractDBModelCore;
import alt.djudge.frontend.server.models.CoreInterface;
import alt.djudge.frontend.server.models.UsersModel;

public class UserScoreModel {

    private static CoreInterface<UserScoreEntry> core = Deployment.isGAE() ? new AbstractBigtableModelCore<UserScoreEntry>() {

        @Override
        protected Order getOrdering() {
            return Order.asc("id");
        }

        @Override
        protected Class<UserScoreEntry> getEntryClass() {
            return UserScoreEntry.class;
        }
    } : new AbstractDBModelCore<UserScoreEntry>() {

        @Override
        protected Order getOrdering() {
            return Order.asc("id");
        }

        @Override
        protected Class<UserScoreEntry> getEntryClass() {
            return UserScoreEntry.class;
        }
    };

    public static List<UserScoreEntry> getAllEntries() {
        List<UserScoreEntry> list = (List<UserScoreEntry>) core.getAllEntries();
        return list;
    }

    public static UserScoreEntry getUserEntry(Long userId) {
        UserScoreEntry entry = null;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        List<UserScoreEntry> list = (List<UserScoreEntry>) core.getEntries(map);
        if (list.size() > 0) {
            entry = list.get(0);
        }
        return entry;
    }

    public static Map<Long, UserScoreEntry> getEntriesMap() {
        HashMap<Long, UserScoreEntry> map = new HashMap<Long, UserScoreEntry>();
        List<UserEntry> userEntries = UsersModel.getAllEntries();
        for (UserEntry userEntry : userEntries) map.put(userEntry.getId(), getUserEntry(userEntry.getId()));
        return map;
    }

    public static void deleteAllEntries() {
        core.deleteAllEntries();
    }
}

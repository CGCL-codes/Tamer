package alt.djudge.frontend.server.gui;

import java.util.Map;
import alt.djudge.frontend.server.datatypes.SessionEntry;
import alt.djudge.frontend.server.datatypes.UserEntry;
import alt.djudge.frontend.server.models.UsersModel;

public class SessionsFormatter extends AbstractFormatter<SessionEntry> {

    private final Map<Long, UserEntry> usersMap = UsersModel.getEntriesMap();

    @Override
    protected String getCustomRowClass(SessionEntry entry) {
        return null;
    }

    @Override
    protected void addClassData(SessionEntry entry) {
        SessionEntry s = (SessionEntry) entry;
        addTableData(s.getId());
        addTableData(usersMap.get(s.getUserId()).getUsername());
        addTableData(s.getIp());
        addTableData(s.getCreateTime());
        addTableData(s.getExpireTime());
        addTableData(s.getAuthToken().substring(0, 20) + "...");
    }

    @Override
    protected void addClassHeaders() {
        addTableHeader("ID");
        addTableHeader("UserID");
        addTableHeader("IP");
        addTableHeader("Created");
        addTableHeader("Expires");
        addTableHeader("Token");
    }
}

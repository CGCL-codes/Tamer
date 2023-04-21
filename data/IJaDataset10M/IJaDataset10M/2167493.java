package macromedia.qa.metrics;

import java.util.Map;
import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

/**
 * A Project instance represents a row in the
 * Project table in the Metrics Database.
 *
 * A project must have a name.
 *
 * @author Peter Farland
 */
public class Project extends Persistable {

    public static final String TABLE_NAME = "Project";

    public final String name;

    public String contact;

    public String email;

    private Map clause;

    private String[] tables = new String[] { TABLE_NAME };

    Project(String name) {
        this.name = name;
    }

    public void load(MetricsDatabase database) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = get(database);
            rs = statement.executeQuery();
            rs.first();
            Object i = rs.getObject("id");
            if (i != null) {
                id = MetricsDatabase.getId(i);
                contact = rs.getString("contact");
                email = rs.getString("email");
            }
        } catch (SQLException ex) {
        } finally {
            closeResultSet(rs);
            closeStatement(statement);
        }
    }

    public String getIdentity() {
        return TABLE_NAME + ": " + name + ", id: " + id;
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    protected String[] getTables() {
        return tables;
    }

    protected Map getInserts() {
        Map inserts = getUpdates();
        inserts.put("name", name);
        return inserts;
    }

    protected Map getUpdates() {
        HashMap updates = new HashMap();
        if (contact != null) updates.put("contact", contact);
        if (email != null) updates.put("email", email);
        return updates;
    }

    protected Map getClauses() {
        if (clause == null) {
            clause = new HashMap();
            clause.put("name", name);
        }
        if (id >= 0) clause.put("id", new Long(id));
        return clause;
    }
}

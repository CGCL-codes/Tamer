package pub.db;

import pub.beans.*;
import pub.utils.StringUtils;
import java.sql.*;
import java.util.*;

public class PubSourceTable extends GeneralDB {

    /**
     * Constructor takes a database connection.
     */
    public PubSourceTable(pub.db.PubConnection conn) {
        super(conn, "pub_source", "id");
    }

    public String addEntry(Map map) {
        try {
            String new_id = super.addEntry(map);
            this.touch(new_id, "date_updated");
            this.touch(new_id, "date_entered");
            return new_id;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List getAllTextBooks() {
        try {
            List l = new ArrayList();
            PreparedStatement stmt = conn.prepareStatement("select id from pub_source " + " where is_textbook='y' and is_obsolete='n'");
            try {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    l.add(pub.beans.BeanFactory.getPubSourceBean(conn, rs.getString(1)));
                }
                return l;
            } finally {
                stmt.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List pubSourceTypes() {
        try {
            String query = "select distinct type from pub_source where type is not null";
            PreparedStatement stmt = conn.prepareStatement(query);
            try {
                java.util.List types = new ArrayList();
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    types.add(rs.getString(1));
                }
                return types;
            } finally {
                stmt.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public TreeMap pubSourceTypesAsMap() {
        TreeMap choices = new TreeMap();
        for (int i = 0; i < pubSourceTypes().size(); i++) {
            choices.put((String) pubSourceTypes().get(i), (String) pubSourceTypes().get(i));
        }
        return choices;
    }

    public String lookupThroughIssn(String issn_number) {
        String[] fields = { "issn_number", "essn_number" };
        for (int i = 0; i < fields.length; i++) {
            if (lookup(fields[i], issn_number).length() > 0) {
                return lookup(fields[i], issn_number);
            }
        }
        return "";
    }

    public String lookup(String pub_source_name) {
        try {
            String query = "select id from  pub_source  where " + "name=? and (is_obsolete = 'n' or is_obsolete is null )limit 1";
            PreparedStatement stmt = conn.prepareStatement(query);
            try {
                stmt.setString(1, pub_source_name);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getString(1);
                } else {
                    return "";
                }
            } finally {
                stmt.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * obsolete a pubsource entry
     * this should merge all the articles using this pubsource and the aliases 
     * of this pubsource
     */
    public void updateObsoleteField(String pubsource_id, String is_obsolete, String replaced_by, String user_id) {
        pub.db.command.ObsoletePubSource cmd = new pub.db.command.ObsoletePubSource();
        cmd.setConnection(conn);
        cmd.setPubSourceId(pubsource_id);
        cmd.setObsolete(StringUtils.isTrue(is_obsolete));
        if (StringUtils.isNotEmpty(replaced_by)) {
            cmd.setReplacedBy(replaced_by);
        }
        cmd.setUpdatedBy(user_id);
        cmd.execute();
    }
}

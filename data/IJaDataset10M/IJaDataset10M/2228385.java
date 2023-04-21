package de.benedetto.database;

import java.sql.*;

public class DB_RootNodeIndex extends DB_RootNode {

    public DB_RootNodeIndex(int p_id, String p_name, int p_noChildren, Database p_database, String p_SQL) {
        super(p_id, p_name, p_noChildren, p_database, p_SQL);
    }

    public DB_TreeNode getChildNode(int p_id, String p_name, int p_noChildren) {
        return new DB_TreeNodeIndex(p_id, p_name, p_noChildren, database, "SELECT e.id, bf_entry_name(coalesce(e.e_part, ''), coalesce(bf_entry_person(e.id), ''), coalesce(bf_entry_title(e.id), '')) AS name, e.e_num_children AS num_children\n" + "  FROM b_entry e, b_addedentry ae\n" + "  WHERE ae.ae_index = " + p_id + "\n" + "    AND ae.ae_entry = e.id\n" + "  ORDER BY name;");
    }
}

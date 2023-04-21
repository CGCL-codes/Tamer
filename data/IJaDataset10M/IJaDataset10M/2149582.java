package org.nodal.storage.jdbc;

import org.nodal.util.Name;
import org.nodal.util.Namespace;
import org.nodal.util.NamespaceImpl;
import java.sql.*;
import java.util.Hashtable;

/**
 * @author Lee Iverson <leei@ece.ubc.ca> 
 */
public class NamespaceRef {

    static boolean createTables(jdbcRepository r) {
        String stmt = "create table _ndl_namespace (" + "  ns_id int not null primary key," + "  uri varchar not null" + ");";
        try {
            r.submit(stmt);
            r.commit();
            r.addTable("_ndl_namespace");
            return true;
        } catch (SQLException e) {
            System.out.println("commit: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    final jdbcRepository repo;

    final int ns_id;

    final Namespace ns;

    final Hashtable names;

    private NamespaceRef(jdbcRepository r, int id, Namespace n) {
        System.out.println("new NamespaceRef(" + id + ", " + n.uri() + ")");
        repo = r;
        ns_id = id;
        ns = n;
        names = new Hashtable();
    }

    static PreparedStatement id_getter = null;

    static NamespaceRef get(jdbcRepository r, int ns_id) {
        try {
            if (id_getter == null) id_getter = r.prepare("SELECT * FROM _ndl_namespace WHERE ns_id = ?;");
            id_getter.setInt(1, ns_id);
            System.out.println("NamespaceRef.get (" + ns_id + ")");
            ResultSet rs = id_getter.executeQuery();
            if (rs.next()) {
                return new NamespaceRef(r, ns_id, NamespaceImpl.use(rs.getString("uri")));
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    static PreparedStatement uri_getter = null;

    static PreparedStatement uri_create = null;

    static NamespaceRef get(jdbcRepository r, Namespace ns) {
        try {
            String uri = ns.uri();
            if (uri_getter == null) uri_getter = r.prepare("SELECT * FROM _ndl_namespace WHERE uri = ?;");
            uri_getter.setString(1, uri);
            ResultSet rs = uri_getter.executeQuery();
            if (rs.next()) {
                System.out.println("NamespaceRef.get (" + uri + ")");
                return new NamespaceRef(r, rs.getInt("ns_id"), ns);
            } else {
                if (uri_create == null) {
                    String create = "INSERT INTO _ndl_namespace VALUES(?,?);";
                    uri_create = r.prepare(create);
                }
                System.out.println("NamespaceRef.get (" + uri + "): create");
                uri_create.setInt(1, r.nextID("_ndl_namespace"));
                uri_create.setString(2, uri);
                if (uri_create.executeUpdate() == 1) return get(r, ns);
            }
        } catch (SQLException e) {
            System.out.println("NamespaceRef.get: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    NameRef nameRef(Name nm) {
        NameRef name = (NameRef) names.get(nm);
        if (name == null) {
            name = NameRef.get(repo, nm, this);
            names.put(nm, name);
        }
        return name;
    }
}

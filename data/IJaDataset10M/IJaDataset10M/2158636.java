package yajcp.core.modules.menu.classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import yajcp.core.classes.MySQL;

public class TypeMenu {

    private Integer id;

    private String name;

    private String description;

    public TypeMenu() {
        new MySQL();
    }

    public void add(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Boolean load(Integer id) throws SQLException {
        this.id = id;
        ResultSet rs = MySQL.connection.createStatement().executeQuery("SELECT * FROM menus WHERE id = " + id);
        rs.first();
        name = rs.getString("name");
        description = rs.getString("description");
        return true;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String toString() {
        return id + "|" + name + "|" + description;
    }
}

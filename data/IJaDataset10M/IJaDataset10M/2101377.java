package se.sandos.pediasuckr;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Concept {

    private int id = -1;

    private List links = new LinkedList();

    private String name;

    public Concept(String name) {
        this.name = name;
    }

    public Concept(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public void addLink(Link l) {
        links.add(l);
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Concept <").append(name).append("> with ").append(links.size()).append(" links.\n");
        Iterator i = links.iterator();
        while (i.hasNext()) {
            Link l = (Link) i.next();
            sb.append("\t").append(l);
        }
        sb.append("\n");
        return sb.toString();
    }

    public static Concept getConcept(String c, String namespace) throws SQLException {
        Test.prepSelectConcept.setString(1, c);
        Test.prepSelectConcept.executeQuery();
        try {
            ResultSet rs = Test.prepSelectConcept.getResultSet();
            if (rs.next()) {
                return new Concept(rs.getString(2), new Integer(rs.getInt(1)));
            }
        } catch (Throwable e) {
        }
        Test.prepInsertConcept.setString(1, c);
        Test.prepInsertConcept.setString(2, namespace);
        Test.prepInsertConcept.executeUpdate();
        return getConcept(c);
    }

    public static Concept getConcept(String c) throws SQLException {
        return getConcept(c, null);
    }
}

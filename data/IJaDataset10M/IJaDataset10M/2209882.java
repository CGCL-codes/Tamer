package storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import ants.Taste;
import ants.AntNeighbourHood.Storetype;

public class Database implements Store {

    private static Connection con;

    private static PreparedStatement[] INSERT, REMOVE, GET_TRIPEL, GET_HASHES;

    public void init(String name) {
        try {
            System.out.println("Initialazing Database");
            Class.forName("org.hsqldb.jdbcDriver");
            con = DriverManager.getConnection("jdbc:hsqldb:file:" + name, "SA", "");
            droptables();
            inittables();
            initPreparedStatements();
            System.out.println("Initialazing for Database finished");
        } catch (Exception e) {
            System.out.println("ERROR: failed to load HSQLDB JDBC driver.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void inittables() {
        try {
            for (Storetype type : Storetype.values()) {
                execute("CREATE TABLE " + type + " (hash bigint, subject varchar(100), predicate varchar(100),object varchar(100));");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void droptables() {
        try {
            for (Storetype type : Storetype.values()) execute("DROP TABLE " + type);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void initPreparedStatements() {
        try {
            INSERT = new PreparedStatement[Storetype.values().length];
            REMOVE = new PreparedStatement[Storetype.values().length];
            GET_TRIPEL = new PreparedStatement[Storetype.values().length];
            GET_HASHES = new PreparedStatement[Storetype.values().length];
            for (int i = 0; i < Storetype.values().length; i++) {
                INSERT[i] = con.prepareStatement("INSERT INTO " + Storetype.values()[i] + " VALUES (?,?,?,?)");
                GET_HASHES[i] = con.prepareStatement("SELECT DISTINCT (hash) FROM " + Storetype.values()[i]);
                REMOVE[i] = con.prepareStatement("DELETE FROM " + Storetype.values()[i] + " WHERE hash = ?");
                GET_TRIPEL[i] = con.prepareStatement("SELECT subject, predicate, object FROM " + Storetype.values()[i] + " WHERE hash=?");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void writeMultiple(int hash, Storetype type, Collection<String[]> tripels) {
        for (String[] tripel : tripels) {
            write(hash, type, tripel);
        }
    }

    public int getMostInhomogenTripel(Storetype type, Taste locaTaste) {
        return 0;
    }

    public void write(int hash, Storetype type, String[] tripel) {
        try {
            PreparedStatement statement = INSERT[type.ordinal()];
            statement.setInt(1, hash);
            statement.setString(2, tripel[0]);
            statement.setString(3, tripel[1]);
            statement.setString(4, tripel[2]);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Set<Integer> getHashes(Storetype type) {
        Set<Integer> resultSet = new HashSet<Integer>();
        try {
            PreparedStatement statement = GET_HASHES[type.ordinal()];
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                resultSet.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public Collection<String[]> read(Storetype type, int hash) {
        List<String[]> result = new LinkedList<String[]>();
        PreparedStatement statement = GET_TRIPEL[type.ordinal()];
        try {
            statement.setInt(1, hash);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                result.add(new String[] { rs.getString(1), rs.getString(2), rs.getString(3) });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(result);
        return result;
    }

    public List<String[]> delete(Storetype type, int hash) {
        try {
            PreparedStatement statement = REMOVE[type.ordinal()];
            System.out.println(hash);
            statement.setInt(1, hash);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean execute(String query) throws SQLException {
        boolean success = true;
        try {
            PreparedStatement stm = con.prepareStatement(query);
            stm.executeUpdate();
            stm.close();
            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (Exception rbex) {
                rbex.printStackTrace();
            }
            success = false;
            throw e;
        }
        return success;
    }
}

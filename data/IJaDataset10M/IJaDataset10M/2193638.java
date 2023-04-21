package storeMovie.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    public static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static final ThreadLocal session = new ThreadLocal();

    public static Session currentSession() throws HibernateException {
        Session s = (Session) session.get();
        if (s == null) {
            s = sessionFactory.openSession();
            session.set(s);
        }
        return s;
    }

    public static void closeSession() throws HibernateException {
        Session s = (Session) session.get();
        if (s != null) s.close();
        session.set(null);
    }

    static Connection conn;

    static Statement st;

    public static void setup(String sql) {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            System.out.println("Driver Loaded.");
            String url = "jdbc:hsqldb:data/tutorial";
            conn = DriverManager.getConnection(url, "sa", "");
            System.out.println("Got Connection.");
            st = conn.createStatement();
            st.executeUpdate(sql);
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void checkData(String sql) {
        try {
            HibernateUtil.outputResultSet(st.executeQuery(sql));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void outputResultSet(ResultSet rs) throws Exception {
        ResultSetMetaData metadata = rs.getMetaData();
        int numcols = metadata.getColumnCount();
        String[] labels = new String[numcols];
        int[] colwidths = new int[numcols];
        int[] colpos = new int[numcols];
        int linewidth;
        linewidth = 1;
        for (int i = 0; i < numcols; i++) {
            colpos[i] = linewidth;
            labels[i] = metadata.getColumnLabel(i + 1);
            int size = metadata.getColumnDisplaySize(i + 1);
            if (size > 30 || size == -1) size = 30;
            int labelsize = labels[i].length();
            if (labelsize > size) size = labelsize;
            colwidths[i] = size + 1;
            linewidth += colwidths[i] + 2;
        }
        StringBuffer divider = new StringBuffer(linewidth);
        StringBuffer blankline = new StringBuffer(linewidth);
        for (int i = 0; i < linewidth; i++) {
            divider.insert(i, '-');
            blankline.insert(i, " ");
        }
        for (int i = 0; i < numcols; i++) divider.setCharAt(colpos[i] - 1, '+');
        divider.setCharAt(linewidth - 1, '+');
        System.out.println(divider);
        StringBuffer line = new StringBuffer(blankline.toString());
        line.setCharAt(0, '|');
        for (int i = 0; i < numcols; i++) {
            int pos = colpos[i] + 1 + (colwidths[i] - labels[i].length()) / 2;
            overwrite(line, pos, labels[i]);
            overwrite(line, colpos[i] + colwidths[i], " |");
        }
        System.out.println(line);
        System.out.println(divider);
        while (rs.next()) {
            line = new StringBuffer(blankline.toString());
            line.setCharAt(0, '|');
            for (int i = 0; i < numcols; i++) {
                Object value = rs.getObject(i + 1);
                overwrite(line, colpos[i] + 1, value.toString().trim());
                overwrite(line, colpos[i] + colwidths[i], " |");
            }
            System.out.println(line);
        }
        System.out.println(divider);
    }

    static void overwrite(StringBuffer b, int pos, String s) {
        int len = s.length();
        for (int i = 0; i < len; i++) b.setCharAt(pos + i, s.charAt(i));
    }
}

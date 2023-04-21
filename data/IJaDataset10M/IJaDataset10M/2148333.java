package in.espirit.tracer.database.dao;

import in.espirit.tracer.database.connection.ConnectionFactory;
import in.espirit.tracer.database.connection.ConnectionPool;
import in.espirit.tracer.model.Mail;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.log4j.Logger;

public class MailDao {

    private static Logger logger = Logger.getLogger(TicketDao.class.getName());

    public static Mail getMailTemplate(String name) throws Exception {
        ConnectionPool pool = ConnectionFactory.getPool();
        Connection con = pool.getConnection();
        Statement st = null;
        ResultSet rs = null;
        Mail template = new Mail();
        String query = "";
        query = "SELECT f_subject, f_body FROM t_mailtemplates WHERE f_name='" + name + "'";
        try {
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                template.setSubject(rs.getString(1));
                template.setMessage(rs.getString(2));
            }
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        } catch (Exception e) {
            logger.error("Getting all activities failed with " + e.getMessage());
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            throw new Exception(e.getMessage());
        } finally {
            if (con != null) con.close();
        }
        return template;
    }
}

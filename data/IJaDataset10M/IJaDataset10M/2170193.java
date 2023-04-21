package homelesspartners.server.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import ashbyutils.database.testing.JndiUnitTestHelper;
import ashbyutils.email.Email;

public class ThankYouSender {

    private static final String FILE_NAME = "src/homelesspartners/server/DatabaseUnitTests.properties";

    private static final String EMAIL_TEXT = "src/homelesspartners/server/utils/Text.properties";

    private static String emailText = null;

    private static String emailListIntro = null;

    private static String emailSignOff = null;

    /**
	 * @param args
	 * @throws NamingException
	 * @throws IOException
	 * @throws SQLException
	 */
    public static void main(String[] args) throws IOException, NamingException, SQLException {
        System.out.println("thanking the donors");
        String fileName = null;
        if (args.length == 0) {
            fileName = FILE_NAME;
        } else {
            fileName = args[0];
        }
        JndiUnitTestHelper.init(fileName);
        InitialContext context = new InitialContext();
        DataSource dataSource = (DataSource) context.lookup("java:/comp/env/jdbc/homelesspartners");
        Connection connection = dataSource.getConnection();
        ResourceBundle bundle = ResourceBundle.getBundle("homelesspartners.server.utils.Text");
        emailText = bundle.getString("email.thankyou");
        emailListIntro = bundle.getString("email.list");
        emailSignOff = bundle.getString("email.end");
        String getDonorListSQL = "SELECT DISTINCT U.USER_ID AS EMAIL, U.ID AS ID FROM HOMELESSPARTNERS.USERS U, HOMELESSPARTNERS.PLEDGES P WHERE U.ID=P.FK_USER_ID AND P.TIME_STAMP >= DATE_SUB(NOW(), INTERVAL 1 DAY);";
        String getGiftListSQL = "SELECT S.FIRST_NAME, S.LAST_INITIAL, S.ASSIGNED_ID, G.DESCRIPTION FROM HOMELESSPARTNERS.STORIES S, HOMELESSPARTNERS.GIFTS G, HOMELESSPARTNERS.PLEDGES P WHERE P.FK_USER_ID=? AND P.FK_GIFT_ID=G.ID AND G.FK_STORIES_ID=S.ID ORDER BY S.FIRST_NAME;";
        PreparedStatement getDonorListStatement = connection.prepareStatement(getDonorListSQL);
        PreparedStatement getGiftListStatement = connection.prepareStatement(getGiftListSQL);
        ResultSet results = getDonorListStatement.executeQuery();
        while (results.next()) {
            String emailAddress = results.getString("EMAIL");
            System.out.println(emailAddress);
            int id = results.getInt("ID");
            Email email = Email.createInstance();
            email.setTo(emailAddress);
            email.setFrom("homelesspartners@gmail.com");
            email.setSubject("Thank You!");
            email.setHtml(true);
            email.setCc("homelesspartners@gmail.com");
            email.setBcc("ashbygreg@yahoo.com");
            email.setBody(getEmailText(getGiftListStatement, id));
            try {
                email.send();
            } catch (AddressException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Done thanking the donors");
    }

    private static String getEmailText(PreparedStatement getGiftListStatement, int id) throws SQLException {
        getGiftListStatement.setInt(1, id);
        ResultSet results = getGiftListStatement.executeQuery();
        StringBuffer buffer = new StringBuffer();
        buffer.append(emailText);
        buffer.append(emailListIntro);
        while (results.next()) {
            String name = results.getString("FIRST_NAME");
            String initial = results.getString("LAST_INITIAL");
            String assignedId = results.getString("ASSIGNED_ID");
            String descripton = results.getString("DESCRIPTION");
            buffer.append("- ");
            buffer.append(descripton);
            buffer.append(" (");
            buffer.append(name);
            buffer.append(" ");
            buffer.append(initial);
            buffer.append("., #");
            buffer.append(assignedId);
            buffer.append(")<br />");
        }
        buffer.append(emailSignOff);
        return buffer.toString();
    }
}

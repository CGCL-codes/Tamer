package be.oxys.itimesheets.reminders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import be.oxys.itimesheets.exceptions.ReminderException;
import be.oxys.itimesheets.tos.UserTO;

/**
 * This class sends a reminder to all employees who have not filled for at least
 * 4 days and a min. of 7.6h/day
 * 
 * @author jnc
 * 
 */
public class EmployeeWeeklyReminder {

    public void sendReminder(Calendar firstDay, Connection connection, boolean reallySend, boolean forceSend, Session mailSession, String adminEmail) throws ReminderException {
        if (firstDay.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            throw new ReminderException("Firstday should be Monday");
        }
        Calendar lastDay = Calendar.getInstance();
        lastDay.setTimeInMillis(firstDay.getTimeInMillis());
        lastDay.add(Calendar.DAY_OF_MONTH, 7);
        Calendar activeOn = Calendar.getInstance();
        activeOn.setTimeInMillis(firstDay.getTimeInMillis());
        activeOn.set(Calendar.DAY_OF_MONTH, 1);
        activeOn.add(Calendar.MONTH, 1);
        Logger.getLogger("iTimesheets.EmployeeWeeklyReminder").fine("First day: " + firstDay.getTime());
        Logger.getLogger("iTimesheets.EmployeeWeeklyReminder").fine("Last day: " + lastDay.getTime());
        Logger.getLogger("iTimesheets.EmployeeWeeklyReminder").fine("ActiveOn: " + activeOn.getTime());
        List<UserTO> incompleteUsers = processUsers(connection, firstDay.getTime(), lastDay.getTime(), activeOn.getTime(), forceSend);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        for (UserTO user : incompleteUsers) {
            System.out.println("User " + user.getUserid() + " (" + user.getFirstName() + " " + user.getLastName() + ")" + " has incomplete timesheets");
            if (reallySend) {
                if ((user.getEmail() != null) && (user.getEmail().length() > 0)) {
                    Message message = new MimeMessage(mailSession);
                    try {
                        message.setFrom(new InternetAddress(adminEmail));
                        message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
                        message.addRecipient(Message.RecipientType.CC, new InternetAddress(adminEmail));
                        message.setSubject("Incomplete timesheets");
                        StringBuffer sb = new StringBuffer();
                        sb.append("Dear ");
                        sb.append(user.getFirstName());
                        sb.append(",\n\n");
                        sb.append("Your timesheets for the week of ");
                        sb.append(sdf.format(firstDay.getTime()));
                        sb.append(" are incomplete, which means either that you have reported hours for less than 4 days ");
                        sb.append(" or that the total number of hours for the week is below 30. ");
                        sb.append(" Could you please fill in the missing data?\n\n");
                        sb.append("Thanks a lot\n\n");
                        sb.append("The Timesheets administrators");
                        message.setText(sb.toString());
                        Transport.send(message);
                    } catch (MessagingException ex) {
                        System.err.println("Cannot send email. " + ex);
                    }
                } else {
                    Logger.getLogger("iTimesheets.EmployeeWeeklyReminder").warning("User " + user.getUserid() + "(" + user.getFirstName() + " " + user.getLastName() + ")" + " has no email");
                }
            }
        }
    }

    private List<UserTO> processUsers(Connection connection, Date firstDay, Date lastDay, Date activeOn, boolean forceSend) throws ReminderException {
        ResultSet rset = null;
        PreparedStatement stmt = null;
        List<UserTO> users = new ArrayList<UserTO>();
        Logger.getLogger("iTimesheets.EmployeeWeeklyReminder").fine("Entering processUsers");
        try {
            try {
                String queryString = "select users.*, Z.line_date, sum(Z.line_hours) as sumhours " + "from users left join " + "((select * from reportlines where (reportlines.line_date >= ?) " + "and (reportlines.line_date < ?)) as Z) " + "on users.userid = Z.userid " + "where (users.exitdate >= ? or users.exitdate is null) ";
                if (!forceSend) {
                    queryString += "and (users.sendreminders = true) ";
                }
                queryString += "group by users.userid, Z.line_date ";
                stmt = connection.prepareStatement(queryString);
                stmt.setDate(1, new java.sql.Date(firstDay.getTime()));
                stmt.setDate(2, new java.sql.Date(lastDay.getTime()));
                stmt.setDate(3, new java.sql.Date(activeOn.getTime()));
                rset = stmt.executeQuery();
                long currentUserId = -1;
                int rowNumber = 0;
                float totalHours = 0F;
                UserTO user = new UserTO();
                while (rset.next()) {
                    long userId = rset.getLong("userid");
                    if (userId != currentUserId) {
                        if (currentUserId != -1) {
                            if ((rowNumber < 4) || (totalHours < 30)) {
                                users.add(user);
                            }
                        }
                        user = new UserTO();
                        user.setUserid(userId);
                        user.setUsername(rset.getString("username"));
                        user.setFirstName(rset.getString("firstname"));
                        user.setLastName(rset.getString("lastname"));
                        user.setEmail(rset.getString("email"));
                        currentUserId = userId;
                        rowNumber = 0;
                        totalHours = 0F;
                        Logger.getLogger("iTimesheets.EmployeeWeeklyReminder").fine("Processing " + user.getUsername());
                    }
                    Logger.getLogger("iTimesheets.EmployeeWeeklyReminder").fine("sumhours: " + rset.getFloat("sumhours"));
                    rowNumber++;
                    totalHours += rset.getFloat("sumhours");
                }
                if (currentUserId != -1) {
                    if ((rowNumber < 4) || (totalHours < 30)) {
                        users.add(user);
                    }
                }
            } finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
                if (rset != null) {
                    rset.close();
                    rset = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            }
        } catch (SQLException e) {
            throw new ReminderException(e.getMessage());
        }
        return users;
    }
}

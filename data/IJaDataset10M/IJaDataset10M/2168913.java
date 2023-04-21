package com.nyandu.weboffice.mail.database.mysql;

import com.nyandu.weboffice.common.database.BaseDAO;
import com.nyandu.weboffice.common.database.DBDataSource;
import com.nyandu.weboffice.common.database.DAOException;
import com.nyandu.weboffice.common.database.mysql.MySQLDataSource;
import com.nyandu.weboffice.common.util.Util;
import com.nyandu.weboffice.mail.business.MailMessage;
import com.nyandu.weboffice.mail.database.IMailMessageDAO;
import com.nyandu.weboffice.calendar.business.DateTime;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.Date;
import java.text.ParseException;

/**
 * 
 *  The contents of this file are subject to the Nandu Public License
 * Version 1.1 ("License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.nyandu.com
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and limitations
 * under the License.
 * 
 * The Initial Developer of the Original Code is User.
 * Portions created by User are Copyleft (C) www.nyandu.com. 
 * All Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * User: ern
 * Date: Mar 15, 2005
 * Time: 5:02:43 PM
 */
public class MySQLIMailsDAO extends BaseDAO implements IMailMessageDAO {

    private static final String SELECT_MAILS_BY_ACCOUNT = "SELECT " + "id, " + "account_id, " + "parent_folder_id, " + "`from`, " + "`to`, " + "cc, " + "date, " + "subject, " + "content, " + "received_date, " + "marked, " + "opened, " + "flag, " + "attachment, " + "size " + "FROM account_mails " + "WHERE account_id = {0}";

    private static final String SELECT_MAILS_BY_ACCOUNT_AND_FOLDER = "SELECT " + "id, " + "account_id, " + "parent_folder_id, " + "`from`, " + "`to`, " + "cc, " + "date, " + "subject, " + "content, " + "received_date, " + "marked, " + "opened, " + "flag, " + "attachment, " + "size " + "FROM account_mails " + "WHERE account_id = {0} AND parent_folder_id = {1}";

    private static final String SELECT_MAILS_BY_USER_AND_FOLDER = "SELECT " + "am.id, " + "am.account_id, " + "am.parent_folder_id, " + "am.from, " + "am.to, " + "am.cc, " + "am.date, " + "am.subject, " + "am.content, " + "am.received_date, " + "am.marked, " + "am.opened, " + "am.flag, " + "am.attachment, " + "am.size " + "FROM account_mails am, accounts_folders af " + "WHERE am.parent_folder_id = af.id AND " + "af.user_id = {0} AND am.parent_folder_id = {1}";

    private static final String SELECT_MAIL = "SELECT " + "id, " + "account_id, " + "parent_folder_id, " + "`from`, " + "`to`, " + "cc, " + "date, " + "subject, " + "content, " + "received_date, " + "message_string, " + "marked, " + "opened, " + "flag, " + "attachment, " + "size " + "FROM account_mails " + "WHERE id = {0}";

    private static final String INSERT_MAIL = "INSERT INTO account_mails (" + "account_id, " + "parent_folder_id, " + "`from`, " + "`to`, " + "cc, " + "date, " + "subject, " + "content, " + "received_date, " + "message_string, " + "marked, " + "opened, " + "flag, " + "attachment, " + "size) " + "VALUES ({0}, {1}, '{2}', '{3}', '{4}', '{5}', '{6}', '{7}', '{8}', '{9}', {10}, {11}, {12}, {13}, {14})";

    private static final String UPDATE_MAIL = "UPDATE account_mails SET parent_folder_id = {0} WHERE id = {1}";

    private static final String UPDATE_MAIL_FLAG = "UPDATE account_mails SET {0} = {1} WHERE id = {2}";

    private static final String DELETE_MAIL = "DELETE FROM account_mails WHERE id = {0}";

    private static final String SUM_MAIL_SIZE_BY_USER = "SELECT SUM(am.size) size " + "FROM accounts a, account_mails am " + "WHERE a.id = am.account_id AND a.user_id = {0}";

    private MySQLDataSource dataSource;

    public MySQLIMailsDAO(DBDataSource dataSource) {
        super(dataSource);
        this.dataSource = (MySQLDataSource) dataSource;
    }

    public MailMessage[] selectMailMessagesByAccount(int accountId) throws DAOException {
        String sql = Util.replace(SELECT_MAILS_BY_ACCOUNT, new Integer(accountId));
        LinkedList list = new LinkedList();
        try {
            ResultSet rs = dataSource.executeQuery(sql);
            MailMessage mail;
            while (rs.next()) {
                mail = new MailMessage();
                mail.setNumber(rs.getInt("id"));
                mail.setAccountId(rs.getInt("account_id"));
                mail.setFolderId(rs.getInt("parent_folder_id"));
                mail.setFrom(rs.getString("from"));
                mail.setTo(rs.getString("to"));
                mail.setCc(rs.getString("cc"));
                try {
                    mail.setDateDT(new DateTime(rs.getString("date")));
                } catch (ParseException e) {
                    mail.setDateD(new Date(System.currentTimeMillis()));
                }
                mail.setSubject(rs.getString("subject"));
                mail.setContent(rs.getString("content"));
                try {
                    mail.setReceivedDateDT(new DateTime(rs.getString("received_date")));
                } catch (ParseException pe) {
                    mail.setReceivedDateD(new Date(System.currentTimeMillis()));
                }
                mail.setMarked(rs.getInt("marked"));
                mail.setOpened(rs.getBoolean("opened"));
                mail.setFlag(rs.getBoolean("flag"));
                mail.setAttachment(rs.getBoolean("attachment"));
                mail.setSize(rs.getInt("size"));
                list.addLast(mail);
            }
            rs.close();
        } catch (SQLException sqle) {
            throw new DAOException(sqle);
        }
        MailMessage result[] = new MailMessage[list.size()];
        result = (MailMessage[]) list.toArray(result);
        return result;
    }

    public MailMessage[] selectMailMessagesByAccountAndFolder(int accountId, int folderId) throws DAOException {
        String sql = Util.replace(SELECT_MAILS_BY_ACCOUNT_AND_FOLDER, new Integer(accountId), new Integer(folderId));
        LinkedList list = new LinkedList();
        try {
            ResultSet rs = dataSource.executeQuery(sql);
            MailMessage mail;
            while (rs.next()) {
                mail = new MailMessage();
                mail.setNumber(rs.getInt("id"));
                mail.setAccountId(rs.getInt("account_id"));
                mail.setFolderId(folderId);
                mail.setFrom(rs.getString("from"));
                mail.setTo(rs.getString("to"));
                mail.setCc(rs.getString("cc"));
                try {
                    mail.setDateDT(new DateTime(rs.getString("date")));
                } catch (ParseException e) {
                    mail.setDateD(new Date(System.currentTimeMillis()));
                }
                mail.setSubject(rs.getString("subject"));
                mail.setContent(rs.getString("content"));
                try {
                    mail.setReceivedDateDT(new DateTime(rs.getString("received_date")));
                } catch (ParseException pe) {
                    mail.setReceivedDateD(new Date(System.currentTimeMillis()));
                }
                mail.setMarked(rs.getInt("marked"));
                mail.setOpened(rs.getBoolean("opened"));
                mail.setFlag(rs.getBoolean("flag"));
                mail.setAttachment(rs.getBoolean("attachment"));
                mail.setSize(rs.getInt("size"));
                list.addLast(mail);
            }
            rs.close();
        } catch (SQLException sqle) {
            throw new DAOException(sqle);
        }
        MailMessage result[] = new MailMessage[list.size()];
        result = (MailMessage[]) list.toArray(result);
        return result;
    }

    public MailMessage[] selectMailMessagesByUser(int userId, int folderId) throws DAOException {
        String sql = Util.replace(SELECT_MAILS_BY_USER_AND_FOLDER, new Integer(userId), new Integer(folderId));
        LinkedList list = new LinkedList();
        try {
            ResultSet rs = dataSource.executeQuery(sql);
            MailMessage mail;
            while (rs.next()) {
                mail = new MailMessage();
                mail.setNumber(rs.getInt("am.id"));
                mail.setAccountId(rs.getInt("am.account_id"));
                mail.setFolderId(folderId);
                mail.setFrom(rs.getString("am.from"));
                mail.setTo(rs.getString("am.to"));
                mail.setCc(rs.getString("am.cc"));
                try {
                    mail.setDateDT(new DateTime(rs.getString("am.date")));
                } catch (ParseException e) {
                    mail.setDateD(new Date(System.currentTimeMillis()));
                }
                mail.setSubject(rs.getString("am.subject"));
                mail.setContent(rs.getString("am.content"));
                try {
                    mail.setReceivedDateDT(new DateTime(rs.getString("am.received_date")));
                } catch (ParseException pe) {
                    mail.setReceivedDateD(new Date(System.currentTimeMillis()));
                }
                mail.setMarked(rs.getInt("am.marked"));
                mail.setOpened(rs.getBoolean("am.opened"));
                mail.setFlag(rs.getBoolean("am.flag"));
                mail.setAttachment(rs.getBoolean("am.attachment"));
                mail.setSize(rs.getInt("am.size"));
                list.addLast(mail);
            }
            rs.close();
        } catch (SQLException sqle) {
            throw new DAOException(sqle);
        }
        MailMessage result[] = new MailMessage[list.size()];
        result = (MailMessage[]) list.toArray(result);
        return result;
    }

    public MailMessage selectMailMessages(int id) throws DAOException {
        String sql = Util.replace(SELECT_MAIL, new Integer(id));
        MailMessage mail = null;
        try {
            ResultSet rs = dataSource.executeQuery(sql);
            if (rs.next()) {
                mail = new MailMessage();
                mail.setNumber(rs.getInt("id"));
                mail.setAccountId(rs.getInt("account_id"));
                mail.setFolderId(rs.getInt("parent_folder_id"));
                mail.setFrom(rs.getString("from"));
                mail.setTo(rs.getString("to"));
                mail.setCc(rs.getString("cc"));
                try {
                    mail.setDateDT(new DateTime(rs.getString("date")));
                } catch (ParseException e) {
                    mail.setDateD(new Date(System.currentTimeMillis()));
                }
                mail.setSubject(rs.getString("subject"));
                mail.setContent(rs.getString("content"));
                try {
                    mail.setReceivedDateDT(new DateTime(rs.getString("received_date")));
                } catch (ParseException pe) {
                    mail.setReceivedDateD(new Date(System.currentTimeMillis()));
                }
                mail.setMessageString(rs.getString("message_string"));
                mail.setMarked(rs.getInt("marked"));
                mail.setOpened(rs.getBoolean("opened"));
                mail.setFlag(rs.getBoolean("flag"));
                mail.setAttachment(rs.getBoolean("attachment"));
                mail.setSize(rs.getInt("size"));
            }
            rs.close();
        } catch (SQLException sqle) {
            new DAOException(sqle);
        }
        return mail;
    }

    public MailMessage insertMailMessage(MailMessage mail) throws DAOException {
        String sql = Util.replace(INSERT_MAIL, new Integer(mail.getAccountId()), new Integer(mail.getFolderId()), mail.getFrom(), mail.getTo(), mail.getCc(), mail.getDateDT(), mail.getSubject(), mail.getContent(), mail.getReceivedDateDT(), mail.getMessageString(), new Integer(mail.getMarked()), new Boolean(mail.isOpened()), new Boolean(mail.isFlag()), new Boolean(mail.getAttachment()), new Integer(mail.getSize()));
        try {
            dataSource.executeUpdate(sql);
            mail.setNumber(dataSource.getLastInsertedId());
        } catch (SQLException sqle) {
            throw new DAOException(sqle);
        }
        return mail;
    }

    public void updateMailMessage(int mailId, int folderId) throws DAOException {
        String sql = Util.replace(UPDATE_MAIL, new Integer(folderId), new Integer(mailId));
        try {
            dataSource.executeUpdate(sql);
        } catch (SQLException sqle) {
            throw new DAOException(sqle);
        }
    }

    public void updateMailMessageFlag(int mailId, String flag, boolean value) throws DAOException {
        String sql = Util.replace(UPDATE_MAIL_FLAG, flag, new Boolean(value), new Integer(mailId));
        try {
            dataSource.executeUpdate(sql);
        } catch (SQLException sqle) {
            throw new DAOException(sqle);
        }
    }

    public void deleteMailMessage(int mailId) throws DAOException {
        String sql = Util.replace(DELETE_MAIL, new Integer(mailId));
        try {
            dataSource.executeUpdate(sql);
        } catch (SQLException sqle) {
            throw new DAOException(sqle);
        }
    }

    public long selectSizeByUser(int userId) throws DAOException {
        String sql = Util.replace(SUM_MAIL_SIZE_BY_USER, new Integer(userId));
        long result = 0;
        try {
            ResultSet rs = dataSource.executeQuery(sql);
            if (rs.next()) {
                result = rs.getLong("size");
            }
            rs.close();
        } catch (SQLException sqle) {
            throw new DAOException(sqle);
        }
        return result;
    }
}

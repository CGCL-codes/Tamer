package windu.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import windu.database.Koneksi;
import windu.sms.entity.Contact;
import windu.sms.entity.Inbox;

/**
 *
 * @author Windu Purnomo
 */
public class InboxDao {

    Koneksi objKon = new Koneksi();

    private PreparedStatement delState;

    private PreparedStatement selectState;

    private Connection con = objKon.buatKoneksi();

    private Statement st = null;

    private ResultSet rs = null;

    private String qDelete = "DELETE FROM inbox WHERE receivingDateTime = ? " + "AND senderNumber = ?";

    private String qSelect = "SELECT SenderNumber, textDecoded, receivingDateTime," + " processed FROM inbox ORDER BY receivingDateTime DESC";

    private String qSelectByNama = "SELECT * FROM windu_contact WHERE nama = ?";

    private String qSelectLast = "SELECT * FROM windu_contact ORDER BY id LIMIT 0,1";

    public void deleteInbox(Inbox inbox) {
        try {
            delState = con.prepareStatement(qDelete);
            delState.setString(1, inbox.getReceivingDateTime());
            delState.setString(2, inbox.getSenderNumber());
            int x = delState.executeUpdate();
        } catch (SQLException ex) {
        }
    }

    public List<Inbox> selectInbox() {
        List<Inbox> list = new ArrayList<Inbox>();
        try {
            selectState = con.prepareStatement(qSelect);
            rs = selectState.executeQuery();
            while (rs.next()) {
                Inbox i = new Inbox();
                i.setSenderName(new ContactDao().selectName(rs.getString(1)));
                i.setSenderNumber(rs.getString(1));
                i.setTextDecoded(rs.getString(2));
                i.setReceivingDateTime(rs.getString(3));
                i.setProcessed(rs.getString(4));
                list.add(i);
            }
        } catch (SQLException ex) {
        }
        return list;
    }

    public Contact selectByNama(String nama) {
        try {
            selectState = con.prepareStatement(qSelectLast);
            selectState.setString(1, nama);
            rs = selectState.executeQuery();
            while (rs.next()) {
                Contact c = new Contact();
                c.setId(rs.getInt(1));
                c.setNama(rs.getString(2));
                c.setNohp(rs.getString(3));
                c.setEmail(rs.getString(4));
                return c;
            }
        } catch (SQLException ex) {
        }
        return null;
    }
}

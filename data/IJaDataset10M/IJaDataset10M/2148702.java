package gcr.mmm2.model;

import gcr.mmm2.rdb.RDBConnection;
import gcr.mmm2.rdb.ResultSetWrapper;
import gcr.mmm2.util.StringUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * @author Simon King
 * 
 */
public class ContactImpl implements IContact {

    private int m_id;

    private IPerson m_person;

    private IUser m_user;

    private Date m_createDate;

    private String m_alias;

    private int m_ownerID;

    private String m_phoneNumber;

    protected ContactImpl(int id) throws ObjectNotFoundException {
        this.m_id = id;
        String query = "select c.person_id, c.created_by, c.date_added, p.phone_num, c.alias from contact c left join person_phone p on (c.person_id=p.person_id and c.created_by=p.created_by and p.date_inactive is null) " + "where c.id=" + this.m_id + " and c.date_inactive is null";
        ResultSetWrapper rsw = RDBConnection.executeQuery(query);
        ResultSet rs = rsw.getResultSet();
        try {
            if (rs.next()) {
                m_ownerID = rs.getInt("created_by");
                int personID = rs.getInt("person_id");
                m_phoneNumber = rs.getString("phone_num");
                m_person = new PersonImpl(personID);
                try {
                    m_user = new UserImpl(personID);
                } catch (ObjectNotFoundException e) {
                    m_user = null;
                }
                this.m_alias = StringUtils.URLDecode(rs.getString("alias"));
                this.m_createDate = rs.getDate("date_added");
            } else {
                throw new ObjectNotFoundException("Unable to find contact(id)=" + this.m_id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ObjectNotFoundException("Unable to find contact(id)=" + this.m_id);
        } finally {
            rsw.closeAll();
        }
    }

    public IUser getOwner() {
        return UserManager.getByID(m_ownerID);
    }

    public String getAlias() {
        return this.m_alias;
    }

    private void updateValue(String colName, String newVal) {
        String query = "update contact set " + colName + "=? where id=?";
        Object[] params = { StringUtils.URLEncode(newVal), new Integer(m_id) };
        RDBConnection.executePrepared(query, params);
    }

    private boolean hasPermission(IUser owner) {
        if (m_user != null) {
            return false;
        }
        try {
            return (owner.getID() == m_ownerID || UserManager.isAdmin(owner));
        } catch (Exception e) {
            return false;
        }
    }

    public void setAlias(String alias, IUser owner) throws PermissionException {
        if (owner != null && (owner.getID() == m_ownerID || UserManager.isAdmin(owner))) {
            updateValue("alias", alias);
            this.m_alias = alias;
        } else {
            throw new PermissionException("User " + owner.getID() + " tried to modify contact id " + this.m_id + " owner by " + m_ownerID);
        }
    }

    public List listEmails() {
        if (m_user == null) {
            String query = "select id, email_address, person_id from person_email where person_id=" + m_person.getID() + " and created_by=" + m_ownerID + " and date_inactive is null";
            return PersonManager.listEmails(query);
        }
        return m_user.listEmails();
    }

    public String getPhoneNumber() {
        if (m_user != null) {
            return m_user.getPhoneNumber();
        }
        try {
            return StringUtils.formatPhoneNumber(m_phoneNumber);
        } catch (StringFormatException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setPhoneNumber(String phoneNumber, IUser owner) throws PermissionException, StringFormatException {
        if (!hasPermission(owner)) {
            throw new PermissionException("User " + owner.getID() + " tried to modify contact id " + this.m_id + " owner by " + m_ownerID);
        }
        String digits = StringUtils.extractPhoneDigits(phoneNumber);
        String query = "update person_phone set date_inactive=now() where person_id=? and created_by=?";
        Object[] params = { new Integer(m_person.getID()), new Integer(m_ownerID) };
        RDBConnection.executePrepared(query, params);
        query = "insert into person_phone (id, person_id, phone_num, date_added, created_by) values (nextval('person_phone_id_seq'),?,?,now(),?)";
        Object[] param2 = { new Integer(m_person.getID()), digits, new Integer(m_ownerID) };
        RDBConnection.executePrepared(query, param2);
        m_phoneNumber = digits;
    }

    public EmailAddress getPrimaryEmail() {
        if (m_user != null) {
            return m_user.getPrimaryEmail();
        }
        String query = "select id, email_address from person_email where person_id=" + m_person.getID() + " and created_by=" + m_ownerID + " and date_inactive is null and is_primary=TRUE";
        ResultSetWrapper rsw = RDBConnection.executeQuery(query);
        ResultSet rs = rsw.getResultSet();
        EmailAddress email = null;
        try {
            if (rs.next()) {
                String addr = rs.getString("email_address");
                int id = rs.getInt("id");
                email = new EmailAddress(id, addr, this);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            rsw.closeAll();
        }
        return email;
    }

    private boolean hasEmail(String email) {
        boolean rtnVal = false;
        if (email == null) {
            return false;
        }
        String query = "select person_id from person_email where person_id=" + this.m_id + " and created_by=" + m_ownerID + " and upper(email_address) like '" + email.toUpperCase() + "%' and date_inactive is null";
        ResultSetWrapper rsw = RDBConnection.executeQuery(query);
        ResultSet rs = rsw.getResultSet();
        try {
            if (rs.next()) {
                rtnVal = true;
            } else {
                rtnVal = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            rtnVal = false;
        } finally {
            rsw.closeAll();
        }
        return rtnVal;
    }

    private boolean hasEmail(int id) {
        boolean rtnVal = false;
        String query = "select person_id from person_email where person_id=" + this.m_id + " and created_by=" + m_ownerID + " and id=" + id + " and date_inactive is null";
        ResultSetWrapper rsw = RDBConnection.executeQuery(query);
        ResultSet rs = rsw.getResultSet();
        try {
            if (rs.next()) {
                rtnVal = true;
            } else {
                rtnVal = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            rtnVal = false;
        } finally {
            rsw.closeAll();
        }
        return rtnVal;
    }

    public void addEmail(String email, IUser owner) throws PermissionException, StringFormatException {
        if (!hasPermission(owner)) {
            throw new PermissionException("User " + owner.getID() + " tried to modify contact id " + this.m_id + " owner by " + m_ownerID);
        }
        StringUtils.validateEmail(email);
        if (email != null && !hasEmail(email)) {
            String query = "update person_email set date_inactive=now() where person_id=? and created_by=?";
            Object[] params = { new Integer(m_person.getID()), new Integer(m_ownerID) };
            RDBConnection.executePrepared(query, params);
            query = "insert into person_email (id, person_id, email_address, is_primary, date_added, created_by) " + "values (nextval('person_email_id_seq'),?,?,TRUE,now(),?)";
            Object[] param2 = { new Integer(m_person.getID()), email, new Integer(m_ownerID) };
            RDBConnection.executePrepared(query, param2);
        }
    }

    public void setPrimaryEmail(int id, IUser owner) throws PermissionException {
        if (!hasPermission(owner)) {
            throw new PermissionException("User " + owner.getID() + " tried to modify contact id " + this.m_id + " owner by " + m_ownerID);
        }
        if (hasEmail(id)) {
            String query = "update person_email set is_primary=FALSE where person_id=" + this.m_id + "and id!=" + id + " and created_by=" + m_ownerID + " and date_inactive is null";
            RDBConnection.execute(query);
            query = "update person_email set is_primary=TURE where person_id=" + this.m_id + "and id=" + id + " and created_by=" + m_ownerID + " and date_inactive is null";
            RDBConnection.execute(query);
        }
    }

    public void removeEmail(int id, IUser owner) throws PermissionException {
        if (!hasPermission(owner)) {
            throw new PermissionException("User " + owner.getID() + " tried to modify contact id " + this.m_id + " owner by " + m_ownerID);
        }
        if (hasEmail(id)) {
            String query = "update person_email set date_inactive=now() where id=" + id;
            RDBConnection.execute(query);
        }
    }

    public int getID() {
        return m_person.getID();
    }

    public int getContactID() {
        return this.m_id;
    }

    public String getFirstName() {
        return m_person.getFirstName();
    }

    public String getLastName() {
        return m_person.getLastName();
    }

    public void delete(IUser owner) throws PermissionException {
        if (m_ownerID != owner.getID() && !UserManager.isAdmin(owner)) {
            throw new PermissionException("User " + owner.getID() + " tried to modify contact id " + this.m_id + " owner by " + m_ownerID);
        }
        ContactManager.deleteContact(this);
    }

    public String getURI() {
        return m_person.getURI();
    }

    public String toString() {
        if (this.m_alias != null) {
            return this.m_alias;
        }
        return getFirstName() + " " + getLastName();
    }

    public void setFirstName(String firstName) throws PermissionException {
        ContactManager.setFirstName(this, firstName);
    }

    public void setLastName(String lastName) throws PermissionException {
        ContactManager.setLastName(this, lastName);
    }

    public List listBluetoothDevices() {
        return PersonManager.listBTDevices(this.m_person);
    }

    public void removeBluetoothMAC(String btMAC, IUser owner) throws PermissionException {
        if (!hasPermission(owner)) {
            throw new PermissionException("User " + owner.getID() + " tried to modify contact id " + this.m_id);
        }
        PersonManager.removeBluetoothMAC(this.m_person, btMAC);
    }

    public void addBluetoothMAC(String btMAC, IUser owner) throws PermissionException {
        if (!hasPermission(owner)) {
            throw new PermissionException("User " + owner.getID() + " tried to modify contact id " + this.m_id);
        }
        PersonManager.associateBTDevice(this.m_person, btMAC, owner);
    }
}

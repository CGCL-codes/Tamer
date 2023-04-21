package com.myopa.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.myopa.vo.ContactVo;
import com.myopa.vo.OrganizationContactVo;

/**
 * This class is an implementation of ContactDAO for organization contacts with a PostgreSQL database.
 *
 * @author Clint Burns <c1burns@users.sourceforge.net>
 */
public class ContactDAOImplPostgre_Organization extends ContactDAOImplPostgre {

    public ContactVo getContact(int contactId) throws Exception {
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        OrganizationContactVo contact = new OrganizationContactVo();
        try {
            final String sql = "select name, do_not_contact, comm_method_code, contact_type_code, " + " email from organization where contact_id = ?";
            connection = ConnectionUtility.getInstance().getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, contactId);
            rs = ps.executeQuery();
            if (rs.next()) {
                contact.setName(rs.getString("name"));
                contact.setContactId(contactId);
                contact.setDoNotContact(rs.getBoolean("do_not_contact"));
                contact.setCommunicationMethodCode(rs.getShort("comm_method_code"));
                contact.setContactTypeCode(rs.getShort("contact_type_code"));
                contact.setEmail(rs.getString("email"));
                contact.setAddresses(getAddressesForContact(contactId));
                contact.setPhones(getPhonesForContact(contactId));
            }
        } catch (Exception e) {
            logger.error(CONTACTDAOERROR + e);
            throw e;
        } finally {
            ConnectionUtility.closeJDBCObjects(rs, ps, connection);
        }
        return contact;
    }

    public void updateContact(ContactVo contact) throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        final String sql = "update organization set do_not_contact=?, comm_method_code=?, contact_type_code=?, email=?, name=? " + "where contact_id=?;";
        try {
            OrganizationContactVo orgContact = (OrganizationContactVo) contact;
            connection = ConnectionUtility.getInstance().getConnection();
            ps = connection.prepareStatement(sql);
            ps.setBoolean(1, orgContact.isDoNotContact());
            ps.setShort(2, orgContact.getCommunicationMethodCode());
            ps.setShort(3, orgContact.getContactTypeCode());
            ps.setString(4, orgContact.getEmail());
            ps.setString(5, orgContact.getName());
            ps.setInt(6, orgContact.getContactId());
            ps.execute();
            updateAddresses(orgContact.getAddresses(), orgContact.getContactId(), connection);
            updatePhones(orgContact.getPhones(), orgContact.getContactId(), connection);
            connection.commit();
        } catch (Exception e) {
            logger.error(CONTACTDAOERROR + e);
            throw e;
        } finally {
            ConnectionUtility.closeJDBCObjects(connection, ps);
        }
    }

    public void deleteContact(int contactId) throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        final String sql = "delete from organization where contact_id=?;";
        try {
            connection = ConnectionUtility.getInstance().getConnection();
            deletePhones(contactId, connection);
            deleteAddresses(contactId, connection);
            ps = connection.prepareStatement(sql);
            ps.setInt(1, contactId);
            ps.execute();
            connection.commit();
        } catch (Exception e) {
            logger.error(CONTACTDAOERROR + e);
            throw e;
        } finally {
            ConnectionUtility.closeJDBCObjects(connection, ps);
        }
    }

    public ContactVo createContact(ContactVo contact) throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        OrganizationContactVo orgContact = (OrganizationContactVo) contact;
        final String contactSQL = "insert into organization (name, do_not_contact, comm_method_code, " + " contact_type_code, email) values (?, ?, ?, ?, ?) RETURNING contact_id";
        try {
            connection = ConnectionUtility.getInstance().getConnection();
            ps = connection.prepareStatement(contactSQL);
            ps.setString(1, orgContact.getName());
            ps.setBoolean(2, orgContact.isDoNotContact());
            ps.setShort(3, orgContact.getCommunicationMethodCode());
            ps.setShort(4, orgContact.getContactTypeCode());
            ps.setString(5, orgContact.getEmail());
            ps.execute();
            int contactId = 0;
            if (ps.getResultSet().next()) {
                contactId = ps.getResultSet().getInt("contact_id");
            }
            contact.setContactId(contactId);
            createPhones(contact.getPhones(), contactId, connection);
            createAddresses(contact.getAddresses(), contactId, connection);
            connection.commit();
        } catch (Exception e) {
            logger.error(CONTACTDAOERROR + e);
            throw e;
        } finally {
            ConnectionUtility.closeJDBCObjects(connection, ps);
        }
        return contact;
    }
}

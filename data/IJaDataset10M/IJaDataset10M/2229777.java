package de.ios.kontor.sv.address.impl;

import java.rmi.*;
import java.sql.*;
import de.ios.framework.basic.*;
import de.ios.framework.db2.*;
import de.ios.kontor.utils.*;
import de.ios.kontor.sv.address.co.*;
import de.ios.kontor.sv.basic.co.*;
import de.ios.kontor.sv.basic.impl.*;

/**
 * ContactPersonControllerImpl deals with a set of ContactPersons
 * within the kontor framework.
 * 
 * @author js (Joachim Schaaf)
 * @version $Id: ContactPersonControllerImpl.java,v 1.1.1.1 2004/03/24 23:02:44 nanneb Exp $
 */
public class ContactPersonControllerImpl extends BasicControllerImpl implements ContactPersonController {

    /**
   * Constructor
   *
   * @param _db - Connection to the database
   * @param _f - a factory to create ContactPerson-objects
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   */
    public ContactPersonControllerImpl(DBObjectServer _db, ContactPersonFactoryImpl _f) throws java.rmi.RemoteException, FactoryException {
        super(_db, _f);
    }

    /**
   * Return the Order clause for getObjects(comp).
   * @param _c The DBObject
   * @return The Orderclause
   */
    public DBOrderClause getObjectsOrderClause(BasicDBO _c) {
        ContactPersonDBO c = (ContactPersonDBO) _c;
        DBOrderClause oc = new DBOrderClause();
        oc.add(c, c.department);
        oc.add(c, c.position);
        return oc;
    }

    /**
   * Create a new ContactPerson
   * @return A new ContactPerson
   * @exception de.ios.kontor.utils.KontorException if the creation of ContactPerson failed.
   */
    public ContactPerson createContactPerson() throws KontorException {
        return (ContactPerson) createImpl();
    }

    /**
   * Create new ContactPerson with full description.
   *
   * @param bpOId      Object-Id of the Businesspartner to create ContactPerson for.
   * @param person     Person to contact.
   * @param position   Position of thi person relativ to his BusinessPartner.
   * @param department Deparment of person.
   *
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   * @exception de.ios.kontor.utils.KontorException if the loading of ContactPersons failed.
   */
    public ContactPerson createContactPerson(long bpOId, Person person, String position, String department) throws KontorException {
        try {
            BusinessPartner pbp = (BusinessPartner) person;
            BusinessPartnerDBO pd = (BusinessPartnerDBO) pbp.getDBObject();
            ContactPersonDBO cpo = new ContactPersonDBO(bpOId, position, department, pd);
            return (ContactPerson) factory.create(cpo);
        } catch (Throwable e) {
            throw new KontorException("Creation of ContactPerson failed.", e);
        }
    }

    /**
   * Get the ContactPerson with the matching Object-ID
   * @exception de.ios.kontor.utils.KontorException if the loading of ContactPerson failed.
   */
    public ContactPerson getContactPersonByOId(long oid) throws KontorException {
        return (ContactPerson) getImplByOId(oid);
    }

    /**
   * Find ContactPersons by some of its Attributes
   *
   * @param cp ContactPerson DBObject
   * @return An iterator with ContactPersons
   * @exception de.ios.kontor.utils.KontorException if the loading of ContactPersons failed.
   */
    public Iterator getContactPersons(ContactPerson cp) throws KontorException, RemoteException {
        return getObjectsDirect(cp);
    }

    /**
   * Get all ContactPersons of one BusinessPartner
   * 
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   * @exception de.ios.kontor.utils.KontorException if the creation of ContactPerson failed.
   */
    public Iterator getContactPersons(BusinessPartner bp) throws KontorException {
        try {
            ContactPersonDBO cp = (ContactPersonDBO) createDBO();
            DBWhereClause wc = new DBWhereClause(cp.businessPOId).isEqual(bp.getObjectId());
            DBOrderClause oc = new DBOrderClause();
            oc.addAsc(cp, cp.department);
            oc.addAsc(cp, cp.position);
            return db.fetchObjectDirect(-1, cp, factory, wc, oc);
        } catch (Throwable t) {
            throw new KontorException("Loading ContactPersons by BusinessPartner failed!", t);
        }
    }

    /**
   * Find Contact Persons by oid of the business partner using Data Carrier/direct SQL
   *
   * @param the Object-Id of the business partner
   * @exception de.ios.kontor.utils.KontorException if the loading of Contact Persons failed.
   */
    public Iterator getContactPersonDC(long oid) throws KontorException, RemoteException {
        try {
            getRights(ContactPerson.LOAD_RIGHT);
            String kind = getSessionImpl().getAddressControllerImpl().getDefaultAddressName();
            Long koid = getSessionImpl().getAddressControllerImpl().getOIdOfAddressKind(kind);
            String ksqlStr;
            if (koid != null) ksqlStr = " k.objectid = " + koid.longValue() + " "; else ksqlStr = " k.objectid = (select min(objectid) from kindofaddress) ";
            String sqlString = "select " + " cp.objectid as objectid, " + " cp.f_position as position, " + " cp.department as department, " + " p.title as title, " + " p.firstname as firstname, " + " p.name as name, " + " adr.telephone as telephone, " + " adr.telefax as fax, " + " adr.mobile as mobile, " + " adr.email as email, " + " adr.www as www, " + " bp.f_remark as f_remark " + "from " + " businessp bp1, contactpersons cp, businessp bp, " + " address adr, person p, kindofaddress k " + "where " + " bp1.objectid = " + oid + " and " + " bp1.objectid = cp.businessPOId and " + " cp.person = bp.objectid and " + " bp.person = p.objectid and " + " bp.objectid = adr.addressID(+) and " + " adr.kindOId = k.objectid(+) and " + ksqlStr + " order by cp.department, name";
            return db.queryObjectsDirect(-1, new ContactPersonDC().getClass(), null, sqlString);
        } catch (Throwable t) {
            throw new KontorException("getContactPersonDC failed!", t);
        }
    }

    protected String fromWhere = "from " + " businessp bp1, contactpersons cp, businessp bp, " + " address adr, person p, kindofaddress k, " + " customer co, company c, person p2, address adr2 " + "where " + " bp1.objectid = co.businessPOId and " + " bp1.objectid = adr2.addressID(+) and " + " adr2.kindOId = k.objectid(+) and " + " bp1.company = c.objectid(+) and " + " bp1.person = p2.objectid(+) and " + " bp1.objectid = cp.businessPOId and " + " cp.person = bp.objectid and " + " bp.person = p.objectid and " + " bp.objectid = adr.addressID(+) and " + " adr.kindOId = k.objectid(+) and ";

    protected String getDefaultAddressSQL() throws java.rmi.RemoteException, KontorException {
        String kind = getSessionImpl().getAddressControllerImpl().getDefaultAddressName();
        Long koid = getSessionImpl().getAddressControllerImpl().getOIdOfAddressKind(kind);
        String ksqlStr;
        if (koid != null) return " k.objectid = " + koid.longValue() + " "; else return " k.objectid = (select min(objectid) from kindofaddress) ";
    }

    /**
   * Get the number of all Contactpersons including the bp they belong to.
   * @return Number of alle DC's
   * @exception de.ios.kontor.utils.KontorException if the loading of Contact Persons failed.
   */
    public long getNumberOfAllContactPersonExtendedDC() throws KontorException {
        ResultSet rs = null;
        int tId = -1;
        try {
            tId = db.beginTransaction();
            Statement stmt = db.getConnection(tId).createStatement();
            rs = stmt.executeQuery("select count(*) " + fromWhere + getDefaultAddressSQL());
            if (rs.next()) return rs.getLong(1);
        } catch (Throwable t) {
            throw new KontorException("getContactPersonExtendedDC failed: " + t.getMessage(), t);
        } finally {
            try {
                rs.close();
            } catch (Throwable e) {
            }
            ;
            try {
                db.abortTransaction(tId);
            } catch (Throwable e) {
            }
            ;
        }
        return -1;
    }

    /**
   * Get all Contactpersons including the bp they belong to.
   * @return Iterator filled with ContactPersonExtendedDC
   * @exception de.ios.kontor.utils.KontorException if the loading of Contact Persons failed.
   */
    public Iterator getAllContactPersonExtendedDC() throws KontorException, RemoteException {
        try {
            getRights(ContactPerson.LOAD_RIGHT);
            String sqlString = "select " + " cp.objectid as objectid, " + " cp.f_position as position, " + " cp.department as department, " + " p.title as title, " + " p.firstname as firstname, " + " p.name as name, " + " adr.telephone as telephone, " + " adr.telefax as fax, " + " adr.mobile as mobile, " + " adr.email as email, " + " adr.www as www," + " bp.f_remark as f_remark, " + " co.f_number as bpNumber," + " c.name1 as bpName1," + " c.name2 as bpName2," + " c.shortname as bpShortname," + " adr2.street as bpStreet," + " adr2.zip as bpZip," + " adr2.city as bpCity," + " adr2.telephone as bpTelephone," + " adr2.telefax as bpTelefax," + " adr2.mobile as bpMobile," + " adr2.email as bpEmail," + " k.kind as bpKind " + fromWhere + getDefaultAddressSQL() + " order by c.name1, p2.name, p.name ";
            return new DBResultSetIterator(db.queryObjects(-1, new ContactPersonExtendedDC().getClass(), null, sqlString, 10));
        } catch (Throwable t) {
            throw new KontorException("getContactPersonExtendedDC failed: " + t.getMessage(), t);
        }
    }
}

;

package org.primordion.user.app.database.school.building.common;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primordion.user.app.database.school.XhSchool;
import org.primordion.user.app.database.HibernateUtil;
import org.primordion.xholon.base.IMessage;
import org.primordion.xholon.base.IXholon;
import org.primordion.xholon.base.Message;
import org.primordion.xholon.base.XholonList;

/**
 * <p>Data Access Object (DAO) - Version 02</p>
 * <p>In this version of the DAO class:</p>
 * <ol>
 * <li>The session is always obtained by calling getCurrentSession() .</li>
 * <li>The session is never closed, but instead spans all transactions.</li>
 * <li>Exceptions are caught.</li>
 * <li>Print statistics.</li>
 * <li>Use merge() or saveOrUpdate() . merge() causes an extra initial select.</li>
 * </ol>
 * @author <a href="mailto:ken@primordion.com">Ken Webb</a>
 * @see <a href="http://www.primordion.com/Xholon">Xholon Project website</a>
 * @since 0.8 (Created on July 11, 2009)
*/
public class BuildingDao_02 extends XhSchool {

    /**
	 * The name of an externalized HQL query.
	 */
    protected static final String HQL_QUERYNAME = "findAllBuildingsOrderedBy";

    /**
	 * The name of an externalized HQL query.
	 */
    protected static final String HQL_QUERYNAME_PICKER = "findAllBuildingsPickerOrderedBy";

    /**
	 * The name of an entity used in the Hibernate XML mapping file.
	 */
    protected static final String HQL_ENTITYNAME = "BuildingAll";

    /**
	 * The name of a Xholon class.
	 */
    protected static final String XHOLON_CLASSNAME = "Building";

    public IMessage processReceivedSyncMessage(IMessage msg) {
        switch(msg.getSignal()) {
            case SIG_SAVE:
                saveToDatabase((IXholon) msg.getData());
                return new Message(SIG_SAVED, null, msg.getReceiver(), msg.getSender());
            case SIG_DELETE:
                deleteFromDatabase((IXholon) msg.getData());
                return new Message(SIG_DELETED, null, msg.getReceiver(), msg.getSender());
            case SIG_RETRIEVE_ALL:
                {
                    List records = retrieveAllFromDatabase();
                    return new Message(SIG_RETRIEVED, records, msg.getReceiver(), msg.getSender());
                }
            case SIG_RETRIEVE_ALL_PICKER:
                {
                    List records = retrieveAllPickerFromDatabase();
                    return new Message(SIG_RETRIEVED_PICKER, records, msg.getReceiver(), msg.getSender());
                }
            default:
                return new Message(msg.getSignal(), msg.getData(), msg.getReceiver(), msg.getSender());
        }
    }

    /**
	 * Save a new or updated record to the database.
	 * @param record The new or updated record.
	 */
    protected void saveToDatabase(IXholon record) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tx = session.beginTransaction();
            session.saveOrUpdate(HQL_ENTITYNAME, record);
            tx.commit();
        } catch (RuntimeException e) {
            try {
                logger.error("Couldn't save to database,", e);
                tx.rollback();
            } catch (RuntimeException erb) {
                logger.error("Couldn't rollback transaction,", erb);
            }
            throw e;
        }
    }

    /**
	 * Delete a record from the database.
	 * @param record The record to be deleted.
	 */
    protected void deleteFromDatabase(IXholon record) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        session.delete(HQL_ENTITYNAME, record);
        tx.commit();
    }

    /**
	 * Retrieve all records from the database.
	 * @return A List of records.
	 */
    protected List retrieveAllFromDatabase() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        List xhList = makeXholonList(session.getNamedQuery(HQL_QUERYNAME).list());
        System.out.println("Session entity count:" + session.getStatistics().getEntityCount());
        tx.commit();
        return xhList;
    }

    /**
	 * Retrieve all records from the database.
	 * @return A List of records.
	 */
    protected List retrieveAllPickerFromDatabase() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        List xhList = makeXholonList(session.getNamedQuery(HQL_QUERYNAME_PICKER).list());
        System.out.println("Session entity count:" + session.getStatistics().getEntityCount());
        tx.commit();
        return xhList;
    }

    /**
	 * Make and initialize a XholonList object,
	 * and populate it with the xholons in the hbmList.
	 * @param hbmList A list of records created by a Hibernate query.
	 * @return A new populated XholonList.
	 */
    protected List makeXholonList(List hbmList) {
        XholonList xhList = new XholonList();
        xhList.setId(getNextId());
        xhList.setXhc(getClassNode("XholonList"));
        for (int i = 0; i < hbmList.size(); i++) {
            IXholon node = (IXholon) hbmList.get(i);
            node.setId(getNextId());
            node.setXhc(getClassNode(XHOLON_CLASSNAME));
            xhList.add(node);
        }
        return xhList;
    }
}

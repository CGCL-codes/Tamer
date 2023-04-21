package net.sourceforge.jcoupling.bus.server.mql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sourceforge.jcoupling.bus.dao.LockException;
import net.sourceforge.jcoupling.bus.server.CommunicatorID;
import net.sourceforge.jcoupling.bus.server.NotFoundException;
import net.sourceforge.jcoupling.bus.server.callout.RequestDao;
import net.sourceforge.jcoupling.factory.database.HibernateUtil;
import net.sourceforge.jcoupling.peer.Message;
import net.sourceforge.jcoupling.peer.Request;
import net.sourceforge.jcoupling.peer.interaction.MessageID;
import net.sourceforge.jcoupling.peer.mql.Property;
import net.sourceforge.jcoupling.peer.property.RequestKey;
import net.sourceforge.jcoupling.peer.property.TimestampProperty;
import net.sourceforge.jcoupling.wca.WCAChannel;
import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 * Creates tables for each destination and then when a message arrives it evaluates any properties defined over that
 * destination and store the results.
 * 
 * @author Lachlan Aldred
 */
public class MessagePropertyLogger {

    private static Property timestampProp = new TimestampProperty();

    private Map<WCAChannel, Map<String, Property>> _propertiesMap;

    private Logger _logger = Logger.getLogger(MessagePropertyLogger.class);

    private RequestDao _requestDao;

    public static final String PROP_PREFIX = "jdprop_";

    static final MessageFormat createPropertyTable = new MessageFormat("CREATE TABLE {0} " + "( " + "messageid varchar(80) NOT NULL, " + "\"timestamp\" timestamp, " + "locked bool, " + "locker varchar(80), " + "CONSTRAINT pkey{0} PRIMARY KEY (messageid) " + ")");

    static final MessageFormat insertRow = new MessageFormat("INSERT INTO {0} " + "VALUES ( {1} );");

    static final MessageFormat alterTable = new MessageFormat("ALTER TABLE {0} " + "ADD {1} " + "{2}");

    static final String check4Tables = "SELECT tablename FROM pg_tables " + "WHERE schemaname = 'public' " + "AND tablename LIKE '" + MessagePropertyLogger.PROP_PREFIX + "%'";

    static final MessageFormat check4Locking = new MessageFormat("SELECT locked " + "FROM {0}  " + "WHERE messageid = {1}");

    static final MessageFormat lockMessage = new MessageFormat("UPDATE {0} " + "SET locked={1}, locker={2} " + "WHERE messageid='{3}'");

    /**
	 * Constructor.
	 */
    public MessagePropertyLogger(RequestDao requestDao) {
        _requestDao = requestDao;
        _propertiesMap = new LinkedHashMap<WCAChannel, Map<String, Property>>();
    }

    /**
	 * Adds properties to the table.
	 * 
	 * @param destination
	 *          PRE: not null.
	 * @param properties
	 *          PRE: not null and no properties already exist in table<destination> with same name.
	 * @throws SQLException
	 *           if DB access problem.
	 */
    public void addPropertyColumns(WCAChannel destination, Set<Property> properties) throws SQLException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Connection con = session.connection();
        try {
            createPropertyTable(destination);
            extendPropertyList(destination, properties);
            Statement statement = con.createStatement();
            for (Property property : properties) {
                String propertyName = removeBadChars(property.getName());
                statement.executeUpdate(alterTable.format(new Object[] { getTableName(destination), propertyName, property.getDBColumnType() }));
            }
            con.commit();
            con.close();
            session.close();
        } catch (SQLException e) {
            con.rollback();
            session.close();
            throw e;
        }
    }

    public static String getTableName(WCAChannel destination) {
        String destinationName = removeBadChars(destination.getName());
        return PROP_PREFIX + destinationName;
    }

    /**
	 * Creates a properties table and inserts the timestamp property into it.
	 * 
	 * @param destination
	 * @throws SQLException
	 */
    public void createPropertyTable(WCAChannel destination) throws SQLException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Connection con = session.connection();
        Statement statement = con.createStatement();
        ResultSet res = statement.executeQuery(check4Tables);
        boolean tableExists = false;
        while (res.next()) {
            if (res.getString(1).equalsIgnoreCase(getTableName(destination))) {
                tableExists = true;
            }
        }
        if (!tableExists) {
            statement.executeUpdate(createPropertyTable.format(new Object[] { getTableName(destination) }));
            con.commit();
        }
        session.close();
        if (!_propertiesMap.containsKey(destination)) {
            Map<String, Property> propsMap = new LinkedHashMap<String, Property>();
            _propertiesMap.put(destination, propsMap);
            propsMap.put(timestampProp.getName(), timestampProp);
        }
    }

    /**
	 * Processes the message and populates the destination properties table for querying. Finishing immediately.
	 * 
	 * @param message
	 *          the message
	 * @param destination
	 *          the destination.
	 * @throws net.sourceforge.jcoupling.bus.server.NotFoundException
	 *           if the destination not found in system.
	 */
    public void sendRequest(Message message, WCAChannel destination) throws NotFoundException, SQLException {
        createPropertyTable(destination);
        System.out.println("message = " + message.getTimestamp());
        Map<String, Property> properties = _propertiesMap.get(destination);
        StringBuffer propertiesTuple = new StringBuffer();
        propertiesTuple.append("'").append(message.getID().toString()).append("'");
        for (Property property : properties.values()) {
            Object value = "";
            try {
                value = "REFACTORED AWAY";
            } catch (Exception e) {
                _logger.error(e);
                e.printStackTrace();
            }
            propertiesTuple.append(",'").append(value).append("'");
        }
        Session session = HibernateUtil.getSessionFactory().openSession();
        Connection con = session.connection();
        Statement statement = con.createStatement();
        System.out.print("Tablename = " + getTableName(destination) + " : ");
        System.out.println("propertiesTuple = " + propertiesTuple);
        statement.executeUpdate(insertRow.format(new Object[] { getTableName(destination), propertiesTuple }));
        con.commit();
        session.close();
    }

    private static String removeBadChars(String input) {
        return input.replaceAll("[^\\d_a-zA-Z]", "");
    }

    private void extendPropertyList(WCAChannel destination, Set<Property> properties) {
        Map<String, Property> propList = _propertiesMap.get(destination);
        for (Property property : properties) {
            propList.put(property.getName(), property);
        }
    }

    public void removePropertyRows(RequestKey requestKey, List<MessageID> msgList) throws SQLException {
        Request recReq = _requestDao.lookupReceive(requestKey);
        List<WCAChannel> dests = recReq.getDestinations();
        for (WCAChannel dest : dests) {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Connection con = session.connection();
            String query = "DELETE FROM " + getTableName(dest) + " " + "WHERE  messageid IN " + formatMessageIDs(msgList) + ");";
            Statement statement = con.createStatement();
            statement.executeUpdate(query);
            con.commit();
            session.close();
        }
    }

    private String formatMessageIDs(List<MessageID> msgList) {
        StringBuffer buf = new StringBuffer();
        buf.append('(');
        for (int i = 0; i < msgList.size(); i++) {
            MessageID mid = msgList.get(i);
            buf.append('\'').append(mid).append('\'');
            if (i + 1 < msgList.size()) {
                buf.append(',');
            }
        }
        buf.append(')');
        return buf.toString();
    }

    public static void main(String[] args) throws SQLException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Connection con = session.connection();
        Statement statement = con.createStatement();
        ResultSet res = statement.executeQuery("SELECT messageid " + "FROM " + "jdprop_helloworld " + "WHERE 3 < (select count(*) from jdprop_helloworld) ");
        while (res.next()) {
            System.out.println(res.getString(1));
        }
        session.close();
    }

    /**
	 * Locks an unlocked message, or unlocks a locked message.
	 * 
	 * @param messageID
	 * @param channels
	 * @param commID
	 * @param lockit
	 * @throws NotFoundException
	 */
    public static void lock(MessageID messageID, List<WCAChannel> channels, CommunicatorID commID, boolean lockit) throws NotFoundException, LockException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Connection con = session.connection();
        try {
            for (WCAChannel channel : channels) {
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery(check4Locking.format(new String[] { getTableName(channel), messageID.toString() }));
                if (rs.next()) {
                    if (rs.getBoolean(1) != lockit) {
                        statement.executeUpdate(lockMessage.format(new String[] { getTableName(channel), "" + lockit, commID.toString(), messageID.toString() }));
                        con.commit();
                        return;
                    } else {
                        throw new LockException("Message (" + messageID + ") already " + (lockit ? "" : "un") + "locked.");
                    }
                }
            }
            throw new NotFoundException("Message " + messageID + " not found.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }
}

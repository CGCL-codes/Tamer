package org.exolab.jms.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import org.exolab.jms.client.JmsDestination;
import org.exolab.jms.client.JmsQueue;
import org.exolab.jms.client.JmsTopic;

/**
 * This class provides persistency for JmsDestination objects in an RDBMS
 * database.
 *
 * @author <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 * @version $Revision: 1.5 $ $Date: 2007/04/16 02:50:21 $
 */
class Destinations {

    /**
     * The seed generator.
     */
    private final SeedGenerator _seeds;

    /**
     * The consumer manager.
     */
    private final Consumers _consumers;

    /**
     * Cache of all destinations indexed on names.
     */
    private final HashMap _destinations = new HashMap();

    /**
     * Cache of all destinations indexed on identity.
     */
    private final HashMap _ids = new HashMap();

    /**
     * This is the name of the destination id generator, which uniquely created
     * identities for destinations.
     */
    private static final String DESTINATION_ID_SEED = "destinationId";

    /**
     * Create a new <code>Destinations</code>.
     *
     * @param seeds the seed manager
     * @param consumers the consumer manager
     * @param connection the connection to initialise from
     * @throws PersistenceException if initialisation fails
     */
    public Destinations(SeedGenerator seeds, Consumers consumers, Connection connection) throws PersistenceException {
        _seeds = seeds;
        _consumers = consumers;
        load(connection);
    }

    /**
     * Add a new destination to the database. This method will also assign it a
     * unique identity.
     *
     * @param connection  the connection to use.
     * @param destination the destination to add
     * @throws PersistenceException if the destination cannot be added
     */
    public synchronized void add(Connection connection, JmsDestination destination) throws PersistenceException {
        PreparedStatement insert = null;
        try {
            long Id = _seeds.next(connection, DESTINATION_ID_SEED);
            boolean isQueue = (destination instanceof JmsQueue);
            insert = connection.prepareStatement("insert into destinations (name, isQueue, destinationId) " + "values (?, ?, ?)");
            insert.setString(1, destination.getName());
            insert.setBoolean(2, isQueue);
            insert.setLong(3, Id);
            insert.executeUpdate();
            cache(destination, Id);
        } catch (Exception error) {
            throw new PersistenceException("Destinations.add failed with " + error.toString());
        } finally {
            SQLHelper.close(insert);
        }
    }

    /**
     * Remove a destination from the database. This removes all associated
     * consumers, and messages.
     *
     * @param connection  the connection to use
     * @param destination the destination
     * @return <code>true</cpde> if it was removed
     * @throws PersistenceException if the request fails
     */
    public synchronized boolean remove(Connection connection, JmsDestination destination) throws PersistenceException {
        boolean success = false;
        PreparedStatement deleteDestinations = null;
        PreparedStatement deleteMessages = null;
        PreparedStatement deleteConsumers = null;
        PreparedStatement deleteMessageHandles = null;
        Pair pair = (Pair) _destinations.get(destination.getName());
        if (pair != null) {
            try {
                deleteDestinations = connection.prepareStatement("delete from destinations where name=?");
                deleteDestinations.setString(1, destination.getName());
                deleteMessages = connection.prepareStatement("delete from messages where destinationId=?");
                deleteMessages.setLong(1, pair.Id);
                deleteMessageHandles = connection.prepareStatement("delete from message_handles where destinationId=?");
                deleteMessageHandles.setLong(1, pair.Id);
                deleteConsumers = connection.prepareStatement("delete from consumers where destinationId=?");
                deleteConsumers.setLong(1, pair.Id);
                deleteDestinations.executeUpdate();
                deleteMessages.executeUpdate();
                deleteMessageHandles.executeUpdate();
                deleteConsumers.executeUpdate();
                _consumers.removeCached(pair.Id);
                _destinations.remove(destination.getName());
                _ids.remove(new Long(pair.Id));
                success = true;
            } catch (Exception error) {
                throw new PersistenceException("Failed to remove destination", error);
            } finally {
                SQLHelper.close(deleteDestinations);
                SQLHelper.close(deleteMessages);
                SQLHelper.close(deleteConsumers);
                SQLHelper.close(deleteMessageHandles);
            }
        }
        return success;
    }

    /**
     * Returns the destination associated with name.
     *
     * @param name the name of the destination
     * @return the destination, or null
     */
    public synchronized JmsDestination get(String name) {
        Pair pair = (Pair) _destinations.get(name);
        return (pair != null) ? pair.destination : null;
    }

    /**
     * Returns the destination associated with Id.
     *
     * @param id the destination Id
     * @return the destination or null
     */
    public synchronized JmsDestination get(long id) {
        Pair pair = (Pair) _ids.get(new Long(id));
        return (pair != null) ? pair.destination : null;
    }

    /**
     * Returns the Id for a given destination name
     *
     * @param name the destination name
     * @return the destination Id, or 0 if it doesn't exist
     */
    public synchronized long getId(String name) {
        Pair pair = (Pair) _destinations.get(name);
        return (pair != null) ? pair.Id : 0;
    }

    /**
     * Returns the list of destination names.
     *
     * @return list of destination names
     */
    public synchronized Vector getNames() {
        Vector result = new Vector(_destinations.size());
        Iterator iter = _destinations.keySet().iterator();
        while (iter.hasNext()) {
            result.add((String) iter.next());
        }
        return result;
    }

    /**
     * Returns the list of destination objects.
     *
     * @return list of destination objects
     */
    public synchronized Vector getDestinations() {
        Vector result = new Vector(_destinations.size());
        Iterator iter = _destinations.values().iterator();
        while (iter.hasNext()) {
            result.add(((Pair) iter.next()).destination);
        }
        return result;
    }

    /**
     * Deallocates resources owned or referenced by the instance.
     */
    public synchronized void close() {
        _destinations.clear();
        _ids.clear();
    }

    /**
     * Load all the destinations in memory. It uses the transaction service and
     * the database service to access the appropriate resources.
     *
     * @param connection the connection to use
     * @throws PersistenceException if the
     */
    private void load(Connection connection) throws PersistenceException {
        PreparedStatement select = null;
        ResultSet set = null;
        try {
            select = connection.prepareStatement("select name, isQueue, destinationId from destinations");
            set = select.executeQuery();
            String name = null;
            boolean isQueue = false;
            JmsDestination destination = null;
            long Id = 0;
            while (set.next()) {
                name = set.getString(1);
                isQueue = set.getBoolean(2);
                destination = (isQueue) ? (JmsDestination) new JmsQueue(name) : (JmsDestination) new JmsTopic(name);
                Id = set.getLong(3);
                destination.setPersistent(true);
                cache(destination, Id);
            }
        } catch (Exception exception) {
            throw new PersistenceException("Failed to load destinations", exception);
        } finally {
            SQLHelper.close(set);
            SQLHelper.close(select);
        }
    }

    /**
     * Cache a destination.
     *
     * @param destination the destination to cache
     * @param Id          the destination identity
     */
    private void cache(JmsDestination destination, long Id) {
        Pair pair = new Pair(destination, Id);
        _destinations.put(destination.getName(), pair);
        _ids.put(new Long(Id), pair);
    }

    /**
     * This private static class holds the name and identity of the destination
     */
    private static class Pair {

        public Pair(JmsDestination destination, long Id) {
            this.destination = destination;
            this.Id = Id;
        }

        public JmsDestination destination;

        public long Id;
    }
}

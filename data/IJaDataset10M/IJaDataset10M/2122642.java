package onepoint.persistence;

import java.sql.Blob;
import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import onepoint.log.XLog;
import onepoint.log.XLogFactory;
import onepoint.persistence.hibernate.OpScrollableResults;
import onepoint.project.modules.site_management.OpSite;

public class OpBroker {

    private static final XLog logger = XLogFactory.getLogger(OpBroker.class);

    private static final int BULK_FETCH_COUNT = 250;

    private OpConnection defaultConnection;

    private final OpSource source;

    private static Stack<String> brokerStack = null;

    /**
    * Thread local OpBroker.
    */
    static ThreadLocal brokers = new ThreadLocal();

    /**
    * Creates a new broker.
    *
    * @param sourceName source name to use.
    */
    OpBroker(OpSite site) {
        this(OpSourceManager.getSource(site));
    }

    public OpBroker(OpSource source) {
        this.source = source;
        if (source != null) {
            defaultConnection = source.newConnection(this);
        }
        if (logger.isLoggable(XLog.DEBUG)) {
            synchronized (logger) {
                if (brokerStack == null) {
                    brokerStack = new Stack<String>();
                }
                brokerStack.add(new Throwable().getStackTrace()[3].toString());
                if (brokerStack.size() > 1) {
                    logger.debug("duplicate brokers at: ");
                    for (String st : brokerStack) {
                        logger.debug(st);
                    }
                }
            }
        }
        setBroker(this);
    }

    public static void debugBrokerStack() {
        logger.debug("Broker Stack: " + OpBroker.brokerStackToString());
    }

    public static String brokerStackToString() {
        StringBuffer buffer = new StringBuffer();
        synchronized (logger) {
            if (brokerStack == null) {
                return null;
            }
            for (String stackline : brokerStack) {
                buffer.append(stackline);
                buffer.append('\n');
            }
        }
        return buffer.toString();
    }

    public OpConnection getConnection() {
        return defaultConnection;
    }

    public void synchronizeSet(Set<? extends OpObjectIfc> set) {
        defaultConnection.synchronizeCollection(set);
    }

    public void synchronizeSet(Set<? extends OpObjectIfc> set, boolean remove) {
        defaultConnection.synchronizeCollection(set, remove);
    }

    public void makePersistent(OpObjectIfc opObject) {
        defaultConnection.persistObject(opObject);
        logger.debug("OpBroker.makePersistent(): id = " + opObject.getId());
    }

    public <C extends OpObjectIfc> C getObject(String s) {
        OpLocator locator = OpLocator.parseLocator(s);
        return (C) (locator != null ? getObject(locator) : null);
    }

    /**
    * @param locator
    * @return
    * @pre
    * @post
    */
    public OpObjectIfc getObject(OpLocator locator) {
        return getObject(locator.getPrototype().getInstanceClass(), locator.getID());
    }

    public <C extends OpObjectIfc> C getObject(Class<C> c, long id) {
        logger.debug("getObject(): id = " + id);
        return defaultConnection.getObject(c, id);
    }

    public void updateObject(OpObjectIfc object) {
        logger.debug("OpBroker.updateObject()");
        defaultConnection.updateObject(object);
        logger.debug("/OpBroker.updateObject()");
    }

    public void refreshObject(OpObjectIfc object) {
        logger.debug("OpBroker.refreshObject()");
        defaultConnection.refreshObject(object);
        logger.debug("/OpBroker.refreshObject()");
    }

    public void deleteObject(OpObjectIfc object) {
        logger.debug("OpBroker.deleteObject()");
        defaultConnection.deleteObject(object);
        logger.debug("/OpBroker.deleteObject()");
    }

    public List list(OpQuery query) {
        if (defaultConnection != null) {
            return defaultConnection.list(query);
        } else {
            return null;
        }
    }

    /**
    * @param query
    * @param name
    * @return
    * @pre
    * @post
    */
    @SuppressWarnings("unchecked")
    public <C> C uniqueResult(OpQuery query, Class<C> type) {
        if (defaultConnection != null) {
            return (C) defaultConnection.uniqueResult(query);
        } else {
            return null;
        }
    }

    /**
    * @param query
    * @param name
    * @return
    * @pre
    * @post
    */
    @SuppressWarnings("unchecked")
    public <C> List<C> list(OpQuery query, Class<C> type) {
        if (defaultConnection != null) {
            return defaultConnection.list(query);
        } else {
            return null;
        }
    }

    public <C> List<C> listViaIterate(OpQuery query, Class<C> type) {
        List<C> ret = new LinkedList<C>();
        Iterator it = iterate(query);
        while (it.hasNext()) {
            ret.add((C) it.next());
        }
        return ret;
    }

    public Iterator iterate(OpQuery query) {
        return forceIterate(query);
    }

    public Iterator forceIterate(OpQuery query) {
        if (defaultConnection != null) {
            return defaultConnection.iterate(query);
        } else {
            return null;
        }
    }

    public OpScrollableResults scroll(OpQuery query) {
        if (defaultConnection != null) {
            return defaultConnection.scroll(query);
        } else {
            return null;
        }
    }

    public int execute(OpQuery query) {
        if (defaultConnection != null) {
            return defaultConnection.execute(query);
        } else {
            return 0;
        }
    }

    public Blob newBlob(byte[] bytes) {
        return defaultConnection.newBlob(bytes);
    }

    public OpQuery newQuery(String s) {
        return defaultConnection.newQuery(s);
    }

    public void close() {
        removeBroker();
        if (logger.isLoggable(XLog.DEBUG) && (brokerStack != null)) {
            if (brokerStack.size() < 1) {
                logger.debug("closing non opened broker: " + new Throwable().getStackTrace()[3].toString());
            } else {
                synchronized (logger) {
                    brokerStack.pop();
                }
            }
        }
        if (defaultConnection != null) {
            defaultConnection.close();
            defaultConnection = null;
        }
    }

    /**
    * Closes this broker and evicts the cache.
    */
    public void closeAndEvict() {
        clear();
        close();
    }

    /**
    * Evicts the cache for this broker.
    */
    public void clear() {
        if (defaultConnection != null) {
            defaultConnection.clear();
            source.clear();
        }
    }

    /**
    * Checks if this broker (and the underlying connection) is still open.
    *
    * @return true if it's open.
    */
    public boolean isOpen() {
        return defaultConnection != null && defaultConnection.isOpen();
    }

    public boolean isValid() {
        if (defaultConnection == null) {
            return (false);
        }
        return (defaultConnection.isValid());
    }

    public OpTransaction newTransaction() {
        return defaultConnection.newTransaction();
    }

    public Connection getJDBCConnection() {
        return defaultConnection.getJDBCConnection();
    }

    /**
    * @return
    * @pre
    * @post
    */
    public OpSource getSource() {
        return source;
    }

    /**
    * Sets the whole system to read only or read/write mode according the given parameter
    * @param readOnly if true sets read only mode else sets read write mode.
    */
    public void setReadOnlyMode(boolean readOnly) {
        source.setReadOnlyMode(readOnly);
    }

    /**
    * Gets the read only mode.
    * @return true if the system is within read only mode, false otherwise.
    */
    public boolean isReadOnlyMode() {
        return source.isReadOnlyMode();
    }

    /**
    * Used for testing if a given object is of a given type.
    * Throws an <code>UnsupportedOperationException</code> if the broker on witch we call the method is closed.
    *
    * @param id represents the object id
    * @param objectType represents the type to witch we want to compare the object type
    * @return returns <code>true</code> if the object with the specified id is of type <code>objectType</code> otherwise
    * it returns <code>false</code>
    */
    public boolean isOfType(Long id, String objectType) {
        if (!this.isValid()) {
            throw new UnsupportedOperationException();
        }
        StringBuffer buffer = new StringBuffer("select obj.id from ");
        buffer.append(objectType);
        buffer.append(" obj where obj.id = :objID");
        OpQuery query = this.newQuery(buffer.toString());
        query.setLong("objID", id);
        Iterator it = this.iterate(query);
        if (it != null) {
            return it.hasNext();
        }
        return false;
    }

    /**
    * Gets the OpBroker held as thread local.
    *
    * @return the thread depending OpProjectSession.
    */
    public static OpBroker getBroker() {
        Object existing = brokers.get();
        if (existing == null) {
            return null;
        }
        if (existing instanceof OpBroker) {
            return (OpBroker) existing;
        }
        return ((Stack<OpBroker>) existing).peek();
    }

    /**
    * Sets the given broker as thread local.
    *
    * @param session the session to set
    */
    public static void setBroker(OpBroker broker) {
        Object existing = brokers.get();
        if (existing == null) {
            brokers.set(broker);
            return;
        }
        if (existing instanceof OpBroker) {
            if (broker == null) {
                brokers.set(null);
                return;
            }
            Stack<OpBroker> stack = new Stack<OpBroker>();
            stack.push((OpBroker) existing);
            existing = stack;
            brokers.set(stack);
        }
        Stack<OpBroker> stack = (Stack<OpBroker>) existing;
        if (broker == null) {
            stack.pop();
            if (stack.size() == 1) {
                brokers.set(stack.pop());
            }
            return;
        }
        stack.push(broker);
    }

    /**
    * Removes the thread local broker
    */
    public static void removeBroker() {
        setBroker(null);
    }

    public <C extends OpLocatable> Set<C> getObjectsByLocatorStrings(Collection<String> locators) {
        if (locators == null) {
            return null;
        }
        Set<OpLocator> locs = new HashSet<OpLocator>();
        for (String l : locators) {
            OpLocator loc = OpLocator.parseLocator(l);
            if (loc == null) {
                continue;
            }
            locs.add(loc);
        }
        return getObjectsByLocators(locs);
    }

    public <C extends OpLocatable> Set<C> getObjectsByLocators(Collection<OpLocator> locs) {
        Map<OpLocator, C> tmp = findObjectsByLocators(locs);
        return new HashSet<C>(tmp.values());
    }

    public <C extends OpLocatable> List<C> getObjectsByLocatorsPreserveOrder(Collection<OpLocator> locs) {
        Map<OpLocator, C> tmp = findObjectsByLocators(locs);
        List<C> result = new LinkedList<C>();
        for (OpLocator l : locs) {
            result.add(l != null ? tmp.get(l) : null);
        }
        return result;
    }

    public <C extends OpLocatable> Map<OpLocator, C> findObjectsByLocators(Collection<OpLocator> locs) {
        Map<Class, Set<Long>> classToIdMap = new HashMap<Class, Set<Long>>();
        for (OpLocator loc : locs) {
            if (loc == null) {
                continue;
            }
            Class instanceClass = loc.getPrototype().getInstanceClass();
            Set<Long> idsForClass = classToIdMap.get(instanceClass);
            if (idsForClass == null) {
                idsForClass = new HashSet<Long>();
                classToIdMap.put(instanceClass, idsForClass);
            }
            idsForClass.add(Long.valueOf(loc.getID()));
        }
        Map<OpLocator, C> result = new HashMap<OpLocator, C>();
        for (Map.Entry<Class, Set<Long>> ce : classToIdMap.entrySet()) {
            Iterator<Long> idit = ce.getValue().iterator();
            Set<Long> idsToFetch = new HashSet<Long>(BULK_FETCH_COUNT);
            while (idit.hasNext()) {
                idsToFetch.add(idit.next());
                if (idsToFetch.size() == BULK_FETCH_COUNT || !idit.hasNext()) {
                    OpQuery objsByIdsQ = newQuery("select o from " + ce.getKey().getCanonicalName() + " o where o.id in (:ids)");
                    objsByIdsQ.setCollection("ids", idsToFetch);
                    OpPrototype pt = OpTypeManager.getPrototypeByClassName(ce.getKey().getName());
                    Iterator<C> qit = iterate(objsByIdsQ);
                    while (qit.hasNext()) {
                        C o = qit.next();
                        result.put(new OpLocator(pt, o.getId()), o);
                    }
                    idsToFetch.clear();
                }
            }
        }
        return result;
    }

    public OpTransaction commitEveryNElements(OpTransaction tx, int count, int commitInterval) {
        if (count % commitInterval == 0) {
            tx.commit();
            tx = newTransaction();
        }
        return tx;
    }

    public <E> void closeIterator(Iterator<E> toClose) {
        if (getConnection() != null && toClose != null) {
            getConnection().closeDataBaseIterator(toClose);
        }
    }
}

package org.equanda.tapestry.navigation;

import org.equanda.persistence.om.EquandaProxy;
import org.equanda.persistence.om.EquandaSelector;
import org.equanda.persistence.om.query.EquandaQueryImpl;
import org.equanda.tapestry.util.EquandaProxyAccessor;
import org.apache.log4j.Logger;
import org.jboss.serial.io.JBossObjectInputStream;
import org.jboss.serial.io.JBossObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Container for NavigationEntry objects, does not really store anything (it is all in the db),
 * but does handle the interaction with the db (using proxies).
 *
 * @author <a href="mailto:andrei@paragon-software.ro">Andrei Chiritescu</a>
 * @author <a href="mailto:joachim@progs.be">Joachim Van der Auwera</a>
 */
public class NavigationEntryContainer {

    private static long GARBAGE_ENTRY_COLLECT_INTERVAL = 3600 * 1000;

    private static long GARBAGE_TIMEOUT_INTERVAL = 24 * 3600 * 1000;

    public static final String NAVIGATION_TABLE = "EquandaNavigation";

    public static final String FIELD_NAVIGATIONENTRY = "NavigationEntry";

    private static final Logger log = Logger.getLogger(NavigationEntryContainer.class);

    protected EntryGarbageCollector garbageCollector;

    public NavigationEntryContainer() {
        if (garbageCollector == null) {
            garbageCollector = new EntryGarbageCollector();
            garbageCollector.setDaemon(true);
        }
        if (!garbageCollector.isAlive()) garbageCollector.start();
    }

    /**
     * @param id NavigationEntry id
     * @return the requested NavigationEntry of null if not found
     */
    public NavigationEntry getEntry(String id) {
        EquandaProxy proxy = EquandaProxyAccessor.selectUoid(NAVIGATION_TABLE, id);
        if (log.isDebugEnabled()) log.debug("getEntry for " + id + " proxy is " + proxy);
        if (proxy == null) return null;
        JBossObjectInputStream ois = null;
        try {
            byte[] data = (byte[]) EquandaProxyAccessor.getField(proxy, FIELD_NAVIGATIONENTRY);
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ois = new JBossObjectInputStream(bais);
            Object read = ois.readObject();
            return (NavigationEntry) read;
        } catch (Exception ex) {
            log.error(ex, ex);
            return null;
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException ioe) {
                }
            }
        }
    }

    public String prepareSaveEntry(String id) {
        try {
            EquandaProxy proxy;
            if (id != null) {
                proxy = EquandaProxyAccessor.selectUoid(NAVIGATION_TABLE, id);
            } else {
                proxy = EquandaProxyAccessor.createProxy(NAVIGATION_TABLE);
            }
            proxy.equandaUpdate();
            return proxy.getUOID().getId();
        } catch (Exception ex) {
            log.error(ex, ex);
            return null;
        }
    }

    public String saveEntry(String id, NavigationEntry entry) {
        JBossObjectOutputStream oos = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            oos = new JBossObjectOutputStream(baos);
            oos.writeObject(entry);
            oos.flush();
            byte[] serialized = baos.toByteArray();
            EquandaProxy proxy;
            if (id != null) {
                proxy = EquandaProxyAccessor.selectUoid(NAVIGATION_TABLE, id);
            } else {
                proxy = EquandaProxyAccessor.createProxy(NAVIGATION_TABLE);
            }
            EquandaProxyAccessor.setField(proxy, FIELD_NAVIGATIONENTRY, byte[].class, serialized);
            proxy.equandaUpdate();
            return proxy.getUOID().getId();
        } catch (Exception ex) {
            log.error(ex, ex);
            return null;
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException ioe) {
                }
            }
        }
    }

    private class EntryGarbageCollector extends Thread {

        @Override
        public void run() {
            while (true) {
                if (log.isDebugEnabled()) {
                    log.debug("------------- EntryGarbageCollector - another garbage check ------------- ");
                }
                long threshold = System.currentTimeMillis() - GARBAGE_TIMEOUT_INTERVAL;
                EquandaQueryImpl query = new EquandaQueryImpl("delete from DMEquandaNavigationBean o where o.equandaModificationDate <= :time");
                Map<String, Object> parameters = new HashMap<String, Object>();
                parameters.put("time", new Timestamp(threshold));
                query.setParameters(parameters);
                try {
                    EquandaSelector sel = EquandaProxyAccessor.getSelector(NAVIGATION_TABLE);
                    sel.equandaRunUpdateQuery(query);
                    sel.remove();
                } catch (Exception e) {
                    log.error(e, e);
                }
                try {
                    Thread.sleep(GARBAGE_ENTRY_COLLECT_INTERVAL);
                } catch (Exception e) {
                }
            }
        }
    }
}

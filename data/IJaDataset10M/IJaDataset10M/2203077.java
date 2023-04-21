package org.bitdrive.network.history.impl;

import org.bitdrive.core.logging.api.Level;
import org.bitdrive.core.logging.api.Logger;
import org.bitdrive.core.settings.api.Settings;
import org.bitdrive.network.history.api.ConnectionHistory;
import org.bitdrive.network.http.api.RPCService;
import org.bitdrive.network.http.api.RPCServiceMethod;
import java.util.HashMap;
import java.util.LinkedList;

public class ConnectionHistoryImpl implements RPCService, ConnectionHistory {

    private final String SETTING_CONNECTION_HISTORY = "SETTING_CONNECTION_HISTORY";

    private Logger logger;

    private Settings settings;

    private LinkedList<ConnectionHistoryItem> history;

    public ConnectionHistoryImpl(Logger logger, Settings settings) {
        this.logger = logger;
        this.settings = settings;
        this.history = new LinkedList<ConnectionHistoryItem>();
        load();
    }

    private ConnectionHistoryItem getItem(String addr, int port) {
        for (ConnectionHistoryItem item : history) {
            if (item.addr.equals(addr)) {
                if (item.port == port) {
                    return item;
                }
            }
        }
        return null;
    }

    private void load() {
        LinkedList<Object[]> items = (LinkedList<Object[]>) settings.getObject(SETTING_CONNECTION_HISTORY, new LinkedList<Object[]>());
        for (Object[] item : items) {
            try {
                history.add(new ConnectionHistoryItem(item));
            } catch (Exception e) {
            }
        }
    }

    private void save() {
        LinkedList<Object[]> items = new LinkedList<Object[]>();
        for (ConnectionHistoryItem item : history) items.add(item.toStandardObjects());
        settings.setObject(SETTING_CONNECTION_HISTORY, items);
    }

    public void addToHistory(String addr, int port) {
        ConnectionHistoryItem item;
        if (addr.equals("127.0.0.1")) return;
        if (addr.equals("localhost")) return;
        item = getItem(addr, port);
        if (item == null) {
            logger.log(Level.FINE, "ConnectionHistoryImpl.addToHistory added host \"" + addr + "\" to connection history");
            history.add(new ConnectionHistoryItem(addr, port));
        } else {
            item.updateDate();
        }
        save();
    }

    public LinkedList<Object[]> getAutoConnectEntries() {
        LinkedList<Object[]> autoConnectList = new LinkedList<Object[]>();
        for (ConnectionHistoryItem item : history) {
            if (item.autoConnect) {
                autoConnectList.add(new Object[] { item.addr, item.port });
            }
        }
        return autoConnectList;
    }

    @RPCServiceMethod
    public Object history() {
        LinkedList<HashMap> items;
        items = new LinkedList<HashMap>();
        for (ConnectionHistoryItem historyItem : history) {
            HashMap<String, Object> item;
            item = new HashMap<String, Object>();
            item.put("addr", historyItem.addr);
            item.put("port", historyItem.port);
            item.put("date", historyItem.date);
            item.put("autoconnect", historyItem.autoConnect);
            items.add(item);
        }
        return items;
    }

    @RPCServiceMethod
    public void remove(String addr, int port) {
        ConnectionHistoryItem item;
        item = getItem(addr, port);
        if (item != null) {
            history.remove(item);
        }
        save();
    }

    @RPCServiceMethod
    public void autoconnect(String addr, int port) {
        ConnectionHistoryItem item;
        item = getItem(addr, port);
        if (item != null) {
            item.autoConnect = !item.autoConnect;
            save();
        }
        save();
    }

    @RPCServiceMethod
    public void autoconnect(String addr, int port, boolean autoConnect) {
        ConnectionHistoryItem item;
        item = getItem(addr, port);
        if (item != null) {
            item.autoConnect = autoConnect;
        }
        save();
    }

    public String getName() {
        return "history";
    }
}

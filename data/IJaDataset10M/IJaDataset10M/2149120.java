package net.sf.doolin.app.sc.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import net.sf.doolin.app.sc.common.turn.GameHistoryItem;

public class History implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<Integer, List<GameHistoryItem>> history = new TreeMap<Integer, List<GameHistoryItem>>();

    private int maxSize = 200;

    public synchronized void add(GameHistoryItem item) {
        addInternal(item);
        trim();
    }

    public synchronized void clear() {
        this.history.clear();
    }

    public void copy(History h, Integer year) {
        this.maxSize = h.getMaxSize();
        this.history.clear();
        List<GameHistoryItem> items = h.getItems(year);
        for (GameHistoryItem item : items) {
            addInternal(item);
        }
        trim();
    }

    public List<GameHistoryItem> getAllItems() {
        return getItems(null);
    }

    public synchronized List<GameHistoryItem> getItems(Integer year) {
        if (year != null) {
            List<GameHistoryItem> items = this.history.get(year);
            if (items != null) {
                return items;
            } else {
                return Collections.emptyList();
            }
        } else {
            List<GameHistoryItem> result = new ArrayList<GameHistoryItem>();
            for (List<GameHistoryItem> items : this.history.values()) {
                result.addAll(items);
            }
            return result;
        }
    }

    public int getMaxSize() {
        return this.maxSize;
    }

    public synchronized int getSize() {
        int size = 0;
        for (List<GameHistoryItem> items : this.history.values()) {
            size += items.size();
        }
        return size;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
        trim();
    }

    private void addInternal(GameHistoryItem item) {
        int year = item.getYear();
        List<GameHistoryItem> items = this.history.get(year);
        if (items == null) {
            items = new ArrayList<GameHistoryItem>();
            this.history.put(year, items);
        }
        items.add(item);
    }

    private synchronized void trim() {
        Iterator<Entry<Integer, List<GameHistoryItem>>> i = this.history.entrySet().iterator();
        while (getSize() > this.maxSize && i.hasNext()) {
            i.next();
            i.remove();
        }
    }
}

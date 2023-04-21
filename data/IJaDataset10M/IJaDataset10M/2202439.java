package com.wuala.server.loader.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import com.wuala.loader2.loader.data.ItemData;
import com.wuala.loader2.loader.data.VersionIndex;
import com.wuala.server.loader.data.ClassStore;

public class ClassQueue {

    private ClassStore classes;

    private ItemData[] data;

    private HashSet<String> prioSent;

    private HashSet<String> normSent;

    private LinkedList<ItemData> priority;

    private int pos;

    public ClassQueue(Collection<String> missings, VersionIndex index, ClassStore classes) {
        assert missings != null;
        this.classes = classes;
        this.prioSent = new HashSet<String>();
        this.normSent = new HashSet<String>(missings.size());
        this.data = classes.getOrderedList(index, missings);
        this.pos = 0;
    }

    public ClassQueue(VersionIndex index, ClassStore classes) {
        this.classes = classes;
        this.prioSent = new HashSet<String>();
        this.data = classes.getOrderedList(index);
        this.normSent = new HashSet<String>(data.length);
        this.pos = 0;
    }

    public boolean hasMore() {
        return pos < data.length;
    }

    public ItemData consume() {
        if (priority == null) {
            ItemData next = data[pos++];
            normSent.add(next.getName());
            while (hasMore() && prioSent.contains(data[pos].getName())) {
                pos++;
            }
            return next;
        } else {
            synchronized (this) {
                ItemData next = priority.removeFirst();
                prioSent.add(next.getName());
                if (priority.isEmpty()) {
                    this.priority = null;
                }
                return next;
            }
        }
    }

    public synchronized void request(String name, short version) {
        if (!normSent.contains(name)) {
            ItemData data = version == 0 ? new ItemData(name, true) : classes.get(name, version);
            assert data != null;
            if (priority == null) {
                LinkedList<ItemData> priority = new LinkedList<ItemData>();
                priority.add(data);
                this.priority = priority;
            } else {
                priority.add(data);
            }
        }
    }

    public synchronized void finish() {
        pos = data.length;
    }
}

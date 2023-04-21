package com.sun.star.lib.uno.protocols.urp;

import com.sun.star.lib.uno.environments.remote.ThreadId;
import com.sun.star.uno.IMethodDescription;
import java.util.HashMap;
import java.util.Stack;

final class PendingRequests {

    public PendingRequests() {
    }

    public synchronized void push(ThreadId tid, Item item) {
        Stack s = (Stack) map.get(tid);
        if (s == null) {
            s = new Stack();
            map.put(tid, s);
        }
        s.push(item);
    }

    public synchronized Item pop(ThreadId tid) {
        Stack s = (Stack) map.get(tid);
        Item i = (Item) s.pop();
        if (s.empty()) {
            map.remove(tid);
        }
        return i;
    }

    public static final class Item {

        public Item(boolean internal, IMethodDescription function, Object[] arguments) {
            this.internal = internal;
            this.function = function;
            this.arguments = arguments;
        }

        public final boolean internal;

        public final IMethodDescription function;

        public final Object[] arguments;
    }

    private final HashMap map = new HashMap();
}

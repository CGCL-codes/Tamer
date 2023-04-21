package org.sourceforge.jemm.client.events;

public class LockEvent {

    Object source;

    Object lock;

    int count;

    public LockEvent(Object source, Object lock, int count) {
        this.source = source;
        this.lock = lock;
        this.count = count;
    }

    public Object getSource() {
        return source;
    }

    public Object getLock() {
        return lock;
    }

    public int getCount() {
        return count;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + count;
        result = prime * result + ((lock == null) ? 0 : lock.hashCode());
        result = prime * result + ((source == null) ? 0 : source.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LockEvent)) return false;
        LockEvent other = (LockEvent) obj;
        return source == other.source && lock == other.lock && count == other.count;
    }

    @Override
    public String toString() {
        return "LockEvent( " + source + " ," + lock + ", count=" + count + ")";
    }
}

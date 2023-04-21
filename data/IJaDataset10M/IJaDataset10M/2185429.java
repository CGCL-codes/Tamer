package com.peterhix.net.client.launcher;

/**
 * Runnables with timestamp info
 * 
 * @author HAI YUN TAO
 */
public abstract class AbstractRunnable implements Runnable, Comparable<Object> {

    private long timestamp;

    /**
	 * 
	 */
    public AbstractRunnable() {
        timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int compareTo(Object other) {
        if (other instanceof AbstractRunnable) {
            AbstractRunnable that = (AbstractRunnable) other;
            return Long.valueOf(this.timestamp).compareTo(that.timestamp);
        } else {
            return Integer.valueOf(this.hashCode()).compareTo(other.hashCode());
        }
    }
}

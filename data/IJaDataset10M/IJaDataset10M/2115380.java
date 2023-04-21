package jgnash.engine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Exchange rate object
 *
 * @author Craig Cavanaugh
 * @version $Id: ExchangeRate.java 3051 2012-01-02 11:27:23Z ccavanaugh $
 */
public class ExchangeRate extends StoredObject {

    private static final long serialVersionUID = -2365289994847042288L;

    private List<ExchangeRateHistoryNode> historyNodes = new ArrayList<ExchangeRateHistoryNode>();

    /**
     * Cache the last exchange rate
     */
    private transient BigDecimal lastRate;

    /**
     * Identifier for the ExchangeRate object
     */
    private String rateId;

    /**
     * ReadWrite lock
     */
    private transient ReadWriteLock lock;

    /**
     * No argument constructor for reflection purposes.
     * <b>Do not use to create a new instance</b>
     *
     * @deprecated
     */
    @Deprecated
    public ExchangeRate() {
    }

    ExchangeRate(final String rateId) {
        this.rateId = rateId;
    }

    private synchronized ReadWriteLock getLock() {
        if (lock == null) {
            lock = new ReentrantReadWriteLock(true);
        }
        return lock;
    }

    public boolean contains(final ExchangeRateHistoryNode node) {
        Lock l = getLock().readLock();
        l.lock();
        boolean result = false;
        try {
            result = historyNodes.contains(node);
        } finally {
            l.unlock();
        }
        return result;
    }

    public List<ExchangeRateHistoryNode> getHistory() {
        return new ArrayList<ExchangeRateHistoryNode>(historyNodes);
    }

    boolean addHistoryNode(final ExchangeRateHistoryNode node) {
        boolean result = false;
        Lock l = getLock().writeLock();
        l.lock();
        try {
            int index = Collections.binarySearch(historyNodes, node);
            if (index < 0) {
                historyNodes.add(-index - 1, node);
            } else {
                historyNodes.set(index, node);
            }
            lastRate = null;
            result = true;
        } finally {
            l.unlock();
        }
        return result;
    }

    boolean removeHistoryNode(final ExchangeRateHistoryNode hNode) {
        boolean result = false;
        Lock l = getLock().writeLock();
        l.lock();
        try {
            result = historyNodes.remove(hNode);
            if (result) {
                lastRate = null;
            }
        } finally {
            l.unlock();
        }
        return result;
    }

    public String getRateId() {
        return rateId;
    }

    public BigDecimal getRate() {
        getLock().readLock().lock();
        try {
            if (lastRate == null) {
                if (!historyNodes.isEmpty()) {
                    lastRate = historyNodes.get(historyNodes.size() - 1).getRate();
                } else {
                    lastRate = BigDecimal.ONE;
                }
            }
        } finally {
            getLock().readLock().unlock();
        }
        return lastRate;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        return this == other || other instanceof ExchangeRate && this.rateId.equals(((ExchangeRate) other).rateId);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return super.hashCode() * 67 + rateId.hashCode();
    }
}

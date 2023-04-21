package com.phloc.commons.stats;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.jmx.JMXUtils;
import com.phloc.commons.string.StringHelper;

/**
 * Provides a central manager for the internal statistics.
 * 
 * @author philip
 */
@ThreadSafe
public final class StatisticsManager {

    public static final boolean DEFAULT_JMX_ENABLED = false;

    private static final AtomicBoolean s_aJMXEnabled = new AtomicBoolean(DEFAULT_JMX_ENABLED);

    private static final ReadWriteLock s_aRWLockCache = new ReentrantReadWriteLock();

    private static final ReadWriteLock s_aRWLockTimer = new ReentrantReadWriteLock();

    private static final ReadWriteLock s_aRWLockKeyedTimer = new ReentrantReadWriteLock();

    private static final ReadWriteLock s_aRWLockSize = new ReentrantReadWriteLock();

    private static final ReadWriteLock s_aRWLockKeyedSize = new ReentrantReadWriteLock();

    private static final ReadWriteLock s_aRWLockCounter = new ReentrantReadWriteLock();

    private static final ReadWriteLock s_aRWLockKeyedCounter = new ReentrantReadWriteLock();

    private static final Map<String, StatisticsHandlerCache> s_aHdlCache = new HashMap<String, StatisticsHandlerCache>();

    private static final Map<String, StatisticsHandlerTimer> s_aHdlTimer = new HashMap<String, StatisticsHandlerTimer>();

    private static final Map<String, StatisticsHandlerKeyedTimer> s_aHdlKeyedTimer = new HashMap<String, StatisticsHandlerKeyedTimer>();

    private static final Map<String, StatisticsHandlerSize> s_aHdlSize = new HashMap<String, StatisticsHandlerSize>();

    private static final Map<String, StatisticsHandlerKeyedSize> s_aHdlKeyedSize = new HashMap<String, StatisticsHandlerKeyedSize>();

    private static final Map<String, StatisticsHandlerCounter> s_aHdlCounter = new HashMap<String, StatisticsHandlerCounter>();

    private static final Map<String, StatisticsHandlerKeyedCounter> s_aHdlKeyedCounter = new HashMap<String, StatisticsHandlerKeyedCounter>();

    @PresentForCodeCoverage
    @SuppressWarnings("unused")
    private static final StatisticsManager s_aInstance = new StatisticsManager();

    private StatisticsManager() {
    }

    public static boolean isJMXEnabled() {
        return s_aJMXEnabled.get();
    }

    public static void setJMXEnabled(final boolean bEnabled) {
        s_aJMXEnabled.set(bEnabled);
    }

    @Nonnull
    public static IStatisticsHandlerCache getCacheHandler(@Nonnull final Class<?> aClass) {
        if (aClass == null) throw new NullPointerException("class");
        return getCacheHandler(aClass.getName());
    }

    @Nonnull
    public static IStatisticsHandlerCache getCacheHandler(@Nonnull @Nonempty final String sName) {
        if (StringHelper.hasNoText(sName)) throw new IllegalArgumentException("name");
        s_aRWLockCache.writeLock().lock();
        try {
            StatisticsHandlerCache aHdl = s_aHdlCache.get(sName);
            if (aHdl == null) {
                aHdl = new StatisticsHandlerCache();
                if (isJMXEnabled()) JMXUtils.exposeMBeanWithAutoName(aHdl, sName);
                s_aHdlCache.put(sName, aHdl);
            }
            return aHdl;
        } finally {
            s_aRWLockCache.writeLock().unlock();
        }
    }

    @Nonnull
    @ReturnsMutableCopy
    public static Set<String> getAllCacheHandler() {
        s_aRWLockCache.readLock().lock();
        try {
            return ContainerHelper.newSet(s_aHdlCache.keySet());
        } finally {
            s_aRWLockCache.readLock().unlock();
        }
    }

    @Nonnull
    public static IStatisticsHandlerTimer getTimerHandler(@Nonnull final Class<?> aClass) {
        if (aClass == null) throw new NullPointerException("class");
        return getTimerHandler(aClass.getName());
    }

    @Nonnull
    public static IStatisticsHandlerTimer getTimerHandler(@Nonnull @Nonempty final String sName) {
        if (StringHelper.hasNoText(sName)) throw new IllegalArgumentException("name");
        s_aRWLockTimer.writeLock().lock();
        try {
            StatisticsHandlerTimer aHdl = s_aHdlTimer.get(sName);
            if (aHdl == null) {
                aHdl = new StatisticsHandlerTimer();
                if (isJMXEnabled()) JMXUtils.exposeMBeanWithAutoName(aHdl, sName);
                s_aHdlTimer.put(sName, aHdl);
            }
            return aHdl;
        } finally {
            s_aRWLockTimer.writeLock().unlock();
        }
    }

    @Nonnull
    @ReturnsMutableCopy
    public static Set<String> getAllTimerHandler() {
        s_aRWLockTimer.readLock().lock();
        try {
            return ContainerHelper.newSet(s_aHdlTimer.keySet());
        } finally {
            s_aRWLockTimer.readLock().unlock();
        }
    }

    @Nonnull
    public static IStatisticsHandlerKeyedTimer getKeyedTimerHandler(@Nonnull final Class<?> aClass) {
        if (aClass == null) throw new NullPointerException("class");
        return getKeyedTimerHandler(aClass.getName());
    }

    @Nonnull
    public static IStatisticsHandlerKeyedTimer getKeyedTimerHandler(@Nonnull @Nonempty final String sName) {
        if (StringHelper.hasNoText(sName)) throw new IllegalArgumentException("name");
        s_aRWLockKeyedTimer.writeLock().lock();
        try {
            StatisticsHandlerKeyedTimer aHdl = s_aHdlKeyedTimer.get(sName);
            if (aHdl == null) {
                aHdl = new StatisticsHandlerKeyedTimer();
                if (isJMXEnabled()) JMXUtils.exposeMBeanWithAutoName(aHdl, sName);
                s_aHdlKeyedTimer.put(sName, aHdl);
            }
            return aHdl;
        } finally {
            s_aRWLockKeyedTimer.writeLock().unlock();
        }
    }

    @Nonnull
    @ReturnsMutableCopy
    public static Set<String> getAllKeyedTimerHandler() {
        s_aRWLockKeyedTimer.readLock().lock();
        try {
            return ContainerHelper.newSet(s_aHdlKeyedTimer.keySet());
        } finally {
            s_aRWLockKeyedTimer.readLock().unlock();
        }
    }

    @Nonnull
    public static IStatisticsHandlerSize getSizeHandler(@Nonnull final Class<?> aClass) {
        if (aClass == null) throw new NullPointerException("class");
        return getSizeHandler(aClass.getName());
    }

    @Nonnull
    public static IStatisticsHandlerSize getSizeHandler(@Nonnull @Nonempty final String sName) {
        if (StringHelper.hasNoText(sName)) throw new IllegalArgumentException("name");
        s_aRWLockSize.writeLock().lock();
        try {
            StatisticsHandlerSize aHdl = s_aHdlSize.get(sName);
            if (aHdl == null) {
                aHdl = new StatisticsHandlerSize();
                if (isJMXEnabled()) JMXUtils.exposeMBeanWithAutoName(aHdl, sName);
                s_aHdlSize.put(sName, aHdl);
            }
            return aHdl;
        } finally {
            s_aRWLockSize.writeLock().unlock();
        }
    }

    @Nonnull
    @ReturnsMutableCopy
    public static Set<String> getAllSizeHandler() {
        s_aRWLockSize.readLock().lock();
        try {
            return ContainerHelper.newSet(s_aHdlSize.keySet());
        } finally {
            s_aRWLockSize.readLock().unlock();
        }
    }

    @Nonnull
    public static IStatisticsHandlerKeyedSize getKeyedSizeHandler(@Nonnull final Class<?> aClass) {
        if (aClass == null) throw new NullPointerException("class");
        return getKeyedSizeHandler(aClass.getName());
    }

    @Nonnull
    public static IStatisticsHandlerKeyedSize getKeyedSizeHandler(@Nonnull @Nonempty final String sName) {
        if (StringHelper.hasNoText(sName)) throw new IllegalArgumentException("name");
        s_aRWLockKeyedSize.writeLock().lock();
        try {
            StatisticsHandlerKeyedSize aHdl = s_aHdlKeyedSize.get(sName);
            if (aHdl == null) {
                aHdl = new StatisticsHandlerKeyedSize();
                if (isJMXEnabled()) JMXUtils.exposeMBeanWithAutoName(aHdl, sName);
                s_aHdlKeyedSize.put(sName, aHdl);
            }
            return aHdl;
        } finally {
            s_aRWLockKeyedSize.writeLock().unlock();
        }
    }

    @Nonnull
    @ReturnsMutableCopy
    public static Set<String> getAllKeyedSizeHandler() {
        s_aRWLockKeyedSize.readLock().lock();
        try {
            return ContainerHelper.newSet(s_aHdlKeyedSize.keySet());
        } finally {
            s_aRWLockKeyedSize.readLock().unlock();
        }
    }

    @Nonnull
    public static IStatisticsHandlerCounter getCounterHandler(@Nonnull final Class<?> aClass) {
        if (aClass == null) throw new NullPointerException("class");
        return getCounterHandler(aClass.getName());
    }

    @Nonnull
    public static IStatisticsHandlerCounter getCounterHandler(@Nonnull @Nonempty final String sName) {
        if (StringHelper.hasNoText(sName)) throw new IllegalArgumentException("name");
        s_aRWLockCounter.writeLock().lock();
        try {
            StatisticsHandlerCounter aHdl = s_aHdlCounter.get(sName);
            if (aHdl == null) {
                aHdl = new StatisticsHandlerCounter();
                if (isJMXEnabled()) JMXUtils.exposeMBeanWithAutoName(aHdl, sName);
                s_aHdlCounter.put(sName, aHdl);
            }
            return aHdl;
        } finally {
            s_aRWLockCounter.writeLock().unlock();
        }
    }

    @Nonnull
    @ReturnsMutableCopy
    public static Set<String> getAllCounterHandler() {
        s_aRWLockCounter.readLock().lock();
        try {
            return ContainerHelper.newSet(s_aHdlCounter.keySet());
        } finally {
            s_aRWLockCounter.readLock().unlock();
        }
    }

    @Nonnull
    public static IStatisticsHandlerKeyedCounter getKeyedCounterHandler(@Nonnull final Class<?> aClass) {
        if (aClass == null) throw new NullPointerException("class");
        return getKeyedCounterHandler(aClass.getName());
    }

    @Nonnull
    public static IStatisticsHandlerKeyedCounter getKeyedCounterHandler(@Nonnull @Nonempty final String sName) {
        if (StringHelper.hasNoText(sName)) throw new IllegalArgumentException("name");
        s_aRWLockKeyedCounter.writeLock().lock();
        try {
            StatisticsHandlerKeyedCounter aHdl = s_aHdlKeyedCounter.get(sName);
            if (aHdl == null) {
                aHdl = new StatisticsHandlerKeyedCounter();
                if (isJMXEnabled()) JMXUtils.exposeMBeanWithAutoName(aHdl, sName);
                s_aHdlKeyedCounter.put(sName, aHdl);
            }
            return aHdl;
        } finally {
            s_aRWLockKeyedCounter.writeLock().unlock();
        }
    }

    @Nonnull
    @ReturnsMutableCopy
    public static Set<String> getAllKeyedCounterHandler() {
        s_aRWLockKeyedCounter.readLock().lock();
        try {
            return ContainerHelper.newSet(s_aHdlKeyedCounter.keySet());
        } finally {
            s_aRWLockKeyedCounter.readLock().unlock();
        }
    }
}

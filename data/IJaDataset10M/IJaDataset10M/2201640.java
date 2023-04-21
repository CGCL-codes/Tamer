package com.phloc.commons.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Based on a DefaultThreadFactory somewhere in the Sun JDK but with the ability
 * to change the name of the thread slightly :)
 * 
 * @author philip
 */
@ThreadSafe
public class ExtendedDefaultThreadFactory implements ThreadFactory {

    private static final AtomicInteger s_aPoolNumber = new AtomicInteger(1);

    private final ThreadGroup m_aThreadGroup;

    private final AtomicInteger m_aThreadNumber = new AtomicInteger(1);

    private final String m_sNamePrefix;

    public ExtendedDefaultThreadFactory() {
        this("threadpool");
    }

    public ExtendedDefaultThreadFactory(@Nonnull @Nonempty final String sPoolPrefix) {
        final SecurityManager aSecMgr = System.getSecurityManager();
        m_aThreadGroup = aSecMgr != null ? aSecMgr.getThreadGroup() : Thread.currentThread().getThreadGroup();
        m_sNamePrefix = sPoolPrefix + "[p" + s_aPoolNumber.getAndIncrement() + "-t";
    }

    @Nonnull
    public Thread newThread(@Nonnull final Runnable r) {
        final String sThreadName = m_sNamePrefix + m_aThreadNumber.getAndIncrement() + ']';
        final Thread aThread = new Thread(m_aThreadGroup, r, sThreadName, 0);
        if (aThread.isDaemon()) aThread.setDaemon(false);
        if (aThread.getPriority() != Thread.NORM_PRIORITY) aThread.setPriority(Thread.NORM_PRIORITY);
        return aThread;
    }

    @Override
    public String toString() {
        return new ToStringGenerator(this).append("threadGroup", m_aThreadGroup).append("threadNumber", m_aThreadNumber).append("namePrefix", m_sNamePrefix).toString();
    }
}

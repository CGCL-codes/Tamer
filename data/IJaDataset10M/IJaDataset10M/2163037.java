package net.sf.syracuse.threads.impl;

import net.sf.syracuse.threads.ThreadState;
import net.sf.syracuse.threads.ThreadStateManager;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Concrete implementation of {@code ThreadStateManager}.
 *
 * @author Chris Conrad
 * @since 1.0.0
 */
public class ThreadStateManagerImpl implements ThreadStateManager {

    private final Map<Thread, ThreadState> threadStateMap;

    public ThreadStateManagerImpl() {
        threadStateMap = Collections.synchronizedMap(new WeakHashMap<Thread, ThreadState>());
    }

    public Map<Thread, ThreadState> getAllThreadsState() {
        return threadStateMap;
    }

    public ThreadState getThreadState() {
        return getThreadState(Thread.currentThread());
    }

    public ThreadState getThreadState(Thread thread) {
        return threadStateMap.get(thread);
    }

    public void setThreadState(ThreadState threadState) {
        setThreadState(Thread.currentThread(), threadState);
    }

    public void setThreadState(Thread thread, ThreadState threadState) {
        threadStateMap.put(thread, threadState);
    }

    public void setThreadStateAttachment(Object attachment) {
        setThreadStateAttachment(Thread.currentThread(), attachment);
    }

    public void setThreadStateAttachment(Thread thread, Object attachment) {
        threadStateMap.get(thread).setAttachment(attachment);
    }
}

package org.japura.task;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * <P>
 * Copyright (C) 2011-2012 Carlos Eduardo Leite de Andrade
 * <P>
 * This library is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * <P>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * <P>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <A
 * HREF="www.gnu.org/licenses/">www.gnu.org/licenses/</A>
 * <P>
 * For more information, contact: <A HREF="www.japura.org">www.japura.org</A>
 * <P>
 * 
 * @author Carlos Eduardo Leite de Andrade
 */
final class AsynchronousTaskExecutor extends AbstractTaskExecutor {

    public AsynchronousTaskExecutor() {
        super(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    }

    @Override
    void beforeExecute(Task<?> task) {
        TaskManager.addToDebugWindow(task);
        task.setThreadId(Thread.currentThread().getId());
        TaskManager.addToDebugWindow(task, TaskEvent.BEFORE);
        TaskManager.fireBeforeExecuteListeners(task);
        if (task.isCanceled()) {
            return;
        }
        task.willExecute();
    }

    @Override
    protected void afterExecute(final Task<?> task) {
        TaskManager.removeToDebugWindow(task);
        TaskManager.addToDebugWindow(task, TaskEvent.AFTER);
        TaskManager.fireAfterExecuteListeners(task);
        applyAfterExecute(task);
    }

    @Override
    protected boolean isWaitForEDT(Task<?> task) {
        return false;
    }
}

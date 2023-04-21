package org.japura.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.SwingUtilities;
import org.japura.controller.AbstractController.TokenKey;
import org.japura.controller.ControllerModel;
import org.japura.controller.Group;
import org.japura.debug.DebugComponent;
import org.japura.debug.DebugResult;
import org.japura.debug.DebugWindow;
import org.japura.task.DebugTasks.AddParameter;
import org.japura.task.DebugTasks.AddParameterEvent;
import org.japura.task.DebugTasks.RemoveParameter;

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
public final class TaskManager {

    private TaskManager() {
    }

    private static ReentrantLock lock = new ReentrantLock();

    private static List<TaskManagerListener> listeners = new ArrayList<TaskManagerListener>();

    /** for tasks without controller */
    private static SynchronousTaskExecutor synchronousExecutor = new SynchronousTaskExecutor();

    /** for any task with asynchronous mode */
    private static AsynchronousTaskExecutor asynchronousExecutor = new AsynchronousTaskExecutor();

    /** for tasks from controller group */
    private static Map<Group, SynchronousTaskExecutor> groupExecutors = new HashMap<Group, SynchronousTaskExecutor>();

    private static List<Group> pendingToRemove = new ArrayList<Group>();

    static void fireBeforeExecuteListeners(Task<?> task) {
        for (TaskManagerListener listener : listeners) {
            listener.beforeExecute(task);
        }
    }

    static void fireRemovedListeners(Task<?> task) {
        for (TaskManagerListener listener : listeners) {
            listener.removed(task);
        }
    }

    static void fireAfterExecuteListeners(Task<?> task) {
        for (TaskManagerListener listener : listeners) {
            listener.afterExecute(task);
        }
    }

    public static synchronized void addListener(TaskManagerListener listener) {
        listeners.add(listener);
    }

    public static synchronized void removeListener(TaskManagerListener listener) {
        listeners.remove(listener);
    }

    public static synchronized List<TaskManagerListener> getListeners() {
        return Collections.unmodifiableList(listeners);
    }

    static List<DebugResult> addToDebugWindow(Task<?> task, TaskEvent event) {
        if (DebugComponent.exists(DebugTasks.class)) {
            AddParameterEvent parameter = new AddParameterEvent(task, event, System.currentTimeMillis(), SwingUtilities.isEventDispatchThread(), getQueueCount());
            return DebugComponent.publish(DebugTasks.class, parameter);
        }
        return null;
    }

    static void addToDebugWindow(Task<?> task) {
        if (DebugComponent.exists(DebugTasks.class)) {
            AddParameter parameter = new AddParameter(task);
            DebugComponent.publish(DebugTasks.class, parameter);
        }
    }

    static void removeToDebugWindow(Task<?> task) {
        if (DebugComponent.exists(DebugTasks.class)) {
            RemoveParameter parameter = new RemoveParameter(task);
            DebugComponent.publish(DebugTasks.class, parameter);
        }
    }

    public static void submit(Task<?> task) {
        submit(task, false);
    }

    public static void submit(Task<?> task, boolean isAsynchronous) {
        lock.lock();
        try {
            if (task.isSubmitted()) {
                throw new TaskExeception("Task already submitted");
            }
            task.setSubmitted(true);
        } finally {
            lock.unlock();
        }
        addToDebugWindow(task, TaskEvent.SUBMIT);
        for (TaskManagerListener listener : listeners) {
            listener.submitted(task);
        }
        task.submitted();
        if (isAsynchronous) {
            task.setCancelToken(asynchronousExecutor.getCancelToken());
            asynchronousExecutor.submit(task);
            return;
        }
        SynchronousTaskExecutor executor = synchronousExecutor;
        ControllerModel controller = task.getRootModel();
        if (controller != null) {
            Group group = controller.getGroup();
            executor = groupExecutors.get(group);
        }
        if (executor == null) {
            throw new RuntimeException("There is no executor");
        }
        task.setCancelToken(executor.getCancelToken());
        executor.submit(task);
    }

    static void checkAndClean(Task<?> task) {
        ControllerModel controller = task.getRootModel();
        if (controller == null) {
            return;
        }
        Group group = controller.getGroup();
        checkAndClean(group);
    }

    private static void checkAndClean(Group group) {
        lock.lock();
        try {
            if (pendingToRemove.size() == 0) {
                return;
            }
            if (pendingToRemove.contains(group) == false) {
                return;
            }
            SynchronousTaskExecutor executor = groupExecutors.get(group);
            if (executor != null && executor.hasTask() == false) {
                executor.cancel();
                pendingToRemove.remove(group);
                groupExecutors.remove(group);
            }
        } finally {
            lock.unlock();
        }
    }

    public static void register(Group group, TokenKey tokenKey) {
        if (tokenKey == null) {
            return;
        }
        lock.lock();
        try {
            groupExecutors.put(group, new SynchronousTaskExecutor());
        } finally {
            lock.unlock();
        }
    }

    public static void unregister(Group group, TokenKey tokenKey) {
        if (tokenKey == null) {
            return;
        }
        lock.lock();
        try {
            pendingToRemove.add(group);
        } finally {
            lock.unlock();
        }
        checkAndClean(group);
    }

    public static boolean hasTask(Group group) {
        if (asynchronousExecutor.hasTask(group.getId())) {
            return true;
        }
        lock.lock();
        try {
            SynchronousTaskExecutor executor = groupExecutors.get(group);
            if (executor != null) {
                return executor.hasTask();
            }
        } finally {
            lock.unlock();
        }
        return false;
    }

    public static boolean hasTask() {
        lock.lock();
        try {
            for (SynchronousTaskExecutor executor : groupExecutors.values()) {
                if (executor.hasTask()) {
                    return true;
                }
            }
        } finally {
            lock.unlock();
        }
        return synchronousExecutor.hasTask() || asynchronousExecutor.hasTask();
    }

    public static void cancel(int controllerGroupId) {
        lock.lock();
        try {
            for (Entry<Group, SynchronousTaskExecutor> entry : groupExecutors.entrySet()) {
                Group group = entry.getKey();
                if (group.getId() == controllerGroupId) {
                    entry.getValue().cancel();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public static void cancelAll() {
        asynchronousExecutor.cancel();
        synchronousExecutor.cancel();
        lock.lock();
        try {
            for (SynchronousTaskExecutor executor : groupExecutors.values()) {
                executor.cancel();
            }
        } finally {
            lock.unlock();
        }
    }

    public static int getQueueCount() {
        int count = 0;
        lock.lock();
        try {
            for (SynchronousTaskExecutor executor : groupExecutors.values()) {
                count += executor.getQueue().size();
            }
        } finally {
            lock.unlock();
        }
        count += synchronousExecutor.getQueue().size();
        count += asynchronousExecutor.getQueue().size();
        return count;
    }

    public static DebugComponent createDebugPanel() {
        DebugTasks component = new DebugTasks();
        DebugComponent.add(component);
        return component;
    }

    public static void showDebugWindow() {
        DebugWindow.showDebugWindow(createDebugPanel());
    }
}

package org.redroo.metamodel;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import org.redroo.util.Assert;

public class ConcurrentTransitionActionsWrapper {

    private Set<MetaTransitionAction> actions;

    public ConcurrentTransitionActionsWrapper(Set<MetaTransitionAction> actions) {
        Assert.notEmpty(actions, IllegalArgumentException.class, "actions must not be null or empty");
        this.actions = actions;
    }

    public MetaTransitionAction getMetaTransitionAction() {
        MetaTransitionAction action = null;
        try {
            action = new MetaTransitionAction();
            action.instance = this;
            action.action = getClass().getDeclaredMethod("performActions", (Class<?>) null);
        } catch (Exception e) {
        }
        return action;
    }

    public void performActions() {
        Queue<Thread> threads = new LinkedList<Thread>();
        for (final MetaTransitionAction action : actions) {
            Thread thread = new Thread(new Runnable() {

                public void run() {
                    try {
                        action.perform();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            threads.add(thread);
            thread.setDaemon(true);
            thread.start();
        }
        while (!threads.isEmpty()) {
            Thread thread = threads.poll();
            while (thread.isAlive()) {
                try {
                    thread.join();
                } catch (InterruptedException ignored) {
                }
            }
        }
    }
}

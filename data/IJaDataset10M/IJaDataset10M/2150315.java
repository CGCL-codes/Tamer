package com.knightsoft.util;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * // A Future and the Callable that produced it.
 * 
 * @version V1.0 Nov 08, 2008
 * @author Michael
 * @param <R>
 * @param <C>
 */
public class TaskItem<R, C extends Callable<R>> {

    public final Future<R> future;

    public final C task;

    public TaskItem(Future<R> future, C task) {
        this.future = future;
        this.task = task;
    }
}

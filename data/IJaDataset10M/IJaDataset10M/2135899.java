package com.infomancers.collections.actions;

import com.infomancers.collections.Action;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author aviadbd
 */
public final class ActionList<T> implements Action<T> {

    private Collection<Action<T>> actions;

    public ActionList(Action<T>... actions) {
        this(Arrays.asList(actions));
    }

    public ActionList(Collection<Action<T>> actions) {
        this.actions = actions;
    }

    public void accept(T item) throws Exception {
        for (Action<T> action : actions) {
            action.accept(item);
        }
    }
}

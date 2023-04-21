package com.alesj.newsfeed.bool.impl;

import java.util.Set;
import com.alesj.newsfeed.bool.Operator;

/**
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
public class And<T> implements Operator<T> {

    public static final And INSTANCE = new And();

    private And() {
    }

    public Set<T> execute(Set<T> left, Set<T> right) {
        left.retainAll(right);
        return left;
    }
}

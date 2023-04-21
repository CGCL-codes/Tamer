package org.jtools.iofs.memory;

import org.jpattern.condition.Condition;

public final class Filter {

    private Filter() {
    }

    public static final Condition<TNode> DIRECTORIES = new Condition<TNode>() {

        public boolean match(TNode filterable) {
            return filterable.isDir();
        }
    };

    public static final Condition<TNode> FILES = new Condition<TNode>() {

        public boolean match(TNode filterable) {
            return !filterable.isDir();
        }
    };
}

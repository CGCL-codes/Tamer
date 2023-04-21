package org.sc.tree;

import java.util.Collections;
import java.util.List;

public class ExtendedTypeCallable {

    protected List<Type> args;

    protected Type ret;

    public ExtendedTypeCallable(List<Type> args, Type ret) {
        this.args = Collections.unmodifiableList(args);
        this.ret = ret;
        if (ret == null || args == null) {
            throw new NullPointerException();
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (args.hashCode());
        result = prime * result + (ret.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        ExtendedTypeCallable other;
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        other = (ExtendedTypeCallable) obj;
        if (!ret.equals(other.ret)) {
            return false;
        }
        return args.equals(other.args);
    }

    public List<Type> getArgs() {
        return args;
    }

    public Type getReturnType() {
        return ret;
    }
}

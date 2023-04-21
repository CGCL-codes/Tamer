package jfun.yan.lifecycle;

import java.io.Serializable;
import java.lang.reflect.Method;
import jfun.util.SerializableMethod;

final class MethodProcedure implements Procedure, Serializable {

    private final SerializableMethod mtd;

    public void invoke(Object self, Object[] args) throws Throwable {
        invokeMethod(self, mtd.getMethod(), args);
    }

    MethodProcedure(Method mtd) {
        this.mtd = new SerializableMethod(mtd);
    }

    static void invokeMethod(Object self, Method mtd, Object[] args) throws Throwable {
        mtd.invoke(self, args);
    }

    public boolean equals(Object obj) {
        if (obj instanceof MethodProcedure) {
            final MethodProcedure other = (MethodProcedure) obj;
            return mtd.equals(other.mtd);
        } else return false;
    }

    public int hashCode() {
        return mtd.hashCode();
    }

    public String toString() {
        return mtd.toString();
    }
}

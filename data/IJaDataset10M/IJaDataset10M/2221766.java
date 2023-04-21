package org.tagbox.util.test;

import org.tagbox.util.MinMaxPool;
import java.lang.reflect.Method;
import org.tagbox.util.Log;

public class MinMaxPoolTest {

    private static int count = 0;

    public static Integer newInt() {
        return new Integer(count++);
    }

    public static void main(String args[]) throws Exception {
        int sz = 15;
        Log.trace("pool exercise -> " + Integer.toString(sz));
        Method meth = MinMaxPoolTest.class.getMethod("newInt", new Class[] {});
        MinMaxPool pool = new MinMaxPool(null, meth, null);
        Log.trace("test the pool");
        Log.trace(pool.toString());
        Object o = pool.get();
        Log.trace(pool.toString());
        pool.release(o);
        Log.trace(pool.toString());
        pool.setMinimumFree(5);
        pool.setMaximumFree(10);
        Object[] store = new Object[sz];
        Log.trace("here we go");
        for (int i = 0; i < sz; ++i) {
            store[i] = pool.get();
            Log.trace(pool.toString());
            holdon();
        }
        for (int i = 0; i < sz; ++i) {
            pool.release(store[i]);
            Log.trace(pool.toString());
            holdon();
        }
        Log.trace("holdon");
        holdon();
        holdon();
        holdon();
        Log.trace(pool.toString());
        Log.trace("bye!");
    }

    private static void holdon() throws Exception {
        Thread.currentThread().sleep(300);
    }
}

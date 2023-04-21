package me.fantasy2.threadpool;

import java.util.Hashtable;
import java.util.Stack;

/**
 *
 * @author Cloudee
 */
public final class ThreadPool {

    private static Hashtable namedPool = new Hashtable();

    public static ThreadPool forName(String name) {
        ThreadPool ret;
        ret = (ThreadPool) namedPool.get(name);
        if (ret == null) {
            ret = new ThreadPool();
            namedPool.put(name, ret);
        }
        return ret;
    }

    int minIdle = 5;

    int maxIdle = 30;

    Stack idles = new Stack();

    public synchronized void invoke(Runnable r) {
        Runner runner;
        if (idles.size() > minIdle + 1) {
            runner = (Runner) idles.pop();
        } else {
            runner = new Runner(this);
        }
        runner.invoke(r);
    }

    synchronized void addIdle(Runner r) {
        if (idles.size() < maxIdle) {
            idles.push(r);
        } else {
            r.destory();
        }
    }
}

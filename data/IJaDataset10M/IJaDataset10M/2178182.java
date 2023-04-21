package org.neodatis.odb.test.multithread.sameVm;

import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.test.vo.login.Function;

/**
 * @author olivier
 * 
 */
public class ThreadOdbGetWithSameConnection extends Thread {

    private ODB odb;

    private String id;

    private boolean goOn;

    public ThreadOdbGetWithSameConnection(ODB odb) {
        this.odb = odb;
        this.goOn = true;
    }

    public void run() {
        Objects<Function> functions = odb.getObjects(Function.class);
        System.out.println(String.format("%d functions in thread %s", functions.size(), Thread.currentThread().getName()));
    }

    public void end() {
        goOn = false;
    }
}

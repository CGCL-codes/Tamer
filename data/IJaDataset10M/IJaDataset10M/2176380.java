package com.myjavalab.thread;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class ThreadSpecificPatternLog {

    public static void main(String[] args) {
        new ClientThreadLog("Alice").start();
        new ClientThreadLog("Bobby").start();
        new ClientThreadLog("Chris").start();
        Log.close();
    }
}

class TSLog {

    private PrintWriter writer = null;

    public TSLog(String filename) {
        try {
            writer = new PrintWriter(new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void println(String s) {
        writer.println(s);
    }

    public void close() {
        writer.println("------End of Log--------");
        writer.close();
    }
}

class Log {

    private static final ThreadLocal<TSLog> tsLogCollection = new ThreadLocal<TSLog>();

    public static void println(String s) {
        getTSLog().println(s);
    }

    public static void close() {
        getTSLog().close();
    }

    private static TSLog getTSLog() {
        TSLog tsLog = tsLogCollection.get();
        if (tsLog == null) {
            tsLog = new TSLog(Thread.currentThread().getName() + "--log.txt");
            tsLogCollection.set(tsLog);
        }
        return tsLog;
    }
}

class ClientThreadLog extends Thread {

    public ClientThreadLog(String name) {
        super(name);
        Log.println(name + " constructor is called.");
    }

    public void run() {
        System.out.println(getName() + " Begin");
        for (int i = 0; i < 10; i++) {
            Log.println("i = " + i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.close();
        System.out.println("end");
    }
}

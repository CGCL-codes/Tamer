package com.gtt.pattern.creational.factory;

/**
 * Database Log
 *
 * @author Michael(gao12581@sina.com)
 * @date 2011-2-22 09:47:46
 * 
 */
public class DatabaseLog extends Log {

    @Override
    public void write() {
        System.out.println("database log");
    }
}

package com.gtt.pattern.creational.factory;

/**
 * Event Log Factory
 *
 * @author Michael(gao12581@sina.com)
 * @date 2011-2-22 09:49:42
 * 
 */
public class EventLogFactory extends LogFactory {

    @Override
    public Log create() {
        return new EventLog();
    }
}

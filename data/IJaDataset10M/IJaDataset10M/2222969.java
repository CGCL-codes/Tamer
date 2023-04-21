package net.sf.jzeno.util;

import org.apache.log4j.Logger;

/**
 * @author Dimitry D'hondt
 * 
 * This class is responsible for generating a unique identifier. It is used by
 * the mechanism that keeps client and server state in sync.
 */
public class GuidGenerator {

    protected static Logger log = Logger.getLogger(GuidGenerator.class);

    private static GuidGenerator instance = new GuidGenerator();

    private GuidGenerator() {
    }

    public static GuidGenerator getInstance() {
        return instance;
    }

    public String generate() {
        return Long.toString((long) (Math.random() * 1000000));
    }

    public String generateId() {
        return "id" + generate();
    }
}

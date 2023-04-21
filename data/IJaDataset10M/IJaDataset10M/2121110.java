package com.sjt.pi.engine;

/**
 * Base class for the Engine that contains reusable attributes and methods for the other applications.
 * 
 * @author Alexey Pashkevich
 * 
 */
public abstract class Engine {

    private static MappingEngine mappingEngine;

    private static String config = "com/sjt/pi/mapping.xml";

    public Engine() {
        mappingEngine = new MappingEngine(config);
    }

    public static MappingEngine getMappingEngine() {
        return mappingEngine;
    }
}

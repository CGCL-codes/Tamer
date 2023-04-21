package com.fury.demos.runners;

import com.fury.framework.FuryEngine;
import com.fury.framework.FuryException;

/**
 * Just a simple demo to build a basic system from scratch. 
 * 
 * Version 0.1 - This will be a test in order to just start the engine with a single system (rendersystem), 
 * based on slick2D. A simple window creation is a good test.
 * This will be done in order to fully understand how the engine works.
 * 
 * @author Spiegel
 *
 */
public class FuryRunner {

    FuryEngine engine;

    public FuryRunner() {
        engine = new FuryEngine();
    }

    public void start() {
        try {
            engine.start("src/main/java/com/fury/demos/runners/FuryConfiguration.xml");
        } catch (FuryException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FuryRunner fury = new FuryRunner();
        fury.start();
    }
}

package com.fury.demos.robots;

import com.fury.framework.FuryException;
import com.fury.framework.Scheduler;
import com.fury.systems.scripting.ScriptingSystem;

public class RobotDemo {

    public static void main(String[] args) {
        try {
            new RobotDemo();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public RobotDemo() throws InterruptedException {
        System.out.println("Running RobotDemo");
        Scheduler scheduler = new Scheduler();
        try {
            scheduler.addSystem(new ScriptingSystem());
        } catch (FuryException e) {
            e.printStackTrace();
        }
        long lastTime = System.currentTimeMillis();
        long acc = 0;
        int fps = 0;
        int frames = 0;
        while (true) {
            long now = System.currentTimeMillis();
            acc += now - lastTime;
            frames++;
            if (acc >= 1000) {
                fps = frames;
                frames = 0;
                acc -= 1000;
                System.out.println("FPS: " + fps);
            }
            lastTime = now;
            scheduler.tick();
        }
    }
}

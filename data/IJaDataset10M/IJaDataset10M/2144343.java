package SuperCraft;

import Engine.*;

/**
 * Write a description of class Launcher here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Launcher {

    public static void main(String[] args) {
        System.out.println("Starting Game");
        Engine engine = new Engine("MarioTest");
        Level1 world = new Level1(engine);
        engine.AddRenderComponent(world);
        System.out.println("Finished adding World");
        engine.start();
    }
}

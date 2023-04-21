package se.liu.oschi129.animation.animationspace;

import java.util.Hashtable;
import se.liu.oschi129.animation.Animation;
import se.liu.oschi129.filenameparser.FilenameParser;

/**
 * This is a class contains all animations in the game.
 * Use AnimationSpace.add() to add an animation into the animation space and 
 * AnimationSpace.get() to return it from the collection. 
 * 
 * @author oschi129
 */
public abstract class AnimationSpace {

    private static Hashtable<String, AnimatedImage> space = new Hashtable<String, AnimatedImage>();

    public static void add(String name, AnimatedImage image) {
        space.put(name, image);
    }

    public static void add(String name, String regularExpression) {
        FilenameParser parser = new FilenameParser(regularExpression);
        AnimatedImage image = new AnimatedImage();
        for (String filename : parser.getFilenames()) image.addResource(filename);
        space.put(name, image);
    }

    public static void remove(String name) {
        space.remove(name);
    }

    public static Animation get(String name) {
        return space.get(name).getAnimation();
    }
}

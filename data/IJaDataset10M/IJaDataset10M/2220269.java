package de.syfy.project.engine;

import de.syfy.project.engine.errors.AnimationException;
import java.io.IOException;

/**
 *
 * @author Timo
 */
public class Animation {

    public static long STANDARD_DELAY = 200;

    private Texture[] textures;

    private long[] timing;

    private int pointer, timePointer;

    public void loadTextures(String dirPath, TextureLoader loader) {
        timing = new long[textures.length - 1];
        for (int i = 0; i < timing.length; i++) {
            timing[i] = STANDARD_DELAY;
        }
    }

    public void loadTextures(String[] texPaths, TextureLoader loader) {
        textures = new Texture[texPaths.length];
        for (int i = 0; i < texPaths.length; i++) {
            try {
                textures[i] = loader.getTexture(texPaths[i]);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        timing = new long[textures.length - 1];
        for (int i = 0; i < timing.length; i++) {
            timing[i] = STANDARD_DELAY;
        }
    }

    public void loadTextures(Texture[] tex) {
        textures = tex;
        timing = new long[textures.length - 1];
        for (int i = 0; i < timing.length; i++) {
            timing[i] = STANDARD_DELAY;
        }
    }

    public void setTiming(int index, long time) throws AnimationException {
        if (index >= timing.length) {
            throw new AnimationException("The index chosen for animation timing" + " is not within the bounds!");
        }
        timing[index] = time;
    }

    public Texture getNext() {
        Texture retTex = textures[pointer];
        pointer++;
        if (pointer >= textures.length) {
            pointer = 0;
        }
        return retTex;
    }

    public long getTiming() {
        return timing[timePointer];
    }

    public void setNextTiming() {
        timePointer++;
        if (timePointer >= timing.length) {
            timePointer = 0;
        }
    }
}

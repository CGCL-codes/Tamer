package org.javacraft.test.bouncingbox;

import org.javacraft.core.Task;

/**
 * @author edwardrf
 *
 * Updates the box's position
 */
public class BoxUpdateTask implements Task {

    private BouncingBox[] boxes;

    public BoxUpdateTask(BouncingBox[] boxes) {
        this.boxes = boxes;
    }

    public void execute(int task) {
        for (int x = 0; x < boxes.length; x++) boxes[x].move();
    }
}

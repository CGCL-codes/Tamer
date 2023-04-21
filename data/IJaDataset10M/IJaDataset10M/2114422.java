package org.xith3d.physics.collision;

import org.xith3d.loop.Updatable;
import org.xith3d.loop.UpdatingThread.TimingMode;

/**
 * Insert type comment here.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class CollisionCheckList implements Updatable {

    protected final CollisionEngine collEng;

    /**
     * {@inheritDoc}
     */
    public void update(long gameTime, long frameTime, TimingMode timingMode) {
    }

    public CollisionCheckList(CollisionEngine collEng) {
        this.collEng = collEng;
    }
}

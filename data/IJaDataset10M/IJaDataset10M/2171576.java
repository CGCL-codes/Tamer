package org.xith3d.render.loop;

/**
 * This interface is used to let an object know the average FPS count
 * of the last interval.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public interface FPSListener {

    /**
     * This method is called by the RenderLoop each FPS-counting interval
     * 
     * @param fps the average frames per second during the last interval
     */
    public void onFPSCountIntervalHit(double fps);
}

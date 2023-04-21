package net.jgf.jme.engine;

import java.util.Date;

/**
 * <p>EngineStats holds information about the number of frames rendered.</p>
 * 
 * @author jjmontes
 */
public class EngineStats {

    protected long renderedFrames;

    protected long cappedFrames;

    protected Date engineStart;

    public long getRenderedFrames() {
        return renderedFrames;
    }

    public void setRenderedFrames(long renderedFrames) {
        this.renderedFrames = renderedFrames;
    }

    public long getCappedFrames() {
        return cappedFrames;
    }

    public void setCappedFrames(long cappedFrames) {
        this.cappedFrames = cappedFrames;
    }

    public Date getEngineStart() {
        return engineStart;
    }

    public void setEngineStart(Date engineStart) {
        this.engineStart = engineStart;
    }

    @Override
    public String toString() {
        double cappedRatio = (((double) this.getCappedFrames()) / ((double) this.getRenderedFrames()));
        String toString = "JMEEngine stats [renderedFrames=" + this.getRenderedFrames() + ",cappedFrames=" + this.getCappedFrames() + ",ratio=" + cappedRatio + "]";
        return toString;
    }
}

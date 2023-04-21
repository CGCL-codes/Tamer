package org.capocaccia.cne.jaer.cne2011;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import net.sf.jaer.Description;
import net.sf.jaer.DevelopmentStatus;
import net.sf.jaer.chip.AEChip;
import net.sf.jaer.event.BasicEvent;
import net.sf.jaer.event.EventPacket;
import net.sf.jaer.event.OutputEventIterator;
import net.sf.jaer.eventprocessing.EventFilter2D;
import net.sf.jaer.graphics.FrameAnnotater;

/**
 * Example filter used in Capo Caccia Neuromorphic Cognition Workshop 2011.  
 * This filter computes a running mean event location and only transmits 
 * events within some chosen radius of the mean.
 * It also draws a rectangle over the mean location and optionally 
 * only transmits events that are within a desired radius of the mean
 * location.
 * 
 * @author tobi
 */
@Description("Example class for CNE 2011")
@DevelopmentStatus(DevelopmentStatus.Status.Experimental)
public class MeanEventLocationTracker extends EventFilter2D implements FrameAnnotater {

    float xmean, ymean;

    private float mixingRate = getFloat("mixingRate", 0.01f);

    private float radiusOfTransmission = getFloat("radiusOfTransmission", 10);

    public MeanEventLocationTracker(AEChip chip) {
        super(chip);
        setPropertyTooltip("mixingRate", "rate that mean location is updated in events. 1 means instantaneous and 0 freezes values");
        setPropertyTooltip("radiusOfTransmission", "radius in pixels around the mean that events are tranmitted out");
    }

    /** The main filtering method. It computes the mean location using an event-driven update of location and then
     * filters out events that are outside this location by more than the radius.
     * @param in input packet
     * @return output packet
     */
    @Override
    public EventPacket<?> filterPacket(EventPacket<?> in) {
        for (BasicEvent o : in) {
            xmean = (1 - mixingRate) * xmean + o.x * mixingRate;
            ymean = (1 - mixingRate) * ymean + o.y * mixingRate;
        }
        checkOutputPacketEventType(in);
        float maxsq = radiusOfTransmission * radiusOfTransmission;
        OutputEventIterator itr = out.outputIterator();
        for (BasicEvent e : in) {
            float dx = e.x - xmean;
            float dy = e.y - ymean;
            float sq = dx * dx + dy * dy;
            if (sq < maxsq) {
                BasicEvent outEvent = itr.nextOutput();
                outEvent.copyFrom(e);
            }
        }
        return out;
    }

    /** called when filter is reset
     * 
     */
    @Override
    public void resetFilter() {
        xmean = chip.getSizeX() / 2;
        ymean = chip.getSizeY() / 2;
    }

    @Override
    public void initFilter() {
    }

    /** Called after events are rendered. Here we just render something to show the mean location.
     * 
     * @param drawable the open GL surface. 
     */
    @Override
    public void annotate(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glColor4f(1, 1, 0, .3f);
        gl.glRectf(xmean - 4, ymean - 4, xmean + 4, ymean + 4);
    }

    /**
     * @return the mixingRate
     */
    public float getMixingRate() {
        return mixingRate;
    }

    /**
     * @param mixingRate the mixingRate to set
     */
    public void setMixingRate(float mixingRate) {
        float old = this.mixingRate;
        this.mixingRate = mixingRate;
        putFloat("mixingRate", mixingRate);
        getSupport().firePropertyChange("mixingRate", old, mixingRate);
    }

    /**
     * @return the radiusOfTransmission
     */
    public float getRadiusOfTransmission() {
        return radiusOfTransmission;
    }

    /**
     * @param radiusOfTransmission the radiusOfTransmission to set
     */
    public void setRadiusOfTransmission(float radiusOfTransmission) {
        float old = this.radiusOfTransmission;
        this.radiusOfTransmission = radiusOfTransmission;
        putFloat("radiusOfTransmission", radiusOfTransmission);
        getSupport().firePropertyChange("radiusOfTransmission", old, radiusOfTransmission);
    }
}

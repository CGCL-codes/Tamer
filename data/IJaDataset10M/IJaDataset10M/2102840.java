package ru.cos.sim.meters.data;

/**
 *
 * @author zroslaw
 */
public class SATSMeterInitData extends MeterData {

    private int linkId;

    private int segmentId;

    private float position;

    private float length;

    public int getLinkId() {
        return linkId;
    }

    public void setLinkId(int linkId) {
        this.linkId = linkId;
    }

    public int getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(int segmentId) {
        this.segmentId = segmentId;
    }

    public float getPosition() {
        return position;
    }

    public void setPosition(float position) {
        this.position = position;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getLength() {
        return length;
    }
}

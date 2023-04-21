package com.croftsoft.app.sieve.body;

import java.util.*;

/***********************************************************************
    * Implementation.
    * 
    * @version
    *   $Id: SieveSensorImp.java 152 2011-10-17 03:47:39Z croft $
    * @since
    *   2008-04-04
    * @author
    *   <a href="http://www.CroftSoft.com/">David Wallace Croft</a>
    ***********************************************************************/
public final class SieveSensorImp implements SieveSensor {

    public enum Sensor {

        OPTIC
    }

    private static final int SENSOR_LABEL_PREFIX_LENGTH = SENSOR_LABEL_PREFIX.length();

    private static final int OPTIC_WIDTH = 100, OPTIC_HEIGHT = 100, OPTIC_LENGTH = 3 * OPTIC_WIDTH * OPTIC_HEIGHT, OPTIC_OFFSET = 0, SENSOR_LENGTH = OPTIC_LENGTH;

    private final BitSet bitSet;

    public static String getSensorLabel(final Sensor sensor) {
        return SENSOR_LABEL_PREFIX + sensor.name();
    }

    public static String[] getSensorLabels() {
        final List<String> labelList = new ArrayList<String>();
        final Sensor[] sensorArray = Sensor.values();
        for (final Sensor sensor : sensorArray) {
            labelList.add(getSensorLabel(sensor));
        }
        return labelList.toArray(new String[0]);
    }

    public static Sensor getSensor(final String fullLabel) {
        if (!fullLabel.startsWith(SENSOR_LABEL_PREFIX)) {
            throw new IllegalArgumentException();
        }
        return Sensor.valueOf(fullLabel.substring(SENSOR_LABEL_PREFIX_LENGTH));
    }

    public SieveSensorImp() {
        bitSet = new BitSet(SENSOR_LENGTH);
    }

    public String[] getLabels() {
        return getSensorLabels();
    }

    public int getLength(String sensorFullLabel) {
        return getLength(getSensor(sensorFullLabel));
    }

    public int getOffset(String sensorFullLabel) {
        return getOffset(getSensor(sensorFullLabel));
    }

    public int getLength(final Sensor sensor) {
        switch(sensor) {
            case OPTIC:
                return OPTIC_LENGTH;
            default:
                throw new IllegalArgumentException(sensor.name());
        }
    }

    public int getOffset(final Sensor sensor) {
        switch(sensor) {
            case OPTIC:
                return OPTIC_OFFSET;
            default:
                throw new IllegalArgumentException(sensor.name());
        }
    }

    public int getOpticHeight() {
        return OPTIC_HEIGHT;
    }

    public int getOpticWidth() {
        return OPTIC_WIDTH;
    }

    public boolean isSpiking(final int index) {
        return bitSet.get(index);
    }

    public void clear() {
        bitSet.clear();
    }

    public void setSpiking(final BitSet spikingBitSet) {
        bitSet.or(spikingBitSet);
    }

    public void setSpiking(final int index) {
        bitSet.set(index);
    }

    public void setSpiking(final int index, final boolean spiking) {
        bitSet.set(index, spiking);
    }
}

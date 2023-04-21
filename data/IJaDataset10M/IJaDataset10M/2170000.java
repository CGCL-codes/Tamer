package de.gstpl.data.set;

/**
 * @author Peter Karich, peat_hal 'at' users 'dot' sourceforge 'dot' net
 */
class WeekRasterImpl1 implements IWeekRaster {

    private IRaster raster;

    private int iLength;

    public WeekRasterImpl1(int length) {
        iLength = length;
    }

    private synchronized IRaster getRaster() {
        if (raster == null) {
            raster = SetFactory.get().createBooleanRaster(iLength);
        }
        return raster;
    }

    @Override
    public void set(int timeslot, ERasterType type) {
        getRaster().setAssignment(timeslot, type != ERasterType.FORBIDDEN);
    }

    @Override
    public IRaster getAllowed() {
        return getRaster();
    }

    @Override
    public String toString() {
        return raster.toString();
    }
}

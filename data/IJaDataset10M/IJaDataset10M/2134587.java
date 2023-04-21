package eyetrackercalibrator.framemanaging;

import org.jfree.data.DomainOrder;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;

/**
 *
 * @author ruj
 */
public class IlluminationXYDataSet extends SyncXYDataSet {

    InformationDatabase informationDatabase = null;

    protected int offset = 0;

    private int lastItem = 0;

    /** Creates a new instance of IlluminationXYDataSet
     * @param infoDatabase 
     */
    public IlluminationXYDataSet(InformationDatabase infoDatabase) {
        this.informationDatabase = infoDatabase;
    }

    public DomainOrder getDomainOrder() {
        return DomainOrder.ASCENDING;
    }

    public int getItemCount(int i) {
        return lastItem;
    }

    /**
     * This X represent a frame 
     */
    public Number getX(int series, int item) {
        return new Integer(item);
    }

    /**
     * This X represent a frame 
     */
    public double getXValue(int series, int item) {
        return (double) (item);
    }

    /**
     * Return (x,y) coor of the pupil depending on series value
     * @param series 0 for X value of pupil 1 for Y value of pupil
     */
    public Number getY(int series, int item) {
        return new Double(getYValue(series, item));
    }

    public double getYValue(int series, int item) {
        double result = 0d;
        Double info = informationDatabase.getInfo(item + offset);
        if (info != null) {
            if (series == 0) {
                result = info;
            }
        }
        return result;
    }

    public int getSeriesCount() {
        return 1;
    }

    public Comparable getSeriesKey(int series) {
        if (series == 0) {
            return "Illumination";
        } else {
            return null;
        }
    }

    public int indexOf(Comparable seriesKey) {
        if (seriesKey.equals(getSeriesKey(0))) {
            return 0;
        } else {
            return -1;
        }
    }

    public void addChangeListener(DatasetChangeListener datasetChangeListener) {
    }

    public void removeChangeListener(DatasetChangeListener datasetChangeListener) {
    }

    public DatasetGroup getGroup() {
        return new DatasetGroup("Illumination data group");
    }

    public void setGroup(DatasetGroup datasetGroup) {
    }

    public int getLastItem() {
        return lastItem;
    }

    public void setLastItem(int lastItem) {
        this.lastItem = lastItem + 1;
    }
}

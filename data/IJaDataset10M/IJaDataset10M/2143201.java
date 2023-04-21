package eyetrackercalibrator.framemanaging;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Arrays;
import org.jfree.data.DomainOrder;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;

/**
 *
 * @author eeglab
 */
public class CornerDistanceXYDataSet extends SyncXYDataSet {

    FrameManager frameInfoManager = null;

    private double[][] buffer;

    private int firstItem = 0;

    private int bufferSize = 0;

    private static final int missingValueValue = 50;

    /**
     * Creates a new instance of CornerSimilarityXYDataSet
     * it is necessary that the FrameManager returns EyeViewFrameInfo
     * @param  frameInfoManager FrameManager which returns ScreenViewFrameInfo when
     *         getFrameInfo method is called
     * @param  Set the buffer size to greater to total domain you want
     *         to display
     */
    public CornerDistanceXYDataSet(FrameManager frameInfoManager, int bufferSize) {
        this.frameInfoManager = frameInfoManager;
        buffer = new double[4][];
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = new double[bufferSize];
            Arrays.fill(buffer[i], -1);
        }
        this.bufferSize = bufferSize;
    }

    /**
     * Return (x,y) coor of the similarity depending on series value
     * @item if frame number starting from 1
     * @return -1 When information is not avilable
     */
    public double getYValue(int series, int item) {
        if (item < firstItem + 1 || item >= firstItem + bufferSize) {
            rebuffer(item + 1);
        }
        if (item <= 1) {
            return -1;
        } else {
            int pos = (item + 1) % bufferSize;
            if (buffer[series][pos] >= 0) {
                return buffer[series][pos];
            } else {
                rebuffer(item + 1);
                return buffer[series][pos];
            }
        }
    }

    private void rebuffer(int pos) {
        int arrayPos = 0;
        int startPos = 0;
        int changes = 0;
        if (pos < firstItem) {
            changes = Math.min(firstItem - pos, bufferSize);
            arrayPos = pos % bufferSize;
            startPos = pos;
            firstItem = pos;
        } else if (pos >= firstItem + bufferSize) {
            changes = Math.min(pos - bufferSize - firstItem + 1, bufferSize);
            arrayPos = (pos - changes + 1) % bufferSize;
            startPos = pos - changes + 1;
            firstItem = firstItem + changes;
        } else if (pos <= this.frameSynchronizor.getTotalFrame()) {
            changes = 1;
            arrayPos = pos % bufferSize;
            startPos = pos;
        }
        Point2D bottomLeft = new Point(0, 0), bottomRight = new Point(0, 0), topLeft = new Point(0, 0), topRight = new Point(0, 0);
        ScreenViewFrameInfo info = (ScreenViewFrameInfo) frameInfoManager.getFrameInfo(this.frameSynchronizor.getSceneFrame(startPos - 1));
        if (info != null) {
            bottomLeft = info.getBottomLeft();
            bottomRight = info.getBottomRight();
            topLeft = info.getTopLeft();
            topRight = info.getTopRight();
        }
        Point2D currentBottomLeft = new Point(0, 0), currentBottomRight = new Point(0, 0), currentTopLeft = new Point(0, 0), currentTopRight = new Point(0, 0);
        for (int i = 0; i < changes; i++) {
            info = (ScreenViewFrameInfo) frameInfoManager.getFrameInfo(this.frameSynchronizor.getSceneFrame(startPos + i));
            if (info == null) {
                for (int j = 0; j < buffer.length; j++) {
                    buffer[j][arrayPos] = missingValueValue;
                }
                currentBottomLeft = null;
                currentBottomRight = null;
                currentTopLeft = null;
                currentTopRight = null;
            } else {
                currentBottomLeft = info.getBottomLeft();
                currentBottomRight = info.getBottomRight();
                currentTopLeft = info.getTopLeft();
                currentTopRight = info.getTopRight();
            }
            if (currentBottomLeft == null || bottomLeft == null) {
                buffer[ScreenViewFrameInfo.BOTTOMLEFT][arrayPos] = missingValueValue;
            } else {
                buffer[ScreenViewFrameInfo.BOTTOMLEFT][arrayPos] = Math.min(currentBottomLeft.distance(bottomLeft), missingValueValue);
            }
            if (currentBottomRight == null || bottomRight == null) {
                buffer[ScreenViewFrameInfo.BOTTOMRIGHT][arrayPos] = missingValueValue;
            } else {
                buffer[ScreenViewFrameInfo.BOTTOMRIGHT][arrayPos] = Math.min(currentBottomRight.distance(bottomRight), missingValueValue);
            }
            if (currentTopLeft == null || topLeft == null) {
                buffer[ScreenViewFrameInfo.TOPLEFT][arrayPos] = missingValueValue;
            } else {
                buffer[ScreenViewFrameInfo.TOPLEFT][arrayPos] = Math.min(currentTopLeft.distance(topLeft), missingValueValue);
            }
            if (currentTopRight == null || topRight == null) {
                buffer[ScreenViewFrameInfo.TOPRIGHT][arrayPos] = missingValueValue;
            } else {
                buffer[ScreenViewFrameInfo.TOPRIGHT][arrayPos] = Math.min(currentTopRight.distance(topRight), missingValueValue);
            }
            arrayPos = (arrayPos + 1) % bufferSize;
        }
    }

    public DatasetGroup getGroup() {
        return new DatasetGroup("Corner Distance");
    }

    public DomainOrder getDomainOrder() {
        return DomainOrder.ASCENDING;
    }

    public int getItemCount(int i) {
        return frameInfoManager.getTotalFrames();
    }

    public Number getX(int series, int item) {
        return new Integer(item + 1);
    }

    public double getXValue(int series, int item) {
        return (double) (item + 1);
    }

    public Number getY(int series, int item) {
        return new Double(getYValue(series, item));
    }

    /**
     * We have 4 series for each corner
     */
    public int getSeriesCount() {
        return 4;
    }

    public Comparable getSeriesKey(int series) {
        switch(series) {
            case ScreenViewFrameInfo.BOTTOMLEFT:
                return "Bottom left";
            case ScreenViewFrameInfo.BOTTOMRIGHT:
                return "Bottom right";
            case ScreenViewFrameInfo.TOPLEFT:
                return "Top left";
            case ScreenViewFrameInfo.TOPRIGHT:
                return "Top right";
            default:
                return null;
        }
    }

    public int indexOf(Comparable seriesKey) {
        if (seriesKey.equals("Bottom left")) {
            return ScreenViewFrameInfo.BOTTOMLEFT;
        } else if (seriesKey.equals("Bottom right")) {
            return ScreenViewFrameInfo.BOTTOMRIGHT;
        } else if (seriesKey.equals("Top left")) {
            return ScreenViewFrameInfo.TOPLEFT;
        } else if (seriesKey.equals("Top right")) {
            return ScreenViewFrameInfo.TOPRIGHT;
        }
        return -1;
    }

    public void addChangeListener(DatasetChangeListener datasetChangeListener) {
    }

    public void removeChangeListener(DatasetChangeListener datasetChangeListener) {
    }

    public void setGroup(DatasetGroup datasetGroup) {
    }
}

package imtek.optsuite.analysis;

import imtek.optsuite.acquisition.datastore.AcquiredData;
import imtek.optsuite.analysis.plot.PlotAcquiredData;
import imtek.optsuite.util.Array2DUtils;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import org.eclipse.swt.graphics.Image;

/**
 * Abstract partial implementation of DataAnalyser for common functionality.
 * 
 * @author Alexander Bieber <fleque@users.sourceforge.net>
 *
 */
public abstract class AbstractDataAnalyser implements DataAnalyser {

    private String id;

    private String name;

    private String description;

    private Image image;

    /**
	 * 
	 */
    public AbstractDataAnalyser() {
        super();
    }

    /**
	 * @see imtek.optsuite.analysis.DataAnalyser#setID(java.lang.String)
	 */
    public void setID(String id) {
        this.id = id;
    }

    /**
	 * @see imtek.optsuite.analysis.DataAnalyser#getID()
	 */
    public String getID() {
        return id;
    }

    /**
	 * @see imtek.optsuite.analysis.DataAnalyser#getDescription()
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * @see imtek.optsuite.analysis.DataAnalyser#setDescription(java.lang.String)
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * @see imtek.optsuite.analysis.DataAnalyser#getName()
	 */
    public String getName() {
        return name;
    }

    /**
	 * @see imtek.optsuite.analysis.DataAnalyser#setName(java.lang.String)
	 */
    public void setName(String name) {
        this.name = name;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image img) {
        this.image = img;
    }

    /**
	 * @see imtek.optsuite.analysis.DataAnalyser#saveAnalysedDataToFile(imtek.optsuite.acquisition.datastore.AcquiredData, java.io.File)
	 */
    public void saveAnalysedDataToFile(AcquiredData data, File file) {
    }

    public AcquiredData reduceToMaskedArea(AcquiredData data) {
        if (data.getMask() == null) return PlotAcquiredData.makeData((double[]) data.getData().clone(), data.getDimension()); else {
            Rectangle rect = data.getMask().getEnclosingRectangle();
            if (rect.x < 0) {
                rect.width = rect.width + rect.x;
                rect.x = 0;
            }
            if (rect.y < 0) {
                rect.height = rect.height + rect.y;
                rect.y = 0;
            }
            double remainW = data.getDimension().getWidth() - rect.x;
            double remainH = data.getDimension().getHeight() - rect.y;
            if (rect.width > remainW) rect.width = (int) remainW;
            if (rect.height > remainH) rect.height = (int) remainH;
            double[] result = new double[rect.width * rect.height];
            Dimension relDim = new Dimension(rect.width, rect.height);
            Point point = new Point(0, 0);
            for (int row = rect.y; row < rect.y + rect.height; row++) {
                for (int col = rect.x; col < rect.x + rect.width; col++) {
                    point.x = col;
                    point.y = row;
                    int relRow = row - rect.y;
                    int relCol = col - rect.x;
                    int idx = Array2DUtils.getArrayIndex(row + 1, col + 1, data.getDimension());
                    int relIdx = Array2DUtils.getArrayIndex(relRow + 1, relCol + 1, relDim);
                    if (data.getMask().contains(point)) result[relIdx] = data.getData()[idx]; else result[relIdx] = Double.NaN;
                }
            }
            return PlotAcquiredData.makeData(result, relDim);
        }
    }
}

package org.simbrain.util.projection;

import java.io.File;
import java.io.FileInputStream;
import org.apache.log4j.Logger;
import org.simbrain.util.projection.Settings.SammonAddMethod;
import com.Ostermiller.util.CSVParser;

/**
 * <b>ProjectionMethod</b> is a class describing a projection algorithm, which
 * contains a high dimensional dataset (an "upstairs") and a low-dimensional
 * projection of that high dimensional data (a "downstairs"). Classes which
 * extend this class provide different ways of projecting the high dimensional
 * space to the low dimensional space. This class provides general methods for
 * handling pairs of datasets and checking their integrity.
 */
public abstract class ProjectionMethod {

    /** Logger. */
    private Logger logger = Logger.getLogger(ProjectionMethod.class);

    /**
      * A set of hi-d datapoints, each of which is an array of doubles
      * The data to be projected.
      */
    protected Dataset upstairs;

    /**
     * A set of low-d datapoints, each of which is an array of doubles
     * The projection of the upstairs data.
     */
    protected Dataset downstairs;

    /**
     * Reference to an object which contains information which must persist between
     * re-inits of the projector.
     */
    protected Settings theSettings;

    /**
     * Initialize the projector with high and low-d data.
     *
     * @param up Reference to high dimensional dataset
     * @param down Reference to low dimensional dataset.  Pass NULL if not available
     */
    public void init(final Dataset up, final Dataset down) {
        if (logger == null) {
            logger = Logger.getLogger(ProjectionMethod.class);
        }
        logger.trace("In projector.init(up, down)");
        upstairs = up;
        downstairs = down;
        if (down == null) {
            downstairs = new Dataset(2, upstairs.getNumPoints());
            ProjectCoordinate initialProjection = new ProjectCoordinate(theSettings);
            initialProjection.init(upstairs, downstairs);
            initialProjection.project();
            downstairs.mirror(initialProjection.getDownstairs());
        }
        checkDatasets();
    }

    /**
     * Check validity of datasets.
     */
    public void checkDatasets() {
        if ((upstairs == null) || (downstairs == null) || (upstairs.getNumPoints() == 0)) {
            logger.debug("Could not invoke ProjectionMethod.init()");
            return;
        }
        compareDatasets();
    }

    /**
     * Sets persistent forms of data.
     */
    public void preSaveInit() {
        upstairs.preSaveInit();
        downstairs.preSaveInit();
    }

    /**
     * Updates datasets from persistent forms of data.
     */
    public void postOpenInit() {
        logger = Logger.getLogger(ProjectionMethod.class);
        upstairs.postOpenInit();
        downstairs.postOpenInit();
    }

    /**
     * Initilize a projector when only the dimension of the dataset is known.
     *
     * @param dims Dimensionality of the new dataset
     */
    public void init(final int dims) {
        Dataset up = new Dataset(dims);
        init(up, null);
    }

    /**
     * Add new high-d datapoints and reinitialize the datasets.
     *
     * @param theFile file containing the high-d data, forwarded to a dataset method
     */
    public void addUpstairs(final File theFile) {
        String[][] values = null;
        CSVParser theParser = null;
        try {
            theParser = new CSVParser(new FileInputStream(theFile), "", "", "#");
            values = theParser.getAllValues();
        } catch (Exception e) {
            System.out.println("Could not open file stream: " + e.toString());
        }
        String[] line;
        double[] dataPoint;
        for (int i = 0; i < values.length; i++) {
            line = values[i];
            dataPoint = new double[values[0].length];
            for (int j = 0; j < line.length; j++) {
                dataPoint[j] = Double.parseDouble(line[j]);
            }
            System.out.println("about to add datapoint");
            addDatapoint(dataPoint);
        }
        init(upstairs, downstairs);
    }

    /**
     * Check the integrity of the two datasets by checking: (1) That the low-d
     * set is at least 2 dimensions (2) That the low d space is lower
     * dimensional than the hi d space (3) That both datasets have the same
     * number of points.
     *
     * @return true if low dimensions are lower than hi dimensions and low
     *         dimension is less than one
     */
    public boolean compareDatasets() {
        if (downstairs.getDimensions() < 1) {
            System.out.println("WARNING: The dimension of the low dimensional data set");
            System.out.println("cannot be less than 1");
            return false;
        }
        if (downstairs.getDimensions() > upstairs.getDimensions()) {
            System.out.println("WARNING: The dimension of the low dimensional data set");
            System.out.println("cannot be greater than the dimension of the hi");
            System.out.println("dimensional data set.\n");
            System.out.println("hiDimension = " + upstairs.getDimensions() + "\n");
            System.out.println("lowD = " + downstairs.getDimensions());
            return false;
        }
        if (downstairs.getNumPoints() != upstairs.getNumPoints()) {
            System.out.println("WARNING: The number of points in the hi-d set (" + upstairs.getNumPoints() + "" + ") does not match that in the low-d set (" + downstairs.getNumPoints() + ")\n");
            return false;
        }
        return true;
    }

    /**
     * Perform operations necessay to project the data. For iterable functions this is just a stub.
     */
    public abstract void project();

    /**
     * Iterate, if it is iterable.  For non iterable-projectors this is just a stub
     *
     * @return the current error
     */
    public abstract double iterate();

    /**
     * @return true if this projection algorithm is iterable, false otherwise
     */
    public abstract boolean isIterable();

    /**
     * @return true if this projection algorithm accepts new new points, false
     *         otherwise
     */
    public abstract boolean isExtendable();

    /**
     * @return true true if current projector has a dialog.
     */
    public abstract boolean hasDialog();

    /**
     * @return the high-dimensional dataset
     */
    public Dataset getUpstairs() {
        return upstairs;
    }

    /**
     * @return the low dimensional dataset
     */
    public Dataset getDownstairs() {
        return downstairs;
    }

    /**
     * Add a datapoint to the upstairs dataset, and a corresponding point to the
     * downstairs dataset. Add the new point using the currently selected method
     *
     * @param point point to be added
     * @return true if the point was added, false otherwise.
     */
    public boolean addDatapoint(final double[] point) {
        double tolerance = theSettings.getTolerance();
        double[] newPoint = null;
        if (upstairs.addPoint(point, tolerance)) {
            if (point.length == 1) {
                newPoint = new double[] { point[0], 0 };
                downstairs.addPoint(newPoint);
                return true;
            }
            if (this instanceof ProjectSammon) {
                if (theSettings.getSammonAddMethod() == SammonAddMethod.REFRESH) {
                    newPoint = AddData.coordinate(theSettings.getHiD1(), theSettings.getHiD2(), point);
                } else if (theSettings.getSammonAddMethod() == SammonAddMethod.TRIANGULATE) {
                    newPoint = AddData.triangulate(upstairs, downstairs, point);
                } else if (theSettings.getSammonAddMethod() == SammonAddMethod.NN_SUBSPACE) {
                    newPoint = AddData.nnSubspace(upstairs, downstairs, point);
                }
            } else {
                newPoint = AddData.coordinate(theSettings.getHiD1(), theSettings.getHiD2(), point);
            }
            downstairs.addPoint(newPoint);
            this.project();
            return true;
        }
        return false;
    }

    /**
     * @return distance within which points are considered unique
     */
    public double getTolerance() {
        return theSettings.getTolerance();
    }

    /**
     * @param d distance within which points are considered unique
     */
    public void setTolerance(final double d) {
        theSettings.setTolerance(d);
    }

    /**
     * @return amount by which to perturb datapoints which overlap
     */
    public double getPerturbationAmount() {
        return theSettings.getPerturbationAmount();
    }

    /**
     * @param d amount by which to perturb datapoints which overlap
     */
    public void setPerturbationAmount(final double d) {
        theSettings.setPerturbationAmount(d);
    }

    /**
     * @return reference to object which contains preferences which persist  when the projector is re-initialized
     */
    public Settings getTheSettings() {
        return theSettings;
    }

    /**
     * @param settings reference to object which contains preferences which persist  when the projector is
     *        re-initialized
     */
    public void setTheSettings(final Settings settings) {
        theSettings = settings;
    }

    /**
     * @param downstairs Sets projector downstairs.
     */
    public void setDownstairs(final Dataset downstairs) {
        this.downstairs = downstairs;
    }

    /**
     * @param upstairs Sets projector upstairs.
     */
    public void setUpstairs(final Dataset upstairs) {
        this.upstairs = upstairs;
    }
}

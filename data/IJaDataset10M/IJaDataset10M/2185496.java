package data.dataset;

import data.image.Digit;

/**
 * @author TOSHIBA
 *
 */
public abstract class DataSetGenerator {

    protected int status = DataBaseConnector.TRAIN;

    public abstract Digit getSingleImage(String st);
}

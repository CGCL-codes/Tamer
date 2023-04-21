package ca.nanometrics.gflot.client;

/**
 * Handler used to manipulate a series and its data.
 *
 * @author Alexander De Leon
 */
public class SeriesHandler {

    private final Series series;

    private SeriesData data;

    public SeriesHandler(Series series, SeriesData data) {
        this.series = series;
        this.data = data;
    }

    /**
     * Add a datapoint
     *
     * @param datapoint datapoint to add
     */
    public void add(DataPoint datapoint) {
        data.add(datapoint);
    }

    /**
     * Clear data
     */
    public void clear() {
        data.clear();
    }

    /**
     * Set if the series is visible or not
     *
     * @param visible true if the series is visible, false otherwise
     */
    public void setVisible(boolean visible) {
        if (visible) {
            series.setData(data);
        } else {
            series.setData(null);
        }
    }

    /**
     * @return true if the series is visible, false otherwise
     */
    public boolean isVisible() {
        return null != series.getData();
    }

    /**
     * @return the series associated to this handler
     */
    public Series getSeries() {
        return series;
    }

    /**
     * @return the data associated to this handler
     */
    public SeriesData getData() {
        return data;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof SeriesHandler) {
            return series.equals(((SeriesHandler) obj).series);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return series.hashCode();
    }

    void setData(SeriesData newData) {
        data = newData;
        if (isVisible()) {
            series.setData(data);
        }
    }
}

package uk.ac.rdg.resc.ncwms.controller;

import java.io.IOException;
import java.util.List;
import org.joda.time.DateTime;
import uk.ac.rdg.resc.ncwms.coords.HorizontalGrid;
import uk.ac.rdg.resc.ncwms.exceptions.InvalidDimensionValueException;
import uk.ac.rdg.resc.ncwms.exceptions.LayerNotDefinedException;
import uk.ac.rdg.resc.ncwms.usagelog.UsageLogEntry;
import uk.ac.rdg.resc.ncwms.util.WmsUtils;
import uk.ac.rdg.resc.ncwms.wms.Dataset;
import uk.ac.rdg.resc.ncwms.wms.Layer;
import uk.ac.rdg.resc.ncwms.wms.ScalarLayer;

/**
 * Partial implementation of the {@link ServerConfig} interface, providing
 * default implementations of some methods
 * @author Jon
 */
public abstract class AbstractServerConfig implements ServerConfig {

    /**
     * {@inheritDoc}
     * <p>This implementation assumes that the unique layer name is comprised
     * of the {@link Dataset} id and the {@link Layer} id, separated by a forward
     * slash.  It calls {@link #getDatasetById(java.lang.String)} to retrieve
     * the Dataset, then {@link Dataset#
     * @throws LayerNotDefinedException if the given name does not match a layer
     * on this server
     */
    @Override
    public Layer getLayerByUniqueName(String uniqueLayerName) throws LayerNotDefinedException {
        try {
            String[] els = WmsUtils.parseUniqueLayerName(uniqueLayerName);
            Dataset ds = this.getDatasetById(els[0]);
            if (ds == null) throw new NullPointerException();
            Layer layer = ds.getLayerById(els[1]);
            if (layer == null) throw new NullPointerException();
            return layer;
        } catch (Exception e) {
            throw new LayerNotDefinedException(uniqueLayerName);
        }
    }

    /**
     * {@inheritDoc}
     * <p>This implementation simply defers to {@link
     * ScalarLayer#readPointList(org.joda.time.DateTime, double,
     * uk.ac.rdg.resc.ncwms.datareader.PointList) layer.readPointList()},
     * ignoring the usage log entry.  No data are cached.</p>
     */
    @Override
    public List<Float> readDataGrid(ScalarLayer layer, DateTime dateTime, double elevation, HorizontalGrid grid, UsageLogEntry usageLogEntry) throws InvalidDimensionValueException, IOException {
        return layer.readPointList(dateTime, elevation, grid);
    }
}

package es.prodevelop.cit.gvsig.arcims.fmap.drivers;

import java.awt.geom.Rectangle2D;
import org.gvsig.remoteClient.arcims.exceptions.ArcImsException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.layers.FBitSet;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.fmap.layers.VectorialAdapter;

public class ArcImsVectorialAdapter extends VectorialAdapter {

    private IFeature[] testArray;

    public ArcImsVectorialAdapter(FMapFeatureArcImsDriver drv) {
        setDriver(drv);
    }

    public IGeometry getShape(int index) throws ReadDriverException {
        return getDriver().getShape(index);
    }

    public int getShapeCount() throws ReadDriverException {
        return getDriver().getShapeCount();
    }

    public Rectangle2D getFullExtent() throws ReadDriverException {
        return getDriver().getFullExtent();
    }

    public int getShapeType() throws ReadDriverException {
        return getDriver().getShapeType();
    }

    /**
     * This method is used when the data source is a proper database that needs
     * initialization.
     */
    public void start() throws ReadDriverException {
        ((FMapFeatureArcImsDriver) getDriver()).getRecordSet().start();
    }

    /**
     * This method is used when the data source is a proper database that needs
     * to be closed after use.
     */
    public void stop() throws ReadDriverException {
        ((FMapFeatureArcImsDriver) driver).getRecordSet().stop();
    }

    public SelectableDataSource getRecordset() throws ReadDriverException {
        return ((FMapFeatureArcImsDriver) driver).getRecordSet();
    }

    public void requestFeatureAttributes(FBitSet fbs) throws ArcImsException {
        ((FMapFeatureArcImsDriver) driver).requestFeatureAttributes(fbs);
    }
}

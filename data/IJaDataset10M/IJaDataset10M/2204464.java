package com.iver.cit.gvsig.fmap.layers;

import org.cresques.cts.IProjection;
import com.hardcode.driverManager.DriverLoadException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.exceptions.layers.DriverLayerException;
import com.iver.cit.gvsig.exceptions.layers.LegendLayerException;
import com.iver.cit.gvsig.exceptions.layers.LoadLayerException;
import com.iver.cit.gvsig.exceptions.layers.NameLayerException;
import com.iver.cit.gvsig.exceptions.layers.ProjectionLayerException;
import com.iver.cit.gvsig.exceptions.layers.XMLLayerException;
import com.iver.cit.gvsig.fmap.crs.CRSFactory;
import com.iver.cit.gvsig.fmap.drivers.DefaultJDBCDriver;
import com.iver.cit.gvsig.fmap.drivers.IVectorialDatabaseDriver;
import com.iver.cit.gvsig.fmap.drivers.WithDefaultLegend;
import com.iver.cit.gvsig.fmap.rendering.IVectorLegend;
import com.iver.cit.gvsig.fmap.rendering.LegendFactory;
import com.iver.cit.gvsig.fmap.rendering.styling.labeling.AttrInTableLabelingStrategy;
import com.iver.cit.gvsig.fmap.rendering.styling.labeling.ILabelingStrategy;
import com.iver.utiles.XMLEntity;

public class FLayerVectorialDB extends FLyrVect {

    private boolean loaded = false;

    private IVectorialDatabaseDriver dbDriver = null;

    public void setProjectionByName(String projectionName) throws Exception {
        IProjection proj = CRSFactory.getCRS(projectionName);
        if (proj == null) {
            throw new Exception("No se ha encontrado la proyeccion: " + projectionName);
        }
        this.setProjection(proj);
    }

    public void setDriver(IVectorialDatabaseDriver driver) {
        this.dbDriver = driver;
    }

    public void setDriverByName(String driverName) throws ReadDriverException {
        try {
            this.setDriver((IVectorialDatabaseDriver) LayerFactory.getDM().getDriver(driverName));
        } catch (DriverLoadException e) {
            throw new ReadDriverException(getName(), e);
        }
    }

    public IVectorialDatabaseDriver getDriver() {
        return this.dbDriver;
    }

    public void wakeUp() throws LoadLayerException {
        if (!loaded) {
            this.load();
        }
    }

    public void load() throws LoadLayerException {
        if (this.getName() == null || this.getName().length() == 0) {
            this.setAvailable(false);
            throw new NameLayerException(this.getName(), null);
        }
        if (this.dbDriver == null) {
            this.setAvailable(false);
            throw new DriverLayerException(this.getName(), null);
        }
        if (this.getProjection() == null) {
            this.setAvailable(false);
            throw new ProjectionLayerException(this.getName(), null);
        }
        try {
            ((DefaultJDBCDriver) this.dbDriver).load();
        } catch (ReadDriverException e1) {
            throw new LoadLayerException(this.getName(), e1);
        }
        VectorialDBAdapter dbAdapter = new VectorialDBAdapter();
        dbAdapter.setDriver(this.dbDriver);
        this.setSource(dbAdapter);
        try {
            this.putLoadSelection();
            this.putLoadLegend();
            this.initializeLegendDefault();
        } catch (XMLException e) {
            this.setAvailable(false);
            throw new XMLLayerException(this.getName(), e);
        } catch (LegendLayerException e) {
            this.setAvailable(false);
            throw new LegendLayerException(this.getName(), e);
        } catch (ReadDriverException e) {
            this.setAvailable(false);
            throw new LoadLayerException(this.getName(), e);
        }
        this.cleanLoadOptions();
    }

    private void initializeLegendDefault() throws ReadDriverException, LegendLayerException {
        if (this.getLegend() == null) {
            if (this.getRecordset().getDriver() instanceof WithDefaultLegend) {
                WithDefaultLegend aux = (WithDefaultLegend) this.getRecordset().getDriver();
                this.setLegend((IVectorLegend) aux.getDefaultLegend());
                ILabelingStrategy labeler = aux.getDefaultLabelingStrategy();
                if (labeler instanceof AttrInTableLabelingStrategy) {
                    ((AttrInTableLabelingStrategy) labeler).setLayer(this);
                }
                this.setLabelingStrategy(labeler);
            } else {
                this.setLegend(LegendFactory.createSingleSymbolLegend(this.getShapeType()));
            }
        }
    }

    public void setXMLEntity(XMLEntity xml) throws XMLException {
        IProjection proj = null;
        if (xml.contains("proj")) {
            proj = CRSFactory.getCRS(xml.getStringProperty("proj"));
        } else {
            proj = this.getMapContext().getViewPort().getProjection();
        }
        this.setName(xml.getName());
        this.setProjection(proj);
        String driverName = xml.getStringProperty("db");
        IVectorialDatabaseDriver driver;
        try {
            driver = (IVectorialDatabaseDriver) LayerFactory.getDM().getDriver(driverName);
            driver.setXMLEntity(xml.getChild(2));
            this.setDriver(driver);
        } catch (Exception e) {
            throw new XMLException(e);
        }
        super.setXMLEntityNew(xml);
    }
}

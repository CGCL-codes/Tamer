package com.iver.cit.gvsig.geoprocess.impl.dissolve.fmap;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.cresques.cts.ICoordTrans;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.data.driver.DriverException;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileReadException;
import com.iver.cit.gvsig.exceptions.visitors.ProcessVisitorException;
import com.iver.cit.gvsig.exceptions.visitors.StartVisitorException;
import com.iver.cit.gvsig.exceptions.visitors.StopVisitorException;
import com.iver.cit.gvsig.exceptions.visitors.VisitorException;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.v02.FConverter;
import com.iver.cit.gvsig.fmap.drivers.DriverIOException;
import com.iver.cit.gvsig.fmap.layers.FBitSet;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.fmap.layers.layerOperations.AlphanumericData;
import com.iver.cit.gvsig.fmap.layers.layerOperations.VectorialData;
import com.iver.cit.gvsig.fmap.operations.strategies.FeatureVisitor;
import com.iver.cit.gvsig.fmap.operations.strategies.Strategy;
import com.iver.cit.gvsig.fmap.operations.strategies.StrategyManager;
import com.iver.cit.gvsig.geoprocess.core.fmap.FeatureProcessor;
import com.iver.cit.gvsig.geoprocess.core.fmap.GeoprocessException;
import com.iver.cit.gvsig.geoprocess.core.util.JTSFacade;
import com.iver.utiles.swing.threads.Cancellable;
import com.iver.utiles.swing.threads.CancellableMonitorable;
import com.vividsolutions.jts.geom.Geometry;

/**
 * <p>
 * Dissolve features in base of a given dissolve criteria.
 * </p>
 * By 'dissolving' we understand the union of the geometry of many features
 *
 * @author azabala
 *
 */
public class FeatureDissolver {

    public static final int ALPHANUMERIC_DISSOLVE = 0;

    public static final int SPATIAL_DISSOLVE = 1;

    private int dissolveType = ALPHANUMERIC_DISSOLVE;

    /**
	 * Fetches attributes of disolved layer features
	 */
    protected SelectableDataSource recordset;

    protected ICoordTrans ct;

    /**
	 * Is used to do spatial querys (looking for adjacent polygons to visited
	 * feature geometry
	 */
    protected FLyrVect dissolvedLayer;

    protected int geometryType;

    /**
	 * It marks all features that have already been dissolved (to avoid process
	 * them in subsecuent steps)
	 *
	 */
    protected FBitSet dissolvedGeometries;

    /**
	 * If its not null, the dissolver will only dissolve the selected features
	 */
    protected FBitSet selection;

    /**
	 * Relates a numerical field name with its sumarization functions
	 */
    protected Map numericField_sumarizeFunction;

    /**
	 * Processes results of dissolve operations (save them in a file, or cache
	 * them in memory, etc)
	 */
    protected FeatureProcessor featureProcessor;

    /**
	 * Index of the result
	 */
    protected int fid = 0;

    /**
	 * It decides if two features must be dissolved, and builds the feature
	 * resulting of the dissolution
	 */
    protected IDissolveCriteria dissolveCriteria;

    /**
	 *
	 * @param processor
	 * @param layer
	 * @throws GeoprocessException
	 */
    public FeatureDissolver(FeatureProcessor processor, FLyrVect layer, Map numericField_sumFunction, IDissolveCriteria criteria, int dissolveType) throws GeoprocessException {
        this.featureProcessor = processor;
        this.numericField_sumarizeFunction = numericField_sumFunction;
        this.dissolveCriteria = criteria;
        this.dissolveType = dissolveType;
        dissolvedGeometries = new FBitSet();
        if (layer instanceof AlphanumericData && layer instanceof VectorialData) {
            try {
                dissolvedLayer = (FLyrVect) layer;
                geometryType = dissolvedLayer.getShapeType();
                recordset = ((AlphanumericData) layer).getRecordset();
                ct = dissolvedLayer.getCoordTrans();
                featureProcessor.start();
            } catch (ReadDriverException e) {
                throw new GeoprocessException("Error al acceder al recordset de la capa " + layer.getName(), e);
            } catch (StartVisitorException e) {
                throw new GeoprocessException("Error al preparar donde se van a escribir los resultados del dissolve de la capa " + layer.getName(), e);
            }
        } else {
            throw new GeoprocessException("La capa a dissolver debe ser VectorialData y AlphanumericData");
        }
    }

    public void setDissolveCriteria(IDissolveCriteria criteria) {
        this.dissolveCriteria = criteria;
    }

    public void setDissolvedAttributesInfo(Map numericField_sumFunction) {
        this.numericField_sumarizeFunction = numericField_sumFunction;
    }

    public int getNumProcessedGeometries() {
        return dissolvedGeometries.cardinality();
    }

    public void dissolve(CancellableMonitorable cancel) throws GeoprocessException {
        try {
            ReadableVectorial va = dissolvedLayer.getSource();
            va.start();
            for (int i = 0; i < va.getShapeCount(); i++) {
                if (verifyCancelation(cancel, va)) {
                    this.featureProcessor.finish();
                    return;
                }
                if (selection != null) {
                    if (!selection.get(i)) continue;
                }
                if (!dissolvedGeometries.get(i)) {
                    try {
                        if (dissolveType == SPATIAL_DISSOLVE) process(i, va, cancel); else if (dissolveType == ALPHANUMERIC_DISSOLVE) processAlphanumeric(i, va, cancel);
                    } catch (ReadDriverException e) {
                        throw new GeoprocessException("Error accediendo a datos durante dissolve", e);
                    } catch (ProcessVisitorException e) {
                        throw new GeoprocessException("Error procesando datos durante dissolve", e);
                    } catch (VisitorException e) {
                        throw new GeoprocessException("Error accediendo a datos durante dissolve", e);
                    }
                }
            }
            va.stop();
            this.featureProcessor.finish();
        } catch (ReadDriverException e) {
            e.printStackTrace();
        } catch (StopVisitorException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Verifies cancelation events, and return a boolean flag if processes must
	 * be stopped for this cancelations events.
	 *
	 * @param cancel
	 * @param va
	 * @param visitor
	 * @return
	 * @throws DriverIOException
	 */
    protected boolean verifyCancelation(Cancellable cancel, ReadableVectorial va) {
        if (cancel != null) {
            if (cancel.isCanceled()) {
                try {
                    va.stop();
                } finally {
                    return true;
                }
            }
        }
        return false;
    }

    class DissolveVisitor implements FeatureVisitor {

        IDissolveCriteria criteria;

        int index1;

        Stack stack;

        List geometries;

        ReadableVectorial va;

        FunctionSummarizer sumarizer;

        CancellableMonitorable cancel;

        DissolveVisitor(IDissolveCriteria criteria, int index1, Stack stack, List geometries, ReadableVectorial va, FunctionSummarizer sumarizer, CancellableMonitorable cancel) {
            this.criteria = criteria;
            this.index1 = index1;
            this.stack = stack;
            this.geometries = geometries;
            this.va = va;
            this.sumarizer = sumarizer;
            this.cancel = cancel;
        }

        public void visit(IGeometry g, int index2) throws VisitorException, ProcessVisitorException {
            if (g == null) return;
            if (verifyCancelation(cancel, va)) {
                featureProcessor.finish();
                return;
            }
            if (index1 == index2) {
                return;
            }
            if (selection != null) {
                if (!selection.get(index2)) return;
            }
            if (dissolvedGeometries.get(index2)) {
                return;
            }
            if (criteria.verifyIfDissolve(index1, index2)) {
                if (stack != null) stack.push(new Integer(index2));
                try {
                    if (criteria instanceof ISpatialDissolveCriteria) {
                        geometries.add(((ISpatialDissolveCriteria) criteria).getSecondGeometry().toJTSGeometry());
                    } else {
                        IGeometry g2 = va.getShape(index2);
                        if (ct != null) g2.reProject(ct);
                        geometries.add(g2.toJTSGeometry());
                    }
                    sumarizer.applySumarizeFunction(index2);
                } catch (ReadDriverException e) {
                    throw new ProcessVisitorException(recordset.getName(), e, "Error durante lectura de geometria en dissolve");
                }
                if (stack == null) dissolvedGeometries.set(index2);
            }
        }

        public String getProcessDescription() {
            return "";
        }

        public void stop(FLayer layer) throws VisitorException {
        }

        public boolean start(FLayer layer) throws StartVisitorException {
            return true;
        }
    }

    /**
	 * @param index1
	 * @param va
	 * @param cancel
	 * @throws DriverException
	 * @throws ExpansionFileReadException
	 * @throws ReadDriverException
	 * @throws VisitorException
	 * @throws ProcessWriterException
	 * @throws IOException
	 * @throws DriverIOException
	 */
    public void processAlphanumeric(int index1, ReadableVectorial va, CancellableMonitorable cancel) throws ReadDriverException, ExpansionFileReadException, VisitorException {
        dissolvedGeometries.set(index1);
        Strategy strategy = StrategyManager.getStrategy(dissolvedLayer);
        ArrayList geometries = new ArrayList();
        IGeometry g1 = va.getShape(index1);
        if (g1 == null) return;
        if (ct != null) g1.reProject(ct);
        geometries.add(g1.toJTSGeometry());
        if (dissolveCriteria instanceof ISpatialDissolveCriteria) {
            ((ISpatialDissolveCriteria) dissolveCriteria).setCoordTrans(ct);
            ((ISpatialDissolveCriteria) dissolveCriteria).setFirstGeometry(g1);
        }
        FunctionSummarizer sumarizer = new FunctionSummarizer(numericField_sumarizeFunction, recordset);
        DissolveVisitor visitor = new DissolveVisitor(dissolveCriteria, index1, null, geometries, va, sumarizer, cancel);
        strategy.process(visitor);
        IGeometry newGeometry = FConverter.jts_to_igeometry(union2(geometries));
        List sumarizedValues = sumarizer.getValues();
        IFeature dissolvedFeature = null;
        if (sumarizedValues != null || sumarizedValues.size() != 0) {
            dissolvedFeature = dissolveCriteria.getFeatureBuilder().createFeature(newGeometry, sumarizedValues, fid, index1);
        } else {
            dissolvedFeature = dissolveCriteria.getFeatureBuilder().createFeature(newGeometry, index1, fid);
        }
        if (dissolvedFeature.getGeometry() != null) {
            fid++;
            featureProcessor.processFeature(dissolvedFeature);
        }
        dissolveCriteria.clear();
    }

    class SpatialDissolveVisitor implements FeatureVisitor {

        IDissolveCriteria criteria;

        Geometry geom1;

        StackEntry entry1;

        Stack stack;

        List geometries;

        ReadableVectorial va;

        FunctionSummarizer sumarizer;

        CancellableMonitorable cancel;

        SpatialDissolveVisitor(IDissolveCriteria criteria, Geometry geo1, StackEntry entry1, Stack stack, List geometries, ReadableVectorial va, FunctionSummarizer sumarizer, CancellableMonitorable cancel) {
            this.criteria = criteria;
            this.geom1 = geo1;
            this.entry1 = entry1;
            this.stack = stack;
            this.geometries = geometries;
            this.va = va;
            this.sumarizer = sumarizer;
            this.cancel = cancel;
        }

        void setSeed(StackEntry entry, Geometry jtsGeo) {
            this.entry1 = entry;
            this.geom1 = jtsGeo;
        }

        public void visit(IGeometry g, int index2) throws VisitorException, ProcessVisitorException {
            if (g == null) return;
            if (verifyCancelation(cancel, va)) {
                featureProcessor.finish();
                return;
            }
            if (entry1.index == index2) {
                return;
            }
            if (selection != null) {
                if (!selection.get(index2)) return;
            }
            if (dissolvedGeometries.get(index2)) {
                return;
            }
            if (criteria instanceof ISpatialDissolveCriteria) {
                ((ISpatialDissolveCriteria) criteria).setSecondGeometry(g);
            }
            if (criteria.verifyIfDissolve(entry1.index, index2)) {
                StackEntry entry2 = new StackEntry();
                entry2.g = g;
                entry2.index = index2;
                stack.push(entry2);
                System.out.println("Anado " + index2 + " al stack");
                dumpStack(stack);
                if (criteria instanceof ISpatialDissolveCriteria) {
                    ISpatialDissolveCriteria c = (ISpatialDissolveCriteria) criteria;
                    geometries.add(c.getSecondJts());
                } else geometries.add(g.toJTSGeometry());
                try {
                    sumarizer.applySumarizeFunction(index2);
                } catch (ReadDriverException e) {
                    throw new ProcessVisitorException(recordset.getName(), e, "Error al aplicar la funcion de sumarizacion en dissolve");
                }
                dissolvedGeometries.set(index2);
            }
        }

        public String getProcessDescription() {
            return "";
        }

        public void stop(FLayer layer) throws VisitorException {
        }

        public boolean start(FLayer layer) throws StartVisitorException {
            return true;
        }
    }

    class StackEntry {

        public IGeometry g;

        public int index;
    }

    /**
	 * Processes the given feature looking for features to dissolve with. The
	 * criteria to decide if dissolve two features is given by
	 * IDissolveCriteria.
	 *
	 * @param criteria
	 *            decides if dissolve two features
	 * @param index1
	 *            index of feature we are processing
	 * @param va
	 *            it reads geometry of features
	 * @param cancel
	 *            listen cancelations
	 * @throws ExpansionFileReadException
	 * @throws ReadDriverException
	 * @throws VisitorException
	 * @throws ProcessWriterException
	 * @throws DriverException
	 * @throws IOException
	 * @throws DriverIOException
	 */
    public void process(int index1, ReadableVectorial va, CancellableMonitorable cancel) throws ReadDriverException, ExpansionFileReadException, VisitorException {
        if (dissolvedGeometries.get(index1)) return;
        Strategy strategy = StrategyManager.getStrategy(dissolvedLayer);
        IGeometry g1 = va.getShape(index1);
        if (g1 == null) return;
        if (ct != null) g1.reProject(ct);
        StackEntry entry = new StackEntry();
        entry.g = g1;
        entry.index = index1;
        Stack stack = new Stack();
        stack.push(entry);
        ArrayList geometries = new ArrayList();
        Geometry jtsGeo = g1.toJTSGeometry();
        geometries.add(jtsGeo);
        if (dissolveCriteria instanceof ISpatialDissolveCriteria) {
            ((ISpatialDissolveCriteria) dissolveCriteria).setCoordTrans(ct);
            ((ISpatialDissolveCriteria) dissolveCriteria).setFirstGeometry(g1);
        }
        FunctionSummarizer sumarizer = new FunctionSummarizer(numericField_sumarizeFunction, recordset);
        SpatialDissolveVisitor visitor = new SpatialDissolveVisitor(dissolveCriteria, jtsGeo, entry, stack, geometries, va, sumarizer, cancel);
        while (stack.size() != 0) {
            dumpStack(stack);
            StackEntry sEntry = (StackEntry) stack.pop();
            dissolvedGeometries.set(sEntry.index);
            Rectangle2D rect = sEntry.g.getBounds2D();
            if (ct != null) rect = ct.convert(rect);
            double xmin = rect.getMinX();
            double ymin = rect.getMinY();
            double xmax = rect.getMaxX();
            double ymax = rect.getMaxY();
            double magnify = 15d;
            Rectangle2D query = new Rectangle2D.Double(xmin - magnify, ymin - magnify, (xmax - xmin) + magnify, (ymax - ymin) + magnify);
            Geometry jts = sEntry.g.toJTSGeometry();
            visitor.setSeed(sEntry, jts);
            strategy.process(visitor, query);
        }
        IGeometry newGeometry = FConverter.jts_to_igeometry(union2(geometries));
        List sumarizedValues = sumarizer.getValues();
        IFeature dissolvedFeature = null;
        if (sumarizedValues != null || sumarizedValues.size() != 0) {
            dissolvedFeature = dissolveCriteria.getFeatureBuilder().createFeature(newGeometry, sumarizedValues, fid, index1);
        } else {
            dissolvedFeature = dissolveCriteria.getFeatureBuilder().createFeature(newGeometry, index1, fid);
        }
        fid++;
        featureProcessor.processFeature(dissolvedFeature);
        dissolveCriteria.clear();
    }

    private void dumpStack(Stack stack) {
        Enumeration e = stack.elements();
        System.out.println("#####Elementos por procesar");
        while (e.hasMoreElements()) {
            System.out.println("#######-     " + ((StackEntry) e.nextElement()).index);
        }
    }

    protected Geometry union2(List geometries) {
        Geometry union = null;
        Geometry[] geom = new Geometry[geometries.size()];
        geometries.toArray(geom);
        union = JTSFacade.union(geom, geometryType);
        return union;
    }

    public void setSelection(FBitSet selection) {
        this.selection = selection;
    }
}

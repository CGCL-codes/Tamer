package com.iver.cit.gvsig.geoprocess.impl.dissolve.fmap;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.data.driver.DriverException;
import com.hardcode.gdbms.engine.values.NumericValue;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileReadException;
import com.iver.cit.gvsig.exceptions.visitors.ProcessVisitorException;
import com.iver.cit.gvsig.exceptions.visitors.StartVisitorException;
import com.iver.cit.gvsig.exceptions.visitors.VisitorException;
import com.iver.cit.gvsig.fmap.core.DefaultFeature;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.v02.FConverter;
import com.iver.cit.gvsig.fmap.layers.FBitSet;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.fmap.layers.layerOperations.AlphanumericData;
import com.iver.cit.gvsig.fmap.layers.layerOperations.VectorialData;
import com.iver.cit.gvsig.fmap.operations.strategies.FeatureVisitor;
import com.iver.cit.gvsig.fmap.operations.strategies.Strategy;
import com.iver.cit.gvsig.geoprocess.core.fmap.FeatureProcessor;
import com.iver.cit.gvsig.geoprocess.core.fmap.SummarizationFunction;
import com.iver.cit.gvsig.geoprocess.core.util.JTSFacade;
import com.vividsolutions.jts.geom.Geometry;

/**
 * <p>
 * This FeatureVisitor processes each geometry of a polygon layer, looks for
 * adjacent polygons and verify values of a dissolve field. If these values are
 * coincident, it creates a new polygon by unioning original polygons. Also
 * applies a sumarization function to numeric fields of original features.
 * </p>
 *
 * @author azabala
 *
 */
public class DissolveVisitor implements FeatureVisitor {

    /**
	 * Allows to get attributes of disolved layer features
	 */
    protected SelectableDataSource recordset;

    /**
	 * Is used to do spatial querys (looking for adjacent polygons to visited
	 * feature geometry
	 */
    protected FLyrVect dissolvedLayer;

    protected int geometryType;

    /**
	 * Field which adjacent features must have the same value to dissolve them
	 */
    protected String dissolveField;

    /**
	 * It marks all features that have already been dissolved (to avoid process
	 * them in subsecuent steps)
	 *
	 */
    protected FBitSet dissolvedGeometries;

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
	 * Strategy of the layer we want to dissolve
	 */
    protected Strategy strategy;

    /**
	 * Index of the result
	 */
    protected int fid = 0;

    /**
	 * Constructor
	 * @param dissolveField
	 * @param processor
	 */
    public DissolveVisitor(String dissolveField, FeatureProcessor processor) {
        this.dissolveField = dissolveField;
        this.featureProcessor = processor;
        dissolvedGeometries = new FBitSet();
    }

    public int getNumProcessedGeometries() {
        return dissolvedGeometries.cardinality();
    }

    public FBitSet getDissolvedGeometries() {
        return dissolvedGeometries;
    }

    public void setDissolvedAttributesInfo(Map numericField_sumFunction) {
        this.numericField_sumarizeFunction = numericField_sumFunction;
    }

    public void visit(IGeometry g, int index) throws VisitorException, ProcessVisitorException {
        if (g == null) return;
        if (!dissolvedGeometries.get(index)) {
            Stack toDissol = new Stack();
            DissolvedFeature feature;
            try {
                feature = createFeature(g, index);
                toDissol.push(feature);
                ArrayList geometries = new ArrayList();
                Value[] values = dissolveGeometries(toDissol, geometries);
                Geometry geometry = union(geometries);
                Value[] valuesWithFID = new Value[values.length + 1];
                System.arraycopy(values, 0, valuesWithFID, 1, values.length);
                valuesWithFID[0] = ValueFactory.createValue(fid);
                DissolvedFeature dissolved = new DissolvedFeature(null, valuesWithFID, fid);
                dissolved.setJtsGeometry(geometry);
                this.featureProcessor.processFeature(dissolved);
                fid++;
                resetFunctions();
            } catch (ReadDriverException e) {
                throw new ProcessVisitorException(recordset.getName(), e, "Error al procesar las geometrias a fusionar durante dissolve");
            } catch (VisitorException e) {
                throw new ProcessVisitorException(recordset.getName(), e, "Error al procesar las geometrias a fusionar durante dissolve");
            }
        }
    }

    /**
	 * Returns the union of all geometries of the list
	 * @param geometries
	 * @return
	 */
    protected Geometry union(List geometries) {
        Geometry[] geom = new Geometry[geometries.size()];
        geometries.toArray(geom);
        return JTSFacade.union(geom, geometryType);
    }

    /**
	 * FIXME Redise�ar esto, pues el codigo es similar al de Spatial Join
	 *
	 */
    protected void resetFunctions() {
        if (numericField_sumarizeFunction == null) return;
        Iterator fieldsIt = numericField_sumarizeFunction.keySet().iterator();
        while (fieldsIt.hasNext()) {
            String field = (String) fieldsIt.next();
            SummarizationFunction[] functions = (SummarizationFunction[]) numericField_sumarizeFunction.get(field);
            for (int i = 0; i < functions.length; i++) {
                functions[i].reset();
            }
        }
    }

    /**
	 * Inner class to manage dissolve geoprocess. It mantains feature info
	 * interesting for dissolve (int index, JTS Geometry, etc)
	 *
	 * @author azabala
	 *
	 */
    protected class DissolvedFeature extends DefaultFeature {

        int index;

        Geometry jtsGeometry;

        public DissolvedFeature(IGeometry geom, Value[] att, int index) {
            super(geom, att);
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public Geometry getJtsGeometry() {
            return jtsGeometry;
        }

        public void setJtsGeometry(Geometry jtsGeometry) {
            this.jtsGeometry = jtsGeometry;
        }

        public IGeometry getGeometry() {
            IGeometry solution = super.getGeometry();
            if (solution == null && jtsGeometry != null) {
                solution = FConverter.jts_to_igeometry(jtsGeometry);
            }
            return solution;
        }
    }

    /**
	 * Creates a new IFeature with util info for dissolve geoprocess (it ignore
	 * non numerical values, etc)
	 *
	 * @param g
	 * @param index
	 * @return
	 * @throws ReadDriverException
	 * @throws DriverException
	 */
    protected DissolvedFeature createFeature(IGeometry g, int index) throws ReadDriverException {
        DissolvedFeature solution = null;
        int numNumericFields = 0;
        if (numericField_sumarizeFunction != null) numNumericFields = numericField_sumarizeFunction.keySet().size();
        Value[] values = new Value[numNumericFields + 1];
        if (numericField_sumarizeFunction != null) {
            Iterator fieldIt = numericField_sumarizeFunction.keySet().iterator();
            int valueIndex = 0;
            while (fieldIt.hasNext()) {
                String fieldName = (String) fieldIt.next();
                int fieldIndex = recordset.getFieldIndexByName(fieldName);
                values[valueIndex] = recordset.getFieldValue(index, fieldIndex);
                valueIndex++;
            }
        }
        int dissolveField = recordset.getFieldIndexByName(this.dissolveField);
        values[numNumericFields] = recordset.getFieldValue(index, dissolveField);
        solution = new DissolvedFeature(g, values, index);
        return solution;
    }

    protected boolean verifyIfDissolve(DissolvedFeature fet1, DissolvedFeature fet2) {
        Geometry jtsGeo = fet1.getJtsGeometry();
        Geometry featureJtsGeo = fet2.getJtsGeometry();
        if (jtsGeo.intersects(featureJtsGeo)) {
            int fieldIndex = 0;
            if (numericField_sumarizeFunction != null) fieldIndex = numericField_sumarizeFunction.keySet().size();
            Value adjacentVal = fet1.getAttribute(fieldIndex);
            Value val = fet2.getAttribute(fieldIndex);
            if (adjacentVal.doEquals(val)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * For each individual geometry processed in DissolveVisitor's visit method,
	 * this Visitor visits its adjacent polygons geometries to check dissolve
	 * conditions.
	 *
	 * @author azabala
	 *
	 */
    protected class IndividualGeometryDissolveVisitor implements FeatureVisitor {

        /**
		 * Marks index of features that have been dissolved yet
		 */
        FBitSet dissolvedFeatures;

        /**
		 * It saves all features for we are looking for adjacent geometries.
		 * Dissolving is similar to network tracking algorithms: one feature is
		 * adjacent to two, two is adjacent to four, etc We will save features
		 * to process in this stack
		 */
        Stack featuresToDissolve;

        /**
		 * Field use to dissolve adjacent geometries with the same value for it
		 *
		 * FIXME Change it for an evaluator of logical expresions
		 */
        String dissolveField;

        /**
		 * Maps for each numerical field its sumarization functions
		 */
        Map fields_sumarizeFunc;

        /**
		 * Feature for which we are looking for features to dissolve
		 */
        DissolvedFeature feature;

        /**
		 * jts geometry of feature
		 */
        private Geometry featureJtsGeo;

        /**
		 * Numeric values result of a sumarization operation
		 */
        private List sumarizedValues;

        /**
		 * Constructor
		 *
		 * @param feature
		 *            Feature we are analizing to found its adjacents
		 *
		 * @param dissolvedFeatures
		 *            bitset that marks all analyzed features (to not to analyze
		 *            later)
		 *
		 * @param featuresToDissolve
		 *            stack where we put adjacent features to analyze later
		 *
		 * @param fields_sumarize
		 *            maps a numeric field of the solution layer with its
		 *            sumarization functions
		 */
        protected IndividualGeometryDissolveVisitor(DissolvedFeature feature, FBitSet dissolvedFeatures, Stack featuresToDissolve, Map fields_sumarize) {
            this.dissolvedFeatures = dissolvedFeatures;
            this.feature = feature;
            this.featuresToDissolve = featuresToDissolve;
            this.featureJtsGeo = feature.getGeometry().toJTSGeometry();
            feature.setJtsGeometry(featureJtsGeo);
            this.sumarizedValues = new ArrayList();
            this.fields_sumarizeFunc = fields_sumarize;
        }

        public String getProcessDescription() {
            return "Dissolving a polygon with its adjacents";
        }

        /**
		 * Applies to sumarization functions feature values.
		 * @throws ReadDriverException
		 *
		 * @throws DriverException
		 *
		 * FIXME Redise�ar, pues el codigo es similar al de Spatial Join
		 */
        protected void applySumarizeFunction(int recordIndex) throws ReadDriverException {
            if (fields_sumarizeFunc == null) return;
            Iterator fieldsIt = fields_sumarizeFunc.keySet().iterator();
            while (fieldsIt.hasNext()) {
                String field = (String) fieldsIt.next();
                int fieldIndex = recordset.getFieldIndexByName(field);
                Value valToSumarize = recordset.getFieldValue(recordIndex, fieldIndex);
                SummarizationFunction[] functions = (SummarizationFunction[]) fields_sumarizeFunc.get(field);
                for (int i = 0; i < functions.length; i++) {
                    functions[i].process((NumericValue) valToSumarize);
                    sumarizedValues.add(functions[i].getSumarizeValue());
                }
            }
        }

        /**
		 * Analizes a feature (defined by g and its index) to see if it is an
		 * adjacent feature to the given feature, and to check its dissolve
		 * condition.
		 *
		 * @param g
		 * @param index
		 */
        public void visit(IGeometry g, int index) throws VisitorException, ProcessVisitorException {
            if (index == feature.getIndex()) return;
            if (dissolvedFeatures.get(index)) return;
            try {
                DissolvedFeature adjacentFeature = createFeature(g, index);
                Geometry jtsGeo = g.toJTSGeometry();
                adjacentFeature.setJtsGeometry(jtsGeo);
                if (verifyIfDissolve(feature, adjacentFeature)) {
                    dissolvedFeatures.set(index);
                    featuresToDissolve.push(adjacentFeature);
                    applySumarizeFunction(index);
                }
            } catch (ReadDriverException e) {
                throw new ProcessVisitorException(recordset.getName(), e, "Error al cargar los pol�gonos adyacentes durante un dissolve");
            }
        }

        public void stop(FLayer layer) throws VisitorException {
        }

        public boolean start(FLayer layer) throws StartVisitorException {
            try {
                recordset = ((AlphanumericData) layer).getRecordset();
                this.applySumarizeFunction(feature.getIndex());
            } catch (ReadDriverException e) {
                return false;
            }
            return true;
        }

        public void setFields_sumarizeFunc(Map fields_sumarizeFunc) {
            this.fields_sumarizeFunc = fields_sumarizeFunc;
        }

        public Map getFields_sumarizeFunc() {
            return fields_sumarizeFunc;
        }

        public Geometry getFeatureJtsGeo() {
            return featureJtsGeo;
        }

        public Value[] getSumarizedValues() {
            Value[] solution = new Value[sumarizedValues.size()];
            sumarizedValues.toArray(solution);
            return solution;
        }

        public Value[] getSumarizedValues2() {
            Value[] solution = null;
            ArrayList values = new ArrayList();
            if (fields_sumarizeFunc != null) {
                Iterator fieldsIt = fields_sumarizeFunc.keySet().iterator();
                while (fieldsIt.hasNext()) {
                    String field = (String) fieldsIt.next();
                    SummarizationFunction[] functions = (SummarizationFunction[]) fields_sumarizeFunc.get(field);
                    for (int i = 0; i < functions.length; i++) {
                        values.add(functions[i].getSumarizeValue());
                    }
                }
            }
            if (dissolveField != null) {
                try {
                    int dissolveFieldIndex = recordset.getFieldIndexByName(dissolveField);
                    Value dissolveField = recordset.getFieldValue(feature.getIndex(), dissolveFieldIndex);
                    values.add(dissolveField);
                } catch (ReadDriverException e) {
                    e.printStackTrace();
                }
            }
            solution = new Value[values.size()];
            values.toArray(solution);
            return solution;
        }

        public void setDissolveField(String dissolveField) {
            this.dissolveField = dissolveField;
        }

        public void setProcessedFeature(DissolvedFeature feature2) {
            this.feature = feature2;
        }
    }

    protected Value[] dissolveGeometries(Stack toDissol, List geometries) throws ProcessVisitorException, ReadDriverException, ExpansionFileReadException, VisitorException {
        IndividualGeometryDissolveVisitor visitor = null;
        DissolvedFeature feature = null;
        while (toDissol.size() != 0) {
            feature = (DissolvedFeature) toDissol.pop();
            dissolvedGeometries.set(feature.getIndex());
            if (visitor == null) {
                visitor = new IndividualGeometryDissolveVisitor(feature, dissolvedGeometries, toDissol, numericField_sumarizeFunction);
                visitor.setDissolveField(this.dissolveField);
            } else {
                visitor.setProcessedFeature(feature);
            }
            Rectangle2D bounds = feature.getGeometry().getBounds2D();
            double xmin = bounds.getMinX();
            double ymin = bounds.getMinY();
            double xmax = bounds.getMaxX();
            double ymax = bounds.getMaxY();
            double magnify = 15d;
            Rectangle2D query = new Rectangle2D.Double(xmin - magnify, ymin - magnify, (xmax - xmin) + magnify, (ymax - ymin) + magnify);
            strategy.process(visitor, query);
            geometries.add(feature.getJtsGeometry());
        }
        Value[] values = visitor.getSumarizedValues2();
        return values;
    }

    protected DissolvedFeature dissolve(Stack toDissol) throws ReadDriverException, ExpansionFileReadException, VisitorException {
        DissolvedFeature feature = null;
        Geometry jtsGeometry = null;
        IndividualGeometryDissolveVisitor visitor = null;
        while (toDissol.size() != 0) {
            feature = (DissolvedFeature) toDissol.pop();
            dissolvedGeometries.set(feature.getIndex());
            if (visitor == null) {
                visitor = new IndividualGeometryDissolveVisitor(feature, dissolvedGeometries, toDissol, numericField_sumarizeFunction);
                visitor.setDissolveField(this.dissolveField);
            } else {
                visitor.setProcessedFeature(feature);
            }
            Rectangle2D bounds = feature.getGeometry().getBounds2D();
            double xmin = bounds.getMinX();
            double ymin = bounds.getMinY();
            double xmax = bounds.getMaxX();
            double ymax = bounds.getMaxY();
            double magnify = 15d;
            Rectangle2D query = new Rectangle2D.Double(xmin - magnify, ymin - magnify, (xmax - xmin) + magnify, (ymax - ymin) + magnify);
            strategy.process(visitor, query);
            Geometry jtsGeo = visitor.getFeatureJtsGeo();
            if (jtsGeometry == null) {
                jtsGeometry = jtsGeo;
            } else {
                jtsGeometry = jtsGeometry.union(jtsGeo);
            }
        }
        Value[] values = visitor.getSumarizedValues2();
        feature = new DissolvedFeature(null, values, -1);
        feature.setJtsGeometry(jtsGeometry);
        return feature;
    }

    public void stop(FLayer layer) throws VisitorException {
        this.featureProcessor.finish();
    }

    public boolean start(FLayer layer) throws StartVisitorException {
        if (layer instanceof AlphanumericData && layer instanceof VectorialData) {
            try {
                dissolvedLayer = (FLyrVect) layer;
                geometryType = dissolvedLayer.getShapeType();
                recordset = ((AlphanumericData) layer).getRecordset();
                featureProcessor.start();
            } catch (ReadDriverException e) {
                return false;
            }
            return true;
        }
        return false;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public String getProcessDescription() {
        return "Dissolving polygons of a layer";
    }
}

package com.entelience.metrics.events;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import com.entelience.sql.Db;
import com.entelience.objects.metricsquery.QueryBean;

/**
 * Bundle all the configured things together into one class.
 * <br>
 * This object is created by MetricsXMLParser and contains our knowledge of
 * what is present in the XML file.
 * <br>
 * Note that we check to see whether all the (base) dimension classes
 * of an axes are pre-declared - this helps us greatly with our optimisations.
 */
public final class Bundle {

    private final Map<String, Dimension> dimensionsToObjects = new HashMap<String, Dimension>();

    private final Map<Dimension, String> objectsToDimensions = new HashMap<Dimension, String>();

    private final Map<String, Axes> axesDefinitions = new HashMap<String, Axes>();

    private final Map<String, Position> positionDefinitions = new HashMap<String, Position>();

    private final Map<String, Boolean> isSimple = new HashMap<String, Boolean>();

    private final Map<String, Boolean> isMulti = new HashMap<String, Boolean>();

    private final Map<String, String> tableNames = new HashMap<String, String>();

    private final Map<String, String> schemaNames = new HashMap<String, String>();

    private final Map<String, ValueType> valueTypes = new HashMap<String, ValueType>();

    private final Map<String, CustomValueTypeFactory> valueFactories = new HashMap<String, CustomValueTypeFactory>();

    private final Map<String, int[]> intervals = new HashMap<String, int[]>();

    private final Map<String, String> intervalsForMetric = new HashMap<String, String>();

    private final Map<String, FileInformation> files = new HashMap<String, FileInformation>();

    private final Map<String, MetricGroup> groups = new HashMap<String, MetricGroup>();

    private final Map<String, ValueSlice> valueSlices = new HashMap<String, ValueSlice>();

    /**
     * Does this metrics bundle represent only 1 file?
     */
    public boolean isSingleFile() {
        return this.files.size() == 1;
    }

    /**
     * Get a unique fileinformation, use only when this is expected.
     */
    public FileInformation getSingleFileInformation() {
        Iterator<FileInformation> i = this.files.values().iterator();
        if (i.hasNext()) {
            FileInformation fi = i.next();
            if (i.hasNext()) throw new IllegalStateException("This configuration bundle represents many XML files, cannot resolve to a single filename.");
            return fi;
        } else throw new IllegalStateException("No file information present");
    }

    public void setFileInformation(FileInformation fi) {
        if (fi == null || fi.filename == null) throw new IllegalArgumentException("filename is unspecified.");
        this.files.put(fi.filename, fi);
    }

    public FileInformation getFileInformation(String filename) {
        return this.files.get(filename);
    }

    public void setMetricGroup(MetricGroup mg) {
        if (mg == null || mg.groupName == null) throw new IllegalArgumentException("metric's group name is unspecified.");
        this.groups.put(mg.groupName, mg);
    }

    public MetricGroup getMetricGroup(String groupName) {
        return this.groups.get(groupName);
    }

    public Iterator<MetricGroup> getAllMetricGroups() {
        return this.groups.values().iterator();
    }

    public void setValueType(String metricName, ValueType valueType) {
        if (valueType == null) throw new IllegalArgumentException("ValueType unspecified for metric [" + metricName + "]");
        this.valueTypes.put(metricName, valueType);
    }

    public ValueType getValueType(String metricName) {
        return valueTypes.get(metricName);
    }

    public void setValueFactory(String metricName, CustomValueTypeFactory valueFactory) {
        if (valueFactory == null) throw new IllegalArgumentException("ValueFactory unspecified for metric [" + metricName + "]");
        this.valueFactories.put(metricName, valueFactory);
    }

    public CustomValueTypeFactory getValueFactory(String metricName) {
        return valueFactories.get(metricName);
    }

    public void setValueSlice(String metricName, ValueSlice valueSlice) {
        this.valueSlices.put(metricName, valueSlice);
    }

    public ValueSlice getValueSlice(String metricName) {
        return (ValueSlice) valueSlices.get(metricName);
    }

    public boolean exists(String metricName) {
        return tableNames.containsKey(metricName);
    }

    public boolean hasAxes(String metricName) {
        return axesDefinitions.containsKey(metricName);
    }

    public boolean getIsSimple(String metricName) {
        return isSimple.containsKey(metricName);
    }

    public boolean getIsMulti(String metricName) {
        return isMulti.containsKey(metricName);
    }

    public Axes getAxes(String metricName) {
        Axes axes = (Axes) axesDefinitions.get(metricName);
        if (axes == null) throw new IllegalArgumentException("Metric [" + metricName + "] not defined.");
        return axes;
    }

    public Position getPosition(String metricName) {
        Position p = (Position) positionDefinitions.get(metricName);
        if (p == null) {
            p = getAxes(metricName).toPosition(dimensionsToObjects);
            positionDefinitions.put(metricName, p);
        }
        return p;
    }

    public Dimension getDimensionObject(String dimensionName) {
        return (Dimension) dimensionsToObjects.get(dimensionName);
    }

    protected void setDimensionObject(String dimensionName, Dimension object) {
        if (dimensionName == null) throw new IllegalArgumentException("Must specify dimensionName");
        if (object == null) throw new IllegalArgumentException("Must specify object");
        if (dimensionsToObjects.containsKey(dimensionName)) throw new IllegalStateException("Dimension " + dimensionName + " already exists.");
        dimensionsToObjects.put(dimensionName, object);
        objectsToDimensions.put(object, dimensionName);
    }

    protected Map<String, Dimension> getDimensionsToObjectsMap() {
        return dimensionsToObjects;
    }

    protected Map<Dimension, String> getObjectsToDimensionsMap() {
        return objectsToDimensions;
    }

    protected void setAxes(String metricName, Axes object) {
        if (metricName == null) throw new IllegalArgumentException("Must specify metricName");
        if (object == null) throw new IllegalArgumentException("Must specify axes");
        if (axesDefinitions.containsKey(metricName)) throw new IllegalStateException("Axes for metric " + metricName + " already exists.");
        for (int i = 0; i < object.size(); ++i) {
            Axis axis = object.get(i);
            for (int j = 0; j < axis.size(); ++j) {
                Dimension d = axis.get(j);
                if (!predeclared(d)) throw new IllegalStateException("Axes must be defined using Dimensions that are pre-declared.");
                if (!unique(d, axis, j - 1)) throw new IllegalStateException("Duplication of any Dimension along this axis is not permitted.");
            }
        }
        axesDefinitions.put(metricName, object);
    }

    /**
     * Are we re-using a dimension that was already declared by the header
     * of the XML file?  This only applies to the StorablePositionValueType Dimensions.
     * Recurse into all PositionOperation objects.
     * <br>
     * This allows us to pre-cache all StorablePositionValueType objects once so that
     * we avoid lots of pointless object instanciation.
     * <br>
     * Primitive values (database keys for joins and / or representations of
     * data structures to analyse) are only stored in one unique "vector" of
     * StorablePositionValueType/name combinations.
     * <br>
     * Results of database operations (either the implicit "drilldown" through
     * the dimensions of an axis within a set of multiple axes -or- operations
     * only on distinct subsets of position vector values eg "CrossProduct") 
     * are thus calculated in one pass on the source data, without creating
     * new object representations in this part of the API.
     * 
     * @see Position, CrossProduct
     * @return true if we have already declared this dimension (pre-cache all StorablePositionValueType (primitive)
     */
    private boolean predeclared(Dimension d) {
        if (d instanceof StorablePositionValueType) {
            Iterator<Dimension> i = dimensionsToObjects.values().iterator();
            while (i.hasNext()) {
                Dimension d2 = i.next();
                if (d == d2) return true;
            }
        } else if (d instanceof PositionOperation) {
            PositionOperation po = ((PositionOperation) d);
            for (int i = 0, size = po.size(); i < size; ++i) {
                if (!predeclared(po.getStorableDimension(i))) return false;
            }
            return true;
        } else {
            throw new IllegalStateException("Unknown dimension type " + d);
        }
        return false;
    }

    /**
     * Has dimension d been declared uniquely up to and including
     * axis[max] (max is always < axis.size(), max can be -1)
     * @return false if we find a duplicate StorablePositionValueType anywhere
     */
    private boolean unique(Dimension d, Axis axis, int max) {
        if (max < 0) return true;
        if (d instanceof StorablePositionValueType) {
            Object o1 = (Object) d;
            for (int i = 0; i <= max; ++i) {
                Object o2 = axis.get(i);
                if (o1 == o2) return false;
                if (o2 instanceof PositionOperation) {
                    PositionOperation po = ((PositionOperation) o2);
                    for (int j = 0, size = po.size(); j < size; ++j) {
                        Object o3 = (Object) po.getStorableDimension(j);
                        if (o1 == o3) return false;
                    }
                }
            }
        } else if (d instanceof PositionOperation) {
            PositionOperation po = ((PositionOperation) d);
            for (int i = 0, size = po.size(); i < size; ++i) {
                if (!unique(po.getStorableDimension(i), axis, max)) return false;
            }
        }
        return true;
    }

    protected void setIsMulti(String metricName) {
        if (metricName == null) throw new IllegalArgumentException("Must specify metricName");
        isMulti.put(metricName, Boolean.TRUE);
    }

    protected void setIsSimple(String metricName) {
        if (metricName == null) throw new IllegalArgumentException("Must specify metricName");
        isSimple.put(metricName, Boolean.TRUE);
    }

    protected void setSchema(String metricName, String schema) {
        schemaNames.put(metricName, schema);
    }

    protected void setTable(String metricName, String table) {
        tableNames.put(metricName, table);
    }

    public String getSchema(String metricName) {
        return (String) schemaNames.get(metricName);
    }

    public String getTable(String metricName) {
        return (String) tableNames.get(metricName);
    }

    public EventImportDb getEventImportDb(Db db, String metricName) throws Exception {
        EventImportDb eidb = new EventImportDb();
        eidb.setTable(getSchema(metricName), getTable(metricName));
        if (!getIsSimple(metricName)) {
            eidb.setValueType(getValueType(metricName));
            if (getIsMulti(metricName)) {
                eidb.setMulti();
            }
            eidb.setPosition(getPosition(metricName));
        }
        eidb.setMetricName(metricName);
        eidb.setDb(db);
        return eidb;
    }

    public void setIntervals(String name, int i[]) {
        this.intervals.put(name, i);
    }

    public int[] getIntervals(String name) {
        int ary[] = (int[]) this.intervals.get(name);
        int data[] = new int[ary.length];
        for (int i = 0; i < data.length; ++i) data[i] = ary[i];
        return data;
    }

    public void setIntervalsForMetric(String metricName, String intervalsName) {
        if (this.intervals.containsKey(intervalsName)) this.intervalsForMetric.put(metricName, intervalsName); else throw new IllegalArgumentException("Intervals named [" + intervalsName + "] must be added before being referenced by metric [" + metricName + ']');
    }

    public int[] getIntervalsForMetric(String metricName) {
        String intervalsName = (String) intervalsForMetric.get(metricName);
        if (this.intervals.containsKey(intervalsName)) {
            return getIntervals(intervalsName);
        } else throw new IllegalArgumentException("Intervals named [" + intervalsName + "] cannot be found (metric [" + metricName + "])");
    }

    public EventCalculationDb getEventCalculationDb(Db db, String metricName, SDWOperationDb sdwOpDb) throws Exception {
        EventCalculationDb ecdb = new EventCalculationDb();
        ecdb.setBundle(this);
        ecdb.setTable(getSchema(metricName), getTable(metricName));
        ecdb.setMetricName(metricName);
        ecdb.setReportIntervals(getIntervalsForMetric(metricName));
        if (getIsSimple(metricName)) {
            ecdb.setSimple();
        } else {
            ecdb.setValueType(getValueType(metricName));
            if (valueSlices.containsKey(metricName)) ecdb.setValueSlice((ValueSlice) valueSlices.get(metricName));
            if (getIsMulti(metricName)) {
                ecdb.setMulti();
            }
            ecdb.setPosition(getPosition(metricName));
            ecdb.setAxes(getAxes(metricName));
        }
        ecdb.setSDWOperationDb(sdwOpDb);
        ecdb.setDb(db);
        return ecdb;
    }

    public EventDb getEventDb(Db db, QueryBean queryBean) throws Exception {
        EventDb evdb = new EventDb();
        evdb.setDb(db);
        return evdb;
    }
}

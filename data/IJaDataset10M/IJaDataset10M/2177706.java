package org.virbo.netCDF;

import java.text.ParseException;
import java.util.logging.Logger;
import org.das2.datum.Units;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.das2.datum.UnitsConverter;
import org.das2.util.monitor.NullProgressMonitor;
import org.das2.util.monitor.ProgressMonitor;
import org.virbo.dataset.AbstractDataSet;
import org.virbo.dataset.DataSetUtil;
import org.virbo.dataset.QDataSet;
import org.virbo.dataset.SemanticOps;
import org.virbo.dsops.Ops;
import org.virbo.metatree.IstpMetadataModel;
import ucar.ma2.InvalidRangeException;
import ucar.ma2.Range;
import ucar.nc2.Variable;
import ucar.nc2.Attribute;
import ucar.nc2.dataset.NetcdfDataset;

/**
 * wraps a netCDF variable to present it as a QDataSet.
 *
 * @author jbf
 */
public class NetCdfVarDataSet extends AbstractDataSet {

    Variable v;

    double[] data;

    int[] shape;

    private static final Logger logger = Logger.getLogger("virbo.netcdf");

    public static NetCdfVarDataSet create(Variable variable, String constraint, NetcdfDataset ncfile, ProgressMonitor mon) throws IOException {
        NetCdfVarDataSet result = new NetCdfVarDataSet();
        result.read(variable, ncfile, constraint, mon);
        return result;
    }

    private NetCdfVarDataSet() {
    }

    public static String sliceConstraints(String constraints, int i) {
        if (constraints == null) {
            return null;
        } else {
            if (constraints.startsWith("[") && constraints.endsWith("]")) {
                constraints = constraints.substring(1, constraints.length() - 1);
            }
            String[] cc = constraints.split(",");
            if (i >= cc.length) {
                return null;
            } else if (cc[i].equals(":")) {
                return null;
            } else {
                return cc[i];
            }
        }
    }

    /**
     * returns [ start, stop, stride ] or [ start, -1, -1 ] for slice.  This is
     * provided to reduce code and for uniform behavior.
     * 
     * See CdfJavaDataSource, which is where this was copied from.
     * @param constraint, such as "[0:100:2]" for even records between 0 and 100, non-inclusive.
     * @return [ start, stop, stride ] or [ start, -1, -1 ] for slice.
     */
    public static long[] parseConstraint(String constraint, long recCount) throws ParseException {
        long[] result = new long[] { 0, recCount, 1 };
        if (constraint == null) {
            return result;
        } else {
            if (constraint.startsWith("[") && constraint.endsWith("]")) {
                constraint = constraint.substring(1, constraint.length() - 1);
            }
            try {
                String[] ss = constraint.split(":", -2);
                if (ss.length > 0 && ss[0].length() > 0) {
                    result[0] = Integer.parseInt(ss[0]);
                    if (result[0] < 0) result[0] = recCount + result[0];
                }
                if (ss.length > 1 && ss[1].length() > 0) {
                    result[1] = Integer.parseInt(ss[1]);
                    if (result[1] < 0) result[1] = recCount + result[1];
                }
                if (ss.length > 2 && ss[2].length() > 0) {
                    result[2] = Integer.parseInt(ss[2]);
                }
                if (ss.length == 1) {
                    result[1] = -1;
                    result[2] = -1;
                }
            } catch (NumberFormatException ex) {
                throw new ParseException("expected integer: " + ex.toString(), 0);
            }
            return result;
        }
    }

    private int sliceCount(boolean[] slice, int idim) {
        int result = 0;
        for (int i = 0; i < idim; i++) {
            if (slice[i]) result++;
        }
        return result;
    }

    /**
     * Read the NetCDF data.
     * @param variable
     * @param ncfile
     * @param constraints null, or string like "[0:10]"  Note it's allowed for the constraint to not have [] because this is called recursively.
     * @param mon
     * @throws IOException
     */
    private void read(Variable variable, NetcdfDataset ncfile, String constraints, ProgressMonitor mon) throws IOException {
        this.v = variable;
        if (!mon.isStarted()) mon.started();
        mon.setProgressMessage("reading " + v.getNameAndDimensions());
        shape = v.getShape();
        boolean[] slice = new boolean[shape.length];
        ucar.ma2.Array a;
        if (constraints != null) {
            if (constraints.startsWith("[") && constraints.endsWith("]")) {
                constraints = constraints.substring(1, constraints.length() - 1);
            }
            try {
                String[] cc = constraints.split(",");
                List<Range> ranges = new ArrayList(v.getRanges());
                for (int i = 0; i < cc.length; i++) {
                    long[] ir = parseConstraint(cc[i], ranges.get(i).last() + 1);
                    if (ir[1] == -1) {
                        ranges.set(i, new Range((int) ir[0], (int) ir[0]));
                        shape[i] = 1;
                        slice[i] = true;
                    } else {
                        ranges.set(i, new Range((int) ir[0], (int) ir[1] - 1, (int) ir[2]));
                        shape[i] = (int) ((ir[1] - ir[0]) / ir[2]);
                    }
                }
                a = v.read(ranges);
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            } catch (InvalidRangeException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            a = v.read();
        }
        char[] cdata = null;
        try {
            if (a.getElementType() == char.class) {
                cdata = (char[]) a.get1DJavaArray(char.class);
            } else {
                data = (double[]) a.get1DJavaArray(Double.class);
            }
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException("data cannot be converted to numbers", ex);
        }
        properties.put(QDataSet.NAME, Ops.safeName(variable.getName()));
        if (shape.length > 1) properties.put(QDataSet.QUBE, Boolean.TRUE);
        boolean isCoordinateVariable = false;
        for (int ir = 0; ir < a.getRank(); ir++) {
            if (!slice[ir]) {
                ucar.nc2.Dimension d = v.getDimension(ir);
                Variable cv = ncfile.findVariable(d.getName());
                if ((cv != null) && cv.isCoordinateVariable()) {
                    Variable dv = cv;
                    if (dv != variable && dv.getRank() == 1) {
                        mon.setProgressMessage("reading " + dv.getNameAndDimensions());
                        QDataSet dependi = create(dv, sliceConstraints(constraints, ir), ncfile, new NullProgressMonitor());
                        properties.put("DEPEND_" + (ir - sliceCount(slice, ir)), dependi);
                    } else {
                        isCoordinateVariable = true;
                    }
                }
            }
        }
        Map<String, Object> attributes = new HashMap();
        mon.setProgressMessage("reading attributes");
        List attrs = v.getAttributes();
        for (Iterator i = attrs.iterator(); i.hasNext(); ) {
            Attribute attr = (Attribute) i.next();
            if (!attr.isArray()) {
                if (attr.isString()) {
                    attributes.put(attr.getName(), attr.getStringValue());
                } else {
                    attributes.put(attr.getName(), String.valueOf(attr.getNumericValue()));
                }
            }
        }
        if (attributes.containsKey("units")) {
            String unitsString = (String) attributes.get("units");
            if (unitsString.contains(" since ")) {
                Units u;
                try {
                    u = SemanticOps.lookupTimeUnits(unitsString);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
                properties.put(QDataSet.UNITS, u);
                properties.put(QDataSet.MONOTONIC, Boolean.TRUE);
            }
        }
        if (data == null) {
            if (cdata == null) {
                throw new RuntimeException("Either data or cdata should be defined at this point");
            }
            if (shape.length == 2 && shape[1] >= 14 && shape[1] <= 35) {
                logger.fine("parsing times formatted in char arrays");
                data = new double[shape[0]];
                String ss = new String(cdata);
                for (int i = 0; i < shape[0]; i++) {
                    int n = i * shape[1];
                    String s = ss.substring(n, n + shape[1]);
                    try {
                        data[i] = Units.us2000.parse(s).doubleValue(Units.us2000);
                    } catch (ParseException ex) {
                        data[i] = Units.us2000.getFillDouble();
                    }
                }
                properties.put(QDataSet.UNITS, Units.us2000);
                shape = new int[] { shape[0] };
            } else {
                data = (double[]) a.get1DJavaArray(Double.class);
            }
        }
        if (attributes.containsKey("_FillValue")) {
            double fill = Double.parseDouble((String) attributes.get("_FillValue"));
            for (int i = 0; i < data.length; i++) {
                if (data[i] == fill) data[i] = -1e31;
            }
        }
        if (attributes.containsKey("VAR_TYPE") || attributes.containsKey("DEPEND_0")) {
            properties.put(QDataSet.METADATA_MODEL, QDataSet.VALUE_METADATA_MODEL_ISTP);
            Map<String, Object> istpProps = new IstpMetadataModel().properties(attributes);
            if (properties.get(QDataSet.UNITS) == Units.us2000) {
                UnitsConverter uc = UnitsConverter.getConverter(Units.cdfEpoch, Units.us2000);
                if (istpProps.containsKey(QDataSet.VALID_MIN)) istpProps.put(QDataSet.VALID_MIN, uc.convert((Number) istpProps.get(QDataSet.VALID_MIN)));
                if (istpProps.containsKey(QDataSet.VALID_MAX)) istpProps.put(QDataSet.VALID_MAX, uc.convert((Number) istpProps.get(QDataSet.VALID_MAX)));
                if (istpProps.containsKey(QDataSet.TYPICAL_MIN)) istpProps.put(QDataSet.TYPICAL_MIN, uc.convert((Number) istpProps.get(QDataSet.TYPICAL_MIN)));
                if (istpProps.containsKey(QDataSet.TYPICAL_MAX)) istpProps.put(QDataSet.TYPICAL_MAX, uc.convert((Number) istpProps.get(QDataSet.TYPICAL_MAX)));
                istpProps.put(QDataSet.UNITS, Units.us2000);
            }
            properties.putAll(istpProps);
            for (int ir = 0; ir < a.getRank(); ir++) {
                String s = (String) attributes.get("DEPEND_" + ir);
                if (s != null) {
                    Variable dv = ncfile.findVariable(s);
                    if (dv != null && dv != variable) {
                        QDataSet dependi = create(dv, sliceConstraints(constraints, ir), ncfile, new NullProgressMonitor());
                        properties.put("DEPEND_" + (ir - sliceCount(slice, ir)), dependi);
                    }
                }
            }
        }
        ArrayList<Integer> newShape = new ArrayList(shape.length);
        for (int i = 0; i < shape.length; i++) {
            if (!slice[i]) {
                newShape.add(shape[i]);
            }
        }
        shape = new int[newShape.size()];
        for (int i = 0; i < newShape.size(); i++) shape[i] = newShape.get(i);
        if (properties.get(QDataSet.FILL_VALUE) == null && properties.get(QDataSet.VALID_MIN) == null) {
            properties.put(QDataSet.VALID_MIN, -1e90);
        }
        if (isCoordinateVariable) {
            properties.put(QDataSet.CADENCE, DataSetUtil.guessCadenceNew(this, null));
        }
        mon.finished();
    }

    public int rank() {
        return shape.length;
    }

    @Override
    public double value(int i) {
        return data[i];
    }

    @Override
    public double value(int i, int j) {
        int index = j + shape[1] * i;
        if (index >= data.length) {
            throw new IllegalArgumentException("how");
        }
        return data[index];
    }

    @Override
    public double value(int i, int j, int k) {
        int index = k + shape[2] * j + shape[2] * shape[1] * i;
        if (index >= data.length) {
            throw new IllegalArgumentException("how");
        }
        return data[index];
    }

    @Override
    public double value(int i, int j, int k, int l) {
        int index = l + shape[3] * k + shape[3] * shape[2] * j + shape[3] * shape[2] * shape[1] * i;
        if (index >= data.length) {
            throw new IllegalArgumentException("how");
        }
        return data[index];
    }

    @Override
    public int length() {
        return shape[0];
    }

    @Override
    public int length(int dim) {
        return shape[1];
    }

    @Override
    public int length(int dim0, int dim1) {
        return shape[2];
    }

    @Override
    public int length(int dim0, int dim1, int dim2) {
        return shape[3];
    }
}

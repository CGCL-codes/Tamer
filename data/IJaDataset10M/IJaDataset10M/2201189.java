package gov.nasa.gsfc.visbard.repository.resource;

import gov.nasa.gsfc.visbard.repository.category.CategoryType;
import gov.nasa.gsfc.visbard.util.Range;
import gov.nasa.gsfc.visbard.util.VisbardException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

public class ResourceReaderCombined implements ResourceReader {

    static org.apache.log4j.Category sLogger = org.apache.log4j.Category.getInstance(ResourceReaderCombined.class.getName());

    private String fName;

    private ResourceInfoCombined fInfo;

    private Range fTimeSpan;

    private double fCurTime;

    private boolean fUseDelta;

    private boolean fIsOpen = false;

    private double fDelta;

    private ColumnCombinedTime fTimeColDelta;

    private ColumnCombined fTimeColCombined;

    private ColumnCombined[] fCols;

    private HashMap fColToLastVal;

    private HashMap fReaderToLastAccessTime;

    private HashMap fReaderToCol;

    private static int idx = 0;

    public ResourceReaderCombined(ResourceInfo info) throws VisbardException {
        if (!(info instanceof ResourceInfoCombined)) throw new VisbardException("Improper info specified for reader");
        fInfo = (ResourceInfoCombined) info;
        Column[] cols = fInfo.getAllDescribedColumns();
        fCols = new ColumnCombined[cols.length];
        fReaderToCol = new HashMap();
        fColToLastVal = new HashMap();
        fReaderToLastAccessTime = new HashMap();
        fTimeColCombined = null;
        fTimeColDelta = null;
        fName = fInfo.getName();
        for (int i = 0; i < cols.length; i++) {
            if (cols[i].getName().equals(CategoryType.TIME.getName())) {
                if (cols[i] instanceof ColumnCombinedTime) {
                    fUseDelta = true;
                    fTimeColDelta = (ColumnCombinedTime) cols[i];
                    fDelta = fTimeColDelta.getDelta();
                    fTimeSpan = fTimeColDelta.getTimeSpan();
                } else {
                    fTimeColCombined = (ColumnCombined) cols[i];
                }
            }
            if (!(cols[i] instanceof ColumnCombinedTime)) {
                fCols[i] = (ColumnCombined) cols[i];
                ResourceReader rdr = fCols[i].getRdr();
                ArrayList list = (ArrayList) fReaderToCol.get(rdr);
                if (list == null) list = new ArrayList();
                list.add(fCols[i]);
                fReaderToCol.put(rdr, list);
            }
        }
        if (!fUseDelta) {
            if (fTimeColCombined == null) throw new VisbardException("Time column not found, " + "unable to intialize reader");
            fTimeSpan = fTimeColCombined.getRdr().getTimespan();
        }
        this.close();
    }

    /**
     * Returns the centricity of this reader
     */
    public String getCentricity() {
        if (!fReaderToCol.isEmpty()) {
            ((ResourceReader) (fReaderToCol.keySet().iterator().next())).getCentricity();
        }
        return fInfo.getCentricity();
    }

    /**
     * Returns the name of this resource
     */
    public String getName() {
        return fName;
    }

    public ResourceInfo getInfo() {
        return fInfo;
    }

    /**
     * Returns an array of avaoiable columns (which will be presetn in reading)
     */
    public Column[] getAllAvaliableColumns() {
        return fInfo.getAllDescribedColumns();
    }

    /**
     * Returns the timespan of the resource.
     */
    public Range getTimespan() {
        return fTimeSpan;
    }

    /**
     * Equivalent to getNumReadings(getTimeSpan())
     * (Can be accessed while the resource is closed)
     */
    public int getNumReadings() {
        if (fUseDelta) {
            return (int) (fTimeSpan.getExtent() / fDelta);
        } else {
            return fTimeColCombined.getRdr().getNumReadings();
        }
    }

    /**
     * Returns the number of readings in the time range specified.
     */
    public int getNumReadings(Range timeRange) {
        if (fUseDelta) {
            return (int) (fTimeSpan.getExtent() / fDelta);
        } else {
            return fTimeColCombined.getRdr().getNumReadings(timeRange);
        }
    }

    /**
     * Shifts the reader position to the start of the resource.
     * Next reading returned will be the first reading in the resource.
     */
    public void reset() {
        Iterator it = fReaderToCol.keySet().iterator();
        fReaderToLastAccessTime.clear();
        while (it.hasNext()) {
            ResourceReader rdr = (ResourceReader) it.next();
            rdr.reset();
            rdr.fastForward(fTimeSpan.fStart);
            fReaderToLastAccessTime.put(rdr, new Double(Double.NaN));
        }
        fColToLastVal.clear();
        fCurTime = fTimeSpan.fStart;
    }

    /**
     * Returns true if another reading is avaliable.
     */
    public boolean hasNext() {
        if (fUseDelta) {
            return (fCurTime <= fTimeSpan.fEnd);
        } else {
            return fTimeColCombined.getRdr().hasNext();
        }
    }

    /**
     * Read and return the next reading in the resource. This call will return
     * null if hasNext() returns false.
     */
    public Reading next() {
        if (!hasNext()) return null;
        Reading r = this.peekNext();
        if (fUseDelta) {
            fCurTime += fDelta;
        } else {
            fCurTime = fTimeColCombined.getRdr().peekTime();
        }
        return r;
    }

    /**
     * Read the next reading but do not advance the reader forward
     */
    public Reading peekNext() {
        HashMap map = new HashMap();
        Iterator it = fReaderToCol.keySet().iterator();
        while (it.hasNext()) {
            ResourceReader rdr = (ResourceReader) it.next();
            ArrayList cols = (ArrayList) fReaderToCol.get(rdr);
            ArrayList vals = getVals(rdr, cols);
            for (int i = 0; i < vals.size(); i++) {
                map.put(cols.get(i), vals.get(i));
            }
        }
        if (fUseDelta) {
            map.put(fTimeColDelta, new double[] { fCurTime });
        }
        return new Reading(map);
    }

    private ArrayList getVals(ResourceReader rdr, ArrayList cols) {
        Reading r;
        double time, oldTime, ratioT;
        double[] oldVal, val, tmp;
        ArrayList vals = new ArrayList();
        if (!rdr.hasNext()) {
            for (int i = 0; i < cols.size(); i++) {
                Column col = (Column) cols.get(i);
                if (col.isScalar()) vals.add(new double[] { Double.NaN }); else vals.add(new double[] { Double.NaN, Double.NaN, Double.NaN });
            }
            return vals;
        }
        time = rdr.peekTime();
        if (time > fCurTime) {
            r = rdr.peekNext();
            oldTime = ((Double) fReaderToLastAccessTime.get(rdr)).doubleValue();
            for (int i = 0; i < cols.size(); i++) {
                Column col = (Column) cols.get(i);
                oldVal = (double[]) fColToLastVal.get(col);
                val = r.get(col);
                ratioT = (fCurTime - oldTime) / (time - oldTime);
                if (col.isScalar()) {
                    if (!Double.isNaN(oldTime) && (oldVal != null) && (!Double.isNaN(oldVal[0])) && (!Double.isNaN(val[0]))) {
                        val[0] = oldVal[0] + (val[0] - oldVal[0]) * ratioT;
                    } else {
                        val[0] = Double.NaN;
                    }
                } else {
                    if (!Double.isNaN(oldTime) && (oldVal != null) && (!Double.isNaN(oldVal[0])) && (!Double.isNaN(oldVal[1])) && (!Double.isNaN(oldVal[2])) && (!Double.isNaN(val[0])) && (!Double.isNaN(val[1])) && (!Double.isNaN(val[2]))) {
                        val[0] = oldVal[0] + (val[0] - oldVal[0]) * ratioT;
                        val[1] = oldVal[1] + (val[1] - oldVal[1]) * ratioT;
                        val[2] = oldVal[2] + (val[2] - oldVal[2]) * ratioT;
                    } else {
                        val[0] = Double.NaN;
                        val[1] = Double.NaN;
                        val[2] = Double.NaN;
                    }
                }
                vals.add(val);
            }
            return vals;
        }
        double[] counts = new double[cols.size()];
        for (int i = 0; i < cols.size(); i++) {
            Column col = (Column) cols.get(i);
            if (col.isScalar()) {
                vals.add(new double[] { 0 });
            } else {
                vals.add(new double[] { 0, 0, 0 });
            }
            counts[i] = 0;
        }
        boolean duplicate = false;
        while (rdr.hasNext() && (time <= fCurTime) && (duplicate == false)) {
            r = advanceReaderAndUpdateCache(rdr);
            for (int i = 0; i < cols.size(); i++) {
                Column col = (Column) cols.get(i);
                val = (double[]) vals.get(i);
                tmp = r.get(col);
                if (col.isScalar()) {
                    if (!Double.isNaN(tmp[0])) {
                        val[0] += tmp[0];
                        counts[i]++;
                    }
                } else {
                    if ((!Double.isNaN(tmp[0])) && (!Double.isNaN(tmp[1])) && (!Double.isNaN(tmp[2]))) {
                        val[0] += tmp[0];
                        val[1] += tmp[1];
                        val[2] += tmp[2];
                        counts[i]++;
                    }
                }
            }
            if (time == fCurTime) duplicate = true;
            time = rdr.peekTime();
        }
        for (int i = 0; i < cols.size(); i++) {
            Column col = (Column) cols.get(i);
            val = (double[]) vals.get(i);
            if (col.isScalar()) {
                val[0] = val[0] / counts[i];
            } else {
                val[0] = val[0] / counts[i];
                val[1] = val[1] / counts[i];
                val[2] = val[2] / counts[i];
            }
        }
        return vals;
    }

    /**
     * Returns the time value of the "next" reading, Double.NaN
     * if hasNext() is false
     */
    public double peekTime() {
        if (!this.hasNext()) return Double.NaN;
        return fCurTime;
    }

    /**
     * Skip the specified number of readings. Skip one advances the reader
     * one reading forward
     */
    public void skip(int n) {
        double time;
        ResourceReader rdr;
        int count = 0;
        while ((count < n) && this.hasNext()) {
            Iterator it = fReaderToCol.keySet().iterator();
            while (it.hasNext()) {
                rdr = (ResourceReader) it.next();
                time = rdr.peekTime();
                while (rdr.hasNext() && (time <= fCurTime)) {
                    advanceReaderAndUpdateCache(rdr);
                    time = rdr.peekTime();
                }
            }
            if (fUseDelta) {
                fCurTime += fDelta;
            } else {
                fCurTime = fTimeColCombined.getRdr().peekTime();
            }
            count++;
        }
    }

    /**
     * Fast forward to time x. After this call next() should return
     * the first reading in the resource which occured after or at time x.
     */
    public void fastForward(double destinationTime) {
        double time;
        ResourceReader rdr;
        while ((fCurTime < destinationTime) && this.hasNext()) {
            Iterator it = fReaderToCol.keySet().iterator();
            while (it.hasNext()) {
                rdr = (ResourceReader) it.next();
                time = rdr.peekTime();
                while (rdr.hasNext() && (time <= fCurTime)) {
                    advanceReaderAndUpdateCache(rdr);
                    time = rdr.peekTime();
                }
            }
            if (fUseDelta) {
                fCurTime += fDelta;
            } else {
                fCurTime = fTimeColCombined.getRdr().peekTime();
            }
        }
    }

    private Reading advanceReaderAndUpdateCache(ResourceReader rdr) {
        double time = rdr.peekTime();
        Reading r = rdr.next();
        ArrayList cols = (ArrayList) fReaderToCol.get(rdr);
        for (int i = 0; i < cols.size(); i++) {
            Column col = (Column) cols.get(i);
            double[] val = r.get(col);
            fColToLastVal.put(col, val);
        }
        fReaderToLastAccessTime.put(rdr, new Double(time));
        return r;
    }

    /**
     * Opens the files and "reset" the resource.
     */
    public void open() {
        if (!fIsOpen) {
            Iterator it = fReaderToCol.keySet().iterator();
            while (it.hasNext()) {
                ResourceReader rdr = (ResourceReader) it.next();
                rdr.open();
            }
            fIsOpen = true;
        }
    }

    /**
     * Returns true if the resource is currently open
     */
    public boolean isOpen() {
        return fIsOpen;
    }

    public void close() {
        if (fIsOpen) {
            Iterator it = fReaderToCol.keySet().iterator();
            while (it.hasNext()) {
                ResourceReader rdr = (ResourceReader) it.next();
                rdr.close();
            }
            fIsOpen = false;
        }
    }

    public boolean isMhdFile() {
        return false;
    }
}

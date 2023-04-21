package gov.nasa.gsfc.visbard.repository.resource;

import gov.nasa.gsfc.visbard.util.VisbardException;
import gsfc.nssdc.cdf.CDF;
import gsfc.nssdc.cdf.CDFConstants;
import gsfc.nssdc.cdf.CDFException;
import gsfc.nssdc.cdf.Variable;

public class VariableReaderSingle implements VariableReader {

    private CDF fCDF;

    private Variable fVar;

    private double fFillval;

    private long fCacheRecNum = -1;

    private double[][] fCache = null;

    private long fType;

    private String fName;

    private boolean fScalar;

    private static final org.apache.log4j.Category sLogger = org.apache.log4j.Category.getInstance(VariableReaderSingle.class.getName());

    private static final int CACHE_SIZE = 100;

    public VariableReaderSingle(CDF cdf, String source, boolean scalar) throws VisbardException {
        fCDF = cdf;
        fName = source;
        fScalar = scalar;
        try {
            fVar = fCDF.getVariable(fName);
        } catch (CDFException e) {
            throw new VisbardException("Unable to access variable : " + fName);
        }
        fFillval = getFillVal(fVar);
        fType = fVar.getDataType();
        if (scalar) fCache = new double[CACHE_SIZE][1]; else fCache = new double[CACHE_SIZE][3];
    }

    public boolean isScalar() {
        return fScalar;
    }

    private double getFillVal(Variable cvar) {
        Object data;
        long type;
        double scal = Double.NaN;
        try {
            data = cvar.getEntryData("FILLVAL");
            type = cvar.getDataType();
        } catch (CDFException e) {
            return scal;
        }
        if ((type == CDFConstants.CDF_BYTE) || (type == CDFConstants.CDF_INT1)) {
            scal = ((Byte) data).doubleValue();
        } else if ((type == CDFConstants.CDF_UINT1) || (type == CDFConstants.CDF_INT2)) {
            scal = ((Short) data).doubleValue();
        } else if ((type == CDFConstants.CDF_UINT2) || (type == CDFConstants.CDF_INT4)) {
            scal = ((Integer) data).doubleValue();
        } else if (type == CDFConstants.CDF_UINT4) {
            scal = ((Long) data).doubleValue();
        } else if ((type == CDFConstants.CDF_FLOAT) || (type == CDFConstants.CDF_REAL4)) {
            if (data instanceof String) scal = Double.parseDouble((String) data); else scal = ((Float) data).doubleValue();
        } else if (type == CDFConstants.CDF_DOUBLE) {
            try {
                if (data instanceof Float) scal = ((Float) data).doubleValue(); else if (data instanceof Double) scal = ((Double) data).doubleValue();
            } catch (ClassCastException cce) {
                sLogger.error("Error casting CDF data from CDF_DOUBLE to a Java double or float: " + cce.getMessage());
                scal = Double.NaN;
            }
        } else if (type == CDFConstants.CDF_REAL8) {
            scal = ((Double) data).doubleValue();
        }
        return scal;
    }

    private void updateCacheVector(long recnum) {
        try {
            if ((fType == CDFConstants.CDF_BYTE) || (fType == CDFConstants.CDF_INT1)) {
                byte[][] arr = (byte[][]) fVar.getHyperData(recnum, CACHE_SIZE, 1L, new long[] { 0L }, new long[] { 3L }, new long[] { 1L });
                for (int z = 0; z < arr.length; z++) {
                    fCache[z][0] = (double) arr[z][0];
                    fCache[z][1] = (double) arr[z][1];
                    fCache[z][2] = (double) arr[z][2];
                }
            } else if ((fType == CDFConstants.CDF_UINT1) || (fType == CDFConstants.CDF_INT2)) {
                short[][] arr = (short[][]) fVar.getHyperData(recnum, CACHE_SIZE, 1L, new long[] { 0L }, new long[] { 3L }, new long[] { 1L });
                for (int z = 0; z < arr.length; z++) {
                    fCache[z][0] = (double) arr[z][0];
                    fCache[z][1] = (double) arr[z][1];
                    fCache[z][2] = (double) arr[z][2];
                }
            } else if ((fType == CDFConstants.CDF_UINT2) || (fType == CDFConstants.CDF_INT4)) {
                int[][] arr = (int[][]) fVar.getHyperData(recnum, CACHE_SIZE, 1L, new long[] { 0L }, new long[] { 3L }, new long[] { 1L });
                for (int z = 0; z < arr.length; z++) {
                    fCache[z][0] = (double) arr[z][0];
                    fCache[z][1] = (double) arr[z][1];
                    fCache[z][2] = (double) arr[z][2];
                }
            } else if (fType == CDFConstants.CDF_UINT4) {
                long[][] arr = (long[][]) fVar.getHyperData(recnum, CACHE_SIZE, 1L, new long[] { 0L }, new long[] { 3L }, new long[] { 1L });
                for (int z = 0; z < arr.length; z++) {
                    fCache[z][0] = (double) arr[z][0];
                    fCache[z][1] = (double) arr[z][1];
                    fCache[z][2] = (double) arr[z][2];
                }
            } else if ((fType == CDFConstants.CDF_FLOAT) || (fType == CDFConstants.CDF_REAL4)) {
                Object obj = fVar.getHyperData(recnum, CACHE_SIZE, 1L, new long[] { 0L }, new long[] { 3L }, new long[] { 1L });
                float[][] arr = (float[][]) fVar.getHyperData(recnum, CACHE_SIZE, 1L, new long[] { 0L }, new long[] { 3L }, new long[] { 1L });
                for (int z = 0; z < arr.length; z++) {
                    fCache[z][0] = (double) arr[z][0];
                    fCache[z][1] = (double) arr[z][1];
                    fCache[z][2] = (double) arr[z][2];
                }
            } else if ((fType == CDFConstants.CDF_DOUBLE) || (fType == CDFConstants.CDF_REAL8)) {
                double[][] arr = (double[][]) fVar.getHyperData(recnum, CACHE_SIZE, 1L, new long[] { 0L }, new long[] { 3L }, new long[] { 1L });
                for (int z = 0; z < arr.length; z++) {
                    fCache[z][0] = (double) arr[z][0];
                    fCache[z][1] = (double) arr[z][1];
                    fCache[z][2] = (double) arr[z][2];
                }
            } else {
                sLogger.error("Unable to interpret variable '" + fVar.getName() + "' as vector!");
            }
        } catch (CDFException e) {
            sLogger.error("Unable to retrieve variable data from CDF: " + e.getMessage());
        }
        fCacheRecNum = recnum;
    }

    private void updateCacheScalar(long recnum) {
        try {
            if ((fType == CDFConstants.CDF_BYTE) || (fType == CDFConstants.CDF_INT1)) {
                byte[] arr = (byte[]) fVar.getHyperData(recnum, CACHE_SIZE, 1L, new long[] { 0L }, new long[] { 1L }, new long[] { 1L });
                for (int z = 0; z < arr.length; z++) fCache[z][0] = (double) arr[z];
            } else if ((fType == CDFConstants.CDF_UINT1) || (fType == CDFConstants.CDF_INT2)) {
                short[] arr = (short[]) fVar.getHyperData(recnum, CACHE_SIZE, 1L, new long[] { 0L }, new long[] { 1L }, new long[] { 1L });
                for (int z = 0; z < arr.length; z++) fCache[z][0] = (double) arr[z];
            } else if ((fType == CDFConstants.CDF_UINT2) || (fType == CDFConstants.CDF_INT4)) {
                int[] arr = (int[]) fVar.getHyperData(recnum, CACHE_SIZE, 1L, new long[] { 0L }, new long[] { 1L }, new long[] { 1L });
                for (int z = 0; z < arr.length; z++) fCache[z][0] = (double) arr[z];
            } else if (fType == CDFConstants.CDF_UINT4) {
                long[] arr = (long[]) fVar.getHyperData(recnum, CACHE_SIZE, 1L, new long[] { 0L }, new long[] { 1L }, new long[] { 1L });
                for (int z = 0; z < arr.length; z++) fCache[z][0] = (double) arr[z];
            } else if ((fType == CDFConstants.CDF_FLOAT) || (fType == CDFConstants.CDF_REAL4)) {
                float[] arr = (float[]) fVar.getHyperData(recnum, CACHE_SIZE, 1L, new long[] { 0L }, new long[] { 1L }, new long[] { 1L });
                for (int z = 0; z < arr.length; z++) fCache[z][0] = (double) arr[z];
            } else if ((fType == CDFConstants.CDF_DOUBLE) || (fType == CDFConstants.CDF_REAL8) || (fType == CDFConstants.CDF_EPOCH)) {
                double[] arr = (double[]) fVar.getHyperData(recnum, CACHE_SIZE, 1L, new long[] { 0L }, new long[] { 1L }, new long[] { 1L });
                for (int z = 0; z < arr.length; z++) fCache[z][0] = (double) arr[z];
            }
        } catch (CDFException e) {
            sLogger.error("Unable to retrieve variable data from CDF");
        }
        fCacheRecNum = recnum;
    }

    public double[] getVector(long recnum) {
        if (fCacheRecNum == -1) {
            fCache = new double[CACHE_SIZE][3];
            this.updateCacheVector(recnum);
        } else if ((recnum < fCacheRecNum) || (recnum >= fCacheRecNum + CACHE_SIZE)) {
            this.updateCacheVector(recnum);
        }
        double[] rv = fCache[(int) (recnum - fCacheRecNum)];
        if (rv[0] == fFillval) rv[0] = Double.NaN;
        if (rv[1] == fFillval) rv[1] = Double.NaN;
        if (rv[2] == fFillval) rv[2] = Double.NaN;
        double[] real = { rv[0], rv[1], rv[2] };
        return real;
    }

    public double[] getScalar(long recnum) {
        if (fCacheRecNum == -1) {
            fCache = new double[CACHE_SIZE][1];
            this.updateCacheScalar(recnum);
        } else if ((recnum < fCacheRecNum) || (recnum >= fCacheRecNum + CACHE_SIZE)) {
            this.updateCacheScalar(recnum);
        }
        double[] rv = fCache[(int) (recnum - fCacheRecNum)];
        if (rv[0] == fFillval) rv[0] = Double.NaN;
        double[] real = { rv[0] };
        return real;
    }

    public void clearCache() {
        fCache = new double[0][0];
        fCacheRecNum = -1;
    }
}

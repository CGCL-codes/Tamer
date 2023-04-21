package net.sf.mzmine.modules.rawdatamethods.filtering.scanfilters.mean;

import java.util.Vector;
import net.sf.mzmine.data.DataPoint;
import net.sf.mzmine.data.Scan;
import net.sf.mzmine.data.impl.SimpleDataPoint;
import net.sf.mzmine.data.impl.SimpleScan;
import net.sf.mzmine.modules.rawdatamethods.filtering.scanfilters.ScanFilter;
import net.sf.mzmine.parameters.ParameterSet;

public class MeanFilter implements ScanFilter {

    @Override
    public Scan filterScan(Scan sc, ParameterSet parameters) {
        double windowLength = parameters.getParameter(MeanFilterParameters.oneSidedWindowLength).getValue();
        if (sc.getMSLevel() != 1) {
            return sc;
        }
        Vector<Double> massWindow = new Vector<Double>();
        Vector<Double> intensityWindow = new Vector<Double>();
        double currentMass;
        double lowLimit;
        double hiLimit;
        double mzVal;
        double elSum;
        DataPoint oldDataPoints[] = sc.getDataPoints();
        DataPoint newDataPoints[] = new DataPoint[oldDataPoints.length];
        int addi = 0;
        for (int i = 0; i < oldDataPoints.length; i++) {
            currentMass = oldDataPoints[i].getMZ();
            lowLimit = currentMass - windowLength;
            hiLimit = currentMass + windowLength;
            if (massWindow.size() > 0) {
                mzVal = massWindow.get(0).doubleValue();
                while ((massWindow.size() > 0) && (mzVal < lowLimit)) {
                    massWindow.remove(0);
                    intensityWindow.remove(0);
                    if (massWindow.size() > 0) {
                        mzVal = massWindow.get(0).doubleValue();
                    }
                }
            }
            while ((addi < oldDataPoints.length) && (oldDataPoints[addi].getMZ() <= hiLimit)) {
                massWindow.add(oldDataPoints[addi].getMZ());
                intensityWindow.add(oldDataPoints[addi].getIntensity());
                addi++;
            }
            elSum = 0;
            for (int j = 0; j < intensityWindow.size(); j++) {
                elSum += ((Double) (intensityWindow.get(j))).doubleValue();
            }
            newDataPoints[i] = new SimpleDataPoint(currentMass, elSum / (double) intensityWindow.size());
        }
        Scan newScan = new SimpleScan(sc.getDataFile(), sc.getScanNumber(), sc.getMSLevel(), sc.getRetentionTime(), sc.getParentScanNumber(), sc.getPrecursorMZ(), sc.getPrecursorCharge(), sc.getFragmentScanNumbers(), newDataPoints, true);
        return newScan;
    }

    @Override
    public String getName() {
        return "Mean filter";
    }

    @Override
    public Class<? extends ParameterSet> getParameterSetClass() {
        return MeanFilterParameters.class;
    }
}

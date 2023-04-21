package net.sf.mzmine.modules.visualization.twod;

import java.text.NumberFormat;
import net.sf.mzmine.data.ChromatographicPeak;
import net.sf.mzmine.data.PeakList;
import net.sf.mzmine.data.PeakListRow;
import net.sf.mzmine.main.MZmineCore;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.data.xy.XYDataset;

/**
 * Tooltip generator for 2D visualizer
 */
class PeakToolTipGenerator implements XYToolTipGenerator {

    private NumberFormat rtFormat = MZmineCore.getConfiguration().getRTFormat();

    private NumberFormat mzFormat = MZmineCore.getConfiguration().getMZFormat();

    private NumberFormat intensityFormat = MZmineCore.getConfiguration().getIntensityFormat();

    /**
     * @see org.jfree.chart.labels.XYToolTipGenerator#generateToolTip(org.jfree.data.xy.XYDataset,
     *      int, int)
     */
    public String generateToolTip(XYDataset dataset, int series, int item) {
        PeakDataSet peakDataSet = (PeakDataSet) dataset;
        PeakDataPoint dataPoint = peakDataSet.getDataPoint(series, item);
        PeakList peakList = peakDataSet.getPeakList();
        ChromatographicPeak peak = peakDataSet.getPeak(series);
        PeakListRow row = peakList.getPeakRow(peak);
        double rtValue = dataPoint.getRT();
        double intValue = dataPoint.getIntensity();
        double mzValue = dataPoint.getMZ();
        int scanNumber = dataPoint.getScanNumber();
        String toolTip = "Peak: " + peak + "\nStatus: " + peak.getPeakStatus() + "\nPeak list row: " + row + "\nScan #" + scanNumber + "\nRetention time: " + rtFormat.format(rtValue) + "\nm/z: " + mzFormat.format(mzValue) + "\nIntensity: " + intensityFormat.format(intValue);
        return toolTip;
    }
}

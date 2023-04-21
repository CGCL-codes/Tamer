package com.inetmon.jn.mplsgeneralstatistic.views;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import org.eclipse.swt.widgets.Composite;
import com.inetmon.jn.core.CorePlugin;
import com.inetmon.jn.mplsgeneralstatistic.Activator;
import com.inetmon.jn.statistic.MPLSNetUtilizationStat;
import com.inetmon.jn.statistic.MPLSPerSecondStat;
import com.inetmon.jn.statistic.SinglePerSecondStat;
import com.inetmon.jn.statistic.VLANNetUtilizationStat;
import com.inetmon.jn.statistic.VLANPerSecondStat;
import com.inetmon.jn.statistic.VLANSinglePerSecondStat;

public class MPLSGeneralUtilizationView extends GeneralMPLSView {

    public static final String ID_VIEW = "com.inetmon.jn.mplsgeneralstatistic.views.MPLSGeneralUtilizationView";

    private long bandwidth = 1000000;

    private String linespeed;

    private String Vid;

    private long cumulValue = 0;

    private long saveCumulValue = 0;

    private long peakValue = 0;

    private long peakTime = 0;

    private float sec = 0;

    private int firstTime = 0;

    public MPLSGeneralUtilizationView() {
        super();
        statRecorder = new MPLSNetUtilizationStat();
        Activator.getGeneralViewSync().addView(this, statRecorder);
    }

    public void dispose() {
    }

    /**
	     * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	     */
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        super.initGraphAndLabels();
    }

    /**
	     * @see com.inetmon.jn.statistic.general.single.model.MPLSIGeneralStatListener#asyncUpdateView()
	     */
    public void asyncUpdateView() {
        long[] values = getValue();
        if (CorePlugin.getDefault().isProvide() || CorePlugin.getDefault().isOpenFile()) {
        } else {
            saveCumulValue = 0;
        }
        if (data != null) {
            data.addValue(types[MPLSPerSecondStat.INDEX_CURRENT], values[MPLSPerSecondStat.INDEX_CURRENT] / (float) 100);
            data.addValue(types[MPLSPerSecondStat.INDEX_AVERAGE], values[MPLSPerSecondStat.INDEX_AVERAGE] / (float) 100);
            data.update();
            pLabel.setTextValueLabel(String.valueOf(values[MPLSPerSecondStat.INDEX_CURRENT] / (float) 100), String.valueOf(values[MPLSPerSecondStat.INDEX_AVERAGE] / (float) 100), String.valueOf(values[MPLSPerSecondStat.INDEX_PEAK] / (float) 100), new Date(values[MPLSPerSecondStat.INDEX_DATE_PEAK]));
        }
    }

    /**
		 * Get network bandwidth
		 */
    public void getLineSpeed() {
        String temp = "";
        try {
            BufferedReader in = new BufferedReader(new FileReader("configuration.ini"));
            String str = in.readLine();
            linespeed = str;
            in.close();
        } catch (IOException e) {
        }
        int i = 0;
        while (i < linespeed.length() && Character.isDigit(linespeed.charAt(i))) {
            temp += linespeed.charAt(i);
            i++;
        }
        linespeed = temp;
    }

    public long[] getValue() {
        long[] ret = new long[4];
        for (int i = 0; i < 4; i++) ret[i] = 0;
        getLineSpeed();
        bandwidth = Long.parseLong(linespeed) * 1000000;
        if (CorePlugin.getDefault().getFirstMPLSTime() != null && CorePlugin.getDefault().getLastMPLSTime() != null) {
            sec = (CorePlugin.getDefault().getLastMPLSTime().getTime() - CorePlugin.getDefault().getFirstMPLSTime().getTime()) / (float) 1000;
        } else saveCumulValue = 0;
        if (firstTime == 0) {
            cumulValue = 0;
        } else {
            cumulValue = Math.round(CorePlugin.getDefault().getSizeOfMPLSPackets() * 8 * 10000 / (float) bandwidth);
        }
        if (sec == 0) {
            ret[MPLSPerSecondStat.INDEX_CURRENT] = 0;
        } else {
            if (cumulValue != 0) {
                ret[MPLSPerSecondStat.INDEX_CURRENT] = cumulValue - saveCumulValue;
            } else {
                ret[MPLSPerSecondStat.INDEX_CURRENT] = cumulValue;
            }
            if (firstTime == 0) {
                saveCumulValue = Math.round(CorePlugin.getDefault().getSizeOfMPLSPackets() * 8 * 10000 / (float) bandwidth);
                firstTime++;
            } else {
                saveCumulValue = cumulValue;
            }
        }
        if (ret[MPLSPerSecondStat.INDEX_CURRENT] > peakValue) {
            peakValue = ret[MPLSPerSecondStat.INDEX_CURRENT];
            peakTime = (new Date()).getTime();
        }
        ret[MPLSPerSecondStat.INDEX_PEAK] = peakValue;
        ret[MPLSPerSecondStat.INDEX_DATE_PEAK] = peakTime;
        if (sec == 0) ret[MPLSPerSecondStat.INDEX_AVERAGE] = 0; else {
            ret[MPLSPerSecondStat.INDEX_AVERAGE] = Math.round(cumulValue / sec);
        }
        return ret;
    }
}

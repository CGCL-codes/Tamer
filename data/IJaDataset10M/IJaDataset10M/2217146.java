package de.unikoeln.genetik.popgen.jfms.gui;

import de.unikoeln.genetik.popgen.jfms.model.analysis.TestStatistics;

@SuppressWarnings("serial")
public class FayWuMonitor extends AbstractTestStatisticMonitor {

    public FayWuMonitor(double ymin, double ymax) {
        super(ymin, ymax);
    }

    @Override
    double getTestStatistic(int[] siteFrequencySpectrum) {
        return TestStatistics.compute_Omega_H(siteFrequencySpectrum, true);
    }
}

package net.sourceforge.xmlfit.mojo.fit.listener;

import fit.Counts;
import net.sourceforge.xmlfit.mojo.fit.FitRunnerExecutionListener;

public class AbstractFitRunnerListener implements FitRunnerExecutionListener {

    public void beginFitTest(int fitIndex, String sourceFolder, Counts counts) {
    }

    public void finishListener(Counts globalFinalCount, int fitTestCount) {
    }

    public void finishedFitTest(Counts globalCount, String fileName, int index, Counts fixtureCount) {
    }

    public void prepareListener(String sourceDirectory, String outputDirectory, int expectedFixtureCount) {
    }
}

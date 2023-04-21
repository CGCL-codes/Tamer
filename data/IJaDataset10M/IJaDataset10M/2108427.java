package net.sourceforge.xmlfit.mojo.fit;

import fit.Counts;

public interface FitRunnerExecutionListener {

    void prepareListener(String sourceDirectory, String outputDirectory, int expectedFixtureCount);

    void finishedFitTest(Counts globalCount, String fileName, int index, Counts fixtureCount);

    void finishListener(Counts globalFinalCount, int fitTestCount);

    void beginFitTest(int fitIndex, String sourceFolder, Counts counts);
}

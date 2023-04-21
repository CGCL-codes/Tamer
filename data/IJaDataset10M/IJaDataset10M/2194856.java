package org.approvaltests.reporters;

public class DiffReporter extends FirstWorkingReporter {

    public DiffReporter() {
        super(TortoiseDiffReporter.INSTANCE, BeyondCompareReporter.INSTANCE, WinMergeReporter.INSTANCE, JunitReporter.INSTANCE, QuietReporter.INSTANCE);
    }
}

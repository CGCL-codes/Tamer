package org.encog.workbench.util;

import org.encog.StatusReportable;
import org.encog.workbench.EncogWorkBench;

public class OutputStatusReportable implements StatusReportable {

    public void report(int total, int current, String message) {
        StringBuilder str = new StringBuilder();
        if (total == 0) {
            str.append(current);
        } else {
            str.append(current);
            str.append('/');
            str.append(total);
        }
        str.append(": ");
        str.append(message);
        EncogWorkBench.getInstance().outputLine(str.toString());
    }

    public void reportPhase(int arg0, int arg1, String arg2) {
    }
}

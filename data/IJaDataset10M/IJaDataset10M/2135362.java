package net.sourceforge.jabm.learning;

import net.sourceforge.jabm.report.DataSeriesWriter;

/**
 * @author Steve Phelps
 * @version $Revision: 16 $
 */
public class GraphLearnerMonitor extends DataSeriesWriter implements LearnerMonitor {

    public GraphLearnerMonitor() {
        super();
    }

    public void startRecording() {
        clear();
    }

    public void finishRecording() {
    }
}

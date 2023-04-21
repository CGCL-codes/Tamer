package eyetrackercalibrator.gui;

import eyetrackercalibrator.framemanaging.FrameSynchronizor;
import eyetrackercalibrator.framemanaging.IlluminationXYDataSet;
import eyetrackercalibrator.framemanaging.InformationDatabase;

/**
 *
 * @author SQ
 */
public class IlluminationGraphTabPanel extends GraphTabPanel {

    IlluminationXYDataSet dataSet = null;

    FrameSynchronizor frameSynchronizor = null;

    public void setFrameSynchronizor(FrameSynchronizor frameSynchronizor) {
        this.frameSynchronizor = frameSynchronizor;
        if (this.dataSet != null) {
            this.dataSet.setFrameSynchronizor(frameSynchronizor);
        }
    }

    private static final String[] graphName = { "Illumination" };

    public IlluminationGraphTabPanel() {
        super(graphName);
    }

    /** Setting data set for the graph
     * @param infoDatabase 
     */
    public void setDataSet(InformationDatabase infoDatabase, int lastFrame) {
        this.dataSet = new IlluminationXYDataSet(infoDatabase);
        this.dataSet.setFrameSynchronizor(this.frameSynchronizor);
        this.dataSet.setLastItem(lastFrame);
        setDataSet(dataSet, this.graphPanel[0]);
    }
}

package guineu.modules.filter.Alignment.centering.median;

import guineu.data.Dataset;
import guineu.data.PeakListRow;
import guineu.taskcontrol.AbstractTask;
import guineu.taskcontrol.TaskStatus;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class MedianCenteringTask extends AbstractTask {

    private Dataset peakLists[];

    private int processedRows, totalRows;

    public MedianCenteringTask(Dataset[] peakLists) {
        this.peakLists = peakLists;
    }

    public String getTaskDescription() {
        return "Median centering";
    }

    public double getFinishedPercentage() {
        if (totalRows == 0) {
            return 0f;
        }
        return (double) processedRows / (double) totalRows;
    }

    public void cancel() {
        setStatus(TaskStatus.CANCELED);
    }

    /**
         * @see Runnable#run()
         */
    public void run() {
        setStatus(TaskStatus.PROCESSING);
        for (Dataset data : this.peakLists) {
            normalize(data);
        }
        setStatus(TaskStatus.FINISHED);
    }

    private void normalize(Dataset data) {
        for (String nameExperiment : data.getAllColumnNames()) {
            List<Double> median = new ArrayList<Double>();
            for (PeakListRow row : data.getRows()) {
                Object value = row.getPeak(nameExperiment);
                if (value != null && value instanceof Double) {
                    median.add((Double) value);
                }
            }
            Collections.sort(median);
            for (PeakListRow row : data.getRows()) {
                Object value = row.getPeak(nameExperiment);
                if (value != null && value instanceof Double) {
                    row.setPeak(nameExperiment, Math.abs((Double) value - median.get(median.size() / 2)));
                }
            }
        }
    }
}

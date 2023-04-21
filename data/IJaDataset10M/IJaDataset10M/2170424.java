package guineu.modules.mylly.alignment.basicAligner;

import guineu.data.Dataset;
import guineu.data.PeakListRow;
import guineu.data.impl.datasets.SimpleGCGCDataset;
import guineu.data.impl.peaklists.SimplePeakListRowGCGC;
import guineu.main.GuineuCore;
import guineu.taskcontrol.AbstractTask;
import guineu.taskcontrol.TaskStatus;
import guineu.util.Range;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BasicAlignerGCGCTask extends AbstractTask {

    private Dataset peakLists[], alignedPeakList;

    private int processedRows, totalRows;

    private String peakListName;

    private double RT1Tolerance;

    private double RT2Tolerance;

    private double progress;

    public BasicAlignerGCGCTask(Dataset[] peakLists, BasicAlignerGCGCParameters parameters) {
        this.peakLists = peakLists;
        peakListName = parameters.getParameter(BasicAlignerGCGCParameters.peakListName).getValue();
        RT1Tolerance = parameters.getParameter(BasicAlignerGCGCParameters.RT1Tolerance).getValue().getTolerance();
        RT2Tolerance = parameters.getParameter(BasicAlignerGCGCParameters.RT2Tolerance).getValue().getTolerance();
    }

    public String getTaskDescription() {
        return "Basic aligner, " + peakListName + " (" + peakLists.length + " peak lists)";
    }

    public double getFinishedPercentage() {
        if (totalRows == 0) {
            return 0f;
        }
        return progress;
    }

    public void cancel() {
        setStatus(TaskStatus.CANCELED);
    }

    public void run() {
        setStatus(TaskStatus.PROCESSING);
        for (int i = 0; i < peakLists.length; i++) {
            totalRows += peakLists[i].getNumberRows() * 2;
        }
        this.alignedPeakList = peakLists[0].clone();
        this.alignedPeakList.setDatasetName("Aligned Dataset");
        for (Dataset dataset : this.peakLists) {
            if (dataset != peakLists[0]) {
                for (String experimentName : dataset.getAllColumnNames()) {
                    this.alignedPeakList.addColumnName(experimentName);
                    for (String parameterName : dataset.getParametersName()) {
                        alignedPeakList.addParameterValue(experimentName, parameterName, dataset.getParametersValue(experimentName, parameterName));
                    }
                }
            }
        }
        for (Dataset peakList : peakLists) {
            TreeSet<RowVsRowScore> scoreSet = new TreeSet<RowVsRowScore>();
            PeakListRow allRows[] = peakList.getRows().toArray(new PeakListRow[0]);
            for (PeakListRow row : allRows) {
                if (getStatus() == TaskStatus.CANCELED) {
                    return;
                }
                double RT1Min = ((SimplePeakListRowGCGC) row).getRT1() - this.RT1Tolerance;
                double RT1Max = ((SimplePeakListRowGCGC) row).getRT1() + this.RT1Tolerance;
                double RT2Min = ((SimplePeakListRowGCGC) row).getRT2() - this.RT2Tolerance;
                double RT2Max = ((SimplePeakListRowGCGC) row).getRT2() + this.RT2Tolerance;
                PeakListRow candidateRows[] = ((SimpleGCGCDataset) alignedPeakList).getRowsInsideRT1AndRT2Range(new Range(RT1Min, RT1Max), new Range(RT2Min, RT2Max));
                for (PeakListRow candidate : candidateRows) {
                    try {
                        RowVsRowScore score = new RowVsRowScore(row, candidate, this.RT1Tolerance, 10, this.RT2Tolerance, 10);
                        scoreSet.add(score);
                    } catch (Exception ex) {
                        Logger.getLogger(BasicAlignerGCGCTask.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                processedRows++;
            }
            Hashtable<PeakListRow, PeakListRow> alignmentMapping = new Hashtable<PeakListRow, PeakListRow>();
            Iterator<RowVsRowScore> scoreIterator = scoreSet.iterator();
            while (scoreIterator.hasNext()) {
                RowVsRowScore score = scoreIterator.next();
                if (alignmentMapping.containsKey(score.getPeakListRow())) {
                    continue;
                }
                if (alignmentMapping.containsValue(score.getAlignedRow())) {
                    continue;
                }
                alignmentMapping.put(score.getPeakListRow(), score.getAlignedRow());
            }
            for (PeakListRow row : peakList.getRows()) {
                PeakListRow targetRow = alignmentMapping.get(row);
                if (targetRow == null) {
                    alignedPeakList.addRow(row.clone());
                } else {
                    for (String file : peakList.getAllColumnNames()) {
                        targetRow.setPeak(file, (Double) row.getPeak(file));
                    }
                }
                progress = (double) processedRows++ / (double) totalRows;
            }
        }
        GuineuCore.getDesktop().AddNewFile(alignedPeakList);
        setStatus(TaskStatus.FINISHED);
    }

    public Object[] getCreatedObjects() {
        return new Object[] { alignedPeakList };
    }
}

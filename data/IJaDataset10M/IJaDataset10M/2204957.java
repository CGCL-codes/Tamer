package guineu.modules.filter.Alignment.RANSAC;

import guineu.data.Dataset;
import guineu.data.DatasetType;
import guineu.data.LCMSColumnName;
import guineu.data.PeakListRow;
import guineu.data.impl.datasets.SimpleLCMSDataset;
import guineu.data.impl.peaklists.SimplePeakListRowLCMS;
import guineu.main.GuineuCore;
import guineu.parameters.parametersType.MZTolerance;
import guineu.parameters.parametersType.RTTolerance;
import guineu.taskcontrol.AbstractTask;
import guineu.taskcontrol.TaskStatus;
import guineu.util.Range;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;
import org.apache.commons.math.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math.optimization.fitting.PolynomialFitter;
import org.apache.commons.math.optimization.general.GaussNewtonOptimizer;
import org.apache.commons.math.stat.regression.SimpleRegression;

public class RansacAlignerTask extends AbstractTask {

    private Dataset peakLists[], alignedPeakList;

    private int processedRows, totalRows;

    private String peakListName;

    private MZTolerance mzTolerance;

    private RTTolerance rtTolerance;

    private RTTolerance rtToleranceAfterRTcorrection;

    private RansacAlignerParameters parameters;

    private double progress;

    private RANSAC ransac;

    public RansacAlignerTask(Dataset[] peakLists, RansacAlignerParameters parameters) {
        this.peakLists = peakLists;
        this.parameters = parameters;
        peakListName = parameters.getParameter(RansacAlignerParameters.peakListName).getValue();
        mzTolerance = parameters.getParameter(RansacAlignerParameters.MZTolerance).getValue();
        rtTolerance = parameters.getParameter(RansacAlignerParameters.RTTolerance).getValue();
        rtToleranceAfterRTcorrection = parameters.getParameter(RansacAlignerParameters.RTToleranceValueAbs).getValue();
    }

    public String getTaskDescription() {
        return "Ransac aligner, " + peakListName + " (" + peakLists.length + " peak lists)";
    }

    public double getFinishedPercentage() {
        if (totalRows == 0) {
            return 0f;
        }
        if (ransac != null) {
            return ransac.getProgress();
        }
        return progress;
    }

    public void cancel() {
        setStatus(TaskStatus.CANCELED);
    }

    /**
         * @see Runnable#run()
         */
    public void run() {
        setStatus(TaskStatus.PROCESSING);
        for (int i = 0; i < peakLists.length; i++) {
            totalRows += peakLists[i].getNumberRows() * 2;
        }
        this.alignedPeakList = peakLists[0].clone();
        this.alignedPeakList.setDatasetName(this.peakListName);
        this.alignedPeakList.setType(peakLists[0].getType());
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
            if (peakList != peakLists[0]) {
                Hashtable<PeakListRow, PeakListRow> alignmentMapping = this.getAlignmentMap(peakList);
                for (PeakListRow row : peakList.getRows()) {
                    PeakListRow targetRow = alignmentMapping.get(row);
                    if (targetRow == null) {
                        alignedPeakList.addRow(row.clone());
                    } else {
                        setColumns(row, targetRow);
                        for (String file : peakList.getAllColumnNames()) {
                            targetRow.setPeak(file, (Double) row.getPeak(file));
                        }
                    }
                    progress = (double) processedRows++ / (double) totalRows;
                }
            }
        }
        for (PeakListRow row : alignedPeakList.getRows()) {
            int alignmentNumber = (Integer) row.getVar(LCMSColumnName.ALIGNMENT.getGetFunctionName());
            if (alignmentNumber == -1) {
                row.setVar(LCMSColumnName.ALIGNMENT.getSetFunctionName(), 1);
            }
        }
        GuineuCore.getDesktop().AddNewFile(alignedPeakList);
        setStatus(TaskStatus.FINISHED);
    }

    /**
         * Updates the value of some columns with the values of every data set combined.
         *
         * @param row Source row
         * @param targetRow Combined row
         */
    private void setColumns(PeakListRow row, PeakListRow targetRow) {
        int alignmentNumber = (Integer) targetRow.getVar(LCMSColumnName.ALIGNMENT.getGetFunctionName());
        if (alignmentNumber == -1) {
            alignmentNumber = 1;
        }
        targetRow.setVar(LCMSColumnName.ALIGNMENT.getSetFunctionName(), ++alignmentNumber);
        double numberFound = (Double) targetRow.getVar(LCMSColumnName.NFOUND.getGetFunctionName());
        double numberFound2 = (Double) row.getVar(LCMSColumnName.NFOUND.getGetFunctionName());
        targetRow.setVar(LCMSColumnName.NFOUND.getSetFunctionName(), numberFound + numberFound2);
        String name = (String) targetRow.getVar(LCMSColumnName.ALLNAMES.getGetFunctionName());
        String name2 = (String) row.getVar(LCMSColumnName.NAME.getGetFunctionName());
        String allNames = "";
        if (name != null && name.isEmpty()) {
            allNames = name2;
        } else {
            allNames = name + " // " + name2;
        }
        targetRow.setVar(LCMSColumnName.ALLNAMES.getSetFunctionName(), allNames);
    }

    /**
         *
         * @param peakList
         * @return
         */
    private Hashtable<PeakListRow, PeakListRow> getAlignmentMap(Dataset peakList) {
        Hashtable<PeakListRow, PeakListRow> alignmentMapping = new Hashtable<PeakListRow, PeakListRow>();
        if (alignedPeakList.getNumberRows() < 1) {
            return alignmentMapping;
        }
        TreeSet<RowVsRowScore> scoreSet = new TreeSet<RowVsRowScore>();
        Vector<AlignStructMol> list = ransacPeakLists(alignedPeakList, peakList);
        PolynomialFunction function = this.getPolynomialFunction(list, ((SimpleLCMSDataset) alignedPeakList).getRowsRTRange());
        PeakListRow allRows[] = peakList.getRows().toArray(new PeakListRow[0]);
        for (PeakListRow row : allRows) {
            double rt = function.value(((SimplePeakListRowLCMS) row).getRT());
            if (Double.isNaN(rt) || rt == -1) {
                rt = ((SimplePeakListRowLCMS) row).getRT();
            }
            Range mzRange = this.mzTolerance.getToleranceRange(((SimplePeakListRowLCMS) row).getMZ());
            Range rtRange = this.rtToleranceAfterRTcorrection.getToleranceRange(rt);
            PeakListRow candidateRows[] = ((SimpleLCMSDataset) alignedPeakList).getRowsInsideRTAndMZRange(rtRange, mzRange);
            for (PeakListRow candidate : candidateRows) {
                RowVsRowScore score;
                try {
                    score = new RowVsRowScore(row, candidate, mzTolerance.getTolerance(), rtToleranceAfterRTcorrection.getTolerance(), rt);
                    scoreSet.add(score);
                    errorMessage = score.getErrorMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                    setStatus(TaskStatus.ERROR);
                    return null;
                }
            }
            progress = (double) processedRows++ / (double) totalRows;
        }
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
        return alignmentMapping;
    }

    /**
         * RANSAC
         *
         * @param alignedPeakList
         * @param peakList
         * @return
         */
    private Vector<AlignStructMol> ransacPeakLists(Dataset alignedPeakList, Dataset peakList) {
        Vector<AlignStructMol> list = this.getVectorAlignment(alignedPeakList, peakList);
        ransac = new RANSAC(parameters);
        ransac.alignment(list);
        return list;
    }

    /**
         * Returns the corrected RT of the row
         *
         * @param row
         * @param list
         * @return
         */
    private PolynomialFunction getPolynomialFunction(Vector<AlignStructMol> list, Range RTrange) {
        List<RTs> data = new ArrayList<RTs>();
        for (AlignStructMol m : list) {
            if (m.Aligned) {
                data.add(new RTs(m.RT2, m.RT));
            }
        }
        data = this.smooth(data, RTrange);
        Collections.sort(data, new RTs());
        try {
            PolynomialFitter fitter = new PolynomialFitter(3, new GaussNewtonOptimizer(true));
            for (RTs rt : data) {
                fitter.addObservedPoint(1, rt.RT, rt.RT2);
            }
            return fitter.fit();
        } catch (Exception ex) {
            return null;
        }
    }

    private List<RTs> smooth(List<RTs> list, Range RTrange) {
        try {
            Collections.sort(list, new RTs());
            RTs firstPoint = list.get(0);
            RTs lastPoint = list.get(list.size() - 1);
            double min = Math.abs(firstPoint.RT - RTrange.getMin());
            double RTx = firstPoint.RT - min;
            double RTy = firstPoint.RT2 - min;
            RTs newPoint = new RTs(RTx, RTy);
            list.add(newPoint);
            double max = Math.abs(RTrange.getMin() - lastPoint.RT);
            RTx = lastPoint.RT + max;
            RTy = lastPoint.RT2 + max;
            newPoint = new RTs(RTx, RTy);
            list.add(newPoint);
        } catch (Exception exception) {
        }
        Collections.sort(list, new RTs());
        for (int i = 0; i < list.size() - 1; i++) {
            RTs point1 = list.get(i);
            RTs point2 = list.get(i + 1);
            if (point1.RT < point2.RT - 2) {
                SimpleRegression regression = new SimpleRegression();
                regression.addData(point1.RT, point1.RT2);
                regression.addData(point2.RT, point2.RT2);
                double rt = point1.RT + 1;
                while (rt < point2.RT) {
                    RTs newPoint = new RTs(rt, regression.predict(rt));
                    list.add(newPoint);
                    rt++;
                }
            }
        }
        return list;
    }

    /**
         * Create the vector which contains all the possible aligned peaks.
         *
         * @param peakListX
         * @param peakListY
         * @return vector which contains all the possible aligned peaks.
         */
    private Vector<AlignStructMol> getVectorAlignment(Dataset peakListX, Dataset peakListY) {
        Vector<AlignStructMol> alignMol = new Vector<AlignStructMol>();
        for (PeakListRow row : peakListX.getRows()) {
            if (getStatus() == TaskStatus.CANCELED) {
                return null;
            }
            Range mzRange = this.mzTolerance.getToleranceRange(((SimplePeakListRowLCMS) row).getMZ());
            Range rtRange = this.rtTolerance.getToleranceRange(((SimplePeakListRowLCMS) row).getRT());
            PeakListRow candidateRows[] = ((SimpleLCMSDataset) peakListY).getRowsInsideRTAndMZRange(rtRange, mzRange);
            for (PeakListRow candidateRow : candidateRows) {
                alignMol.addElement(new AlignStructMol((SimplePeakListRowLCMS) row, (SimplePeakListRowLCMS) candidateRow));
            }
        }
        return alignMol;
    }
}

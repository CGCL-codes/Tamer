package org.unitmetrics.ui.views;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.resources.IResource;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.unitmetrics.IMetric;
import org.unitmetrics.IResult;
import org.unitmetrics.IResultChangeEvent;
import org.unitmetrics.IUnit;
import org.unitmetrics.util.Arrays;

/** 
 * Reports changes to the result store. This view might get handy for debugging
 * sessions.
 * @author Martin Kersten 
 */
public class ReportingView extends AbstractMetricViewPart {

    Text text;

    ReportingModel model = new ReportingModel();

    public void createPart(Composite parent) {
        text = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        text.setText("");
    }

    protected void resultChanged(IResultChangeEvent changeEvent) {
        if (changeEvent.getNewResult() != null || changeEvent.getOldResult() != null) {
            model.addChange(changeEvent);
            refreshView();
        }
    }

    private void refreshView() {
        StringBuffer report = new StringBuffer();
        for (ResultChange change : model.getResultChanges()) {
            IUnit unit = change.getUnit();
            IResource resource = unit.getResource();
            report.append("Resource: " + resource.getName() + "\n");
            report.append("Unit: " + unit.getIdentifier() + "\n");
            report.append("Metric: " + change.getMetric() + "\n");
            report.append("New result: " + describeResult(change.newResult) + "\n");
            report.append("Old result: " + describeResult(change.oldResult) + "\n");
            report.append("\n");
        }
        setText(report.toString());
    }

    private String describeResult(IResult newResult) {
        if (newResult != null) {
            return "" + newResult.getValue();
        } else return "null";
    }

    private void setText(final String text) {
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                ReportingView.this.text.setText(text);
            }
        });
    }

    public void setFocus() {
        text.setFocus();
    }

    private static class ReportingModel {

        private static final int MAX_COUNT = 20;

        private List<ResultChange> resultChanges = new ArrayList<ResultChange>();

        private int nextSequenceId = 0;

        public synchronized void addChange(IResultChangeEvent changeEvent) {
            resultChanges.add(0, new ResultChange(nextSequenceId++, changeEvent.getUnit(), changeEvent.getMetric(), changeEvent.getNewResult(), changeEvent.getOldResult()));
            if (resultChanges.size() > MAX_COUNT) resultChanges.remove(resultChanges.size() - 1);
        }

        public synchronized ResultChange[] getResultChanges() {
            return Arrays.toArray(resultChanges, ResultChange.class);
        }
    }

    private static class ResultChange {

        private final int sequenceId;

        private final IUnit unit;

        private final IMetric metric;

        private final IResult newResult;

        private final IResult oldResult;

        public ResultChange(int sequenceId, IUnit unit, IMetric metric, IResult newResult, IResult oldResult) {
            this.sequenceId = sequenceId;
            this.unit = unit;
            this.metric = metric;
            this.newResult = newResult;
            this.oldResult = oldResult;
        }

        public int getSequenceId() {
            return sequenceId;
        }

        public IUnit getUnit() {
            return unit;
        }

        public IMetric getMetric() {
            return metric;
        }

        public IResult getNewResult() {
            return newResult;
        }

        public IResult getOldResult() {
            return oldResult;
        }
    }
}
